package org.katyshevtseva.medialog.view.music;

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

import static org.katyshevtseva.medialog.view.utils.ViewConstants.ALBUMS_NODE;
import static org.katyshevtseva.medialog.view.utils.ViewConstants.SINGERS_AND_GENRES_NODE;

public class MainMusicController extends AbstractSwitchController implements SectionController {
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
                new Section("Albums", new AlbumsController(),
                        controller -> WindowBuilder.getNode(ALBUMS_NODE, controller)),
                new Section("Singers and Genres", new SingersAndGenresController(),
                        controller -> WindowBuilder.getNode(SINGERS_AND_GENRES_NODE, controller)));
    }

    private void placeButton(Button button) {
        buttonBox.getChildren().addAll(FxUtils.getPaneWithWidth(30), button);
    }
}
