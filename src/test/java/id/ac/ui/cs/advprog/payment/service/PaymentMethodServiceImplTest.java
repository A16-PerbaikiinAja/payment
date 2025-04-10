package id.ac.ui.cs.advprog.payment.service;

import id.ac.ui.cs.advprog.payment.dto.paymentmethod.*;
import id.ac.ui.cs.advprog.payment.model.PaymentMethod;
import id.ac.ui.cs.advprog.payment.repository.PaymentMethodRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class PaymentMethodServiceImplTest {

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
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setName(paymentMethodRegisterDTO.getName());
        paymentMethod.setDescription(paymentMethodRegisterDTO.getDescription());
        paymentMethod.setProcessingFee(paymentMethodRegisterDTO.getProcessingFee());
        paymentMethod.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        paymentMethod.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
        paymentMethod.setCreatedBy(UUID.fromString(paymentMethodRegisterDTO.getCreatedBy()));

        when(paymentMethodRepository.save(any(PaymentMethod.class))).thenReturn(paymentMethod);

        PaymentMethodDTO result = paymentMethodService.createPaymentMethod(paymentMethodRegisterDTO);

        assertNotNull(result);
        assertEquals(paymentMethodRegisterDTO.getName(), result.getName());
        assertEquals(paymentMethodRegisterDTO.getDescription(), result.getDescription());
        assertEquals(paymentMethodRegisterDTO.getProcessingFee(), result.getProcessingFee());
    }

    @Test
    void updatePaymentMethod_ShouldReturnUpdatedPaymentMethodDTO_WhenValidInput() {
        UUID id = UUID.randomUUID();
        PaymentMethodRegisterDTO updateDTO = new PaymentMethodRegisterDTO();
        updateDTO.setName("Updated Payment");
        updateDTO.setDescription("Updated Description");
        updateDTO.setProcessingFee(BigDecimal.valueOf(3.0));
        updateDTO.setCreatedBy(UUID.randomUUID().toString());

        PaymentMethod updatedPaymentMethod = new PaymentMethod();
        updatedPaymentMethod.setId(id);
        updatedPaymentMethod.setName(updateDTO.getName());
        updatedPaymentMethod.setDescription(updateDTO.getDescription());
        updatedPaymentMethod.setProcessingFee(updateDTO.getProcessingFee());
        updatedPaymentMethod.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        updatedPaymentMethod.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
        updatedPaymentMethod.setCreatedBy(UUID.fromString(updateDTO.getCreatedBy()));

        when(paymentMethodRepository.save(any(PaymentMethod.class))).thenReturn(updatedPaymentMethod);

        PaymentMethodDTO result = paymentMethodService.updatePaymentMethod(id, updateDTO);

        assertNotNull(result);
        assertEquals(updateDTO.getName(), result.getName());
        assertEquals(updateDTO.getDescription(), result.getDescription());
        assertEquals(updateDTO.getProcessingFee(), result.getProcessingFee());
    }

    @Test
    void findAllPaymentMethod_ShouldReturnPagedResult_WhenValidRequest() {
        List<PaymentMethod> paymentMethods = List.of(
                new PaymentMethod("Test Payment", "Test Description", BigDecimal.valueOf(2.5), Timestamp.valueOf(LocalDateTime.now()), Timestamp.valueOf(LocalDateTime.now()), UUID.randomUUID())
        );

        when(paymentMethodRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(paymentMethods));

        Page<PaymentMethodDTO> result = paymentMethodService.findAllPaymentMethod(0, 10, true, "COD", "name", "ASC");

        assertNotNull(result);
        assertEquals(paymentMethods.size(), result.getContent().size());
    }

    @Test
    void findPaymentMethodById_ShouldReturnPaymentMethodDTO_WhenPaymentMethodExists() {
        UUID id = UUID.randomUUID();

        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setId(id);
        paymentMethod.setName(paymentMethodRegisterDTO.getName());
        paymentMethod.setDescription(paymentMethodRegisterDTO.getDescription());
        paymentMethod.setProcessingFee(paymentMethodRegisterDTO.getProcessingFee());
        paymentMethod.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        paymentMethod.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
        paymentMethod.setCreatedBy(UUID.fromString(paymentMethodRegisterDTO.getCreatedBy()));

        when(paymentMethodRepository.findById(any(UUID.class))).thenReturn(java.util.Optional.of(paymentMethod));

        PaymentMethodDTO result = paymentMethodService.findPaymentMethodById(id.toString());

        assertNotNull(result);
        assertEquals(paymentMethod.getId(), result.getId());
    }

}
