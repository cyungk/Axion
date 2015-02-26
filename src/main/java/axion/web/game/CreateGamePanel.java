package axion.web.game;

import axion.domain.Game;
import axion.domain.base.BasePage;
import axion.domain.base.BasePanel;
import axion.domain.victorycondition.VictoryCondition;
import axion.service.AxionService;
import axion.service.DeckService;
import com.google.inject.Inject;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.util.List;

public class CreateGamePanel extends BasePanel {

    @Inject
    private transient AxionService axionService;
    @Inject
    private transient DeckService deckService;

    public CreateGamePanel(String id) {
        super(id);
    }

    @Override
    protected void onBeforeFirstRender() {
        Form<Void> form = new Form<>("form");
        add(form);

        final FeedbackPanel feedbackPanel = new FeedbackPanel("feedback");
        feedbackPanel.setOutputMarkupId(true);
        form.add(feedbackPanel);

        IModel<String> nameModel = Model.of();
        form.add(new RequiredTextField<>("name", nameModel));

        IModel<String> passwordModel = Model.of();
        form.add(new PasswordTextField("password", passwordModel).setRequired(false));

        IModel<List<VictoryCondition>> victoryConditionsModel = new LoadableDetachableModel<List<VictoryCondition>>() {
            @Override
            protected List<VictoryCondition> load() {
                return deckService.getVictoryConditions();
            }
        };
        IModel<VictoryCondition> victoryConditionModel = Model.of();
        form.add(new DropDownChoice<>("victoryCondition", victoryConditionModel, victoryConditionsModel,
                new ChoiceRenderer<>("name")).setRequired(true));

        form.add(new AjaxSubmitLink("createGame") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                Game game = new Game();
                game.setName(nameModel.getObject());
                game.setPassword(passwordModel.getObject());
                game.setVictoryCondition(victoryConditionModel.getObject());
                game.addPlayer(getPlayer());
                axionService.createGame(game);
                PageParameters params = getPage().getPageParameters();
                params.set(BasePage.GAME_ID, game.getId());
                setResponsePage(ConfigurePlayerPage.class, params);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                target.add(feedbackPanel);
            }
        });
    }
}
