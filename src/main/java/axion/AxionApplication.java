package axion;

import axion.modules.MyBatisModule;
import axion.modules.ReflectionsModule;
import axion.web.lobby.LobbyPage;
import axion.web.LoginPage;
import org.apache.wicket.Session;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AnnotationsRoleAuthorizationStrategy;
import org.apache.wicket.guice.GuiceComponentInjector;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;

public class AxionApplication extends AuthenticatedWebApplication {

	@Override
	public void init() {
		super.init();
        initGuice();
    }

    private void initGuice() {
        injector = new GuiceComponentInjector(this, new MyBatisModule(), new ReflectionsModule());
        getComponentPreOnBeforeRenderListeners().add(c -> AxionApplication.this.getInjector().inject(c));
        getSecuritySettings().setAuthorizationStrategy(new AnnotationsRoleAuthorizationStrategy(this));
        injector.inject(this);
    }

    @Override
    protected Class<? extends AbstractAuthenticatedWebSession> getWebSessionClass() {
        return AxionSession.class;
    }

    @Override
    public Session newSession(Request request, Response response) {
        Session session = super.newSession(request, response);
        injector.inject(session);
        return session;
    }

    @Override
    protected Class<? extends WebPage> getSignInPageClass() {
        return LoginPage.class;
    }

    @Override
    public Class<? extends WebPage> getHomePage() {
        return LobbyPage.class;
    }

    private static GuiceComponentInjector injector;

    public static GuiceComponentInjector getInjector() {
        return injector;
    }
}
