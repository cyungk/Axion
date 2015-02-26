package axion.web;

import axion.AxionSession;
import axion.domain.Player;
import axion.service.AxionService;
import axion.web.lobby.LobbyPage;
import axion.web.util.DefaultFocusBehavior;
import com.google.inject.Inject;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class LoginPage extends WebPage {

    @Inject
    private transient AxionService axionService;

    public LoginPage() {
        if(((AxionSession)getSession()).isSignedIn()) {
            setResponsePage(LobbyPage.class);
        }

        Form<Void> form = new Form<>("form");
        add(form);

        final FeedbackPanel feedbackPanel = new FeedbackPanel("feedback");
        feedbackPanel.setOutputMarkupId(true);
        form.add(feedbackPanel);

        IModel<String> nameModel = Model.of();
        form.add(new RequiredTextField<>("username", nameModel).add(new DefaultFocusBehavior()));

        IModel<String> passwordModel = Model.of();
        form.add(new PasswordTextField("password", passwordModel));

        AjaxSubmitLink submitLink;
        form.add(submitLink = new AjaxSubmitLink("submitLink") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                if(((AxionSession)getSession()).signIn(nameModel.getObject(), passwordModel.getObject())) {
                    continueToOriginalDestination();
                    setResponsePage(getApplication().getHomePage());
                } else {
                    error("Invalid username and password");
                    target.add(feedbackPanel);
                }
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                target.add(feedbackPanel);
            }
        });
        form.setDefaultButton(submitLink);

        form.add(new AjaxSubmitLink("createLink") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                Player player = axionService.findPlayer(nameModel.getObject());
                if (player == null) {
                    axionService.createPlayer(nameModel.getObject(), passwordModel.getObject());
                    ((AxionSession) getSession()).signIn(nameModel.getObject(), passwordModel.getObject());
                    setResponsePage(getApplication().getHomePage());
                } else {
                    error("Player already exists");
                    target.add(feedbackPanel);
                }
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                target.add(feedbackPanel);
            }
        });
    }
}
