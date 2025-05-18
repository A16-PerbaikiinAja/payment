package id.ac.ui.cs.advprog.payment.repository;

import id.ac.ui.cs.advprog.payment.model.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, UUID>, JpaSpecificationExecutor<PaymentMethod> {
    List<PaymentMethod> findByDeletedAtNull();

}