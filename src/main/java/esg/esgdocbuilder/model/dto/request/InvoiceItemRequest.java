package esg.esgdocbuilder.model.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class InvoiceItemRequest {
    @NotNull
    private Long productId;


    @PositiveOrZero
    private BigDecimal quantity;

    @PositiveOrZero
    private BigDecimal unitPrice;

    @PositiveOrZero
    private BigDecimal vatMultiplier;

    @PositiveOrZero
    private BigDecimal marginality;

    @PositiveOrZero
    private BigDecimal totalPrice;


}
