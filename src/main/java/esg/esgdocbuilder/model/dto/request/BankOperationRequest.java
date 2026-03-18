package esg.esgdocbuilder.model.dto.request;

import esg.esgdocbuilder.model.enums.TypeOfOperationEnums;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankOperationRequest {
    private TypeOfOperationEnums type;
    private BigDecimal amount;
    private String accountName;
    private String comment;
}
