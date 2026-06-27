package com.example.cincuentazo.model.card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.LinkedList;

/**
 * Represents the deck of cards used in a game of Cincuentazo.
 * <p>
 * The deck is backed by a {@link Queue}, which models the natural
 * behaviour required by the game: cards are drawn from the front of the
 * queue, while cards returned to the deck (for example, from an eliminated
 * player) are placed at the back.
 * </p>
 *
 * <p>A standard 52-card deck is built on creation and shuffled. When the
 * deck runs out of cards, it can be refilled from the table's discarded
 * cards (handled by the game logic in a later phase).</p>
 *
 */
public class Deck {

    /** Queue holding the cards of the deck (front = next to be drawn). */
    private final Queue<Card> cards;

    /**
     * Builds a full, shuffled 52-card deck.
     */
    public Deck() {
        this.cards = new LinkedList<>();
        buildFullDeck();
        shuffle();
    }

    /**
     * Populates the deck with all 52 combinations of suit and rank.
     */
    private void buildFullDeck() {
        List<Card> temporaryList = new ArrayList<>();
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                temporaryList.add(new Card(suit, rank));
            }
        }
        cards.addAll(temporaryList);
    }

    /**
     * Randomly shuffles the cards currently in the deck.
     * <p>
     * Because {@link Collections#shuffle} works on lists, the cards are
     * temporarily copied to a list, shuffled, and then placed back into
     * the queue. This list is a transient utility and not part of the
     * deck's persistent structure.
     * </p>
     */
    public void shuffle() {
        List<Card> temporaryList = new ArrayList<>(cards);
        Collections.shuffle(temporaryList);
        cards.clear();
        cards.addAll(temporaryList);
    }

    /**
     * Draws and removes the card at the front of the deck.
     *
     * @return the card drawn from the top of the deck
     * @throws NoSuchElementException if the deck is empty
     */
    public Card draw() {
        if (cards.isEmpty()) {
            throw new NoSuchElementException(
                    "Cannot draw from an empty deck"
            );
        }
        return cards.poll();
    }

    /**
     * Adds a card to the back of the deck. Used when an eliminated player's
     * cards are returned to the deck.
     *
     * @param card the card to add; must not be {@code null}
     * @throws IllegalArgumentException if the card is {@code null}
     */
    public void addToBottom(Card card) {
        if (card == null) {
            throw new IllegalArgumentException("Cannot add a null card");
        }
        cards.offer(card);
    }

    /**
     * Returns the number of cards currently in the deck.
     *
     * @return the deck size
     */
    public int size() {
        return cards.size();
    }

    /**
     * Checks whether the deck has no cards left.
     *
     * @return {@code true} if the deck is empty
     */
    public boolean isEmpty() {
        return cards.isEmpty();
    }
}
