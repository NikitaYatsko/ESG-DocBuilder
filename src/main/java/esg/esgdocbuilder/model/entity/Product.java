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
    @Column(name = "general_data", nullable = false)
    private String generalData;
    @Column(name = "purchase_price")
    private BigDecimal purchasePrice;
    @Column(name = "sell_price")
    private BigDecimal sellPrice;
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    @Enumerated(EnumType.STRING)
    @Column(name = "type_of_unit", nullable = false)
    private TypeOfUnitEnum typeOfUnit;
}
