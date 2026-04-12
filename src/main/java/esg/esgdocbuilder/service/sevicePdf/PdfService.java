package esg.esgdocbuilder.service.sevicePdf;


import java.io.ByteArrayOutputStream;

public interface PdfService {

    ByteArrayOutputStream generateInvoicePdf(Long invoiceId);
    ByteArrayOutputStream generateInternalInvoicePdf(Long invoiceId);
}