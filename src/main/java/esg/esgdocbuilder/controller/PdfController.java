package esg.esgdocbuilder.controller;

import esg.esgdocbuilder.model.entity.Product;
import esg.esgdocbuilder.service.BankPdfService;
import esg.esgdocbuilder.service.ProductService;
import esg.esgdocbuilder.service.PdfService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/pdf")
public class PdfController {
    private final PdfService pdfService;
    private final ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<byte[]> getProducts() {
        List<Product> products = productService.getAllProducts();
        byte[] pdfBytes = pdfService.generateProductsPdf(products);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=products.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }


    private final BankPdfService bankPdfService;

    @GetMapping("/bank")
    public ResponseEntity<byte[]> generateBankPdf(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end
    ) {
        ByteArrayOutputStream pdfStream = bankPdfService.generateBankOperationsPdf(start, end);
        byte[] pdfBytes = pdfStream.toByteArray();

        String filename = "bank_operations_" +
                (start != null ? start.toString() : "from_begin") +
                "_to_" +
                (end != null ? end.toString() : "now") +
                ".pdf";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}
