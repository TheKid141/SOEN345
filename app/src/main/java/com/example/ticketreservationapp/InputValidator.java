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
}