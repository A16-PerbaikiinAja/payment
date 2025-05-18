package id.ac.ui.cs.advprog.payment.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "cod")
@PrimaryKeyJoinColumn(name = "id")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class COD extends PaymentMethod {

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "instructions", columnDefinition = "TEXT")
    private String instructions;

    public COD(PaymentMethod paymentMethod) {
        super(
                paymentMethod.getName(),
                paymentMethod.getDescription(),
                paymentMethod.getProcessingFee(),
                paymentMethod.getCreatedBy()
        );
    }
}
