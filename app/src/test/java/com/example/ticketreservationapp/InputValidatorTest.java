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
        assertEquals("Please enter a password", validator.validatePassword(null));
    }
    @Test
    void testEmptyPasswordReturnsError() {
        assertEquals("Please enter a password", validator.validatePassword(""));
    }
    @Test
    void testWhitespacePasswordReturnsError() {
        assertEquals("Please enter a password", validator.validatePassword("     "));
    }
    @Test
    void testPasswordBoundaryValues() {
        assertEquals("Password must be at least 6 characters", validator.validatePassword("12345"));
        assertNull(validator.validatePassword("123456"));
        assertNull(validator.validatePassword("1234567"));
    }

    // =========================================================================
    // Admin Event Creation Validation Tests
    // =========================================================================

    @Test
    void shouldPassWhenAllEventInputsAreValid() {
        assertTrue(validator.isEventInputValid("Concert", "Montreal", "Music", true, true));
    }

    @Test
    void shouldFailWhenEventTitleIsEmpty() {
        assertFalse(validator.isEventInputValid("", "Montreal", "Music", true, true));
    }

    @Test
    void shouldFailWhenEventLocationIsEmpty() {
        assertFalse(validator.isEventInputValid("Concert", "", "Music", true, true));
    }

    @Test
    void shouldFailWhenEventDateIsNotSelected() {
        assertFalse(validator.isEventInputValid("Concert", "Montreal", "Music", false, true));
    }

    @Test
    void shouldFailWhenEventTimeIsNotSelected() {
        assertFalse(validator.isEventInputValid("Concert", "Montreal", "Music", true, false));
    }
}