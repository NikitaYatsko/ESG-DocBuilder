package esg.esgdocbuilder.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApiErrorMessage {

    PRODUCT_NOT_FOUND("Product not found"),
    PRODUCT_ALREADY_EXISTS("Product already exists"),
    CATEGORY_NOT_FOUND("Category not found"),
    USER_DOES_NOT_EXIST("User doesn't exist"),
    TOKEN_EXPIRED("Token expired"),
    BAD_CREDENTIALS("Invalid Email or password"),
    FILE_IS_EMPTY("File is empty"),
    REFRESH_TOKEN_NOT_FOUND("Refresh token not found"),
    INVOICE_NOT_FOUND("Invoice not found"),
    INVOICE_ITEM_NOT_FOUND("Invoice item not found"),
    ACCOUNT_NOT_FOUND("Account not found"),
    ;


    private String message;

    public String getMessage(Object... args) {
        return String.format(message, args);
    }
}
