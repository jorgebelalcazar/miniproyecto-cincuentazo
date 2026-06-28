package com.example.cincuentazo.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the main game view of Cincuentazo.
 * <p>
 * This controller wires the game board defined in {@code game-view.fxml}
 * with the game logic. In this phase it only holds the FXML references and
 * displays a placeholder state; the full interaction (dealing cards,
 * handling clicks, machine turns) is added in later phases.
 * </p>
 *
 */
public class GameController implements Initializable {

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

    /**
     * Sets the number of machine opponents chosen by the player on the
     * start screen. Called by the {@code ViewManager} right after loading
     * this view.
     *
     * @param machineOpponents the number of machine opponents (1, 2 or 3)
     */
    public void setMachineOpponents(int machineOpponents) {
        this.machineOpponents = machineOpponents;
        statusLabel.setText(
                "Juego iniciado con " + machineOpponents
                        + " maquina(s). "
        );
    }

    /**
     * Initialization callback invoked automatically after the FXML view has
     * been loaded. Wires the back button. Full board setup happens once the
     * number of opponents is known.
     *
     * @param location  the FXML location (provided by JavaFX, unused here)
     * @param resources the resource bundle (provided by JavaFX, unused here)
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        backButton.setOnAction(event -> handleBackToStart());
        sumLabel.setText("0");
        statusLabel.setText("Preparando la mesa...");
    }

    /**
     * Handles the back button by returning to the start screen.
     */
    private void handleBackToStart() {
        com.example.cincuentazo.view.ViewManager.getInstance().showStartView();
    }
}
