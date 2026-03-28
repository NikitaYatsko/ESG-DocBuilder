package esg.esgdocbuilder.service.impl.implPdf;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import esg.esgdocbuilder.model.dto.response.InvoiceItemResponse;
import esg.esgdocbuilder.model.dto.response.InvoiceResponse;
import esg.esgdocbuilder.service.InvoiceService;
import esg.esgdocbuilder.service.sevicePdf.PdfService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PdfServiceImpl implements PdfService {

    private final InvoiceService invoiceService;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");

    // Цвета для таблицы товаров
    private static final Color HEADER_BG_COLOR = new DeviceRgb(207, 149, 44);
    private static final Color ROW_BG_COLOR_1 = ColorConstants.WHITE;
    private static final DeviceRgb ROW_BG_COLOR_2 = new DeviceRgb(255, 249, 196);
    private static final Color BORDER_COLOR = ColorConstants.LIGHT_GRAY;

    // Цвета для контактного блока
    private static final Color CONTACT_BG_COLOR = new DeviceRgb(250, 250, 250);
    private static final Color CONTACT_BORDER_COLOR = new DeviceRgb(200, 200, 200);

    // Цвет для итоговой суммы
    private static final Color TOTAL_BG_COLOR = new DeviceRgb(255, 249, 196);

    @Override
    public ByteArrayOutputStream generateInvoicePdf(Long invoiceId) {
        InvoiceResponse invoice = invoiceService.getInvoiceById(invoiceId);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);
            document.setMargins(5, 36, 36, 36);

            PdfFont font = PdfFontFactory.createFont(
                    "fonts/FreeSans.ttf",
                    PdfEncodings.IDENTITY_H
            );

            // Шапка: логотип, разделитель, контакты (пропорции 50/30/30)
            Table headerTable = new Table(UnitValue.createPercentArray(new float[]{50, 30, 30}));
            headerTable.setWidth(UnitValue.createPercentValue(100));
            headerTable.setMarginBottom(5);

            // Левая колонка: логотип
            try {
                String logoPath = getClass().getClassLoader().getResource("img/logo.png").getPath();
                Image logo = new Image(ImageDataFactory.create(logoPath));
                logo.setWidth(85);
                logo.setAutoScale(true);
                Cell logoCell = new Cell().add(logo)
                        .setBorder(Border.NO_BORDER)
                        .setPadding(0)
                        .setTextAlignment(TextAlignment.LEFT)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE);
                headerTable.addCell(logoCell);
            } catch (Exception e) {
                log.warn("Логотип не найден, пропускаем");
                headerTable.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
            }

            // Средняя колонка: невидимый разделитель
            Cell spacerCell = new Cell()
                    .setBorder(Border.NO_BORDER)
                    .setPadding(0);
            headerTable.addCell(spacerCell);

            // Правая колонка: контактный блок
            int fontSize = 9;
            String dateStr = DATE_FORMAT.format(
                    Date.from(invoice.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant())
            );

            Table infoBoxTable = new Table(1);
            infoBoxTable.setWidth(UnitValue.createPercentValue(100));

            Cell infoCell = new Cell()
                    .setBackgroundColor(CONTACT_BG_COLOR)
                    .setBorder(new SolidBorder(CONTACT_BORDER_COLOR, 0.5f))
                    .setPadding(6)
                    .setPaddingTop(4)
                    .setPaddingBottom(4)
                    .setTextAlignment(TextAlignment.LEFT);

            infoCell.add(new Paragraph("Номер сметы: " + invoice.getInvoiceNumber())
                    .setFont(font).setFontSize(fontSize).setMargin(0));
            infoCell.add(new Paragraph("Дата: " + dateStr)
                    .setFont(font).setFontSize(fontSize).setMargin(0));
            infoCell.add(new Paragraph(" ")
                    .setFontSize(2).setMargin(0));
            infoCell.add(new Paragraph("Телефон: +373 60 113 867")
                    .setFont(font).setFontSize(fontSize).setMargin(0));
            infoCell.add(new Paragraph("Емаил: info@solution.md")
                    .setFont(font).setFontSize(fontSize).setMargin(0));
            infoCell.add(new Paragraph("Сайт: www.solution.md")
                    .setFont(font).setFontSize(fontSize).setMargin(0));

            infoBoxTable.addCell(infoCell);

            Cell rightCell = new Cell().add(infoBoxTable)
                    .setBorder(Border.NO_BORDER)
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE);
            headerTable.addCell(rightCell);

            document.add(headerTable);

            // Заголовок документа
            Paragraph title = new Paragraph("Коммерческое предложение")
                    .setFont(font)
                    .setFontSize(16)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(5);
            document.add(title);

            // Таблица позиций
            List<InvoiceItemResponse> items = invoice.getItems();
            if (!items.isEmpty()) {
                Table table = createTable(font);
                boolean isEvenRow = false;
                for (InvoiceItemResponse item : items) {
                    addTableRow(table, font, item, isEvenRow);
                    isEvenRow = !isEvenRow;
                }
                document.add(table);
            }

            // Рассчитываем общую сумму НДС (просто суммируем vatMultiplier)
            BigDecimal totalVat = BigDecimal.ZERO;
            for (InvoiceItemResponse item : items) {
                totalVat = totalVat.add(item.getVatMultiplier());
            }

            // Итоговая таблица: Итого НДС и Итого (СЭС) в одной строке
            Table totalTable = new Table(UnitValue.createPercentArray(new float[]{50, 50}));
            totalTable.setWidth(UnitValue.createPercentValue(100));
            totalTable.setMarginTop(5);

            // Ячейка "Итого НДС"
            Paragraph vatParagraph = new Paragraph()
                    .setFont(font)
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setMargin(0);
            vatParagraph.add("Итого НДС: ").add(totalVat.toString()).add(" MDL");
            Cell vatCell = new Cell()
                    .add(vatParagraph)
                    .setBackgroundColor(TOTAL_BG_COLOR)
                    .setBorder(new SolidBorder(BORDER_COLOR, 0.5f))
                    .setPadding(4)
                    .setTextAlignment(TextAlignment.RIGHT);
            totalTable.addCell(vatCell);

            // Ячейка "Итого (СЭС)"
            Paragraph totalParagraph = new Paragraph()
                    .setFont(font)
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setMargin(0);
            totalParagraph.add("Итого (СЭС): ").add(invoice.getTotalAmount().toString()).add(" MDL");
            Cell totalCell = new Cell()
                    .add(totalParagraph)
                    .setBackgroundColor(TOTAL_BG_COLOR)
                    .setBorder(new SolidBorder(BORDER_COLOR, 0.5f))
                    .setPadding(4)
                    .setTextAlignment(TextAlignment.RIGHT);
            totalTable.addCell(totalCell);

            document.add(totalTable);

            document.close();
            log.info("PDF для сметы {} успешно сгенерирован", invoice.getInvoiceNumber());

        } catch (Exception e) {
            log.error("Ошибка генерации PDF для сметы с ID {}", invoiceId, e);
            throw new RuntimeException("Ошибка генерации PDF", e);
        }

        return outputStream;
    }

    private Table createTable(PdfFont font) {
        float[] columnWidths = {210, 60, 60, 60, 70};
        Table table = new Table(UnitValue.createPointArray(columnWidths));
        table.setWidth(UnitValue.createPercentValue(100));

        String[] headers = {"Наименование", "Кол-во", "Цена", "НДС", "Сумма"};
        for (String header : headers) {
            Cell cell = new Cell()
                    .add(new Paragraph(header).setFont(font).setFontSize(9))
                    .setBackgroundColor(HEADER_BG_COLOR)
                    .setFontColor(ColorConstants.WHITE)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setPadding(2)
                    .setBorder(new SolidBorder(BORDER_COLOR, 0.5f));
            table.addCell(cell);
        }
        return table;
    }

    private void addTableRow(Table table, PdfFont font, InvoiceItemResponse item, boolean isEvenRow) {
        Color bgColor = isEvenRow ? ROW_BG_COLOR_2 : ROW_BG_COLOR_1;
        Border border = new SolidBorder(BORDER_COLOR, 0.5f);

        Cell nameCell = new Cell()
                .add(new Paragraph(item.getNameProduct()).setFont(font).setFontSize(8))
                .setBackgroundColor(bgColor)
                .setBorder(border)
                .setPadding(2);
        table.addCell(nameCell);

        table.addCell(createDataCell(font, item.getQuantity().toString(), bgColor, border, 8));
        table.addCell(createDataCell(font, item.getUnitPrice().toString(), bgColor, border, 8));
        BigDecimal vatMultiplier = item.getVatMultiplier();
        String vatStr = String.format("%.2f", vatMultiplier);
        table.addCell(createDataCell(font, vatStr, bgColor, border, 8));
        table.addCell(createDataCell(font, item.getTotalPrice().toString(), bgColor, border, 8));
    }

    private Cell createDataCell(PdfFont font, String text, Color bgColor, Border border, int fontSize) {
        return new Cell()
                .add(new Paragraph(text).setFont(font).setFontSize(fontSize))
                .setBackgroundColor(bgColor)
                .setTextAlignment(TextAlignment.RIGHT)
                .setBorder(border)
                .setPadding(2);
    }
}