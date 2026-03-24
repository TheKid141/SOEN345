package com.example.ticketreservationapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LoginValidatorTest {

    private LoginValidator validator;

    @BeforeEach
    void setUp() {
        validator = new LoginValidator();
    }

    // Email Tests

    @Test
    void emptyEmail(){
        assertEquals("Please enter an email address", validator.EmailValidator(""));
    }

    @Test
    void nullEmail(){
        assertEquals("Please enter an email address", validator.EmailValidator(null));
    }

    @Test
    void validEmail(){
        assertNull(validator.EmailValidator("test@gmail.com"));
    }

    @Test
    void invalidEmail(){
        assertEquals("Please enter a valid email address", validator.EmailValidator("testgmail.com"));
    }

    // Password Tests

    @Test
    void emptyPassword(){
        assertEquals("Please enter a password", validator.PasswordValidator(""));
    }

    @Test
    void nullPassword(){
        assertEquals("Please enter a password", validator.PasswordValidator(null));
    }

    @Test
    void validPassword(){
        assertNull(validator.PasswordValidator("password123"));

    }

    // Business Logic / Role Selection Tests

    @Test
    void testAdminRoleIsBlocked() {
        // The boolean represents 'isAdminSelected'. True should return false (blocked).
        //TODO This test needs to be removed and updated when Admin is added.
        assertFalse(validator.isRoleSelectionValid(true));
    }

    @Test
    void testCustomerRoleIsAllowed() {
        // False represents a standard customer. Should return true (allowed).
        assertTrue(validator.isRoleSelectionValid(false));
    }
}