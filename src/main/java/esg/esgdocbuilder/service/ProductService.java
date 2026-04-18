package esg.esgdocbuilder.service;

import esg.esgdocbuilder.model.dto.request.NewProductRequest;
import esg.esgdocbuilder.model.dto.response.CategoryResponse;
import esg.esgdocbuilder.model.dto.response.PaginationResponse;
import esg.esgdocbuilder.model.dto.response.ProductResponse;
import esg.esgdocbuilder.model.entity.Product;
import esg.esgdocbuilder.model.enums.TypeOfUnitEnum;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface ProductService {
    ProductResponse getProductById(Long id);

    ProductResponse createProduct(NewProductRequest newProductRequest);

    PaginationResponse<ProductResponse> getAllProducts(Pageable pageable);

    //Поиск
    PaginationResponse<ProductResponse> searchProducts(String query, Pageable pageable);

    //Фильтры
    PaginationResponse<ProductResponse> getProductsByCategory(Long categoryId, Pageable pageable);

    PaginationResponse<ProductResponse> getProductsByTypeOfUnit(TypeOfUnitEnum unitId, Pageable pageable);

    List<CategoryResponse> getAllCategory ();
  
    List<Product> getAllProducts();

    ProductResponse updateProduct( NewProductRequest request, Long id);

    void deleteProduct(Long id);


}
