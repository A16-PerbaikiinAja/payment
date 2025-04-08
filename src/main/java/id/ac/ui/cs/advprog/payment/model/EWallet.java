package id.ac.ui.cs.advprog.payment.model;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Generated;

@Entity
@Table(name = "e_wallet")
@PrimaryKeyJoinColumn(name = "id")
@Generated
@Data
public class EWallet extends PaymentMethod {
    private String accountName;
    private String virtualAccountNumber;
    private String instructions;

    public EWallet(PaymentMethod paymentMethod) {
        super(paymentMethod);
    }

    public EWallet() {}
}