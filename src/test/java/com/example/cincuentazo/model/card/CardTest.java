package com.example.cincuentazo.model.card;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the {@link Card} class and its value rules.
 *
 */
class CardTest {

    @Test
    @DisplayName("A number card reports its face value as both min and max")
    void numberCardReportsFaceValue() {
        Card five = new Card(Suit.HEARTS, Rank.FIVE);
        assertEquals(5, five.getMinValue());
        assertEquals(5, five.getMaxValue());
        assertFalse(five.hasFlexibleValue());
    }

    @Test
    @DisplayName("The nine neither adds nor subtracts")
    void nineHasZeroValue() {
        Card nine = new Card(Suit.CLUBS, Rank.NINE);
        assertEquals(0, nine.getMinValue());
        assertEquals(0, nine.getMaxValue());
    }

    @Test
    @DisplayName("Face cards J, Q, K subtract ten")
    void faceCardsSubtractTen() {
        assertEquals(-10, new Card(Suit.SPADES, Rank.JACK).getMinValue());
        assertEquals(-10, new Card(Suit.SPADES, Rank.QUEEN).getMinValue());
        assertEquals(-10, new Card(Suit.SPADES, Rank.KING).getMinValue());
    }

    @Test
    @DisplayName("The Ace can be worth either 1 or 10")
    void aceHasFlexibleValue() {
        Card ace = new Card(Suit.DIAMONDS, Rank.ACE);
        assertEquals(1, ace.getMinValue());
        assertEquals(10, ace.getMaxValue());
        assertTrue(ace.hasFlexibleValue());
    }

    @Test
    @DisplayName("Two cards with same suit and rank are equal")
    void cardsWithSameSuitAndRankAreEqual() {
        Card cardA = new Card(Suit.HEARTS, Rank.SEVEN);
        Card cardB = new Card(Suit.HEARTS, Rank.SEVEN);
        assertEquals(cardA, cardB);
        assertEquals(cardA.hashCode(), cardB.hashCode());
    }

    @Test
    @DisplayName("Cards with different rank are not equal")
    void cardsWithDifferentRankAreNotEqual() {
        Card seven = new Card(Suit.HEARTS, Rank.SEVEN);
        Card eight = new Card(Suit.HEARTS, Rank.EIGHT);
        assertNotEquals(seven, eight);
    }

    @Test
    @DisplayName("Creating a card with null suit throws an exception")
    void nullSuitThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Card(null, Rank.ACE));
    }

    @Test
    @DisplayName("Creating a card with null rank throws an exception")
    void nullRankThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Card(Suit.HEARTS, null));
    }
}