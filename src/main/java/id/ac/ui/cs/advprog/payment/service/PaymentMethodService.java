package id.ac.ui.cs.advprog.payment.service;

import id.ac.ui.cs.advprog.payment.dto.paymentmethod.PaymentMethodDTO;
import id.ac.ui.cs.advprog.payment.dto.paymentmethod.PaymentMethodDetailsDTO;
import id.ac.ui.cs.advprog.payment.dto.paymentmethod.PaymentMethodRegisterDTO;
import id.ac.ui.cs.advprog.payment.dto.paymentmethod.PaymentMethodUserDTO;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface PaymentMethodService {
    // Admin create & update always full DTO
    PaymentMethodDTO createPaymentMethod(PaymentMethodRegisterDTO paymentMethodToRegister);
    PaymentMethodDTO updatePaymentMethod(UUID id, PaymentMethodRegisterDTO dto);

    // Admin views (full DTO)
    List<PaymentMethodDTO> findAllPaymentMethods();
    PaymentMethodDTO findPaymentMethodById(String id);
    Map<String, Object> deletePaymentMethod(String id);
    List<PaymentMethodDTO> findByTypeForAdmin(String type);
    PaymentMethodDTO activatePaymentMethod(String id);

    // User/public views (simpler DTO)
    List<PaymentMethodUserDTO> findAllActivePaymentMethods();
    PaymentMethodUserDTO findActivePaymentMethodById(String id);
    List<PaymentMethodUserDTO> findActivePaymentMethodsByType(String type);

    //Asynchronous with Order Service
    List<PaymentMethodDetailsDTO> getAllPaymentMethodsWithOrderCounts(HttpServletRequest request);

}
