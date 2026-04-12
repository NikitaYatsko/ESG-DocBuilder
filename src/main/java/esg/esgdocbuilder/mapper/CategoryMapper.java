package esg.esgdocbuilder.mapper;


import esg.esgdocbuilder.model.dto.response.CategoryResponse;
import esg.esgdocbuilder.model.entity.Category;
import org.springframework.stereotype.Component;


@Component
public class CategoryMapper {
    public CategoryResponse toResponse(Category category){
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
