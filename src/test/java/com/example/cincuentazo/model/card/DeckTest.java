package com.example.cincuentazo.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the {@link Deck} class and its queue-based behaviour.
 *
 */
class DeckTest {

    private Deck deck;

    @BeforeEach
    void setUp() {
        deck = new Deck();
    }

    @Test
    @DisplayName("A new deck contains the full 52 cards")
    void newDeckHasFiftyTwoCards() {
        assertEquals(52, deck.size());
        assertFalse(deck.isEmpty());
    }

    @Test
    @DisplayName("Drawing a card reduces the deck size by one")
    void drawingReducesSize() {
        int initialSize = deck.size();
        Card drawn = deck.draw();
        assertEquals(initialSize - 1, deck.size());
        assertTrue(drawn != null);
    }

    @Test
    @DisplayName("Adding a card to the bottom increases the deck size")
    void addingToBottomIncreasesSize() {
        int initialSize = deck.size();
        deck.addToBottom(new Card(Suit.HEARTS, Rank.ACE));
        assertEquals(initialSize + 1, deck.size());
    }

    @Test
    @DisplayName("Drawing all cards empties the deck")
    void drawingAllEmptiesDeck() {
        while (!deck.isEmpty()) {
            deck.draw();
        }
        assertTrue(deck.isEmpty());
        assertEquals(0, deck.size());
    }

    @Test
    @DisplayName("Drawing from an empty deck throws an exception")
    void drawingFromEmptyDeckThrows() {
        while (!deck.isEmpty()) {
            deck.draw();
        }
        assertThrows(NoSuchElementException.class, () -> deck.draw());
    }

    @Test
    @DisplayName("Adding a null card to the bottom throws an exception")
    void addingNullCardThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> deck.addToBottom(null));
    }
}
