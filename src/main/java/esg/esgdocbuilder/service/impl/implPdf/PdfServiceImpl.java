package esg.esgdocbuilder.service.impl.implPdf;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
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
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;

@Slf4j
@Service
@RequiredArgsConstructor
public class PdfServiceImpl implements PdfService {

    private final InvoiceService invoiceService;

    @Override
    public ByteArrayOutputStream generateInvoicePdf(Long invoiceId) {
        // 1. Получаем данные о смете через существующий сервис
        InvoiceResponse invoice = invoiceService.getInvoiceById(invoiceId);

        // 2. Создаём поток для хранения PDF в памяти
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            // 3. Инициализируем PdfWriter и PdfDocument
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // 4. Создаём шрифт (Helvetica – встроенный, бесплатный)
            PdfFont font = PdfFontFactory.createFont(
                    "fonts/FreeSans.ttf",
                    PdfEncodings.IDENTITY_H
            );

            // 5. Заголовок документа
            Paragraph title = new Paragraph("СМЕТА")
                    .setFont(font)
                    .setFontSize(18)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20);
            document.add(title);

            // 6. Информация о смете (номер, дата)
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            Paragraph info = new Paragraph()
                    .setFont(font)
                    .setFontSize(10)
                    .setMarginBottom(10);
            info.add("Номер: ").add(invoice.getInvoiceNumber()).add("\n");
            info.add("Создана: ")
                    .add(dateFormat.format(
                            Date.from(invoice.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant())
                    ))
                    .add("\n");


            document.add(info);

            // 7. Таблица позиций
            // Колонки: Наименование, Кол-во, Цена, Множитель НДС, Сумма
            float[] columnWidths = {200, 60, 80, 80, 100};
            Table table = new Table(UnitValue.createPercentArray(columnWidths));
            table.setWidth(UnitValue.createPercentValue(100));

            // Заголовки таблицы
            addTableHeader(table, font, "name");
            addTableHeader(table, font, "Кол-во");
            addTableHeader(table, font, "Цена");
            addTableHeader(table, font, "НДС");
            addTableHeader(table, font, "Сумма");

            // Заполнение таблицы
            int rowsPerPage = 15;
            List<InvoiceItemResponse> items = invoice.getItems();

            for (int i = 0; i < items.size(); i += rowsPerPage) {
                List<InvoiceItemResponse> pageItems =
                        items.subList(i, Math.min(i + rowsPerPage, items.size()));

                // создаём таблицу
                Table tableAdd = createTable(font);

                // заполняем
                for (InvoiceItemResponse item : pageItems) {
                    addTableCell(tableAdd, font, item.getNameProduct());
                    addTableCell(tableAdd, font, item.getQuantity().toString());
                    addTableCell(tableAdd, font, item.getUnitPrice().toString());
                    addTableCell(tableAdd, font, item.getVatMultiplier().toString());
                    addTableCell(tableAdd, font, item.getTotalPrice().toString());
                }

                document.add(tableAdd);

                // если не последняя страница → новая страница
                if (i + rowsPerPage < items.size()) {
                    document.add(new AreaBreak());
                }
            }

            // 8. Итоговая сумма (СЭС)
            Paragraph totalParagraph = new Paragraph()
                    .setFont(font)
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setMarginTop(20);
            totalParagraph.add("Итого (СЭС): ").add(invoice.getTotalAmount().toString()).add(" MDL");
            document.add(totalParagraph);

            // 9. Закрытие документа (автоматически завершает запись)
            document.close();

            log.info("PDF для сметы {} успешно сгенерирован", invoice.getInvoiceNumber());
        } catch (Exception e) {
            log.error("Ошибка генерации PDF для сметы с ID {}", invoiceId, e);
            throw new RuntimeException("Ошибка генерации PDF", e);
        }

        return outputStream;
    }

    /**
     * Вспомогательный метод для создания ячейки заголовка таблицы.
     */
    private void addTableHeader(Table table, PdfFont font, String text) {
        Cell cell = new Cell()
                .add(new Paragraph(text).setFont(font).setFontSize(10))
                .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                .setTextAlignment(TextAlignment.CENTER)
                .setPadding(5);
        table.addCell(cell);
    }

    /**
     * Вспомогательный метод для создания ячейки данных.
     */
    private void addTableCell(Table table, PdfFont font, String text) {
        Cell cell = new Cell()
                .add(new Paragraph(text).setFont(font).setFontSize(10))
                .setTextAlignment(TextAlignment.LEFT)
                .setPadding(5);
        table.addCell(cell);
    }


    private Table createTable(PdfFont font) {
        float[] columnWidths = {200, 60, 80, 80, 100};
        Table table = new Table(UnitValue.createPercentArray(columnWidths));
        table.setWidth(UnitValue.createPercentValue(100));

        addTableHeader(table, font, "Наименование");
        addTableHeader(table, font, "Кол-во");
        addTableHeader(table, font, "Цена");
        addTableHeader(table, font, "НДС");
        addTableHeader(table, font, "Сумма");

        return table;
    }

}