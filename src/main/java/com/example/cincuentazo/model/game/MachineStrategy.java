package com.example.cincuentazo.model.game;

import com.example.cincuentazo.model.Card;
import com.example.cincuentazo.model.player.Player;

import java.util.Optional;

/**
 * Encapsulates the decision logic a machine player uses to choose which
 * card to play on its turn.
 * <p>
 * The strategy inspects the machine's hand and the current table, and
 * selects a legal card to play. For each candidate card it considers the
 * possible values (including both values of an Ace) and returns the first
 * legal choice found. If no card can be played legally, it returns an
 * empty result, which signals that the player must be eliminated.
 * </p>
 *
 * <p>This class contains pure decision logic with no UI or threading
 * concerns, which makes it easy to unit-test.</p>
 *
 */
public class MachineStrategy {

    /**
     * Represents a chosen move: which card to play and with what value.
     */
    public static class Move {

        /** The card the machine will play. */
        private final Card card;

        /** The value the card will contribute to the table sum. */
        private final int value;

        /**
         * Creates a move.
         *
         * @param card  the card to play
         * @param value the value the card contributes
         */
        public Move(Card card, int value) {
            this.card = card;
            this.value = value;
        }

        /**
         * Returns the card of this move.
         *
         * @return the card
         */
        public Card getCard() {
            return card;
        }

        /**
         * Returns the chosen value of this move.
         *
         * @return the value
         */
        public int getValue() {
            return value;
        }
    }

    /**
     * Chooses a legal move for the given machine player at the current
     * table state.
     *
     * @param player the machine player taking its turn
     * @param table  the current table
     * @return an {@link Optional} containing a legal move, or empty if no
     *         legal move exists
     */
    public Optional<Move> chooseMove(Player player, Table table) {
        for (Card card : player.getHand()) {
            // Try the maximum value first (e.g. Ace as 10), then the minimum
            if (table.isLegalMove(card.getMaxValue())) {
                return Optional.of(new Move(card, card.getMaxValue()));
            }
            if (table.isLegalMove(card.getMinValue())) {
                return Optional.of(new Move(card, card.getMinValue()));
            }
        }
        return Optional.empty();
    }
}