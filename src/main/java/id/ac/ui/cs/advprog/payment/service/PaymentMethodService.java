package id.ac.ui.cs.advprog.payment.service;

import id.ac.ui.cs.advprog.payment.dto.paymentmethod.PaymentMethodDTO;
import id.ac.ui.cs.advprog.payment.dto.paymentmethod.PaymentMethodRegisterDTO;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.UUID;

public interface PaymentMethodService {
    PaymentMethodDTO createPaymentMethod(PaymentMethodRegisterDTO paymentMethodToRegister);
    PaymentMethodDTO updatePaymentMethod(UUID id, PaymentMethodRegisterDTO dto);
    Page<PaymentMethodDTO> findAllPaymentMethod(int page, int size, Boolean isActive, String paymentMethod, String sortBy, String sortDirection);
    PaymentMethodDTO findPaymentMethodById(String id);
    ArrayList<Object> deletePaymentMethod(String id);
}