package esg.esgdocbuilder.service.impl;

import esg.esgdocbuilder.constants.ApiErrorMessage;
import esg.esgdocbuilder.exception.exceptions.CategoryNotFoundException;
import esg.esgdocbuilder.exception.exceptions.ProductNotFoundException;
import esg.esgdocbuilder.mapper.ProductMapper;
import esg.esgdocbuilder.model.dto.request.NewProductRequest;

import esg.esgdocbuilder.model.dto.response.PaginationResponse;
import esg.esgdocbuilder.model.dto.response.ProductResponse;
import esg.esgdocbuilder.model.entity.Category;
import esg.esgdocbuilder.model.entity.Product;
import esg.esgdocbuilder.repository.CategoryRepository;
import esg.esgdocbuilder.repository.ProductRepository;
import esg.esgdocbuilder.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;


    @Override
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new ProductNotFoundException(
                        ApiErrorMessage.PRODUCT_NOT_FOUND.getMessage()));
        return productMapper.toResponse(product);
    }

    @Override
    public ProductResponse createProduct(NewProductRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException(
                        ApiErrorMessage.CATEGORY_NOT_FOUND.getMessage()));


        Product product = productMapper.toEntity(request, category);


        Product savedProduct = productRepository.save(product);
        return productMapper.toResponse(savedProduct);
    }

    @Override
    public ProductResponse updateProduct(NewProductRequest request, Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(
                        ApiErrorMessage.PRODUCT_NOT_FOUND.getMessage()));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException(
                        ApiErrorMessage.CATEGORY_NOT_FOUND.getMessage()));


        product.setName(request.getName());
        product.setTypeOfUnit(request.getTypeOfUnit());
        product.setCategory(category);
        product.setCostPrice(request.getCostPrice());
        product.setSellPrice(request.getSellPrice());
        product.setMarginality(request.getMarginality());
        product.setVat(request.getVat());

        Product updatedProduct = productRepository.save(product);
        return productMapper.toResponse(updatedProduct);
    }

    @Override
    public PaginationResponse<ProductResponse> getAllProducts(Pageable pageable) {

        Page<Product> products = productRepository.findAll(pageable);
        Page<ProductResponse> dtos = products.map(productMapper::toResponse);

        return new PaginationResponse<>(
                dtos.getContent(),
                new PaginationResponse.Pagination(
                        dtos.getTotalElements(),
                        pageable.getPageSize(),
                        dtos.getNumber() + 1,
                        dtos.getTotalPages()
                )
        );
    }

    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(ApiErrorMessage.PRODUCT_NOT_FOUND.getMessage());
        }
        productRepository.deleteById(id);
    }
}
