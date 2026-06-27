package com.example.cincuentazo.model.card;

import java.util.Objects;

/**
 * Represents a single immutable playing card, defined by its suit and rank.
 * <p>
 * A card knows how to report the value it contributes to the table sum
 * according to the rules of Cincuentazo. Since the Ace can contribute
 * either 1 or 10, the card exposes both its minimum and maximum value.
 * The decision of which value to use is left to the game logic.
 * </p>
 *
 * <p>Cards are immutable: once created, their suit and rank never change.
 * This makes them safe to store in any data structure and to share
 * between the deck, the players' hands and the table.</p>
 *
 */
public final class Card {

    /** The suit of this card. */
    private final Suit suit;

    /** The rank of this card. */
    private final Rank rank;

    /**
     * Creates a new card with the given suit and rank.
     *
     * @param suit the suit of the card; must not be {@code null}
     * @param rank the rank of the card; must not be {@code null}
     * @throws IllegalArgumentException if suit or rank are {@code null}
     */
    public Card(Suit suit, Rank rank) {
        if (suit == null || rank == null) {
            throw new IllegalArgumentException(
                    "Suit and rank must not be null"
            );
        }
        this.suit = suit;
        this.rank = rank;
    }

    /**
     * Returns the suit of this card.
     *
     * @return the card suit
     */
    public Suit getSuit() {
        return suit;
    }

    /**
     * Returns the rank of this card.
     *
     * @return the card rank
     */
    public Rank getRank() {
        return rank;
    }

    /**
     * Returns the minimum value this card contributes to the table sum.
     *
     * @return the minimum value
     */
    public int getMinValue() {
        return rank.getMinValue();
    }

    /**
     * Returns the maximum value this card contributes to the table sum.
     *
     * @return the maximum value
     */
    public int getMaxValue() {
        return rank.getMaxValue();
    }

    /**
     * Indicates whether this card has a flexible value (only Aces do).
     *
     * @return {@code true} if the card can take more than one value
     */
    public boolean hasFlexibleValue() {
        return rank.hasFlexibleValue();
    }

    /**
     * Compares this card with another object for equality. Two cards are
     * equal if they share the same suit and rank.
     *
     * @param other the object to compare against
     * @return {@code true} if both cards have the same suit and rank
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        Card card = (Card) other;
        return suit == card.suit && rank == card.rank;
    }

    /**
     * Returns a hash code consistent with {@link #equals(Object)}.
     *
     * @return the hash code of this card
     */
    @Override
    public int hashCode() {
        return Objects.hash(suit, rank);
    }

    /**
     * Returns a human-readable representation of this card, useful for
     * debugging and console output.
     *
     * @return a string describing the card
     */
    @Override
    public String toString() {
        return rank.getSymbol() + " of " + suit.getDisplayName();
    }
}
