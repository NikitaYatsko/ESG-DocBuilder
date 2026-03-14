package esg.esgdocbuilder.service.impl;

import esg.esgdocbuilder.constants.ApiErrorMessage;
import esg.esgdocbuilder.exception.exceptions.CategoryNotFoundException;
import esg.esgdocbuilder.exception.exceptions.ProductNotFoundException;
import esg.esgdocbuilder.mapper.ProductMapper;
import esg.esgdocbuilder.model.dto.request.NewProductRequest;
import esg.esgdocbuilder.model.dto.response.ProductResponse;
import esg.esgdocbuilder.model.entity.Category;
import esg.esgdocbuilder.model.entity.Product;
import esg.esgdocbuilder.repository.CategoryRepository;
import esg.esgdocbuilder.repository.ProductRepository;
import esg.esgdocbuilder.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

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

        // 1. Базовое создание сущности через маппер
        Product product = productMapper.toEntity(request, category);

        // 2. Расчёт sellPrice и marginality на основе costPrice и markupPercent
        BigDecimal costPrice = request.getCostPrice();
        BigDecimal markupPercent = request.getMarkupPercent() != null ? request.getMarkupPercent() : BigDecimal.ZERO;

        // sellPrice = costPrice + (costPrice * markupPercent / 100)
        BigDecimal sellPrice = costPrice.add(
                costPrice.multiply(markupPercent)
                        .divide(new BigDecimal(100), 2, RoundingMode.HALF_UP)
        );
        product.setSellPrice(sellPrice);

        // marginality = ((sellPrice - costPrice) / sellPrice) * 100
        if (sellPrice.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal marginality = sellPrice.subtract(costPrice)
                    .divide(sellPrice, 4, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal(100))
                    .setScale(2, RoundingMode.HALF_UP);
            product.setMarginality(marginality);
        } else {
            product.setMarginality(BigDecimal.ZERO);
        }

        // 3. Установка НДС (ставка в процентах, например 20%)
        if (request.getHasVat() != null && request.getHasVat()) {
            // Здесь можно либо брать ставку из конфигурации, либо фиксированно 20%
            product.setVat(new BigDecimal("20")); // или другая логика
        } else {
            product.setVat(BigDecimal.ZERO);
        }

        // 4. Сохранение
        Product savedProduct = productRepository.save(product);
        return productMapper.toResponse(savedProduct);
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(productMapper::toResponse).collect(Collectors.toList());
    }
}
