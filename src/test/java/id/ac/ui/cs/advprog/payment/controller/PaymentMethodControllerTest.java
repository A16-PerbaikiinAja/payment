//package id.ac.ui.cs.advprog.payment.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import id.ac.ui.cs.advprog.payment.dto.Response;
//import id.ac.ui.cs.advprog.payment.dto.exception.ErrorResponse;
//import id.ac.ui.cs.advprog.payment.dto.paymentmethod.PaymentMethodDTO;
//import id.ac.ui.cs.advprog.payment.dto.paymentmethod.PaymentMethodDetailsDTO;
//import id.ac.ui.cs.advprog.payment.dto.paymentmethod.PaymentMethodRegisterDTO;
//import id.ac.ui.cs.advprog.payment.dto.paymentmethod.PaymentMethodUserDTO;
//import id.ac.ui.cs.advprog.payment.enums.Status;
//import id.ac.ui.cs.advprog.payment.service.PaymentMethodService;
//import jakarta.persistence.EntityNotFoundException;
//import jakarta.servlet.http.HttpServletRequest;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@ExtendWith(MockitoExtension.class)
//class PaymentMethodControllerTest {
//
//    @Mock
//    private PaymentMethodService paymentMethodService;
//
//    @Mock
//    private HttpServletRequest httpServletRequest;
//
//    @InjectMocks
//    private PaymentMethodController paymentMethodController;
//
//    private MockMvc mockMvc;
//    private ObjectMapper objectMapper;
//    private PaymentMethodRegisterDTO paymentMethodRegisterDTO;
//    private PaymentMethodDTO paymentMethodDTO;
//    private PaymentMethodUserDTO paymentMethodUserDTO;
//    private PaymentMethodDetailsDTO paymentMethodDetailsDTO;
//    private UUID testId;
//
//    @BeforeEach
//    void setUp() {
//        mockMvc = MockMvcBuilders.standaloneSetup(paymentMethodController).build();
//        objectMapper = new ObjectMapper();
//        testId = UUID.randomUUID();
//
//        // Initialize test DTOs
//        paymentMethodRegisterDTO = new PaymentMethodRegisterDTO();
//        paymentMethodRegisterDTO.setName("Credit Card");
//        // paymentMethodRegisterDTO.setType("CARD"); // Remove if setType method doesn't exist
//        paymentMethodRegisterDTO.setDescription("Credit card payment");
//
//        paymentMethodDTO = new PaymentMethodDTO();
//        paymentMethodDTO.setId(testId);
//        paymentMethodDTO.setName("Credit Card");
//        // paymentMethodDTO.setType("CARD"); // Remove if setType method doesn't exist
//        paymentMethodDTO.setDescription("Credit card payment");
//
//        paymentMethodUserDTO = new PaymentMethodUserDTO();
//        paymentMethodUserDTO.setId(testId);
//        paymentMethodUserDTO.setName("Credit Card");
//        // paymentMethodUserDTO.setType("CARD"); // Remove if setType method doesn't exist
//
//        paymentMethodDetailsDTO = new PaymentMethodDetailsDTO();
//        paymentMethodDetailsDTO.setId(String.valueOf(testId));
//        paymentMethodDetailsDTO.setName("Credit Card");
//        paymentMethodDetailsDTO.setOrderCount(10);
//    }
//
//    // ==================== ADMIN ENDPOINTS TESTS ====================
//
//    @Test
//    void createPaymentMethod_Success() {
//        // Given
//        when(paymentMethodService.createPaymentMethod(any(PaymentMethodRegisterDTO.class)))
//                .thenReturn(paymentMethodDTO);
//
//        // When
//        ResponseEntity<?> response = paymentMethodController.createPaymentMethod(paymentMethodRegisterDTO);
//
//        // Then
//        assertEquals(HttpStatus.CREATED, response.getStatusCode());
//        assertNotNull(response.getBody());
//
//        Response responseBody = (Response) response.getBody();
//        assertEquals(Status.success.toString(), responseBody.getStatus());
//        assertEquals("Payment method created successfully", responseBody.getMessage());
//        assertEquals(paymentMethodDTO, responseBody.getData());
//
//        verify(paymentMethodService, times(1)).createPaymentMethod(paymentMethodRegisterDTO);
//    }
//
//    @Test
//    void createPaymentMethod_IllegalArgumentException() {
//        // Given
//        String errorMessage = "Invalid payment method data";
//        when(paymentMethodService.createPaymentMethod(any(PaymentMethodRegisterDTO.class)))
//                .thenThrow(new IllegalArgumentException(errorMessage));
//
//        // When
//        ResponseEntity<?> response = paymentMethodController.createPaymentMethod(paymentMethodRegisterDTO);
//
//        // Then
//        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//
//        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
//        assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.getMessage());
//        assertEquals(HttpStatus.BAD_REQUEST.getReasonPhrase(), errorResponse.getError());
//        assertEquals(errorMessage, errorResponse.getMessage());
//    }
//
//    @Test
//    void createPaymentMethod_EntityNotFoundException() {
//        // Given
//        String errorMessage = "Entity not found";
//        when(paymentMethodService.createPaymentMethod(any(PaymentMethodRegisterDTO.class)))
//                .thenThrow(new EntityNotFoundException(errorMessage));
//
//        // When
//        ResponseEntity<?> response = paymentMethodController.createPaymentMethod(paymentMethodRegisterDTO);
//
//        // Then
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//
//        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
//        assertEquals(HttpStatus.NOT_FOUND.value(), errorResponse.getMessage());
//        assertEquals(HttpStatus.NOT_FOUND.getReasonPhrase(), errorResponse.getError());
//        assertEquals(errorMessage, errorResponse.getMessage());
//    }
//
//    @Test
//    void createPaymentMethod_RuntimeException() {
//        // Given
//        String errorMessage = "Runtime error occurred";
//        when(paymentMethodService.createPaymentMethod(any(PaymentMethodRegisterDTO.class)))
//                .thenThrow(new RuntimeException(errorMessage));
//
//        // When
//        ResponseEntity<?> response = paymentMethodController.createPaymentMethod(paymentMethodRegisterDTO);
//
//        // Then
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
//
//        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorResponse.getMessage());
//        assertEquals("Internal Server Error", errorResponse.getError());
//        assertEquals(errorMessage, errorResponse.getMessage());
//    }
//
//    @Test
//    void findAllPaymentMethods_Success() {
//        // Given
//        List<PaymentMethodDTO> paymentMethods = Arrays.asList(paymentMethodDTO);
//        when(paymentMethodService.findAllPaymentMethods()).thenReturn(paymentMethods);
//
//        // When
//        ResponseEntity<?> response = paymentMethodController.findAllPaymentMethods();
//
//        // Then
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//
//        Response responseBody = (Response) response.getBody();
//        assertEquals("success", responseBody.getStatus());
//        assertEquals("Payment methods retrieved successfully", responseBody.getMessage());
//        assertEquals(paymentMethods, responseBody.getData());
//    }
//
//    @Test
//    void findAllPaymentMethods_Exception() {
//        // Given
//        when(paymentMethodService.findAllPaymentMethods())
//                .thenThrow(new RuntimeException("Database error"));
//
//        // When
//        ResponseEntity<?> response = paymentMethodController.findAllPaymentMethods();
//
//        // Then
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
//    }
//
//    @Test
//    void updatePaymentMethod_Success() {
//        // Given
//        when(paymentMethodService.updatePaymentMethod(eq(testId), any(PaymentMethodRegisterDTO.class)))
//                .thenReturn(paymentMethodDTO);
//
//        // When
//        ResponseEntity<?> response = paymentMethodController.updatePaymentMethod(testId, paymentMethodRegisterDTO);
//
//        // Then
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//
//        Response responseBody = (Response) response.getBody();
//        assertEquals("success", responseBody.getStatus());
//        assertEquals("Payment method updated successfully", responseBody.getMessage());
//        assertEquals(paymentMethodDTO, responseBody.getData());
//    }
//
//    @Test
//    void updatePaymentMethod_IllegalArgumentException() {
//        // Given
//        String errorMessage = "Invalid argument";
//        when(paymentMethodService.updatePaymentMethod(eq(testId), any(PaymentMethodRegisterDTO.class)))
//                .thenThrow(new IllegalArgumentException(errorMessage));
//
//        // When
//        ResponseEntity<?> response = paymentMethodController.updatePaymentMethod(testId, paymentMethodRegisterDTO);
//
//        // Then
//        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//
//        Response responseBody = (Response) response.getBody();
//        assertEquals("error", responseBody.getStatus());
//        assertEquals(errorMessage, responseBody.getMessage());
//        assertNull(responseBody.getData());
//    }
//
//    @Test
//    void updatePaymentMethod_IllegalStateException() {
//        // Given
//        String errorMessage = "Invalid state";
//        when(paymentMethodService.updatePaymentMethod(eq(testId), any(PaymentMethodRegisterDTO.class)))
//                .thenThrow(new IllegalStateException(errorMessage));
//
//        // When
//        ResponseEntity<?> response = paymentMethodController.updatePaymentMethod(testId, paymentMethodRegisterDTO);
//
//        // Then
//        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//
//        Response responseBody = (Response) response.getBody();
//        assertEquals("error", responseBody.getStatus());
//        assertEquals(errorMessage, responseBody.getMessage());
//        assertNull(responseBody.getData());
//    }
//
//    @Test
//    void updatePaymentMethod_Exception() {
//        // Given
//        when(paymentMethodService.updatePaymentMethod(eq(testId), any(PaymentMethodRegisterDTO.class)))
//                .thenThrow(new RuntimeException("Database error"));
//
//        // When
//        ResponseEntity<?> response = paymentMethodController.updatePaymentMethod(testId, paymentMethodRegisterDTO);
//
//        // Then
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
//
//        Response responseBody = (Response) response.getBody();
//        assertEquals("error", responseBody.getStatus());
//        assertEquals("Internal Server Error", responseBody.getMessage());
//        assertNull(responseBody.getData());
//    }
//
//    @Test
//    void deletePaymentMethod_Success() {
//        // Given
//        String id = "test-id";
//        Map<String, Object> result = new HashMap<>();
//        result.put("deleted", true);
//        when(paymentMethodService.deletePaymentMethod(id)).thenReturn(result);
//
//        // When
//        ResponseEntity<?> response = paymentMethodController.deletePaymentMethod(id);
//
//        // Then
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//
//        Response responseBody = (Response) response.getBody();
//        assertEquals("success", responseBody.getStatus());
//        assertEquals("Payment method deleted successfully", responseBody.getMessage());
//        assertEquals(result, responseBody.getData());
//    }
//
//    @Test
//    void activatePaymentMethod_Success() {
//        // Given
//        String id = "test-id";
//        when(paymentMethodService.activatePaymentMethod(id)).thenReturn(paymentMethodDTO);
//
//        // When
//        ResponseEntity<?> response = paymentMethodController.activatePaymentMethod(id);
//
//        // Then
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//
//        Response responseBody = (Response) response.getBody();
//        assertEquals("success", responseBody.getStatus());
//        assertEquals("Payment method reactivate successfully", responseBody.getMessage());
//        assertEquals(paymentMethodDTO, responseBody.getData());
//    }
//
//    @Test
//    void getAdminPaymentMethodById_Success() {
//        // Given
//        String id = "test-id";
//        when(paymentMethodService.findPaymentMethodById(id)).thenReturn(paymentMethodDTO);
//
//        // When
//        ResponseEntity<?> response = paymentMethodController.getAdminPaymentMethodById(id);
//
//        // Then
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//
//        Response responseBody = (Response) response.getBody();
//        assertEquals("success", responseBody.getStatus());
//        assertEquals("Payment method retrieved successfully", responseBody.getMessage());
//        assertEquals(paymentMethodDTO, responseBody.getData());
//    }
//
//    @Test
//    void getAdminPaymentMethodById_NotFound() {
//        // Given
//        String id = "test-id";
//        when(paymentMethodService.findPaymentMethodById(id)).thenReturn(null);
//
//        // When
//        ResponseEntity<?> response = paymentMethodController.getAdminPaymentMethodById(id);
//
//        // Then
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//
//        Response responseBody = (Response) response.getBody();
//        assertEquals("error", responseBody.getStatus());
//        assertEquals("Payment method not found.", responseBody.getMessage());
//        assertNull(responseBody.getData());
//    }
//
//    @Test
//    void getAdminPaymentMethodById_Exception() {
//        // Given
//        String id = "test-id";
//        when(paymentMethodService.findPaymentMethodById(id))
//                .thenThrow(new RuntimeException("Database error"));
//
//        // When
//        ResponseEntity<?> response = paymentMethodController.getAdminPaymentMethodById(id);
//
//        // Then
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
//
//        @SuppressWarnings("unchecked")
//        Map<String, String> errorResponse = (Map<String, String>) response.getBody();
//        assertEquals("5000", errorResponse.get("code"));
//        assertEquals("Internal Server Error", errorResponse.get("message"));
//    }
//
//    @Test
//    void getAllPaymentMethodsWithOrderDetailsForAdmin_Success() {
//        // Given
//        List<PaymentMethodDetailsDTO> result = Arrays.asList(paymentMethodDetailsDTO);
//        when(paymentMethodService.getAllPaymentMethodsWithOrderCounts(httpServletRequest))
//                .thenReturn(result);
//
//        // When
//        ResponseEntity<?> response = paymentMethodController
//                .getAllPaymentMethodsWithOrderDetailsForAdmin(httpServletRequest);
//
//        // Then
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//
//        Response responseBody = (Response) response.getBody();
//        assertEquals(Status.success.toString(), responseBody.getStatus());
//        assertEquals("All payment methods with order counts retrieved successfully for admin",
//                responseBody.getMessage());
//        assertEquals(result, responseBody.getData());
//    }
//
//    @Test
//    void getAllPaymentMethodsWithOrderDetailsForAdmin_Exception() {
//        // Given
//        when(paymentMethodService.getAllPaymentMethodsWithOrderCounts(httpServletRequest))
//                .thenThrow(new RuntimeException("Service error"));
//
//        // When
//        ResponseEntity<?> response = paymentMethodController
//                .getAllPaymentMethodsWithOrderDetailsForAdmin(httpServletRequest);
//
//        // Then
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
//
//        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorResponse.getMessage());
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), errorResponse.getError());
//        assertTrue(errorResponse.getMessage().contains("Failed to retrieve payment methods with order counts for admin"));
//    }
//
//    @Test
//    void getByTypeForAdmin_Success() {
//        // Given
//        String type = "CARD";
//        List<PaymentMethodDTO> result = Arrays.asList(paymentMethodDTO);
//        when(paymentMethodService.findByTypeForAdmin(type)).thenReturn(result);
//
//        // When
//        ResponseEntity<?> response = paymentMethodController.getByTypeForAdmin(type);
//
//        // Then
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//
//        Response responseBody = (Response) response.getBody();
//        assertEquals("success", responseBody.getStatus());
//        assertEquals("Payment methods by type retrieved successfully", responseBody.getMessage());
//        assertEquals(result, responseBody.getData());
//    }
//
//    @Test
//    void getByTypeForAdmin_Exception() {
//        // Given
//        String type = "CARD";
//        when(paymentMethodService.findByTypeForAdmin(type))
//                .thenThrow(new RuntimeException("Database error"));
//
//        // When
//        ResponseEntity<?> response = paymentMethodController.getByTypeForAdmin(type);
//
//        // Then
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
//
//        @SuppressWarnings("unchecked")
//        Map<String, String> errorResponse = (Map<String, String>) response.getBody();
//        assertEquals("500", errorResponse.get("code"));
//        assertEquals("Internal Server Error", errorResponse.get("message"));
//    }
//
//    // ==================== PUBLIC ENDPOINTS TESTS ====================
//
//    @Test
//    void findAllActivePaymentMethods_Success() {
//        // Given
//        List<PaymentMethodUserDTO> result = Arrays.asList(paymentMethodUserDTO);
//        when(paymentMethodService.findAllActivePaymentMethods()).thenReturn(result);
//
//        // When
//        ResponseEntity<?> response = paymentMethodController.findAllActivePaymentMethods();
//
//        // Then
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//
//        Response responseBody = (Response) response.getBody();
//        assertEquals("success", responseBody.getStatus());
//        assertEquals("Active payment methods retrieved successfully", responseBody.getMessage());
//        assertEquals(result, responseBody.getData());
//    }
//
//    @Test
//    void findAllActivePaymentMethods_Exception() {
//        // Given
//        when(paymentMethodService.findAllActivePaymentMethods())
//                .thenThrow(new RuntimeException("Database error"));
//
//        // When
//        ResponseEntity<?> response = paymentMethodController.findAllActivePaymentMethods();
//
//        // Then
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
//    }
//
//    @Test
//    void getActivePaymentMethodById_Success() {
//        // Given
//        String id = "test-id";
//        when(paymentMethodService.findActivePaymentMethodById(id)).thenReturn(paymentMethodUserDTO);
//
//        // When
//        ResponseEntity<?> response = paymentMethodController.getActivePaymentMethodById(id);
//
//        // Then
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//
//        Response responseBody = (Response) response.getBody();
//        assertEquals("success", responseBody.getStatus());
//        assertEquals("Active payment method retrieved successfully", responseBody.getMessage());
//        assertEquals(paymentMethodUserDTO, responseBody.getData());
//    }
//
//    @Test
//    void getActivePaymentMethodById_NotFound() {
//        // Given
//        String id = "test-id";
//        when(paymentMethodService.findActivePaymentMethodById(id)).thenReturn(null);
//
//        // When
//        ResponseEntity<?> response = paymentMethodController.getActivePaymentMethodById(id);
//
//        // Then
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//
//        Response responseBody = (Response) response.getBody();
//        assertEquals("error", responseBody.getStatus());
//        assertEquals("Active payment method not found.", responseBody.getMessage());
//        assertNull(responseBody.getData());
//    }
//
//    @Test
//    void getActivePaymentMethodById_Exception() {
//        // Given
//        String id = "test-id";
//        when(paymentMethodService.findActivePaymentMethodById(id))
//                .thenThrow(new RuntimeException("Database error"));
//
//        // When
//        ResponseEntity<?> response = paymentMethodController.getActivePaymentMethodById(id);
//
//        // Then
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
//
//        @SuppressWarnings("unchecked")
//        Map<String, String> errorResponse = (Map<String, String>) response.getBody();
//        assertEquals("5000", errorResponse.get("code"));
//        assertEquals("Internal Server Error", errorResponse.get("message"));
//    }
//
//    @Test
//    void getByType_Success() {
//        // Given
//        String type = "CARD";
//        List<PaymentMethodUserDTO> result = Arrays.asList(paymentMethodUserDTO);
//        when(paymentMethodService.findActivePaymentMethodsByType(type)).thenReturn(result);
//
//        // When
//        ResponseEntity<?> response = paymentMethodController.getByType(type);
//
//        // Then
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//
//        Response responseBody = (Response) response.getBody();
//        assertEquals("success", responseBody.getStatus());
//        assertEquals("Payment methods by type retrieved successfully", responseBody.getMessage());
//        assertEquals(result, responseBody.getData());
//    }
//
//    @Test
//    void getByType_Exception() {
//        // Given
//        String type = "CARD";
//        when(paymentMethodService.findActivePaymentMethodsByType(type))
//                .thenThrow(new RuntimeException("Database error"));
//
//        // When
//        ResponseEntity<?> response = paymentMethodController.getByType(type);
//
//        // Then
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
//
//        @SuppressWarnings("unchecked")
//        Map<String, String> errorResponse = (Map<String, String>) response.getBody();
//        assertEquals("5000", errorResponse.get("code"));
//        assertEquals("Internal Server Error", errorResponse.get("message"));
//    }
//
//    // ==================== INTEGRATION TESTS ====================
//
//    @Test
//    void testControllerWithMockMvc_CreatePaymentMethod() throws Exception {
//        // Given
//        when(paymentMethodService.createPaymentMethod(any(PaymentMethodRegisterDTO.class)))
//                .thenReturn(paymentMethodDTO);
//
//        String requestBody = objectMapper.writeValueAsString(paymentMethodRegisterDTO);
//
//        // When & Then
//        mockMvc.perform(post("/payment-methods/admin/create")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestBody))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.status").value("success"))
//                .andExpect(jsonPath("$.message").value("Payment method created successfully"));
//    }
//
//    @Test
//    void testControllerWithMockMvc_GetAllActivePaymentMethods() throws Exception {
//        // Given
//        List<PaymentMethodUserDTO> result = Arrays.asList(paymentMethodUserDTO);
//        when(paymentMethodService.findAllActivePaymentMethods()).thenReturn(result);
//
//        // When & Then
//        mockMvc.perform(get("/payment-methods/active"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.status").value("success"))
//                .andExpect(jsonPath("$.message").value("Active payment methods retrieved successfully"));
//    }
//}