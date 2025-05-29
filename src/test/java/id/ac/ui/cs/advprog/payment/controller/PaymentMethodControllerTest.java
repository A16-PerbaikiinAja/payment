package id.ac.ui.cs.advprog.payment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.payment.dto.Response;
import id.ac.ui.cs.advprog.payment.dto.exception.ErrorResponse;
import id.ac.ui.cs.advprog.payment.dto.paymentmethod.PaymentMethodDTO;
import id.ac.ui.cs.advprog.payment.dto.paymentmethod.PaymentMethodDetailsDTO;
import id.ac.ui.cs.advprog.payment.dto.paymentmethod.PaymentMethodRegisterDTO;
import id.ac.ui.cs.advprog.payment.dto.paymentmethod.PaymentMethodUserDTO;
import id.ac.ui.cs.advprog.payment.enums.ErrorCode;
import id.ac.ui.cs.advprog.payment.service.PaymentMethodService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentMethodControllerTest {

    @Mock
    private PaymentMethodService paymentMethodService;

    @Mock
    private HttpServletRequest httpServletRequest;

    @InjectMocks
    private PaymentMethodController paymentMethodController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private PaymentMethodRegisterDTO paymentMethodRegisterDTO;
    private PaymentMethodDTO paymentMethodDTO;
    private PaymentMethodUserDTO paymentMethodUserDTO;
    private PaymentMethodDetailsDTO paymentMethodDetailsDTO;
    private UUID testId;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(paymentMethodController).build();
        objectMapper = new ObjectMapper();
        testId = UUID.randomUUID();

        paymentMethodRegisterDTO = new PaymentMethodRegisterDTO();
        paymentMethodRegisterDTO.setName("Credit Card");
        paymentMethodRegisterDTO.setDescription("Credit card payment");

        paymentMethodDTO = new PaymentMethodDTO();
        paymentMethodDTO.setId(testId);
        paymentMethodDTO.setName("Credit Card");
        paymentMethodDTO.setDescription("Credit card payment");

        paymentMethodUserDTO = new PaymentMethodUserDTO();
        paymentMethodUserDTO.setId(testId);
        paymentMethodUserDTO.setName("Credit Card");

        paymentMethodDetailsDTO = new PaymentMethodDetailsDTO();
        paymentMethodDetailsDTO.setId(String.valueOf(testId));
        paymentMethodDetailsDTO.setName("Credit Card");
        paymentMethodDetailsDTO.setOrderCount(10);
    }

    // =========================
    // ADMIN ENDPOINTS TESTS
    // =========================

    @Test
    void createPaymentMethod_Success() {
        when(paymentMethodService.createPaymentMethod(any(PaymentMethodRegisterDTO.class)))
                .thenReturn(paymentMethodDTO);

        ResponseEntity<?> response = paymentMethodController.createPaymentMethod(paymentMethodRegisterDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Response responseBody = (Response) response.getBody();
        assertEquals("success", responseBody.getStatus());
        assertEquals("Payment method created successfully", responseBody.getMessage());
        assertEquals(paymentMethodDTO, responseBody.getData());
        verify(paymentMethodService).createPaymentMethod(paymentMethodRegisterDTO);
    }

    @Test
    void createPaymentMethod_IllegalArgumentException() {
        when(paymentMethodService.createPaymentMethod(any(PaymentMethodRegisterDTO.class)))
                .thenThrow(new IllegalArgumentException("Invalid payment method data"));

        ResponseEntity<?> response = paymentMethodController.createPaymentMethod(paymentMethodRegisterDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertNotNull(errorResponse);
    }

    @Test
    void createPaymentMethod_EntityNotFoundException() {
        when(paymentMethodService.createPaymentMethod(any(PaymentMethodRegisterDTO.class)))
                .thenThrow(new EntityNotFoundException("Entity not found"));

        ResponseEntity<?> response = paymentMethodController.createPaymentMethod(paymentMethodRegisterDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertNotNull(errorResponse);
    }

    @Test
    void createPaymentMethod_RuntimeException() {
        when(paymentMethodService.createPaymentMethod(any(PaymentMethodRegisterDTO.class)))
                .thenThrow(new RuntimeException("Unexpected error"));

        ResponseEntity<?> response = paymentMethodController.createPaymentMethod(paymentMethodRegisterDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertNotNull(errorResponse);
    }

    @Test
    void findAllPaymentMethods_Success() {
        List<PaymentMethodDTO> paymentMethods = Arrays.asList(paymentMethodDTO);
        when(paymentMethodService.findAllPaymentMethods()).thenReturn(paymentMethods);

        ResponseEntity<?> response = paymentMethodController.findAllPaymentMethods();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Response responseBody = (Response) response.getBody();
        assertEquals("success", responseBody.getStatus());
        assertEquals("Payment methods retrieved successfully", responseBody.getMessage());
        assertEquals(paymentMethods, responseBody.getData());
    }

    @Test
    void findAllPaymentMethods_Exception() {
        when(paymentMethodService.findAllPaymentMethods())
                .thenThrow(new RuntimeException("Database error"));

        ResponseEntity<?> response = paymentMethodController.findAllPaymentMethods();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(ErrorCode.GENERAL_ERROR.toErrorResponse(), response.getBody());
    }

    @Test
    void updatePaymentMethod_Success() {
        when(paymentMethodService.updatePaymentMethod(any(UUID.class), any(PaymentMethodRegisterDTO.class)))
                .thenReturn(paymentMethodDTO);

        ResponseEntity<?> response = paymentMethodController.updatePaymentMethod(testId, paymentMethodRegisterDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Response responseBody = (Response) response.getBody();
        assertEquals("success", responseBody.getStatus());
        assertEquals("Payment method updated successfully", responseBody.getMessage());
        assertEquals(paymentMethodDTO, responseBody.getData());
    }

    @Test
    void updatePaymentMethod_IllegalArgumentException() {
        when(paymentMethodService.updatePaymentMethod(any(UUID.class), any(PaymentMethodRegisterDTO.class)))
                .thenThrow(new IllegalArgumentException("Invalid data"));

        ResponseEntity<?> response = paymentMethodController.updatePaymentMethod(testId, paymentMethodRegisterDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Response responseBody = (Response) response.getBody();
        assertEquals("error", responseBody.getStatus());
        assertEquals("Invalid data", responseBody.getMessage());
    }

    @Test
    void updatePaymentMethod_IllegalStateException() {
        when(paymentMethodService.updatePaymentMethod(any(UUID.class), any(PaymentMethodRegisterDTO.class)))
                .thenThrow(new IllegalStateException("Invalid state"));

        ResponseEntity<?> response = paymentMethodController.updatePaymentMethod(testId, paymentMethodRegisterDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Response responseBody = (Response) response.getBody();
        assertEquals("error", responseBody.getStatus());
        assertEquals("Invalid state", responseBody.getMessage());
    }

    @Test
    void updatePaymentMethod_GeneralException() {
        when(paymentMethodService.updatePaymentMethod(any(UUID.class), any(PaymentMethodRegisterDTO.class)))
                .thenThrow(new RuntimeException("Unexpected error"));

        ResponseEntity<?> response = paymentMethodController.updatePaymentMethod(testId, paymentMethodRegisterDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Response responseBody = (Response) response.getBody();
        assertEquals("error", responseBody.getStatus());
        assertEquals("Internal Server Error", responseBody.getMessage());
    }

    @Test
    void deletePaymentMethod_Success() {
        Map<String, Object> result = new HashMap<>();
        result.put("deleted", true);
        when(paymentMethodService.deletePaymentMethod(anyString())).thenReturn(result);

        ResponseEntity<?> response = paymentMethodController.deletePaymentMethod(testId.toString());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Response responseBody = (Response) response.getBody();
        assertEquals("success", responseBody.getStatus());
        assertEquals("Payment method deleted successfully", responseBody.getMessage());
        assertEquals(result, responseBody.getData());
    }

    @Test
    void activatePaymentMethod_Success() {
        when(paymentMethodService.activatePaymentMethod(anyString())).thenReturn(paymentMethodDTO);

        ResponseEntity<?> response = paymentMethodController.activatePaymentMethod(testId.toString());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Response responseBody = (Response) response.getBody();
        assertEquals("success", responseBody.getStatus());
        assertEquals("Payment method reactivate successfully", responseBody.getMessage());
        assertEquals(paymentMethodDTO, responseBody.getData());
    }

    @Test
    void getAdminPaymentMethodById_Success() {
        when(paymentMethodService.findPaymentMethodById(anyString())).thenReturn(paymentMethodDTO);

        ResponseEntity<?> response = paymentMethodController.getAdminPaymentMethodById(testId.toString());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Response responseBody = (Response) response.getBody();
        assertEquals("success", responseBody.getStatus());
        assertEquals("Payment method retrieved successfully", responseBody.getMessage());
        assertEquals(paymentMethodDTO, responseBody.getData());
    }

    @Test
    void getAdminPaymentMethodById_NotFound() {
        when(paymentMethodService.findPaymentMethodById(anyString())).thenReturn(null);

        ResponseEntity<?> response = paymentMethodController.getAdminPaymentMethodById(testId.toString());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Response responseBody = (Response) response.getBody();
        assertEquals("error", responseBody.getStatus());
        assertEquals("Payment method not found.", responseBody.getMessage());
    }

    @Test
    void getAdminPaymentMethodById_Exception() {
        when(paymentMethodService.findPaymentMethodById(anyString()))
                .thenThrow(new RuntimeException("Database error"));

        ResponseEntity<?> response = paymentMethodController.getAdminPaymentMethodById(testId.toString());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        @SuppressWarnings("unchecked")
        Map<String, String> errorResponse = (Map<String, String>) response.getBody();
        assertEquals("5000", errorResponse.get("code"));
        assertEquals("Internal Server Error", errorResponse.get("message"));
    }

    @Test
    void getAllPaymentMethodsWithOrderDetailsForAdmin_Success() {
        List<PaymentMethodDetailsDTO> details = Arrays.asList(paymentMethodDetailsDTO);
        when(paymentMethodService.getAllPaymentMethodsWithOrderCounts(any(HttpServletRequest.class)))
                .thenReturn(details);

        ResponseEntity<?> response = paymentMethodController
                .getAllPaymentMethodsWithOrderDetailsForAdmin(httpServletRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Response responseBody = (Response) response.getBody();
        assertEquals("success", responseBody.getStatus());
        assertEquals("All payment methods with order counts retrieved successfully for admin",
                responseBody.getMessage());
        assertEquals(details, responseBody.getData());
    }

    @Test
    void getAllPaymentMethodsWithOrderDetailsForAdmin_Exception() {
        when(paymentMethodService.getAllPaymentMethodsWithOrderCounts(any(HttpServletRequest.class)))
                .thenThrow(new RuntimeException("Service error"));

        ResponseEntity<?> response = paymentMethodController
                .getAllPaymentMethodsWithOrderDetailsForAdmin(httpServletRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertNotNull(errorResponse);
    }

    @Test
    void getByTypeForAdmin_Success() {
        List<PaymentMethodDTO> paymentMethods = Arrays.asList(paymentMethodDTO);
        when(paymentMethodService.findByTypeForAdmin(anyString())).thenReturn(paymentMethods);

        ResponseEntity<?> response = paymentMethodController.getByTypeForAdmin("CARD");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Response responseBody = (Response) response.getBody();
        assertEquals("success", responseBody.getStatus());
        assertEquals("Payment methods by type retrieved successfully", responseBody.getMessage());
        assertEquals(paymentMethods, responseBody.getData());
    }

    @Test
    void getByTypeForAdmin_Exception() {
        when(paymentMethodService.findByTypeForAdmin(anyString()))
                .thenThrow(new RuntimeException("Database error"));

        ResponseEntity<?> response = paymentMethodController.getByTypeForAdmin("CARD");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        @SuppressWarnings("unchecked")
        Map<String, String> errorResponse = (Map<String, String>) response.getBody();
        assertEquals("500", errorResponse.get("code"));
        assertEquals("Internal Server Error", errorResponse.get("message"));
    }

    // =========================
    // PUBLIC ENDPOINTS TESTS
    // =========================

    @Test
    void findAllActivePaymentMethods_Success() {
        List<PaymentMethodUserDTO> activePaymentMethods = Arrays.asList(paymentMethodUserDTO);
        when(paymentMethodService.findAllActivePaymentMethods()).thenReturn(activePaymentMethods);

        ResponseEntity<?> response = paymentMethodController.findAllActivePaymentMethods();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Response responseBody = (Response) response.getBody();
        assertEquals("success", responseBody.getStatus());
        assertEquals("Active payment methods retrieved successfully", responseBody.getMessage());
        assertEquals(activePaymentMethods, responseBody.getData());
    }

    @Test
    void findAllActivePaymentMethods_Exception() {
        when(paymentMethodService.findAllActivePaymentMethods())
                .thenThrow(new RuntimeException("Database error"));

        ResponseEntity<?> response = paymentMethodController.findAllActivePaymentMethods();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(ErrorCode.GENERAL_ERROR.toErrorResponse(), response.getBody());
    }

    @Test
    void getActivePaymentMethodById_Success() {
        when(paymentMethodService.findActivePaymentMethodById(anyString())).thenReturn(paymentMethodUserDTO);

        ResponseEntity<?> response = paymentMethodController.getActivePaymentMethodById(testId.toString());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Response responseBody = (Response) response.getBody();
        assertEquals("success", responseBody.getStatus());
        assertEquals("Active payment method retrieved successfully", responseBody.getMessage());
        assertEquals(paymentMethodUserDTO, responseBody.getData());
    }

    @Test
    void getActivePaymentMethodById_NotFound() {
        when(paymentMethodService.findActivePaymentMethodById(anyString())).thenReturn(null);

        ResponseEntity<?> response = paymentMethodController.getActivePaymentMethodById(testId.toString());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Response responseBody = (Response) response.getBody();
        assertEquals("error", responseBody.getStatus());
        assertEquals("Active payment method not found.", responseBody.getMessage());
    }

    @Test
    void getActivePaymentMethodById_Exception() {
        when(paymentMethodService.findActivePaymentMethodById(anyString()))
                .thenThrow(new RuntimeException("Database error"));

        ResponseEntity<?> response = paymentMethodController.getActivePaymentMethodById(testId.toString());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        @SuppressWarnings("unchecked")
        Map<String, String> errorResponse = (Map<String, String>) response.getBody();
        assertEquals("5000", errorResponse.get("code"));
        assertEquals("Internal Server Error", errorResponse.get("message"));
    }

    @Test
    void getByType_Success() {
        List<PaymentMethodUserDTO> paymentMethods = Arrays.asList(paymentMethodUserDTO);
        when(paymentMethodService.findActivePaymentMethodsByType(anyString())).thenReturn(paymentMethods);

        ResponseEntity<?> response = paymentMethodController.getByType("CARD");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Response responseBody = (Response) response.getBody();
        assertEquals("success", responseBody.getStatus());
        assertEquals("Payment methods by type retrieved successfully", responseBody.getMessage());
        assertEquals(paymentMethods, responseBody.getData());
    }

    @Test
    void getByType_Exception() {
        when(paymentMethodService.findActivePaymentMethodsByType(anyString()))
                .thenThrow(new RuntimeException("Database error"));

        ResponseEntity<?> response = paymentMethodController.getByType("CARD");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        @SuppressWarnings("unchecked")
        Map<String, String> errorResponse = (Map<String, String>) response.getBody();
        assertEquals("5000", errorResponse.get("code"));
        assertEquals("Internal Server Error", errorResponse.get("message"));
    }
}