package id.ac.ui.cs.advprog.payment.repository;

import id.ac.ui.cs.advprog.payment.model.EWallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EWalletRepository extends JpaRepository<EWallet, UUID> {
    List<EWallet> findAllByDeletedAtIsNull();
}
