package axion.domain.base;

import axion.AxionApplication;
import axion.AxionSession;
import axion.domain.Game;
import axion.domain.Player;
import axion.service.AxionService;
import com.google.inject.Inject;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;

import java.io.IOException;
import java.io.ObjectInputStream;

@AuthorizeInstantiation("PLAYER")
public class BasePage extends WebPage {

    public static final int GAME_ID = 0;

    @Inject
    private transient AxionService axionService;

    private Integer gameId;

    public BasePage(PageParameters params) {
        super(params);
        StringValue gameIdValue = params.get(GAME_ID);
        if(!gameIdValue.isEmpty()) {
            gameId = gameIdValue.toInt();
        }
    }

    public Game getGame() {
        return axionService.getGame(gameId);
    }

    public Player getPlayer() {
        return ((AxionSession)getSession()).getPlayer();
    }

    private void readObject(ObjectInputStream in)
            throws ClassNotFoundException, IOException {
        in.defaultReadObject();
        ((AxionApplication)getApplication()).getInjector().inject(this);
    }
}
