package id.ac.ui.cs.advprog.payment.service;

import id.ac.ui.cs.advprog.payment.dto.paymentmethod.PaymentMethodDTO;
import id.ac.ui.cs.advprog.payment.dto.paymentmethod.PaymentMethodRegisterDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PaymentMethodServiceTest {

    @Mock
    private PaymentMethodService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePaymentMethod() {
        PaymentMethodRegisterDTO dto = new PaymentMethodRegisterDTO();
        dto.setName("Test Method");
        dto.setDescription("Test Desc");
        dto.setProcessingFee(BigDecimal.valueOf(1000));
        dto.setCreatedBy(UUID.randomUUID().toString());
        dto.setPaymentMethod("COD");

        PaymentMethodDTO resultDTO = new PaymentMethodDTO();
        resultDTO.setName("Test Method");

        when(service.createPaymentMethod(dto)).thenReturn(resultDTO);

        PaymentMethodDTO response = service.createPaymentMethod(dto);
        assertEquals("Test Method", response.getName());

        verify(service).createPaymentMethod(dto);
    }

    @Test
    void testFindById() {
        UUID id = UUID.randomUUID();
        PaymentMethodDTO dto = new PaymentMethodDTO();
        dto.setId(id);

        when(service.findPaymentMethodById(id.toString())).thenReturn(dto);

        PaymentMethodDTO result = service.findPaymentMethodById(id.toString());
        assertEquals(id, result.getId());

        verify(service).findPaymentMethodById(id.toString());
    }

    @Test
    void testDeletePaymentMethod() {
        UUID id = UUID.randomUUID();
        Map<String, Object> response = Map.of("id", id, "deleted_at", new Date());

        when(service.deletePaymentMethod(id.toString())).thenReturn(response);

        Map<String, Object> result = service.deletePaymentMethod(id.toString());
        assertTrue(result.containsKey("deleted_at"));

        verify(service).deletePaymentMethod(id.toString());
    }

    @Test
    void testFindAll() {
        Page<PaymentMethodDTO> page = new PageImpl<>(List.of(new PaymentMethodDTO()));
        when(service.findAllPaymentMethod(0, 10, true, null, "id", "asc")).thenReturn(page);

        Page<PaymentMethodDTO> result = service.findAllPaymentMethod(0, 10, true, null, "id", "asc");
        assertFalse(result.isEmpty());

        verify(service).findAllPaymentMethod(0, 10, true, null, "id", "asc");
    }
}
