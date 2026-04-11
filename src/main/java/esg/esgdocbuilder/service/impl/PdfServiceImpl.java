package esg.esgdocbuilder.service.impl;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import esg.esgdocbuilder.model.entity.Product;
import esg.esgdocbuilder.service.PdfService;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class PdfServiceImpl implements PdfService {

    // Цвета
    private static final DeviceRgb HEADER_BG_COLOR = new DeviceRgb(207, 149, 44);
    private static final DeviceRgb ROW_BG_COLOR_1 = new DeviceRgb(255, 255, 255);
    private static final DeviceRgb ROW_BG_COLOR_2 = new DeviceRgb(255, 249, 196);
    private static final DeviceRgb BORDER_COLOR = new DeviceRgb(200, 200, 200);

    @Override
    public byte[] generateProductsPdf(List<Product> products) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Шрифт с поддержкой кириллицы
            PdfFont font = PdfFontFactory.createFont(
                    "src/main/resources/fonts/OpenSans-Regular.ttf",
                    PdfEncodings.IDENTITY_H
            );
            document.setFont(font);

            // Заголовок
            document.add(new Paragraph("Список продуктов")
                    .setFontSize(16)
                    .setBold()
                    .setMarginBottom(10));

            // Таблица
            float[] columnWidths = {1F, 3F, 2F, 2F, 3F, 1F};
            Table table = new Table(columnWidths);
            table.setWidth(UnitValue.createPercentValue(100));

// Заголовки
            String[] headers = {"ID", "Название", "Цена Покупки", "Цена Продажи", "Категория", "Маржинальность"};
            for (String header : headers) {
                Cell cell = new Cell()
                        .add(new Paragraph(header))
                        .setBackgroundColor(HEADER_BG_COLOR)
                        .setFontColor(ColorConstants.WHITE)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE)
                        .setPadding(5)
                        .setBorder(new SolidBorder(BORDER_COLOR, 1));
                table.addHeaderCell(cell);
            }

// Данные
            boolean alternate = false;
            for (Product product : products) {
                DeviceRgb rowColor = alternate ? ROW_BG_COLOR_2 : ROW_BG_COLOR_1;
                alternate = !alternate;

                table.addCell(createCell(String.valueOf(product.getId()), rowColor));
                table.addCell(createCell(product.getName(), rowColor));
                table.addCell(createCell(product.getCostPrice() + " MDL", rowColor));
                table.addCell(createCell(product.getSellPrice() + " MDL", rowColor));
                table.addCell(createCell(product.getCategory().getName(), rowColor));
                table.addCell(createCell(product.getMarginality() + "%", rowColor));
            }

            document.add(table);
            document.close();

            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Ошибка при создании PDF", e);
        }
    }

    private Cell createCell(String content, DeviceRgb bgColor) {
        return new Cell()
                .add(new Paragraph(content))
                .setBackgroundColor(bgColor)
                .setTextAlignment(TextAlignment.LEFT)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setPadding(5)
                .setBorder(new SolidBorder(BORDER_COLOR, 1)); // границы ячеек
    }
}