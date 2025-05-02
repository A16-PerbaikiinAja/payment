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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.ArrayList;

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
        return null;
    }

    @Override
    public Page<PaymentMethodDTO> findAllPaymentMethod(int page, int size, Boolean isActive, String paymentMethod, String sortBy, String sortDirection) {
        Sort.Direction direction = (sortDirection != null && "DESC".equalsIgnoreCase(sortDirection)) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, sortBy != null && !sortBy.isEmpty() ? sortBy : "id");
        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<PaymentMethod> spec = Specification.where(null);

        if (isActive != null) {
            if (isActive) {
                spec = spec.and((root, query, cb) -> cb.isNull(root.get("deletedAt")));
            } else {
                spec = spec.and((root, query, cb) -> cb.isNotNull(root.get("deletedAt")));
            }
        }

        if (paymentMethod != null) {
            Class<? extends PaymentMethod> entityClass = switch (PaymentMethodType.fromString(paymentMethod)) {
                case COD -> COD.class;
                case BANK_TRANSFER -> BankTransfer.class;
                case E_WALLET -> EWallet.class;
                default -> throw new IllegalArgumentException("Unknown payment method type: " + paymentMethod);
            };

            String entityName = entityClass.getSimpleName();

            String isActiveCondition = isActive != null && isActive ? "AND p.deletedAt IS NULL" : "";

            String jpql = "SELECT p FROM " + entityName + " p WHERE p.deletedAt IS NULL " + isActiveCondition;
            List<? extends PaymentMethod> resultList = entityManager.createQuery(jpql, entityClass)
                    .setFirstResult(page * size)
                    .setMaxResults(size)
                    .getResultList();

            String countJpql = "SELECT COUNT(p) FROM " + entityName + " p WHERE p.deletedAt IS NULL " + isActiveCondition;
            Long count = entityManager.createQuery(countJpql, Long.class).getSingleResult();

            List<PaymentMethodDTO> dtoList = resultList.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            return new PageImpl<>(dtoList, PageRequest.of(page, size), count);
        }

        Page<PaymentMethod> paymentMethods = paymentMethodRepository.findAll(spec, pageable);
        return paymentMethods == null ? Page.empty() : paymentMethods.map(this::convertToDTO);
    }

    @Override
    public PaymentMethodDTO findPaymentMethodById(String id) {
        UUID uuid = UUID.fromString(id);
        PaymentMethod paymentMethod = paymentMethodRepository.findById(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Payment method not found with id: " + id));

        return convertToDTO(paymentMethod);
    }

    @Override
    public ArrayList<Object> deletePaymentMethod(String id) {
        return new ArrayList<>();
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