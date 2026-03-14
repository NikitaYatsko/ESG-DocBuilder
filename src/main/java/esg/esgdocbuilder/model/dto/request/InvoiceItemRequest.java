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

    @DecimalMin("0.0")
    @DecimalMax("100.0")
    private BigDecimal vat = BigDecimal.ZERO;
}
