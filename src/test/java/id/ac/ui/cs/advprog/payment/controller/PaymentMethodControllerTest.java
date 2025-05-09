package id.ac.ui.cs.advprog.payment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.payment.dto.paymentmethod.PaymentMethodDTO;
import id.ac.ui.cs.advprog.payment.dto.paymentmethod.PaymentMethodRegisterDTO;
import id.ac.ui.cs.advprog.payment.service.PaymentMethodService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.*;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PaymentMethodController.class)
public class PaymentMethodControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
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
    void testCreatePaymentMethod() throws Exception {
        when(paymentMethodService.createPaymentMethod(ArgumentMatchers.any())).thenReturn(sampleDTO);

        mockMvc.perform(post("/payment-methods/admin/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.name").value("Sample Method"));
    }

    @Test
    void testFindAllPaymentMethods() throws Exception {
        when(paymentMethodService.findAllPaymentMethod(0, 10, null, null, "id", "ASC"))
                .thenReturn(new PageImpl<>(List.of(sampleDTO)));

        mockMvc.perform(get("/payment-methods/admin")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "id")
                        .param("sortDirection", "ASC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].name").value("Sample Method"));
    }

    @Test
    void testGetPaymentMethodById() throws Exception {
        when(paymentMethodService.findPaymentMethodById(sampleDTO.getId().toString())).thenReturn(sampleDTO);

        mockMvc.perform(get("/payment-methods/admin/" + sampleDTO.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Sample Method"));
    }

    @Test
    void testUpdatePaymentMethod() throws Exception {
        when(paymentMethodService.updatePaymentMethod(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(sampleDTO);

        mockMvc.perform(put("/payment-methods/admin/" + UUID.randomUUID() + "/edit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Sample Method"));
    }

    @Test
    void testDeletePaymentMethod() throws Exception {
        Map<String, Object> result = new HashMap<>();
        result.put("deleted", true);

        when(paymentMethodService.deletePaymentMethod(sampleDTO.getId().toString())).thenReturn(result);

        mockMvc.perform(delete("/payment-methods/admin/" + sampleDTO.getId() + "/delete"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.deleted").value(true));
    }

    @Test
    void testFindAllActivePaymentMethods() throws Exception {
        when(paymentMethodService.findAllPaymentMethod(0, 10, true, null, "id", "ASC"))
                .thenReturn(new PageImpl<>(List.of(sampleDTO)));

        mockMvc.perform(get("/payment-methods/active")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "id")
                        .param("sortDirection", "ASC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].name").value("Sample Method"));
    }

    @Test
    void testGetActivePaymentMethodById() throws Exception {
        when(paymentMethodService.findPaymentMethodById(sampleDTO.getId().toString())).thenReturn(sampleDTO);

        mockMvc.perform(get("/payment-methods/active/" + sampleDTO.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Sample Method"));
    }

    @Test
    void testGetByType() throws Exception {
        when(paymentMethodService.findAllPaymentMethod(0, 10, true, "COD", "id", "ASC"))
                .thenReturn(new PageImpl<>(List.of(sampleDTO)));

        mockMvc.perform(get("/payment-methods/type")
                        .param("type", "COD")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "id")
                        .param("sortDirection", "ASC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].name").value("Sample Method"));
    }

    @Test
    void testTestingEndpoint() throws Exception {
        mockMvc.perform(get("/payment-methods/test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("This is a test response!"))
                .andExpect(jsonPath("$.status").value("success"));
    }
}
