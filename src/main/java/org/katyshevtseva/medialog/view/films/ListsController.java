package org.katyshevtseva.medialog.view.films;

import com.katyshevtseva.fx.Size;
import com.katyshevtseva.fx.Styler;
import com.katyshevtseva.fx.component.ComponentBuilder;
import com.katyshevtseva.fx.component.controller.BlockGridController;
import com.katyshevtseva.fx.switchcontroller.SectionController;
import com.katyshevtseva.fx.windowbuilder.WindowBuilder;
import org.katyshevtseva.medialog.core.films.Service;
import org.katyshevtseva.medialog.core.films.model.Film;
import org.katyshevtseva.medialog.core.films.model.FilmGrade;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.katyshevtseva.fx.Styler.ThingToColor.*;
import static org.katyshevtseva.medialog.core.films.model.FilmGrade.*;
import static org.katyshevtseva.medialog.view.utils.ViewConstants.FILM_DETAIL_DIALOG;

public class ListsController implements SectionController {
    private final Map<FilmGrade, BlockGridController<Film>> filmGridControllerMap = new HashMap<>();
    @FXML
    private HBox filmsPane;
    @FXML
    private TextField searchTextField;
    @FXML
    private Button clearButton;

    @FXML
    private void initialize() {
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> updateContent());
        clearButton.setOnAction(event -> searchTextField.setText(""));

        adjustFilmsPane();
    }

    @Override
    public void update() {
        updateContent();
    }

    private void adjustFilmsPane() {
        int gridColumnWidth = 210;
        Size gridColumnSize = new Size(780, gridColumnWidth);
        Size smallGridColumnSize = new Size(380, gridColumnWidth);
        int filmBlockWidth = gridColumnWidth - 50;//BlockGridController FRAME_SIZE = 20; 50=20*2+10; 10 на scrollbar

        //FAVOURITE, EXCELLENT, GOOD, NORMAL
        for (FilmGrade grade : Arrays.asList(FAVOURITE, EXCELLENT, GOOD, NORMAL)) {
            ComponentBuilder.Component<BlockGridController<Film>> component =
                    new ComponentBuilder().setSize(gridColumnSize).getBlockGridComponent(filmBlockWidth,
                            null, null, this::getFilmNode);
            component.getController().getGridPane().setStyle(Styler.getColorfullStyle(BACKGROUND, grade.getColor()));
            VBox vBox = new VBox();
            vBox.getChildren().addAll(new Label(grade.toString()), component.getNode());
            filmsPane.getChildren().add(vBox);
            filmGridControllerMap.put(grade, component.getController());
        }

        for (List<FilmGrade> grades : Arrays.asList(Arrays.asList(SOSO, NOTCLASSIFIED), Arrays.asList(BAD, NOTFINISHED))) {
            VBox vBox = new VBox();
            for (FilmGrade grade : grades) {
                ComponentBuilder.Component<BlockGridController<Film>> component =
                        new ComponentBuilder().setSize(smallGridColumnSize).getBlockGridComponent(filmBlockWidth,
                                null, null, this::getFilmNode);
                component.getController().getGridPane().setStyle(Styler.getColorfullStyle(BACKGROUND, grade.getColor()));
                vBox.getChildren().addAll(new Label(grade.toString()), component.getNode());
                filmGridControllerMap.put(grade, component.getController());
            }
            filmsPane.getChildren().add(vBox);
        }
    }

    private Node getFilmNode(Film film, int blockWidth) {
        VBox vBox = new VBox();
        vBox.setStyle(vBox.getStyle() + Styler.getColorfullStyle(BORDER, Styler.StandardColor.BLACK));
        vBox.setAlignment(Pos.CENTER);

        Label label = new Label(film.getTitleAndYear());
        label.setStyle(label.getStyle() + Styler.getColorfullStyle(TEXT, Styler.StandardColor.BLACK));
        label.setMaxWidth(blockWidth);
        label.setWrapText(true);
        label.setAlignment(Pos.CENTER);
        label.setTextAlignment(TextAlignment.CENTER);
        vBox.getChildren().add(label);
        vBox.setOnMouseClicked(event ->
                WindowBuilder.openDialog(FILM_DETAIL_DIALOG, new DetailsController(film, this::updateContent)));

        return vBox;
    }

    private void updateContent() {
        for (Map.Entry<FilmGrade, BlockGridController<Film>> entry : filmGridControllerMap.entrySet()) {
            entry.getValue().setContent(Service.getFilms(entry.getKey(), searchTextField.getText()));
        }
    }
}
