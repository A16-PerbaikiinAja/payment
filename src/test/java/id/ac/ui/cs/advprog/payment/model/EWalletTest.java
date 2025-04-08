package id.ac.ui.cs.advprog.payment.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

class EWalletTest {

    @Test
    void testEWalletConstructor() {
        UUID id = UUID.randomUUID();
        String name = "E-Wallet";
        String description = "Payment via E-wallet";
        BigDecimal processingFee = BigDecimal.valueOf(3.0);
        Timestamp createdAt = new Timestamp(System.currentTimeMillis());
        Timestamp updatedAt = new Timestamp(System.currentTimeMillis());
        Timestamp deletedAt = null;
        UUID createdBy = UUID.randomUUID();
        String accountName = "John E-Wallet";
        String virtualAccountNumber = "987654321";
        String instructions = "Payment will be processed instantly";

        EWallet eWallet = new EWallet();
        eWallet.setId(id);
        eWallet.setName(name);
        eWallet.setDescription(description);
        eWallet.setProcessingFee(processingFee);
        eWallet.setCreatedAt(createdAt);
        eWallet.setUpdatedAt(updatedAt);
        eWallet.setCreatedBy(createdBy);
        eWallet.setAccountName(accountName);
        eWallet.setVirtualAccountNumber(virtualAccountNumber);
        eWallet.setInstructions(instructions);


        assertEquals(id, eWallet.getId());
        assertEquals(name, eWallet.getName());
        assertEquals(description, eWallet.getDescription());
        assertEquals(processingFee, eWallet.getProcessingFee());
        assertEquals(deletedAt, eWallet.getDeletedAt());
        assertEquals(createdAt, eWallet.getCreatedAt());
        assertEquals(updatedAt, eWallet.getUpdatedAt());
        assertEquals(createdBy, eWallet.getCreatedBy());
        assertEquals(accountName, eWallet.getAccountName());
        assertEquals(virtualAccountNumber, eWallet.getVirtualAccountNumber());
        assertEquals(instructions, eWallet.getInstructions());
    }

    @Test
    void testEWalletNoArgsConstructor() {
        EWallet eWallet = new EWallet();

        assertNull(eWallet.getId());
        assertNull(eWallet.getName());
        assertNull(eWallet.getDescription());
        assertNull(eWallet.getProcessingFee());
        assertNull(eWallet.getDeletedAt());
        assertNull(eWallet.getCreatedAt());
        assertNull(eWallet.getUpdatedAt());
        assertNull(eWallet.getCreatedBy());
        assertNull(eWallet.getAccountName());
        assertNull(eWallet.getVirtualAccountNumber());
        assertNull(eWallet.getInstructions());
    }

    @Test
    void testSetterAndGetter() {
        EWallet eWallet = new EWallet();

        UUID id = UUID.randomUUID();
        String name = "E-Wallet";
        String description = "Payment via E-wallet";
        BigDecimal processingFee = BigDecimal.valueOf(3.0);
        Timestamp createdAt = new Timestamp(System.currentTimeMillis());
        Timestamp updatedAt = new Timestamp(System.currentTimeMillis());
        Timestamp deletedAt = new Timestamp(System.currentTimeMillis());
        UUID createdBy = UUID.randomUUID();
        String accountName = "John E-Wallet";
        String virtualAccountNumber = "987654321";
        String instructions = "Payment will be processed instantly";

        eWallet.setId(id);
        eWallet.setName(name);
        eWallet.setDescription(description);
        eWallet.setProcessingFee(processingFee);
        eWallet.setCreatedAt(createdAt);
        eWallet.setUpdatedAt(updatedAt);
        eWallet.setDeletedAt(deletedAt);
        eWallet.setCreatedBy(createdBy);
        eWallet.setAccountName(accountName);
        eWallet.setVirtualAccountNumber(virtualAccountNumber);
        eWallet.setInstructions(instructions);

        assertEquals(id, eWallet.getId());
        assertEquals(name, eWallet.getName());
        assertEquals(description, eWallet.getDescription());
        assertEquals(processingFee, eWallet.getProcessingFee());
        assertEquals(createdAt, eWallet.getCreatedAt());
        assertEquals(updatedAt, eWallet.getUpdatedAt());
        assertEquals(deletedAt, eWallet.getDeletedAt());
        assertEquals(createdBy, eWallet.getCreatedBy());
        assertEquals(accountName, eWallet.getAccountName());
        assertEquals(virtualAccountNumber, eWallet.getVirtualAccountNumber());
        assertEquals(instructions, eWallet.getInstructions());
    }

    @Test
    void testEWalletEquality() {
        UUID id = UUID.randomUUID();
        String name = "E-Wallet";
        String description = "Payment via E-wallet";
        BigDecimal processingFee = BigDecimal.valueOf(3.0);
        Timestamp createdAt = new Timestamp(System.currentTimeMillis());
        Timestamp updatedAt = new Timestamp(System.currentTimeMillis());
        Timestamp deletedAt = null;
        UUID createdBy = UUID.randomUUID();
        String accountName = "John E-Wallet";
        String virtualAccountNumber = "987654321";
        String instructions = "Payment will be processed instantly.";

        EWallet eWallet1 = new EWallet();
        eWallet1.setId(id);
        eWallet1.setName(name);
        eWallet1.setDescription(description);
        eWallet1.setProcessingFee(processingFee);
        eWallet1.setCreatedAt(createdAt);
        eWallet1.setUpdatedAt(updatedAt);
        eWallet1.setCreatedBy(createdBy);
        eWallet1.setAccountName(accountName);
        eWallet1.setVirtualAccountNumber(virtualAccountNumber);
        eWallet1.setInstructions(instructions);

        EWallet eWallet2 = new EWallet();
        eWallet2.setId(id);
        eWallet2.setName(name);
        eWallet2.setDescription(description);
        eWallet2.setProcessingFee(processingFee);
        eWallet2.setCreatedAt(createdAt);
        eWallet2.setUpdatedAt(updatedAt);
        eWallet2.setCreatedBy(createdBy);
        eWallet2.setAccountName(accountName);
        eWallet2.setVirtualAccountNumber(virtualAccountNumber);
        eWallet2.setInstructions(instructions);

        assertEquals(eWallet1, eWallet2);
    }

    @Test
    void testEWalletHashCode() {
        UUID id = UUID.randomUUID();
        String name = "E-Wallet";
        String description = "Payment via E-wallet";
        BigDecimal processingFee = BigDecimal.valueOf(3.0);
        Timestamp createdAt = new Timestamp(System.currentTimeMillis());
        Timestamp updatedAt = new Timestamp(System.currentTimeMillis());
        Timestamp deletedAt = null;
        UUID createdBy = UUID.randomUUID();
        String accountName = "John E-Wallet";
        String virtualAccountNumber = "987654321";
        String instructions = "Payment will be processed instantly.";

        EWallet eWallet1 = new EWallet();
        eWallet1.setId(id);
        eWallet1.setName(name);
        eWallet1.setDescription(description);
        eWallet1.setProcessingFee(processingFee);
        eWallet1.setCreatedAt(createdAt);
        eWallet1.setUpdatedAt(updatedAt);
        eWallet1.setCreatedBy(createdBy);
        eWallet1.setAccountName(accountName);
        eWallet1.setVirtualAccountNumber(virtualAccountNumber);
        eWallet1.setInstructions(instructions);

        EWallet eWallet2 = new EWallet();
        eWallet2.setId(id);
        eWallet2.setName(name);
        eWallet2.setDescription(description);
        eWallet2.setProcessingFee(processingFee);
        eWallet2.setCreatedAt(createdAt);
        eWallet2.setUpdatedAt(updatedAt);
        eWallet2.setCreatedBy(createdBy);
        eWallet2.setAccountName(accountName);
        eWallet2.setVirtualAccountNumber(virtualAccountNumber);
        eWallet2.setInstructions(instructions);

        assertEquals(eWallet1.hashCode(), eWallet2.hashCode());
    }
}
