package esg.esgdocbuilder.model.entity;

import esg.esgdocbuilder.model.enums.TypeOfOperationEnums;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "operation", schema = "esgschema")
@Data
public class BankOperation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TypeOfOperationEnums type;
    @Column(name = "amount", nullable = false)
    private BigDecimal amount;
    @Column(name = "comment")
    private String comment;
    @Column(name = "created_at")
    private LocalDateTime createdAt;

}
