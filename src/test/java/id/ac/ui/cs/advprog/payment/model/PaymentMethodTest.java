package id.ac.ui.cs.advprog.payment.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

class PaymentMethodTest {

    private PaymentMethod paymentMethod;
    private PaymentMethod paymentMethodCopy;

    @BeforeEach
    void setUp() {
        User mockUser = mock(User.class);

        paymentMethod = new PaymentMethod();
        paymentMethod.setId(UUID.randomUUID());
        paymentMethod.setName("Credit Card");
        paymentMethod.setDescription("Payment via credit card");
        paymentMethod.setProcessingFee(new BigDecimal("2.5"));
        paymentMethod.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        paymentMethod.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        paymentMethod.setCreatedBy(mockUser);
    }

    @Test
    void testConstructorWithAnotherPaymentMethod() {
        paymentMethodCopy = new PaymentMethod(paymentMethod);

        assertEquals(paymentMethod.getName(), paymentMethodCopy.getName());
        assertEquals(paymentMethod.getDescription(), paymentMethodCopy.getDescription());
        assertEquals(paymentMethod.getProcessingFee(), paymentMethodCopy.getProcessingFee());
        assertEquals(paymentMethod.getCreatedAt(), paymentMethodCopy.getCreatedAt());
        assertEquals(paymentMethod.getUpdatedAt(), paymentMethodCopy.getUpdatedAt());
        assertEquals(paymentMethod.getCreatedBy(), paymentMethodCopy.getCreatedBy());
    }

    @Test
    void testDefaultConstructor() {
        paymentMethodCopy = new PaymentMethod();
        assertNotNull(paymentMethodCopy);
        assertNull(paymentMethodCopy.getName());
        assertNull(paymentMethodCopy.getDescription());
        assertNull(paymentMethodCopy.getProcessingFee());
        assertNull(paymentMethodCopy.getCreatedAt());
        assertNull(paymentMethodCopy.getUpdatedAt());
        assertNull(paymentMethodCopy.getCreatedBy());
    }

    @Test
    void testGettersAndSetters() {
        paymentMethod.setName("Debit Card");
        paymentMethod.setDescription("Payment via debit card");
        paymentMethod.setProcessingFee(new BigDecimal("1.5"));
        paymentMethod.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        paymentMethod.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        paymentMethod.setCreatedBy(new User());
        assertEquals("Debit Card", paymentMethod.getName());
        assertEquals("Payment via debit card", paymentMethod.getDescription());
        assertEquals(new BigDecimal("1.5"), paymentMethod.getProcessingFee());
        assertNotNull(paymentMethod.getCreatedAt());
        assertNotNull(paymentMethod.getUpdatedAt());
        assertNotNull(paymentMethod.getCreatedBy());
    }

    @Test
    void testIdGeneration() {
        assertNotNull(paymentMethod.getId());
    }

    @Test
    void testFieldValidation() {
        PaymentMethod invalidPaymentMethod = new PaymentMethod();
        assertNull(invalidPaymentMethod.getName());
        assertNull(invalidPaymentMethod.getDescription());

        invalidPaymentMethod.setName("Bank Transfer");
        invalidPaymentMethod.setDescription("Bank account transfer");

        assertNotNull(invalidPaymentMethod.getName());
        assertNotNull(invalidPaymentMethod.getDescription());
    }
}
