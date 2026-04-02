package com.example.ticketreservationapp;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EventValidationTest {

    private boolean isEventInputValid(String title, String location, String category,
                                      boolean isDateSelected, boolean isTimeSelected) {
        return !title.trim().isEmpty()
                && !location.trim().isEmpty()
                && !category.trim().isEmpty()
                && isDateSelected
                && isTimeSelected;
    }

    @Test
    void shouldPassWhenAllInputsAreValid() {
        assertTrue(isEventInputValid("Concert", "Montreal", "Music", true, true));
    }

    @Test
    void shouldFailWhenTitleIsEmpty() {
        assertFalse(isEventInputValid("", "Montreal", "Music", true, true));
    }

    @Test
    void shouldFailWhenLocationIsEmpty() {
        assertFalse(isEventInputValid("Concert", "", "Music", true, true));
    }

    @Test
    void shouldFailWhenCategoryIsEmpty() {
        assertFalse(isEventInputValid("Concert", "Montreal", "", true, true));
    }

    @Test
    void shouldFailWhenDateIsNotSelected() {
        assertFalse(isEventInputValid("Concert", "Montreal", "Music", false, true));
    }

    @Test
    void shouldFailWhenTimeIsNotSelected() {
        assertFalse(isEventInputValid("Concert", "Montreal", "Music", true, false));
    }

    @Test
    void shouldFailWhenAllTextFieldsAreBlank() {
        assertFalse(isEventInputValid("", "", "", true, true));
    }
}