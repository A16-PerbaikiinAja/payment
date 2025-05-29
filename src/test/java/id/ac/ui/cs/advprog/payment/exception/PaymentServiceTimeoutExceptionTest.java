package id.ac.ui.cs.advprog.payment.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PaymentServiceTimeoutExceptionTest {

    @Test
    void testPaymentServiceTimeoutExceptionWithMessage() {
        String message = "Payment service timeout occurred";

        PaymentServiceTimeoutException exception = new PaymentServiceTimeoutException(message);

        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testPaymentServiceTimeoutExceptionWithMessageAndCause() {
        String message = "Payment service timeout occurred";
        Throwable cause = new RuntimeException("Connection timeout");

        PaymentServiceTimeoutException exception = new PaymentServiceTimeoutException(message, cause);

        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testPaymentServiceTimeoutExceptionIsRuntimeException() {
        PaymentServiceTimeoutException exception = new PaymentServiceTimeoutException("Test message");

        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void testPaymentServiceTimeoutExceptionCanBeThrown() {
        String message = "Service timeout";

        assertThrows(PaymentServiceTimeoutException.class, () -> {
            throw new PaymentServiceTimeoutException(message);
        });
    }

    @Test
    void testPaymentServiceTimeoutExceptionCanBeThrownWithCause() {
        String message = "Service timeout";
        Throwable cause = new RuntimeException("Network error");

        PaymentServiceTimeoutException thrown = assertThrows(PaymentServiceTimeoutException.class, () -> {
            throw new PaymentServiceTimeoutException(message, cause);
        });

        assertEquals(message, thrown.getMessage());
        assertEquals(cause, thrown.getCause());
    }
}