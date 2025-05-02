package id.ac.ui.cs.advprog.payment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.payment.dto.paymentmethod.PaymentMethodDTO;
import id.ac.ui.cs.advprog.payment.dto.paymentmethod.PaymentMethodRegisterDTO;
import id.ac.ui.cs.advprog.payment.service.PaymentMethodService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PaymentMethodController.class)
public class PaymentMethodControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PaymentMethodService paymentMethodService;

    @Autowired
    private ObjectMapper objectMapper;

    private PaymentMethodDTO sampleDTO;
    private PaymentMethodRegisterDTO registerDTO;

    @BeforeEach
    void setUp() {
        sampleDTO = new PaymentMethodDTO();
        sampleDTO.setId(UUID.randomUUID());
        sampleDTO.setName("Sample Method");
        sampleDTO.setDescription("desc");
        sampleDTO.setProcessingFee(BigDecimal.valueOf(1000));
        sampleDTO.setCreatedBy(UUID.randomUUID());
        sampleDTO.setDeletedAt(null);

        registerDTO = new PaymentMethodRegisterDTO();
        registerDTO.setName("Sample");
        registerDTO.setDescription("desc");
        registerDTO.setProcessingFee(BigDecimal.valueOf(1000));
        registerDTO.setCreatedBy(UUID.randomUUID().toString());
        registerDTO.setPaymentMethod("COD");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreatePaymentMethod() throws Exception {
        when(paymentMethodService.createPaymentMethod(any())).thenReturn(sampleDTO);

        mockMvc.perform(post("/payment-methods/admin/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Sample Method"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testFindAllPaymentMethods() throws Exception {
        Page<PaymentMethodDTO> page = new PageImpl<>(List.of(sampleDTO), PageRequest.of(0, 10), 1);
        when(paymentMethodService.findAllPaymentMethod(0, 10, null, null, "id", "ASC")).thenReturn(page);

        mockMvc.perform(get("/payment-methods/admin?page=0&size=10"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetPaymentMethodById() throws Exception {
        when(paymentMethodService.findPaymentMethodById(anyString())).thenReturn(sampleDTO);

        mockMvc.perform(get("/payment-methods/admin/{id}", UUID.randomUUID()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Sample Method"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdatePaymentMethod() throws Exception {
        when(paymentMethodService.updatePaymentMethod(any(), any())).thenReturn(sampleDTO);

        mockMvc.perform(put("/payment-methods/admin/{id}/edit", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Sample Method"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeletePaymentMethod() throws Exception {
        Map<String, Object> result = new HashMap<>();
        result.put("id", UUID.randomUUID());
        result.put("deleted_at", new Date());

        when(paymentMethodService.deletePaymentMethod(anyString())).thenReturn(result);

        mockMvc.perform(delete("/payment-methods/admin/{id}/delete", UUID.randomUUID()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deleted_at").exists());
    }

    @Test
    void testFindAllActivePaymentMethods() throws Exception {
        Page<PaymentMethodDTO> page = new PageImpl<>(List.of(sampleDTO));
        when(paymentMethodService.findAllPaymentMethod(0, 10, true, null, "id", "ASC")).thenReturn(page);

        mockMvc.perform(get("/payment-methods/active?page=0&size=10"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetActivePaymentMethodById() throws Exception {
        when(paymentMethodService.findPaymentMethodById(anyString())).thenReturn(sampleDTO);

        mockMvc.perform(get("/payment-methods/active/{id}", UUID.randomUUID()))
                .andExpect(status().isOk());
    }

    @Test
    void testGetByType() throws Exception {
        Page<PaymentMethodDTO> page = new PageImpl<>(List.of(sampleDTO));
        when(paymentMethodService.findAllPaymentMethod(0, 10, true, "COD", "id", "ASC")).thenReturn(page);

        mockMvc.perform(get("/payment-methods/type?type=COD&page=0&size=10"))
                .andExpect(status().isOk());
    }

    @Test
    void testTestEndpoint() throws Exception {
        mockMvc.perform(get("/payment-methods/test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("This is a test response!"));
    }
}
