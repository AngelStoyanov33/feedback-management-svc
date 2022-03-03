package me.angelstoyanov.sporton.management.feedback.exception;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class RatingAlreadyExistsException extends RuntimeException {
    public RatingAlreadyExistsException(String message) {
        super(message);
    }

    public RatingAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
