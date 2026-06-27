package com.example.cincuentazo.model.card;

/**
 * Represents the rank of a card and its associated value rules in the
 * game of Cincuentazo.
 * <p>
 * Each rank carries the contribution it makes to the table sum according
 * to the game rules:
 * </p>
 * <ul>
 *   <li>Ranks 2 to 8 and 10 add their face number.</li>
 *   <li>Rank 9 neither adds nor subtracts (value 0).</li>
 *   <li>Ranks J, Q and K subtract 10.</li>
 *   <li>Rank A (Ace) can add either 1 or 10, whichever is more convenient.</li>
 * </ul>
 *
 * <p>Because the Ace is the only rank with a flexible value, it is modeled
 * with both a minimum and a maximum value. All other ranks have the same
 * minimum and maximum value.</p>
 *
 */
public enum Rank {

    /** Ace: contributes either 1 or 10, whichever is more convenient. */
    ACE("A", 1, 10),

    /** Two: adds 2. */
    TWO("2", 2, 2),

    /** Three: adds 3. */
    THREE("3", 3, 3),

    /** Four: adds 4. */
    FOUR("4", 4, 4),

    /** Five: adds 5. */
    FIVE("5", 5, 5),

    /** Six: adds 6. */
    SIX("6", 6, 6),

    /** Seven: adds 7. */
    SEVEN("7", 7, 7),

    /** Eight: adds 8. */
    EIGHT("8", 8, 8),

    /** Nine: neither adds nor subtracts. */
    NINE("9", 0, 0),

    /** Ten: adds 10. */
    TEN("10", 10, 10),

    /** Jack: subtracts 10. */
    JACK("J", -10, -10),

    /** Queen: subtracts 10. */
    QUEEN("Q", -10, -10),

    /** King: subtracts 10. */
    KING("K", -10, -10);

    /** Symbol displayed on the card (e.g. "A", "10", "K"). */
    private final String symbol;

    /** Minimum value this rank contributes to the table sum. */
    private final int minValue;

    /** Maximum value this rank contributes to the table sum. */
    private final int maxValue;

    /**
     * Creates a rank with its display symbol and value range.
     *
     * @param symbol   the symbol shown on the card
     * @param minValue the minimum value this rank contributes
     * @param maxValue the maximum value this rank contributes
     */
    Rank(String symbol, int minValue, int maxValue) {
        this.symbol = symbol;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    /**
     * Returns the symbol displayed on the card.
     *
     * @return the card symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Returns the minimum value this rank contributes to the table sum.
     * <p>
     * For every rank except the Ace, this equals the maximum value.
     * </p>
     *
     * @return the minimum contributed value
     */
    public int getMinValue() {
        return minValue;
    }

    /**
     * Returns the maximum value this rank contributes to the table sum.
     * <p>
     * For every rank except the Ace, this equals the minimum value.
     * </p>
     *
     * @return the maximum contributed value
     */
    public int getMaxValue() {
        return maxValue;
    }

    /**
     * Indicates whether this rank has a flexible value (only the Ace).
     *
     * @return {@code true} if the rank can take more than one value
     */
    public boolean hasFlexibleValue() {
        return minValue != maxValue;
    }
}
