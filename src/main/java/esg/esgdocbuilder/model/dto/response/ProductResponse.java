package esg.esgdocbuilder.model.dto.response;


import esg.esgdocbuilder.model.enums.TypeOfUnitEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
public class ProductResponse {
    private Long id;
    private String name;
    private String category;
    private BigDecimal costPrice;
    private BigDecimal sellPrice;
    private BigDecimal marginality;
    private BigDecimal vat;
    private LocalDateTime createdAt;
    private TypeOfUnitEnum typeOfUnit;
    private boolean isDeleted;
}
