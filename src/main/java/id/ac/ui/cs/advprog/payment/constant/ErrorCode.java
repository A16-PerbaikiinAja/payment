package id.ac.ui.cs.advprog.payment.constant;

import id.ac.ui.cs.advprog.payment.dto.exception.ErrorResponse;
import org.springframework.http.HttpStatus;

public enum ErrorCode {
    GENERAL_ERROR(HttpStatus.CONFLICT, "Conflict", "General Error"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "Not Found", "The resource was not found"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Unauthorized", "Authentication required"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "Bad Request", "Invalid request parameters");

    private final HttpStatus status;
    private final String reasonPhrase;
    private final String message;

    ErrorCode(HttpStatus status, String reasonPhrase, String message) {
    }

    public HttpStatus getStatus() {
    }

    public String getReasonPhrase() {
    }

    public String getMessage() {
    }

    public ErrorResponse toErrorResponse() {
        );
    }
}