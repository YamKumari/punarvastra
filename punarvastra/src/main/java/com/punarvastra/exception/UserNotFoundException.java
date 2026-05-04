package com.punarvastra.exception;

/**
 * Thrown when a user record cannot be located by identifier or credentials.
 */
public class UserNotFoundException extends Exception {

    /**
     * @param message detail for logging or generic user message
     */
    public UserNotFoundException(String message) {
        super(message);
    }
}
