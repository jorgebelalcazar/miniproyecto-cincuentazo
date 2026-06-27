package com.example.cincuentazo.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.RadioButton;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the start view, where the human player chooses how many
 * machine opponents (1, 2 or 3) to play against before starting the game.
 * <p>
 * This controller belongs to the controller layer of the MVC architecture.
 * It reacts to user input on the start screen and, in later phases, will
 * request the transition to the game view through the view manager.
 * </p>
 *
 */
public class StartController implements Initializable {

    /** Radio button to play against one machine opponent. */
    @FXML
    private RadioButton oneOpponentRadio;

    /** Radio button to play against two machine opponents. */
    @FXML
    private RadioButton twoOpponentsRadio;

    /** Radio button to play against three machine opponents. */
    @FXML
    private RadioButton threeOpponentsRadio;

    /** Button that starts the game with the selected number of opponents. */
    @FXML
    private Button startButton;

    /** Label used to display status or feedback messages to the player. */
    @FXML
    private Label statusLabel;

    /** Toggle group that ensures only one opponent option is selected. */
    private ToggleGroup opponentsToggleGroup;

    /**
     * Initialization callback invoked automatically after the FXML view has
     * been loaded. Groups the radio buttons together and wires the start
     * button action.
     *
     * @param location  the FXML location (provided by JavaFX, unused here)
     * @param resources the resource bundle (provided by JavaFX, unused here)
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        opponentsToggleGroup = new ToggleGroup();
        oneOpponentRadio.setToggleGroup(opponentsToggleGroup);
        twoOpponentsRadio.setToggleGroup(opponentsToggleGroup);
        threeOpponentsRadio.setToggleGroup(opponentsToggleGroup);

        oneOpponentRadio.setSelected(true);

        startButton.setOnAction(event -> handleStartGame());
    }

    /**
     * Handles the start button action. In this phase it only reports the
     * selected number of opponents. In a later phase it will request the
     * transition to the game view.
     */
    private void handleStartGame() {
        int selectedOpponents = getSelectedOpponents();
        statusLabel.setText(
                "Juego configurado para " + selectedOpponents
                        + " jugador(es) maquina. "
        );
    }

    /**
     * Returns the number of machine opponents selected by the player.
     *
     * @return the number of opponents (1, 2 or 3)
     */
    private int getSelectedOpponents() {
        if (twoOpponentsRadio.isSelected()) {
            return 2;
        }
        if (threeOpponentsRadio.isSelected()) {
            return 3;
        }
        return 1;
    }
}