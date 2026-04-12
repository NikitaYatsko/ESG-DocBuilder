package esg.esgdocbuilder.service;


import esg.esgdocbuilder.model.entity.Product;

import java.io.ByteArrayOutputStream;
import java.util.List;

public interface PdfService {

    ByteArrayOutputStream generateInvoicePdf(Long invoiceId);
    ByteArrayOutputStream generateInternalInvoicePdf(Long invoiceId);
    byte[] generateProductsPdf(List<Product> products);
}