package id.ac.ui.cs.advprog.payment.controller;

import id.ac.ui.cs.advprog.payment.dto.Response;
import id.ac.ui.cs.advprog.payment.dto.exception.ErrorResponse;
import id.ac.ui.cs.advprog.payment.dto.paymentmethod.PaymentMethodDTO;
import id.ac.ui.cs.advprog.payment.dto.paymentmethod.PaymentMethodDetailsDTO;
import id.ac.ui.cs.advprog.payment.dto.paymentmethod.PaymentMethodRegisterDTO;
import id.ac.ui.cs.advprog.payment.dto.paymentmethod.PaymentMethodUserDTO;
import id.ac.ui.cs.advprog.payment.enums.Status;
import id.ac.ui.cs.advprog.payment.service.PaymentMethodService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentMethodControllerTest {

    @Mock
    private PaymentMethodService paymentMethodService;

    @Mock
    private HttpServletRequest httpServletRequest;

    @InjectMocks
    private PaymentMethodController paymentMethodController;

    private PaymentMethodRegisterDTO bankTransferDTO;
    private PaymentMethodRegisterDTO eWalletDTO;
    private PaymentMethodRegisterDTO codDTO;
    private PaymentMethodDTO mockPaymentMethodDTO;
    private PaymentMethodUserDTO mockPaymentMethodUserDTO;
    private PaymentMethodDetailsDTO mockPaymentMethodDetailsDTO;
    private UUID testUUID;
    private String testUUIDString;

    @BeforeEach
    void setUp() {
        testUUID = UUID.randomUUID();
        testUUIDString = testUUID.toString();

        // Bank Transfer DTO Setup
        bankTransferDTO = new PaymentMethodRegisterDTO();
        bankTransferDTO.setPaymentMethod("BANK_TRANSFER");
        bankTransferDTO.setAccountName("Test Account");
        bankTransferDTO.setAccountNumber("1234567890");
        bankTransferDTO.setBankName("Test Bank");

        // E-Wallet DTO Setup
        eWalletDTO = new PaymentMethodRegisterDTO();
        eWalletDTO.setPaymentMethod("E_WALLET");
        eWalletDTO.setAccountName("Test E-Wallet");
        eWalletDTO.setInstructions("Scan QR Code");
        eWalletDTO.setVirtualAccountNumber("9876543210");

        // COD DTO Setup
        codDTO = new PaymentMethodRegisterDTO();
        codDTO.setPaymentMethod("COD");
        codDTO.setPhoneNumber("08123456789");
        codDTO.setInstructions("Call before delivery");

        // Mock Payment Method DTO
        mockPaymentMethodDTO = new PaymentMethodDTO();
        mockPaymentMethodDTO.setId(testUUID);
        mockPaymentMethodDTO.setPaymentMethod("BANK_TRANSFER");
        mockPaymentMethodDTO.setName("Test Account");

        // Mock Payment Method User DTO
        mockPaymentMethodUserDTO = new PaymentMethodUserDTO();
        mockPaymentMethodUserDTO.setId(testUUID);
        mockPaymentMethodUserDTO.setPaymentMethod("BANK_TRANSFER");
        mockPaymentMethodUserDTO.setName("Test Account");

        // Mock Payment Method Details DTO
        mockPaymentMethodDetailsDTO = new PaymentMethodDetailsDTO();
        mockPaymentMethodDetailsDTO.setId(String.valueOf(testUUID));
        mockPaymentMethodDetailsDTO.setMethodType("BANK_TRANSFER");
        mockPaymentMethodDetailsDTO.setName("Test Account");
        mockPaymentMethodDetailsDTO.setOrderCount(5);
    }

    // ==================== CREATE PAYMENT METHOD TESTS ====================

    @Test
    void createPaymentMethod_BankTransfer_Success() {
        when(paymentMethodService.createPaymentMethod(any(PaymentMethodRegisterDTO.class)))
                .thenReturn(mockPaymentMethodDTO);

        ResponseEntity<?> response = paymentMethodController.createPaymentMethod(bankTransferDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Response responseBody = (Response) response.getBody();
        assertEquals(Status.success.toString(), responseBody.getStatus());
        assertEquals("Payment method created successfully", responseBody.getMessage());
        assertEquals(mockPaymentMethodDTO, responseBody.getData());
        verify(paymentMethodService).createPaymentMethod(bankTransferDTO);
    }

    @Test
    void createPaymentMethod_EWallet_Success() {
        when(paymentMethodService.createPaymentMethod(any(PaymentMethodRegisterDTO.class)))
                .thenReturn(mockPaymentMethodDTO);

        ResponseEntity<?> response = paymentMethodController.createPaymentMethod(eWalletDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Response responseBody = (Response) response.getBody();
        assertEquals(Status.success.toString(), responseBody.getStatus());
        assertEquals("Payment method created successfully", responseBody.getMessage());
        verify(paymentMethodService).createPaymentMethod(eWalletDTO);
    }

    @Test
    void createPaymentMethod_COD_Success() {
        when(paymentMethodService.createPaymentMethod(any(PaymentMethodRegisterDTO.class)))
                .thenReturn(mockPaymentMethodDTO);

        ResponseEntity<?> response = paymentMethodController.createPaymentMethod(codDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Response responseBody = (Response) response.getBody();
        assertEquals(Status.success.toString(), responseBody.getStatus());
        assertEquals("Payment method created successfully", responseBody.getMessage());
        verify(paymentMethodService).createPaymentMethod(codDTO);
    }

    @Test
    void createPaymentMethod_NullPaymentMethod_BadRequest() {
        PaymentMethodRegisterDTO invalidDTO = new PaymentMethodRegisterDTO();
        invalidDTO.setPaymentMethod(null);

        ResponseEntity<?> response = paymentMethodController.createPaymentMethod(invalidDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertEquals(400, errorResponse.getStatus());
        assertEquals("paymentMethod cannot be null or empty", errorResponse.getMessage());
        verify(paymentMethodService, never()).createPaymentMethod(any());
    }

    @Test
    void createPaymentMethod_BlankPaymentMethod_BadRequest() {
        PaymentMethodRegisterDTO invalidDTO = new PaymentMethodRegisterDTO();
        invalidDTO.setPaymentMethod("");

        ResponseEntity<?> response = paymentMethodController.createPaymentMethod(invalidDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertEquals(400, errorResponse.getStatus());
        assertEquals("paymentMethod cannot be null or empty", errorResponse.getMessage());
        verify(paymentMethodService, never()).createPaymentMethod(any());
    }

    @Test
    void createPaymentMethod_COD_MissingPhoneNumber_BadRequest() {
        PaymentMethodRegisterDTO invalidCOD = new PaymentMethodRegisterDTO();
        invalidCOD.setPaymentMethod("COD");
        invalidCOD.setInstructions("Call before delivery");

        ResponseEntity<?> response = paymentMethodController.createPaymentMethod(invalidCOD);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertEquals("phoneNumber is required for COD", errorResponse.getMessage());
        verify(paymentMethodService, never()).createPaymentMethod(any());
    }

    @Test
    void createPaymentMethod_COD_MissingInstructions_BadRequest() {
        PaymentMethodRegisterDTO invalidCOD = new PaymentMethodRegisterDTO();
        invalidCOD.setPaymentMethod("COD");
        invalidCOD.setPhoneNumber("08123456789");

        ResponseEntity<?> response = paymentMethodController.createPaymentMethod(invalidCOD);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertEquals("instruction is required for COD", errorResponse.getMessage());
        verify(paymentMethodService, never()).createPaymentMethod(any());
    }

    @Test
    void createPaymentMethod_EWallet_MissingAccountName_BadRequest() {
        PaymentMethodRegisterDTO invalidEWallet = new PaymentMethodRegisterDTO();
        invalidEWallet.setPaymentMethod("E_WALLET");
        invalidEWallet.setInstructions("Scan QR");
        invalidEWallet.setVirtualAccountNumber("123456");

        ResponseEntity<?> response = paymentMethodController.createPaymentMethod(invalidEWallet);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertEquals("accountName is required for E_WALLET", errorResponse.getMessage());
        verify(paymentMethodService, never()).createPaymentMethod(any());
    }

    @Test
    void createPaymentMethod_EWallet_MissingInstructions_BadRequest() {
        PaymentMethodRegisterDTO invalidEWallet = new PaymentMethodRegisterDTO();
        invalidEWallet.setPaymentMethod("E_WALLET");
        invalidEWallet.setAccountName("Test Account");
        invalidEWallet.setVirtualAccountNumber("123456");

        ResponseEntity<?> response = paymentMethodController.createPaymentMethod(invalidEWallet);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertEquals("instruction is required for E_WALLET", errorResponse.getMessage());
        verify(paymentMethodService, never()).createPaymentMethod(any());
    }

    @Test
    void createPaymentMethod_EWallet_MissingVirtualAccountNumber_BadRequest() {
        PaymentMethodRegisterDTO invalidEWallet = new PaymentMethodRegisterDTO();
        invalidEWallet.setPaymentMethod("E_WALLET");
        invalidEWallet.setAccountName("Test Account");
        invalidEWallet.setInstructions("Scan QR");

        ResponseEntity<?> response = paymentMethodController.createPaymentMethod(invalidEWallet);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertEquals("virtualAccountNumber is required for E_WALLET", errorResponse.getMessage());
        verify(paymentMethodService, never()).createPaymentMethod(any());
    }

    @Test
    void createPaymentMethod_BankTransfer_MissingAccountName_BadRequest() {
        PaymentMethodRegisterDTO invalidBankTransfer = new PaymentMethodRegisterDTO();
        invalidBankTransfer.setPaymentMethod("BANK_TRANSFER");
        invalidBankTransfer.setAccountNumber("123456");
        invalidBankTransfer.setBankName("Test Bank");

        ResponseEntity<?> response = paymentMethodController.createPaymentMethod(invalidBankTransfer);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertEquals("accountName is required for BANK_TRANSFER", errorResponse.getMessage());
        verify(paymentMethodService, never()).createPaymentMethod(any());
    }

    @Test
    void createPaymentMethod_BankTransfer_MissingAccountNumber_BadRequest() {
        PaymentMethodRegisterDTO invalidBankTransfer = new PaymentMethodRegisterDTO();
        invalidBankTransfer.setPaymentMethod("BANK_TRANSFER");
        invalidBankTransfer.setAccountName("Test Account");
        invalidBankTransfer.setBankName("Test Bank");

        ResponseEntity<?> response = paymentMethodController.createPaymentMethod(invalidBankTransfer);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertEquals("accountNumber is required for BANK_TRANSFER", errorResponse.getMessage());
        verify(paymentMethodService, never()).createPaymentMethod(any());
    }

    @Test
    void createPaymentMethod_BankTransfer_MissingBankName_BadRequest() {
        PaymentMethodRegisterDTO invalidBankTransfer = new PaymentMethodRegisterDTO();
        invalidBankTransfer.setPaymentMethod("BANK_TRANSFER");
        invalidBankTransfer.setAccountName("Test Account");
        invalidBankTransfer.setAccountNumber("123456");

        ResponseEntity<?> response = paymentMethodController.createPaymentMethod(invalidBankTransfer);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertEquals("bankName is required for BANK_TRANSFER", errorResponse.getMessage());
        verify(paymentMethodService, never()).createPaymentMethod(any());
    }

    @Test
    void createPaymentMethod_UnsupportedPaymentMethod_BadRequest() {
        PaymentMethodRegisterDTO invalidDTO = new PaymentMethodRegisterDTO();
        invalidDTO.setPaymentMethod("INVALID_METHOD");

        ResponseEntity<?> response = paymentMethodController.createPaymentMethod(invalidDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertEquals("Unsupported payment method: INVALID_METHOD", errorResponse.getMessage());
        verify(paymentMethodService, never()).createPaymentMethod(any());
    }

    @Test
    void createPaymentMethod_ServiceThrowsException_InternalServerError() {
        when(paymentMethodService.createPaymentMethod(any(PaymentMethodRegisterDTO.class)))
                .thenThrow(new RuntimeException("Database error"));

        ResponseEntity<?> response = paymentMethodController.createPaymentMethod(bankTransferDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertEquals(500, errorResponse.getStatus());
        assertTrue(errorResponse.getMessage().contains("An unexpected error occurred"));
    }

    // ==================== FIND ALL PAYMENT METHODS TESTS ====================

    @Test
    void findAllPaymentMethods_Success() {
        List<PaymentMethodDTO> mockList = Arrays.asList(mockPaymentMethodDTO);
        when(paymentMethodService.findAllPaymentMethods()).thenReturn(mockList);

        ResponseEntity<?> response = paymentMethodController.findAllPaymentMethods();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Response responseBody = (Response) response.getBody();
        assertEquals("success", responseBody.getStatus());
        assertEquals("Payment methods retrieved successfully", responseBody.getMessage());
        assertEquals(mockList, responseBody.getData());
    }

    @Test
    void findAllPaymentMethods_ServiceThrowsException_InternalServerError() {
        when(paymentMethodService.findAllPaymentMethods()).thenThrow(new RuntimeException("Database error"));

        ResponseEntity<?> response = paymentMethodController.findAllPaymentMethods();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    // ==================== UPDATE PAYMENT METHOD TESTS ====================

    @Test
    void updatePaymentMethod_Success() {
        when(paymentMethodService.updatePaymentMethod(eq(testUUID), any(PaymentMethodRegisterDTO.class)))
                .thenReturn(mockPaymentMethodDTO);

        ResponseEntity<?> response = paymentMethodController.updatePaymentMethod(testUUID, bankTransferDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Response responseBody = (Response) response.getBody();
        assertEquals("success", responseBody.getStatus());
        assertEquals("Payment method updated successfully", responseBody.getMessage());
        assertEquals(mockPaymentMethodDTO, responseBody.getData());
    }

    @Test
    void updatePaymentMethod_IllegalArgumentException_BadRequest() {
        when(paymentMethodService.updatePaymentMethod(eq(testUUID), any(PaymentMethodRegisterDTO.class)))
                .thenThrow(new IllegalArgumentException("Invalid payment method"));

        ResponseEntity<?> response = paymentMethodController.updatePaymentMethod(testUUID, bankTransferDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Response responseBody = (Response) response.getBody();
        assertEquals("error", responseBody.getStatus());
        assertEquals("Invalid payment method", responseBody.getMessage());
    }

    @Test
    void updatePaymentMethod_IllegalStateException_BadRequest() {
        when(paymentMethodService.updatePaymentMethod(eq(testUUID), any(PaymentMethodRegisterDTO.class)))
                .thenThrow(new IllegalStateException("Payment method is deleted"));

        ResponseEntity<?> response = paymentMethodController.updatePaymentMethod(testUUID, bankTransferDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Response responseBody = (Response) response.getBody();
        assertEquals("error", responseBody.getStatus());
        assertEquals("Payment method is deleted", responseBody.getMessage());
    }

    @Test
    void updatePaymentMethod_GeneralException_InternalServerError() {
        when(paymentMethodService.updatePaymentMethod(eq(testUUID), any(PaymentMethodRegisterDTO.class)))
                .thenThrow(new RuntimeException("Database error"));

        ResponseEntity<?> response = paymentMethodController.updatePaymentMethod(testUUID, bankTransferDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Response responseBody = (Response) response.getBody();
        assertEquals("error", responseBody.getStatus());
        assertEquals("Internal Server Error", responseBody.getMessage());
    }

    // ==================== DELETE PAYMENT METHOD TESTS ====================

    @Test
    void deletePaymentMethod_Success() {
        Map<String, Object> mockResult = new HashMap<>();
        mockResult.put("deleted", true);
        when(paymentMethodService.deletePaymentMethod(testUUIDString)).thenReturn(mockResult);

        ResponseEntity<?> response = paymentMethodController.deletePaymentMethod(testUUIDString);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Response responseBody = (Response) response.getBody();
        assertEquals("success", responseBody.getStatus());
        assertEquals("Payment method deleted successfully", responseBody.getMessage());
        assertEquals(mockResult, responseBody.getData());
    }

    // ==================== ACTIVATE PAYMENT METHOD TESTS ====================

    @Test
    void activatePaymentMethod_Success() {
        when(paymentMethodService.activatePaymentMethod(testUUIDString)).thenReturn(mockPaymentMethodDTO);

        ResponseEntity<?> response = paymentMethodController.activatePaymentMethod(testUUIDString);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Response responseBody = (Response) response.getBody();
        assertEquals("success", responseBody.getStatus());
        assertEquals("Payment method reactivate successfully", responseBody.getMessage());
        assertEquals(mockPaymentMethodDTO, responseBody.getData());
    }

    // ==================== GET ADMIN PAYMENT METHOD BY ID TESTS ====================

    @Test
    void getAdminPaymentMethodById_Success() {
        when(paymentMethodService.findPaymentMethodById(testUUIDString)).thenReturn(mockPaymentMethodDTO);

        ResponseEntity<?> response = paymentMethodController.getAdminPaymentMethodById(testUUIDString);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Response responseBody = (Response) response.getBody();
        assertEquals("success", responseBody.getStatus());
        assertEquals("Payment method retrieved successfully", responseBody.getMessage());
        assertEquals(mockPaymentMethodDTO, responseBody.getData());
    }

    @Test
    void getAdminPaymentMethodById_NotFound() {
        when(paymentMethodService.findPaymentMethodById(testUUIDString)).thenReturn(null);

        ResponseEntity<?> response = paymentMethodController.getAdminPaymentMethodById(testUUIDString);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Response responseBody = (Response) response.getBody();
        assertEquals("error", responseBody.getStatus());
        assertEquals("Payment method not found.", responseBody.getMessage());
        assertNull(responseBody.getData());
    }

    @Test
    void getAdminPaymentMethodById_ServiceThrowsException_InternalServerError() {
        when(paymentMethodService.findPaymentMethodById(testUUIDString))
                .thenThrow(new RuntimeException("Database error"));

        ResponseEntity<?> response = paymentMethodController.getAdminPaymentMethodById(testUUIDString);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        @SuppressWarnings("unchecked")
        Map<String, String> errorResponse = (Map<String, String>) response.getBody();
        assertEquals("5000", errorResponse.get("code"));
        assertEquals("Internal Server Error", errorResponse.get("message"));
    }

    // ==================== GET ALL PAYMENT METHODS WITH ORDER COUNTS TESTS ====================

    @Test
    void getAllPaymentMethodsWithOrderDetailsForAdmin_Success() {
        List<PaymentMethodDetailsDTO> mockList = Arrays.asList(mockPaymentMethodDetailsDTO);
        when(paymentMethodService.getAllPaymentMethodsWithOrderCounts(httpServletRequest)).thenReturn(mockList);

        ResponseEntity<?> response = paymentMethodController.getAllPaymentMethodsWithOrderDetailsForAdmin(httpServletRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Response responseBody = (Response) response.getBody();
        assertEquals(Status.success.toString(), responseBody.getStatus());
        assertEquals("All payment methods with order counts retrieved successfully for admin", responseBody.getMessage());
        assertEquals(mockList, responseBody.getData());
    }

    @Test
    void getAllPaymentMethodsWithOrderDetailsForAdmin_ServiceThrowsException_InternalServerError() {
        when(paymentMethodService.getAllPaymentMethodsWithOrderCounts(httpServletRequest))
                .thenThrow(new RuntimeException("Database error"));

        ResponseEntity<?> response = paymentMethodController.getAllPaymentMethodsWithOrderDetailsForAdmin(httpServletRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertEquals(500, errorResponse.getStatus());
        assertTrue(errorResponse.getMessage().contains("Failed to retrieve payment methods with order counts for admin"));
    }

    // ==================== FIND ALL ACTIVE PAYMENT METHODS TESTS ====================

    @Test
    void findAllActivePaymentMethods_Success() {
        List<PaymentMethodUserDTO> mockList = Arrays.asList(mockPaymentMethodUserDTO);
        when(paymentMethodService.findAllActivePaymentMethods()).thenReturn(mockList);

        ResponseEntity<?> response = paymentMethodController.findAllActivePaymentMethods();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Response responseBody = (Response) response.getBody();
        assertEquals("success", responseBody.getStatus());
        assertEquals("Active payment methods retrieved successfully", responseBody.getMessage());
        assertEquals(mockList, responseBody.getData());
    }

    @Test
    void findAllActivePaymentMethods_ServiceThrowsException_InternalServerError() {
        when(paymentMethodService.findAllActivePaymentMethods()).thenThrow(new RuntimeException("Database error"));

        ResponseEntity<?> response = paymentMethodController.findAllActivePaymentMethods();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    // ==================== GET ACTIVE PAYMENT METHOD BY ID TESTS ====================

    @Test
    void getActivePaymentMethodById_Success() {
        when(paymentMethodService.findActivePaymentMethodById(testUUIDString)).thenReturn(mockPaymentMethodUserDTO);

        ResponseEntity<?> response = paymentMethodController.getActivePaymentMethodById(testUUIDString);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Response responseBody = (Response) response.getBody();
        assertEquals("success", responseBody.getStatus());
        assertEquals("Active payment method retrieved successfully", responseBody.getMessage());
        assertEquals(mockPaymentMethodUserDTO, responseBody.getData());
    }

    @Test
    void getActivePaymentMethodById_NotFound() {
        when(paymentMethodService.findActivePaymentMethodById(testUUIDString)).thenReturn(null);

        ResponseEntity<?> response = paymentMethodController.getActivePaymentMethodById(testUUIDString);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Response responseBody = (Response) response.getBody();
        assertEquals("error", responseBody.getStatus());
        assertEquals("Active payment method not found.", responseBody.getMessage());
        assertNull(responseBody.getData());
    }

    @Test
    void getActivePaymentMethodById_ServiceThrowsException_InternalServerError() {
        when(paymentMethodService.findActivePaymentMethodById(testUUIDString))
                .thenThrow(new RuntimeException("Database error"));

        ResponseEntity<?> response = paymentMethodController.getActivePaymentMethodById(testUUIDString);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        @SuppressWarnings("unchecked")
        Map<String, String> errorResponse = (Map<String, String>) response.getBody();
        assertEquals("5000", errorResponse.get("code"));
        assertEquals("Internal Server Error", errorResponse.get("message"));
    }

    // ==================== GET BY TYPE TESTS ====================

    @Test
    void getByType_Success() {
        List<PaymentMethodUserDTO> mockList = Arrays.asList(mockPaymentMethodUserDTO);
        when(paymentMethodService.findActivePaymentMethodsByType("BANK_TRANSFER")).thenReturn(mockList);

        ResponseEntity<?> response = paymentMethodController.getByType("BANK_TRANSFER");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Response responseBody = (Response) response.getBody();
        assertEquals("success", responseBody.getStatus());
        assertEquals("Payment methods by type retrieved successfully", responseBody.getMessage());
        assertEquals(mockList, responseBody.getData());
    }

    @Test
    void getByType_ServiceThrowsException_InternalServerError() {
        when(paymentMethodService.findActivePaymentMethodsByType("BANK_TRANSFER"))
                .thenThrow(new RuntimeException("Database error"));

        ResponseEntity<?> response = paymentMethodController.getByType("BANK_TRANSFER");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        @SuppressWarnings("unchecked")
        Map<String, String> errorResponse = (Map<String, String>) response.getBody();
        assertEquals("5000", errorResponse.get("code"));
        assertEquals("Internal Server Error", errorResponse.get("message"));
    }

    // ==================== GET BY TYPE FOR ADMIN TESTS ====================

    @Test
    void getByTypeForAdmin_Success() {
        List<PaymentMethodDTO> mockList = Arrays.asList(mockPaymentMethodDTO);
        when(paymentMethodService.findByTypeForAdmin("BANK_TRANSFER")).thenReturn(mockList);

        ResponseEntity<?> response = paymentMethodController.getByTypeForAdmin("BANK_TRANSFER");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Response responseBody = (Response) response.getBody();
        assertEquals("success", responseBody.getStatus());
        assertEquals("Payment methods by type retrieved successfully", responseBody.getMessage());
        assertEquals(mockList, responseBody.getData());
    }

    @Test
    void getByTypeForAdmin_ServiceThrowsException_InternalServerError() {
        when(paymentMethodService.findByTypeForAdmin("BANK_TRANSFER"))
                .thenThrow(new RuntimeException("Database error"));

        ResponseEntity<?> response = paymentMethodController.getByTypeForAdmin("BANK_TRANSFER");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        @SuppressWarnings("unchecked")
        Map<String, String> errorResponse = (Map<String, String>) response.getBody();
        assertEquals("500", errorResponse.get("code"));
        assertEquals("Internal Server Error", errorResponse.get("message"));
    }

    // ==================== ADDITIONAL EDGE CASE TESTS FOR COMPLETE COVERAGE ====================

    @Test
    void createPaymentMethod_COD_BlankPhoneNumber_BadRequest() {
        PaymentMethodRegisterDTO invalidCOD = new PaymentMethodRegisterDTO();
        invalidCOD.setPaymentMethod("COD");
        invalidCOD.setPhoneNumber("");
        invalidCOD.setInstructions("Call before delivery");

        ResponseEntity<?> response = paymentMethodController.createPaymentMethod(invalidCOD);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertEquals("phoneNumber is required for COD", errorResponse.getMessage());
    }

    @Test
    void createPaymentMethod_COD_BlankInstructions_BadRequest() {
        PaymentMethodRegisterDTO invalidCOD = new PaymentMethodRegisterDTO();
        invalidCOD.setPaymentMethod("COD");
        invalidCOD.setPhoneNumber("08123456789");
        invalidCOD.setInstructions("");

        ResponseEntity<?> response = paymentMethodController.createPaymentMethod(invalidCOD);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertEquals("instruction is required for COD", errorResponse.getMessage());
    }

    @Test
    void createPaymentMethod_EWallet_BlankAccountName_BadRequest() {
        PaymentMethodRegisterDTO invalidEWallet = new PaymentMethodRegisterDTO();
        invalidEWallet.setPaymentMethod("E_WALLET");
        invalidEWallet.setAccountName("");
        invalidEWallet.setInstructions("Scan QR");
        invalidEWallet.setVirtualAccountNumber("123456");

        ResponseEntity<?> response = paymentMethodController.createPaymentMethod(invalidEWallet);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertEquals("accountName is required for E_WALLET", errorResponse.getMessage());
    }

    @Test
    void createPaymentMethod_EWallet_BlankInstructions_BadRequest() {
        PaymentMethodRegisterDTO invalidEWallet = new PaymentMethodRegisterDTO();
        invalidEWallet.setPaymentMethod("E_WALLET");
        invalidEWallet.setAccountName("Test Account");
        invalidEWallet.setInstructions("");
        invalidEWallet.setVirtualAccountNumber("123456");

        ResponseEntity<?> response = paymentMethodController.createPaymentMethod(invalidEWallet);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertEquals("instruction is required for E_WALLET", errorResponse.getMessage());
    }

    @Test
    void createPaymentMethod_EWallet_BlankVirtualAccountNumber_BadRequest() {
        PaymentMethodRegisterDTO invalidEWallet = new PaymentMethodRegisterDTO();
        invalidEWallet.setPaymentMethod("E_WALLET");
        invalidEWallet.setAccountName("Test Account");
        invalidEWallet.setInstructions("Scan QR");
        invalidEWallet.setVirtualAccountNumber("");

        ResponseEntity<?> response = paymentMethodController.createPaymentMethod(invalidEWallet);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertEquals("virtualAccountNumber is required for E_WALLET", errorResponse.getMessage());
    }

    @Test
    void createPaymentMethod_BankTransfer_BlankAccountName_BadRequest() {
        PaymentMethodRegisterDTO invalidBankTransfer = new PaymentMethodRegisterDTO();
        invalidBankTransfer.setPaymentMethod("BANK_TRANSFER");
        invalidBankTransfer.setAccountName("");
        invalidBankTransfer.setAccountNumber("123456");
        invalidBankTransfer.setBankName("Test Bank");

        ResponseEntity<?> response = paymentMethodController.createPaymentMethod(invalidBankTransfer);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertEquals("accountName is required for BANK_TRANSFER", errorResponse.getMessage());
    }

    @Test
    void createPaymentMethod_BankTransfer_BlankAccountNumber_BadRequest() {
        PaymentMethodRegisterDTO invalidBankTransfer = new PaymentMethodRegisterDTO();
        invalidBankTransfer.setPaymentMethod("BANK_TRANSFER");
        invalidBankTransfer.setAccountName("Test Account");
        invalidBankTransfer.setAccountNumber("");
        invalidBankTransfer.setBankName("Test Bank");

        ResponseEntity<?> response = paymentMethodController.createPaymentMethod(invalidBankTransfer);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertEquals("accountNumber is required for BANK_TRANSFER", errorResponse.getMessage());
    }

    @Test
    void createPaymentMethod_BankTransfer_BlankBankName_BadRequest() {
        PaymentMethodRegisterDTO invalidBankTransfer = new PaymentMethodRegisterDTO();
        invalidBankTransfer.setPaymentMethod("BANK_TRANSFER");
        invalidBankTransfer.setAccountName("Test Account");
        invalidBankTransfer.setAccountNumber("123456");
        invalidBankTransfer.setBankName("");

        ResponseEntity<?> response = paymentMethodController.createPaymentMethod(invalidBankTransfer);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertEquals("bankName is required for BANK_TRANSFER", errorResponse.getMessage());
    }

    @Test
    void createPaymentMethod_CaseInsensitivePaymentMethod_Success() {
        PaymentMethodRegisterDTO codDTOLowerCase = new PaymentMethodRegisterDTO();
        codDTOLowerCase.setPaymentMethod("cod");
        codDTOLowerCase.setPhoneNumber("08123456789");
        codDTOLowerCase.setInstructions("Call before delivery");

        when(paymentMethodService.createPaymentMethod(any(PaymentMethodRegisterDTO.class)))
                .thenReturn(mockPaymentMethodDTO);

        ResponseEntity<?> response = paymentMethodController.createPaymentMethod(codDTOLowerCase);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Response responseBody = (Response) response.getBody();
        assertEquals(Status.success.toString(), responseBody.getStatus());
        assertEquals("Payment method created successfully", responseBody.getMessage());
        verify(paymentMethodService).createPaymentMethod(codDTOLowerCase);
    }
}