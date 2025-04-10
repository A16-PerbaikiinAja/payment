package id.ac.ui.cs.advprog.payment.controller;

import id.ac.ui.cs.advprog.payment.dto.paymentmethod.PaymentMethodRegisterDTO;
import id.ac.ui.cs.advprog.payment.service.PaymentMethodService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PaymentMethodControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private PaymentMethodController paymentMethodController;

    @Mock
    private PaymentMethodService paymentMethodService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(paymentMethodController).build();
    }

    @Test
    void createPaymentMethod_ShouldReturnStatusOk_WhenValidInput() throws Exception {
        PaymentMethodRegisterDTO dto = new PaymentMethodRegisterDTO();
        dto.setName("Test Payment Method");
        dto.setDescription("Test Description");

        mockMvc.perform(post("/payment-methods/admin/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Test Payment Method\", \"description\":\"Test Description\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void findAllPaymentMethods_ShouldReturnStatusOk_WhenAdminAccess() throws Exception {
        mockMvc.perform(get("/payment-methods/admin")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());
    }

    @Test
    void getPaymentMethodById_ShouldReturnStatusOk_WhenValidId() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(get("/payment-methods/admin/" + id))
                .andExpect(status().isOk());
    }

    @Test
    void updatePaymentMethod_ShouldReturnStatusOk_WhenValidInput() throws Exception {
        UUID id = UUID.randomUUID();
        PaymentMethodRegisterDTO dto = new PaymentMethodRegisterDTO();
        dto.setName("Updated Payment Method");
        dto.setDescription("Updated Description");

        mockMvc.perform(put("/payment-methods/admin/" + id + "/edit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Payment Method\", \"description\":\"Updated Description\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void deletePaymentMethod_ShouldReturnStatusOk_WhenValidId() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/payment-methods/admin/" + id + "/delete"))
                .andExpect(status().isOk());
    }

    @Test
    void findAllActivePaymentMethods_ShouldReturnStatusOk_WhenValidRequest() throws Exception {
        mockMvc.perform(get("/payment-methods/active")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());
    }

    @Test
    void getActivePaymentMethodById_ShouldReturnStatusOk_WhenValidId() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(get("/payment-methods/active/" + id))
                .andExpect(status().isOk());
    }

    @Test
    void getByType_ShouldReturnStatusOk_WhenValidType() throws Exception {
        mockMvc.perform(get("/payment-methods/type")
                        .param("type", "COD")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());
    }

    @Test
    void testTesting_ShouldReturnStatusOk_WhenTestEndpointCalled() throws Exception {
        mockMvc.perform(get("/payment-methods/test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("This is a test response!"));
    }
}
