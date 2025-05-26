package id.ac.ui.cs.advprog.payment.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Digits;
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

    @Digits(integer = 20, fraction = 0, message = "Nomor rekening harus berupa angka tanpa desimal, maksimal 20 digit")
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
