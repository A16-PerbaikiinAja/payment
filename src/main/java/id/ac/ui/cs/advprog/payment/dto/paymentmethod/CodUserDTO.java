package id.ac.ui.cs.advprog.payment.dto.paymentmethod;

import jakarta.validation.constraints.Digits;
import lombok.Data;
import lombok.Generated;

@Generated
@Data
public class CodUserDTO extends PaymentMethodUserDTO {
    @Digits(integer = 15, fraction = 0, message = "Nomor telepon harus berupa angka tanpa desimal")
    private String phoneNumber;
    private String instructions;
}