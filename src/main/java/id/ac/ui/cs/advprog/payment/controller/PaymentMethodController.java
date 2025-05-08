package id.ac.ui.cs.advprog.payment.controller;

import id.ac.ui.cs.advprog.payment.constant.ErrorCode;
import id.ac.ui.cs.advprog.payment.constant.Status;
import id.ac.ui.cs.advprog.payment.dto.Response;
import id.ac.ui.cs.advprog.payment.dto.paymentmethod.CustomPageResponse;
import id.ac.ui.cs.advprog.payment.dto.paymentmethod.PaymentMethodDTO;
import id.ac.ui.cs.advprog.payment.dto.paymentmethod.PaymentMethodRegisterDTO;
import id.ac.ui.cs.advprog.payment.service.PaymentMethodService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import jakarta.annotation.security.PermitAll;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/payment-methods")
public class PaymentMethodController {

    @Autowired
    private PaymentMethodService paymentMethodService;

    // Create Payment Method (C) - Admin Only
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/create")
    public ResponseEntity<?> createPaymentMethod(@Valid @RequestBody PaymentMethodRegisterDTO paymentMethodToRegister) {
        try {

            if (paymentMethodToRegister.getPaymentMethod() == null || paymentMethodToRegister.getPaymentMethod().isBlank()) {
                throw new IllegalArgumentException("paymentMethod cannot be null or empty");
            }

            switch (paymentMethodToRegister.getPaymentMethod().toUpperCase()) {
                case "COD" -> {
                    if (paymentMethodToRegister.getPhoneNumber() == null || paymentMethodToRegister.getPhoneNumber().isBlank()) {
                        throw new IllegalArgumentException("phoneNumber is required for COD");
                    }
                    if (paymentMethodToRegister.getInstructions() == null || paymentMethodToRegister.getInstructions().isBlank()) {
                        throw new IllegalArgumentException("instruction is required for COD");
                    }
                }
                case "E_WALLET" -> {
                    if (paymentMethodToRegister.getAccountName() == null || paymentMethodToRegister.getAccountName().isBlank()) {
                        throw new IllegalArgumentException("accountName is required for E_WALLET");
                    }
                    if (paymentMethodToRegister.getInstructions() == null || paymentMethodToRegister.getInstructions().isBlank()) {
                        throw new IllegalArgumentException("instruction is required for E_WALLET");
                    }
                    if (paymentMethodToRegister.getVirtualAccountNumber() == null || paymentMethodToRegister.getVirtualAccountNumber().isBlank()) {
                        throw new IllegalArgumentException("virtualAccountNumber is required for E_WALLET");
                    }
                }
                case "BANK_TRANSFER" -> {
                    if (paymentMethodToRegister.getAccountName() == null || paymentMethodToRegister.getAccountName().isBlank()) {
                        throw new IllegalArgumentException("accountName is required for BANK_TRANSFER");
                    }
                    if (paymentMethodToRegister.getAccountNumber() == null || paymentMethodToRegister.getAccountNumber().isBlank()) {
                        throw new IllegalArgumentException("accountNumber is required for BANK_TRANSFER");
                    }
                    if (paymentMethodToRegister.getBankName() == null || paymentMethodToRegister.getBankName().isBlank()) {
                        throw new IllegalArgumentException("bankName is required for BANK_TRANSFER");
                    }
                }
                default -> throw new IllegalArgumentException("Unsupported payment method: " + paymentMethodToRegister.getPaymentMethod());
            }

            PaymentMethodDTO result = paymentMethodService.createPaymentMethod(paymentMethodToRegister);
            Response response = new Response(Status.success.toString(), "Payment method created successfully", result);

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IllegalStateException | IllegalArgumentException | NullPointerException e) {
            Response response = new Response(Status.error.toString(), e.getMessage(), null);

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(ErrorCode.GENERAL_ERROR.toErrorResponse(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // View all Payment Methods (R) - Admin Only
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<?> findAllPaymentMethods(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) String paymentMethod,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(required = false, defaultValue = "ASC") String sortDirection
    ) {
        try {
            Page<PaymentMethodDTO> result = paymentMethodService.findAllPaymentMethod(page, size, isActive, paymentMethod, sortBy, sortDirection);
            CustomPageResponse<PaymentMethodDTO> customResponse = new CustomPageResponse<>(result);
            Response response = new Response("success", "Payment methods retrieved successfully", customResponse);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(ErrorCode.GENERAL_ERROR.toErrorResponse(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // View Payment Method details by ID (R) - Admin Only
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/{id}")
    public ResponseEntity<?> getPaymentMethodById(@PathVariable String id) {
        PaymentMethodDTO dto = paymentMethodService.findPaymentMethodById(id);
        if (dto.getDeletedAt() != null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Payment method not found or inactive.");
        }
        return ResponseEntity.ok(dto);
    }

    // Update Payment Method (U) - Admin Only
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/{id}/edit")
    public ResponseEntity<?> updatePaymentMethod(
            @PathVariable UUID id,
            @RequestBody PaymentMethodRegisterDTO dto
    ) {
        PaymentMethodDTO result = paymentMethodService.updatePaymentMethod(id, dto);
        Response response = new Response("success", "Payment method updated successfully", result);
        return ResponseEntity.ok(response);
    }

    // Delete Payment Method (D) - Admin Only
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/{id}/delete")
    public ResponseEntity<?> deletePaymentMethod(@PathVariable String id) {
        Map<String, Object> result = paymentMethodService.deletePaymentMethod(id);
        return ResponseEntity.ok(result);
    }

    // View all Active Payment Methods (R) - Public (All Users)
    @PermitAll
    @GetMapping("/active")
    public ResponseEntity<?> findAllActivePaymentMethods(@RequestParam int page, @RequestParam int size) {
        Page<PaymentMethodDTO> result = paymentMethodService.findAllPaymentMethod(page, size, true, null, "id", "ASC");
        return ResponseEntity.ok(result);
    }

    // View Active Payment Method details (R) - Public (All Users)
    @PermitAll
    @GetMapping("/active/{id}")
    public ResponseEntity<?> getActivePaymentMethodById(@PathVariable String id) {
        PaymentMethodDTO dto = paymentMethodService.findPaymentMethodById(id);
        if (dto.getDeletedAt() != null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Payment method not found or inactive.");
        }
        return ResponseEntity.ok(dto);
    }

    @PermitAll
    @GetMapping("/type")
    public ResponseEntity<?> getByType(@RequestParam("type") String type, @RequestParam int page, @RequestParam int size) {
        Page<PaymentMethodDTO> result = paymentMethodService.findAllPaymentMethod(page, size, true, type, "id", "ASC");
        return ResponseEntity.ok(result);
    }

    // For testing (no security, public)
    @PermitAll
    @GetMapping("/test")
    public ResponseEntity<?> testTesting() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "This is a test response!");
        response.put("status", "success");
        response.put("data", "Any other test data can go here!");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}