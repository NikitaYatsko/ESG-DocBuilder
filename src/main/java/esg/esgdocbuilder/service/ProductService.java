package esg.esgdocbuilder.service;

import esg.esgdocbuilder.model.dto.request.NewProductRequest;
import esg.esgdocbuilder.model.dto.response.CategoryResponse;
import esg.esgdocbuilder.model.dto.response.PaginationResponse;
import esg.esgdocbuilder.model.dto.response.ProductResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface ProductService {
    ProductResponse getProductById(Long id);

    ProductResponse createProduct(NewProductRequest newProductRequest);

    PaginationResponse<ProductResponse> getAllProducts(Pageable pageable);

    List<CategoryResponse> getAllCategory ();

    ProductResponse updateProduct( NewProductRequest request, Long id);

    void deleteProduct(Long id);

    List<ProductResponse> getProductsByCategory(Long categoryId);
}
