package id.ac.ui.cs.advprog.payment.dto.paymentmethod;

import jakarta.validation.constraints.Digits;
import lombok.Data;
import lombok.Generated;

@Generated
@Data
public class EWalletUserDTO extends PaymentMethodUserDTO {
    private String accountName;
    @Digits(integer = 20, fraction = 0, message = "Virtual Account Number harus berupa angka tanpa desimal")
    private String virtualAccountNumber;
    private String instructions;
}