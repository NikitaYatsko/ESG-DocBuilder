package esg.esgdocbuilder.model.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class InvoiceRequest {
    @NotBlank(message = "Название сметы обязательно")
    private String invoiceName;

    private BigDecimal power;
    @PositiveOrZero
    private BigDecimal vat_amount;

    @PositiveOrZero
    private BigDecimal discountPercent;

    @PositiveOrZero
    private BigDecimal sumMarginality;

    @PositiveOrZero
    private BigDecimal sum;

    @Valid
    private List<InvoiceItemRequest> items;
}
