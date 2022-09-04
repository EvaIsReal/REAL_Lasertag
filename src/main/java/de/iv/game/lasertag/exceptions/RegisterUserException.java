package de.iv.game.lasertag.exceptions;

public class RegisterUserException extends Exception {

    @Override
    public String getMessage() {
        return "User is already registered";
    }
}
