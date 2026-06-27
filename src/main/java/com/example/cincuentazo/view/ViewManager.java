package com.example.cincuentazo.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 * Centralized manager responsible for loading FXML views and swapping
 * scenes on the primary stage.
 * <p>
 * This class is the single point of contact with the JavaFX {@link Stage}.
 * No controller, model or other view ever touches the stage directly;
 * instead, they request screen changes through this manager. This keeps
 * the navigation logic in one place and guarantees that the stage is
 * always loaded and displayed consistently.
 * </p>
 *
 * <p>The class follows the Singleton pattern: a single shared instance
 * coordinates all view transitions throughout the application's lifetime.</p>
 *
 */
public final class ViewManager {

    /** Title shown in the application window. */
    private static final String WINDOW_TITLE = "Cincuentazo - Universidad del Valle";

    /** Classpath location of the start (player selection) view. */
    private static final String START_VIEW_PATH =
            "/com/example/cincuentazo/view/start-view.fxml";

    /** The single shared instance of this manager. */
    private static final ViewManager INSTANCE = new ViewManager();

    /** The primary stage provided by the JavaFX runtime. */
    private Stage primaryStage;

    /**
     * Private constructor to enforce the Singleton pattern.
     */
    private ViewManager() {
    }

    /**
     * Returns the single shared instance of the view manager.
     *
     * @return the shared {@code ViewManager} instance
     */
    public static ViewManager getInstance() {
        return INSTANCE;
    }

    /**
     * Stores the primary stage and applies the base window configuration.
     * Must be called once, from the application's {@code start} method,
     * before requesting any view.
     *
     * @param stage the primary stage provided by JavaFX
     */
    public void initialize(Stage stage) {
        this.primaryStage = stage;
        this.primaryStage.setTitle(WINDOW_TITLE);
        this.primaryStage.setResizable(false);
    }

    /**
     * Loads and displays the start view, where the player selects how many
     * machine opponents to play against.
     */
    public void showStartView() {
        swapScene(START_VIEW_PATH);
    }

    /**
     * Loads the FXML located at the given classpath path and sets it as the
     * current scene on the primary stage.
     * <p>
     * This method performs a robust lookup of the FXML resource and fails
     * fast with a descriptive message if the file cannot be found, which
     * makes configuration problems easy to diagnose.
     * </p>
     *
     * @param fxmlPath the absolute classpath path of the FXML to load
     * @throws IllegalStateException if the FXML resource cannot be located
     *                               or loaded
     */
    private void swapScene(String fxmlPath) {
        URL resourceUrl = ViewManager.class.getResource(fxmlPath);
        if (resourceUrl == null) {
            throw new IllegalStateException(
                    "No se pudo encontrar el archivo FXML en: " + fxmlPath
            );
        }

        try {
            FXMLLoader loader = new FXMLLoader(resourceUrl);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.centerOnScreen();
            primaryStage.show();
        } catch (IOException e) {
            throw new IllegalStateException(
                    "No se pudo cargar la vista: " + fxmlPath, e
            );
        }
    }
}
