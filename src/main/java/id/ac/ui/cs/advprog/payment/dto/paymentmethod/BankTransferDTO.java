package id.ac.ui.cs.advprog.payment.dto.paymentmethod;

import lombok.Data;
import lombok.Generated;

@Generated
@Data
public class BankTransferDTO extends PaymentMethodDTO {
    private String accountName;
    private String accountNumber;
    private String bankName;
}