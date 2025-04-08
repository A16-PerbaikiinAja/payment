package id.ac.ui.cs.advprog.payment.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Generated;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "users")
@Generated
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "address")
    private String address;
    @Column(name = "role", nullable = false)
    private String role;
    @Column(name = "profile_image")
    private String profileImage;
    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;
    @Column(name = "updated_at")
    private Timestamp updatedAt;
    @Column(name = "deleted_at")
    private Timestamp deletedAt;
    @Column(name = "last_login")
    private Timestamp lastLogin;
}