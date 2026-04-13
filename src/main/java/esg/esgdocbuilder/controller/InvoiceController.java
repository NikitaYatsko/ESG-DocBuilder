package esg.esgdocbuilder.controller;

import esg.esgdocbuilder.model.dto.request.InvoiceRequest;
import esg.esgdocbuilder.model.dto.response.InvoiceResponse;
import esg.esgdocbuilder.model.dto.response.InvoiceTitleResponse;
import esg.esgdocbuilder.service.InvoiceService;
import esg.esgdocbuilder.service.PdfService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;


import java.io.ByteArrayOutputStream;
import java.util.List;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final PdfService pdfService;


    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> generatePdf(@PathVariable Long id) {
        ByteArrayOutputStream pdfStream = pdfService.generateInvoicePdf(id);
        byte[] pdfBytes = pdfStream.toByteArray();

        // Получаем номер сметы для имени файла
        InvoiceResponse invoice = invoiceService.getInvoiceById(id);
        String filename = "smeta_" + invoice.getInvoiceName() + ".pdf";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

    @GetMapping("/{id}/internal-pdf")
    public ResponseEntity<byte[]> generateInternalPdf(@PathVariable Long id) {
        ByteArrayOutputStream pdfStream = pdfService.generateInternalInvoicePdf(id);
        byte[] pdfBytes = pdfStream.toByteArray();
        InvoiceResponse invoice = invoiceService.getInvoiceById(id);
        String filename = "internal_smeta_" + invoice.getInvoiceName() + ".pdf";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

    @PostMapping
    public ResponseEntity<InvoiceResponse> createInvoice(@Valid @RequestBody InvoiceRequest request) {
        InvoiceResponse response = invoiceService.createInvoice(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceResponse> getInvoiceById(@PathVariable Long id) {
        return ResponseEntity.ok(invoiceService.getInvoiceById(id));
    }

    @GetMapping
    public ResponseEntity<List<InvoiceResponse>> getAllInvoices() {
        return ResponseEntity.ok(invoiceService.getAllInvoices());
    }


    @GetMapping("/invoice-title")
    public ResponseEntity<List<InvoiceTitleResponse>> getInvoiceAllTitle() {
        return ResponseEntity.ok(invoiceService.getInvoiceAllTitle());
    }

    @PutMapping("/{id}")
    public ResponseEntity<InvoiceResponse> updateInvoice(@PathVariable Long id,
                                                         @Valid @RequestBody InvoiceRequest request) {
        return ResponseEntity.ok(invoiceService.updateInvoice(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable Long id) {
        invoiceService.deleteInvoice(id);
        return ResponseEntity.noContent().build();
    }
}