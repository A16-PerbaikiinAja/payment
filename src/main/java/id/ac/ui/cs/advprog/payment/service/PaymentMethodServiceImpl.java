package id.ac.ui.cs.advprog.payment.service;

import id.ac.ui.cs.advprog.payment.dto.paymentmethod.EWalletDTO;
import id.ac.ui.cs.advprog.payment.dto.paymentmethod.BankTransferDTO;
import id.ac.ui.cs.advprog.payment.dto.paymentmethod.CodDTO;
import id.ac.ui.cs.advprog.payment.dto.paymentmethod.PaymentMethodDTO;
import id.ac.ui.cs.advprog.payment.dto.paymentmethod.PaymentMethodRegisterDTO;
import id.ac.ui.cs.advprog.payment.enums.PaymentMethodType;
import id.ac.ui.cs.advprog.payment.model.BankTransfer;
import id.ac.ui.cs.advprog.payment.model.COD;
import id.ac.ui.cs.advprog.payment.model.EWallet;
import id.ac.ui.cs.advprog.payment.model.PaymentMethod;
import id.ac.ui.cs.advprog.payment.repository.PaymentMethodRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PaymentMethodServiceImpl implements PaymentMethodService {

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public PaymentMethodDTO createPaymentMethod(PaymentMethodRegisterDTO dto) {
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setName(dto.getName());
        paymentMethod.setDescription(dto.getDescription());
        paymentMethod.setProcessingFee(dto.getProcessingFee());
        paymentMethod.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        paymentMethod.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
        paymentMethod.setCreatedBy(UUID.fromString(dto.getCreatedBy()));

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
    }

    @Override
    public PaymentMethodDTO updatePaymentMethod(UUID id, PaymentMethodRegisterDTO dto) {
        PaymentMethod existing = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Payment method not found with id: " + id));

        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        existing.setProcessingFee(dto.getProcessingFee());
        existing.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));

        if (dto.getStatus().equals("ACTIVE")) {
            existing.setDeletedAt(null);
        } else if (dto.getStatus().equals("INACTIVE")) {
            existing.setDeletedAt(Timestamp.valueOf(LocalDateTime.now()));
        }

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
    public List<PaymentMethodDTO> findAllActivePaymentMethods() {
        List<PaymentMethod> paymentMethods = paymentMethodRepository.findByDeletedAtNotNull();
        return paymentMethods.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PaymentMethodDTO> findAllPaymentMethods() {
        List<PaymentMethod> paymentMethods = paymentMethodRepository.findAll();
        return paymentMethods.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
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

        paymentMethod.setDeletedAt(Timestamp.valueOf(LocalDateTime.now()));
        paymentMethod.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));

        paymentMethodRepository.save(paymentMethod);

        Map<String, Object> deletedData = new HashMap<>();
        deletedData.put("id", paymentMethod.getId());
        deletedData.put("deleted_at", paymentMethod.getDeletedAt());

        return deletedData;
    }

    @Override
    public List<PaymentMethodDTO> findByType(String type) {
        return List.of();
    }

    private PaymentMethodDTO convertToDTO(PaymentMethod method) {
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
}
