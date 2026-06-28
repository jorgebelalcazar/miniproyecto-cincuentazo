package com.example.cincuentazo.model.exception;

/**
 * Unchecked exception thrown when an operation is attempted in an invalid
 * game state — for example, trying to play a card before the game has
 * been set up, or after it has already finished.
 * <p>
 * This is an <em>unchecked</em> exception (it extends
 * {@link RuntimeException}) because reaching such a state indicates a
 * programming error in the flow of the application, not a normal,
 * recoverable situation. The correct fix is to call the methods in the
 * proper order, not to catch and recover at runtime.
 * </p>
 *
 */
public class GameStateException extends RuntimeException {

    /**
     * Constructs the exception with a descriptive message.
     *
     * @param message the detail message
     */
    public GameStateException(String message) {
        super(message);
    }
}
