package id.ac.ui.cs.advprog.payment.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "payment_methods")
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class PaymentMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "processing_fee")
    @DecimalMin(value = "0.0", inclusive = true, message = "processingFee must be at least 0")
    private BigDecimal processingFee;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", updatable = false, nullable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;

    @Column(name = "created_by")
    private UUID createdBy;

    public PaymentMethod(String name, String description, BigDecimal processingFee, UUID createdBy) {
        this.name = name;
        this.description = description;
        this.processingFee = processingFee;
        this.createdBy = createdBy;
    }
}
