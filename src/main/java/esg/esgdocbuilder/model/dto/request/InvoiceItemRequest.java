package esg.esgdocbuilder.model.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class InvoiceItemRequest {
    @NotNull
    private Long productId;

    @NotNull
    @Positive
    private BigDecimal quantity;

    @NotNull @PositiveOrZero
    private BigDecimal unitPrice;

    @PositiveOrZero
    private BigDecimal vatMultiplier;

    @NotNull @PositiveOrZero
    private BigDecimal totalPrice;


}
