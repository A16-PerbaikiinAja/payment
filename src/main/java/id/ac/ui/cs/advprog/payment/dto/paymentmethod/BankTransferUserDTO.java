package id.ac.ui.cs.advprog.payment.dto.paymentmethod;

import jakarta.validation.constraints.Digits;
import lombok.Data;
import lombok.Generated;

@Generated
@Data
public class BankTransferUserDTO extends PaymentMethodUserDTO {
    private String accountName;
    @Digits(integer = 20, fraction = 0, message = "Nomor rekening harus berupa angka tanpa desimal, maksimal 20 digit")
    private String accountNumber;
    private String bankName;
}