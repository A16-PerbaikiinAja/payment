//package id.ac.ui.cs.advprog.payment.controller;
//
//import id.ac.ui.cs.advprog.payment.dto.Response;
//import id.ac.ui.cs.advprog.payment.dto.paymentmethod.PaymentMethodDTO;
//import id.ac.ui.cs.advprog.payment.dto.paymentmethod.PaymentMethodRegisterDTO;
//import id.ac.ui.cs.advprog.payment.dto.paymentmethod.PaymentMethodUserDTO;
//import id.ac.ui.cs.advprog.payment.enums.Status;
//import id.ac.ui.cs.advprog.payment.service.PaymentMethodService;
//import jakarta.persistence.EntityNotFoundException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class PaymentMethodControllerTest {
//
//    @Mock
//    private PaymentMethodService paymentMethodService;
//
//    @InjectMocks
//    private PaymentMethodController paymentMethodController;
//
//    private PaymentMethodRegisterDTO bankTransferDTO;
//    private PaymentMethodRegisterDTO eWalletDTO;
//    private PaymentMethodRegisterDTO codDTO;
//    private PaymentMethodDTO mockPaymentMethodDTO;
//    private PaymentMethodUserDTO mockPaymentMethodUserDTO;
//    private UUID testUUID;
//    private String testUUIDString;
//
//    @BeforeEach
//    void setUp() {
//        testUUID = UUID.randomUUID();
//        testUUIDString = testUUID.toString();
//
//        // Bank Transfer DTO Setup
//        bankTransferDTO = new PaymentMethodRegisterDTO();
//        bankTransferDTO.setPaymentMethod("BANK_TRANSFER");
//        bankTransferDTO.setAccountName("Test Account");
//        bankTransferDTO.setAccountNumber("1234567890");
//        bankTransferDTO.setBankName("Test Bank");
//
//        // E-Wallet DTO Setup
//        eWalletDTO = new PaymentMethodRegisterDTO();
//        eWalletDTO.setPaymentMethod("E_WALLET");
//        eWalletDTO.setAccountName("Test E-Wallet");
//        eWalletDTO.setInstructions("Scan QR Code");
//        eWalletDTO.setVirtualAccountNumber("9876543210");
//
//        // COD DTO Setup
//        codDTO = new PaymentMethodRegisterDTO();
//        codDTO.setPaymentMethod("COD");
//        codDTO.setPhoneNumber("08123456789");
//        codDTO.setInstructions("Call before delivery");
//
//        // Mock Payment Method DTO
//        mockPaymentMethodDTO = new PaymentMethodDTO();
//        mockPaymentMethodDTO.setId(UUID.fromString(testUUIDString));
//        mockPaymentMethodDTO.setPaymentMethod("BANK_TRANSFER");
//        mockPaymentMethodDTO.setName("Test Account");
//        mockPaymentMethodDTO.setDeletedAt(null);
//
//        // Mock Payment Method User DTO
//        mockPaymentMethodUserDTO = new PaymentMethodUserDTO();
//        mockPaymentMethodUserDTO.setId(UUID.fromString(testUUIDString));
//        mockPaymentMethodUserDTO.setPaymentMethod("BANK_TRANSFER");
//        mockPaymentMethodUserDTO.setName("Test Account");
//    }
//
//    @Test
//    void createPaymentMethod_BankTransfer_Success() {
//        when(paymentMethodService.createPaymentMethod(any(PaymentMethodRegisterDTO.class)))
//                .thenReturn(mockPaymentMethodDTO);
//
//        ResponseEntity<?> response = paymentMethodController.createPaymentMethod(bankTransferDTO);
//
//        assertEquals(HttpStatus.CREATED, response.getStatusCode());
//        Response responseBody = (Response) response.getBody();
//        assertNotNull(responseBody);
//        assertEquals(Status.success.toString(), responseBody.getStatus());
//        assertEquals("Payment method created successfully", responseBody.getMessage());
//        assertEquals(mockPaymentMethodDTO, responseBody.getData());
//        verify(paymentMethodService).createPaymentMethod(any(PaymentMethodRegisterDTO.class));
//    }
//
//    @Test
//    void createPaymentMethod_EWallet_Success() {
//        when(paymentMethodService.createPaymentMethod(any(PaymentMethodRegisterDTO.class)))
//                .thenReturn(mockPaymentMethodDTO);
//
//        ResponseEntity<?> response = paymentMethodController.createPaymentMethod(eWalletDTO);
//
//        assertEquals(HttpStatus.CREATED, response.getStatusCode());
//        Response responseBody = (Response) response.getBody();
//        assertNotNull(responseBody);
//        assertEquals(Status.success.toString(), responseBody.getStatus());
//        verify(paymentMethodService).createPaymentMethod(any(PaymentMethodRegisterDTO.class));
//    }
//
//    @Test
//    void createPaymentMethod_COD_Success() {
//        when(paymentMethodService.createPaymentMethod(any(PaymentMethodRegisterDTO.class)))
//                .thenReturn(mockPaymentMethodDTO);
//
//        ResponseEntity<?> response = paymentMethodController.createPaymentMethod(codDTO);
//
//        assertEquals(HttpStatus.CREATED, response.getStatusCode());
//        Response responseBody = (Response) response.getBody();
//        assertNotNull(responseBody);
//        assertEquals(Status.success.toString(), responseBody.getStatus());
//        verify(paymentMethodService).createPaymentMethod(any(PaymentMethodRegisterDTO.class));
//    }
//
//    @Test
//    void createPaymentMethod_InvalidPaymentMethod_ThrowsException() {
//        PaymentMethodRegisterDTO invalidDTO = new PaymentMethodRegisterDTO();
//        invalidDTO.setPaymentMethod("INVALID_METHOD");
//
//        ResponseEntity<?> response = paymentMethodController.createPaymentMethod(invalidDTO);
//
//        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//        Response responseBody = (Response) response.getBody();
//        assertNotNull(responseBody);
//        assertEquals(Status.error.toString(), responseBody.getStatus());
//        assertEquals("Unsupported payment method: INVALID_METHOD", responseBody.getMessage());
//        verifyNoInteractions(paymentMethodService);
//    }
//
//    @Test
//    void createPaymentMethod_MissingRequiredFields_BankTransfer() {
//        PaymentMethodRegisterDTO incompleteDTO = new PaymentMethodRegisterDTO();
//        incompleteDTO.setPaymentMethod("BANK_TRANSFER");
//        // Missing accountName, accountNumber, bankName
//
//        ResponseEntity<?> response = paymentMethodController.createPaymentMethod(incompleteDTO);
//
//        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//        Response responseBody = (Response) response.getBody();
//        assertNotNull(responseBody);
//        assertEquals(Status.error.toString(), responseBody.getStatus());
//        assertEquals("accountName is required for BANK_TRANSFER", responseBody.getMessage());
//        verifyNoInteractions(paymentMethodService);
//    }
//
//    @Test
//    void createPaymentMethod_MissingRequiredFields_EWallet() {
//        PaymentMethodRegisterDTO incompleteDTO = new PaymentMethodRegisterDTO();
//        incompleteDTO.setPaymentMethod("E_WALLET");
//        // Missing accountName, instructions, virtualAccountNumber
//
//        ResponseEntity<?> response = paymentMethodController.createPaymentMethod(incompleteDTO);
//
//        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//        Response responseBody = (Response) response.getBody();
//        assertNotNull(responseBody);
//        assertEquals(Status.error.toString(), responseBody.getStatus());
//        assertEquals("accountName is required for E_WALLET", responseBody.getMessage());
//        verifyNoInteractions(paymentMethodService);
//    }
//
//    @Test
//    void createPaymentMethod_MissingRequiredFields_COD() {
//        PaymentMethodRegisterDTO incompleteDTO = new PaymentMethodRegisterDTO();
//        incompleteDTO.setPaymentMethod("COD");
//        // Missing phoneNumber, instructions
//
//        ResponseEntity<?> response = paymentMethodController.createPaymentMethod(incompleteDTO);
//
//        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//        Response responseBody = (Response) response.getBody();
//        assertNotNull(responseBody);
//        assertEquals(Status.error.toString(), responseBody.getStatus());
//        assertEquals("phoneNumber is required for COD", responseBody.getMessage());
//        verifyNoInteractions(paymentMethodService);
//    }
//
//    @Test
//    void createPaymentMethod_EmptyPaymentMethod() {
//        PaymentMethodRegisterDTO incompleteDTO = new PaymentMethodRegisterDTO();
//        incompleteDTO.setPaymentMethod("");  // Empty payment method
//
//        ResponseEntity<?> response = paymentMethodController.createPaymentMethod(incompleteDTO);
//
//        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//        Response responseBody = (Response) response.getBody();
//        assertNotNull(responseBody);
//        assertEquals(Status.error.toString(), responseBody.getStatus());
//        assertEquals("paymentMethod cannot be null or empty", responseBody.getMessage());
//        verifyNoInteractions(paymentMethodService);
//    }
//
//    @Test
//    void createPaymentMethod_NullPaymentMethod() {
//        PaymentMethodRegisterDTO incompleteDTO = new PaymentMethodRegisterDTO();
//        incompleteDTO.setPaymentMethod(null);  // Null payment method
//
//        ResponseEntity<?> response = paymentMethodController.createPaymentMethod(incompleteDTO);
//
//        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//        Response responseBody = (Response) response.getBody();
//        assertNotNull(responseBody);
//        assertEquals(Status.error.toString(), responseBody.getStatus());
//        assertEquals("paymentMethod cannot be null or empty", responseBody.getMessage());
//        verifyNoInteractions(paymentMethodService);
//    }
//
//    @Test
//    void createPaymentMethod_EntityNotFoundException() {
//        when(paymentMethodService.createPaymentMethod(any(PaymentMethodRegisterDTO.class)))
//                .thenThrow(new EntityNotFoundException("Entity not found"));
//
//        ResponseEntity<?> response = paymentMethodController.createPaymentMethod(bankTransferDTO);
//
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//        Response responseBody = (Response) response.getBody();
//        assertNotNull(responseBody);
//        assertEquals(Status.error.toString(), responseBody.getStatus());
//        assertEquals("Entity not found", responseBody.getMessage());
//    }
//
//    @Test
//    void createPaymentMethod_GeneralException() {
//        when(paymentMethodService.createPaymentMethod(any(PaymentMethodRegisterDTO.class)))
//                .thenThrow(new RuntimeException("Something went wrong"));
//
//        ResponseEntity<?> response = paymentMethodController.createPaymentMethod(bankTransferDTO);
//
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
//        Response responseBody = (Response) response.getBody();
//        assertNotNull(responseBody);
//        assertEquals(Status.error.toString(), responseBody.getStatus());
//        assertEquals("Internal Server Error", responseBody.getMessage());
//    }
//
//    @Test
//    void findAllPaymentMethods_Success() {
//        List<PaymentMethodDTO> paymentMethodList = Collections.singletonList(mockPaymentMethodDTO);
//        when(paymentMethodService.findAllPaymentMethods()).thenReturn(paymentMethodList);
//
//        ResponseEntity<?> response = paymentMethodController.findAllPaymentMethods();
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        Response responseBody = (Response) response.getBody();
//        assertNotNull(responseBody);
//        assertEquals("success", responseBody.getStatus());
//        assertEquals("Payment methods retrieved successfully", responseBody.getMessage());
//        assertEquals(paymentMethodList, responseBody.getData());
//        verify(paymentMethodService).findAllPaymentMethods();
//    }
//
//    @Test
//    void updatePaymentMethod_Success() {
//        when(paymentMethodService.updatePaymentMethod(eq(testUUID), any(PaymentMethodRegisterDTO.class)))
//                .thenReturn(mockPaymentMethodDTO);
//
//        ResponseEntity<?> response = paymentMethodController.updatePaymentMethod(testUUID, bankTransferDTO);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        Response responseBody = (Response) response.getBody();
//        assertNotNull(responseBody);
//        assertEquals("success", responseBody.getStatus());
//        assertEquals("Payment method updated successfully", responseBody.getMessage());
//        assertEquals(mockPaymentMethodDTO, responseBody.getData());
//        verify(paymentMethodService).updatePaymentMethod(eq(testUUID), any(PaymentMethodRegisterDTO.class));
//    }
//
//    @Test
//    void updatePaymentMethod_EntityNotFoundException() {
//        when(paymentMethodService.updatePaymentMethod(eq(testUUID), any(PaymentMethodRegisterDTO.class)))
//                .thenThrow(new EntityNotFoundException("Payment method not found"));
//
//        ResponseEntity<?> response = paymentMethodController.updatePaymentMethod(testUUID, bankTransferDTO);
//
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//        Response responseBody = (Response) response.getBody();
//        assertNotNull(responseBody);
//        assertEquals("error", responseBody.getStatus());
//        assertEquals("Payment method not found", responseBody.getMessage());
//    }
//
//    @Test
//    void updatePaymentMethod_IllegalArgumentException() {
//        when(paymentMethodService.updatePaymentMethod(eq(testUUID), any(PaymentMethodRegisterDTO.class)))
//                .thenThrow(new IllegalArgumentException("Invalid payment method type"));
//
//        ResponseEntity<?> response = paymentMethodController.updatePaymentMethod(testUUID, bankTransferDTO);
//
//        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//        Response responseBody = (Response) response.getBody();
//        assertNotNull(responseBody);
//        assertEquals("error", responseBody.getStatus());
//        assertEquals("Invalid payment method type", responseBody.getMessage());
//    }
//
//    @Test
//    void updatePaymentMethod_GeneralException() {
//        when(paymentMethodService.updatePaymentMethod(eq(testUUID), any(PaymentMethodRegisterDTO.class)))
//                .thenThrow(new RuntimeException("Something went wrong"));
//
//        ResponseEntity<?> response = paymentMethodController.updatePaymentMethod(testUUID, bankTransferDTO);
//
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
//        Response responseBody = (Response) response.getBody();
//        assertNotNull(responseBody);
//        assertEquals("error", responseBody.getStatus());
//        assertEquals("Internal Server Error", responseBody.getMessage());
//    }
//
//    @Test
//    void deletePaymentMethod_Success() {
//        Map<String, Object> resultMap = new HashMap<>();
//        resultMap.put("id", testUUIDString);
//        resultMap.put("deleted", true);
//
//        when(paymentMethodService.deletePaymentMethod(testUUIDString)).thenReturn(resultMap);
//
//        ResponseEntity<?> response = paymentMethodController.deletePaymentMethod(testUUIDString);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        Response responseBody = (Response) response.getBody();
//        assertNotNull(responseBody);
//        assertEquals("success", responseBody.getStatus());
//        assertEquals("Payment method deleted successfully", responseBody.getMessage());
//        assertEquals(resultMap, responseBody.getData());
//        verify(paymentMethodService).deletePaymentMethod(testUUIDString);
//    }
//
//    @Test
//    void activatePaymentMethod_Success() {
//        when(paymentMethodService.activatePaymentMethod(testUUIDString)).thenReturn(mockPaymentMethodDTO);
//
//        ResponseEntity<?> response = paymentMethodController.activatePaymentMethod(testUUIDString);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        Response responseBody = (Response) response.getBody();
//        assertNotNull(responseBody);
//        assertEquals("success", responseBody.getStatus());
//        assertEquals("Payment method reactivate successfully", responseBody.getMessage());
//        assertEquals(mockPaymentMethodDTO, responseBody.getData());
//        verify(paymentMethodService).activatePaymentMethod(testUUIDString);
//    }
//
//    @Test
//    void getAdminPaymentMethodById_Success() {
//        when(paymentMethodService.findPaymentMethodById(testUUIDString)).thenReturn(mockPaymentMethodDTO);
//
//        ResponseEntity<?> response = paymentMethodController.getAdminPaymentMethodById(testUUIDString);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        Response responseBody = (Response) response.getBody();
//        assertNotNull(responseBody);
//        assertEquals("success", responseBody.getStatus());
//        assertEquals("Payment method retrieved successfully", responseBody.getMessage());
//        assertEquals(mockPaymentMethodDTO, responseBody.getData());
//        verify(paymentMethodService).findPaymentMethodById(testUUIDString);
//    }
//
//    @Test
//    void getAdminPaymentMethodById_NotFound() {
//        when(paymentMethodService.findPaymentMethodById(testUUIDString)).thenReturn(null);
//
//        ResponseEntity<?> response = paymentMethodController.getAdminPaymentMethodById(testUUIDString);
//
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//        Response responseBody = (Response) response.getBody();
//        assertNotNull(responseBody);
//        assertEquals("error", responseBody.getStatus());
//        assertEquals("Payment method not found.", responseBody.getMessage());
//        verify(paymentMethodService).findPaymentMethodById(testUUIDString);
//    }
//
//    @Test
//    void getAdminPaymentMethodById_Exception() {
//        when(paymentMethodService.findPaymentMethodById(testUUIDString)).thenThrow(new RuntimeException("Database error"));
//
//        ResponseEntity<?> response = paymentMethodController.getAdminPaymentMethodById(testUUIDString);
//
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
//        Map<String, String> responseBody = (Map<String, String>) response.getBody();
//        assertNotNull(responseBody);
//        assertEquals("5000", responseBody.get("code"));
//        assertEquals("Internal Server Error", responseBody.get("message"));
//    }
//
//    @Test
//    void findAllActivePaymentMethods_Success() {
//        List<PaymentMethodUserDTO> activePaymentMethods = Collections.singletonList(mockPaymentMethodUserDTO);
//        when(paymentMethodService.findAllActivePaymentMethods()).thenReturn(activePaymentMethods);
//
//        ResponseEntity<?> response = paymentMethodController.findAllActivePaymentMethods();
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        Response responseBody = (Response) response.getBody();
//        assertNotNull(responseBody);
//        assertEquals("success", responseBody.getStatus());
//        assertEquals("Active payment methods retrieved successfully", responseBody.getMessage());
//        assertEquals(activePaymentMethods, responseBody.getData());
//        verify(paymentMethodService).findAllActivePaymentMethods();
//    }
//
//    @Test
//    void getActivePaymentMethodById_Success() {
//        when(paymentMethodService.findActivePaymentMethodById(testUUIDString)).thenReturn(mockPaymentMethodUserDTO);
//
//        ResponseEntity<?> response = paymentMethodController.getActivePaymentMethodById(testUUIDString);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        Response responseBody = (Response) response.getBody();
//        assertNotNull(responseBody);
//        assertEquals("success", responseBody.getStatus());
//        assertEquals("Active payment method retrieved successfully", responseBody.getMessage());
//        assertEquals(mockPaymentMethodUserDTO, responseBody.getData());
//        verify(paymentMethodService).findActivePaymentMethodById(testUUIDString);
//    }
//
//    @Test
//    void getActivePaymentMethodById_NotFound() {
//        when(paymentMethodService.findActivePaymentMethodById(testUUIDString)).thenReturn(null);
//
//        ResponseEntity<?> response = paymentMethodController.getActivePaymentMethodById(testUUIDString);
//
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//        Response responseBody = (Response) response.getBody();
//        assertNotNull(responseBody);
//        assertEquals("error", responseBody.getStatus());
//        assertEquals("Active payment method not found.", responseBody.getMessage());
//        verify(paymentMethodService).findActivePaymentMethodById(testUUIDString);
//    }
//
//    @Test
//    void getActivePaymentMethodById_Exception() {
//        when(paymentMethodService.findActivePaymentMethodById(testUUIDString)).thenThrow(new RuntimeException("Database error"));
//
//        ResponseEntity<?> response = paymentMethodController.getActivePaymentMethodById(testUUIDString);
//
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
//        Map<String, String> responseBody = (Map<String, String>) response.getBody();
//        assertNotNull(responseBody);
//        assertEquals("5000", responseBody.get("code"));
//        assertEquals("Internal Server Error", responseBody.get("message"));
//    }
//
//    @Test
//    void getByType_Success() {
//        List<PaymentMethodUserDTO> paymentMethodByType = Collections.singletonList(mockPaymentMethodUserDTO);
//        when(paymentMethodService.findActivePaymentMethodsByType("BANK_TRANSFER")).thenReturn(paymentMethodByType);
//
//        ResponseEntity<?> response = paymentMethodController.getByType("BANK_TRANSFER");
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        Response responseBody = (Response) response.getBody();
//        assertNotNull(responseBody);
//        assertEquals("success", responseBody.getStatus());
//        assertEquals("Payment methods by type retrieved successfully", responseBody.getMessage());
//        assertEquals(paymentMethodByType, responseBody.getData());
//        verify(paymentMethodService).findActivePaymentMethodsByType("BANK_TRANSFER");
//    }
//
//    @Test
//    void getByType_Exception() {
//        when(paymentMethodService.findActivePaymentMethodsByType("BANK_TRANSFER")).thenThrow(new RuntimeException("Database error"));
//
//        ResponseEntity<?> response = paymentMethodController.getByType("BANK_TRANSFER");
//
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
//        Map<String, String> responseBody = (Map<String, String>) response.getBody();
//        assertNotNull(responseBody);
//        assertEquals("5000", responseBody.get("code"));
//        assertEquals("Internal Server Error", responseBody.get("message"));
//    }
//
//    @Test
//    void getByTypeForAdmin_Success() {
//        List<PaymentMethodDTO> paymentMethodByType = Collections.singletonList(mockPaymentMethodDTO);
//        when(paymentMethodService.findByTypeForAdmin("BANK_TRANSFER")).thenReturn(paymentMethodByType);
//
//        ResponseEntity<?> response = paymentMethodController.getByTypeForAdmin("BANK_TRANSFER");
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        Response responseBody = (Response) response.getBody();
//        assertNotNull(responseBody);
//        assertEquals("success", responseBody.getStatus());
//        assertEquals("Payment methods by type retrieved successfully", responseBody.getMessage());
//        assertEquals(paymentMethodByType, responseBody.getData());
//        verify(paymentMethodService).findByTypeForAdmin("BANK_TRANSFER");
//    }
//
//    @Test
//    void getByTypeForAdmin_Exception() {
//        when(paymentMethodService.findByTypeForAdmin("BANK_TRANSFER")).thenThrow(new RuntimeException("Database error"));
//
//        ResponseEntity<?> response = paymentMethodController.getByTypeForAdmin("BANK_TRANSFER");
//
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
//        Map<String, String> responseBody = (Map<String, String>) response.getBody();
//        assertNotNull(responseBody);
//        assertEquals("500", responseBody.get("code"));
//        assertEquals("Internal Server Error", responseBody.get("message"));
//    }
//}