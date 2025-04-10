package id.ac.ui.cs.advprog.payment.service;

import id.ac.ui.cs.advprog.payment.dto.paymentmethod.*;
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
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PaymentMethodServiceImpl implements PaymentMethodService {

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public PaymentMethodDTO createPaymentMethod(PaymentMethodRegisterDTO dto) {
    }

    @Override
    public PaymentMethodDTO updatePaymentMethod(UUID id, PaymentMethodRegisterDTO dto) {
        return null;
    }

    @Override
    public Page<PaymentMethodDTO> findAllPaymentMethod(int page, int size, Boolean isActive, String paymentMethod, String sortBy, String sortDirection) {
    }

    @Override
    public PaymentMethodDTO findPaymentMethodById(String id) {
    }

    @Override
    public Map<String, Object> deletePaymentMethod(String id) {
        return null;
    }

    private PaymentMethodDTO convertToDTO(PaymentMethod method) {
    }
}
