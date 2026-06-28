package com.example.cincuentazo.controller.handler;

import com.example.cincuentazo.model.Card;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

/**
 * Adapter that bridges low-level JavaFX mouse events to the higher-level
 * {@link CardClickListener} contract.
 * <p>
 * This class implements the generic {@link EventHandler} for
 * {@link MouseEvent} and adapts it to the specific need of reporting a
 * card click. Each adapter instance is bound to a particular {@link Card},
 * so when the mouse event fires it knows exactly which card was clicked
 * and forwards that information to the registered listener.
 * </p>
 *
 * <p>This is a classic example of the Adapter design pattern: it converts
 * the interface of a JavaFX mouse event into the interface our application
 * expects.</p>
 *
 */
public class CardMouseAdapter implements EventHandler<MouseEvent> {

    /** The card this adapter is bound to. */
    private final Card card;

    /** The listener notified when the card is clicked. */
    private final CardClickListener listener;

    /**
     * Creates an adapter binding a card to a click listener.
     *
     * @param card     the card associated with this adapter
     * @param listener the listener to notify on click
     */
    public CardMouseAdapter(Card card, CardClickListener listener) {
        this.card = card;
        this.listener = listener;
    }

    /**
     * Handles a mouse click by forwarding the bound card to the listener.
     *
     * @param event the mouse event that triggered this handler
     */
    @Override
    public void handle(MouseEvent event) {
        if (listener != null) {
            listener.onCardClicked(card);
        }
    }
}