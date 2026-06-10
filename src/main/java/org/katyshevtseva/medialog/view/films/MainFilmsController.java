package org.katyshevtseva.medialog.view.films;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.switchcontroller.AbstractSwitchController;
import com.katyshevtseva.fx.switchcontroller.Section;
import com.katyshevtseva.fx.switchcontroller.SectionController;
import com.katyshevtseva.fx.windowbuilder.WindowBuilder;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.util.Arrays;
import java.util.List;

import static org.katyshevtseva.medialog.view.utils.ViewConstants.*;

public class MainFilmsController extends AbstractSwitchController implements SectionController {
    @FXML
    private Pane mainPane;
    @FXML
    private HBox buttonBox;

    @FXML
    private void initialize() {
        init(getSections(), mainPane, this::placeButton);
    }

    private List<Section> getSections() {
        return Arrays.asList(
                new Section("Lists", new ListsController(),
                        controller -> WindowBuilder.getNode(FILM_LISTS_NODE, controller)),
                new Section("Statistics", new StatisticsController(),
                        controller -> WindowBuilder.getNode(FILM_STATISTICS_NODE, controller)),
                new Section("Viewing history", new ViewingHistoryController(),
                        controller -> WindowBuilder.getNode(VIEWING_HISTORY_NODE, controller)),
                new Section("To watch", new ToWatchController(),
                        controller -> WindowBuilder.getNode(TO_WATCH_NODE, controller)),
                new Section("Search", new SearchController(),
                        controller -> WindowBuilder.getNode(FILM_SEARCH_NODE, controller))
        );
    }

    private void placeButton(Button button) {
        buttonBox.getChildren().addAll(FxUtils.getPaneWithWidth(30), button);
    }
}
