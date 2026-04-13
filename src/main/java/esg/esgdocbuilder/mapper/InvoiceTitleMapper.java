package esg.esgdocbuilder.mapper;

import esg.esgdocbuilder.model.dto.response.InvoiceTitleResponse;
import esg.esgdocbuilder.model.entity.Invoice;
import org.springframework.stereotype.Component;

@Component
public class InvoiceTitleMapper {

    public InvoiceTitleResponse toResponse(Invoice invoice) {
        if (invoice == null) return null;
        InvoiceTitleResponse response = new InvoiceTitleResponse();
        response.setId(invoice.getId());
        response.setInvoiceName(invoice.getInvoiceName());
        response.setPower(invoice.getPowerKwt());
        response.setVat_amount(invoice.getVatAmount());
        response.setSum(invoice.getSumAmount());
        response.setSumMarginality(invoice.getSumMarginality());
        response.setCreatedAt(invoice.getCreatedAt());
        response.setUpdatedAt(invoice.getUpdatedAt());


        return response;
    }
}
