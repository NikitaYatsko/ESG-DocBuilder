package esg.esgdocbuilder.controller;


import esg.esgdocbuilder.model.dto.request.InvoiceItemRequest;
import esg.esgdocbuilder.model.dto.response.InvoiceItemResponse;
import esg.esgdocbuilder.service.InvoiceItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices/{invoiceId}/items")
@RequiredArgsConstructor
public class InvoiceItemController {
    private final InvoiceItemService itemService;


    @GetMapping("/{itemId}")
    public ResponseEntity<InvoiceItemResponse> getItemById(@PathVariable Long itemId) {
        return ResponseEntity.ok(itemService.getItemById(itemId));
    }

    @PostMapping
    public ResponseEntity<InvoiceItemResponse> addItem(@PathVariable Long invoiceId,
                                                       @Valid @RequestBody InvoiceItemRequest request) {
        return new ResponseEntity<>(itemService.addItemToInvoice(invoiceId, request), HttpStatus.CREATED);
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<InvoiceItemResponse> updateItem(@PathVariable Long itemId,
                                                          @Valid @RequestBody InvoiceItemRequest request) {
        return ResponseEntity.ok(itemService.updateItem(itemId, request));
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long itemId) {
        itemService.deleteItem(itemId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<InvoiceItemResponse>> getItems(@PathVariable Long invoiceId) {
        return ResponseEntity.ok(itemService.getItemsByInvoiceId(invoiceId));
    }
}
