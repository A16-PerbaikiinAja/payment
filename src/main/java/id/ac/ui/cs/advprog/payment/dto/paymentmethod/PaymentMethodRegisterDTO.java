package id.ac.ui.cs.advprog.payment.dto.paymentmethod;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.Generated;

import java.math.BigDecimal;

@Generated
@Data
public class PaymentMethodRegisterDTO {

    @NotBlank(message = "Name must not be blank")
    @NotNull(message = "Name must not be null")
    private String name;

    @NotNull(message = "Payment method type must not be null")
    @NotBlank(message = "Payment method type must not be blank")
    @Pattern(
            regexp = "COD|BANK_TRANSFER|E_WALLET",
            message = "Payment method type must be one of: COD, BANK_TRANSFER, E_WALLET"
    )
    @JsonProperty("payment_method")
    private String paymentMethod;

    @JsonProperty("description")
    private String description;

    @JsonProperty("processing_fee")
    @NotNull(message = "processingFee must not be null")
    private BigDecimal processingFee;

//    @NotBlank(message = "CreatedBy must not be blank")
//    @NotNull(message = "CreatedBy must not be null")

    @JsonProperty("created_by")
    private String createdBy;

    @JsonProperty("account_name")
    private String accountName;
    @JsonProperty("account_number")
    private String accountNumber;
    @JsonProperty("bank_name")
    private String bankName;
    @JsonProperty("virtual_account_number")
    private String virtualAccountNumber;
    @JsonProperty("phone_number")
    private String phoneNumber;
    @JsonProperty("instructions")
    private String instructions;

}
