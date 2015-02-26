package axion.domain.resource;

import axion.domain.Tier;
import axion.domain.Card;

public class Resource extends Card {

    private Tier tier;

    public Resource(String name, Tier tier) {
        super(name);
        this.tier = tier;
    }

    public Tier getTier() {
        return tier;
    }
}
