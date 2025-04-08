package id.ac.ui.cs.advprog.payment.model;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Generated;

@Entity
@Table(name = "bank_transfer")
@PrimaryKeyJoinColumn(name = "id")
@Generated
@Data
public class BankTransfer extends PaymentMethod {

    public BankTransfer(PaymentMethod paymentMethod) {
    }

    public BankTransfer() {}
}