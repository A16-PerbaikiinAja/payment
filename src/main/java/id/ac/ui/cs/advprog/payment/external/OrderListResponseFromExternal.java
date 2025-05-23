package id.ac.ui.cs.advprog.payment.external;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderListResponseFromExternal {
    private List<OrderData> orders;
    private int count;
}