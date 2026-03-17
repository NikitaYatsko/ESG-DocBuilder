package esg.esgdocbuilder.model.dto.response;

import esg.esgdocbuilder.model.enums.TypeOfOperationEnums;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Data
public class BankOperationResponse {
    private TypeOfOperationEnums type;
    private BigDecimal amount;
    private String accountName;
    private String comment;
    private LocalDateTime createdAt;
}
