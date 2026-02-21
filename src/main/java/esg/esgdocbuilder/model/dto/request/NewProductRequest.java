package esg.esgdocbuilder.model.dto.request;

import esg.esgdocbuilder.model.enums.TypeOfUnitEnum;
import lombok.Data;

import java.math.BigDecimal;


@Data
public class NewProductRequest {
    private String generalData;
    private BigDecimal purchasePrice;
    private BigDecimal sellPrice;
    private Long categoryId;
    private TypeOfUnitEnum typeOfUnit;
}
