package id.ac.ui.cs.advprog.payment.dto.paymentmethod;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Setter
@Getter
public class PaymentMethodUserDTO {
        private UUID id;
        private String name;
        private String description;
        private BigDecimal processingFee;
        private String paymentMethod;
}
