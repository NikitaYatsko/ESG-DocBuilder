package esg.esgdocbuilder.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NonNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "invoice",schema = "esgschema")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "invoice_name",nullable = false)
    private String invoiceName;

    @Column(name = "power_kwt")
    private BigDecimal powerKwt;

    @Column(name = "vat_amount", nullable = false)
    private BigDecimal vatAmount;

    @Column(name = "sum_amount", nullable = false)
    private BigDecimal sumAmount;

    @Column(name = "discount_percent", nullable = false)
    private BigDecimal discountPercent = BigDecimal.ZERO;

    @Column(name = "sum_marginality", nullable = false)
    private BigDecimal sumMarginality;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvoiceItem> invoiceItems = new ArrayList<>();
}
