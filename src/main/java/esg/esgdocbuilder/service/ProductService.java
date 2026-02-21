package esg.esgdocbuilder.service;

import esg.esgdocbuilder.model.dto.request.NewProductRequest;
import esg.esgdocbuilder.model.dto.response.ProductResponse;

import java.util.List;


public interface ProductService {
    ProductResponse getProductById(Long id);

    ProductResponse createProduct(NewProductRequest newProductRequest);

    List<ProductResponse> getAllProducts();
}
