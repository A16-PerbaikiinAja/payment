package id.ac.ui.cs.advprog.payment.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

class PaymentMethodTest {

    @Test
    void testPaymentMethodConstructor() {
        UUID id = UUID.randomUUID();
        String name = "Credit Card";
        String description = "Payment using credit card";
        BigDecimal processingFee = BigDecimal.valueOf(1.5);
        Timestamp createdAt = new Timestamp(System.currentTimeMillis());
        Timestamp updatedAt = new Timestamp(System.currentTimeMillis());
        Timestamp deletedAt = null;
        UUID createdBy = UUID.randomUUID();

        PaymentMethod paymentMethod = new PaymentMethod(id, name, description, processingFee, deletedAt, createdAt, updatedAt, createdBy);

        assertEquals(id, paymentMethod.getId());
        assertEquals(name, paymentMethod.getName());
        assertEquals(description, paymentMethod.getDescription());
        assertEquals(processingFee, paymentMethod.getProcessingFee());
        assertEquals(deletedAt, paymentMethod.getDeletedAt());
        assertEquals(createdAt, paymentMethod.getCreatedAt());
        assertEquals(updatedAt, paymentMethod.getUpdatedAt());
        assertEquals(createdBy, paymentMethod.getCreatedBy());
    }

    @Test
    void testPaymentMethodNoArgsConstructor() {
        PaymentMethod paymentMethod = new PaymentMethod();

        assertNull(paymentMethod.getId());
        assertNull(paymentMethod.getName());
        assertNull(paymentMethod.getDescription());
        assertNull(paymentMethod.getProcessingFee());
        assertNull(paymentMethod.getDeletedAt());
        assertNull(paymentMethod.getCreatedAt());
        assertNull(paymentMethod.getUpdatedAt());
        assertNull(paymentMethod.getCreatedBy());
    }

    @Test
    void testSetterAndGetter() {
        PaymentMethod paymentMethod = new PaymentMethod();

        UUID id = UUID.randomUUID();
        String name = "Debit Card";
        String description = "Payment using debit card";
        BigDecimal processingFee = BigDecimal.valueOf(0.5);
        Timestamp createdAt = new Timestamp(System.currentTimeMillis());
        Timestamp updatedAt = new Timestamp(System.currentTimeMillis());
        Timestamp deletedAt = new Timestamp(System.currentTimeMillis());
        UUID createdBy = UUID.randomUUID();

        paymentMethod.setId(id);
        paymentMethod.setName(name);
        paymentMethod.setDescription(description);
        paymentMethod.setProcessingFee(processingFee);
        paymentMethod.setCreatedAt(createdAt);
        paymentMethod.setUpdatedAt(updatedAt);
        paymentMethod.setDeletedAt(deletedAt);
        paymentMethod.setCreatedBy(createdBy);

        assertEquals(id, paymentMethod.getId());
        assertEquals(name, paymentMethod.getName());
        assertEquals(description, paymentMethod.getDescription());
        assertEquals(processingFee, paymentMethod.getProcessingFee());
        assertEquals(createdAt, paymentMethod.getCreatedAt());
        assertEquals(updatedAt, paymentMethod.getUpdatedAt());
        assertEquals(deletedAt, paymentMethod.getDeletedAt());
        assertEquals(createdBy, paymentMethod.getCreatedBy());
    }

    @Test
    void testPaymentMethodEquality() {
        UUID id = UUID.randomUUID();
        String name = "Credit Card";
        String description = "Payment using credit card";
        BigDecimal processingFee = BigDecimal.valueOf(1.5);
        Timestamp createdAt = new Timestamp(System.currentTimeMillis());
        Timestamp updatedAt = new Timestamp(System.currentTimeMillis());
        Timestamp deletedAt = null;
        UUID createdBy = UUID.randomUUID();

        PaymentMethod paymentMethod1 = new PaymentMethod(id, name, description, processingFee, deletedAt, createdAt, updatedAt, createdBy);
        PaymentMethod paymentMethod2 = new PaymentMethod(id, name, description, processingFee, deletedAt, createdAt, updatedAt, createdBy);

        assertEquals(paymentMethod1, paymentMethod2);
    }

    @Test
    void testPaymentMethodHashCode() {
        UUID id = UUID.randomUUID();
        String name = "Credit Card";
        String description = "Payment using credit card";
        BigDecimal processingFee = BigDecimal.valueOf(1.5);
        Timestamp createdAt = new Timestamp(System.currentTimeMillis());
        Timestamp updatedAt = new Timestamp(System.currentTimeMillis());
        Timestamp deletedAt = null;
        UUID createdBy = UUID.randomUUID();

        PaymentMethod paymentMethod1 = new PaymentMethod(id, name, description, processingFee, deletedAt, createdAt, updatedAt, createdBy);
        PaymentMethod paymentMethod2 = new PaymentMethod(id, name, description, processingFee, deletedAt, createdAt, updatedAt, createdBy);

        assertEquals(paymentMethod1.hashCode(), paymentMethod2.hashCode());
    }
}
