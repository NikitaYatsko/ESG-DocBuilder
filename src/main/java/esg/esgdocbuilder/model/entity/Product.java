package esg.esgdocbuilder.model.entity;

import esg.esgdocbuilder.model.enums.TypeOfUnitEnum;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "products", schema = "esg-schema")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    // Себестоимость
    @Column(name = "cost_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal purchasePrice;

    // Наценка %
    @Column(name = "markup_percent", nullable = false, precision = 6, scale = 2)
    private BigDecimal markupPercent;

    // Есть ли НДС
    @Column(name = "has_vat", nullable = false)
    private Boolean hasVat;

    // Единица измерения
    @Enumerated(EnumType.STRING)
    @Column(name = "type_of_unit", nullable = false)
    private TypeOfUnitEnum typeOfUnit;

    // Категория
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
