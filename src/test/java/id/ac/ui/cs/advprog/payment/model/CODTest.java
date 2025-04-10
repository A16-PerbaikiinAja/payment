package id.ac.ui.cs.advprog.payment.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

class CODTest {

    @Test
    void testCODConstructor() {
        UUID id = UUID.randomUUID();
        String name = "COD (Cash On Delivery)";
        String description = "Payment on delivery";
        BigDecimal processingFee = BigDecimal.valueOf(1.0);
        Timestamp createdAt = new Timestamp(System.currentTimeMillis());
        Timestamp updatedAt = new Timestamp(System.currentTimeMillis());
        Timestamp deletedAt = null;
        UUID createdBy = UUID.randomUUID();
        String phoneNumber = "081234567890";
        String instructions = "Payment will be made in cash on delivery.";

        COD cod = new COD();
        cod.setId(id);
        cod.setName(name);
        cod.setDescription(description);
        cod.setProcessingFee(processingFee);
        cod.setCreatedAt(createdAt);
        cod.setUpdatedAt(updatedAt);
        cod.setCreatedBy(createdBy);
        cod.setPhoneNumber(phoneNumber);
        cod.setInstructions(instructions);

        assertEquals(id, cod.getId());
        assertEquals(name, cod.getName());
        assertEquals(description, cod.getDescription());
        assertEquals(processingFee, cod.getProcessingFee());
        assertEquals(deletedAt, cod.getDeletedAt());
        assertEquals(createdAt, cod.getCreatedAt());
        assertEquals(updatedAt, cod.getUpdatedAt());
        assertEquals(createdBy, cod.getCreatedBy());
        assertEquals(phoneNumber, cod.getPhoneNumber());
        assertEquals(instructions, cod.getInstructions());
    }

    @Test
    void testCODNoArgsConstructor() {
        COD cod = new COD();

        assertNull(cod.getId());
        assertNull(cod.getName());
        assertNull(cod.getDescription());
        assertNull(cod.getProcessingFee());
        assertNull(cod.getDeletedAt());
        assertNull(cod.getCreatedAt());
        assertNull(cod.getUpdatedAt());
        assertNull(cod.getCreatedBy());
        assertNull(cod.getPhoneNumber());
        assertNull(cod.getInstructions());
    }

    @Test
    void testSetterAndGetter() {
        COD cod = new COD();

        UUID id = UUID.randomUUID();
        String name = "COD (Cash On Delivery)";
        String description = "Payment on delivery";
        BigDecimal processingFee = BigDecimal.valueOf(1.0);
        Timestamp createdAt = new Timestamp(System.currentTimeMillis());
        Timestamp updatedAt = new Timestamp(System.currentTimeMillis());
        Timestamp deletedAt = new Timestamp(System.currentTimeMillis());
        UUID createdBy = UUID.randomUUID();
        String phoneNumber = "081234567890";
        String instructions = "Payment will be made in cash on delivery.";

        cod.setId(id);
        cod.setName(name);
        cod.setDescription(description);
        cod.setProcessingFee(processingFee);
        cod.setCreatedAt(createdAt);
        cod.setUpdatedAt(updatedAt);
        cod.setDeletedAt(deletedAt);
        cod.setCreatedBy(createdBy);
        cod.setPhoneNumber(phoneNumber);
        cod.setInstructions(instructions);

        assertEquals(id, cod.getId());
        assertEquals(name, cod.getName());
        assertEquals(description, cod.getDescription());
        assertEquals(processingFee, cod.getProcessingFee());
        assertEquals(createdAt, cod.getCreatedAt());
        assertEquals(updatedAt, cod.getUpdatedAt());
        assertEquals(deletedAt, cod.getDeletedAt());
        assertEquals(createdBy, cod.getCreatedBy());
        assertEquals(phoneNumber, cod.getPhoneNumber());
        assertEquals(instructions, cod.getInstructions());
    }

    @Test
    void testCODEquality() {
        UUID id = UUID.randomUUID();
        String name = "COD (Cash On Delivery)";
        String description = "Payment on delivery";
        BigDecimal processingFee = BigDecimal.valueOf(1.0);
        Timestamp createdAt = new Timestamp(System.currentTimeMillis());
        Timestamp updatedAt = new Timestamp(System.currentTimeMillis());
        Timestamp deletedAt = null;
        UUID createdBy = UUID.randomUUID();
        String phoneNumber = "081234567890";
        String instructions = "Payment will be made in cash on delivery.";

        COD cod1 = new COD();
        cod1.setId(id);
        cod1.setName(name);
        cod1.setDescription(description);
        cod1.setProcessingFee(processingFee);
        cod1.setCreatedAt(createdAt);
        cod1.setUpdatedAt(updatedAt);
        cod1.setDeletedAt(deletedAt);
        cod1.setCreatedBy(createdBy);
        cod1.setPhoneNumber(phoneNumber);
        cod1.setInstructions(instructions);

        COD cod2 = new COD();
        cod2.setId(id);
        cod2.setName(name);
        cod2.setDescription(description);
        cod2.setProcessingFee(processingFee);
        cod2.setCreatedAt(createdAt);
        cod2.setUpdatedAt(updatedAt);
        cod2.setDeletedAt(deletedAt);
        cod2.setCreatedBy(createdBy);
        cod2.setPhoneNumber(phoneNumber);
        cod2.setInstructions(instructions);

        assertEquals(cod1, cod2);
    }

    @Test
    void testCODHashCode() {
        UUID id = UUID.randomUUID();
        String name = "COD (Cash On Delivery)";
        String description = "Payment on delivery";
        BigDecimal processingFee = BigDecimal.valueOf(1.0);
        Timestamp createdAt = new Timestamp(System.currentTimeMillis());
        Timestamp updatedAt = new Timestamp(System.currentTimeMillis());
        Timestamp deletedAt = null;
        UUID createdBy = UUID.randomUUID();
        String phoneNumber = "081234567890";
        String instructions = "Payment will be made in cash on delivery.";

        COD cod1 = new COD();
        cod1.setId(id);
        cod1.setName(name);
        cod1.setDescription(description);
        cod1.setProcessingFee(processingFee);
        cod1.setCreatedAt(createdAt);
        cod1.setUpdatedAt(updatedAt);
        cod1.setDeletedAt(deletedAt);
        cod1.setCreatedBy(createdBy);
        cod1.setPhoneNumber(phoneNumber);
        cod1.setInstructions(instructions);

        COD cod2 = new COD();
        cod2.setId(id);
        cod2.setName(name);
        cod2.setDescription(description);
        cod2.setProcessingFee(processingFee);
        cod2.setCreatedAt(createdAt);
        cod2.setUpdatedAt(updatedAt);
        cod2.setDeletedAt(deletedAt);
        cod2.setCreatedBy(createdBy);
        cod2.setPhoneNumber(phoneNumber);
        cod2.setInstructions(instructions);

        assertEquals(cod1.hashCode(), cod2.hashCode());
    }
}
