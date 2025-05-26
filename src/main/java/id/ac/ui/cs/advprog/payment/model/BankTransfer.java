package id.ac.ui.cs.advprog.payment.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Digits;
import lombok.*;

@Entity
@Table(name = "bank_transfer")
@PrimaryKeyJoinColumn(name = "id")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class BankTransfer extends PaymentMethod {

    @Column(name = "account_name", nullable = false)
    private String accountName;

    @Digits(integer = 20, fraction = 0, message = "Nomor rekening harus berupa angka tanpa desimal, maksimal 20 digit")
    @Column(name = "account_number", nullable = false)
    private String accountNumber;

    @Column(name = "bank_name", nullable = false)
    private String bankName;

    public BankTransfer(PaymentMethod paymentMethod) {
        super(
                paymentMethod.getName(),
                paymentMethod.getDescription(),
                paymentMethod.getProcessingFee(),
                paymentMethod.getCreatedBy()
        );
    }
}
