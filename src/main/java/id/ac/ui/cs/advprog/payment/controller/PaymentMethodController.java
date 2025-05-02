package id.ac.ui.cs.advprog.payment.controller;

import id.ac.ui.cs.advprog.payment.dto.paymentmethod.PaymentMethodDTO;
import id.ac.ui.cs.advprog.payment.dto.paymentmethod.PaymentMethodRegisterDTO;
import id.ac.ui.cs.advprog.payment.service.PaymentMethodService;
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
    public ResponseEntity<?> createPaymentMethod(@RequestBody PaymentMethodRegisterDTO paymentMethodToRegister) {
        PaymentMethodDTO result = paymentMethodService.createPaymentMethod(paymentMethodToRegister);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    // View all Payment Methods (R) - Admin Only
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<?> findAllPaymentMethods(@RequestParam int page, @RequestParam int size) {
        Page<PaymentMethodDTO> result = paymentMethodService.findAllPaymentMethod(page, size, null, null, "id", "ASC");
        return ResponseEntity.ok(result);
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
    public ResponseEntity<?> updatePaymentMethod(@PathVariable UUID id, @RequestBody PaymentMethodRegisterDTO dto) {
        PaymentMethodDTO result = paymentMethodService.updatePaymentMethod(id, dto);
        return ResponseEntity.ok(result);    }

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