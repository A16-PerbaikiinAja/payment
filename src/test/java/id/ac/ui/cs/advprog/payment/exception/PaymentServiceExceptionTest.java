package id.ac.ui.cs.advprog.payment.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PaymentServiceExceptionTest {

    @Test
    void testPaymentServiceExceptionWithMessage() {
        String message = "Payment service error occurred";

        PaymentServiceException exception = new PaymentServiceException(message);

        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testPaymentServiceExceptionWithMessageAndCause() {
        String message = "Payment service error occurred";
        Throwable cause = new RuntimeException("Root cause");

        PaymentServiceException exception = new PaymentServiceException(message, cause);

        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testPaymentServiceExceptionIsRuntimeException() {
        PaymentServiceException exception = new PaymentServiceException("Test message");

        assertTrue(exception instanceof RuntimeException);
    }
}

