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
    void testValidEmail() {
        assertNull(validator.validateEmail("test@gmail.com"));
        assertNull(validator.validateEmail("student.name@concordia.ca"));
    }
    @Test
    void testEmptyEmail() {
        assertEquals("Please enter an email address", validator.validateEmail(""));
    }
    @Test
    void testNullEmail() {
        assertEquals("Please enter an email address", validator.validateEmail(null));
    }
    @Test
    void testWhitespaceEmail() {
        assertEquals("Please enter an email address", validator.validateEmail("   "));
    }
    @Test
    void testEmailWithoutAtSymbol() {
        assertEquals("Please enter a valid email address", validator.validateEmail("testemail.com"));
    }
    @Test
    void testEmailWithoutDomain() {
        assertEquals("Please enter a valid email address", validator.validateEmail("test@.com"));
        assertEquals("Please enter a valid email address", validator.validateEmail("test@domain"));
    }

    // --- Password Tests ---
    @Test
    void testValidPassword() {
        assertNull(validator.validatePassword("password123"));
        assertNull(validator.validatePassword("securePassw0rd!"));
    }
    @Test
    void testShortPassword() {
        assertEquals("Password must be at least 6 characters", validator.validatePassword("12345"));
    }
    @Test
    void testPasswordExactlySixCharacters() {
        assertNull(validator.validatePassword("123456"));
    }
    @Test
    void testNullPassword() {
        assertEquals("Please enter a password", validator.validatePassword(null));
    }
    @Test
    void testEmptyPassword() {
        assertEquals("Please enter a password", validator.validatePassword(""));
    }
    @Test
    void testWhitespacePassword() {
        assertEquals("Please enter a password", validator.validatePassword("     "));
    }
    @Test
    void testPasswordUpperBoundaryValue() {
        assertNull(validator.validatePassword("1234567"));
    }

    // =========================================================================
    // Admin Event Creation Validation Tests
    // =========================================================================

    @Test
    void validEventInputs() {
        assertTrue(validator.isEventInputValid("Concert", "Montreal", "Music", true, true));
    }

    @Test
    void EmptyEventTitle() {
        assertFalse(validator.isEventInputValid("", "Montreal", "Music", true, true));
    }

    @Test
    void emptyEventLocation() {
        assertFalse(validator.isEventInputValid("Concert", "", "Music", true, true));
    }

    @Test
    void eventDateIsNotSelected() {
        assertFalse(validator.isEventInputValid("Concert", "Montreal", "Music", false, true));
    }

    @Test
    void eventTimeIsNotSelected() {
        assertFalse(validator.isEventInputValid("Concert", "Montreal", "Music", true, false));
    }

    // Capacity Tests
    @Test
    void testValidCapacity() {
            assertNull(validator.validateCapacity("100", 50));
    }

    @Test
    void testCapacityLessThanOne() {
        assertEquals("Capacity must be at least 1", validator.validateCapacity("0", 0));
        assertEquals("Capacity must be at least 1", validator.validateCapacity("-5", 0));
    }

    @Test
    void testCapacityNAN() {
        assertEquals("Enter a valid whole number", validator.validateCapacity("abc", 0));
    }

    @Test
    void testCapacityLessThanBookings() {
        assertEquals("Capacity cannot be less than tickets already booked", validator.validateCapacity("50", 60));
    }
}