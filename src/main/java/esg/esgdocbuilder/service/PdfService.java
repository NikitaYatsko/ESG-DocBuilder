package esg.esgdocbuilder.service;

import esg.esgdocbuilder.model.entity.Product;

import java.util.List;

public interface PdfService {
    byte[] generateProductsPdf(List<Product> products);
}
