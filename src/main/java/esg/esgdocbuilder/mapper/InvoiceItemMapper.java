package esg.esgdocbuilder.mapper;

import esg.esgdocbuilder.model.dto.request.InvoiceItemRequest;
import esg.esgdocbuilder.model.dto.response.InvoiceItemResponse;
import esg.esgdocbuilder.model.entity.Invoice;
import esg.esgdocbuilder.model.entity.InvoiceItem;
import esg.esgdocbuilder.model.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
public class InvoiceItemMapper {

    public InvoiceItemResponse toResponse(InvoiceItem item) {
        if (item == null) return null;
        InvoiceItemResponse response = new InvoiceItemResponse();
        response.setId(item.getId());
        response.setProductId(item.getProduct().getId());

        response.setNameProduct(item.getProduct().getName());
        response.setQuantity(item.getQuantity());
        response.setUnitPrice(item.getUnitPrice());
        response.setVatMultiplier(item.getVatMultiplier());
        response.setMarginality(item.getMarginality());
        response.setTotalPrice(item.getTotalPrice());
        return response;
    }

    public InvoiceItem toEntity(InvoiceItemRequest request, Invoice invoice, Product product) {
        if (request == null) return null;
        InvoiceItem item = new InvoiceItem();
        item.setInvoice(invoice);
        item.setProduct(product);
        item.setQuantity(request.getQuantity());
        item.setUnitPrice(request.getUnitPrice());
        item.setVatMultiplier(request.getVatMultiplier());
        item.setMarginality(request.getMarginality());
        item.setTotalPrice(request.getTotalPrice());
        return item;
    }
}
