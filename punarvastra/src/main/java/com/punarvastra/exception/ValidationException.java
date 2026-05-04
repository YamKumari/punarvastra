package com.punarvastra.exception;

/**
 * Thrown when server-side validation fails for user input.
 */
public class ValidationException extends Exception {

    /**
     * @param message specific validation error for the user
     */
    public ValidationException(String message) {
        super(message);
    }
}
