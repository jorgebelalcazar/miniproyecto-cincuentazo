package com.example.cincuentazo.concurrency;

import javafx.application.Platform;
import javafx.concurrent.Task;

import java.util.Random;

/**
 * Service that schedules the timed actions of a machine player using
 * background threads, so the user interface never freezes.
 * <p>
 * The game rules require a machine to take between 2 and 4 seconds to play
 * a card, and between 1 and 2 seconds to draw a card. These waits are
 * executed on separate threads by means of JavaFX {@link Task}s. When a
 * task finishes, the provided callback is run safely on the JavaFX
 * Application Thread through {@link Platform#runLater}.
 * </p>
 *
 * <p>This class is responsible only for the timing/concurrency aspect; the
 * actual game decisions are passed in as callbacks by the controller.</p>
 *
 */
public class MachineTurnService {

    /** Minimum milliseconds a machine waits before playing a card. */
    private static final int MIN_PLAY_DELAY_MS = 2000;

    /** Maximum extra milliseconds (random) before playing a card. */
    private static final int EXTRA_PLAY_DELAY_MS = 2000; // 2000..4000 total

    /** Minimum milliseconds a machine waits before drawing a card. */
    private static final int MIN_DRAW_DELAY_MS = 1000;

    /** Maximum extra milliseconds (random) before drawing a card. */
    private static final int EXTRA_DRAW_DELAY_MS = 1000; // 1000..2000 total

    /** Random source for the variable delays. */
    private final Random random = new Random();

    /**
     * Schedules the "play a card" action of a machine after a random delay
     * of 2 to 4 seconds. The action runs on the JavaFX Application Thread.
     *
     * @param onPlay the action to execute once the delay elapses
     */
    public void schedulePlay(Runnable onPlay) {
        int delay = MIN_PLAY_DELAY_MS + random.nextInt(EXTRA_PLAY_DELAY_MS + 1);
        runAfterDelay(delay, onPlay);
    }

    /**
     * Schedules the "draw a card" action of a machine after a random delay
     * of 1 to 2 seconds. The action runs on the JavaFX Application Thread.
     *
     * @param onDraw the action to execute once the delay elapses
     */
    public void scheduleDraw(Runnable onDraw) {
        int delay = MIN_DRAW_DELAY_MS + random.nextInt(EXTRA_DRAW_DELAY_MS + 1);
        runAfterDelay(delay, onDraw);
    }

    /**
     * Runs the given action after the specified delay on a background
     * thread, then dispatches the action to the JavaFX Application Thread.
     *
     * @param delayMs the delay in milliseconds
     * @param action  the action to run after the delay
     */
    private void runAfterDelay(int delayMs, Runnable action) {
        Task<Void> waitTask = new Task<>() {
            @Override
            protected Void call() throws InterruptedException {
                Thread.sleep(delayMs);
                return null;
            }
        };

        waitTask.setOnSucceeded(event -> Platform.runLater(action));

        Thread thread = new Thread(waitTask);
        thread.setDaemon(true);
        thread.start();
    }
}