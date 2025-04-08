package id.ac.ui.cs.advprog.payment.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EWalletTest {

    private EWallet eWallet;
    private EWallet eWalletCopy;
    private PaymentMethod paymentMethod;

    @BeforeEach
    void setUp() {
        paymentMethod = mock(PaymentMethod.class);

        eWallet = new EWallet(paymentMethod);
        eWallet.setAccountName("John Doe");
        eWallet.setVirtualAccountNumber("987654321");
        eWallet.setInstructions("Use this for online transactions.");
    }

    @Test
    void testConstructorWithPaymentMethod() {
        assertNotNull(eWallet);
        assertEquals("John Doe", eWallet.getAccountName());
        assertEquals("987654321", eWallet.getVirtualAccountNumber());
        assertEquals("Use this for online transactions.", eWallet.getInstructions());
    }

    @Test
    void testDefaultConstructor() {
        eWalletCopy = new EWallet();
        assertNotNull(eWalletCopy);
        assertNull(eWalletCopy.getAccountName());
        assertNull(eWalletCopy.getVirtualAccountNumber());
        assertNull(eWalletCopy.getInstructions());
    }

    @Test
    void testGettersAndSetters() {
        eWallet.setAccountName("Jane Smith");
        eWallet.setVirtualAccountNumber("123456789");
        eWallet.setInstructions("For payment transfers only.");

        assertEquals("Jane Smith", eWallet.getAccountName());
        assertEquals("123456789", eWallet.getVirtualAccountNumber());
        assertEquals("For payment transfers only.", eWallet.getInstructions());
    }

    @Test
    void testInheritanceFromPaymentMethod() {
        assertTrue(eWallet instanceof PaymentMethod);
    }

    @Test
    void testFieldValidation() {
        EWallet invalidEWallet = new EWallet();

        assertNull(invalidEWallet.getAccountName());
        assertNull(invalidEWallet.getVirtualAccountNumber());
        assertNull(invalidEWallet.getInstructions());

        invalidEWallet.setAccountName("Alice Johnson");
        invalidEWallet.setVirtualAccountNumber("111223344");
        invalidEWallet.setInstructions("Transfer for bill payments");

        assertNotNull(invalidEWallet.getAccountName());
        assertNotNull(invalidEWallet.getVirtualAccountNumber());
        assertNotNull(invalidEWallet.getInstructions());
    }
}
