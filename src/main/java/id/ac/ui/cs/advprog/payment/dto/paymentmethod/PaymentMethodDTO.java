package id.ac.ui.cs.advprog.payment.dto.paymentmethod;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Generated;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@Generated
@Data
public class PaymentMethodDTO {
    private UUID id;
    private String name;
    private String description;
    private BigDecimal processingFee;

    @JsonProperty("created_by")
    private UUID createdBy;

    @JsonProperty("created_at")
    private Date createdAt;

    @JsonProperty("updated_at")
    private Date updatedAt;

    @JsonProperty("deleted_at")
    private Timestamp deletedAt;

    private String paymentMethod;

}
