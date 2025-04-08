package id.ac.ui.cs.advprog.payment.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CODTest {

    private COD cod;
    private COD codCopy;
    private PaymentMethod paymentMethod;

    @BeforeEach
    void setUp() {
        paymentMethod = mock(PaymentMethod.class);

        cod = new COD(paymentMethod);
        cod.setPhoneNumber("123456789");
        cod.setInstructions("Leave at the door.");
    }

    @Test
    void testConstructorWithPaymentMethod() {
        assertNotNull(cod);
        assertEquals("123456789", cod.getPhoneNumber());
        assertEquals("Leave at the door.", cod.getInstructions());
    }

    @Test
    void testDefaultConstructor() {
        codCopy = new COD();
        assertNotNull(codCopy);
        assertNull(codCopy.getPhoneNumber());
        assertNull(codCopy.getInstructions());
    }

    @Test
    void testGettersAndSetters() {
        cod.setPhoneNumber("987654321");
        cod.setInstructions("Leave at the front desk.");

        assertEquals("987654321", cod.getPhoneNumber());
        assertEquals("Leave at the front desk.", cod.getInstructions());
    }

    @Test
    void testInheritanceFromPaymentMethod() {
        assertTrue(cod instanceof PaymentMethod);
    }

    @Test
    void testFieldValidation() {
        COD invalidCod = new COD();

        assertNull(invalidCod.getPhoneNumber());
        assertNull(invalidCod.getInstructions());

        invalidCod.setPhoneNumber("111222333");
        invalidCod.setInstructions("Please call before arrival");

        assertNotNull(invalidCod.getPhoneNumber());
        assertNotNull(invalidCod.getInstructions());
    }
}
