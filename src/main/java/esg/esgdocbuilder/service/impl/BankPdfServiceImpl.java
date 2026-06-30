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
import esg.esgdocbuilder.model.dto.response.BankOperationResponse;
import esg.esgdocbuilder.service.BankOperationService;
import esg.esgdocbuilder.service.BankPdfService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BankPdfServiceImpl implements BankPdfService {

    private final BankOperationService bankOperationService;

    private static final Color HEADER_BG_COLOR = new DeviceRgb(207, 149, 44);
    private static final Color ROW_BG_COLOR_1 = ColorConstants.WHITE;
    private static final Color ROW_BG_COLOR_2 = new DeviceRgb(240, 240, 240);
    private static final Color BORDER_COLOR = ColorConstants.LIGHT_GRAY;
    private static final Color CONTACT_BG_COLOR = new DeviceRgb(250, 250, 250);
    private static final Color CONTACT_BORDER_COLOR = new DeviceRgb(200, 200, 200);
    private static final Color RED = new DeviceRgb(200, 0, 0);
    private static final Color GREEN = new DeviceRgb(0, 150, 0);

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,##0.00");

    @Override
    public ByteArrayOutputStream generateBankOperationsPdf(LocalDate from, LocalDate to) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);
            document.setMargins(5, 36, 36, 36);

            PdfFont font = getPdfFont();

            document.add(createHeaderTable(font));

            Paragraph title = new Paragraph("Отчёт по банковским операциям")
                    .setFont(font).setFontSize(15).setTextAlignment(TextAlignment.CENTER).setMarginBottom(5);
            document.add(title);

            String periodText = (from != null ? from.toString() : "с начала") +
                    " - " +
                    (to != null ? to.toString() : "по настоящее время");
            Paragraph period = new Paragraph("Период: " + periodText)
                    .setFont(font).setFontSize(15).setTextAlignment(TextAlignment.CENTER).setMarginBottom(10);
            document.add(period);

            List<BankOperationResponse> operations = bankOperationService.getOperationsForPeriod(from, to);

            // Разделяем и группируем
            Map<String, BigDecimal> expensesMap = operations.stream()
                    .filter(op -> op.getType().name().equals("EXPENSE"))
                    .collect(Collectors.groupingBy(
                            BankOperationResponse::getCategory,
                            Collectors.mapping(BankOperationResponse::getAmount, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                    ));

            Map<String, BigDecimal> incomesMap = operations.stream()
                    .filter(op -> op.getType().name().equals("INCOME"))
                    .collect(Collectors.groupingBy(
                            BankOperationResponse::getCategory,
                            Collectors.mapping(BankOperationResponse::getAmount, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                    ));

            // Вычисляем итоговые суммы
            BigDecimal totalExpense = expensesMap.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal totalIncome = incomesMap.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal balance = totalIncome.subtract(totalExpense);

            // Основная таблица с двумя колонками
            Table mainTable = new Table(UnitValue.createPercentArray(new float[]{50, 50}));
            mainTable.setWidth(UnitValue.createPercentValue(100));
            mainTable.setMarginTop(5);

            // ---- Левая таблица расходов ----
            Table expenseTable = createExpenseTable(font);
            if (!expensesMap.isEmpty()) {
                boolean isEvenRow = false;
                for (Map.Entry<String, BigDecimal> entry : expensesMap.entrySet()) {
                    String category = entry.getKey();
                    BigDecimal amount = entry.getValue();
                    addExpenseRow(expenseTable, font, category, amount, isEvenRow);
                    isEvenRow = !isEvenRow;
                }
                // Итоговая строка расходов
                Cell expenseTotalCell = new Cell(1, 2)
                        .add(new Paragraph("Итого расходов: " + DECIMAL_FORMAT.format(totalExpense) + " MDL")
                                .setFont(font).setFontSize(10).setBold()
                                .setTextAlignment(TextAlignment.RIGHT))
                        .setBorder(new SolidBorder(BORDER_COLOR, 0.5f))
                        .setPadding(4)
                        .setBackgroundColor(ROW_BG_COLOR_1)
                        .setTextAlignment(TextAlignment.RIGHT);
                expenseTable.addCell(expenseTotalCell);
            } else {
                Cell emptyCell = new Cell(1, 2)
                        .add(new Paragraph("Нет расходов").setFont(font).setFontSize(10).setTextAlignment(TextAlignment.CENTER))
                        .setBorder(new SolidBorder(BORDER_COLOR, 0.5f))
                        .setPadding(4);
                expenseTable.addCell(emptyCell);
            }

            // ---- Правая таблица доходов ----
            Table incomeTable = createIncomeTable(font);
            if (!incomesMap.isEmpty()) {
                boolean isEvenRow = false;
                for (Map.Entry<String, BigDecimal> entry : incomesMap.entrySet()) {
                    String category = entry.getKey();
                    BigDecimal amount = entry.getValue();
                    addIncomeRow(incomeTable, font, category, amount, isEvenRow);
                    isEvenRow = !isEvenRow;
                }
                // Итоговая строка доходов
                Cell incomeTotalCell = new Cell(1, 2)
                        .add(new Paragraph("Итого доходов: " + DECIMAL_FORMAT.format(totalIncome) + " MDL")
                                .setFont(font).setFontSize(10).setBold()
                                .setTextAlignment(TextAlignment.RIGHT))
                        .setBorder(new SolidBorder(BORDER_COLOR, 0.5f))
                        .setPadding(4)
                        .setBackgroundColor(ROW_BG_COLOR_1)
                        .setTextAlignment(TextAlignment.RIGHT);
                incomeTable.addCell(incomeTotalCell);
            } else {
                Cell emptyCell = new Cell(1, 2)
                        .add(new Paragraph("Нет доходов").setFont(font).setFontSize(10).setTextAlignment(TextAlignment.CENTER))
                        .setBorder(new SolidBorder(BORDER_COLOR, 0.5f))
                        .setPadding(4);
                incomeTable.addCell(emptyCell);
            }

            // Вставляем таблицы в основную
            Cell leftCell = new Cell().add(expenseTable).setBorder(Border.NO_BORDER).setPadding(2);
            Cell rightCell = new Cell().add(incomeTable).setBorder(Border.NO_BORDER).setPadding(2);
            mainTable.addCell(leftCell);
            mainTable.addCell(rightCell);

            document.add(mainTable);

            // Баланс
            Paragraph balanceParagraph = new Paragraph("Баланс: " + DECIMAL_FORMAT.format(balance) + " MDL")
                    .setFont(font).setFontSize(12).setBold()
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setMarginTop(10);
            document.add(balanceParagraph);

            document.close();
            log.info("PDF-отчёт по банковским операциям успешно сгенерирован");

        } catch (Exception e) {
            log.error("Ошибка генерации PDF-отчёта по банковским операциям", e);
            throw new RuntimeException("Ошибка генерации PDF-отчёта по банковским операциям", e);
        }

        return outputStream;
    }

    private Table createExpenseTable(PdfFont font) {
        float[] columnWidths = {120, 80}; // 2 колонки: Категория, Сумма
        Table table = new Table(UnitValue.createPointArray(columnWidths));
        table.setWidth(UnitValue.createPercentValue(100));

        String[] headers = {"Категория", "Сумма (MDL)"};
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

    private void addExpenseRow(Table table, PdfFont font, String category, BigDecimal amount, boolean isEvenRow) {
        Color bgColor = isEvenRow ? ROW_BG_COLOR_2 : ROW_BG_COLOR_1;
        Border border = new SolidBorder(BORDER_COLOR, 0.5f);

        table.addCell(createDataCell(font, category, bgColor, border, 8, TextAlignment.CENTER));
        String amountStr = "- " + DECIMAL_FORMAT.format(amount);
        table.addCell(createColoredDataCell(font, amountStr, bgColor, border, 8, TextAlignment.CENTER, RED));
    }

    private Table createIncomeTable(PdfFont font) {
        float[] columnWidths = {120, 80}; // 2 колонки: Категория, Сумма
        Table table = new Table(UnitValue.createPointArray(columnWidths));
        table.setWidth(UnitValue.createPercentValue(100));

        String[] headers = {"Категория", "Сумма (MDL)"};
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

    private void addIncomeRow(Table table, PdfFont font, String category, BigDecimal amount, boolean isEvenRow) {
        Color bgColor = isEvenRow ? ROW_BG_COLOR_2 : ROW_BG_COLOR_1;
        Border border = new SolidBorder(BORDER_COLOR, 0.5f);

        table.addCell(createDataCell(font, category, bgColor, border, 8, TextAlignment.CENTER));
        String amountStr = "+ " + DECIMAL_FORMAT.format(amount);
        table.addCell(createColoredDataCell(font, amountStr, bgColor, border, 8, TextAlignment.CENTER, GREEN));
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

    private Cell createColoredDataCell(PdfFont font, String text, Color bgColor, Border border, int fontSize, TextAlignment align, Color textColor) {
        return new Cell()
                .add(new Paragraph(text).setFont(font).setFontSize(fontSize).setFontColor(textColor))
                .setBackgroundColor(bgColor)
                .setTextAlignment(align)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setBorder(border)
                .setPadding(2);
    }

    private Table createHeaderTable(PdfFont font) throws Exception {
        // (без изменений – код логотипа и контактов)
        Table headerTable = new Table(UnitValue.createPercentArray(new float[]{50, 30, 60}));
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

        int fontSize = 8;
        Table infoBoxTable = new Table(1);
        infoBoxTable.setWidth(UnitValue.createPercentValue(100));

        Cell infoCell = new Cell()
                .setBackgroundColor(CONTACT_BG_COLOR)
                .setBorder(new SolidBorder(CONTACT_BORDER_COLOR, 0.5f))
                .setPadding(6)
                .setPaddingTop(4)
                .setPaddingBottom(4)
                .setTextAlignment(TextAlignment.LEFT);

        infoCell.add(new Paragraph("ENERGY SOLUTION GROUP SRL ")
                .setFont(font).setFontSize(fontSize).setMargin(0).setMultipliedLeading(1.0f));
        infoCell.add(new Paragraph("c.f: 1022602001390 ")
                .setFont(font).setFontSize(fontSize).setMargin(0).setMultipliedLeading(1.0f));
        infoCell.add(new Paragraph("Adresa: Aerodromului 10, Balti ")
                .setFont(font).setFontSize(fontSize).setMargin(0).setMultipliedLeading(1.0f));
        infoCell.add(new Paragraph("IBAN MD96AG000000022514765369 AGRNMD2X750 ")
                .setFont(font).setFontSize(fontSize).setMargin(0).setMultipliedLeading(1.0f));
        infoCell.add(new Paragraph("TVA: 1203281 ")
                .setFont(font).setFontSize(fontSize).setMargin(0).setMultipliedLeading(1.0f));
        infoCell.add(new Paragraph("B.C. MAIB S.A Administrator: Iațco Andrei ")
                .setFont(font).setFontSize(fontSize).setMargin(0).setMultipliedLeading(1.0f));
        infoCell.add(new Paragraph("  \n")
                .setFont(font).setFontSize(fontSize).setMargin(0).setMultipliedLeading(1.0f));
        infoCell.add(new Paragraph("Office: or. Balti  str Aerodromului 10 ")
                .setFont(font).setFontSize(fontSize).setMargin(0).setMultipliedLeading(1.0f));
        infoCell.add(new Paragraph("Depozit: or. Balti str Stefan cel Mare 128 ")
                .setFont(font).setFontSize(fontSize).setMargin(0).setMultipliedLeading(1.0f));
        infoCell.add(new Paragraph("tel: 067252000 / 067600640 ")
                .setFont(font).setFontSize(fontSize).setMargin(0).setMultipliedLeading(1.0f));
        infoCell.add(new Paragraph("email: info@solution.md ")
                .setFont(font).setFontSize(fontSize).setMargin(0).setMultipliedLeading(1.0f));
        infoCell.add(new Paragraph("web: https://solution.md/ ")
                .setFont(font).setFontSize(fontSize).setMargin(0).setMultipliedLeading(1.0f));

        infoBoxTable.addCell(infoCell);

        Cell rightCell = new Cell().add(infoBoxTable)
                .setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.RIGHT)
                .setVerticalAlignment(VerticalAlignment.MIDDLE);
        headerTable.addCell(rightCell);

        return headerTable;
    }

    private PdfFont getPdfFont() {
        try (InputStream is = getClass().getResourceAsStream("/fonts/freesans.ttf")) {
            if (is == null) {
                log.error("Critical error: Font file freesans.ttf not found in classpath at '/fonts/freesans.ttf'");
                throw new RuntimeException("Font for PDF not found. Please ensure your font is in src/main/resources/fonts/");
            }
            byte[] fontBytes = IOUtils.toByteArray(is);
            return PdfFontFactory.createFont(fontBytes, PdfEncodings.IDENTITY_H);
        } catch (Exception e) {
            log.error("Error loading or creating font from resources", e);
            throw new RuntimeException("Failed to load font for PDF generation", e);
        }
    }
}