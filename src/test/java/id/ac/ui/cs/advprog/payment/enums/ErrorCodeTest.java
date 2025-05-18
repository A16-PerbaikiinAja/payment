package id.ac.ui.cs.advprog.payment.enums;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class ErrorCodeTest {

    @Test
    void enumValuesAndGetters() {
        ErrorCode code = ErrorCode.GENERAL_ERROR;
        assertEquals(HttpStatus.CONFLICT, code.getStatus());
        assertEquals("Conflict", code.getReasonPhrase());
        assertEquals("General Error", code.getMessage());
    }

    @Test
    void specificValues() {
        assertEquals(409, ErrorCode.GENERAL_ERROR.getStatus().value());
        assertEquals("Not Found", ErrorCode.NOT_FOUND.getReasonPhrase());
        assertEquals("Authentication required", ErrorCode.UNAUTHORIZED.getMessage());
    }
}
