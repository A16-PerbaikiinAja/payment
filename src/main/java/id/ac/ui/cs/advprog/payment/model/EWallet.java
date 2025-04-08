package id.ac.ui.cs.advprog.payment.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "e_wallet")
@PrimaryKeyJoinColumn(name = "id")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EWallet extends PaymentMethod {

    @Column(name = "account_name", nullable = false, length = 100)
    private String accountName;

    @Column(name = "virtual_account_number", nullable = false, length = 50)
    private String virtualAccountNumber;

    @Column(name = "instructions", columnDefinition = "TEXT")
    private String instructions;
}
