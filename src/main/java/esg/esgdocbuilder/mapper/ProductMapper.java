package esg.esgdocbuilder.mapper;

import esg.esgdocbuilder.model.dto.request.NewProductRequest;
import esg.esgdocbuilder.model.dto.response.ProductResponse;
import esg.esgdocbuilder.model.entity.Category;
import esg.esgdocbuilder.model.entity.Product;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ProductMapper {


    public ProductResponse toResponse(Product product) {
        if (product == null) return null;

        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setCategory(product.getCategory().getName());
        response.setCostPrice(product.getCostPrice());
        response.setSellPrice(product.getSellPrice());
        response.setMarginality(product.getMarginality());
        response.setVat(product.getVat());
        response.setDeleted(product.getIsDeleted());
        response.setCreatedAt(product.getCreatedAt());
        response.setTypeOfUnit(product.getTypeOfUnit());



        return response;
    }

    public Product toEntity(NewProductRequest newProductRequest, Category category) {
        if (newProductRequest == null) return null;
        Product product = new Product();
        product.setName(newProductRequest.getName());
        product.setTypeOfUnit(newProductRequest.getTypeOfUnit());
        product.setCategory(category);
        product.setCostPrice(newProductRequest.getCostPrice());
        product.setSellPrice(newProductRequest.getSellPrice());
        product.setMarginality(newProductRequest.getMarginality());
        product.setVat(newProductRequest.getVat());
        return product;

    }
}
