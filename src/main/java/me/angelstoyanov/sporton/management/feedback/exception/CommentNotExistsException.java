package me.angelstoyanov.sporton.management.feedback.exception;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class CommentNotExistsException extends RuntimeException {
    public CommentNotExistsException(String message) {
        super(message);
    }

    public CommentNotExistsException(String message, Throwable cause) {
        super(message, cause);
    }

}
