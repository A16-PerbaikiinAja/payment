package id.ac.ui.cs.advprog.payment.service;

import id.ac.ui.cs.advprog.payment.dto.paymentmethod.PaymentMethodDTO;
import id.ac.ui.cs.advprog.payment.dto.paymentmethod.PaymentMethodRegisterDTO;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface PaymentMethodService {
    PaymentMethodDTO createPaymentMethod(PaymentMethodRegisterDTO paymentMethodToRegister);
    PaymentMethodDTO updatePaymentMethod(UUID id, PaymentMethodRegisterDTO dto);
    List<PaymentMethodDTO> findAllPaymentMethods();
    List<PaymentMethodDTO> findAllActivePaymentMethods();
    PaymentMethodDTO findPaymentMethodById(String id);
    Map<String, Object> deletePaymentMethod(String id);
    List<PaymentMethodDTO> findByType(String type);
}