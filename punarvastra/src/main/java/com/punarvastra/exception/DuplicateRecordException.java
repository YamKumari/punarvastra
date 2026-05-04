package com.punarvastra.exception;

/**
 * Thrown when a unique database constraint would be violated (e.g. duplicate email).
 */
public class DuplicateRecordException extends Exception {

    /**
     * Creates an exception with a user-facing message.
     *
     * @param message specific reason (shown to user when appropriate)
     */
    public DuplicateRecordException(String message) {
        super(message);
    }
}
