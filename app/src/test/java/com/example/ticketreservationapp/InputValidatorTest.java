package com.example.ticketreservationapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class InputValidatorTest {

    private InputValidator validator;

    @BeforeEach
    void setUp() {
        validator = new InputValidator();
    }

    // --- Email Tests ---
    @Test
    void testValidEmailReturnsNull() {
        assertNull(validator.validateEmail("test@gmail.com"));
        assertNull(validator.validateEmail("student.name@concordia.ca"));
    }

    @Test
    void testEmptyEmailReturnsError() {
        assertEquals("Please enter an email address", validator.validateEmail(""));
    }

    @Test
    void testNullEmailReturnsError() {
        assertEquals("Please enter an email address", validator.validateEmail(null));
    }

    @Test
    void testWhitespaceEmailReturnsError() {
        assertEquals("Please enter an email address", validator.validateEmail("   "));
    }

    @Test
    void testEmailWithoutAtSymbolReturnsError() {
        assertEquals("Please enter a valid email address", validator.validateEmail("testemail.com"));
    }

    @Test
    void testEmailWithoutDomainReturnsError() {
        assertEquals("Please enter a valid email address", validator.validateEmail("test@.com"));
        assertEquals("Please enter a valid email address", validator.validateEmail("test@domain"));
    }

    // --- Password Tests ---
    @Test
    void testValidPasswordReturnsNull() {
        assertNull(validator.validatePassword("password123"));
        assertNull(validator.validatePassword("securePassw0rd!"));
    }

    @Test
    void testShortPasswordReturnsError() {
        assertEquals("Password must be at least 6 characters", validator.validatePassword("12345"));
    }

    @Test
    void testPasswordExactlySixCharactersReturnsNull() {
        assertNull(validator.validatePassword("123456"));
    }

    @Test
    void testNullPasswordReturnsError() {
        assertEquals("Password must be at least 6 characters", validator.validatePassword(null));
    }

    @Test
    void testEmptyPasswordReturnsError() {
        assertEquals("Password must be at least 6 characters", validator.validatePassword(""));
    }

    @Test
    void testWhitespacePasswordReturnsError() {
        assertEquals("Password must be at least 6 characters", validator.validatePassword("     "));
    }

    // --- Boundary Value Analysis Tests ---
    @Test
    void testPasswordBoundaryValues() {
        // TAs love boundary testing! Check exactly 5, 6, and 7 characters.
        assertEquals("Password must be at least 6 characters", validator.validatePassword("12345")); // Just under boundary
        assertNull(validator.validatePassword("123456")); // On boundary
        assertNull(validator.validatePassword("1234567")); // Just over boundary
    }
}