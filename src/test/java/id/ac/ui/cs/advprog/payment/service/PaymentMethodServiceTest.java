package id.ac.ui.cs.advprog.payment.service;

import id.ac.ui.cs.advprog.payment.dto.paymentmethod.PaymentMethodDTO;
import id.ac.ui.cs.advprog.payment.model.PaymentMethod;
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
    private PaymentMethodServiceImpl paymentMethodService;

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
        PaymentMethod paymentMethod = new PaymentMethod();
        when(paymentMethodRepository.save(any(PaymentMethod.class))).thenReturn(paymentMethod);
        when(paymentMethodDTO.getId()).thenReturn(id);
        when(paymentMethodDTO.getName()).thenReturn("Credit Card");

        PaymentMethodDTO result = paymentMethodService.createPaymentMethod(paymentMethodRegisterDTO);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Credit Card", result.getName());
    }

    @Test
    void testUpdatePaymentMethod() {
        PaymentMethod paymentMethod = new PaymentMethod();
        when(paymentMethodRepository.save(any(PaymentMethod.class))).thenReturn(paymentMethod);

        PaymentMethodDTO result = paymentMethodService.updatePaymentMethod(id, paymentMethodRegisterDTO);

        assertNotNull(result);
        verify(paymentMethodRepository, times(1)).save(any(PaymentMethod.class));
    }

    @Test
    void testFindAllPaymentMethod() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        PaymentMethod paymentMethod = new PaymentMethod();
        Page<PaymentMethod> paymentMethodPage = new PageImpl<>(Arrays.asList(paymentMethod));

        when(paymentMethodRepository.findAll(any(), eq(pageRequest))).thenReturn(paymentMethodPage);

        Page<PaymentMethodDTO> result = paymentMethodService.findAllPaymentMethod(0, 10, true, "Credit Card", "name", "asc");

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }

    @Test
    void testFindPaymentMethodById() {
        PaymentMethod paymentMethod = new PaymentMethod();
        when(paymentMethodRepository.findById(any(UUID.class))).thenReturn(java.util.Optional.of(paymentMethod));

        PaymentMethodDTO result = paymentMethodService.findPaymentMethodById(id.toString());

        assertNotNull(result);
        verify(paymentMethodRepository, times(1)).findById(any(UUID.class));
    }

    @Test
    void testDeletePaymentMethod() {
        Map<String, Object> responseMap = Map.of("message", "PaymentMethod deleted successfully");
        when(paymentMethodRepository.existsById(any(UUID.class))).thenReturn(true);

        Map<String, Object> result = paymentMethodService.deletePaymentMethod(id.toString());

        assertNotNull(result);
        assertEquals("PaymentMethod deleted successfully", result.get("message"));
    }
}
