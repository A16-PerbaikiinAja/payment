package id.ac.ui.cs.advprog.payment.repository;

import id.ac.ui.cs.advprog.payment.model.PaymentMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class PaymentMethodRepositoryTest {

    @InjectMocks
    private PaymentMethodRepository paymentMethodRepository;

    @Mock
    private Specification<PaymentMethod> specification;

    private PageRequest pageRequest;

    @BeforeEach
    void setUp() {
        pageRequest = PageRequest.of(0, 10);
    }

    @Test
    void testFindAllWithSpecification() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();

        PaymentMethod paymentMethod1 = new PaymentMethod(id1, "Credit Card", "Payment via credit card", null, null, null, null, id1);
        PaymentMethod paymentMethod2 = new PaymentMethod(id2, "Debit Card", "Payment via debit card", null, null, null, null, id2);

        List<PaymentMethod> paymentMethods = Arrays.asList(paymentMethod1, paymentMethod2);
        Page<PaymentMethod> paymentMethodPage = new PageImpl<>(paymentMethods);

        when(paymentMethodRepository.findAll(specification, pageRequest)).thenReturn(paymentMethodPage);

        Page<PaymentMethod> result = paymentMethodRepository.findAll(specification, pageRequest);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(id1, result.getContent().get(0).getId());
        assertEquals(id2, result.getContent().get(1).getId());
    }

    @Test
    void testFindAllWithNoResults() {
        List<PaymentMethod> emptyList = Arrays.asList();
        Page<PaymentMethod> emptyPage = new PageImpl<>(emptyList);

        when(paymentMethodRepository.findAll(specification, pageRequest)).thenReturn(emptyPage);

        Page<PaymentMethod> result = paymentMethodRepository.findAll(specification, pageRequest);

        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
    }

    @Test
    void testRepositoryFindAll() {
        UUID id1 = UUID.randomUUID();
        PaymentMethod paymentMethod1 = new PaymentMethod(id1, "E-wallet", "Payment via E-wallet", null, null, null, null, id1);
        List<PaymentMethod> paymentMethods = Arrays.asList(paymentMethod1);

        when(paymentMethodRepository.findAll()).thenReturn(paymentMethods);

        List<PaymentMethod> result = paymentMethodRepository.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(id1, result.get(0).getId());
    }
}
