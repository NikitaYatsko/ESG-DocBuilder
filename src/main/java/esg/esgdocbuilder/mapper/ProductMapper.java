package esg.esgdocbuilder.mapper;

import esg.esgdocbuilder.model.dto.request.NewProductRequest;
import esg.esgdocbuilder.model.dto.response.ProductResponse;
import esg.esgdocbuilder.model.entity.Product;
import esg.esgdocbuilder.model.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public  Product toEntity(NewProductRequest request, Category category) {
        Product product = new Product();

        product.setGeneralData(request.getGeneralData());
        product.setPurchasePrice(request.getPurchasePrice());
        product.setSellPrice(request.getSellPrice());
        product.setCategory(category);
        product.setTypeOfUnit(request.getTypeOfUnit());

        return product;
    }

    public  ProductResponse toResponse(Product product) {
        ProductResponse response = new ProductResponse();

        response.setId(product.getId());
        response.setGeneralData(product.getGeneralData());
        response.setPurchasePrice(product.getPurchasePrice());
        response.setSellPrice(product.getSellPrice());
        response.setCategoryName(product.getCategory().getName());
        response.setCreatedAt(product.getCreatedAt());
        response.setUpdatedAt(product.getUpdatedAt());
        response.setTypeOfUnit(product.getTypeOfUnit());

        return response;
    }
}