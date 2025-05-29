package id.ac.ui.cs.advprog.payment.service;

import id.ac.ui.cs.advprog.payment.exception.PaymentServiceException;
import id.ac.ui.cs.advprog.payment.exception.PaymentServiceTimeoutException;
import id.ac.ui.cs.advprog.payment.external.OrderData;
import id.ac.ui.cs.advprog.payment.dto.paymentmethod.*;
import id.ac.ui.cs.advprog.payment.enums.PaymentMethodType;
import id.ac.ui.cs.advprog.payment.external.OrderServiceClient;
import id.ac.ui.cs.advprog.payment.model.BankTransfer;
import id.ac.ui.cs.advprog.payment.model.COD;
import id.ac.ui.cs.advprog.payment.model.EWallet;
import id.ac.ui.cs.advprog.payment.model.PaymentMethod;
import id.ac.ui.cs.advprog.payment.repository.BankTransferRepository;
import id.ac.ui.cs.advprog.payment.repository.CODRepository;
import id.ac.ui.cs.advprog.payment.repository.EWalletRepository;
import id.ac.ui.cs.advprog.payment.repository.PaymentMethodRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpServletRequest;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentMethodServiceImpl implements PaymentMethodService {

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    private final OrderServiceClient orderServiceClient;
    private final ExecutorService customExecutor = Executors.newFixedThreadPool(10);

    @Autowired
    private final CODRepository codRepository;

    @Autowired
    private final BankTransferRepository bankTransferRepository;

    @Autowired
    private final EWalletRepository eWalletRepository;

    public UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }

        // Your principal is the userId (String) asset in JwtAuthenticationFilter
        Object principal = authentication.getPrincipal();

        if (principal instanceof String) {
            return UUID.fromString((String) principal);
        }

        throw new RuntimeException("Cannot extract user id from principal");
    }

    @Override
    public PaymentMethodDTO createPaymentMethod(PaymentMethodRegisterDTO dto) {
        try{
            PaymentMethod paymentMethod = new PaymentMethod();
            paymentMethod.setName(dto.getName());
            paymentMethod.setDescription(dto.getDescription());
            paymentMethod.setProcessingFee(dto.getProcessingFee());
            paymentMethod.setCreatedBy(getCurrentUserId());

            switch (PaymentMethodType.fromString(dto.getPaymentMethod())) {
                case COD -> {
                    COD cod = new COD(paymentMethod);
                    cod.setPhoneNumber(dto.getPhoneNumber());
                    cod.setInstructions(dto.getInstructions());
                    return convertToDTO(paymentMethodRepository.save(cod));
                }
                case BANK_TRANSFER -> {
                    BankTransfer bt = new BankTransfer(paymentMethod);
                    bt.setAccountName(dto.getAccountName());
                    bt.setAccountNumber(dto.getAccountNumber());
                    bt.setBankName(dto.getBankName());
                    return convertToDTO(paymentMethodRepository.save(bt));
                }
                case E_WALLET -> {
                    EWallet ew = new EWallet(paymentMethod);
                    ew.setAccountName(dto.getAccountName());
                    ew.setVirtualAccountNumber(dto.getVirtualAccountNumber());
                    ew.setInstructions(dto.getInstructions());
                    return convertToDTO(paymentMethodRepository.save(ew));
                }
                default -> throw new IllegalArgumentException("Unknown payment method type: " + dto.getPaymentMethod());
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to create payment method: " + e.getMessage());
        }

    }

    @Override
    public PaymentMethodDTO activatePaymentMethod(String id){
        PaymentMethod existing = paymentMethodRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new EntityNotFoundException("Payment method not found with id: " + id));
        if(existing.getDeletedAt() == null) {
            throw new EntityNotFoundException("Payment method with id is already active: " + id);
        }
        existing.setDeletedAt(null);
        paymentMethodRepository.save(existing);
        return convertToDTO(existing);
    }

    @Override
    public PaymentMethodDTO updatePaymentMethod(UUID id, PaymentMethodRegisterDTO dto) {
        PaymentMethod existing = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Payment method not found with id: " + id));
        if(!(existing.getDeletedAt() == null)) {
            throw new EntityNotFoundException("Payment method not found with id: " + id);
        }
        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        existing.setProcessingFee(dto.getProcessingFee());
        existing.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));


        switch (PaymentMethodType.fromString(dto.getPaymentMethod())) {
            case COD -> {
                if (existing instanceof COD) {
                    COD cod = (COD) existing;
                    cod.setPhoneNumber(dto.getPhoneNumber());
                    cod.setInstructions(dto.getInstructions());
                    return convertToDTO(paymentMethodRepository.save(cod));
                }
                throw new IllegalStateException("Mismatched type: expected COD but found " + existing.getClass().getSimpleName());
            }
            case BANK_TRANSFER -> {
                if (existing instanceof BankTransfer) {
                    BankTransfer bt = (BankTransfer) existing;
                    bt.setAccountName(dto.getAccountName());
                    bt.setAccountNumber(dto.getAccountNumber());
                    bt.setBankName(dto.getBankName());
                    return convertToDTO(paymentMethodRepository.save(bt));
                }
                throw new IllegalStateException("Mismatched type: expected BankTransfer but found " + existing.getClass().getSimpleName());
            }
            case E_WALLET -> {
                if (existing instanceof EWallet) {
                    EWallet ew = (EWallet) existing;
                    ew.setAccountName(dto.getAccountName());
                    ew.setVirtualAccountNumber(dto.getVirtualAccountNumber());
                    ew.setInstructions(dto.getInstructions());
                    return convertToDTO(paymentMethodRepository.save(ew));
                }
                throw new IllegalStateException("Mismatched type: expected EWallet but found " + existing.getClass().getSimpleName());
            }
            default -> throw new IllegalArgumentException("Unknown payment method type: " + dto.getPaymentMethod());
        }

    }


    @Override
    public List<PaymentMethodUserDTO> findAllActivePaymentMethods() {
        List<PaymentMethod> paymentMethods = paymentMethodRepository.findByDeletedAtNull();
        List<PaymentMethodUserDTO> listDTO = new ArrayList<>();
        for(PaymentMethod pm : paymentMethods) {
            listDTO.add(convertToUserDTO(pm));
        }
        return listDTO;
    }

    @Override
    public List<PaymentMethodDTO> findAllPaymentMethods() {
        List<PaymentMethod> paymentMethods = paymentMethodRepository.findAll();
        List<PaymentMethodDTO> listDTO = new ArrayList<>();
        for(PaymentMethod pm : paymentMethods) {
            listDTO.add(convertToDTO(pm));
        }
        return listDTO;
    }

    @Override
    public PaymentMethodDTO findPaymentMethodById(String id) {
        UUID uuid = UUID.fromString(id);
        PaymentMethod paymentMethod = paymentMethodRepository.findById(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Payment method not found with id: " + id));

        return convertToDTO(paymentMethod);
    }

    @Override
    public Map<String, Object> deletePaymentMethod(String id) {
        UUID uuid = UUID.fromString(id);
        PaymentMethod paymentMethod = paymentMethodRepository.findById(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Payment method not found with id: " + id));
        if(paymentMethod.getDeletedAt() != null) {
            throw new EntityNotFoundException("Payment method with id is already deleted: " + id);
        }
        paymentMethod.setDeletedAt(Timestamp.valueOf(LocalDateTime.now()));
        paymentMethod.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));

        paymentMethodRepository.save(paymentMethod);

        Map<String, Object> deletedData = new HashMap<>();
        deletedData.put("id", paymentMethod.getId());
        deletedData.put("deleted_at", paymentMethod.getDeletedAt());

        return deletedData;
    }

    @Override
    public List<PaymentMethodUserDTO> findActivePaymentMethodsByType(String type) {
        return switch (PaymentMethodType.fromString(type)) {
            case COD -> codRepository.findAllByDeletedAtIsNull().stream().map(this::convertToUserDTO).toList();
            case BANK_TRANSFER -> bankTransferRepository.findAllByDeletedAtIsNull().stream().map(this::convertToUserDTO).toList();
            case E_WALLET -> eWalletRepository.findAllByDeletedAtIsNull().stream().map(this::convertToUserDTO).toList();
            case INVALID -> null;
        };
    }

    @Override
    public List<PaymentMethodDTO> findByTypeForAdmin(String type) {
        return switch (PaymentMethodType.fromString(type)) {
            case COD -> codRepository.findAll().stream().map(this::convertToDTO).toList();
            case BANK_TRANSFER -> bankTransferRepository.findAll().stream().map(this::convertToDTO).toList();
            case E_WALLET -> eWalletRepository.findAll().stream().map(this::convertToDTO).toList();
            case INVALID -> null;
        };
    }

    @Override
    public PaymentMethodUserDTO findActivePaymentMethodById(String id) {
        UUID uuid = UUID.fromString(id);
        PaymentMethod paymentMethod = paymentMethodRepository.findById(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Payment method not found with id: " + id));

        // Pastikan data aktif (deletedAt == null)
        if (paymentMethod.getDeletedAt() != null) {
            throw new EntityNotFoundException("Active payment method not found with id: " + id);
        }

        return convertToUserDTO(paymentMethod);
    }


    PaymentMethodDTO convertToDTO(PaymentMethod method) {
        PaymentMethodDTO dto;

        if (method instanceof COD) {
            CodDTO cod = new CodDTO();
            cod.setPhoneNumber(((COD) method).getPhoneNumber());
            cod.setInstructions(((COD) method).getInstructions());
            cod.setPaymentMethod(PaymentMethodType.COD.toString());
            dto = cod;
        } else if (method instanceof BankTransfer) {
            BankTransferDTO bt = new BankTransferDTO();
            bt.setAccountName(((BankTransfer) method).getAccountName());
            bt.setAccountNumber(((BankTransfer) method).getAccountNumber());
            bt.setBankName(((BankTransfer) method).getBankName());
            bt.setPaymentMethod(PaymentMethodType.BANK_TRANSFER.toString());
            dto = bt;
        } else if (method instanceof EWallet) {
            EWalletDTO ew = new EWalletDTO();
            ew.setAccountName(((EWallet) method).getAccountName());
            ew.setVirtualAccountNumber(((EWallet) method).getVirtualAccountNumber());
            ew.setInstructions(((EWallet) method).getInstructions());
            ew.setPaymentMethod(PaymentMethodType.E_WALLET.toString());
            dto = ew;
        } else {
            throw new IllegalStateException("Payment method not found");
        }

        dto.setId(method.getId());
        dto.setName(method.getName());
        dto.setDescription(method.getDescription());
        dto.setProcessingFee(method.getProcessingFee());
        dto.setCreatedBy(method.getCreatedBy());
        dto.setCreatedAt(method.getCreatedAt());
        dto.setUpdatedAt(method.getUpdatedAt());
        dto.setDeletedAt(method.getDeletedAt());

        return dto;
    }

    PaymentMethodUserDTO convertToUserDTO(PaymentMethod method) {
        PaymentMethodUserDTO dto;

        if (method instanceof COD) {
            CodUserDTO cod = new CodUserDTO();
            cod.setPhoneNumber(((COD) method).getPhoneNumber());
            cod.setInstructions(((COD) method).getInstructions());
            cod.setPaymentMethod(PaymentMethodType.COD.toString());
            dto = cod;
        } else if (method instanceof BankTransfer) {
            BankTransferUserDTO bt = new BankTransferUserDTO();
            bt.setAccountName(((BankTransfer) method).getAccountName());
            bt.setAccountNumber(((BankTransfer) method).getAccountNumber());
            bt.setBankName(((BankTransfer) method).getBankName());
            bt.setPaymentMethod(PaymentMethodType.BANK_TRANSFER.toString());
            dto = bt;
        } else if (method instanceof EWallet) {
            EWalletUserDTO ew = new EWalletUserDTO();
            ew.setAccountName(((EWallet) method).getAccountName());
            ew.setVirtualAccountNumber(((EWallet) method).getVirtualAccountNumber());
            ew.setInstructions(((EWallet) method).getInstructions());
            ew.setPaymentMethod(PaymentMethodType.E_WALLET.toString());
            dto = ew;
        } else {
            throw new IllegalStateException("Payment method not found");
        }

        dto.setId(method.getId());
        dto.setName(method.getName());
        dto.setDescription(method.getDescription());
        dto.setProcessingFee(method.getProcessingFee());

        return dto;
    }

    public CompletableFuture<List<PaymentMethodDetailsDTO>> getAllPaymentMethodsWithOrderCountsAsync(HttpServletRequest request) {
        List<PaymentMethod> localPaymentMethods = paymentMethodRepository.findAll();

        CompletableFuture<List<OrderData>> allOrdersFuture = CompletableFuture.supplyAsync(
                () -> {
                    if (log.isDebugEnabled()) {
                        log.debug("Memulai panggilan async ke OrderServiceClient.getAllOrders() (Thread: {})",
                                Thread.currentThread().getName());
                    }
                    return orderServiceClient.getAllOrders(request);
                },
                customExecutor
        ).exceptionally(ex -> {
            if (log.isErrorEnabled()) {
                log.error("ERROR SERVICEIMPL: Gagal mengambil data list order dari Order Service: {}", ex.getMessage(), ex);
            }
            return Collections.emptyList();
        });

        return allOrdersFuture.thenApplyAsync(allFetchedOrders -> {
            if (log.isInfoEnabled()) {
                log.info("Memulai pemrosesan data order yang diterima di ServiceImpl (Thread: {})",
                        Thread.currentThread().getName());
            }

            Map<String, Long> countsByPaymentMethodId = allFetchedOrders.stream()
                    .filter(orderData -> orderData.getPaymentMethodId() != null)
                    .collect(Collectors.groupingBy(
                            orderData -> orderData.getPaymentMethodId().toString(),
                            Collectors.counting()
                    ));

            List<PaymentMethodDetailsDTO> resultDetails = localPaymentMethods.stream().map(method -> {
                PaymentMethodDetailsDTO dto = new PaymentMethodDetailsDTO();
                String currentMethodId = method.getId().toString();

                dto.setId(currentMethodId);
                dto.setName(method.getName());
                dto.setMethodType(determinePaymentMethodTypeString(method));

                if (method instanceof COD) {
                    dto.setInstructions(((COD) method).getInstructions());
                } else if (method instanceof EWallet) {
                    dto.setInstructions(((EWallet) method).getInstructions());
                }

                dto.setOrderCount(countsByPaymentMethodId.getOrDefault(currentMethodId, 0L).intValue());
                return dto;
            }).collect(Collectors.toList());

            if (log.isInfoEnabled()) {
                log.info("Semua data berhasil diproses dan digabungkan di ServiceImpl.");
            }
            return resultDetails;
        }, customExecutor);
    }

    @Override
    public List<PaymentMethodDetailsDTO> getAllPaymentMethodsWithOrderCounts(HttpServletRequest request) {
        try {
            if (log.isDebugEnabled()) {
                log.debug("Getting payment methods with order counts with 30 second timeout");
            }
            return getAllPaymentMethodsWithOrderCountsAsync(request)
                    .get(30, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            if (log.isErrorEnabled()) {
                log.error("Timeout: Failed to get data within 30 seconds", e);
            }
            throw new PaymentServiceTimeoutException("Service timeout - external service too slow", e);
        } catch (Exception e) {
            log.error("Error: Failed to get payment methods: {}", e.getMessage(), e);
            throw new PaymentServiceException("Service error", e);
        }
    }

    String determinePaymentMethodTypeString(PaymentMethod method) {
        if (method instanceof COD) {
            return PaymentMethodType.COD.toString();
        } else if (method instanceof BankTransfer) {
            return PaymentMethodType.BANK_TRANSFER.toString();
        } else if (method instanceof EWallet) {
            return PaymentMethodType.E_WALLET.toString();
        }
        throw new IllegalArgumentException("Unknown PaymentMethod subclass: " + method.getClass().getName());
    }

}