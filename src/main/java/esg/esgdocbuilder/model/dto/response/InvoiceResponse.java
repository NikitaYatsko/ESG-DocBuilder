package esg.esgdocbuilder.model.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class InvoiceResponse {
    private Long id;
    private String invoiceName;
    private BigDecimal power;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<InvoiceItemResponse> items;
    private BigDecimal totalAmount;
    private BigDecimal discountPercent;
    private BigDecimal vat_amount;
    private BigDecimal sumMarginality;
    private BigDecimal sum;
}
