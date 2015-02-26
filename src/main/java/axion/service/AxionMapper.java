package axion.service;

import axion.domain.Card;
import axion.domain.Game;
import axion.domain.Player;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AxionMapper {
    public int authenticate(@Param("username") String username, @Param("password") String password);

    public Player findPlayer(@Param("username") String username);
    public int createPlayer(Player player);

    public List<Game> getGames();
    public Game getGame(Integer id);

    public int createGame(Game game);
    public void saveGame(Game game);
    public void addPlayerToGame(@Param("player") Player player, @Param("game") Game game);

    public int validateGamePassword(@Param("game") Game game, @Param("password") String password);

    public void saveCard(Card card);
}
