package id.ac.ui.cs.advprog.payment.dto;

import lombok.Data;
import lombok.Generated;

@Generated
@Data
public class EWalletDTO extends PaymentMethodDTO {
    private String accountName;
    private String virtualAccountNumber;
    private String instructions;
}