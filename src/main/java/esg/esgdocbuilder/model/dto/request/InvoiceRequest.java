package esg.esgdocbuilder.model.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class InvoiceRequest {
    private BigDecimal power;

    @Valid
    @NotEmpty
    private List<InvoiceItemRequest> items;
}
