package id.ac.ui.cs.advprog.payment.controller;

import id.ac.ui.cs.advprog.payment.dto.paymentmethod.PaymentMethodRegisterDTO;
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

    // Create Payment Method (C) - Admin Only
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/create")
    public ResponseEntity<?> createPaymentMethod(@RequestBody PaymentMethodRegisterDTO paymentMethodToRegister) {
        return null;
    }

    // View all Payment Methods (R) - Admin Only
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<?> findAllPaymentMethods(@RequestParam int page, @RequestParam int size) {
        return null;
    }

    // View Payment Method details by ID (R) - Admin Only
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/{id}")
    public ResponseEntity<?> getPaymentMethodById(@PathVariable String id) {
        return null;
    }

    // Update Payment Method (U) - Admin Only
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/{id}/edit")
    public ResponseEntity<?> updatePaymentMethod(@PathVariable UUID id, @RequestBody PaymentMethodRegisterDTO dto) {
        return null;
    }

    // Delete Payment Method (D) - Admin Only
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/{id}/delete")
    public ResponseEntity<?> deletePaymentMethod(@PathVariable String id) {
        return null;
    }

    // View all Active Payment Methods (R) - Public (All Users)
    @PermitAll
    @GetMapping("/active")
    public ResponseEntity<?> findAllActivePaymentMethods(@RequestParam int page, @RequestParam int size) {
        return null;
    }

    // View Active Payment Method details (R) - Public (All Users)
    @PermitAll
    @GetMapping("/active/{id}")
    public ResponseEntity<?> getActivePaymentMethodById(@PathVariable String id) {
        return null;
    }

    @PermitAll
    @GetMapping("/type")
    public ResponseEntity<?> getByType(@RequestParam("type") String type, @RequestParam int page, @RequestParam int size) {
        return null;
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
