package id.ac.ui.cs.advprog.payment.model;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Generated;

@Entity
@Table(name = "cod")
@PrimaryKeyJoinColumn(name = "id")
@Generated
@Data
public class COD extends PaymentMethod {
    private String phoneNumber;
    private String instructions;

    public COD(PaymentMethod paymentMethod) {
        super(paymentMethod);
    }

    public COD() {}
}