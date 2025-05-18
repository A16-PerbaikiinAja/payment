package id.ac.ui.cs.advprog.payment.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "e_wallet")
@PrimaryKeyJoinColumn(name = "id")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class EWallet extends PaymentMethod {

    @Column(name = "account_name", nullable = false)
    private String accountName;

    @Column(name = "virtual_account_number", nullable = false)
    private String virtualAccountNumber;

    @Column(name = "instructions", columnDefinition = "TEXT")
    private String instructions;

    public EWallet(PaymentMethod paymentMethod) {
        super(
                paymentMethod.getName(),
                paymentMethod.getDescription(),
                paymentMethod.getProcessingFee(),
                paymentMethod.getCreatedBy()
        );
    }
}
