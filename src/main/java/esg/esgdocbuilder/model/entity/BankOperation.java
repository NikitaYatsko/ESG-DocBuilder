package esg.esgdocbuilder.model.entity;

import esg.esgdocbuilder.model.enums.TypeOfOperationEnums;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "operation", schema = "esgschema")
public class BankOperation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, columnDefinition = "operation_type")
    private TypeOfOperationEnums type;
    @Column(name = "amount", nullable = false)
    private BigDecimal amount;
    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;
    @Column(name = "comment")
    private String comment;
    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

}
