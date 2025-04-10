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
import org.springframework.test.util.ReflectionTestUtils;

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

        @Test
        void createPaymentMethod_ShouldReturnPaymentMethodDTO_WhenValidInput() {
            assertThrows(UnsupportedOperationException.class, () -> paymentMethodService.createPaymentMethod(paymentMethodRegisterDTO));
        }

        @Test
        void updatePaymentMethod_ShouldReturnUpdatedPaymentMethodDTO_WhenValidInput() {
            assertThrows(UnsupportedOperationException.class, () -> paymentMethodService.updatePaymentMethod(UUID.randomUUID(), paymentMethodRegisterDTO));
        }

        @Test
        void findAllPaymentMethod_ShouldReturnPagedResult_WhenValidRequest() {
            assertThrows(UnsupportedOperationException.class, () -> paymentMethodService.findAllPaymentMethod(0, 10, true, "COD", "name", "ASC"));
        }

        @Test
        void findPaymentMethodById_ShouldReturnPaymentMethodDTO_WhenPaymentMethodExists() {
            assertThrows(UnsupportedOperationException.class, () -> paymentMethodService.findPaymentMethodById("non-existent-id"));
        }

        @Test
        void deletePaymentMethod_ShouldReturnSuccessMessage_WhenValidId() {
            assertThrows(UnsupportedOperationException.class, () -> paymentMethodService.deletePaymentMethod("non-existent-id"));
        }
    }
