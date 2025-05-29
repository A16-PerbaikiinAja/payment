package id.ac.ui.cs.advprog.payment.exception;

public class PaymentServiceTimeoutException extends PaymentServiceException {

    public PaymentServiceTimeoutException(String message) {
        super(message);
    }

    public PaymentServiceTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }

    public PaymentServiceTimeoutException(Throwable cause) {
        super(String.valueOf(cause));
    }
}