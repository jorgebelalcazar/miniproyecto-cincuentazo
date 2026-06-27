package com.example.cincuentazo.model.player;

import com.example.cincuentazo.model.Card;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Represents a player in a game of Cincuentazo, either the human player or
 * a machine opponent.
 * <p>
 * Each player holds a hand of cards. The hand is modeled with a
 * {@link Set} because every card in a standard deck is unique, so
 * duplicates are impossible by design. A {@link LinkedHashSet} is used to
 * preserve the insertion order, which keeps the visual arrangement of the
 * cards stable in the user interface.
 * </p>
 *
 * <p>A player can be marked as eliminated when they can no longer play any
 * card without breaking the core rule of the game.</p>
 *
 */
public class Player {

    /** The maximum number of cards a player holds in hand. */
    public static final int HAND_SIZE = 4;

    /** The player's display name. */
    private final String name;

    /** Whether this player is controlled by the machine. */
    private final boolean machine;

    /** The player's hand of cards (unique cards, insertion-ordered). */
    private final Set<Card> hand;

    /** Whether this player has been eliminated from the game. */
    private boolean eliminated;

    /**
     * Creates a new player with the given name and type.
     *
     * @param name    the player's display name; must not be {@code null}
     * @param machine {@code true} if controlled by the machine,
     *                {@code false} if it is the human player
     * @throws IllegalArgumentException if the name is {@code null} or blank
     */
    public Player(String name, boolean machine) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Player name must not be blank");
        }
        this.name = name;
        this.machine = machine;
        this.hand = new LinkedHashSet<>();
        this.eliminated = false;
    }

    /**
     * Adds a card to the player's hand.
     *
     * @param card the card to add; must not be {@code null}
     * @throws IllegalArgumentException if the card is {@code null}
     */
    public void addCard(Card card) {
        if (card == null) {
            throw new IllegalArgumentException("Cannot add a null card");
        }
        hand.add(card);
    }

    /**
     * Removes a card from the player's hand.
     *
     * @param card the card to remove
     * @return {@code true} if the card was in the hand and was removed
     */
    public boolean removeCard(Card card) {
        return hand.remove(card);
    }

    /**
     * Returns an unmodifiable view of the player's hand, preventing
     * external code from altering the hand directly.
     *
     * @return an unmodifiable set of the player's cards
     */
    public Set<Card> getHand() {
        return Collections.unmodifiableSet(hand);
    }

    /**
     * Returns the number of cards currently in the player's hand.
     *
     * @return the hand size
     */
    public int getHandSize() {
        return hand.size();
    }

    /**
     * Returns the player's display name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Indicates whether this player is controlled by the machine.
     *
     * @return {@code true} if it is a machine player
     */
    public boolean isMachine() {
        return machine;
    }

    /**
     * Indicates whether this player has been eliminated.
     *
     * @return {@code true} if the player is eliminated
     */
    public boolean isEliminated() {
        return eliminated;
    }

    /**
     * Marks this player as eliminated.
     */
    public void eliminate() {
        this.eliminated = true;
    }
}