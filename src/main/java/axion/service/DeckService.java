package axion.service;

import axion.domain.Card;
import axion.domain.Deck;
import axion.domain.Tier;
import axion.domain.event.Event;
import axion.domain.mission.Mission;
import axion.domain.module.Module;
import axion.domain.pilot.Pilot;
import axion.domain.resource.Resource;
import axion.domain.victorycondition.VictoryCondition;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.reflections.Reflections;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Singleton
public class DeckService {

    @Inject
    private Reflections reflections;

    public List<VictoryCondition> getVictoryConditions() {
        return getCards(VictoryCondition.class);
    }

    public List<Pilot> getPilots() {
        return getCards(Pilot.class);
    }

    private <T extends Card> List<T> getCards(Class<T> cardClass) {
        return reflections.getSubTypesOf(cardClass).stream()
                .flatMap(c -> Card.getAllCopies(c).stream())
                .collect(toList());
    }

    public Deck<Event> getEventDeck() {
        List<Event> events = getCards(Event.class);
        return new Deck<>(events);
    }

    public Deck<Pilot> getPilotDeck() {
        List<Pilot> pilots = getCards(Pilot.class);
        return new Deck<>(pilots);
    }

    public Deck<Module> getModuleDeck() {
        List<Module> modules = getCards(Module.class);
        return new Deck<>(modules);
    }

    public Deck<Mission> getMissionDeck(Tier tier) {
        List<Mission> missions = getCards(Mission.class);
        return new Deck<>(missions.stream().filter(m -> m.getTier() == tier).collect(toList()));
    }

    public Deck<Resource> getResourceDeck(Tier tier) {
        List<Resource> resources = getCards(Resource.class);
        return new Deck<>(resources.stream().filter(r -> r.getTier() == tier).collect(toList()));
    }
}
