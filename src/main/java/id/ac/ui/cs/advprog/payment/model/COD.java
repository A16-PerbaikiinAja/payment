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

    public COD(PaymentMethod paymentMethod) {
    }

    public COD() {}
}