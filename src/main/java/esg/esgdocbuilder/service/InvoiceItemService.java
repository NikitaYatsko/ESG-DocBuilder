package esg.esgdocbuilder.service;

import esg.esgdocbuilder.model.dto.request.InvoiceItemRequest;
import esg.esgdocbuilder.model.dto.request.NewProductRequest;
import esg.esgdocbuilder.model.dto.response.InvoiceItemResponse;
import esg.esgdocbuilder.model.dto.response.ProductResponse;

import java.util.List;

public interface InvoiceItemService {
    InvoiceItemResponse addItemToInvoice(Long invoiceId, InvoiceItemRequest itemRequest);

    InvoiceItemResponse updateItem (Long itemId, InvoiceItemRequest itemRequest);

    void deleteItem (Long itemId);

    List<InvoiceItemResponse> getItemsByInvoiceId(Long invoiceId);

    InvoiceItemResponse getItemById(Long itemId);

    void deleteByInvoiceId(Long invoiceId);


}
