package id.ac.ui.cs.advprog.payment.dto;

import lombok.Data;
import lombok.Generated;
import org.springframework.data.domain.Page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Generated
@Data
public class CustomPageResponse<T> {
    private List<T> paymentMethods;
    private Map<String, Object> pagination;

    public CustomPageResponse(Page<T> page) {
    }
}