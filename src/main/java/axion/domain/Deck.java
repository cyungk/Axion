package axion.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck<T extends Card> {

    private List<T> drawPile;
    private List<T> discardPile;

    public Deck(List<T> events) {
        drawPile = new ArrayList<>(events);
        discardPile = new ArrayList<>();
    }

    public synchronized T draw(Player player) {
        if(drawPile.isEmpty()) {
            drawPile.addAll(discardPile);
            discardPile.forEach(c -> c.setDiscarded(false));
            Collections.shuffle(drawPile);
            discardPile.clear();
        }

        T card = drawPile.remove(0);
        card.setPlayer(player);
        return card;
    }

    public synchronized void discard(T card) {
        card.setDiscarded(true);
        discardPile.add(card);
    }

    public List<T> getCards() {
        List<T> cards = new ArrayList<>();
        cards.addAll(drawPile);
        cards.addAll(discardPile);
        return cards;
    }
}
