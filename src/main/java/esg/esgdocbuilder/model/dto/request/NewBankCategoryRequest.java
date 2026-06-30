package esg.esgdocbuilder.model.dto.request;

import lombok.Data;

@Data
public class NewBankCategoryRequest {
    private String name;
    private String type;
}
