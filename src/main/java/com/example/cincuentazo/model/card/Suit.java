package com.example.cincuentazo.model.card;

/**
 * Represents the four suits of a standard French-suited deck of cards.
 * <p>
 * In the game of Cincuentazo the suit does not affect the numeric value
 * of a card; it is used only to identify cards uniquely and to display
 * them correctly in the user interface.
 * </p>
 *
 */
public enum Suit {

    /** The hearts suit. */
    HEARTS("Hearts"),

    /** The diamonds suit. */
    DIAMONDS("Diamonds"),

    /** The clubs suit. */
    CLUBS("Clubs"),

    /** The spades suit. */
    SPADES("Spades");

    /** Human-readable name of the suit. */
    private final String displayName;

    /**
     * Creates a suit with a human-readable display name.
     *
     * @param displayName the name shown in the user interface
     */
    Suit(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns the human-readable name of this suit.
     *
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }
}
