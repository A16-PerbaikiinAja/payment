package id.ac.ui.cs.advprog.payment.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class PaymentMethodServiceImplTest {

    @InjectMocks
    private PaymentMethodServiceImpl paymentMethodService;

    @Mock
    private PaymentMethodRepository paymentMethodRepository;

    @Mock
    private AuthService authService;

    @Mock
    private EntityManager entityManager;

    private PaymentMethodRegisterDTO paymentMethodRegisterDTO;
    private UUID userId;
    private UUID paymentMethodId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        paymentMethodId = UUID.randomUUID();
        paymentMethodRegisterDTO = new PaymentMethodRegisterDTO();
        paymentMethodRegisterDTO.setName("Credit Card");
        paymentMethodRegisterDTO.setDescription("Payment via Credit Card");
        paymentMethodRegisterDTO.setProcessingFee(1.5);
        paymentMethodRegisterDTO.setCreatedBy(userId.toString());
    }

    @Test
    void testCreatePaymentMethod() {
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setId(paymentMethodId);
        when(authService.findUserById(userId)).thenReturn(null);

        when(paymentMethodRepository.save(any(PaymentMethod.class))).thenReturn(paymentMethod);

        PaymentMethodDTO result = paymentMethodService.createPaymentMethod(paymentMethodRegisterDTO);

        assertNotNull(result);
        assertEquals(paymentMethodId, result.getId());
        verify(paymentMethodRepository, times(1)).save(any(PaymentMethod.class));
    }

    @Test
    void testFindPaymentMethodById() {
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setId(paymentMethodId);
        when(paymentMethodRepository.findById(paymentMethodId)).thenReturn(Optional.of(paymentMethod));

        PaymentMethodDTO result = paymentMethodService.findPaymentMethodById(paymentMethodId.toString());

        assertNotNull(result);
        assertEquals(paymentMethodId, result.getId());
        verify(paymentMethodRepository, times(1)).findById(paymentMethodId);
    }

    @Test
    void testFindPaymentMethodById_NotFound() {
        when(paymentMethodRepository.findById(paymentMethodId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> paymentMethodService.findPaymentMethodById(paymentMethodId.toString()));
    }

    @Test
    void testFindAllPaymentMethod() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<PaymentMethod> page = mock(Page.class);
        when(paymentMethodRepository.findAll(any(Specification.class), eq(pageRequest))).thenReturn(page);

        Page<PaymentMethodDTO> result = paymentMethodService.findAllPaymentMethod(0, 10, true, "Credit Card", "name", "asc");

        assertNotNull(result);
        verify(paymentMethodRepository, times(1)).findAll(any(Specification.class), eq(pageRequest));
    }

    @Test
    void testDeletePaymentMethod() {
        when(paymentMethodRepository.existsById(paymentMethodId)).thenReturn(true);
        doNothing().when(paymentMethodRepository).deleteById(paymentMethodId);

        Map<String, Object> result = paymentMethodService.deletePaymentMethod(paymentMethodId.toString());

        assertNotNull(result);
        assertEquals("PaymentMethod deleted successfully", result.get("message"));
        verify(paymentMethodRepository, times(1)).deleteById(paymentMethodId);
    }

    @Test
    void testDeletePaymentMethod_NotFound() {
        when(paymentMethodRepository.existsById(paymentMethodId)).thenReturn(false);

        Map<String, Object> result = paymentMethodService.deletePaymentMethod(paymentMethodId.toString());

        assertNotNull(result);
        assertEquals("PaymentMethod not found", result.get("message"));
    }
}
