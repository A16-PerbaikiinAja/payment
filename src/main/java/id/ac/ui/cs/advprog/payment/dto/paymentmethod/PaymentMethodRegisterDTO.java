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
    private String description;

    @NotBlank(message = "processingFee must not be blank")
    @NotNull(message = "processingFee must not be null")
    private BigDecimal processingFee;

    @NotBlank(message = "CreatedBy must not be blank")
    @NotNull(message = "CreatedBy must not be null")
    @JsonProperty("created_by")
    private String createdBy;

    private String paymentMethod;
    private String accountName;
    private String accountNumber;
    private String bankName;
    private String virtualAccountNumber;
    private String phoneNumber;
    private String instructions;
    private String status;
}