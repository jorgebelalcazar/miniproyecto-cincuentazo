package com.example.cincuentazo.model.game;

import com.example.cincuentazo.model.Card;
import com.example.cincuentazo.model.Rank;
import com.example.cincuentazo.model.Suit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the {@link Table} class and its sum/rule logic.
 */
class TableTest {

    private Table table;

    @BeforeEach
    void setUp() {
        table = new Table();
    }

    @Test
    @DisplayName("Placing the initial card sets the starting sum")
    void initialCardSetsSum() {
        table.placeInitialCard(new Card(Suit.HEARTS, Rank.FIVE));
        assertEquals(5, table.getCurrentSum());
    }

    @Test
    @DisplayName("Playing a card updates the accumulated sum")
    void playingCardUpdatesSum() {
        table.placeInitialCard(new Card(Suit.HEARTS, Rank.FIVE));
        table.playCard(new Card(Suit.CLUBS, Rank.SEVEN), 7);
        assertEquals(12, table.getCurrentSum());
    }

    @Test
    @DisplayName("A move is legal when the resulting sum is 50 or less")
    void moveIsLegalUpToFifty() {
        table.placeInitialCard(new Card(Suit.HEARTS, Rank.TEN));
        assertTrue(table.isLegalMove(40));   // 10 + 40 = 50, legal
    }

    @Test
    @DisplayName("A move is illegal when the resulting sum exceeds 50")
    void moveIsIllegalOverFifty() {
        table.placeInitialCard(new Card(Suit.HEARTS, Rank.TEN));
        assertFalse(table.isLegalMove(41));  // 10 + 41 = 51, illegal
    }

    @Test
    @DisplayName("The top card is the most recently played card")
    void topCardIsMostRecent() {
        table.placeInitialCard(new Card(Suit.HEARTS, Rank.FIVE));
        Card seven = new Card(Suit.CLUBS, Rank.SEVEN);
        table.playCard(seven, 7);
        assertEquals(seven, table.getTopCard());
    }

    @Test
    @DisplayName("Face cards reduce the table sum")
    void faceCardReducesSum() {
        table.placeInitialCard(new Card(Suit.HEARTS, Rank.TEN));
        table.playCard(new Card(Suit.SPADES, Rank.KING), -10);
        assertEquals(0, table.getCurrentSum());
    }
}