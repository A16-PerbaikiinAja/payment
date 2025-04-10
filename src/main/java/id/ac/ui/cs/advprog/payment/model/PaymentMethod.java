package id.ac.ui.cs.advprog.payment.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta. persistence. Id;
import jakarta. persistence. GeneratedValue;
import jakarta. persistence. GenerationType;
import jakarta. persistence. Column;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "payment_methods")
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PaymentMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "processing_fee")
    private BigDecimal processingFee;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at", nullable = false)
    private Timestamp updatedAt;

    @Column(name = "created_by", nullable = false)
    private UUID createdBy;

    public PaymentMethod(String name, String description, BigDecimal processingFee, Timestamp createdAt, Timestamp updatedAt, UUID createdBy) {
        this.name = name;
        this.description = description;
        this.processingFee = processingFee;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
    }
}
