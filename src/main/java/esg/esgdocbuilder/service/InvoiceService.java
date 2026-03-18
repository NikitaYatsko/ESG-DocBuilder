package esg.esgdocbuilder.service;

import esg.esgdocbuilder.model.dto.request.InvoiceRequest;
import esg.esgdocbuilder.model.dto.response.InvoiceResponse;

import java.util.List;

public interface InvoiceService {

    InvoiceResponse createInvoice (InvoiceRequest invoiceRequest);

    InvoiceResponse getInvoiceById(Long id);

    List<InvoiceResponse> getAllInvoices ();

    InvoiceResponse updateInvoice(Long id,InvoiceRequest invoiceRequest);

    void deleteInvoice(Long id);
}
