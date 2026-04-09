package esg.esgdocbuilder.model.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class InvoiceRequest {
    @NotBlank(message = "Название сметы обязательно")
    private String invoiceName;

    private BigDecimal power;

    @Valid
    @NotEmpty
    private List<InvoiceItemRequest> items;
}
