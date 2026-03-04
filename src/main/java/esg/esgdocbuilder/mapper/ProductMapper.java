package esg.esgdocbuilder.mapper;

import esg.esgdocbuilder.model.dto.request.LoginRequest;
import esg.esgdocbuilder.model.dto.request.NewProductRequest;
import esg.esgdocbuilder.model.dto.response.ProductResponse;
import esg.esgdocbuilder.model.entity.Category;
import esg.esgdocbuilder.model.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {


    public ProductResponse toResponse(Product product) {
        if (product == null) return null;

        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setTypeOfUnit(product.getTypeOfUnit());
        response.setCategory(product.getCategory().getName());
        response.setCreatedAt(product.getCreatedAt());
        response.setUpdatedAt(product.getUpdatedAt());

        return response;
    }
    public Product toEntity(NewProductRequest newProductRequest, Category category) {
        if (newProductRequest == null) return null;
        Product product = new Product();
        product.setName(newProductRequest.getName());
        product.setTypeOfUnit(newProductRequest.getTypeOfUnit());
        product.setCategory(category);
        product.setCostPrice(newProductRequest.getCostPrice());
        product.setMarkupPercent(newProductRequest.getMarkupPercent());
        product.setHasVat(newProductRequest.getHasVat());
        return product;

    }
}
