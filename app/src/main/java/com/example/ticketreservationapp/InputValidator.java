package com.example.ticketreservationapp;

public class InputValidator {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-z]{2,}$";

    public String validateEmail(String email){
        if(email == null || email.trim().isEmpty()){
            return "Please enter an email address";
        }
        if(!email.matches(EMAIL_REGEX)){
            return "Please enter a valid email address";
        }
        return null;
    }

    public String validatePassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            return "Please enter a password";
        }
        if (password.trim().length() < 6) {
            return "Password must be at least 6 characters";
        }
        return null;
    }

    public boolean isRoleSelectionValid(boolean isAdminSelected) {
        return true;
    }

    public boolean isEventInputValid(String title, String location, String category, boolean isDateSelected, boolean isTimeSelected) {
        return title != null && !title.trim().isEmpty()
                && location != null && !location.trim().isEmpty()
                && category != null && !category.trim().isEmpty()
                && isDateSelected
                && isTimeSelected;
    }

    public String validateCapacity(String capStr, int existingTicketsBooked) {
        if (capStr == null || capStr.trim().isEmpty()) {
            return null; // Empty capacity is allowed (handled elsewhere as 0)
        }
        try {
            int capacity = Integer.parseInt(capStr);
            if (capacity < 1) {
                return "Capacity must be at least 1";
            }
            if (existingTicketsBooked > 0 && capacity < existingTicketsBooked) {
                return "Capacity cannot be less than tickets already booked";
            }
            return null;
        } catch (NumberFormatException e) {
            return "Enter a valid whole number";
        }
    }
}