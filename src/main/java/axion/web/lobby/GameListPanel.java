package axion.web.lobby;

import axion.domain.Game;
import axion.domain.Player;
import axion.domain.base.BaseListView;
import axion.domain.base.BaseModal;
import axion.domain.base.BasePage;
import axion.domain.base.BasePanel;
import axion.service.AxionService;
import axion.web.game.ConfigurePlayerPage;
import com.google.inject.Inject;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.Strings;

import java.util.List;
import java.util.stream.Collectors;

public class GameListPanel extends BasePanel {

    @Inject
    private transient AxionService axionService;

    public GameListPanel(String id) {
        super(id);
    }

    @Override
    protected void onBeforeFirstRender() {
        Form<Void> form = new Form<>("form");
        add(form);

        final FeedbackPanel feedbackPanel = new FeedbackPanel("feedback");
        feedbackPanel.setOutputMarkupId(true);
        form.add(feedbackPanel);

        // Watch a game

        IModel<List<Game>> gamesModel = new LoadableDetachableModel<List<Game>>() {
            @Override
            protected List<Game> load() {
                return axionService.getGames();
            }
        };
        ListView<Game> games = new BaseListView<Game>("games", gamesModel) {
            @Override
            protected void populateItem(ListItem<Game> item) {
                Game game = item.getModelObject();

                final ModalWindow gamePasswordModal;
                item.add(gamePasswordModal = new BaseModal("gamePassword"));
                gamePasswordModal.setContent(new PasswordPanel(gamePasswordModal.getContentId(), item.getModel()));

                AjaxLink<Void> joinGame;
                item.add(joinGame = new AjaxLink<Void>("joinGame") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        Game game = item.getModelObject();
                        if(Strings.isEmpty(game.getPassword())) {
                            game.addPlayer(getPlayer());
                            axionService.saveGameState(game);

                            PageParameters params = new PageParameters();
                            params.set(BasePage.GAME_ID, game.getId());
                            setResponsePage(ConfigurePlayerPage.class, params);
                        } else {
                            gamePasswordModal.show(target);
                        }
                    }
                });
                joinGame.add(new Label("name", game.getName()));
                joinGame.add(new Label("players", game.getPlayers().stream()
                        .map(Player::getUsername).collect(Collectors.joining(", ", " [", "]"))));
            }
        };
        games.setVisibilityAllowed(!gamesModel.getObject().isEmpty());
        add(games);
    }

    public static class PasswordPanel extends BasePanel {

        @Inject
        private transient AxionService axionService;

        public PasswordPanel(String id, IModel<Game> gameModel) {
            super(id, gameModel);
        }

        @Override
        protected void onBeforeFirstRender() {
            Form<Void> form = new Form<>("form");
            add(form);

            final FeedbackPanel feedbackPanel = new FeedbackPanel("feedback");
            feedbackPanel.setOutputMarkupId(true);
            form.add(feedbackPanel);

            final IModel<String> gamePasswordModel = Model.of();
            form.add(new PasswordTextField("password", gamePasswordModel));

            AjaxSubmitLink submitLink;
            form.add(submitLink = new AjaxSubmitLink("submit") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    Game game = (Game)PasswordPanel.this.getDefaultModelObject();
                    if(axionService.validateGamePassword(game, gamePasswordModel.getObject())) {
                        game.addPlayer(getPlayer());
                        axionService.saveGameState(game);

                        PageParameters params = new PageParameters();
                        params.set(BasePage.GAME_ID, game.getId());
                        setResponsePage(ConfigurePlayerPage.class, params);
                    } else {
                        error("Invalid password");
                        target.add(feedbackPanel);
                    }
                }

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    target.add(feedbackPanel);
                }
            });
            form.setDefaultButton(submitLink);
        }
    }
}
