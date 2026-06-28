package com.example.cincuentazo.model.game;

import com.example.cincuentazo.model.Card;
import com.example.cincuentazo.model.Rank;
import com.example.cincuentazo.model.Suit;
import com.example.cincuentazo.model.exception.EmptyDeckException;
import com.example.cincuentazo.model.exception.GameStateException;
import com.example.cincuentazo.model.exception.InvalidMoveException;
import com.example.cincuentazo.model.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the {@link GameLogic} engine.
 */
class GameLogicTest {

    private GameLogic game;
    private List<Player> players;

    @BeforeEach
    void setUp() {
        players = new ArrayList<>();
        players.add(new Player("Human", false));
        players.add(new Player("Machine 1", true));
    }

    @Test
    @DisplayName("Creating a game with fewer than two players throws an exception")
    void tooFewPlayersThrows() {
        List<Player> single = new ArrayList<>();
        single.add(new Player("Solo", false));
        assertThrows(IllegalArgumentException.class,
                () -> new GameLogic(single));
    }

    @Test
    @DisplayName("After setup, every player has four cards")
    void setupDealsFourCards() throws EmptyDeckException {
        game = new GameLogic(players);
        game.setupGame();
        for (Player player : players) {
            assertEquals(Player.HAND_SIZE, player.getHandSize());
        }
    }

    @Test
    @DisplayName("Playing before setup throws an unchecked GameStateException")
    void playingBeforeSetupThrows() {
        game = new GameLogic(players);
        assertThrows(GameStateException.class,
                () -> game.getCurrentPlayer());
    }

    @Test
    @DisplayName("The game is over when only one player remains")
    void gameOverWithOnePlayer() throws EmptyDeckException {
        game = new GameLogic(players);
        game.setupGame();
        assertFalse(game.isGameOver());
        game.eliminateCurrentPlayer();
        assertTrue(game.isGameOver());
    }

    @Test
    @DisplayName("The winner is the last remaining player")
    void winnerIsLastPlayer() throws EmptyDeckException {
        game = new GameLogic(players);
        game.setupGame();
        game.eliminateCurrentPlayer();
        assertEquals(1, game.getRemainingPlayers());
        assertFalse(game.getWinner().isEliminated());
    }

    @Test
    @DisplayName("Asking for a winner before the game is over throws")
    void winnerBeforeGameOverThrows() throws EmptyDeckException {
        game = new GameLogic(players);
        game.setupGame();
        assertThrows(GameStateException.class,
                () -> game.getWinner());
    }

    @Test
    @DisplayName("Playing a card not in hand throws an exception")
    void playingForeignCardThrows() throws EmptyDeckException {
        game = new GameLogic(players);
        game.setupGame();
        // A card very unlikely to be in hand after controlled setup is hard
        // to guarantee, so we validate the rule path with a fresh card check.
        Card foreign = new Card(Suit.SPADES, Rank.KING);
        Player current = game.getCurrentPlayer();
        if (!current.getHand().contains(foreign)) {
            assertThrows(IllegalArgumentException.class,
                    () -> game.playCard(foreign, foreign.getMaxValue()));
        }
    }
}