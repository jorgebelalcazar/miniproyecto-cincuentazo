package com.example.cincuentazo.model.game;

import com.example.cincuentazo.model.Card;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.NoSuchElementException;

/**
 * Represents the table at the center of a Cincuentazo game.
 * <p>
 * The table holds the stack of played cards and keeps track of the
 * current accumulated sum. Cards are placed on top of one another, which
 * is modeled with a {@link Deque} used as a stack (LIFO): the most
 * recently played card sits on top and is the only one visible/active.
 * </p>
 *
 * <p>The core rule of the game is enforced here: the accumulated sum must
 * never exceed {@value #MAX_SUM}. The table also exposes the cards that
 * can be recycled into the deck when it runs out (every card except the
 * top one).</p>
 *
 */
public class Table {

    /** The maximum allowed value for the table sum. */
    public static final int MAX_SUM = 50;

    /** Stack of played cards; the top is the currently active card. */
    private final Deque<Card> playedCards;

    /** The current accumulated sum on the table. */
    private int currentSum;

    /**
     * Creates an empty table with a starting sum of zero.
     */
    public Table() {
        this.playedCards = new ArrayDeque<>();
        this.currentSum = 0;
    }

    /**
     * Places the initial card on the table and sets the starting sum.
     * <p>
     * The starting sum is the initial card's contribution. For a flexible
     * card (an Ace), the maximum value is used as the starting sum, since
     * the initial card simply establishes the opening total.
     * </p>
     *
     * @param card the initial card; must not be {@code null}
     * @throws IllegalArgumentException if the card is {@code null}
     */
    public void placeInitialCard(Card card) {
        if (card == null) {
            throw new IllegalArgumentException("Initial card must not be null");
        }
        playedCards.push(card);
        currentSum = card.getMaxValue();
    }

    /**
     * Returns the value that would result from playing the given card with
     * a specific chosen value, without modifying the table.
     *
     * @param chosenValue the value the card contributes (relevant for Aces)
     * @return the resulting sum if the card were played
     */
    public int previewSum(int chosenValue) {
        return currentSum + chosenValue;
    }

    /**
     * Checks whether playing the given value would respect the core rule
     * (the resulting sum must not exceed {@value #MAX_SUM}).
     *
     * @param chosenValue the value the card would contribute
     * @return {@code true} if the move would be legal
     */
    public boolean isLegalMove(int chosenValue) {
        return previewSum(chosenValue) <= MAX_SUM;
    }

    /**
     * Plays a card on the table using the given chosen value, updating the
     * accumulated sum. The card is placed on top of the stack.
     *
     * @param card        the card being played; must not be {@code null}
     * @param chosenValue the value the card contributes to the sum
     * @throws IllegalArgumentException if the card is {@code null}
     */
    public void playCard(Card card, int chosenValue) {
        if (card == null) {
            throw new IllegalArgumentException("Played card must not be null");
        }
        playedCards.push(card);
        currentSum += chosenValue;
    }

    /**
     * Returns the card currently on top of the table (the active card).
     *
     * @return the top card
     * @throws NoSuchElementException if the table is empty
     */
    public Card getTopCard() {
        if (playedCards.isEmpty()) {
            throw new NoSuchElementException("The table has no cards");
        }
        return playedCards.peek();
    }

    /**
     * Returns the current accumulated sum on the table.
     *
     * @return the current sum
     */
    public int getCurrentSum() {
        return currentSum;
    }

    /**
     * Removes and returns every card on the table except the top one, so
     * that they can be shuffled back into the deck when it runs out. The
     * accumulated sum is not modified.
     *
     * @return an array-free collection of the recycled cards
     */
    public Deque<Card> collectCardsForRecycling() {
        Deque<Card> recycled = new ArrayDeque<>();
        if (playedCards.size() <= 1) {
            return recycled;
        }
        Card top = playedCards.pop();
        while (!playedCards.isEmpty()) {
            recycled.push(playedCards.pop());
        }
        playedCards.push(top);
        return recycled;
    }

    /**
     * Returns the number of cards currently on the table.
     *
     * @return the number of played cards
     */
    public int size() {
        return playedCards.size();
    }
}
