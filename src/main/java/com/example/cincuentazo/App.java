package com.example.cincuentazo;

import com.example.cincuentazo.view.ViewManager;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Entry point of the Cincuentazo application.
 * <p>
 * This class bootstraps the JavaFX runtime and delegates all view loading
 * to the {@link ViewManager}. Following a strict Model-View-Controller
 * design, this class does not build any UI component by hand: it only
 * starts the JavaFX lifecycle and asks the view manager to display the
 * initial screen.
 * </p>
 *
 * @author Jorge Iván Belalcázar
 * @version 1.0.0
 * @since 2026-06
 */
public class App extends Application {

    /**
     * JavaFX entry point. Called automatically once the runtime is ready.
     * <p>
     * Stores the primary stage in the {@link ViewManager} and requests the
     * initial start screen to be displayed. No UI elements are created here.
     * </p>
     *
     * @param primaryStage the primary window provided by the JavaFX runtime
     */
    @Override
    public void start(Stage primaryStage) {
        ViewManager.getInstance().initialize(primaryStage);
        ViewManager.getInstance().showStartView();
    }

    /**
     * Standard Java entry point. Delegates control to the JavaFX runtime
     * through {@link Application#launch(String...)}.
     *
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        launch(args);
    }
}
