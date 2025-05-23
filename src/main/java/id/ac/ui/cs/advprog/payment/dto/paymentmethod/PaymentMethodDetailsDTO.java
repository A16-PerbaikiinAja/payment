package id.ac.ui.cs.advprog.payment.dto.paymentmethod;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethodDetailsDTO {
    private String id;
    private String name;
    private String methodType;
    private String instructions;
    private int orderCount;
}