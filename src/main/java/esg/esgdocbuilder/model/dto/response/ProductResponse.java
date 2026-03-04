package esg.esgdocbuilder.model.dto.response;


import esg.esgdocbuilder.model.enums.TypeOfUnitEnum;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class ProductResponse {
    private Long id;
    private String name;
    private TypeOfUnitEnum typeOfUnit;
    private String category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
