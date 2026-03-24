package com.example.ticketreservationapp;

public class LoginValidator {

    public String EmailValidator(String email){
        if(email == null || email.trim().isEmpty()){
            return "Please enter an email address";
        }
        if(!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")){
            return "Please enter a valid email address";
        }
        return null;
    }

    public String PasswordValidator(String password) {
        if(password == null || password.trim().isEmpty()){
            return "Please enter a password";
        }
        return null;
    }

    public boolean isRoleSelectionValid(boolean isAdminSelected) {
        // Admin portal is currently blocked/under construction
        //TODO This method needs to be updated when Admin is added.
        return !isAdminSelected;
    }
}