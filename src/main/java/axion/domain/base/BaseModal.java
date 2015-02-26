package axion.domain.base;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.model.IModel;

public class BaseModal extends ModalWindow {
    private Component focusComponent;

    public BaseModal(String id) {
        super(id);
    }

    public BaseModal(String id, IModel<?> model) {
        super(id, model);
    }

    @Override
    public void show(AjaxRequestTarget target) {
        target.appendJavaScript("Wicket.Window.unloadConfirmation = false;");
        super.show(target);
    }

    @Override
    protected CharSequence getShowJavaScript() {
        return "window.setTimeout(function(){\n  Wicket.Window.create(settings).show();\n " +
                "try { $('.focusComponent').focus(); } catch(ignore) {}\n}, 0);\n";
    }
}
