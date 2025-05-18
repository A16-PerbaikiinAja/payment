package id.ac.ui.cs.advprog.payment.controller;

import id.ac.ui.cs.advprog.payment.enums.ErrorCode;
import id.ac.ui.cs.advprog.payment.enums.Status;
import id.ac.ui.cs.advprog.payment.dto.Response;
import id.ac.ui.cs.advprog.payment.dto.paymentmethod.PaymentMethodDTO;
import id.ac.ui.cs.advprog.payment.dto.paymentmethod.PaymentMethodRegisterDTO;
import id.ac.ui.cs.advprog.payment.dto.paymentmethod.PaymentMethodUserDTO;
import id.ac.ui.cs.advprog.payment.service.PaymentMethodService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import jakarta.annotation.security.PermitAll;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@CrossOrigin(
        origins = "*",
        allowedHeaders = "*",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.PATCH}
)
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

        } catch (IllegalArgumentException e) {
            Response response = new Response(Status.error.toString(), e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        } catch (EntityNotFoundException e) {
            Response response = new Response(Status.error.toString(), e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            Response response = new Response(Status.error.toString(), "Internal Server Error", null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // View all Payment Methods (R) - Admin Only
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<?> findAllPaymentMethods() {
        try {
            List<PaymentMethodDTO> result = paymentMethodService.findAllPaymentMethods();
            Response response = new Response("success", "Payment methods retrieved successfully", result);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ErrorCode.GENERAL_ERROR.toErrorResponse(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Update Payment Method (U) - Admin Only
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/{id}/edit")
    public ResponseEntity<?> updatePaymentMethod(
            @PathVariable UUID id,
            @RequestBody PaymentMethodRegisterDTO dto
    ) {
        try {
            PaymentMethodDTO updated = paymentMethodService.updatePaymentMethod(id, dto);
            return ResponseEntity.ok(new Response("success", "Payment method updated successfully", updated));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Response("error", e.getMessage(), null));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response("error", e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response("error", "Internal Server Error", null));
        }
    }

    // Delete Payment Method (D) - Admin Only
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/{id}/delete")
    public ResponseEntity<?> deletePaymentMethod(@PathVariable String id) {
        Map<String, Object> result = paymentMethodService.deletePaymentMethod(id);
        Response response = new Response("success", "Payment method deleted successfully", result);
        return ResponseEntity.ok(response);
    }

    // Activate Payment Method (U) - Admin Only
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/admin/{id}/activate")
    public ResponseEntity<?> activatePaymentMethod(@PathVariable String id) {
        PaymentMethodDTO result = paymentMethodService.activatePaymentMethod(id);
        Response response = new Response("success", "Payment method reactivate successfully", result);
        return ResponseEntity.ok(response);
    }

//    =================

    // View Payment Method details by ID (R) - Admin Only
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/{id}")
    public ResponseEntity<?> getAdminPaymentMethodById(@PathVariable String id) {
        try {
            PaymentMethodDTO dto = paymentMethodService.findPaymentMethodById(id);
            if (dto == null) {
                Response response = new Response("error", "Payment method not found.", null);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            Response response = new Response("success", "Payment method retrieved successfully", dto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("code", "5000");
            errorResponse.put("message", "Internal Server Error");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // View Active Payment Methods (R) - Public (All Users)
    @PermitAll
    @GetMapping("/active")
    public ResponseEntity<?> findAllActivePaymentMethods() {
        try {
            List<PaymentMethodUserDTO> result = paymentMethodService.findAllActivePaymentMethods();
            Response response = new Response("success", "Active payment methods retrieved successfully", result);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ErrorCode.GENERAL_ERROR.toErrorResponse(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // View Active Payment Method details (R) - Public (All Users)
    @PermitAll
    @GetMapping("/active/{id}")
    public ResponseEntity<?> getActivePaymentMethodById(@PathVariable String id) {
        try {
            PaymentMethodUserDTO dto = paymentMethodService.findActivePaymentMethodById(id);
            if (dto == null) {
                Response response = new Response("error", "Active payment method not found.", null);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            Response response = new Response("success", "Active payment method retrieved successfully", dto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("code", "5000");
            errorResponse.put("message", "Internal Server Error");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // View Active Payment Methods by Type (R) - Public (All Users)
    @PermitAll
    @GetMapping("/type")
    public ResponseEntity<?> getByType(@RequestParam("type") String type) {
        try {
            List<PaymentMethodUserDTO> result = paymentMethodService.findActivePaymentMethodsByType(type);
            Response response = new Response("success", "Payment methods by type retrieved successfully", result);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("code", "5000");
            errorResponse.put("message", "Internal Server Error");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // View Payment Methods by Type (R) - Admin Only
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/type")
    public ResponseEntity<?> getByTypeForAdmin(@RequestParam("type") String type) {
        try {
            List<PaymentMethodDTO> result = paymentMethodService.findByTypeForAdmin(type);
            Response response = new Response("success", "Payment methods by type retrieved successfully", result);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("code", "500");
            errorResponse.put("message", "Internal Server Error");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
