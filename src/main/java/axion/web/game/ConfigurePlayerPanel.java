package axion.web.game;

import axion.domain.base.BasePanel;
import axion.domain.pilot.Pilot;
import axion.service.DeckService;
import com.google.inject.Inject;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;

import java.util.List;

public class ConfigurePlayerPanel extends BasePanel {

    @Inject
    private transient DeckService deckService;

    public ConfigurePlayerPanel(String id) {
        super(id);
    }

    @Override
    protected void onBeforeFirstRender() {
        Form<Void> form = new Form<>("form");
        add(form);

        final FeedbackPanel feedbackPanel = new FeedbackPanel("feedback");
        feedbackPanel.setOutputMarkupId(true);
        form.add(feedbackPanel);

        IModel<List<Pilot>> pilotsModel = new LoadableDetachableModel<List<Pilot>>() {
            @Override
            protected List<Pilot> load() {
                return deckService.getPilots();
            }
        };

        IModel<Pilot> pilotModel = Model.of();
        form.add(new DropDownChoice<>("pilot", pilotModel, pilotsModel,
                new ChoiceRenderer<>("name")));


    }
}
