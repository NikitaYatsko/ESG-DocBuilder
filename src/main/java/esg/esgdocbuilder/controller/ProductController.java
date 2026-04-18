package esg.esgdocbuilder.controller;

import esg.esgdocbuilder.model.dto.request.NewProductRequest;
import esg.esgdocbuilder.model.dto.response.CategoryResponse;
import esg.esgdocbuilder.model.dto.response.PaginationResponse;
import esg.esgdocbuilder.model.dto.response.ProductResponse;
import esg.esgdocbuilder.model.entity.Product;
import esg.esgdocbuilder.model.enums.TypeOfUnitEnum;
import esg.esgdocbuilder.repository.CategoryRepository;
import esg.esgdocbuilder.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.AnyDiscriminatorImplicitValues;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<PaginationResponse<ProductResponse>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(productService.getAllProducts(pageable));

    }
    //Поиск
    @GetMapping("/search")
    public ResponseEntity<PaginationResponse<ProductResponse>> searchProducts(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(productService.searchProducts(q, pageable));

    }
    //Фильтры
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<PaginationResponse<ProductResponse>> getProductsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(productService.getProductsByCategory(categoryId, pageable));
    }

    @GetMapping("/type-of-unit/{typeOfUnit}")
    public ResponseEntity<PaginationResponse<ProductResponse>> getProductsByTypeOfUnit(
            @PathVariable TypeOfUnitEnum typeOfUnit,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(productService.getProductsByTypeOfUnit(typeOfUnit, pageable));
    }


    @GetMapping("/all-products")
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }


    @GetMapping("/category")
    public ResponseEntity<List<CategoryResponse>> getAllCategory() {
        return ResponseEntity.ok(productService.getAllCategory());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @RequestBody NewProductRequest request) {

        return ResponseEntity.ok(productService.createProduct(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id,
                                                         @RequestBody NewProductRequest request) {
        return ResponseEntity.ok(productService.updateProduct(request, id));
    }
}
