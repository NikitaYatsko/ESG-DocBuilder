package esg.esgdocbuilder.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApiErrorMessage {

    PRODUCT_NOT_FOUND("Product not found"),
    PRODUCT_ALREADY_EXISTS("Product already exists"),
    CATEGORY_NOT_FOUND("Category not found"),
    ;


    private String message;

    public String getMessage(Object... args) {
        return String.format(message, args);
    }
}
