package com.example.cincuentazo.controller.handler;

import com.example.cincuentazo.model.Card;

/**
 * Custom listener contract notified when the human player clicks on a card
 * from their hand.
 * <p>
 * This interface decouples the visual card components from the controller
 * logic that reacts to a card being selected. Any class interested in
 * reacting to card clicks implements this interface and registers itself
 * with the card views.
 * </p>
 *
 */
public interface CardClickListener {

    /**
     * Invoked when the human player clicks on one of their cards.
     *
     * @param card the card that was clicked
     */
    void onCardClicked(Card card);
}
