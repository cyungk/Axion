package axion.web.game;

import axion.domain.base.BasePage;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class ConfigurePlayerPage extends BasePage {

    public ConfigurePlayerPage(PageParameters params) {
        super(params);
        add(new ConfigurePlayerPanel("configurePlayer"));
    }
}
