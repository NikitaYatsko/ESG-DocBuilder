package esg.esgdocbuilder.model.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class InvoiceItemResponse {
    private Long id;
    private String nameProduct;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private BigDecimal vatMultiplier;
    private BigDecimal totalPrice;
}
