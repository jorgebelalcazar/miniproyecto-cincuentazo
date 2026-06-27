package com.example.cincuentazo.model.player;

import com.example.cincuentazo.model.Card;
import com.example.cincuentazo.model.Rank;
import com.example.cincuentazo.model.Suit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the {@link Player} class.
 *
 */
class PlayerTest {

    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player("Test Player", false);
    }

    @Test
    @DisplayName("A new player starts with an empty hand and is not eliminated")
    void newPlayerStartsEmpty() {
        assertEquals(0, player.getHandSize());
        assertFalse(player.isEliminated());
    }

    @Test
    @DisplayName("Adding cards increases the hand size")
    void addingCardsIncreasesHand() {
        player.addCard(new Card(Suit.HEARTS, Rank.FIVE));
        player.addCard(new Card(Suit.CLUBS, Rank.NINE));
        assertEquals(2, player.getHandSize());
    }

    @Test
    @DisplayName("Removing a card decreases the hand size")
    void removingCardDecreasesHand() {
        Card five = new Card(Suit.HEARTS, Rank.FIVE);
        player.addCard(five);
        assertTrue(player.removeCard(five));
        assertEquals(0, player.getHandSize());
    }

    @Test
    @DisplayName("The same card cannot be added twice (Set behavior)")
    void duplicateCardIsIgnored() {
        Card five = new Card(Suit.HEARTS, Rank.FIVE);
        player.addCard(five);
        player.addCard(five);  // duplicate, ignored by the Set
        assertEquals(1, player.getHandSize());
    }

    @Test
    @DisplayName("A player can be marked as eliminated")
    void playerCanBeEliminated() {
        player.eliminate();
        assertTrue(player.isEliminated());
    }

    @Test
    @DisplayName("Creating a player with a blank name throws an exception")
    void blankNameThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Player("  ", false));
    }

    @Test
    @DisplayName("The hand cannot be modified externally")
    void handIsUnmodifiable() {
        player.addCard(new Card(Suit.HEARTS, Rank.FIVE));
        assertThrows(UnsupportedOperationException.class,
                () -> player.getHand().clear());
    }
}