package org.katyshevtseva.medialog.view;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.switchcontroller.AbstractSwitchController;
import com.katyshevtseva.fx.switchcontroller.Section;
import com.katyshevtseva.fx.windowbuilder.FxController;
import com.katyshevtseva.fx.windowbuilder.WindowBuilder;
import org.katyshevtseva.medialog.core.NbLogger;
import org.katyshevtseva.medialog.view.books.MainBooksController;
import org.katyshevtseva.medialog.view.films.MainFilmsController;
import org.katyshevtseva.medialog.view.music.MainMusicController;
import org.katyshevtseva.medialog.view.series.MainSeriesController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.Arrays;
import java.util.List;

import static org.katyshevtseva.medialog.view.utils.ViewConstants.*;

public class MainController extends AbstractSwitchController implements FxController {
    @FXML
    private Pane mainPane;
    @FXML
    private VBox buttonBox;

    @FXML
    private void initialize() {
        NbLogger.log("********* Started *********");
        init(getSections(), mainPane, this::placeButton);
    }

    private List<Section> getSections() {
        return Arrays.asList(
                new Section("Films", new MainFilmsController(),
                        controller -> WindowBuilder.getNode(SECTION_MAIN_NODE, controller)),
                new Section("Music", new MainMusicController(),
                        controller -> WindowBuilder.getNode(SECTION_MAIN_NODE, controller)),
                new Section("Series", new MainSeriesController(),
                        controller -> WindowBuilder.getNode(SERIES_NODE, controller)),
                new Section("Books", new MainBooksController(),
                        controller -> WindowBuilder.getNode(BOOKS_NODE, controller)));
    }

    private void placeButton(Button button) {
        FxUtils.setWidth(button, 180);
        buttonBox.getChildren().addAll(FxUtils.getPaneWithHeight(40), button);
    }
}