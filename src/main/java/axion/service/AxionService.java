package axion.service;

import axion.domain.*;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.mybatis.guice.transactional.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class AxionService {
    @Inject
    private AxionMapper axionMapper;
    @Inject
    private DeckService deckService;

    private Map<Integer, Game> gameMap = new HashMap<>();

    @Transactional
    public Player createPlayer(String username, String password) {
        Player player = Player.instance(username, password);
        axionMapper.createPlayer(player);
        return player;
    }

    public Player findPlayer(String username) {
        return axionMapper.findPlayer(username);
    }

    @Transactional
    public Game createGame(Game game) {
        axionMapper.createGame(game);
        game.setPilotDeck(deckService.getPilotDeck());
        game.setEventDeck(deckService.getEventDeck());
        game.setModuleDeck(deckService.getModuleDeck());
        game.setMission1Deck(deckService.getMissionDeck(Tier.ONE));
        game.setMission2Deck(deckService.getMissionDeck(Tier.TWO));
        game.setMission3Deck(deckService.getMissionDeck(Tier.THREE));
        game.setResource1Deck(deckService.getResourceDeck(Tier.ONE));
        game.setResource2Deck(deckService.getResourceDeck(Tier.TWO));
        game.setResource3Deck(deckService.getResourceDeck(Tier.THREE));
        saveGameState(game);
        return game;
    }

    public List<Game> getGames() {
        axionMapper.getGames().stream()
                .filter(g -> !gameMap.containsKey(g.getId()))
                .forEach(g -> gameMap.put(g.getId(), g));
        return new ArrayList<>(gameMap.values());
    }

    public Game getGame(Integer id) {
        Game game = gameMap.get(id);
        if(game == null) {
            return axionMapper.getGame(id);
        } else {
            return game;
        }
    }

    // TODO: Figure out when best to call this
    public Game saveGameState(Game game) {
        axionMapper.saveGame(game);
        game.getPlayers().forEach(p -> axionMapper.addPlayerToGame(p, game));
        saveDeck(game.getPilotDeck());
        saveDeck(game.getEventDeck());
        saveDeck(game.getModuleDeck());
        saveDeck(game.getMission1Deck());
        saveDeck(game.getMission2Deck());
        saveDeck(game.getMission3Deck());
        saveDeck(game.getResource1Deck());
        saveDeck(game.getResource2Deck());
        saveDeck(game.getResource3Deck());
        gameMap.put(game.getId(), game);
        return game;
    }

    public boolean validateGamePassword(Game game, String password) {
        return axionMapper.validateGamePassword(game, password) > 0;
    }

    private <T extends Card> void saveDeck(Deck<T> deck) {
        deck.getCards().forEach(axionMapper::saveCard);
    }
}
