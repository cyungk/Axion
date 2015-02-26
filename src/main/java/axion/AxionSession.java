package axion;

import axion.domain.Player;
import axion.service.AxionMapper;
import com.google.inject.Inject;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.Request;

public class AxionSession extends AuthenticatedWebSession {

    @Inject
    private transient AxionMapper axionMapper;
    private String playerName;
    private transient Player player;

    public AxionSession(Request request) {
        super(request);
    }

    @Override
    public boolean authenticate(String username, String password) {
        playerName = username;
        return axionMapper.authenticate(username, password) > 0;
    }

    @Override
    public Roles getRoles() {
        if(isSignedIn()) {
            return new Roles("PLAYER");
        } else {
            return new Roles();
        }
    }

    public Player getPlayer() {
        if(player == null) {
            return axionMapper.findPlayer(playerName);
        } else {
            return player;
        }
    }
}
