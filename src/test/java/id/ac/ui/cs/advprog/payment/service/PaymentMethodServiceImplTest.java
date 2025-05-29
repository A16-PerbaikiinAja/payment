package id.ac.ui.cs.advprog.payment.service;

import id.ac.ui.cs.advprog.payment.dto.paymentmethod.*;
import id.ac.ui.cs.advprog.payment.external.OrderServiceClient;
import id.ac.ui.cs.advprog.payment.model.*;
import id.ac.ui.cs.advprog.payment.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PaymentMethodServiceImplTest {

    private PaymentMethodRepository paymentMethodRepository;
    private CODRepository codRepository;
    private BankTransferRepository bankTransferRepository;
    private EWalletRepository eWalletRepository;
    private OrderServiceClient orderServiceClient;
    private PaymentMethodServiceImpl paymentMethodService;
    private Authentication authentication;
    private SecurityContext securityContext;
    private ExecutorService mockExecutor;
    private HttpServletRequest mockRequest;


    private final UUID USER_ID = UUID.randomUUID();
    private final UUID PAYMENT_METHOD_ID = UUID.randomUUID();

    private PaymentMethodDetailsDTO sampleDTO;
    private List<PaymentMethodDetailsDTO> sampleDTOList;
    private PaymentMethodServiceImpl paymentMethodServiceSpy;

    @BeforeEach
    void setUp() {
        // Create mocks
        paymentMethodRepository = mock(PaymentMethodRepository.class);
        codRepository = mock(CODRepository.class);
        bankTransferRepository = mock(BankTransferRepository.class);
        eWalletRepository = mock(EWalletRepository.class);
        orderServiceClient = mock(OrderServiceClient.class);
        authentication = mock(Authentication.class);
        securityContext = mock(SecurityContext.class);
        mockExecutor = mock(ExecutorService.class);
        mockRequest = mock(HttpServletRequest.class);

        // Create service instance
        paymentMethodService = new PaymentMethodServiceImpl(orderServiceClient, codRepository, bankTransferRepository, eWalletRepository);

        // Set fields using reflection
        ReflectionTestUtils.setField(paymentMethodService, "paymentMethodRepository", paymentMethodRepository);
        ReflectionTestUtils.setField(paymentMethodService, "customExecutor", mockExecutor);

        // Security context setup
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(USER_ID.toString());

        // Inisialisasi spy untuk testing method yang memanggil method lain di kelas yang sama
        paymentMethodServiceSpy = spy(paymentMethodService);

        // Setup sample DTO
        sampleDTO = new PaymentMethodDetailsDTO();
        sampleDTO.setId(PAYMENT_METHOD_ID.toString());
        sampleDTO.setName("Test Method");
        sampleDTO.setOrderCount(10);
        sampleDTOList = Collections.singletonList(sampleDTO);

    }

    @Nested
    class GetCurrentUserIdTest {

        @Test
        void getCurrentUserId_WhenAuthenticationIsNull_ShouldThrowRuntimeException() {
            when(securityContext.getAuthentication()).thenReturn(null);

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> paymentMethodService.getCurrentUserId());
            assertEquals("User not authenticated", exception.getMessage());
        }

        @Test
        void getCurrentUserId_WhenUserNotAuthenticated_ShouldThrowRuntimeException() {
            when(authentication.isAuthenticated()).thenReturn(false);

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> paymentMethodService.getCurrentUserId());
            assertEquals("User not authenticated", exception.getMessage());
        }

        @Test
        void getCurrentUserId_WhenPrincipalIsNotString_ShouldThrowRuntimeException() {
            when(authentication.getPrincipal()).thenReturn(123); // Not a String

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> paymentMethodService.getCurrentUserId());
            assertEquals("Cannot extract user id from principal", exception.getMessage());
        }

        @Test
        void getCurrentUserId_WhenValidAuthentication_ShouldReturnUserId() {
            UUID result = paymentMethodService.getCurrentUserId();
            assertEquals(USER_ID, result);
        }
    }

    @Nested
    class CreatePaymentMethodTest {

        @Test
        void createPaymentMethod_COD_Success() {
            PaymentMethodRegisterDTO dto = new PaymentMethodRegisterDTO();
            dto.setName("Test COD");
            dto.setDescription("Test Description");
            dto.setProcessingFee(BigDecimal.valueOf(5000));
            dto.setPaymentMethod("COD");
            dto.setPhoneNumber("081234567890");
            dto.setInstructions("COD Instructions");

            COD mockCOD = new COD();
            mockCOD.setId(PAYMENT_METHOD_ID);
            mockCOD.setName("Test COD");
            mockCOD.setDescription("Test Description");
            mockCOD.setProcessingFee(BigDecimal.valueOf(5000));
            mockCOD.setPhoneNumber("081234567890");
            mockCOD.setInstructions("COD Instructions");

            when(paymentMethodRepository.save(any(COD.class))).thenReturn(mockCOD);

            PaymentMethodDTO result = paymentMethodService.createPaymentMethod(dto);

            assertNotNull(result);
            verify(paymentMethodRepository).save(any(COD.class));
        }

        @Test
        void createPaymentMethod_BankTransfer_Success() {
            PaymentMethodRegisterDTO dto = new PaymentMethodRegisterDTO();
            dto.setName("Test Bank Transfer");
            dto.setDescription("Test Description");
            dto.setProcessingFee(BigDecimal.valueOf(2500));
            dto.setPaymentMethod("BANK_TRANSFER");
            dto.setAccountName("Test Account");
            dto.setAccountNumber("1234567890");
            dto.setBankName("Test Bank");

            BankTransfer mockBT = new BankTransfer();
            mockBT.setId(PAYMENT_METHOD_ID);
            mockBT.setName("Test Bank Transfer");
            mockBT.setDescription("Test Description");
            mockBT.setProcessingFee(BigDecimal.valueOf(2500));
            mockBT.setAccountName("Test Account");
            mockBT.setAccountNumber("1234567890");
            mockBT.setBankName("Test Bank");

            when(paymentMethodRepository.save(any(BankTransfer.class))).thenReturn(mockBT);

            PaymentMethodDTO result = paymentMethodService.createPaymentMethod(dto);

            assertNotNull(result);
            verify(paymentMethodRepository).save(any(BankTransfer.class));
        }

        @Test
        void createPaymentMethod_EWallet_Success() {
            PaymentMethodRegisterDTO dto = new PaymentMethodRegisterDTO();
            dto.setName("Test E-Wallet");
            dto.setDescription("Test Description");
            dto.setProcessingFee(BigDecimal.valueOf(1000));
            dto.setPaymentMethod("E_WALLET");
            dto.setAccountName("Test E-Wallet Account");
            dto.setVirtualAccountNumber("9876543210");
            dto.setInstructions("E-Wallet Instructions");

            EWallet mockEW = new EWallet();
            mockEW.setId(PAYMENT_METHOD_ID);
            mockEW.setName("Test E-Wallet");
            mockEW.setDescription("Test Description");
            mockEW.setProcessingFee(BigDecimal.valueOf(1000));
            mockEW.setAccountName("Test E-Wallet Account");
            mockEW.setVirtualAccountNumber("9876543210");
            mockEW.setInstructions("E-Wallet Instructions");

            when(paymentMethodRepository.save(any(EWallet.class))).thenReturn(mockEW);

            PaymentMethodDTO result = paymentMethodService.createPaymentMethod(dto);

            assertNotNull(result);
            verify(paymentMethodRepository).save(any(EWallet.class));
        }

        @Test
        void createPaymentMethod_InvalidType_ShouldThrowException() {
            PaymentMethodRegisterDTO dto = new PaymentMethodRegisterDTO();
            dto.setName("Test COD");
            dto.setDescription("Test Description");
            dto.setProcessingFee(BigDecimal.valueOf(5000));
            dto.setPaymentMethod("INVALID");
            dto.setPhoneNumber("081234567890");
            dto.setInstructions("COD Instructions");

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> paymentMethodService.createPaymentMethod(dto));
            assertTrue(exception.getMessage().contains("Failed to create payment method"));
        }

        @Test
        void createPaymentMethod_RepositoryException_ShouldThrowRuntimeException() {
            PaymentMethodRegisterDTO dto = new PaymentMethodRegisterDTO();
            dto.setName("Test COD");
            dto.setDescription("Test Description");
            dto.setProcessingFee(BigDecimal.valueOf(5000));
            dto.setPaymentMethod("COD");
            dto.setPhoneNumber("081234567890");
            dto.setInstructions("COD Instructions");

            when(paymentMethodRepository.save(any())).thenThrow(new RuntimeException("Database error"));

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> paymentMethodService.createPaymentMethod(dto));
            assertTrue(exception.getMessage().contains("Failed to create payment method"));
        }
    }

    @Nested
    class ActivatePaymentMethodTest {

        @Test
        void activatePaymentMethod_Success() {
            COD mockPaymentMethod = new COD();
            mockPaymentMethod.setId(PAYMENT_METHOD_ID);
            mockPaymentMethod.setName("Test COD");
            mockPaymentMethod.setDescription("Test Description");
            mockPaymentMethod.setProcessingFee(BigDecimal.valueOf(5000));
            mockPaymentMethod.setDeletedAt(Timestamp.valueOf(LocalDateTime.now()));

            when(paymentMethodRepository.findById(PAYMENT_METHOD_ID)).thenReturn(Optional.of(mockPaymentMethod));
            when(paymentMethodRepository.save(any())).thenReturn(mockPaymentMethod);

            PaymentMethodDTO result = paymentMethodService.activatePaymentMethod(PAYMENT_METHOD_ID.toString());

            assertNotNull(result);
            assertNull(mockPaymentMethod.getDeletedAt());
            verify(paymentMethodRepository).save(mockPaymentMethod);
        }

        @Test
        void activatePaymentMethod_NotFound_ShouldThrowEntityNotFoundException() {
            when(paymentMethodRepository.findById(PAYMENT_METHOD_ID)).thenReturn(Optional.empty());

            EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                    () -> paymentMethodService.activatePaymentMethod(PAYMENT_METHOD_ID.toString()));
            assertTrue(exception.getMessage().contains("Payment method not found"));
        }

        @Test
        void activatePaymentMethod_AlreadyActive_ShouldThrowEntityNotFoundException() {
            COD mockPaymentMethod = new COD();
            mockPaymentMethod.setId(PAYMENT_METHOD_ID);
            mockPaymentMethod.setName("Test COD");
            mockPaymentMethod.setDescription("Test Description");
            mockPaymentMethod.setProcessingFee(BigDecimal.valueOf(5000));
            mockPaymentMethod.setDeletedAt(null);

            when(paymentMethodRepository.findById(PAYMENT_METHOD_ID)).thenReturn(Optional.of(mockPaymentMethod));

            EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                    () -> paymentMethodService.activatePaymentMethod(PAYMENT_METHOD_ID.toString()));
            assertTrue(exception.getMessage().contains("already active"));
        }
    }

    @Nested
    class UpdatePaymentMethodTest {

        @Test
        void updatePaymentMethod_COD_Success() {
            PaymentMethodRegisterDTO dto = new PaymentMethodRegisterDTO();
            dto.setName("Updated COD");
            dto.setDescription("Updated Description");
            dto.setProcessingFee(BigDecimal.valueOf(6000));
            dto.setPaymentMethod("COD");
            dto.setPhoneNumber("087654321098");
            dto.setInstructions("Updated COD Instructions");

            COD existingCOD = new COD();
            existingCOD.setId(PAYMENT_METHOD_ID);
            existingCOD.setName("Test COD");
            existingCOD.setDescription("Test Description");
            existingCOD.setProcessingFee(BigDecimal.valueOf(5000));
            existingCOD.setDeletedAt(null);

            when(paymentMethodRepository.findById(PAYMENT_METHOD_ID)).thenReturn(Optional.of(existingCOD));
            when(paymentMethodRepository.save(any(COD.class))).thenReturn(existingCOD);

            PaymentMethodDTO result = paymentMethodService.updatePaymentMethod(PAYMENT_METHOD_ID, dto);

            assertNotNull(result);
            verify(paymentMethodRepository).save(existingCOD);
        }

        @Test
        void updatePaymentMethod_BankTransfer_Success() {
            PaymentMethodRegisterDTO dto = new PaymentMethodRegisterDTO();
            dto.setName("Updated Bank Transfer");
            dto.setDescription("Updated Description");
            dto.setProcessingFee(BigDecimal.valueOf(3000));
            dto.setPaymentMethod("BANK_TRANSFER");
            dto.setAccountName("Updated Account");
            dto.setAccountNumber("0987654321");
            dto.setBankName("Updated Bank");

            BankTransfer existingBT = new BankTransfer();
            existingBT.setId(PAYMENT_METHOD_ID);
            existingBT.setName("Test Bank Transfer");
            existingBT.setDescription("Test Description");
            existingBT.setProcessingFee(BigDecimal.valueOf(2500));
            existingBT.setDeletedAt(null);

            when(paymentMethodRepository.findById(PAYMENT_METHOD_ID)).thenReturn(Optional.of(existingBT));
            when(paymentMethodRepository.save(any(BankTransfer.class))).thenReturn(existingBT);

            PaymentMethodDTO result = paymentMethodService.updatePaymentMethod(PAYMENT_METHOD_ID, dto);

            assertNotNull(result);
            verify(paymentMethodRepository).save(existingBT);
        }

        @Test
        void updatePaymentMethod_EWallet_Success() {
            PaymentMethodRegisterDTO dto = new PaymentMethodRegisterDTO();
            dto.setName("Updated E-Wallet");
            dto.setDescription("Updated Description");
            dto.setProcessingFee(BigDecimal.valueOf(1500));
            dto.setPaymentMethod("E_WALLET");
            dto.setAccountName("Updated E-Wallet Account");
            dto.setVirtualAccountNumber("1234567890");
            dto.setInstructions("Updated E-Wallet Instructions");

            EWallet existingEW = new EWallet();
            existingEW.setId(PAYMENT_METHOD_ID);
            existingEW.setName("Test E-Wallet");
            existingEW.setDescription("Test Description");
            existingEW.setProcessingFee(BigDecimal.valueOf(1000));
            existingEW.setDeletedAt(null);

            when(paymentMethodRepository.findById(PAYMENT_METHOD_ID)).thenReturn(Optional.of(existingEW));
            when(paymentMethodRepository.save(any(EWallet.class))).thenReturn(existingEW);

            PaymentMethodDTO result = paymentMethodService.updatePaymentMethod(PAYMENT_METHOD_ID, dto);

            assertNotNull(result);
            verify(paymentMethodRepository).save(existingEW);
        }

        @Test
        void updatePaymentMethod_NotFound_ShouldThrowEntityNotFoundException() {
            PaymentMethodRegisterDTO dto = new PaymentMethodRegisterDTO();
            dto.setName("Test COD");
            dto.setDescription("Test Description");
            dto.setProcessingFee(BigDecimal.valueOf(5000));
            dto.setPaymentMethod("COD");

            when(paymentMethodRepository.findById(PAYMENT_METHOD_ID)).thenReturn(Optional.empty());

            EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                    () -> paymentMethodService.updatePaymentMethod(PAYMENT_METHOD_ID, dto));
            assertTrue(exception.getMessage().contains("Payment method not found"));
        }

        @Test
        void updatePaymentMethod_DeletedPaymentMethod_ShouldThrowEntityNotFoundException() {
            PaymentMethodRegisterDTO dto = new PaymentMethodRegisterDTO();
            dto.setName("Test COD");
            dto.setDescription("Test Description");
            dto.setProcessingFee(BigDecimal.valueOf(5000));
            dto.setPaymentMethod("COD");

            COD deletedCOD = new COD();
            deletedCOD.setId(PAYMENT_METHOD_ID);
            deletedCOD.setDeletedAt(Timestamp.valueOf(LocalDateTime.now()));

            when(paymentMethodRepository.findById(PAYMENT_METHOD_ID)).thenReturn(Optional.of(deletedCOD));

            EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                    () -> paymentMethodService.updatePaymentMethod(PAYMENT_METHOD_ID, dto));
            assertTrue(exception.getMessage().contains("Payment method not found"));
        }

        @Test
        void updatePaymentMethod_TypeMismatch_COD_ShouldThrowIllegalStateException() {
            PaymentMethodRegisterDTO dto = new PaymentMethodRegisterDTO();
            dto.setName("Test COD");
            dto.setDescription("Test Description");
            dto.setProcessingFee(BigDecimal.valueOf(5000));
            dto.setPaymentMethod("COD");
            dto.setPhoneNumber("081234567890");
            dto.setInstructions("COD Instructions");

            // Create a BankTransfer entity but try to update it as COD (type mismatch)
            BankTransfer existingBT = new BankTransfer();
            existingBT.setId(PAYMENT_METHOD_ID);
            existingBT.setName("Test Bank Transfer");
            existingBT.setDescription("Test Description");
            existingBT.setProcessingFee(BigDecimal.valueOf(2500));
            existingBT.setDeletedAt(null);

            when(paymentMethodRepository.findById(PAYMENT_METHOD_ID)).thenReturn(Optional.of(existingBT));

            IllegalStateException exception = assertThrows(IllegalStateException.class,
                    () -> paymentMethodService.updatePaymentMethod(PAYMENT_METHOD_ID, dto));
            assertTrue(exception.getMessage().contains("Payment method type mismatch") ||
                    exception.getMessage().contains("Cannot update") ||
                    exception.getMessage().contains("type"));
        }

        @Test
        void updatePaymentMethod_TypeMismatch_BankTransfer_ShouldThrowIllegalStateException() {
            PaymentMethodRegisterDTO dto = new PaymentMethodRegisterDTO();
            dto.setName("Test Bank Transfer");
            dto.setDescription("Test Description");
            dto.setProcessingFee(BigDecimal.valueOf(2500));
            dto.setPaymentMethod("BANK_TRANSFER");
            dto.setAccountName("Test Account");
            dto.setAccountNumber("1234567890");
            dto.setBankName("Test Bank");

            // Create a COD entity but try to update it as BankTransfer (type mismatch)
            COD existingCOD = new COD();
            existingCOD.setId(PAYMENT_METHOD_ID);
            existingCOD.setName("Test COD");
            existingCOD.setDescription("Test Description");
            existingCOD.setProcessingFee(BigDecimal.valueOf(5000));
            existingCOD.setDeletedAt(null);

            when(paymentMethodRepository.findById(PAYMENT_METHOD_ID)).thenReturn(Optional.of(existingCOD));

            IllegalStateException exception = assertThrows(IllegalStateException.class,
                    () -> paymentMethodService.updatePaymentMethod(PAYMENT_METHOD_ID, dto));
            assertTrue(exception.getMessage().contains("Payment method type mismatch") ||
                    exception.getMessage().contains("Cannot update") ||
                    exception.getMessage().contains("type"));
        }

        @Test
        void updatePaymentMethod_TypeMismatch_EWallet_ShouldThrowIllegalStateException() {
            PaymentMethodRegisterDTO dto = new PaymentMethodRegisterDTO();
            dto.setName("Test E-Wallet");
            dto.setDescription("Test Description");
            dto.setProcessingFee(BigDecimal.valueOf(1000));
            dto.setPaymentMethod("E_WALLET");
            dto.setAccountName("Test E-Wallet Account");
            dto.setVirtualAccountNumber("9876543210");
            dto.setInstructions("E-Wallet Instructions");

            // Create a COD entity but try to update it as EWallet (type mismatch)
            COD existingCOD = new COD();
            existingCOD.setId(PAYMENT_METHOD_ID);
            existingCOD.setName("Test COD");
            existingCOD.setDescription("Test Description");
            existingCOD.setProcessingFee(BigDecimal.valueOf(5000));
            existingCOD.setDeletedAt(null);

            when(paymentMethodRepository.findById(PAYMENT_METHOD_ID)).thenReturn(Optional.of(existingCOD));

            IllegalStateException exception = assertThrows(IllegalStateException.class,
                    () -> paymentMethodService.updatePaymentMethod(PAYMENT_METHOD_ID, dto));
            assertTrue(exception.getMessage().contains("Payment method type mismatch") ||
                    exception.getMessage().contains("Cannot update") ||
                    exception.getMessage().contains("type"));
        }


    }

    @Test
    void testGetCurrentUserId_Success() {
        UUID result = paymentMethodService.getCurrentUserId();
        assertEquals(USER_ID, result);
    }

    @Test
    void testGetCurrentUserId_NoAuthentication() {
        when(securityContext.getAuthentication()).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> paymentMethodService.getCurrentUserId());
        assertEquals("User not authenticated", exception.getMessage());
    }

    @Test
    void testGetCurrentUserId_NotAuthenticated() {
        when(authentication.isAuthenticated()).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> paymentMethodService.getCurrentUserId());
        assertEquals("User not authenticated", exception.getMessage());
    }

    @Test
    void testGetCurrentUserId_InvalidPrincipal() {
        when(authentication.getPrincipal()).thenReturn(123);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> paymentMethodService.getCurrentUserId());
        assertEquals("Cannot extract user id from principal", exception.getMessage());
    }

    @Test
    void testCreatePaymentMethod_COD_Success() {
        PaymentMethodRegisterDTO dto = new PaymentMethodRegisterDTO();
        dto.setName("COD Payment");
        dto.setDescription("Cash on delivery");
        dto.setProcessingFee(BigDecimal.valueOf(5000));
        dto.setPaymentMethod("COD");
        dto.setPhoneNumber("081234567890");
        dto.setInstructions("Call before delivery");

        COD savedCod = new COD();
        savedCod.setId(PAYMENT_METHOD_ID);
        savedCod.setName(dto.getName());
        savedCod.setDescription(dto.getDescription());
        savedCod.setProcessingFee(dto.getProcessingFee());
        savedCod.setCreatedBy(USER_ID);
        savedCod.setPhoneNumber(dto.getPhoneNumber());
        savedCod.setInstructions(dto.getInstructions());

        when(paymentMethodRepository.save(any(COD.class))).thenReturn(savedCod);

        PaymentMethodDTO result = paymentMethodService.createPaymentMethod(dto);

        assertNotNull(result);
        assertTrue(result instanceof CodDTO);
        assertEquals(dto.getName(), result.getName());
        verify(paymentMethodRepository).save(any(COD.class));
    }

    @Test
    void testCreatePaymentMethod_BankTransfer_Success() {
        PaymentMethodRegisterDTO dto = new PaymentMethodRegisterDTO();
        dto.setName("Bank Transfer");
        dto.setDescription("Transfer via bank");
        dto.setProcessingFee(BigDecimal.valueOf(2500));
        dto.setPaymentMethod("BANK_TRANSFER");
        dto.setAccountName("John Doe");
        dto.setAccountNumber("1234567890");
        dto.setBankName("Bank ABC");

        BankTransfer savedBt = new BankTransfer();
        savedBt.setId(PAYMENT_METHOD_ID);
        savedBt.setName(dto.getName());
        savedBt.setDescription(dto.getDescription());
        savedBt.setProcessingFee(dto.getProcessingFee());
        savedBt.setCreatedBy(USER_ID);
        savedBt.setAccountName(dto.getAccountName());
        savedBt.setAccountNumber(dto.getAccountNumber());
        savedBt.setBankName(dto.getBankName());

        when(paymentMethodRepository.save(any(BankTransfer.class))).thenReturn(savedBt);

        PaymentMethodDTO result = paymentMethodService.createPaymentMethod(dto);

        assertNotNull(result);
        assertTrue(result instanceof BankTransferDTO);
        assertEquals(dto.getName(), result.getName());
        verify(paymentMethodRepository).save(any(BankTransfer.class));
    }

    @Test
    void testCreatePaymentMethod_EWallet_Success() {
        PaymentMethodRegisterDTO dto = new PaymentMethodRegisterDTO();
        dto.setName("E-Wallet");
        dto.setDescription("Digital wallet payment");
        dto.setProcessingFee(BigDecimal.valueOf(1000));
        dto.setPaymentMethod("E_WALLET");
        dto.setAccountName("Jane Doe");
        dto.setVirtualAccountNumber("9876543210");
        dto.setInstructions("Use mobile app");

        EWallet savedEw = new EWallet();
        savedEw.setId(PAYMENT_METHOD_ID);
        savedEw.setName(dto.getName());
        savedEw.setDescription(dto.getDescription());
        savedEw.setProcessingFee(dto.getProcessingFee());
        savedEw.setCreatedBy(USER_ID);
        savedEw.setAccountName(dto.getAccountName());
        savedEw.setVirtualAccountNumber(dto.getVirtualAccountNumber());
        savedEw.setInstructions(dto.getInstructions());

        when(paymentMethodRepository.save(any(EWallet.class))).thenReturn(savedEw);

        PaymentMethodDTO result = paymentMethodService.createPaymentMethod(dto);

        assertNotNull(result);
        assertTrue(result instanceof EWalletDTO);
        assertEquals(dto.getName(), result.getName());
        verify(paymentMethodRepository).save(any(EWallet.class));
    }

    @Test
    void testCreatePaymentMethod_Exception() {
        PaymentMethodRegisterDTO dto = new PaymentMethodRegisterDTO();
        dto.setPaymentMethod("COD");

        when(paymentMethodRepository.save(any())).thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> paymentMethodService.createPaymentMethod(dto));
        assertTrue(exception.getMessage().contains("Failed to create payment method"));
    }

    @Test
    void testActivatePaymentMethod_Success() {
        COD paymentMethod = new COD();
        paymentMethod.setId(PAYMENT_METHOD_ID);
        paymentMethod.setName("COD");
        paymentMethod.setDeletedAt(Timestamp.valueOf(LocalDateTime.now())); // Soft deleted

        when(paymentMethodRepository.findById(PAYMENT_METHOD_ID)).thenReturn(Optional.of(paymentMethod));
        when(paymentMethodRepository.save(any())).thenReturn(paymentMethod);

        PaymentMethodDTO result = paymentMethodService.activatePaymentMethod(PAYMENT_METHOD_ID.toString());

        assertNotNull(result);
        verify(paymentMethodRepository).save(paymentMethod);
        assertNull(paymentMethod.getDeletedAt());
    }

    @Test
    void testActivatePaymentMethod_NotFound() {
        when(paymentMethodRepository.findById(any())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> paymentMethodService.activatePaymentMethod(PAYMENT_METHOD_ID.toString()));
        assertTrue(exception.getMessage().contains("Payment method not found"));
    }

    @Test
    void testActivatePaymentMethod_AlreadyActive() {
        COD paymentMethod = new COD();
        paymentMethod.setId(PAYMENT_METHOD_ID);
        paymentMethod.setDeletedAt(null);

        when(paymentMethodRepository.findById(PAYMENT_METHOD_ID)).thenReturn(Optional.of(paymentMethod));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> paymentMethodService.activatePaymentMethod(PAYMENT_METHOD_ID.toString()));
        assertTrue(exception.getMessage().contains("already active"));
    }

    @Test
    void testUpdatePaymentMethod_COD_TypeMismatch() {
        PaymentMethodRegisterDTO dto = new PaymentMethodRegisterDTO();
        dto.setPaymentMethod("COD");

        BankTransfer existingMethod = new BankTransfer(); // Different type

        when(paymentMethodRepository.findById(PAYMENT_METHOD_ID)).thenReturn(Optional.of(existingMethod));

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> paymentMethodService.updatePaymentMethod(PAYMENT_METHOD_ID, dto));
        assertTrue(exception.getMessage().contains("Mismatched type: expected COD"));
    }

    @Test
    void testUpdatePaymentMethod_BankTransfer_TypeMismatch() {
        PaymentMethodRegisterDTO dto = new PaymentMethodRegisterDTO();
        dto.setPaymentMethod("BANK_TRANSFER");

        COD existingMethod = new COD(); // Different type

        when(paymentMethodRepository.findById(PAYMENT_METHOD_ID)).thenReturn(Optional.of(existingMethod));

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> paymentMethodService.updatePaymentMethod(PAYMENT_METHOD_ID, dto));
        assertTrue(exception.getMessage().contains("Mismatched type: expected BankTransfer"));
    }

    @Test
    void testUpdatePaymentMethod_EWallet_TypeMismatch() {
        PaymentMethodRegisterDTO dto = new PaymentMethodRegisterDTO();
        dto.setPaymentMethod("E_WALLET");

        COD existingMethod = new COD(); // Different type

        when(paymentMethodRepository.findById(PAYMENT_METHOD_ID)).thenReturn(Optional.of(existingMethod));

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> paymentMethodService.updatePaymentMethod(PAYMENT_METHOD_ID, dto));
        assertTrue(exception.getMessage().contains("Mismatched type: expected EWallet"));
    }

    @Test
    void testUpdatePaymentMethod_NotFound() {
        PaymentMethodRegisterDTO dto = new PaymentMethodRegisterDTO();

        when(paymentMethodRepository.findById(PAYMENT_METHOD_ID)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> paymentMethodService.updatePaymentMethod(PAYMENT_METHOD_ID, dto));
        assertTrue(exception.getMessage().contains("Payment method not found"));
    }

    @Test
    void testUpdatePaymentMethod_Deleted() {
        PaymentMethodRegisterDTO dto = new PaymentMethodRegisterDTO();

        COD existingMethod = new COD();
        existingMethod.setDeletedAt(Timestamp.valueOf(LocalDateTime.now()));

        when(paymentMethodRepository.findById(PAYMENT_METHOD_ID)).thenReturn(Optional.of(existingMethod));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> paymentMethodService.updatePaymentMethod(PAYMENT_METHOD_ID, dto));
        assertTrue(exception.getMessage().contains("Payment method not found"));
    }

    @Test
    void testFindAllActivePaymentMethods() {
        List<PaymentMethod> methods = Arrays.asList(new COD(), new BankTransfer());
        when(paymentMethodRepository.findByDeletedAtNull()).thenReturn(methods);

        List<PaymentMethodUserDTO> result = paymentMethodService.findAllActivePaymentMethods();

        assertEquals(2, result.size());
        verify(paymentMethodRepository).findByDeletedAtNull();
    }

    @Test
    void testFindAllPaymentMethods() {
        List<PaymentMethod> methods = Arrays.asList(new COD(), new BankTransfer());
        when(paymentMethodRepository.findAll()).thenReturn(methods);

        List<PaymentMethodDTO> result = paymentMethodService.findAllPaymentMethods();

        assertEquals(2, result.size());
        verify(paymentMethodRepository).findAll();
    }

    @Test
    void testFindPaymentMethodById_Success() {
        COD method = new COD();
        method.setId(PAYMENT_METHOD_ID);

        when(paymentMethodRepository.findById(PAYMENT_METHOD_ID)).thenReturn(Optional.of(method));

        PaymentMethodDTO result = paymentMethodService.findPaymentMethodById(PAYMENT_METHOD_ID.toString());

        assertNotNull(result);
        verify(paymentMethodRepository).findById(PAYMENT_METHOD_ID);
    }

    @Test
    void testFindPaymentMethodById_NotFound() {
        when(paymentMethodRepository.findById(any())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> paymentMethodService.findPaymentMethodById(PAYMENT_METHOD_ID.toString()));
        assertTrue(exception.getMessage().contains("Payment method not found"));
    }

    @Test
    void testDeletePaymentMethod_Success() {
        COD method = new COD();
        method.setId(PAYMENT_METHOD_ID);
        method.setDeletedAt(null);

        when(paymentMethodRepository.findById(PAYMENT_METHOD_ID)).thenReturn(Optional.of(method));
        when(paymentMethodRepository.save(any())).thenReturn(method);

        Map<String, Object> result = paymentMethodService.deletePaymentMethod(PAYMENT_METHOD_ID.toString());

        assertNotNull(result);
        assertEquals(PAYMENT_METHOD_ID, result.get("id"));
        assertNotNull(result.get("deleted_at"));
        verify(paymentMethodRepository).save(method);
    }

    @Test
    void testDeletePaymentMethod_NotFound() {
        when(paymentMethodRepository.findById(any())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> paymentMethodService.deletePaymentMethod(PAYMENT_METHOD_ID.toString()));
        assertTrue(exception.getMessage().contains("Payment method not found"));
    }

    @Test
    void testDeletePaymentMethod_AlreadyDeleted() {
        COD method = new COD();
        method.setDeletedAt(Timestamp.valueOf(LocalDateTime.now()));

        when(paymentMethodRepository.findById(PAYMENT_METHOD_ID)).thenReturn(Optional.of(method));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> paymentMethodService.deletePaymentMethod(PAYMENT_METHOD_ID.toString()));
        assertTrue(exception.getMessage().contains("already deleted"));
    }

    @Test
    void testFindActivePaymentMethodsByType_COD() {
        List<COD> codMethods = Arrays.asList(new COD());
        when(codRepository.findAllByDeletedAtIsNull()).thenReturn(codMethods);

        List<PaymentMethodUserDTO> result = paymentMethodService.findActivePaymentMethodsByType("COD");

        assertEquals(1, result.size());
        verify(codRepository).findAllByDeletedAtIsNull();
    }

    @Test
    void testFindActivePaymentMethodsByType_BankTransfer() {
        List<BankTransfer> btMethods = Arrays.asList(new BankTransfer());
        when(bankTransferRepository.findAllByDeletedAtIsNull()).thenReturn(btMethods);

        List<PaymentMethodUserDTO> result = paymentMethodService.findActivePaymentMethodsByType("BANK_TRANSFER");

        assertEquals(1, result.size());
        verify(bankTransferRepository).findAllByDeletedAtIsNull();
    }

    @Test
    void testFindActivePaymentMethodsByType_EWallet() {
        List<EWallet> ewMethods = Arrays.asList(new EWallet());
        when(eWalletRepository.findAllByDeletedAtIsNull()).thenReturn(ewMethods);

        List<PaymentMethodUserDTO> result = paymentMethodService.findActivePaymentMethodsByType("E_WALLET");

        assertEquals(1, result.size());
        verify(eWalletRepository).findAllByDeletedAtIsNull();
    }

    @Test
    void testFindActivePaymentMethodsByType_Invalid() {
        List<PaymentMethodUserDTO> result = paymentMethodService.findActivePaymentMethodsByType("INVALID");

        assertNull(result);
    }

    @Test
    void testFindByTypeForAdmin_COD() {
        List<COD> codMethods = Arrays.asList(new COD());
        when(codRepository.findAll()).thenReturn(codMethods);

        List<PaymentMethodDTO> result = paymentMethodService.findByTypeForAdmin("COD");

        assertEquals(1, result.size());
        verify(codRepository).findAll();
    }

    @Test
    void testFindByTypeForAdmin_BankTransfer() {
        List<BankTransfer> btMethods = Arrays.asList(new BankTransfer());
        when(bankTransferRepository.findAll()).thenReturn(btMethods);

        List<PaymentMethodDTO> result = paymentMethodService.findByTypeForAdmin("BANK_TRANSFER");

        assertEquals(1, result.size());
        verify(bankTransferRepository).findAll();
    }

    @Test
    void testFindByTypeForAdmin_EWallet() {
        List<EWallet> ewMethods = Arrays.asList(new EWallet());
        when(eWalletRepository.findAll()).thenReturn(ewMethods);

        List<PaymentMethodDTO> result = paymentMethodService.findByTypeForAdmin("E_WALLET");

        assertEquals(1, result.size());
        verify(eWalletRepository).findAll();
    }

    @Test
    void testFindByTypeForAdmin_Invalid() {
        List<PaymentMethodDTO> result = paymentMethodService.findByTypeForAdmin("INVALID");

        assertNull(result);
    }

    @Test
    void testFindActivePaymentMethodById_Success() {
        COD method = new COD();
        method.setId(PAYMENT_METHOD_ID);
        method.setDeletedAt(null);

        when(paymentMethodRepository.findById(PAYMENT_METHOD_ID)).thenReturn(Optional.of(method));

        PaymentMethodUserDTO result = paymentMethodService.findActivePaymentMethodById(PAYMENT_METHOD_ID.toString());

        assertNotNull(result);
        verify(paymentMethodRepository).findById(PAYMENT_METHOD_ID);
    }

    @Test
    void testFindActivePaymentMethodById_NotFound() {
        when(paymentMethodRepository.findById(any())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> paymentMethodService.findActivePaymentMethodById(PAYMENT_METHOD_ID.toString()));
        assertTrue(exception.getMessage().contains("Payment method not found"));
    }

    @Test
    void testFindActivePaymentMethodById_Deleted() {
        COD method = new COD();
        method.setDeletedAt(Timestamp.valueOf(LocalDateTime.now()));

        when(paymentMethodRepository.findById(PAYMENT_METHOD_ID)).thenReturn(Optional.of(method));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> paymentMethodService.findActivePaymentMethodById(PAYMENT_METHOD_ID.toString()));
        assertTrue(exception.getMessage().contains("Active payment method not found"));
    }

    @Test
    void testConvertToDTO_UnknownType() {
        PaymentMethod unknownMethod = new PaymentMethod() {
        };

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> paymentMethodService.convertToDTO(unknownMethod));
        assertEquals("Payment method not found", exception.getMessage());
    }

    @Test
    void testConvertToUserDTO_UnknownType() {
        PaymentMethod unknownMethod = new PaymentMethod() {
        };

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> paymentMethodService.convertToUserDTO(unknownMethod));
        assertEquals("Payment method not found", exception.getMessage());
    }



    @Test
    @DisplayName("Test determinePaymentMethodTypeString - COD")
    void testDeterminePaymentMethodTypeString_COD() {
        COD cod = mock(COD.class);
        String result = paymentMethodService.determinePaymentMethodTypeString(cod);
        assertThat(result).isEqualTo("COD");
    }


    @Test
    @DisplayName("Test determinePaymentMethodTypeString - Unknown Type")
    void testDeterminePaymentMethodTypeString_UnknownType() {
        PaymentMethod unknownPayment = mock(PaymentMethod.class);
        assertThatThrownBy(() ->
                paymentMethodService.determinePaymentMethodTypeString(unknownPayment)
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unknown PaymentMethod subclass");
    }


    @Test
    void getCurrentUserId_NotAuthenticated() {
        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = mockStatic(SecurityContextHolder.class)) {
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(null);

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> paymentMethodService.getCurrentUserId());
            assertEquals("User not authenticated", exception.getMessage());
        }
    }

    @Test
    void getCurrentUserId_NotAuthenticatedFalse() {
        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = mockStatic(SecurityContextHolder.class)) {
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.isAuthenticated()).thenReturn(false);

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> paymentMethodService.getCurrentUserId());
            assertEquals("User not authenticated", exception.getMessage());
        }
    }

    @Test
    void getCurrentUserId_InvalidPrincipal() {
        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = mockStatic(SecurityContextHolder.class)) {
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.isAuthenticated()).thenReturn(true);
            when(authentication.getPrincipal()).thenReturn(123); // Not a String

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> paymentMethodService.getCurrentUserId());
            assertEquals("Cannot extract user id from principal", exception.getMessage());
        }
    }




    @Nested
    class GetAllPaymentMethodsWithOrderCountsSynchronousWrapperTest {

        @Test
        void getAllPaymentMethodsWithOrderCounts_Success() {
            CompletableFuture<List<PaymentMethodDetailsDTO>> successfulFuture = CompletableFuture.completedFuture(sampleDTOList);

            doReturn(successfulFuture).when(paymentMethodServiceSpy).getAllPaymentMethodsWithOrderCountsAsync(mockRequest);

            List<PaymentMethodDetailsDTO> result = paymentMethodServiceSpy.getAllPaymentMethodsWithOrderCounts(mockRequest);

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("Test Method", result.get(0).getName());
            assertEquals(10, result.get(0).getOrderCount());
            verify(paymentMethodServiceSpy).getAllPaymentMethodsWithOrderCountsAsync(mockRequest);
        }

        @Test
        void getAllPaymentMethodsWithOrderCounts_Timeout() {
            CompletableFuture<List<PaymentMethodDetailsDTO>> unendingFuture = new CompletableFuture<>();

            doReturn(unendingFuture).when(paymentMethodServiceSpy).getAllPaymentMethodsWithOrderCountsAsync(mockRequest);

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                paymentMethodServiceSpy.getAllPaymentMethodsWithOrderCounts(mockRequest);
            });

            assertTrue(exception.getMessage().contains("Service timeout - external service too slow"));
            verify(paymentMethodServiceSpy).getAllPaymentMethodsWithOrderCountsAsync(mockRequest);
        }

        @Test
        void getAllPaymentMethodsWithOrderCounts_ExecutionException() {
            CompletableFuture<List<PaymentMethodDetailsDTO>> failedFuture = new CompletableFuture<>();
            failedFuture.completeExceptionally(new ExecutionException("Async task failed", new RuntimeException("Original service error")));

            doReturn(failedFuture).when(paymentMethodServiceSpy).getAllPaymentMethodsWithOrderCountsAsync(mockRequest);

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                paymentMethodServiceSpy.getAllPaymentMethodsWithOrderCounts(mockRequest);
            });

            assertTrue(exception.getMessage().contains("Service error"));
            verify(paymentMethodServiceSpy).getAllPaymentMethodsWithOrderCountsAsync(mockRequest);
        }

        @Test
        void getAllPaymentMethodsWithOrderCounts_InterruptedException() {
            CompletableFuture<List<PaymentMethodDetailsDTO>> interruptedFuture = new CompletableFuture<>();
            interruptedFuture.completeExceptionally(new InterruptedException("Thread was interrupted"));

            doReturn(interruptedFuture).when(paymentMethodServiceSpy).getAllPaymentMethodsWithOrderCountsAsync(mockRequest);

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                paymentMethodServiceSpy.getAllPaymentMethodsWithOrderCounts(mockRequest);
            });

            assertTrue(exception.getMessage().contains("Service error"));
            verify(paymentMethodServiceSpy).getAllPaymentMethodsWithOrderCountsAsync(mockRequest);
        }
    }



}
