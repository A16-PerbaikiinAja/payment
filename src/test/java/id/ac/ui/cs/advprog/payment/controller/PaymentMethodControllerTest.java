package id.ac.ui.cs.advprog.payment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.payment.dto.paymentmethod.PaymentMethodRegisterDTO;
import id.ac.ui.cs.advprog.payment.model.PaymentMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
class PaymentMethodControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private PaymentMethodController paymentMethodController;

    @Mock
    private PaymentMethod paymentMethod;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(paymentMethodController).build();
    }

    @Test
    void testFindAllPaymentMethods() throws Exception {
        mockMvc.perform(get("/api/payment")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());
    }

    @Test
    void testFindAllActivePaymentMethods() throws Exception {
        mockMvc.perform(get("/api/payment/active")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdatePaymentMethod() throws Exception {
        UUID id = UUID.randomUUID();
        PaymentMethodRegisterDTO dto = new PaymentMethodRegisterDTO();

        mockMvc.perform(put("/api/payment/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void testGetPaymentMethodById() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(get("/api/payment/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    void testGetByType() throws Exception {
        mockMvc.perform(get("/api/payment/type")
                        .param("type", "credit")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeletePaymentMethod() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/api/payment/{id}", id))
                .andExpect(status().isOk());
    }
}