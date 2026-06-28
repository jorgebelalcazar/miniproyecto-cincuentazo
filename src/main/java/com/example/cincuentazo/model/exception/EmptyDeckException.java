package com.example.cincuentazo.model.exception;

/**
 * Checked exception thrown when an operation requires drawing a card but
 * the deck is empty and cannot be refilled.
 * <p>
 * This is a custom checked exception (it extends {@link Exception})
 * because running out of cards is a foreseeable, recoverable situation:
 * normally the game logic refills the deck from the table's discarded
 * cards. This exception represents the edge case where even that is not
 * possible.
 * </p>
 *
 */
public class EmptyDeckException extends Exception {

    /**
     * Constructs the exception with a descriptive message.
     *
     * @param message the detail message
     */
    public EmptyDeckException(String message) {
        super(message);
    }
}
