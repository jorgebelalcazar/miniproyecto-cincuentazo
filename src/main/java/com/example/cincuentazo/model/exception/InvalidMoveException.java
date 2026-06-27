package com.example.cincuentazo.model.exception;

/**
 * Checked exception thrown when a player attempts to play a card that
 * would break the core rule of Cincuentazo: making the table sum exceed 50.
 * <p>
 * This is a custom, project-specific exception. It is declared as a
 * <em>checked</em> exception (it extends {@link Exception}) because an
 * invalid move is a foreseeable, recoverable situation that the calling
 * code is expected to handle gracefully — for example, by asking the
 * player to choose a different card.
 * </p>
 *
 */
public class InvalidMoveException extends Exception {

    /**
     * Constructs the exception with a descriptive message.
     *
     * @param message the detail message explaining why the move is invalid
     */
    public InvalidMoveException(String message) {
        super(message);
    }

    /**
     * Constructs the exception with a descriptive message and an underlying
     * cause.
     *
     * @param message the detail message explaining why the move is invalid
     * @param cause   the underlying cause of this exception
     */
    public InvalidMoveException(String message, Throwable cause) {
        super(message, cause);
    }
}
