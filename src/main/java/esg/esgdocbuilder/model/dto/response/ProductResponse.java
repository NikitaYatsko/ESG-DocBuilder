package esg.esgdocbuilder.model.dto.response;

import esg.esgdocbuilder.model.entity.Category;
import esg.esgdocbuilder.model.enums.TypeOfUnitEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProductResponse {
    private Long id;
    private String generalData;
    private BigDecimal purchasePrice;
    private BigDecimal sellPrice;
    private Category category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private TypeOfUnitEnum typeOfUnit;
}
