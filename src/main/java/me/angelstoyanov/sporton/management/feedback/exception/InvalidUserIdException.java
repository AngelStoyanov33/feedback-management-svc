package me.angelstoyanov.sporton.management.user.exception;

public class InvalidUserIdException extends RuntimeException{
    public InvalidUserIdException(String message) {
        super(message);
    }
    public InvalidUserIdException(String message, Throwable cause) {
        super(message, cause);
    }
}
