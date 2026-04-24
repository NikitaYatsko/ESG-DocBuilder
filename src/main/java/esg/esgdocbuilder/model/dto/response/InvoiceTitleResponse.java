package esg.esgdocbuilder.model.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
public class InvoiceTitleResponse {
    private Long id;
    private String invoiceName;
    private BigDecimal power;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private BigDecimal totalAmount;
    private BigDecimal vat_amount;
    private BigDecimal sumMarginality;
    private BigDecimal discountPercent;
    private BigDecimal sum;
}
