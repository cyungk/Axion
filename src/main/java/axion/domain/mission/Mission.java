package axion.domain.mission;

import axion.domain.Tier;
import axion.domain.Card;

public class Mission extends Card {
    private Tier tier;

    public Mission(String name, Tier tier) {
        super(name);
        this.tier = tier;
    }

    public Tier getTier() {
        return tier;
    }
}
