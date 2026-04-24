package esg.esgdocbuilder.mapper;


import esg.esgdocbuilder.model.dto.request.InvoiceRequest;
import esg.esgdocbuilder.model.dto.response.InvoiceItemResponse;
import esg.esgdocbuilder.model.dto.response.InvoiceResponse;
import esg.esgdocbuilder.model.entity.Invoice;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class InvoiceMapper {
    private final InvoiceItemMapper itemMapper;

    public InvoiceMapper(InvoiceItemMapper itemMapper) {
        this.itemMapper = itemMapper;
    }

    public InvoiceResponse toResponse(Invoice invoice) {
        if (invoice == null) return null;
        InvoiceResponse response = new InvoiceResponse();
        response.setId(invoice.getId());
        response.setInvoiceName(invoice.getInvoiceName());
        response.setPower(invoice.getPowerKwt());
        response.setVat_amount(invoice.getVatAmount());
        response.setSum(invoice.getSumAmount());
        response.setDiscountPercent(invoice.getDiscountPercent());
        response.setSumMarginality(invoice.getSumMarginality());
        response.setCreatedAt(invoice.getCreatedAt());
        response.setUpdatedAt(invoice.getUpdatedAt());

        List<InvoiceItemResponse> itemResponses = invoice.getInvoiceItems().stream()
                .map(itemMapper::toResponse)
                .collect(Collectors.toList());
        response.setItems(itemResponses);

        BigDecimal total = itemResponses.stream()
                .map(InvoiceItemResponse::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        response.setTotalAmount(total);
        return response;
    }

}