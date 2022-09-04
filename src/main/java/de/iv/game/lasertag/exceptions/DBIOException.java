package de.iv.game.lasertag.exceptions;

public class DBIOException extends Exception {

    private String message;

    public DBIOException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
