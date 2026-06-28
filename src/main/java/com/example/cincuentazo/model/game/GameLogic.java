package com.example.cincuentazo.model.game;

import com.example.cincuentazo.model.Card;
import com.example.cincuentazo.model.Deck;
import com.example.cincuentazo.model.exception.EmptyDeckException;
import com.example.cincuentazo.model.exception.GameStateException;
import com.example.cincuentazo.model.exception.InvalidMoveException;
import com.example.cincuentazo.model.player.Player;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Queue;

/**
 * Central game engine that orchestrates a full game of Cincuentazo.
 * <p>
 * This class coordinates the deck, the table and the players, enforcing
 * all the rules of the game: dealing the initial hands, validating and
 * applying moves, drawing replacement cards, eliminating players who
 * cannot move, recycling cards and detecting the winner.
 * </p>
 *
 * <p>The turn order is modeled with a {@link Queue} of players: the player
 * whose turn it is sits at the front of the queue and, after playing, is
 * moved to the back, which naturally produces the circular turn rotation
 * the game requires.</p>
 *
 * <p>This class contains pure game logic and is completely independent of
 * JavaFX, which makes it straightforward to unit-test.</p>
 *
 */
public class GameLogic {

    /** The deck of cards. */
    private final Deck deck;

    /** The table holding the played cards and the running sum. */
    private final Table table;

    /** Queue of active players defining the turn order. */
    private final Queue<Player> turnOrder;

    /** Whether the game has been set up and is ready to play. */
    private boolean started;

    /**
     * Creates a new game engine with the given players.
     *
     * @param players the participating players (human + machines); must
     *                contain at least two players
     * @throws IllegalArgumentException if there are fewer than two players
     */
    public GameLogic(List<Player> players) {
        if (players == null || players.size() < 2) {
            throw new IllegalArgumentException(
                    "A game requires at least two players"
            );
        }
        this.deck = new Deck();
        this.table = new Table();
        this.turnOrder = new ArrayDeque<>(players);
        this.started = false;
    }

    /**
     * Sets up the game: deals {@link Player#HAND_SIZE} cards to each player
     * and places the initial card on the table (HU-2).
     *
     * @throws EmptyDeckException if the deck runs out while dealing
     */
    public void setupGame() throws EmptyDeckException {
        for (Player player : turnOrder) {
            for (int i = 0; i < Player.HAND_SIZE; i++) {
                player.addCard(drawFromDeck());
            }
        }
        table.placeInitialCard(drawFromDeck());
        started = true;
    }

    /**
     * Returns the player whose turn it currently is, without removing them
     * from the queue.
     *
     * @return the current player
     * @throws GameStateException if the game has not been set up
     */
    public Player getCurrentPlayer() {
        ensureStarted();
        Player current = turnOrder.peek();
        if (current == null) {
            throw new GameStateException("There are no players left in the game");
        }
        return current;
    }

    /**
     * Attempts to play the given card for the current player using the
     * specified value, enforcing the core rule of the game (HU-3).
     *
     * @param card        the card to play; must belong to the current player
     * @param chosenValue the value the card contributes (relevant for Aces)
     * @throws InvalidMoveException if the move would make the sum exceed 50
     * @throws GameStateException   if the game has not been set up
     * @throws IllegalArgumentException if the card is not in the player's hand
     */
    public void playCard(Card card, int chosenValue) throws InvalidMoveException {
        ensureStarted();
        Player current = getCurrentPlayer();

        if (!current.getHand().contains(card)) {
            throw new IllegalArgumentException(
                    "The card is not in the current player's hand"
            );
        }
        if (!table.isLegalMove(chosenValue)) {
            throw new InvalidMoveException(
                    "Playing this card would make the table sum exceed "
                            + Table.MAX_SUM
            );
        }

        current.removeCard(card);
        table.playCard(card, chosenValue);
    }

    /**
     * Draws a replacement card from the deck for the current player and
     * ends their turn, moving them to the back of the turn queue (HU-4).
     *
     * @throws EmptyDeckException if the deck is empty and cannot be refilled
     * @throws GameStateException if the game has not been set up
     */
    public void drawAndEndTurn() throws EmptyDeckException {
        ensureStarted();
        Player current = getCurrentPlayer();
        current.addCard(drawFromDeck());

        // Move the current player to the back of the queue (circular turn)
        turnOrder.poll();
        turnOrder.offer(current);
    }

    /**
     * Determines whether the given player has at least one card they could
     * legally play given the current table sum.
     *
     * @param player the player to check
     * @return {@code true} if the player can make at least one legal move
     */
    public boolean canPlayerMove(Player player) {
        for (Card card : player.getHand()) {
            if (cardHasLegalValue(card)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Eliminates the current player from the game, returning their cards to
     * the bottom of the deck, and advances to the next player (HU-5).
     *
     * @throws GameStateException if the game has not been set up
     */
    public void eliminateCurrentPlayer() {
        ensureStarted();
        Player current = turnOrder.poll();
        if (current == null) {
            throw new GameStateException("No player to eliminate");
        }
        current.eliminate();
        for (Card card : current.getHand()) {
            deck.addToBottom(card);
        }
    }

    /**
     * Checks whether the game is over, which happens when only one player
     * remains in the turn queue (HU-6).
     *
     * @return {@code true} if only one player is left
     */
    public boolean isGameOver() {
        return turnOrder.size() <= 1;
    }

    /**
     * Returns the winner of the game: the single remaining player.
     *
     * @return the winning player
     * @throws GameStateException if the game is not yet over
     */
    public Player getWinner() {
        if (!isGameOver()) {
            throw new GameStateException(
                    "The game is not over yet; there is no winner"
            );
        }
        return turnOrder.peek();
    }

    /**
     * Returns the table associated with this game.
     *
     * @return the game table
     */
    public Table getTable() {
        return table;
    }

    /**
     * Returns the number of players still active in the game.
     *
     * @return the number of remaining players
     */
    public int getRemainingPlayers() {
        return turnOrder.size();
    }

    // ================================================================
    // PRIVATE HELPER METHODS
    // ================================================================

    /**
     * Draws a card from the deck, refilling it from the table's recycled
     * cards if it is empty.
     *
     * @return the drawn card
     * @throws EmptyDeckException if the deck is empty and cannot be refilled
     */
    private Card drawFromDeck() throws EmptyDeckException {
        if (deck.isEmpty()) {
            refillDeckFromTable();
        }
        if (deck.isEmpty()) {
            throw new EmptyDeckException(
                    "The deck is empty and cannot be refilled"
            );
        }
        return deck.draw();
    }

    /**
     * Refills the deck by collecting the table's cards (except the top one),
     * shuffling them and returning them to the deck.
     */
    private void refillDeckFromTable() {
        Deque<Card> recycled = table.collectCardsForRecycling();
        List<Card> shuffleBuffer = new ArrayList<>(recycled);
        Collections.shuffle(shuffleBuffer);
        for (Card card : shuffleBuffer) {
            deck.addToBottom(card);
        }
    }

    /**
     * Checks whether a card has at least one value that would be a legal
     * move at the current table sum.
     *
     * @param card the card to check
     * @return {@code true} if any of the card's possible values is legal
     */
    private boolean cardHasLegalValue(Card card) {
        if (table.isLegalMove(card.getMinValue())) {
            return true;
        }
        return table.isLegalMove(card.getMaxValue());
    }

    /**
     * Ensures the game has been set up before performing an operation.
     *
     * @throws GameStateException if the game has not been started
     */
    private void ensureStarted() {
        if (!started) {
            throw new GameStateException(
                    "The game has not been set up. Call setupGame() first."
            );
        }
    }
}
