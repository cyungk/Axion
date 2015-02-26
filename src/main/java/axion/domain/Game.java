package axion.domain;

import axion.domain.base.BaseDomain;
import axion.domain.event.Event;
import axion.domain.mission.Mission;
import axion.domain.module.Module;
import axion.domain.pilot.Pilot;
import axion.domain.resource.Resource;
import axion.domain.victorycondition.VictoryCondition;

import java.util.ArrayList;
import java.util.List;

public class Game extends BaseDomain {

    private String name;
    private String password;
    private VictoryCondition victoryCondition;
    private List<Player> players;
    private Deck<Pilot> pilotDeck;
    private Deck<Module> moduleDeck;
    private Deck<Resource> resource1Deck;
    private Deck<Resource> resource2Deck;
    private Deck<Resource> resource3Deck;
    private Deck<Mission> mission1Deck;
    private Deck<Mission> mission2Deck;
    private Deck<Mission> mission3Deck;
    private Deck<Event> eventDeck;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setVictoryCondition(VictoryCondition victoryCondition) {
        this.victoryCondition = victoryCondition;
    }

    public VictoryCondition getVictoryCondition() {
        return victoryCondition;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public boolean addPlayer(Player player) {
        if(getPlayers() == null) {
            setPlayers(new ArrayList<>());
        }
        if(getPlayers().contains(player)) {
            return false;
        } else {
            getPlayers().add(player);
            return true;
        }
    }

    public Deck<Pilot> getPilotDeck() {
        return pilotDeck;
    }

    public void setPilotDeck(Deck<Pilot> pilotDeck) {
        this.pilotDeck = pilotDeck;
    }

    public Deck<Module> getModuleDeck() {
        return moduleDeck;
    }

    public void setModuleDeck(Deck<Module> moduleDeck) {
        this.moduleDeck = moduleDeck;
    }

    public Deck<Resource> getResource1Deck() {
        return resource1Deck;
    }

    public void setResource1Deck(Deck<Resource> resource1Deck) {
        this.resource1Deck = resource1Deck;
    }

    public Deck<Resource> getResource2Deck() {
        return resource2Deck;
    }

    public void setResource2Deck(Deck<Resource> resource2Deck) {
        this.resource2Deck = resource2Deck;
    }

    public Deck<Resource> getResource3Deck() {
        return resource3Deck;
    }

    public void setResource3Deck(Deck<Resource> resource3Deck) {
        this.resource3Deck = resource3Deck;
    }

    public Deck<Mission> getMission1Deck() {
        return mission1Deck;
    }

    public void setMission1Deck(Deck<Mission> mission1Deck) {
        this.mission1Deck = mission1Deck;
    }

    public Deck<Mission> getMission2Deck() {
        return mission2Deck;
    }

    public void setMission2Deck(Deck<Mission> mission2Deck) {
        this.mission2Deck = mission2Deck;
    }

    public Deck<Mission> getMission3Deck() {
        return mission3Deck;
    }

    public void setMission3Deck(Deck<Mission> mission3Deck) {
        this.mission3Deck = mission3Deck;
    }

    public Deck<Event> getEventDeck() {
        return eventDeck;
    }

    public void setEventDeck(Deck<Event> eventDeck) {
        this.eventDeck = eventDeck;
    }
}
