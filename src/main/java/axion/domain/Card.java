package axion.domain;

import axion.util.ExceptionUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Card implements Serializable {

    private String name;
    private int cardCount;
    private Player player;
    private Boolean discarded;

    public Card(String name) {
        this(name, 1);
    }

    public Card(String name, int cardCount) {
        this.name = name;
        this.cardCount = cardCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCardCount() {
        return cardCount;
    }

    public void setCardCount(int cardCount) {
        this.cardCount = cardCount;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Boolean getDiscarded() {
        return discarded;
    }

    public void setDiscarded(Boolean discarded) {
        this.discarded = discarded;
    }

    public static <T extends Card> List<T> getAllCopies(Class<T> cardClass){
        try {
            T card = cardClass.newInstance();
            List<T> cardCopies = new ArrayList<>();
            cardCopies.add(card);
            for (int i = 1; i < card.getCardCount(); i++) {
                cardCopies.add(cardClass.newInstance());
            }
            return cardCopies;
        } catch (Exception e) {
            throw ExceptionUtil.runtime(e);
        }
    }
}
