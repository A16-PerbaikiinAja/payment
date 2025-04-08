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
        this.paymentMethods = page.getContent();
        this.pagination = new HashMap<>();
        this.pagination.put("totalItems", page.getTotalElements());
        this.pagination.put("totalPages", page.getTotalPages());
        this.pagination.put("currentPage", page.getNumber());
        this.pagination.put("limit", page.getSize());
    }
}