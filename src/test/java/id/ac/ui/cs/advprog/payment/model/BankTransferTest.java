package id.ac.ui.cs.advprog.payment.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BankTransferTest {

    private BankTransfer bankTransfer;
    private PaymentMethod paymentMethod;

    @BeforeEach
    void setUp() {
        paymentMethod = mock(PaymentMethod.class);
        bankTransfer = new BankTransfer(paymentMethod);
    }

    @Test
    void testConstructorWithPaymentMethod() {
        assertNotNull(bankTransfer);
        assertEquals(paymentMethod, bankTransfer.getPaymentMethod());
    }

    @Test
    void testDefaultConstructor() {
        bankTransfer = new BankTransfer();
        assertNotNull(bankTransfer);
        assertNull(bankTransfer.getAccountName());
        assertNull(bankTransfer.getAccountNumber());
        assertNull(bankTransfer.getBankName());
    }

    @Test
    void testGettersAndSetters() {
        bankTransfer.setAccountName("John Doe");
        bankTransfer.setAccountNumber("123456789");
        bankTransfer.setBankName("Bank ABC");

        assertEquals("John Doe", bankTransfer.getAccountName());
        assertEquals("123456789", bankTransfer.getAccountNumber());
        assertEquals("Bank ABC", bankTransfer.getBankName());
    }

    @Test
    void testInheritanceFromPaymentMethod() {
        assertTrue(bankTransfer instanceof PaymentMethod);
    }

    @Test
    void testSetterAndGetterMethods() {
        bankTransfer.setAccountName("Alice");
        bankTransfer.setAccountNumber("987654321");
        bankTransfer.setBankName("Bank XYZ");

        assertEquals("Alice", bankTransfer.getAccountName());
        assertEquals("987654321", bankTransfer.getAccountNumber());
        assertEquals("Bank XYZ", bankTransfer.getBankName());
    }
}
