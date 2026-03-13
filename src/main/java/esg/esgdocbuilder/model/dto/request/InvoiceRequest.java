package esg.esgdocbuilder.model.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class InvoiceRequest {
    @Valid
    @NotEmpty
    private List<InvoiceItemRequest> items;
}
