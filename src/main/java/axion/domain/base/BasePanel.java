package axion.domain.base;

import axion.AxionApplication;
import axion.AxionSession;
import axion.domain.Game;
import axion.domain.Player;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import java.io.IOException;
import java.io.ObjectInputStream;

public class BasePanel extends Panel {

    private boolean loaded = false;
    private boolean renderChildren = true;

    public BasePanel(String id) {
        super(id);
    }

    public BasePanel(String id, IModel<?> model) {
        super(id, model);
    }

    public Game getGame() {
        return ((BasePage)getPage()).getGame();
    }

    public Player getPlayer() {
        return ((AxionSession)getSession()).getPlayer();
    }

    private void readObject(ObjectInputStream in)
            throws ClassNotFoundException, IOException {
        in.defaultReadObject();
        ((AxionApplication)getApplication()).getInjector().inject(this);
    }

    /**
     * In our app, you should probably be using onBeforeFirstRender or possibly onBeforeEachRender
     *  instead of onConfigure, due to the inability to modify the component hierarchy in onConfigure
     */
    @Override
    @Deprecated
    protected void onConfigure() {
        super.onConfigure();
    }

    /**
     * In our app, you should probably be using onBeforeFirstRender instead of onInitialize, due to our large
     * amount of AJAX calls.  onInitialize gets called after construction, regardless of display
     */
    @Override
    @Deprecated
    protected void onInitialize() {
        super.onInitialize();
    }

    /**
     * Do not override this method directly, it's recommend that you use onBeforeFirstRender or onBeforeEachRender
     * instead.  Note that on the first render, both methods will be called.
     */
    @Override
    protected final void onBeforeRender() {
        if(!loaded) {
            onBeforeFirstRender();
            loaded = true;
        }
        onBeforeEachRender();
        if(renderChildren) {
            super.onBeforeRender();
        }
        renderChildren = true;
    }

    /**
     * This method should _ONLY_ be called once during the onBeforeFirstRender or onBeforeEachRender, and does not need
     * to be called at all.  It forces the children to render before the parent, typically to ensure some amount of
     * structure is available.  Not to be confused with the guaranteeRendered method that ensures the onBeforeFirstRender
     * method has been called, if not..calling it.
     */
    protected final void renderChildren() {
        super.onBeforeRender();
        renderChildren = false;
    }

    /**
     * Is called each time this component is rendered, note that per the Wicket JavaDocs, this occurs before
     * delegating to the super.onBeforeRender()
     */
    protected void onBeforeEachRender() {
        // null-op
    }

    /**
     * Is called before this component is rendered, but only once per component instantiation.  Note that it is called
     * before onBeforeEachRender the one time it is called.
     */
    protected void onBeforeFirstRender() {
        // null-op
    }

    /**
     * This method can be used to force the onBeforeFirstRender to run if it has not already.  Potentially useful
     * in onEvent methods that require the object to be constructed before render time based on receiving certain events.
     * Note that this should not be confused with renderChildren which should only be used within the onBeforeFirstRender
     * or onBeforeEachRender method to change the order that children are rendered.
     */
    protected void guaranteeRendered() {
        if(!loaded) {
            onBeforeFirstRender();
            loaded = true;
        }
    }
}
