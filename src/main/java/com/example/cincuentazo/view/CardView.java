package com.example.cincuentazo.view;

import com.example.cincuentazo.model.Card;
import com.example.cincuentazo.model.Suit;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Visual representation of a single playing card in the user interface.
 * <p>
 * A {@code CardView} can render a card face-up (showing its rank and suit)
 * or face-down (showing the card back). Face-up cards are used for the
 * human player's hand and for the table; face-down cards are used for the
 * machine opponents' hands.
 * </p>
 *
 * <p>This class belongs to the view layer and contains no game logic; it
 * only knows how to draw a card.</p>
 *
 */
public class CardView extends StackPane {

    /** The card this view represents (may be {@code null} for a pure back). */
    private final Card card;

    /**
     * Creates a card view.
     *
     * @param card     the card to represent
     * @param faceUp   {@code true} to show the card face, {@code false} for back
     */
    public CardView(Card card, boolean faceUp) {
        this.card = card;
        getStyleClass().add("card-view");
        if (faceUp && card != null) {
            renderFaceUp();
        } else {
            renderFaceDown();
        }
    }

    /**
     * Renders the card showing its rank and suit.
     */
    private void renderFaceUp() {
        getStyleClass().add("card-face-up");
        if (isRedSuit(card.getSuit())) {
            getStyleClass().add("card-red");
        } else {
            getStyleClass().add("card-black");
        }

        Label rankLabel = new Label(card.getRank().getSymbol());
        rankLabel.getStyleClass().add("card-rank");

        Label suitLabel = new Label(getSuitSymbol(card.getSuit()));
        suitLabel.getStyleClass().add("card-suit");

        VBox content = new VBox(2, rankLabel, suitLabel);
        content.setAlignment(Pos.CENTER);
        getChildren().add(content);
    }

    /**
     * Renders the back of the card (used for hidden machine cards).
     */
    private void renderFaceDown() {
        getStyleClass().add("card-face-down");
        Label backLabel = new Label("?");
        backLabel.getStyleClass().add("card-back-symbol");
        getChildren().add(backLabel);
    }

    /**
     * Returns the card represented by this view.
     *
     * @return the card, or {@code null} if this is a pure back view
     */
    public Card getCard() {
        return card;
    }

    /**
     * Determines whether a suit is rendered in red (hearts or diamonds).
     *
     * @param suit the suit to check
     * @return {@code true} if the suit is red
     */
    private boolean isRedSuit(Suit suit) {
        return suit == Suit.HEARTS || suit == Suit.DIAMONDS;
    }

    /**
     * Returns a single-character symbol for a suit, for display purposes.
     *
     * @param suit the suit
     * @return the suit symbol
     */
    private String getSuitSymbol(Suit suit) {
        switch (suit) {
            case HEARTS:
                return "\u2665";   // ♥
            case DIAMONDS:
                return "\u2666";   // ♦
            case CLUBS:
                return "\u2663";   // ♣
            case SPADES:
                return "\u2660";   // ♠
            default:
                return "?";
        }
    }
}