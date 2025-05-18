package id.ac.ui.cs.advprog.payment.dto.paymentmethod;

import lombok.Data;
import lombok.Generated;

@Generated
@Data
public class BankTransferUserDTO extends PaymentMethodUserDTO {
    private String accountName;
    private String accountNumber;
    private String bankName;
}