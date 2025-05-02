package id.ac.ui.cs.advprog.payment.service;

import id.ac.ui.cs.advprog.payment.dto.paymentmethod.*;
import id.ac.ui.cs.advprog.payment.enums.PaymentMethodType;
import id.ac.ui.cs.advprog.payment.model.*;
import id.ac.ui.cs.advprog.payment.repository.PaymentMethodRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
public class PaymentMethodServiceImplTest {

    @InjectMocks
    private PaymentMethodServiceImpl service;

    @Mock
    private PaymentMethodRepository repository;

    @Mock
    private EntityManager entityManager;

    private PaymentMethodRegisterDTO buildDTO(PaymentMethodType type) {
        PaymentMethodRegisterDTO dto = new PaymentMethodRegisterDTO();
        dto.setName("Test Name");
        dto.setDescription("Test Desc");
        dto.setProcessingFee(BigDecimal.valueOf(1000));
        dto.setCreatedBy(UUID.randomUUID().toString());
        dto.setPaymentMethod(type.toString());
        dto.setStatus("ACTIVE");
        if (type == PaymentMethodType.COD) {
            dto.setPhoneNumber("0812");
            dto.setInstructions("Handle with care");
        } else if (type == PaymentMethodType.BANK_TRANSFER) {
            dto.setAccountName("John Doe");
            dto.setAccountNumber("12345678");
            dto.setBankName("Bank Test");
        } else if (type == PaymentMethodType.E_WALLET) {
            dto.setAccountName("Jane");
            dto.setVirtualAccountNumber("VA12345");
            dto.setInstructions("Scan QR");
        }
        return dto;
    }

    @Test
    void testCreateCOD() {
        PaymentMethodRegisterDTO dto = buildDTO(PaymentMethodType.COD);
        COD cod = new COD();
        cod.setId(UUID.randomUUID());
        when(repository.save(any(COD.class))).thenReturn(cod);
        assertNotNull(service.createPaymentMethod(dto));
    }

    @Test
    void testCreateBankTransfer() {
        PaymentMethodRegisterDTO dto = buildDTO(PaymentMethodType.BANK_TRANSFER);
        BankTransfer bt = new BankTransfer();
        bt.setId(UUID.randomUUID());
        when(repository.save(any(BankTransfer.class))).thenReturn(bt);
        assertNotNull(service.createPaymentMethod(dto));
    }

    @Test
    void testCreateEWallet() {
        PaymentMethodRegisterDTO dto = buildDTO(PaymentMethodType.E_WALLET);
        EWallet ew = new EWallet();
        ew.setId(UUID.randomUUID());
        when(repository.save(any(EWallet.class))).thenReturn(ew);
        assertNotNull(service.createPaymentMethod(dto));
    }

    @Test
    void testUpdateCOD() {
        UUID id = UUID.randomUUID();
        PaymentMethodRegisterDTO dto = buildDTO(PaymentMethodType.COD);
        COD cod = new COD();
        cod.setId(id);
        when(repository.findById(id)).thenReturn(Optional.of(cod));
        when(repository.save(any(COD.class))).thenReturn(cod);
        assertNotNull(service.updatePaymentMethod(id, dto));
    }

    @Test
    void testUpdateMismatchType() {
        UUID id = UUID.randomUUID();
        PaymentMethodRegisterDTO dto = buildDTO(PaymentMethodType.E_WALLET);
        COD cod = new COD();
        cod.setId(id);
        when(repository.findById(id)).thenReturn(Optional.of(cod));
        Exception ex = assertThrows(IllegalStateException.class, () -> service.updatePaymentMethod(id, dto));
        assertTrue(ex.getMessage().contains("Mismatched type"));
    }

    @Test
    void testFindByIdExists() {
        UUID id = UUID.randomUUID();
        COD cod = new COD();
        cod.setId(id);
        when(repository.findById(id)).thenReturn(Optional.of(cod));
        assertNotNull(service.findPaymentMethodById(id.toString()));
    }

    @Test
    void testFindByIdNotFound() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.findPaymentMethodById(id.toString()));
    }

    @Test
    void testDelete() {
        UUID id = UUID.randomUUID();
        COD cod = new COD();
        cod.setId(id);
        when(repository.findById(id)).thenReturn(Optional.of(cod));
        when(repository.save(any())).thenReturn(cod);
        Map<String, Object> result = service.deletePaymentMethod(id.toString());
        assertTrue(result.containsKey("deleted_at"));
    }

    @Test
    void testFindAll_NoType() {
        Page<PaymentMethod> page = new PageImpl<>(List.of(new COD()));
        when(repository.findAll((Specification<PaymentMethod>) any(), any(Pageable.class))).thenReturn(page);
        Page<PaymentMethodDTO> result = service.findAllPaymentMethod(0, 10, true, null, "id", "asc");
        assertFalse(result.isEmpty());
    }
}
