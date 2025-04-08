package id.ac.ui.cs.advprog.payment.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class PaymentMethodServiceTest {

    @InjectMocks
    private PaymentMethodService paymentMethodService;

    @Mock
    private PaymentMethodRepository paymentMethodRepository;

    @Mock
    private PaymentMethodDTO paymentMethodDTO;

    @Mock
    private PaymentMethodRegisterDTO paymentMethodRegisterDTO;

    private UUID id;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
    }

    @Test
    void testCreatePaymentMethod() {
    }

    @Test
    void testUpdatePaymentMethod() {
    }

    @Test
    void testFindAllPaymentMethod() {
    }

    @Test
    void testFindPaymentMethodById() {
    }

    @Test
    void testDeletePaymentMethod() {
    }
}
