package id.ac.ui.cs.advprog.payment.dto;

import lombok.Data;
import lombok.Generated;

@Generated
@Data
public class Response {
    private String status;
    private String message;
    private Object data;

    public Response(String status, String message, Object data) {
    }
}