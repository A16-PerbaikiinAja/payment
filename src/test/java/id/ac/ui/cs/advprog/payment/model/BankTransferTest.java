package id.ac.ui.cs.advprog.payment.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

class BankTransferTest {

    @Test
    void testBankTransferConstructor() {
        UUID id = UUID.randomUUID();
        String name = "Bank Transfer";
        String description = "Payment through bank transfer";
        BigDecimal processingFee = BigDecimal.valueOf(2.0);
        Timestamp createdAt = new Timestamp(System.currentTimeMillis());
        Timestamp updatedAt = new Timestamp(System.currentTimeMillis());
        Timestamp deletedAt = null;
        UUID createdBy = UUID.randomUUID();
        String accountName = "John Doe";
        String accountNumber = "1234567890";
        String bankName = "Bank XYZ";

        BankTransfer bankTransfer = new BankTransfer();
        bankTransfer.setId(id);
        bankTransfer.setName(name);
        bankTransfer.setDescription(description);
        bankTransfer.setProcessingFee(processingFee);
        bankTransfer.setCreatedAt(createdAt);
        bankTransfer.setUpdatedAt(updatedAt);
        bankTransfer.setDeletedAt(deletedAt);
        bankTransfer.setCreatedBy(createdBy);
        bankTransfer.setAccountName(accountName);
        bankTransfer.setAccountNumber(accountNumber);
        bankTransfer.setBankName(bankName);

        assertEquals(id, bankTransfer.getId());
        assertEquals(name, bankTransfer.getName());
        assertEquals(description, bankTransfer.getDescription());
        assertEquals(processingFee, bankTransfer.getProcessingFee());
        assertEquals(deletedAt, bankTransfer.getDeletedAt());
        assertEquals(createdAt, bankTransfer.getCreatedAt());
        assertEquals(updatedAt, bankTransfer.getUpdatedAt());
        assertEquals(createdBy, bankTransfer.getCreatedBy());
        assertEquals(accountName, bankTransfer.getAccountName());
        assertEquals(accountNumber, bankTransfer.getAccountNumber());
        assertEquals(bankName, bankTransfer.getBankName());
    }

    @Test
    void testBankTransferNoArgsConstructor() {
        BankTransfer bankTransfer = new BankTransfer();

        assertNull(bankTransfer.getId());
        assertNull(bankTransfer.getName());
        assertNull(bankTransfer.getDescription());
        assertNull(bankTransfer.getProcessingFee());
        assertNull(bankTransfer.getDeletedAt());
        assertNull(bankTransfer.getCreatedAt());
        assertNull(bankTransfer.getUpdatedAt());
        assertNull(bankTransfer.getCreatedBy());
        assertNull(bankTransfer.getAccountName());
        assertNull(bankTransfer.getAccountNumber());
        assertNull(bankTransfer.getBankName());
    }

    @Test
    void testSetterAndGetter() {
        BankTransfer bankTransfer = new BankTransfer();

        UUID id = UUID.randomUUID();
        String name = "Bank Transfer";
        String description = "Payment through bank transfer";
        BigDecimal processingFee = BigDecimal.valueOf(2.0);
        Timestamp createdAt = new Timestamp(System.currentTimeMillis());
        Timestamp updatedAt = new Timestamp(System.currentTimeMillis());
        Timestamp deletedAt = new Timestamp(System.currentTimeMillis());
        UUID createdBy = UUID.randomUUID();
        String accountName = "John Doe";
        String accountNumber = "9876543210";
        String bankName = "Bank ABC";

        bankTransfer.setId(id);
        bankTransfer.setName(name);
        bankTransfer.setDescription(description);
        bankTransfer.setProcessingFee(processingFee);
        bankTransfer.setCreatedAt(createdAt);
        bankTransfer.setUpdatedAt(updatedAt);
        bankTransfer.setDeletedAt(deletedAt);
        bankTransfer.setCreatedBy(createdBy);
        bankTransfer.setAccountName(accountName);
        bankTransfer.setAccountNumber(accountNumber);
        bankTransfer.setBankName(bankName);

        assertEquals(id, bankTransfer.getId());
        assertEquals(name, bankTransfer.getName());
        assertEquals(description, bankTransfer.getDescription());
        assertEquals(processingFee, bankTransfer.getProcessingFee());
        assertEquals(createdAt, bankTransfer.getCreatedAt());
        assertEquals(updatedAt, bankTransfer.getUpdatedAt());
        assertEquals(deletedAt, bankTransfer.getDeletedAt());
        assertEquals(createdBy, bankTransfer.getCreatedBy());
        assertEquals(accountName, bankTransfer.getAccountName());
        assertEquals(accountNumber, bankTransfer.getAccountNumber());
        assertEquals(bankName, bankTransfer.getBankName());
    }

    @Test
    void testBankTransferEquality() {
        UUID id = UUID.randomUUID();
        String name = "Bank Transfer";
        String description = "Payment through bank transfer";
        BigDecimal processingFee = BigDecimal.valueOf(2.0);
        Timestamp createdAt = new Timestamp(System.currentTimeMillis());
        Timestamp updatedAt = new Timestamp(System.currentTimeMillis());
        Timestamp deletedAt = null;
        UUID createdBy = UUID.randomUUID();
        String accountName = "John Doe";
        String accountNumber = "1234567890";
        String bankName = "Bank XYZ";

        BankTransfer bankTransfer1 = new BankTransfer();
        bankTransfer1.setId(id);
        bankTransfer1.setName(name);
        bankTransfer1.setDescription(description);
        bankTransfer1.setProcessingFee(processingFee);
        bankTransfer1.setCreatedAt(createdAt);
        bankTransfer1.setUpdatedAt(updatedAt);
        bankTransfer1.setDeletedAt(deletedAt);
        bankTransfer1.setCreatedBy(createdBy);
        bankTransfer1.setAccountName(accountName);
        bankTransfer1.setAccountNumber(accountNumber);
        bankTransfer1.setBankName(bankName);

        BankTransfer bankTransfer2 = new BankTransfer();
        bankTransfer2.setId(id);
        bankTransfer2.setName(name);
        bankTransfer2.setDescription(description);
        bankTransfer2.setProcessingFee(processingFee);
        bankTransfer2.setCreatedAt(createdAt);
        bankTransfer2.setUpdatedAt(updatedAt);
        bankTransfer2.setDeletedAt(deletedAt);
        bankTransfer2.setCreatedBy(createdBy);
        bankTransfer2.setAccountName(accountName);
        bankTransfer2.setAccountNumber(accountNumber);
        bankTransfer2.setBankName(bankName);

        assertEquals(bankTransfer1, bankTransfer2);
    }

    @Test
    void testBankTransferHashCode() {
        UUID id = UUID.randomUUID();
        String name = "Bank Transfer";
        String description = "Payment through bank transfer";
        BigDecimal processingFee = BigDecimal.valueOf(2.0);
        Timestamp createdAt = new Timestamp(System.currentTimeMillis());
        Timestamp updatedAt = new Timestamp(System.currentTimeMillis());
        Timestamp deletedAt = null;
        UUID createdBy = UUID.randomUUID();
        String accountName = "John Doe";
        String accountNumber = "1234567890";
        String bankName = "Bank XYZ";

        BankTransfer bankTransfer1 = new BankTransfer();
        bankTransfer1.setId(id);
        bankTransfer1.setName(name);
        bankTransfer1.setDescription(description);
        bankTransfer1.setProcessingFee(processingFee);
        bankTransfer1.setCreatedAt(createdAt);
        bankTransfer1.setUpdatedAt(updatedAt);
        bankTransfer1.setDeletedAt(deletedAt);
        bankTransfer1.setCreatedBy(createdBy);
        bankTransfer1.setAccountName(accountName);
        bankTransfer1.setAccountNumber(accountNumber);
        bankTransfer1.setBankName(bankName);

        BankTransfer bankTransfer2 = new BankTransfer();
        bankTransfer2.setId(id);
        bankTransfer2.setName(name);
        bankTransfer2.setDescription(description);
        bankTransfer2.setProcessingFee(processingFee);
        bankTransfer2.setCreatedAt(createdAt);
        bankTransfer2.setUpdatedAt(updatedAt);
        bankTransfer2.setDeletedAt(deletedAt);
        bankTransfer2.setCreatedBy(createdBy);
        bankTransfer2.setAccountName(accountName);
        bankTransfer2.setAccountNumber(accountNumber);
        bankTransfer2.setBankName(bankName);

        assertEquals(bankTransfer1.hashCode(), bankTransfer2.hashCode());
    }
}
