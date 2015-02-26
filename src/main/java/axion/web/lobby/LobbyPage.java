package axion.web.lobby;

import axion.domain.base.BasePage;
import axion.web.game.CreateGamePanel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class LobbyPage extends BasePage {

    public LobbyPage(PageParameters params) {
        super(params);
        add(new Label("name", getPlayer().getUsername()));

        // Join a game being created
        // Watch a game
        add(new GameListPanel("gameList"));

        // Start new game
        add(new CreateGamePanel("createGame"));

        add(new PlayerProfilePanel("playerProfile"));
    }
}
