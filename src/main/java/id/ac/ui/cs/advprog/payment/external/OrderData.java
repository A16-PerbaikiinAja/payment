package id.ac.ui.cs.advprog.payment.external;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderData {
    private UUID id;
    private UUID paymentMethodId;
     private String itemName;
}