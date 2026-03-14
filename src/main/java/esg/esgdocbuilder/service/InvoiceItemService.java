package esg.esgdocbuilder.service;

import esg.esgdocbuilder.model.dto.request.InvoiceItemRequest;
import esg.esgdocbuilder.model.dto.response.InvoiceItemResponse;

import java.util.List;

public interface InvoiceItemService {
    InvoiceItemResponse addItemToInvoice(Long invoiceId, InvoiceItemRequest itemRequest);

    InvoiceItemResponse updateItem (Long itemId, InvoiceItemRequest itemRequest);

    void deleteItem (Long itemId);

    List<InvoiceItemResponse> getItemsByInvoiceId(Long invoiceId);
}
