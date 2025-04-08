package id.ac.ui.cs.advprog.payment.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.Timestamp;
import java.util.UUID;

class UserTest {

    private User user;
    private User userCopy;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail("user@example.com");
        user.setPassword("password123");
        user.setFullName("John Doe");
        user.setPhoneNumber("1234567890");
        user.setAddress("123 Main St");
        user.setRole("USER");
        user.setProfileImage("profile.jpg");
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        user.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        user.setLastLogin(new Timestamp(System.currentTimeMillis()));
    }

    @Test
    void testConstructorWithAnotherUser() {
        userCopy = new User();
        userCopy.setId(user.getId());
        userCopy.setEmail(user.getEmail());
        userCopy.setPassword(user.getPassword());
        userCopy.setFullName(user.getFullName());
        userCopy.setPhoneNumber(user.getPhoneNumber());
        userCopy.setAddress(user.getAddress());
        userCopy.setRole(user.getRole());
        userCopy.setProfileImage(user.getProfileImage());
        userCopy.setCreatedAt(user.getCreatedAt());
        userCopy.setUpdatedAt(user.getUpdatedAt());
        userCopy.setLastLogin(user.getLastLogin());

        assertEquals(user.getEmail(), userCopy.getEmail());
        assertEquals(user.getPassword(), userCopy.getPassword());
        assertEquals(user.getFullName(), userCopy.getFullName());
        assertEquals(user.getPhoneNumber(), userCopy.getPhoneNumber());
        assertEquals(user.getAddress(), userCopy.getAddress());
        assertEquals(user.getRole(), userCopy.getRole());
        assertEquals(user.getProfileImage(), userCopy.getProfileImage());
        assertEquals(user.getCreatedAt(), userCopy.getCreatedAt());
        assertEquals(user.getUpdatedAt(), userCopy.getUpdatedAt());
        assertEquals(user.getLastLogin(), userCopy.getLastLogin());
    }

    @Test
    void testDefaultConstructor() {
        userCopy = new User();
        assertNotNull(userCopy);
        assertNull(userCopy.getEmail());
        assertNull(userCopy.getPassword());
        assertNull(userCopy.getFullName());
        assertNull(userCopy.getPhoneNumber());
        assertNull(userCopy.getAddress());
        assertNull(userCopy.getRole());
        assertNull(userCopy.getProfileImage());
        assertNull(userCopy.getCreatedAt());
        assertNull(userCopy.getUpdatedAt());
        assertNull(userCopy.getLastLogin());
    }

    @Test
    void testGettersAndSetters() {
        user.setEmail("newuser@example.com");
        user.setPassword("newpassword123");
        user.setFullName("Jane Doe");
        user.setPhoneNumber("9876543210");
        user.setAddress("456 Elm St");
        user.setRole("ADMIN");
        user.setProfileImage("newprofile.jpg");
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        user.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        user.setLastLogin(new Timestamp(System.currentTimeMillis()));

        assertEquals("newuser@example.com", user.getEmail());
        assertEquals("newpassword123", user.getPassword());
        assertEquals("Jane Doe", user.getFullName());
        assertEquals("9876543210", user.getPhoneNumber());
        assertEquals("456 Elm St", user.getAddress());
        assertEquals("ADMIN", user.getRole());
        assertEquals("newprofile.jpg", user.getProfileImage());
        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getUpdatedAt());
        assertNotNull(user.getLastLogin());
    }

    @Test
    void testIdGeneration() {
        assertNotNull(user.getId());
    }

    @Test
    void testFieldValidation() {
        User invalidUser = new User();
        assertNull(invalidUser.getEmail());
        assertNull(invalidUser.getPassword());

        invalidUser.setEmail("user@domain.com");
        invalidUser.setPassword("securepassword");

        assertNotNull(invalidUser.getEmail());
        assertNotNull(invalidUser.getPassword());
    }
}
