package com.example.cincuentazo.controller;

import com.example.cincuentazo.controller.handler.CardClickListener;
import com.example.cincuentazo.controller.handler.CardMouseAdapter;
import com.example.cincuentazo.model.Card;
import com.example.cincuentazo.model.exception.EmptyDeckException;
import com.example.cincuentazo.model.exception.InvalidMoveException;
import com.example.cincuentazo.model.game.GameLogic;
import com.example.cincuentazo.model.player.Player;
import com.example.cincuentazo.view.CardView;
import com.example.cincuentazo.view.ViewManager;
import com.example.cincuentazo.concurrency.MachineTurnService;
import com.example.cincuentazo.model.game.MachineStrategy;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Optional;

/**
 * Controller for the main game view of Cincuentazo.
 * <p>
 * This controller connects the {@link GameLogic} engine with the visual
 * board. It deals the initial hands, renders the cards, and handles the
 * human player's interaction through mouse and keyboard events. Machine
 * turns (with timers) are added in the concurrency phase.
 * </p>
 *
 */
public class GameController implements Initializable, CardClickListener {

    /** Container for the top machine opponent. */
    @FXML
    private Pane topOpponentPane;

    /** Container for the left machine opponent. */
    @FXML
    private Pane leftOpponentPane;

    /** Container for the right machine opponent. */
    @FXML
    private Pane rightOpponentPane;

    /** Stack pane that displays the current top card on the table. */
    @FXML
    private StackPane tableCardPane;

    /** Stack pane that represents the draw deck. */
    @FXML
    private StackPane deckPane;

    /** Label showing the current accumulated sum on the table. */
    @FXML
    private Label sumLabel;

    /** Horizontal box that holds the human player's hand of cards. */
    @FXML
    private HBox humanHandBox;

    /** Label that shows status messages to the player. */
    @FXML
    private Label statusLabel;

    /** Button that returns to the start screen. */
    @FXML
    private Button backButton;

    /** Number of machine opponents selected on the start screen. */
    private int machineOpponents;

    /** The game engine that holds all the rules and state. */
    private GameLogic gameLogic;

    /** The human player. */
    private Player humanPlayer;

    /** The machine players, in display order. */
    private final List<Player> machinePlayers = new ArrayList<>();

    /** Whether the human player has already played a card this turn. */
    private boolean humanHasPlayed;

    /** Service that schedules machine turns on background threads. */
    private final MachineTurnService machineTurnService = new MachineTurnService();

    /** Strategy that decides which card a machine plays. */
    private final MachineStrategy machineStrategy = new MachineStrategy();

    /** Flag to prevent overlapping turn processing. */
    private boolean processingTurn;

    /**
     * Sets the number of machine opponents and starts the game.
     *
     * @param machineOpponents the number of machine opponents (1, 2 or 3)
     */
    public void setMachineOpponents(int machineOpponents) {
        this.machineOpponents = machineOpponents;
        startGame();
    }

    /**
     * Initialization callback. Wires the back button and keyboard handling.
     *
     * @param location  the FXML location (unused)
     * @param resources the resource bundle (unused)
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        backButton.setOnAction(event -> ViewManager.getInstance().showStartView());
    }

    /**
     * Builds the players, sets up the game engine, deals the cards and
     * renders the initial board.
     */
    private void startGame() {
        List<Player> players = new ArrayList<>();
        humanPlayer = new Player("Tu", false);
        players.add(humanPlayer);

        for (int i = 1; i <= machineOpponents; i++) {
            Player machine = new Player("Maquina " + i, true);
            machinePlayers.add(machine);
            players.add(machine);
        }

        gameLogic = new GameLogic(players);
        try {
            gameLogic.setupGame();
        } catch (EmptyDeckException e) {
            statusLabel.setText("Error al repartir las cartas: " + e.getMessage());
            return;
        }

        humanHasPlayed = false;
        installKeyboardHandler();
        refreshBoard();
        statusLabel.setText("Tu turno. Selecciona una carta para jugar.");

    }

    /**
     * Redraws the entire board to reflect the current model state.
     */
    private void refreshBoard() {
        renderTable();
        renderHumanHand();
        renderMachineHands();
        sumLabel.setText(String.valueOf(gameLogic.getTable().getCurrentSum()));
    }

    /**
     * Renders the top card currently on the table.
     */
    private void renderTable() {
        tableCardPane.getChildren().clear();
        Card topCard = gameLogic.getTable().getTopCard();
        tableCardPane.getChildren().add(new CardView(topCard, true));
    }

    /**
     * Renders the human player's hand face-up, attaching a mouse adapter to
     * each card so it can be clicked.
     */
    private void renderHumanHand() {
        humanHandBox.getChildren().clear();
        for (Card card : humanPlayer.getHand()) {
            CardView cardView = new CardView(card, true);
            // Mouse event handling via the Adapter pattern
            cardView.addEventHandler(MouseEvent.MOUSE_CLICKED,
                    new CardMouseAdapter(card, this));
            humanHandBox.getChildren().add(cardView);
        }
    }

    /**
     * Renders the machine players' hands face-down in their panes. Side
     * opponents (left and right) stack their cards vertically to fit the
     * narrow lateral space, while the top opponent stacks them horizontally.
     */
    private void renderMachineHands() {
        Pane[] panes = {leftOpponentPane, topOpponentPane, rightOpponentPane};
        for (int i = 0; i < machinePlayers.size() && i < panes.length; i++) {
            Pane pane = panes[i];
            pane.getChildren().clear();

            boolean isSidePane = (pane == leftOpponentPane || pane == rightOpponentPane);
            int handSize = machinePlayers.get(i).getHandSize();

            if (isSidePane) {
                // Side opponents: stack cards vertically
                javafx.scene.layout.VBox column = new javafx.scene.layout.VBox(5);
                column.setAlignment(javafx.geometry.Pos.CENTER);
                for (int c = 0; c < handSize; c++) {
                    column.getChildren().add(new CardView(null, false));
                }
                pane.getChildren().add(column);
            } else {
                // Top opponent: stack cards horizontally
                HBox row = new HBox(5);
                row.setAlignment(javafx.geometry.Pos.CENTER);
                for (int c = 0; c < handSize; c++) {
                    row.getChildren().add(new CardView(null, false));
                }
                pane.getChildren().add(row);
            }
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Reacts to the human player clicking one of their cards: validates and
     * plays the move, draws a replacement, and then triggers the machine
     * players' turns.
     * </p>
     */
    @Override
    public void onCardClicked(Card card) {
        if (processingTurn) {
            return;
        }
        if (!gameLogic.getCurrentPlayer().equals(humanPlayer)) {
            statusLabel.setText("Espera tu turno.");
            return;
        }

        int chosenValue = chooseCardValue(card);

        try {
            gameLogic.playCard(card, chosenValue);
            gameLogic.drawAndEndTurn();
            refreshBoard();
            statusLabel.setText("Jugaste " + card.getRank().getSymbol()
                    + ". Suma: " + gameLogic.getTable().getCurrentSum());

            // After the human plays, start the machine turns
            processNextTurn();
        } catch (InvalidMoveException e) {
            statusLabel.setText("Movimiento invalido: " + e.getMessage());
        } catch (EmptyDeckException e) {
            statusLabel.setText("No hay cartas para tomar: " + e.getMessage());
        }
    }

    /**
     * Chooses the most convenient value for a card. For an Ace, it picks
     * the highest value that does not exceed the table limit; otherwise it
     * uses the card's fixed value.
     *
     * @param card the card to evaluate
     * @return the chosen value to play the card with
     */
    private int chooseCardValue(Card card) {
        if (!card.hasFlexibleValue()) {
            return card.getMaxValue();
        }
        // Ace: prefer 10 if it fits, otherwise 1
        if (gameLogic.getTable().isLegalMove(card.getMaxValue())) {
            return card.getMaxValue();
        }
        return card.getMinValue();
    }

    /**
     * Installs the keyboard handler on the scene once it is available.
     */
    private void installKeyboardHandler() {
        humanHandBox.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.setOnKeyPressed(new KeyboardHandler());
            }
        });
    }

    // ================================================================
    // INNER CLASS FOR KEYBOARD EVENT HANDLING
    // ================================================================

    /**
     * Processes the next turn. If the game is over, it announces the winner.
     * If it is the human's turn, it waits for the human's click. If it is a
     * machine's turn, it schedules the machine's move on a background thread.
     */
    private void processNextTurn() {
        if (gameLogic.isGameOver()) {
            announceWinner();
            return;
        }

        Player current = gameLogic.getCurrentPlayer();

        if (!current.isMachine()) {
            processingTurn = false;
            statusLabel.setText("Tu turno. Selecciona una carta.");
            return;
        }

        // It is a machine's turn: schedule it on a background thread
        processingTurn = true;
        statusLabel.setText(current.getName() + " esta pensando...");
        machineTurnService.schedulePlay(() -> playMachineTurn(current));
    }

    /**
     * Executes a machine player's turn: chooses a move, plays it (or
     * eliminates the player if no move is possible), draws a card after a
     * short delay, and then advances to the next turn.
     *
     * @param machine the machine player taking its turn
     */
    private void playMachineTurn(Player machine) {
        Optional<MachineStrategy.Move> move =
                machineStrategy.chooseMove(machine, gameLogic.getTable());

        if (move.isEmpty()) {
            // The machine cannot play: eliminate it
            statusLabel.setText(machine.getName() + " no puede jugar y es eliminada.");
            gameLogic.eliminateCurrentPlayer();
            refreshBoard();
            processNextTurn();
            return;
        }

        try {
            gameLogic.playCard(move.get().getCard(), move.get().getValue());
            refreshBoard();
            statusLabel.setText(machine.getName() + " jugo una carta. Suma: "
                    + gameLogic.getTable().getCurrentSum());

            // Schedule drawing a card after 1-2 seconds, then next turn
            machineTurnService.scheduleDraw(() -> {
                try {
                    gameLogic.drawAndEndTurn();
                    refreshBoard();
                    processNextTurn();
                } catch (EmptyDeckException e) {
                    statusLabel.setText("Mazo agotado: " + e.getMessage());
                }
            });
        } catch (InvalidMoveException e) {
            // Should not happen because we validated, but handle defensively
            statusLabel.setText(machine.getName() + " no puede jugar y es eliminada.");
            gameLogic.eliminateCurrentPlayer();
            refreshBoard();
            processNextTurn();
        }
    }

    /**
     * Announces the winner of the game and ends the interaction.
     */
    private void announceWinner() {
        processingTurn = false;
        Player winner = gameLogic.getWinner();
        if (winner.equals(humanPlayer)) {
            statusLabel.setText("Felicidades, ganaste el juego!");
        } else {
            statusLabel.setText("Fin del juego. Gano " + winner.getName() + ".");
        }
    }

    /**
     * Inner class that handles keyboard shortcuts during the game.
     * <p>
     * Implemented as an inner class so it can directly access the
     * controller's state. Currently it supports returning to the start
     * screen with the ESCAPE key.
     * </p>
     */
    private class KeyboardHandler implements javafx.event.EventHandler<KeyEvent> {

        /**
         * Handles a key press event.
         *
         * @param event the key event
         */
        @Override
        public void handle(KeyEvent event) {
            if (event.getCode() == KeyCode.ESCAPE) {
                ViewManager.getInstance().showStartView();
            }
        }
    }
}
