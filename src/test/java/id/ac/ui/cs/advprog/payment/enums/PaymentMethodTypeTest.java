package id.ac.ui.cs.advprog.payment.enums;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PaymentMethodTypeTest {

    @Test
    void testFromString_ValidTypes() {
        assertEquals(PaymentMethodType.COD, PaymentMethodType.fromString("COD"));
        assertEquals(PaymentMethodType.BANK_TRANSFER, PaymentMethodType.fromString("BANK_TRANSFER"));
        assertEquals(PaymentMethodType.E_WALLET, PaymentMethodType.fromString("E_WALLET"));
    }

    @Test
    void testFromString_InvalidType() {
        assertEquals(PaymentMethodType.INVALID, PaymentMethodType.fromString("INVALID_TYPE"));
    }

    @Test
    void testFromString_UpperCase() {
        assertEquals(PaymentMethodType.COD, PaymentMethodType.fromString("cod"));
        assertEquals(PaymentMethodType.BANK_TRANSFER, PaymentMethodType.fromString("bank_transfer"));
        assertEquals(PaymentMethodType.E_WALLET, PaymentMethodType.fromString("e_wallet"));
    }

}
