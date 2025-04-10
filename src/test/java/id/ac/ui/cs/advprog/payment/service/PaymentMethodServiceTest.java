package id.ac.ui.cs.advprog.payment.service;

import id.ac.ui.cs.advprog.payment.dto.paymentmethod.*;
import id.ac.ui.cs.advprog.payment.model.PaymentMethod;
import id.ac.ui.cs.advprog.payment.repository.PaymentMethodRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class PaymentMethodServiceTest {

    @Mock
    private PaymentMethodRepository paymentMethodRepository;

    @Mock
    private PaymentMethodDTO paymentMethodDTO;

    @InjectMocks
    private PaymentMethodServiceImpl paymentMethodService;

    private PaymentMethodRegisterDTO paymentMethodRegisterDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        paymentMethodRegisterDTO = new PaymentMethodRegisterDTO();
        paymentMethodRegisterDTO.setName("Test Payment");
        paymentMethodRegisterDTO.setDescription("Test Description");
        paymentMethodRegisterDTO.setProcessingFee(BigDecimal.valueOf(2.5));
        paymentMethodRegisterDTO.setCreatedBy(UUID.randomUUID().toString());
        paymentMethodRegisterDTO.setPaymentMethod("COD");
    }

    @Test
    void createPaymentMethod_ShouldReturnPaymentMethodDTO_WhenValidInput() {
        PaymentMethod paymentMethod = new PaymentMethod("Test Payment", "Test Description", BigDecimal.valueOf(2.5),
                Timestamp.valueOf(LocalDateTime.now()), Timestamp.valueOf(LocalDateTime.now()),
                UUID.randomUUID());
        when(paymentMethodRepository.save(any(PaymentMethod.class))).thenReturn(paymentMethod);

        PaymentMethodDTO result = paymentMethodService.createPaymentMethod(paymentMethodRegisterDTO);

        assertNotNull(result);
        assertEquals(paymentMethod.getName(), result.getName());
        assertEquals(paymentMethod.getDescription(), result.getDescription());
    }

    @Test
    void updatePaymentMethod_ShouldReturnUpdatedPaymentMethodDTO_WhenValidInput() {
        UUID id = UUID.randomUUID();
        PaymentMethodRegisterDTO updatedDTO = new PaymentMethodRegisterDTO();
        updatedDTO.setName("Updated Payment");
        updatedDTO.setDescription("Updated Description");

        PaymentMethod updatedPaymentMethod = new PaymentMethod("Updated Payment", "Updated Description",
                BigDecimal.valueOf(3.0), Timestamp.valueOf(LocalDateTime.now()),
                Timestamp.valueOf(LocalDateTime.now()), UUID.randomUUID());
        when(paymentMethodRepository.findById(id)).thenReturn(Optional.of(updatedPaymentMethod));

        PaymentMethodDTO result = paymentMethodService.updatePaymentMethod(id, updatedDTO);

        assertNotNull(result);
        assertEquals("Updated Payment", result.getName());
        assertEquals("Updated Description", result.getDescription());
    }

    @Test
    void findAllPaymentMethod_ShouldReturnPagedResult_WhenValidRequest() {
        List<PaymentMethod> paymentMethods = List.of(new PaymentMethod("Test Payment", "Test Description",
                BigDecimal.valueOf(2.5), Timestamp.valueOf(LocalDateTime.now()),
                Timestamp.valueOf(LocalDateTime.now()), UUID.randomUUID()));
        Page<PaymentMethod> paymentMethodPage = new PageImpl<>(paymentMethods);

        when(paymentMethodRepository.findAll(any(Pageable.class))).thenReturn(paymentMethodPage);

        Page<PaymentMethodDTO> result = paymentMethodService.findAllPaymentMethod(0, 10, true, "COD", "name", "ASC");

        assertNotNull(result);
        assertEquals(paymentMethods.size(), result.getContent().size());
    }

    @Test
    void findPaymentMethodById_ShouldReturnPaymentMethodDTO_WhenPaymentMethodExists() {
        UUID id = UUID.randomUUID();
        PaymentMethod paymentMethod = new PaymentMethod("Test Payment", "Test Description",
                BigDecimal.valueOf(2.5), Timestamp.valueOf(LocalDateTime.now()),
                Timestamp.valueOf(LocalDateTime.now()), UUID.randomUUID());
        when(paymentMethodRepository.findById(id)).thenReturn(Optional.of(paymentMethod));

        PaymentMethodDTO result = paymentMethodService.findPaymentMethodById(id.toString());

        assertNotNull(result);
        assertEquals(paymentMethod.getName(), result.getName());
    }

    @Test
    void deletePaymentMethod_ShouldReturnSuccessMessage_WhenValidId() {
        UUID id = UUID.randomUUID();
        when(paymentMethodRepository.existsById(id)).thenReturn(true);

        Map<String, Object> result = paymentMethodService.deletePaymentMethod(id.toString());

        assertNotNull(result);
        assertTrue(result.containsKey("message"));
        assertEquals("Payment method deleted successfully", result.get("message"));
    }
}
