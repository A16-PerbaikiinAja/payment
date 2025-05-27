package id.ac.ui.cs.advprog.payment.service;

import id.ac.ui.cs.advprog.payment.dto.paymentmethod.*;
import id.ac.ui.cs.advprog.payment.enums.PaymentMethodType;
import id.ac.ui.cs.advprog.payment.external.OrderData;
import id.ac.ui.cs.advprog.payment.external.OrderServiceClient;
import id.ac.ui.cs.advprog.payment.model.*;
import id.ac.ui.cs.advprog.payment.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
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
    private HttpServletRequest request;

    private List<PaymentMethod> mockPaymentMethods;
    private List<OrderData> mockOrderData;
    private UUID paymentMethodId1;
    private UUID paymentMethodId2;
    private UUID paymentMethodId3;

    private final UUID USER_ID = UUID.randomUUID();
    private final UUID PAYMENT_METHOD_ID = UUID.randomUUID();

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
        request = mock(HttpServletRequest.class);

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

        paymentMethodId1 = UUID.randomUUID();
        paymentMethodId2 = UUID.randomUUID();
        paymentMethodId3 = UUID.randomUUID();

        // Setup mock payment methods
        COD codMethod = new COD();
        codMethod.setId(paymentMethodId1);
        codMethod.setName("Cash on Delivery");
        codMethod.setInstructions("Pay when the order arrives");

        BankTransfer bankTransferMethod = new BankTransfer();
        bankTransferMethod.setId(paymentMethodId2);
        bankTransferMethod.setName("Bank Transfer");

        EWallet eWalletMethod = new EWallet();
        eWalletMethod.setId(paymentMethodId3);
        eWalletMethod.setName("E-Wallet Payment");
        eWalletMethod.setInstructions("Use mobile app to pay");

    }

    private void setupMockData() {
        // Setup mock payment methods
        COD codMethod = new COD();
        codMethod.setId(paymentMethodId1);
        codMethod.setName("Cash on Delivery");
        codMethod.setInstructions("Pay when the order arrives");

        BankTransfer bankTransferMethod = new BankTransfer();
        bankTransferMethod.setId(paymentMethodId2);
        bankTransferMethod.setName("Bank Transfer");

        EWallet eWalletMethod = new EWallet();
        eWalletMethod.setId(paymentMethodId3);
        eWalletMethod.setName("E-Wallet Payment");
        eWalletMethod.setInstructions("Use mobile app to pay");

        mockPaymentMethods = Arrays.asList(codMethod, bankTransferMethod, eWalletMethod);

        // Setup mock order data
        OrderData order1 = new OrderData();
        order1.setPaymentMethodId(paymentMethodId1);

        OrderData order2 = new OrderData();
        order2.setPaymentMethodId(paymentMethodId1);

        OrderData order3 = new OrderData();
        order3.setPaymentMethodId(paymentMethodId2);

        OrderData order4 = new OrderData();
        order4.setPaymentMethodId(null); // Order without payment method

        mockOrderData = Arrays.asList(order1, order2, order3, order4);
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
            mockPaymentMethod.setDeletedAt(null); // Already active

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


//    @Test
//    @DisplayName("getAllPaymentMethodsWithOrderCounts - Success with all payment method types and order counts")
//    void testGetAllPaymentMethodsWithOrderCounts_Success() {
//        // Arrange
//        setupMockData();
//
//        when(paymentMethodRepository.findAll()).thenReturn(mockPaymentMethods);
//        when(orderServiceClient.getAllOrders(request)).thenReturn(mockOrderData);
//
//        // Act
//        List<PaymentMethodDetailsDTO> result = paymentMethodService.getAllPaymentMethodsWithOrderCounts(request);
//
//        // Assert
//        assertThat(result).hasSize(3);
//
//        // Verify COD payment method
//        PaymentMethodDetailsDTO codDto = result.stream()
//                .filter(dto -> dto.getId().equals(paymentMethodId1.toString()))
//                .findFirst()
//                .orElseThrow();
//
//        assertThat(codDto.getName()).isEqualTo("Cash on Delivery");
//        assertThat(codDto.getMethodType()).isEqualTo("COD");
//        assertThat(codDto.getInstructions()).isEqualTo("Pay when the order arrives");
//        assertThat(codDto.getOrderCount()).isEqualTo(2); // 2 orders use COD
//
//        // Verify Bank Transfer payment method
//        PaymentMethodDetailsDTO bankTransferDto = result.stream()
//                .filter(dto -> dto.getId().equals(paymentMethodId2.toString()))
//                .findFirst()
//                .orElseThrow();
//
//        assertThat(bankTransferDto.getName()).isEqualTo("Bank Transfer");
//        assertThat(bankTransferDto.getMethodType()).isEqualTo("BANK_TRANSFER");
//        assertThat(bankTransferDto.getInstructions()).isNull(); // Bank transfer has no instructions
//        assertThat(bankTransferDto.getOrderCount()).isEqualTo(1); // 1 order uses Bank Transfer
//
//        // Verify E-Wallet payment method
//        PaymentMethodDetailsDTO eWalletDto = result.stream()
//                .filter(dto -> dto.getId().equals(paymentMethodId3.toString()))
//                .findFirst()
//                .orElseThrow();
//
//        assertThat(eWalletDto.getName()).isEqualTo("E-Wallet Payment");
//        assertThat(eWalletDto.getMethodType()).isEqualTo("E_WALLET");
//        assertThat(eWalletDto.getInstructions()).isEqualTo("Use mobile app to pay");
//        assertThat(eWalletDto.getOrderCount()).isEqualTo(0); // No orders use E-Wallet
//
//        // Verify repository and service calls
//        verify(paymentMethodRepository).findAll();
//        verify(orderServiceClient).getAllOrders(request);
//    }

//    @Test
//    @DisplayName("getAllPaymentMethodsWithOrderCounts - OrderService throws exception")
//    void testGetAllPaymentMethodsWithOrderCounts_OrderServiceException() {
//        // Arrange
//        setupMockData();
//
//        when(paymentMethodRepository.findAll()).thenReturn(mockPaymentMethods);
//        when(orderServiceClient.getAllOrders(request)).thenThrow(new RuntimeException("Order service unavailable"));
//
//        // Act
//        List<PaymentMethodDetailsDTO> result = paymentMethodService.getAllPaymentMethodsWithOrderCounts(request);
//
//        // Assert - Should return payment methods with 0 order counts due to exception
//        assertThat(result).hasSize(3);
//
//        result.forEach(dto -> {
//            assertThat(dto.getOrderCount()).isEqualTo(0);
//            assertThat(dto.getId()).isNotNull();
//            assertThat(dto.getName()).isNotNull();
//            assertThat(dto.getMethodType()).isNotNull();
//        });
//
//        verify(paymentMethodRepository).findAll();
//        verify(orderServiceClient).getAllOrders(request);
//    }
//
//    @Test
//    @DisplayName("getAllPaymentMethodsWithOrderCounts - OrderService returns empty list")
//    void testGetAllPaymentMethodsWithOrderCounts_OrderServiceReturnsEmptyList() {
//        // Arrange
//        setupMockData();
//
//        when(paymentMethodRepository.findAll()).thenReturn(mockPaymentMethods);
//        when(orderServiceClient.getAllOrders(request)).thenReturn(Collections.emptyList());
//
//        // Act
//        List<PaymentMethodDetailsDTO> result = paymentMethodService.getAllPaymentMethodsWithOrderCounts(request);
//
//        // Assert - Should return payment methods with 0 order counts
//        assertThat(result).hasSize(3);
//
//        result.forEach(dto -> {
//            assertThat(dto.getOrderCount()).isEqualTo(0);
//            assertThat(dto.getId()).isNotNull();
//            assertThat(dto.getName()).isNotNull();
//            assertThat(dto.getMethodType()).isNotNull();
//        });
//
//        verify(paymentMethodRepository).findAll();
//        verify(orderServiceClient).getAllOrders(request);
//    }
//
//    @Test
//    @DisplayName("getAllPaymentMethodsWithOrderCounts - Empty payment methods list")
//    void testGetAllPaymentMethodsWithOrderCounts_EmptyPaymentMethods() {
//        // Arrange
//        setupMockData();
//
//        when(paymentMethodRepository.findAll()).thenReturn(Collections.emptyList());
//        when(orderServiceClient.getAllOrders(request)).thenReturn(mockOrderData);
//
//        // Act
//        List<PaymentMethodDetailsDTO> result = paymentMethodService.getAllPaymentMethodsWithOrderCounts(request);
//
//        // Assert
//        assertThat(result).isEmpty();
//
//        verify(paymentMethodRepository).findAll();
//        verify(orderServiceClient).getAllOrders(request);
//    }

//    @Test
//    @DisplayName("getAllPaymentMethodsWithOrderCounts - Orders with null payment method IDs are filtered out")
//    void testGetAllPaymentMethodsWithOrderCounts_NullPaymentMethodIds() {
//        // Arrange
//        setupMockData();
//
//        // Create orders where all have null payment method IDs
//        List<OrderData> ordersWithNullPaymentMethods = Arrays.asList(
//                createOrderWithPaymentMethodId(null),
//                createOrderWithPaymentMethodId(null),
//                createOrderWithPaymentMethodId(null)
//        );
//
//        when(paymentMethodRepository.findAll()).thenReturn(mockPaymentMethods);
//        when(orderServiceClient.getAllOrders(request)).thenReturn(ordersWithNullPaymentMethods);
//
//        // Act
//        List<PaymentMethodDetailsDTO> result = paymentMethodService.getAllPaymentMethodsWithOrderCounts(request);
//
//        // Assert - All payment methods should have 0 order count since all orders have null payment method IDs
//        assertThat(result).hasSize(3);
//
//        result.forEach(dto -> {
//            assertThat(dto.getOrderCount()).isEqualTo(0);
//        });
//
//        verify(paymentMethodRepository).findAll();
//        verify(orderServiceClient).getAllOrders(request);
//    }

    @Test
//    @DisplayName("determinePaymentMethodTypeString - Unknown payment method type throws exception")
//    void testDeterminePaymentMethodTypeString_UnknownType() {
//        // Arrange
//        PaymentMethod unknownPaymentMethod = mock(PaymentMethod.class);
//        when(unknownPaymentMethod.getId()).thenReturn(UUID.randomUUID());
//        when(unknownPaymentMethod.getName()).thenReturn("Unknown Payment");
//
//        List<PaymentMethod> paymentMethodsWithUnknown = Arrays.asList(unknownPaymentMethod);
//
//        when(paymentMethodRepository.findAll()).thenReturn(paymentMethodsWithUnknown);
//        when(orderServiceClient.getAllOrders(request)).thenReturn(Collections.emptyList());
//
//        // Act & Assert
//        assertThatThrownBy(() -> paymentMethodService.getAllPaymentMethodsWithOrderCounts(request))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessageContaining("Unknown PaymentMethod subclass:");
//
//        verify(paymentMethodRepository).findAll();
//        verify(orderServiceClient).getAllOrders(request);
//    }

//    @Test
//    @DisplayName("getAllPaymentMethodsWithOrderCounts - Mixed scenario with various order counts")
//    void testGetAllPaymentMethodsWithOrderCounts_MixedScenario() {
//        // Arrange
//        setupMockData();
//
//        // Create additional orders to test different scenarios
//        List<OrderData> mixedOrders = Arrays.asList(
//                createOrderWithPaymentMethodId(paymentMethodId1), // COD
//                createOrderWithPaymentMethodId(paymentMethodId1), // COD
//                createOrderWithPaymentMethodId(paymentMethodId1), // COD
//                createOrderWithPaymentMethodId(paymentMethodId2), // Bank Transfer
//                createOrderWithPaymentMethodId(paymentMethodId2), // Bank Transfer
//                createOrderWithPaymentMethodId(paymentMethodId3), // E-Wallet
//                createOrderWithPaymentMethodId(null), // Should be filtered out
//                createOrderWithPaymentMethodId(UUID.randomUUID()) // Non-existent payment method ID
//        );
//
//        when(paymentMethodRepository.findAll()).thenReturn(mockPaymentMethods);
//        when(orderServiceClient.getAllOrders(request)).thenReturn(mixedOrders);
//
//        // Act
//        List<PaymentMethodDetailsDTO> result = paymentMethodService.getAllPaymentMethodsWithOrderCounts(request);
//
//        // Assert
//        assertThat(result).hasSize(3);
//
//        // Check COD (should have 3 orders)
//        PaymentMethodDetailsDTO codDto = result.stream()
//                .filter(dto -> dto.getId().equals(paymentMethodId1.toString()))
//                .findFirst()
//                .orElseThrow();
//        assertThat(codDto.getOrderCount()).isEqualTo(3);
//
//        // Check Bank Transfer (should have 2 orders)
//        PaymentMethodDetailsDTO bankTransferDto = result.stream()
//                .filter(dto -> dto.getId().equals(paymentMethodId2.toString()))
//                .findFirst()
//                .orElseThrow();
//        assertThat(bankTransferDto.getOrderCount()).isEqualTo(2);
//
//        // Check E-Wallet (should have 1 order)
//        PaymentMethodDetailsDTO eWalletDto = result.stream()
//                .filter(dto -> dto.getId().equals(paymentMethodId3.toString()))
//                .findFirst()
//                .orElseThrow();
//        assertThat(eWalletDto.getOrderCount()).isEqualTo(1);
//
//        verify(paymentMethodRepository).findAll();
//        verify(orderServiceClient).getAllOrders(request);
//    }
//
//    @Test
//    @DisplayName("getAllPaymentMethodsWithOrderCounts - COD payment method without instructions")
//    void testGetAllPaymentMethodsWithOrderCounts_CODWithoutInstructions() {
//        // Arrange
//        COD codMethodWithoutInstructions = new COD();
//        codMethodWithoutInstructions.setId(paymentMethodId1);
//        codMethodWithoutInstructions.setName("Cash on Delivery");
//        // Not setting instructions (will be null)
//
//        List<PaymentMethod> paymentMethods = Arrays.asList(codMethodWithoutInstructions);
//        List<OrderData> orders = Arrays.asList(createOrderWithPaymentMethodId(paymentMethodId1));
//
//        when(paymentMethodRepository.findAll()).thenReturn(paymentMethods);
//        when(orderServiceClient.getAllOrders(request)).thenReturn(orders);
//
//        // Act
//        List<PaymentMethodDetailsDTO> result = paymentMethodService.getAllPaymentMethodsWithOrderCounts(request);
//
//        // Assert
//        assertThat(result).hasSize(1);
//        PaymentMethodDetailsDTO dto = result.get(0);
//        assertThat(dto.getInstructions()).isNull();
//        assertThat(dto.getOrderCount()).isEqualTo(1);
//
//        verify(paymentMethodRepository).findAll();
//        verify(orderServiceClient).getAllOrders(request);
//    }

//    @Test
//    @DisplayName("getAllPaymentMethodsWithOrderCounts - EWallet payment method without instructions")
//    void testGetAllPaymentMethodsWithOrderCounts_EWalletWithoutInstructions() {
//        // Arrange
//        EWallet eWalletMethodWithoutInstructions = new EWallet();
//        eWalletMethodWithoutInstructions.setId(paymentMethodId3);
//        eWalletMethodWithoutInstructions.setName("E-Wallet Payment");
//        // Not setting instructions (will be null)
//
//        List<PaymentMethod> paymentMethods = Arrays.asList(eWalletMethodWithoutInstructions);
//        List<OrderData> orders = Arrays.asList(createOrderWithPaymentMethodId(paymentMethodId3));
//
//        when(paymentMethodRepository.findAll()).thenReturn(paymentMethods);
//        when(orderServiceClient.getAllOrders(request)).thenReturn(orders);
//
//        // Act
//        List<PaymentMethodDetailsDTO> result = paymentMethodService.getAllPaymentMethodsWithOrderCounts(request);
//
//        // Assert
//        assertThat(result).hasSize(1);
//        PaymentMethodDetailsDTO dto = result.get(0);
//        assertThat(dto.getInstructions()).isNull();
//        assertThat(dto.getOrderCount()).isEqualTo(1);
//
//        verify(paymentMethodRepository).findAll();
//        verify(orderServiceClient).getAllOrders(request);
//    }

    // Helper method to create OrderData with specific payment method ID
    private OrderData createOrderWithPaymentMethodId(UUID paymentMethodId) {
        OrderData order = new OrderData();
        order.setId(UUID.randomUUID());
        order.setPaymentMethodId(paymentMethodId);
        order.setItemName("Test Item");
        return order;
    }

}