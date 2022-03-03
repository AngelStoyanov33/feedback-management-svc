package me.angelstoyanov.sporton.management.feedback.exception;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class RatingNotExistsException extends RuntimeException {
    public RatingNotExistsException(String message) {
        super(message);
    }

    public RatingNotExistsException(String message, Throwable cause) {
        super(message, cause);
    }

}
