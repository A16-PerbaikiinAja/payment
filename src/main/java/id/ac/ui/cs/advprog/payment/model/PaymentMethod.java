package id.ac.ui.cs.advprog.payment.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Generated;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "payment_methods")
@Inheritance(strategy = InheritanceType.JOINED)
@Generated
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "created_by", referencedColumnName = "id", nullable = false)
    private User createdBy;

    public PaymentMethod(PaymentMethod other) {
        this.name = other.name;
        this.description = other.description;
        this.processingFee = other.processingFee;
        this.createdAt = other.createdAt;
        this.updatedAt = other.updatedAt;
        this.createdBy = other.createdBy;
    }

    public PaymentMethod() {}
}