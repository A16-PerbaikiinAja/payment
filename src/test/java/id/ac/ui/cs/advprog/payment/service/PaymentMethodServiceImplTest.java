//package id.ac.ui.cs.advprog.payment.service;
//
//import id.ac.ui.cs.advprog.payment.dto.paymentmethod.*;
//import id.ac.ui.cs.advprog.payment.external.OrderServiceClient;
//import id.ac.ui.cs.advprog.payment.model.*;
//import id.ac.ui.cs.advprog.payment.repository.*;
//import jakarta.persistence.EntityNotFoundException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.test.util.ReflectionTestUtils;
//
//import java.math.BigDecimal;
//import java.sql.Timestamp;
//import java.time.LocalDateTime;
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//class PaymentMethodServiceImplTest {
//
//    private PaymentMethodRepository paymentMethodRepository;
//    private CODRepository codRepository;
//    private BankTransferRepository bankTransferRepository;
//    private EWalletRepository eWalletRepository;
//    private PaymentMethodServiceImpl paymentMethodService;
//    private Authentication authentication;
//    private SecurityContext securityContext;
//
//    private final UUID USER_ID = UUID.randomUUID();
//    private final UUID PAYMENT_METHOD_ID = UUID.randomUUID();
//
//    @BeforeEach
//    void setUp() {
//        // Create mocks manually
//        paymentMethodRepository = mock(PaymentMethodRepository.class);
//        codRepository = mock(CODRepository.class);
//        bankTransferRepository = mock(BankTransferRepository.class);
//        eWalletRepository = mock(EWalletRepository.class);
//        orderServiceClient = mock(OrderServiceClient.class); // Inisialisasi mock OrderServiceClient
//        authentication = mock(Authentication.class);
//        securityContext = mock(SecurityContext.class);
//
//        paymentMethodService = new PaymentMethodServiceImpl(
//                paymentMethodRepository,  // Asumsi ini dibutuhkan di constructor
//                orderServiceClient,
//                codRepository,
//                bankTransferRepository,
//                eWalletRepository
//        );
//
//        ReflectionTestUtils.setField(paymentMethodService, "paymentMethodRepository", paymentMethodRepository);
//
//        // Security context setup
//        when(securityContext.getAuthentication()).thenReturn(authentication);
//        SecurityContextHolder.setContext(securityContext);
//        when(authentication.isAuthenticated()).thenReturn(true);
//        when(authentication.getPrincipal()).thenReturn(USER_ID.toString());
//    }
//
//    // Helper method to mock the convertToDTO functionality that's likely in the service
//    private void mockConvertToDTO(PaymentMethod paymentMethod, PaymentMethodDTO returnDto) {
//        // Create a special mock of the service that will handle the convertToDTO method
//        PaymentMethodServiceImpl spyService = spy(paymentMethodService);
//
//        // When convertToDTO is called with this payment method, return our prepared DTO
//        doReturn(returnDto).when(spyService).convertToDTO(paymentMethod);
//
//        // Replace the original service with our spy
//        paymentMethodService = spyService;
//    }
//
//    // Nested classes for each method group:
//
//    @Nested
//    class GetCurrentUserIdTests {
//        @Test
//        void getCurrentUserId_Success() {
//            UUID result = paymentMethodService.getCurrentUserId();
//            assertEquals(USER_ID, result);
//        }
//        @Test
//        void getCurrentUserId_NotAuthenticated() {
//            when(authentication.isAuthenticated()).thenReturn(false);
//            RuntimeException ex = assertThrows(RuntimeException.class, paymentMethodService::getCurrentUserId);
//            assertEquals("User not authenticated", ex.getMessage());
//        }
//        @Test
//        void getCurrentUserId_NullAuthentication() {
//            when(securityContext.getAuthentication()).thenReturn(null);
//            RuntimeException ex = assertThrows(RuntimeException.class, paymentMethodService::getCurrentUserId);
//            assertEquals("User not authenticated", ex.getMessage());
//        }
//        @Test
//        void getCurrentUserId_InvalidPrincipal() {
//            when(authentication.getPrincipal()).thenReturn(12345);
//            RuntimeException ex = assertThrows(RuntimeException.class, paymentMethodService::getCurrentUserId);
//            assertEquals("Cannot extract user id from principal", ex.getMessage());
//        }
//    }
//
//    @Nested
//    class CreatePaymentMethodTests {
//        @Test
//        void createPaymentMethod_COD_Success() {
//            // Create the request DTO
//            PaymentMethodRegisterDTO dto = createCODRegisterDTO();
//
//            // Create a COD entity that should be returned by the repository
//            COD cod = new COD();
//            cod.setId(PAYMENT_METHOD_ID);
//            setupPaymentMethod(cod, null);
//            cod.setPhoneNumber(dto.getPhoneNumber());
//            cod.setInstructions(dto.getInstructions());
//
//            // Create the expected response DTO
//            CodDTO expectedDto = new CodDTO();
//            expectedDto.setId(PAYMENT_METHOD_ID);
//            expectedDto.setName(dto.getName());
//            expectedDto.setDescription(dto.getDescription());
//            expectedDto.setProcessingFee(dto.getProcessingFee());
//            expectedDto.setPhoneNumber(dto.getPhoneNumber());
//            expectedDto.setInstructions(dto.getInstructions());
//
//            // Setup mocks
//            when(paymentMethodRepository.save(any(COD.class))).thenReturn(cod);
//            mockConvertToDTO(cod, expectedDto);
//
//            // Call the service
//            PaymentMethodDTO result = paymentMethodService.createPaymentMethod(dto);
//
//            // Verify
//            assertNotNull(result);
//            assertEquals(PAYMENT_METHOD_ID, result.getId());
//            assertTrue(result instanceof CodDTO);
//            assertEquals(dto.getPhoneNumber(), ((CodDTO) result).getPhoneNumber());
//            verify(paymentMethodRepository).save(any(COD.class));
//        }
//
//        @Test
//        void createPaymentMethod_BankTransfer_Success() {
//            // Create the request DTO
//            PaymentMethodRegisterDTO dto = createBankTransferRegisterDTO();
//
//            // Create a BankTransfer entity that should be returned by the repository
//            BankTransfer bt = new BankTransfer();
//            bt.setId(PAYMENT_METHOD_ID);
//            setupPaymentMethod(bt, null);
//            bt.setAccountName(dto.getAccountName());
//            bt.setAccountNumber(dto.getAccountNumber());
//            bt.setBankName(dto.getBankName());
//
//            // Create the expected response DTO
//            BankTransferDTO expectedDto = new BankTransferDTO();
//            expectedDto.setId(PAYMENT_METHOD_ID);
//            expectedDto.setName(dto.getName());
//            expectedDto.setDescription(dto.getDescription());
//            expectedDto.setProcessingFee(dto.getProcessingFee());
//            expectedDto.setAccountName(dto.getAccountName());
//            expectedDto.setAccountNumber(dto.getAccountNumber());
//            expectedDto.setBankName(dto.getBankName());
//
//            // Setup mocks
//            when(paymentMethodRepository.save(any(BankTransfer.class))).thenReturn(bt);
//            mockConvertToDTO(bt, expectedDto);
//
//            // Call the service
//            PaymentMethodDTO result = paymentMethodService.createPaymentMethod(dto);
//
//            // Verify
//            assertNotNull(result);
//            assertEquals(PAYMENT_METHOD_ID, result.getId());
//            assertTrue(result instanceof BankTransferDTO);
//            assertEquals(dto.getAccountName(), ((BankTransferDTO) result).getAccountName());
//            verify(paymentMethodRepository).save(any(BankTransfer.class));
//        }
//
//        @Test
//        void createPaymentMethod_EWallet_Success() {
//            // Create the request DTO
//            PaymentMethodRegisterDTO dto = createEWalletRegisterDTO();
//
//            // Create an EWallet entity that should be returned by the repository
//            EWallet ew = new EWallet();
//            ew.setId(PAYMENT_METHOD_ID);
//            setupPaymentMethod(ew, null);
//            ew.setAccountName(dto.getAccountName());
//            ew.setVirtualAccountNumber(dto.getVirtualAccountNumber());
//            ew.setInstructions(dto.getInstructions());
//
//            // Create the expected response DTO
//            EWalletDTO expectedDto = new EWalletDTO();
//            expectedDto.setId(PAYMENT_METHOD_ID);
//            expectedDto.setName(dto.getName());
//            expectedDto.setDescription(dto.getDescription());
//            expectedDto.setProcessingFee(dto.getProcessingFee());
//            expectedDto.setAccountName(dto.getAccountName());
//            expectedDto.setVirtualAccountNumber(dto.getVirtualAccountNumber());
//            expectedDto.setInstructions(dto.getInstructions());
//
//            // Setup mocks
//            when(paymentMethodRepository.save(any(EWallet.class))).thenReturn(ew);
//            mockConvertToDTO(ew, expectedDto);
//
//            // Call the service
//            PaymentMethodDTO result = paymentMethodService.createPaymentMethod(dto);
//
//            // Verify
//            assertNotNull(result);
//            assertEquals(PAYMENT_METHOD_ID, result.getId());
//            assertTrue(result instanceof EWalletDTO);
//            assertEquals(dto.getAccountName(), ((EWalletDTO) result).getAccountName());
//            verify(paymentMethodRepository).save(any(EWallet.class));
//        }
//
//        @Test
//        void createPaymentMethod_InvalidType_Throws() {
//            PaymentMethodRegisterDTO dto = new PaymentMethodRegisterDTO();
//            dto.setPaymentMethod("INVALID_TYPE");
//            RuntimeException ex = assertThrows(RuntimeException.class, () -> paymentMethodService.createPaymentMethod(dto));
//            assertTrue(ex.getMessage().startsWith("Failed to create payment method"));
//        }
//    }
//
//    @Nested
//    class ActivatePaymentMethodTests {
//        @Test
//        void activatePaymentMethod_Success() {
//            // Setup
//            String id = PAYMENT_METHOD_ID.toString();
//            COD cod = new COD();
//            cod.setId(PAYMENT_METHOD_ID);
//            setupPaymentMethod(cod, Timestamp.valueOf(LocalDateTime.now().minusDays(1)));
//
//            // Setup mocks for repository calls
//            when(paymentMethodRepository.findById(any(UUID.class))).thenReturn(Optional.of(cod));
//            when(paymentMethodRepository.save(any(PaymentMethod.class))).thenAnswer(invocation -> {
//                PaymentMethod saved = invocation.getArgument(0);
//                assertNull(saved.getDeletedAt()); // DeletedAt should be cleared (activated)
//                return saved;
//            });
//
//            // Prepare expected DTO
//            CodDTO expectedDto = new CodDTO();
//            expectedDto.setId(PAYMENT_METHOD_ID);
//            expectedDto.setName(cod.getName());
//            expectedDto.setDescription(cod.getDescription());
//            expectedDto.setProcessingFee(cod.getProcessingFee());
//            expectedDto.setDeletedAt(null);
//
//            // Correct: pass actual cod object, not any()
//            mockConvertToDTO(cod, expectedDto);
//
//            // Call service method
//            PaymentMethodDTO result = paymentMethodService.activatePaymentMethod(id);
//
//            // Verify result
//            assertNotNull(result);
//            assertNull(result.getDeletedAt());
//
//            verify(paymentMethodRepository).save(any(PaymentMethod.class));
//        }
//
//        @Test
//        void activatePaymentMethod_NotFound() {
//            // Setup
//            String id = PAYMENT_METHOD_ID.toString();
//            when(paymentMethodRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
//
//            // Execute and verify
//            EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
//                    () -> paymentMethodService.activatePaymentMethod(id));
//            assertEquals("Payment method not found with id: " + id, ex.getMessage());
//        }
//
//        @Test
//        void activatePaymentMethod_AlreadyActive() {
//            // Setup
//            String id = PAYMENT_METHOD_ID.toString();
//            COD cod = new COD();
//            cod.setId(PAYMENT_METHOD_ID);
//            setupPaymentMethod(cod, null); // Already active (deletedAt is null)
//
//            when(paymentMethodRepository.findById(any(UUID.class))).thenReturn(Optional.of(cod));
//
//            // Execute and verify
//            EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
//                    () -> paymentMethodService.activatePaymentMethod(id));
//            assertEquals("Payment method with id is already active: " + id, ex.getMessage());
//        }
//    }
//
//    private void setupPaymentMethod(PaymentMethod paymentMethod, Timestamp deletedAt) {
//        paymentMethod.setName(switch (paymentMethod) {
//            case COD ignored -> "Test COD";
//            case BankTransfer ignored -> "Test Bank Transfer";
//            case EWallet ignored -> "Test EWallet";
//            default -> "Unknown Type";
//        });
//        paymentMethod.setDescription("Test Description");
//        paymentMethod.setProcessingFee(BigDecimal.valueOf(10.0));
//        paymentMethod.setCreatedBy(USER_ID);
//        paymentMethod.setCreatedAt(Timestamp.valueOf(LocalDateTime.now().minusDays(7)));
//        paymentMethod.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now().minusDays(3)));
//        paymentMethod.setDeletedAt(deletedAt);
//    }
//
//    private PaymentMethodRegisterDTO createCODRegisterDTO() {
//        PaymentMethodRegisterDTO dto = new PaymentMethodRegisterDTO();
//        dto.setName("Test COD");
//        dto.setDescription("Cash on Delivery");
//        dto.setProcessingFee(BigDecimal.valueOf(10.0));
//        dto.setPaymentMethod("COD");
//        dto.setPhoneNumber("1234567890");
//        dto.setInstructions("Call before delivery");
//        return dto;
//    }
//
//    private PaymentMethodRegisterDTO createBankTransferRegisterDTO() {
//        PaymentMethodRegisterDTO dto = new PaymentMethodRegisterDTO();
//        dto.setName("Test Bank Transfer");
//        dto.setDescription("Bank Transfer");
//        dto.setProcessingFee(BigDecimal.valueOf(5.0));
//        dto.setPaymentMethod("BANK_TRANSFER");
//        dto.setAccountName("John Doe");
//        dto.setAccountNumber("123456789");
//        dto.setBankName("Test Bank");
//        return dto;
//    }
//
//    private PaymentMethodRegisterDTO createEWalletRegisterDTO() {
//        PaymentMethodRegisterDTO dto = new PaymentMethodRegisterDTO();
//        dto.setName("Test EWallet");
//        dto.setDescription("EWallet");
//        dto.setProcessingFee(BigDecimal.valueOf(2.5));
//        dto.setPaymentMethod("E_WALLET");
//        dto.setAccountName("John Doe");
//        dto.setVirtualAccountNumber("123456789");
//        dto.setInstructions("Scan QR Code");
//        return dto;
//    }
//
//
//
//
//    @Nested
//    class UpdatePaymentMethodTests {
//
//        @Test
//        void updatePaymentMethod_COD_Success() {
//            PaymentMethodRegisterDTO dto = createCODRegisterDTO();
//            COD existingCod = new COD();
//            existingCod.setId(PAYMENT_METHOD_ID);
//            setupPaymentMethod(existingCod, null);
//            existingCod.setPhoneNumber("oldPhone");
//            existingCod.setInstructions("oldInstr");
//
//            when(paymentMethodRepository.findById(PAYMENT_METHOD_ID)).thenReturn(Optional.of(existingCod));
//            when(paymentMethodRepository.save(any(COD.class))).thenAnswer(i -> i.getArgument(0));
//
//            PaymentMethodDTO updatedDto = new CodDTO();
//            updatedDto.setId(PAYMENT_METHOD_ID);
//            mockConvertToDTO(existingCod, updatedDto);
//
//            PaymentMethodDTO result = paymentMethodService.updatePaymentMethod(PAYMENT_METHOD_ID, dto);
//
//            assertNotNull(result);
//            assertTrue(result instanceof CodDTO);
//            verify(paymentMethodRepository).save(any(COD.class));
//        }
//
//        @Test
//        void updatePaymentMethod_NotFound_Throws() {
//            PaymentMethodRegisterDTO dto = createCODRegisterDTO();
//            when(paymentMethodRepository.findById(PAYMENT_METHOD_ID)).thenReturn(Optional.empty());
//
//            EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
//                paymentMethodService.updatePaymentMethod(PAYMENT_METHOD_ID, dto);
//            });
//            assertTrue(ex.getMessage().contains("Payment method not found"));
//        }
//
//        @Test
//        void updatePaymentMethod_Deleted_Throws() {
//            PaymentMethodRegisterDTO dto = createCODRegisterDTO();
//            COD cod = new COD();
//            cod.setDeletedAt(Timestamp.valueOf(LocalDateTime.now()));
//            when(paymentMethodRepository.findById(PAYMENT_METHOD_ID)).thenReturn(Optional.of(cod));
//
//            EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
//                paymentMethodService.updatePaymentMethod(PAYMENT_METHOD_ID, dto);
//            });
//            assertTrue(ex.getMessage().contains("Payment method not found"));
//        }
//
//        @Test
//        void updatePaymentMethod_TypeMismatch_Throws() {
//            PaymentMethodRegisterDTO dto = createCODRegisterDTO();
//            dto.setPaymentMethod("BANK_TRANSFER"); // set different type to cause mismatch
//
//            COD cod = new COD();
//            cod.setDeletedAt(null);
//            when(paymentMethodRepository.findById(PAYMENT_METHOD_ID)).thenReturn(Optional.of(cod));
//
//            IllegalStateException ex = assertThrows(IllegalStateException.class, () -> {
//                paymentMethodService.updatePaymentMethod(PAYMENT_METHOD_ID, dto);
//            });
//            assertTrue(ex.getMessage().contains("Mismatched type"));
//        }
//
//        @Test
//        void updatePaymentMethod_InvalidType_Throws() {
//            PaymentMethodRegisterDTO dto = createCODRegisterDTO();
//            dto.setPaymentMethod("INVALID_TYPE");
//
//            COD cod = new COD();
//            cod.setDeletedAt(null);
//            when(paymentMethodRepository.findById(PAYMENT_METHOD_ID)).thenReturn(Optional.of(cod));
//
//            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
//                paymentMethodService.updatePaymentMethod(PAYMENT_METHOD_ID, dto);
//            });
//            assertTrue(ex.getMessage().contains("Unknown payment method type"));
//        }
//    }
//
//    @Nested
//    class DeletePaymentMethodTests {
//
//        @Test
//        void deletePaymentMethod_Success() {
//            PaymentMethod paymentMethod = new PaymentMethod();
//            paymentMethod.setId(PAYMENT_METHOD_ID);
//            paymentMethod.setDeletedAt(null);
//
//            when(paymentMethodRepository.findById(PAYMENT_METHOD_ID)).thenReturn(Optional.of(paymentMethod));
//            when(paymentMethodRepository.save(any(PaymentMethod.class))).thenAnswer(i -> i.getArgument(0));
//
//            Map<String, Object> result = paymentMethodService.deletePaymentMethod(PAYMENT_METHOD_ID.toString());
//
//            assertNotNull(result);
//            assertEquals(PAYMENT_METHOD_ID, result.get("id"));
//            assertNotNull(result.get("deleted_at"));
//            verify(paymentMethodRepository).save(any(PaymentMethod.class));
//        }
//
//        @Test
//        void deletePaymentMethod_NotFound_Throws() {
//            when(paymentMethodRepository.findById(PAYMENT_METHOD_ID)).thenReturn(Optional.empty());
//
//            EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
//                paymentMethodService.deletePaymentMethod(PAYMENT_METHOD_ID.toString());
//            });
//            assertTrue(ex.getMessage().contains("Payment method not found"));
//        }
//
//        @Test
//        void deletePaymentMethod_AlreadyDeleted_Throws() {
//            PaymentMethod paymentMethod = new PaymentMethod();
//            paymentMethod.setDeletedAt(Timestamp.valueOf(LocalDateTime.now()));
//
//            when(paymentMethodRepository.findById(PAYMENT_METHOD_ID)).thenReturn(Optional.of(paymentMethod));
//
//            EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
//                paymentMethodService.deletePaymentMethod(PAYMENT_METHOD_ID.toString());
//            });
//            assertTrue(ex.getMessage().contains("already deleted"));
//        }
//    }
//
//    @Nested
//    class FindActivePaymentMethodsByTypeTests {
//
//        @Test
//        void findActivePaymentMethodsByType_COD() {
//            COD cod = new COD();
//            cod.setId(PAYMENT_METHOD_ID);
//            when(codRepository.findAllByDeletedAtIsNull()).thenReturn(List.of(cod));
//            PaymentMethodUserDTO dto = new CodUserDTO();
//            mockConvertToUserDTO(cod, dto);
//
//            List<PaymentMethodUserDTO> result = paymentMethodService.findActivePaymentMethodsByType("COD");
//
//            assertNotNull(result);
//            assertEquals(1, result.size());
//            verify(codRepository).findAllByDeletedAtIsNull();
//        }
//
//        @Test
//        void findActivePaymentMethodsByType_Invalid() {
//            List<PaymentMethodUserDTO> result = paymentMethodService.findActivePaymentMethodsByType("INVALID_TYPE");
//            assertNull(result);
//        }
//
//        private void mockConvertToUserDTO(PaymentMethod paymentMethod, PaymentMethodUserDTO returnDto) {
//            PaymentMethodServiceImpl spyService = spy(paymentMethodService);
//            doReturn(returnDto).when(spyService).convertToUserDTO(paymentMethod);
//            paymentMethodService = spyService;
//        }
//
//    }
//
//    @Nested
//    class FindByTypeForAdminTests {
//
//        @Test
//        void findByTypeForAdmin_COD() {
//            COD cod = new COD();
//            cod.setId(PAYMENT_METHOD_ID);
//            when(codRepository.findAll()).thenReturn(List.of(cod));
//            PaymentMethodDTO dto = new CodDTO();
//            mockConvertToDTO(cod, dto);
//
//            List<PaymentMethodDTO> result = paymentMethodService.findByTypeForAdmin("COD");
//
//            assertNotNull(result);
//            assertEquals(1, result.size());
//            verify(codRepository).findAll();
//        }
//
//        @Test
//        void findByTypeForAdmin_Invalid() {
//            List<PaymentMethodDTO> result = paymentMethodService.findByTypeForAdmin("INVALID_TYPE");
//            assertNull(result);
//        }
//    }
//
//    @Nested
//    class FindAllActivePaymentMethodsTests {
//
//        @Test
//        void findAllActivePaymentMethods_Success() {
//            PaymentMethod pm = new PaymentMethod();
//            pm.setId(PAYMENT_METHOD_ID);
//            when(paymentMethodRepository.findByDeletedAtNull()).thenReturn(List.of(pm));
//            PaymentMethodUserDTO dto = new PaymentMethodUserDTO();
//            mockConvertToUserDTO(pm, dto);
//
//            List<PaymentMethodUserDTO> result = paymentMethodService.findAllActivePaymentMethods();
//
//            assertNotNull(result);
//            assertEquals(1, result.size());
//            verify(paymentMethodRepository).findByDeletedAtNull();
//        }
//
//        @Test
//        void findAllActivePaymentMethods_Empty() {
//            when(paymentMethodRepository.findByDeletedAtNull()).thenReturn(Collections.emptyList());
//
//            List<PaymentMethodUserDTO> result = paymentMethodService.findAllActivePaymentMethods();
//
//            assertNotNull(result);
//            assertTrue(result.isEmpty());
//        }
//
//        private void mockConvertToUserDTO(PaymentMethod paymentMethod, PaymentMethodUserDTO returnDto) {
//            PaymentMethodServiceImpl spyService = spy(paymentMethodService);
//            doReturn(returnDto).when(spyService).convertToUserDTO(paymentMethod);
//            paymentMethodService = spyService;
//        }
//
//    }
//
//    @Nested
//    class FindAllPaymentMethodsTests {
//
//        @Test
//        void findAllPaymentMethods_Success() {
//            PaymentMethod pm = new PaymentMethod();
//            pm.setId(PAYMENT_METHOD_ID);
//            when(paymentMethodRepository.findAll()).thenReturn(List.of(pm));
//            PaymentMethodDTO dto = new PaymentMethodDTO();
//            mockConvertToDTO(pm, dto);
//
//            List<PaymentMethodDTO> result = paymentMethodService.findAllPaymentMethods();
//
//            assertNotNull(result);
//            assertEquals(1, result.size());
//            verify(paymentMethodRepository).findAll();
//        }
//
//        @Test
//        void findAllPaymentMethods_Empty() {
//            when(paymentMethodRepository.findAll()).thenReturn(Collections.emptyList());
//
//            List<PaymentMethodDTO> result = paymentMethodService.findAllPaymentMethods();
//
//            assertNotNull(result);
//            assertTrue(result.isEmpty());
//        }
//    }
//
//    @Nested
//    class FindActivePaymentMethodByIdTests {
//
//        @Test
//        void findActivePaymentMethodById_Success() {
//            PaymentMethod pm = new PaymentMethod();
//            pm.setId(PAYMENT_METHOD_ID);          // Set the ID as expected
//            pm.setDeletedAt(null);                // Ensure active (not deleted)
//
//            when(paymentMethodRepository.findById(PAYMENT_METHOD_ID)).thenReturn(Optional.of(pm));
//
//            // Prepare a DTO with the same ID to be returned by convertToUserDTO
//            PaymentMethodUserDTO dto = new PaymentMethodUserDTO();
//            dto.setId(PAYMENT_METHOD_ID);
//
//            // Spy and mock convertToUserDTO method in the service
//            PaymentMethodServiceImpl spyService = spy(paymentMethodService);
//            doReturn(dto).when(spyService).convertToUserDTO(pm);
//            paymentMethodService = spyService;
//
//            PaymentMethodUserDTO result = paymentMethodService.findActivePaymentMethodById(PAYMENT_METHOD_ID.toString());
//
//            assertNotNull(result);
//            assertEquals(PAYMENT_METHOD_ID, result.getId());
//            verify(paymentMethodRepository).findById(PAYMENT_METHOD_ID);
//        }
//
//        @Test
//        void findActivePaymentMethodById_NotFound() {
//            when(paymentMethodRepository.findById(PAYMENT_METHOD_ID)).thenReturn(Optional.empty());
//
//            EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
//                paymentMethodService.findActivePaymentMethodById(PAYMENT_METHOD_ID.toString());
//            });
//
//            assertTrue(ex.getMessage().contains("not found"));
//        }
//
//        @Test
//        void findActivePaymentMethodById_Deleted() {
//            PaymentMethod pm = new PaymentMethod();
//            pm.setId(PAYMENT_METHOD_ID);
//            pm.setDeletedAt(Timestamp.valueOf(LocalDateTime.now()));
//            when(paymentMethodRepository.findById(PAYMENT_METHOD_ID)).thenReturn(Optional.of(pm));
//
//            EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
//                paymentMethodService.findActivePaymentMethodById(PAYMENT_METHOD_ID.toString());
//            });
//
//            assertTrue(ex.getMessage().contains("not found"));
//        }
//    }
//
//    @Nested
//    class ConversionTests {
//
//        @Test
//        void convertToDTO_COD() {
//            COD cod = new COD();
//            cod.setId(UUID.randomUUID());
//            cod.setPhoneNumber("08123456789");
//            cod.setInstructions("Call before delivery");
//            setupPaymentMethod(cod, null);
//
//            PaymentMethodDTO dto = paymentMethodService.convertToDTO(cod);
//
//            assertTrue(dto instanceof CodDTO);
//            assertEquals(cod.getPhoneNumber(), ((CodDTO) dto).getPhoneNumber());
//            assertEquals(cod.getInstructions(), ((CodDTO) dto).getInstructions());
//            assertEquals(cod.getId(), dto.getId());
//        }
//
//        @Test
//        void convertToDTO_BankTransfer() {
//            BankTransfer bt = new BankTransfer();
//            bt.setId(UUID.randomUUID());
//            bt.setAccountName("John Doe");
//            bt.setAccountNumber("123456789");
//            bt.setBankName("Test Bank");
//            setupPaymentMethod(bt, null);
//
//            PaymentMethodDTO dto = paymentMethodService.convertToDTO(bt);
//
//            assertTrue(dto instanceof BankTransferDTO);
//            assertEquals(bt.getAccountName(), ((BankTransferDTO) dto).getAccountName());
//            assertEquals(bt.getAccountNumber(), ((BankTransferDTO) dto).getAccountNumber());
//            assertEquals(bt.getBankName(), ((BankTransferDTO) dto).getBankName());
//            assertEquals(bt.getId(), dto.getId());
//        }
//
//        @Test
//        void convertToDTO_EWallet() {
//            EWallet ew = new EWallet();
//            ew.setId(UUID.randomUUID());
//            ew.setAccountName("John Doe");
//            ew.setVirtualAccountNumber("987654321");
//            ew.setInstructions("Scan QR Code");
//            setupPaymentMethod(ew, null);
//
//            PaymentMethodDTO dto = paymentMethodService.convertToDTO(ew);
//
//            assertTrue(dto instanceof EWalletDTO);
//            assertEquals(ew.getAccountName(), ((EWalletDTO) dto).getAccountName());
//            assertEquals(ew.getVirtualAccountNumber(), ((EWalletDTO) dto).getVirtualAccountNumber());
//            assertEquals(ew.getInstructions(), ((EWalletDTO) dto).getInstructions());
//            assertEquals(ew.getId(), dto.getId());
//        }
//
//        @Test
//        void convertToDTO_UnknownType_Throws() {
//            PaymentMethod unknown = new PaymentMethod();
//            unknown.setId(UUID.randomUUID());
//            unknown.setName("Unknown");
//            unknown.setDescription("Unknown type");
//            unknown.setProcessingFee(BigDecimal.ZERO);
//            unknown.setCreatedBy(UUID.randomUUID());
//            unknown.setCreatedAt(new Timestamp(System.currentTimeMillis()));
//            unknown.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
//
//            IllegalStateException ex = assertThrows(IllegalStateException.class, () -> paymentMethodService.convertToDTO(unknown));
//            assertEquals("Payment method not found", ex.getMessage());
//        }
//
//        @Test
//        void convertToUserDTO_COD() {
//            COD cod = new COD();
//            cod.setId(UUID.randomUUID());
//            cod.setPhoneNumber("08123456789");
//            cod.setInstructions("Call before delivery");
//            setupPaymentMethod(cod, null);
//
//            PaymentMethodUserDTO dto = paymentMethodService.convertToUserDTO(cod);
//
//            assertTrue(dto instanceof CodUserDTO);
//            assertEquals(cod.getPhoneNumber(), ((CodUserDTO) dto).getPhoneNumber());
//            assertEquals(cod.getInstructions(), ((CodUserDTO) dto).getInstructions());
//            assertEquals(cod.getId(), dto.getId());
//        }
//
//        @Test
//        void convertToUserDTO_BankTransfer() {
//            BankTransfer bt = new BankTransfer();
//            bt.setId(UUID.randomUUID());
//            bt.setAccountName("John Doe");
//            bt.setAccountNumber("123456789");
//            bt.setBankName("Test Bank");
//            setupPaymentMethod(bt, null);
//
//            PaymentMethodUserDTO dto = paymentMethodService.convertToUserDTO(bt);
//
//            assertTrue(dto instanceof BankTransferUserDTO);
//            assertEquals(bt.getAccountName(), ((BankTransferUserDTO) dto).getAccountName());
//            assertEquals(bt.getAccountNumber(), ((BankTransferUserDTO) dto).getAccountNumber());
//            assertEquals(bt.getBankName(), ((BankTransferUserDTO) dto).getBankName());
//            assertEquals(bt.getId(), dto.getId());
//        }
//
//        @Test
//        void convertToUserDTO_EWallet() {
//            EWallet ew = new EWallet();
//            ew.setId(UUID.randomUUID());
//            ew.setAccountName("John Doe");
//            ew.setVirtualAccountNumber("987654321");
//            ew.setInstructions("Scan QR Code");
//            setupPaymentMethod(ew, null);
//
//            PaymentMethodUserDTO dto = paymentMethodService.convertToUserDTO(ew);
//
//            assertTrue(dto instanceof EWalletUserDTO);
//            assertEquals(ew.getAccountName(), ((EWalletUserDTO) dto).getAccountName());
//            assertEquals(ew.getVirtualAccountNumber(), ((EWalletUserDTO) dto).getVirtualAccountNumber());
//            assertEquals(ew.getInstructions(), ((EWalletUserDTO) dto).getInstructions());
//            assertEquals(ew.getId(), dto.getId());
//        }
//
//        @Test
//        void convertToUserDTO_UnknownType_Throws() {
//            PaymentMethod unknown = new PaymentMethod();
//            unknown.setId(UUID.randomUUID());
//
//            IllegalStateException ex = assertThrows(IllegalStateException.class, () -> paymentMethodService.convertToUserDTO(unknown));
//            assertEquals("Payment method not found", ex.getMessage());
//        }
//    }
//
//
//}