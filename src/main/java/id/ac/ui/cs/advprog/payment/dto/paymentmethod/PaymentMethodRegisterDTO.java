package id.ac.ui.cs.advprog.payment.dto.paymentmethod;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Generated;

import java.math.BigDecimal;

@Generated
@Data
public class PaymentMethodRegisterDTO {
    private String name;
    private String description;
    private BigDecimal processingFee;
    @JsonProperty("created_by")
    private String createdBy;
    private String paymentMethod;
    private String type;
    private String accountName;
    private String accountNumber;
    private String bankName;
    private String virtualAccountNumber;
    private String phoneNumber;
    private String instructions;
}
