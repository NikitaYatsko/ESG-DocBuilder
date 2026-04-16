package esg.esgdocbuilder.service.impl;

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
import esg.esgdocbuilder.model.entity.Product;
import esg.esgdocbuilder.service.InvoiceService;
import esg.esgdocbuilder.service.PdfService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
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

    private static final Color HEADER_BG_COLOR = new DeviceRgb(207, 149, 44);
    private static final Color ROW_BG_COLOR_1 = ColorConstants.WHITE;
    private static final DeviceRgb ROW_BG_COLOR_2 = new DeviceRgb(255, 249, 196);
    private static final Color BORDER_COLOR = ColorConstants.LIGHT_GRAY;
    private static final Color CONTACT_BG_COLOR = new DeviceRgb(250, 250, 250);
    private static final Color CONTACT_BORDER_COLOR = new DeviceRgb(200, 200, 200);
    private static final Color TOTAL_BG_COLOR = new DeviceRgb(255, 249, 196);


    @Override
    public byte[] generateProductsPdf(List<Product> products) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            
            PdfFont font = getPdfFont();
            document.setFont(font);


            document.add(new Paragraph("Список продуктов")
                    .setFontSize(16).setBold().setMarginBottom(10));


            float[] columnWidths = {15, 205, 65, 65, 60, 60};
            Table table = new Table(columnWidths);
            table.setWidth(UnitValue.createPercentValue(100));

            String[] headers = {"ID", "Название", "Цена Покупки", "Цена Продажи", "Категория", "Маржинальность"};
            for (String header : headers) {
                Cell cell = new Cell()
                        .add(new Paragraph(header))
                        .setBackgroundColor(HEADER_BG_COLOR)
                        .setFontColor(ColorConstants.WHITE)
                        .setFontSize(10)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE)
                        .setPadding(1)
                        .setBorder(new SolidBorder(BORDER_COLOR, 1));
                table.addHeaderCell(cell);
            }

            boolean alternate = false;
            for (Product product : products) {
                Color rowColor = alternate ? ROW_BG_COLOR_2 : ROW_BG_COLOR_1;
                alternate = !alternate;

                table.addCell(createDataCell(font, String.valueOf(product.getId()), rowColor, new SolidBorder(BORDER_COLOR, 1), 8, TextAlignment.CENTER));
                table.addCell(createDataCell(font, product.getName(), rowColor, new SolidBorder(BORDER_COLOR, 1), 8, TextAlignment.LEFT));
                table.addCell(createDataCell(font, product.getCostPrice() + "", rowColor, new SolidBorder(BORDER_COLOR, 1), 8, TextAlignment.RIGHT));
                table.addCell(createDataCell(font, product.getSellPrice() + "", rowColor, new SolidBorder(BORDER_COLOR, 1), 8, TextAlignment.RIGHT));
                table.addCell(createDataCell(font, product.getCategory().getName(), rowColor, new SolidBorder(BORDER_COLOR, 1), 8, TextAlignment.RIGHT));
                table.addCell(createDataCell(font, product.getMarginality() + "", rowColor, new SolidBorder(BORDER_COLOR, 1), 8, TextAlignment.RIGHT));
            }

            document.add(table);
            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Ошибка при создании PDF продуктов", e);
        }
    }

    @Override
    public ByteArrayOutputStream generateInvoicePdf(Long invoiceId) {
        InvoiceResponse invoice = invoiceService.getInvoiceById(invoiceId);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);
            document.setMargins(5, 36, 36, 36);

            PdfFont font = getPdfFont();

            document.add(createHeaderTable(font, invoice));

            if (invoice.getPower() != null) {
                Paragraph powerKwt = new Paragraph("Мощность станции " + invoice.getPower() + " кВт")
                        .setFont(font).setFontSize(12).setTextAlignment(TextAlignment.CENTER).setMarginBottom(5);
                document.add(powerKwt);
            }

            Paragraph title = new Paragraph("Смета")
                    .setFont(font).setFontSize(16).setTextAlignment(TextAlignment.CENTER).setMarginBottom(1);
            document.add(title);

            List<InvoiceItemResponse> items = invoice.getItems();
            if (!items.isEmpty()) {
                Table table = createClientTable(font);
                boolean isEvenRow = false;
                for (InvoiceItemResponse item : items) {
                    addClientTableRow(table, font, item, isEvenRow);
                    isEvenRow = !isEvenRow;
                }
                document.add(table);
            }

            BigDecimal totalVat = invoice.getVat_amount() != null ? invoice.getVat_amount() : BigDecimal.ZERO;
            BigDecimal totalAmount = invoice.getSum() != null ? invoice.getSum() : BigDecimal.ZERO;

            Table totalTable = new Table(UnitValue.createPercentArray(new float[]{45, 55}));
            totalTable.setWidth(UnitValue.createPercentValue(100));
            totalTable.setMarginTop(5);

            addTotalCell(totalTable, font, "Итого НДС: " + totalVat + " MDL");
            addTotalCell(totalTable, font, "Итого (СЭС): " + totalAmount + " MDL");

            document.add(totalTable);
            document.close();
            log.info("PDF для сметы {} успешно сгенерирован", invoice.getInvoiceName());

        } catch (Exception e) {
            log.error("Ошибка генерации PDF для сметы с ID {}", invoiceId, e);
            throw new RuntimeException("Ошибка генерации PDF", e);
        }
        return outputStream;
    }

    public ByteArrayOutputStream generateInternalInvoicePdf(Long invoiceId) {
        InvoiceResponse invoice = invoiceService.getInvoiceById(invoiceId);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);
            document.setMargins(5, 36, 36, 36);

            PdfFont font = getPdfFont();

            document.add(createHeaderTable(font, invoice));

            if (invoice.getPower() != null) {
                Paragraph powerKwt = new Paragraph("Мощность станции " + invoice.getPower() + " кВт")
                        .setFont(font).setFontSize(12).setTextAlignment(TextAlignment.CENTER).setMarginBottom(5);
                document.add(powerKwt);
            }

            Paragraph title = new Paragraph("Внутренняя смета")
                    .setFont(font).setFontSize(16).setTextAlignment(TextAlignment.CENTER).setMarginBottom(1);
            document.add(title);

            List<InvoiceItemResponse> items = invoice.getItems();
            if (!items.isEmpty()) {
                Table table = createInternalTable(font);
                boolean isEvenRow = false;
                for (InvoiceItemResponse item : items) {
                    addInternalTableRow(table, font, item, isEvenRow);
                    isEvenRow = !isEvenRow;
                }
                document.add(table);
            }

            BigDecimal totalVat = invoice.getVat_amount() != null ? invoice.getVat_amount() : BigDecimal.ZERO;
            BigDecimal totalAmount = invoice.getSum() != null ? invoice.getSum() : BigDecimal.ZERO;
            BigDecimal totalMarginality = invoice.getSumMarginality() != null ? invoice.getSumMarginality() : BigDecimal.ZERO;

            Table totalTable = new Table(UnitValue.createPercentArray(new float[]{30, 30, 40}));
            totalTable.setWidth(UnitValue.createPercentValue(100));
            totalTable.setMarginTop(5);

            addTotalCell(totalTable, font, "Итого НДС: " + totalVat + " MDL");
            addTotalCell(totalTable, font, "Итого СЭС: " + totalAmount + " MDL");
            addTotalCell(totalTable, font, "Итого Маржинальность: " + totalMarginality + " MDL");


            totalTable.setKeepTogether(true);
            document.add(totalTable);
            document.close();
            log.info("Внутренний PDF для сметы {} успешно сгенерирован", invoice.getInvoiceName());

        } catch (Exception e) {
            log.error("Ошибка генерации внутреннего PDF для сметы с ID {}", invoiceId, e);
            throw new RuntimeException("Ошибка генерации внутреннего PDF", e);
        }
        return outputStream;
    }

    private Table createHeaderTable(PdfFont font, InvoiceResponse invoice) throws Exception {
        Table headerTable = new Table(UnitValue.createPercentArray(new float[]{50, 30, 30}));
        headerTable.setWidth(UnitValue.createPercentValue(100));
        headerTable.setMarginBottom(5);

        try (InputStream logoStream = getClass().getResourceAsStream("/img/logo.png")) {
            if (logoStream == null) {
                log.warn("Логотип не найден, пропускаем");
                headerTable.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
            } else {
                Image logo = new Image(ImageDataFactory.create(logoStream.readAllBytes()));
                logo.setWidth(85);
                logo.setAutoScale(true);
                Cell logoCell = new Cell().add(logo)
                        .setBorder(Border.NO_BORDER)
                        .setPadding(0)
                        .setTextAlignment(TextAlignment.LEFT)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE);
                headerTable.addCell(logoCell);
            }
        } catch (Exception e) {
            log.warn("Логотип не найден, пропускаем", e);
            headerTable.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        }

        Cell spacerCell = new Cell().setBorder(Border.NO_BORDER).setPadding(0);
        headerTable.addCell(spacerCell);

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

        infoCell.add(new Paragraph("Дата: " + dateStr)
                .setFont(font).setFontSize(fontSize).setMargin(0));
        infoCell.add(new Paragraph(" ")
                .setFontSize(2).setMargin(0));
        infoCell.add(new Paragraph("Телефон: 067252000")
                .setFont(font).setFontSize(fontSize).setMargin(0));
        infoCell.add(new Paragraph("Телефон: 067600640")
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

        return headerTable;
    }

    private Table createClientTable(PdfFont font) {
        float[] columnWidths = {210, 60, 60, 60, 70};
        Table table = new Table(UnitValue.createPointArray(columnWidths));
        table.setWidth(UnitValue.createPercentValue(100));

        String[] headers = {"Наименование", "Кол-во", "Цена", "НДС", "Итого MDL"};
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

    private Table createInternalTable(PdfFont font) {
        float[] columnWidths = {200, 50, 60, 50, 45, 70};
        Table table = new Table(UnitValue.createPointArray(columnWidths));
        table.setWidth(UnitValue.createPercentValue(100));

        String[] headers = {"Наименование", "Кол-во", "Цена", "Маржинальность", "НДС", "Итого MDL"};
        for (String header : headers) {
            Cell cell = new Cell()
                    .add(new Paragraph(header).setFont(font).setFontSize(9))
                    .setBackgroundColor(HEADER_BG_COLOR)
                    .setFontColor(ColorConstants.WHITE)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setPadding(2)
                    .setBorder(new SolidBorder(BORDER_COLOR, 0.5f));
            table.addCell(cell);
        }
        return table;
    }

    private void addClientTableRow(Table table, PdfFont font, InvoiceItemResponse item, boolean isEvenRow) {
        Color bgColor = isEvenRow ? ROW_BG_COLOR_2 : ROW_BG_COLOR_1;
        Border border = new SolidBorder(BORDER_COLOR, 0.5f);

        table.addCell(createDataCell(font, item.getNameProduct(), bgColor, border, 7, TextAlignment.LEFT));
        table.addCell(createDataCell(font, item.getQuantity().toString(), bgColor, border, 8, TextAlignment.RIGHT));
        table.addCell(createDataCell(font, item.getUnitPrice().toString(), bgColor, border, 8, TextAlignment.RIGHT));
        table.addCell(createDataCell(font, item.getVatMultiplier().toString(), bgColor, border, 8, TextAlignment.RIGHT));
        table.addCell(createDataCell(font, item.getTotalPrice().toString(), bgColor, border, 8, TextAlignment.RIGHT));
    }

    private void addInternalTableRow(Table table, PdfFont font, InvoiceItemResponse item, boolean isEvenRow) {
        Color bgColor = isEvenRow ? ROW_BG_COLOR_2 : ROW_BG_COLOR_1;
        Border border = new SolidBorder(BORDER_COLOR, 0.5f);

        table.addCell(createDataCell(font, item.getNameProduct(), bgColor, border, 8, TextAlignment.LEFT));
        table.addCell(createDataCell(font, item.getQuantity().toString(), bgColor, border, 8, TextAlignment.RIGHT));
        table.addCell(createDataCell(font, item.getUnitPrice().toString(), bgColor, border, 8, TextAlignment.RIGHT));
        table.addCell(createDataCell(font, item.getMarginality() != null ? item.getMarginality().toString() : "0", bgColor, border, 8, TextAlignment.RIGHT));
        table.addCell(createDataCell(font, item.getVatMultiplier().toString(), bgColor, border, 8, TextAlignment.RIGHT));
        table.addCell(createDataCell(font, item.getTotalPrice().toString(), bgColor, border, 8, TextAlignment.RIGHT));
    }



            private PdfFont getPdfFont() {
                try (InputStream is = getClass().getResourceAsStream("/fonts/FreeSans.ttf")) {
                    if (is == null) {
                        log.error("Критическая ошибка: Файл шрифта FreeSans.ttf не найден в classpath по пути '/fonts/FreeSans.ttf'");
                        throw new RuntimeException("Шрифт для PDF не найден. Пожалуйста, проверьте наличие файла в resources.");
                    }
                    byte[] fontBytes = IOUtils.toByteArray(is);
                    return PdfFontFactory.createFont(fontBytes, PdfEncodings.IDENTITY_H);
                } catch (Exception e) {
                    log.error("Ошибка загрузки или создания шрифта из resources", e);
                    throw new RuntimeException("Не удалось загрузить шрифт для генерации PDF", e);
                }
            }



    private Cell createDataCell(PdfFont font, String text, Color bgColor, Border border, int fontSize, TextAlignment align) {
        return new Cell()
                .add(new Paragraph(text).setFont(font).setFontSize(fontSize))
                .setBackgroundColor(bgColor)
                .setTextAlignment(align)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setBorder(border)
                .setPadding(2);
    }

    private void addTotalCell(Table table, PdfFont font, String text) {
        Paragraph paragraph = new Paragraph(text).setFont(font).setFontSize(8).setTextAlignment(TextAlignment.RIGHT).setMargin(0);
        Cell cell = new Cell().add(paragraph)
                .setBackgroundColor(TOTAL_BG_COLOR)
                .setBorder(new SolidBorder(BORDER_COLOR, 0.5f))
                .setPadding(3)
                .setTextAlignment(TextAlignment.RIGHT);
        table.addCell(cell);
    }
}