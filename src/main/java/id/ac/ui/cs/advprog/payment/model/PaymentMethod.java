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

    public PaymentMethod(PaymentMethod other) {
    }

    public PaymentMethod() {}
}