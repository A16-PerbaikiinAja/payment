package id.ac.ui.cs.advprog.payment.controller;

import id.ac.ui.cs.advprog.payment.dto.paymentmethod.PaymentMethodRegisterDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/payment")
public class PaymentMethodController {

    @GetMapping
    public ResponseEntity<?> findAllPaymentMethods(@RequestParam int page, @RequestParam int size) {
        return null;
    }

    @GetMapping("/active")
    public ResponseEntity<?> findAllActivePaymentMethods(@RequestParam int page, @RequestParam int size) {
        return null;
    }

    @PostMapping
    public ResponseEntity<?> createPaymentMethod(@RequestBody PaymentMethodRegisterDTO paymentMethodToRegister) {
        return null;
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePaymentMethod(@PathVariable UUID id, @RequestBody PaymentMethodRegisterDTO dto) {
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPaymentMethodById(@PathVariable String id) {
        return null;
    }

    @GetMapping("/type")
    public ResponseEntity<?> getByType(@RequestParam("type") String type, @RequestParam int page, @RequestParam int size) {
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePaymentMethod(@PathVariable String id) {
        return null;
    }
}