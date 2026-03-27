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
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import esg.esgdocbuilder.model.dto.response.InvoiceItemResponse;
import esg.esgdocbuilder.model.dto.response.InvoiceResponse;
import esg.esgdocbuilder.service.InvoiceService;
import esg.esgdocbuilder.service.sevicePdf.PdfService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
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

    // Цвета
    private static final Color HEADER_BG_COLOR = ColorConstants.DARK_GRAY;
    private static final Color ROW_BG_COLOR_1 = ColorConstants.WHITE;
    private static final DeviceRgb ROW_BG_COLOR_2 = new DeviceRgb(245, 245, 245);
    private static final Color BORDER_COLOR = ColorConstants.LIGHT_GRAY;

    @Override
    public ByteArrayOutputStream generateInvoicePdf(Long invoiceId) {
        InvoiceResponse invoice = invoiceService.getInvoiceById(invoiceId);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            PdfFont font = PdfFontFactory.createFont(
                    "fonts/FreeSans.ttf",
                    PdfEncodings.IDENTITY_H
            );

            // --- Шапка: логотип слева, контактные данные справа ---
            Table headerTable = new Table(UnitValue.createPercentArray(new float[]{40, 60}));
            headerTable.setWidth(UnitValue.createPercentValue(100));
            headerTable.setMarginBottom(15);

            // Логотип
            try {
                String logoPath = getClass().getClassLoader().getResource("img/logo.png").getPath();
                Image logo = new Image(ImageDataFactory.create(logoPath));
                logo.setWidth(100);
                logo.setAutoScale(true);
                Cell logoCell = new Cell().add(logo).setBorder(Border.NO_BORDER).setPadding(0);
                headerTable.addCell(logoCell);
            } catch (Exception e) {
                log.warn("Логотип не найден, пропускаем");
                headerTable.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
            }

            // Контактная информация (выровнена по левому краю внутри правой колонки)
            Paragraph infoRight = new Paragraph()
                    .setFont(font)
                    .setFontSize(9)
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setMargin(0);
            infoRight.add("Номер сметы: ").add(invoice.getInvoiceNumber()).add("\n");
            infoRight.add("Дата: ")
                    .add(DATE_FORMAT.format(
                            Date.from(invoice.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant())
                    ))
                    .add("\n\n");
            infoRight.add("ООО \"Solution\"\n");
            infoRight.add("+373 60 113 867\n");
            infoRight.add("info@solution.md\n");
            infoRight.add("https://solution.md/ru/home/\n");

            Cell infoCell = new Cell().add(infoRight)
                    .setBorder(Border.NO_BORDER)
                    .setPadding(0)
                    .setTextAlignment(TextAlignment.LEFT);
            headerTable.addCell(infoCell);

            document.add(headerTable);

            // Заголовок документа
            Paragraph title = new Paragraph("СМЕТА")
                    .setFont(font)
                    .setFontSize(20)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(10);
            document.add(title);

            // --- Таблица позиций ---
            List<InvoiceItemResponse> items = invoice.getItems();
            int rowsPerPage = 15;

            for (int i = 0; i < items.size(); i += rowsPerPage) {
                List<InvoiceItemResponse> pageItems =
                        items.subList(i, Math.min(i + rowsPerPage, items.size()));

                Table table = createTable(font);
                boolean isEvenRow = false;
                for (InvoiceItemResponse item : pageItems) {
                    addTableRow(table, font, item, isEvenRow);
                    isEvenRow = !isEvenRow;
                }

                document.add(table);

                if (i + rowsPerPage < items.size()) {
                    document.add(new AreaBreak()); // новая страница
                }
            }

            // Итоговая сумма (СЭС)
            Paragraph totalParagraph = new Paragraph()
                    .setFont(font)
                    .setFontSize(11)
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setMarginTop(15);
            totalParagraph.add("Итого (СЭС): ").add(invoice.getTotalAmount().toString()).add(" MDL");
            document.add(totalParagraph);

            document.close();
            log.info("PDF для сметы {} успешно сгенерирован", invoice.getInvoiceNumber());

        } catch (Exception e) {
            log.error("Ошибка генерации PDF для сметы с ID {}", invoiceId, e);
            throw new RuntimeException("Ошибка генерации PDF", e);
        }

        return outputStream;
    }

    /**
     * Создаёт таблицу с заголовками.
     */
    private Table createTable(PdfFont font) {
        float[] columnWidths = {180, 55, 75, 60, 90}; // ширина колонок в pt
        Table table = new Table(UnitValue.createPointArray(columnWidths));
        table.setWidth(UnitValue.createPercentValue(100));

        // Заголовки с цветным фоном
        String[] headers = {"Наименование", "Кол-во", "Цена, MDL", "НДС, %", "Сумма, MDL"};
        for (String header : headers) {
            Cell cell = new Cell()
                    .add(new Paragraph(header).setFont(font).setFontSize(10))
                    .setBackgroundColor(HEADER_BG_COLOR)
                    .setFontColor(ColorConstants.WHITE)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setPadding(6)
                    .setBorder(new SolidBorder(BORDER_COLOR, 0.5f));
            table.addCell(cell);
        }
        return table;
    }

    /**
     * Добавляет строку данных в таблицу с возможным чередованием фона.
     */
    private void addTableRow(Table table, PdfFont font, InvoiceItemResponse item, boolean isEvenRow) {
        Color bgColor = isEvenRow ? ROW_BG_COLOR_2 : ROW_BG_COLOR_1;
        Border border = new SolidBorder(BORDER_COLOR, 0.5f);

        // Наименование
        Cell nameCell = new Cell()
                .add(new Paragraph(item.getNameProduct()).setFont(font).setFontSize(9))
                .setBackgroundColor(bgColor)
                .setBorder(border)
                .setPadding(4);
        table.addCell(nameCell);

        // Количество
        table.addCell(createDataCell(font, item.getQuantity().toString(), bgColor, border));

        // Цена
        table.addCell(createDataCell(font, item.getUnitPrice().toString(), bgColor, border));

        // НДС (множитель переводим в проценты)
        double vatPercent = (item.getVatMultiplier().doubleValue() - 1) * 100;
        String vatStr = String.format("%.0f%%", vatPercent);
        table.addCell(createDataCell(font, vatStr, bgColor, border));

        // Сумма
        table.addCell(createDataCell(font, item.getTotalPrice().toString(), bgColor, border));
    }

    /**
     * Создаёт ячейку с данными, выровненную по правому краю (кроме наименования).
     */
    private Cell createDataCell(PdfFont font, String text, Color bgColor, Border border) {
        return new Cell()
                .add(new Paragraph(text).setFont(font).setFontSize(9))
                .setBackgroundColor(bgColor)
                .setTextAlignment(TextAlignment.RIGHT)
                .setBorder(border)
                .setPadding(4);
    }
}