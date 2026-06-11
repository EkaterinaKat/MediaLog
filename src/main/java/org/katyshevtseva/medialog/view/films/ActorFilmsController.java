package org.katyshevtseva.medialog.view.films;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.Size;
import com.katyshevtseva.fx.Styler;
import com.katyshevtseva.fx.component.ComponentBuilder;
import com.katyshevtseva.fx.component.controller.BlockGridController;
import com.katyshevtseva.fx.windowbuilder.FxController;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;
import org.katyshevtseva.medialog.core.Dao;
import org.katyshevtseva.medialog.core.films.PosterFileManager;
import org.katyshevtseva.medialog.core.films.model.Actor;
import org.katyshevtseva.medialog.core.films.model.Film;
import org.katyshevtseva.medialog.core.films.model.PosterState;
import org.katyshevtseva.medialog.core.films.model.Role;

import java.util.List;
import java.util.stream.Collectors;

import static com.katyshevtseva.fx.FxUtils.getPaneWithHeight;
import static com.katyshevtseva.fx.FxUtils.getPaneWithWidth;
import static com.katyshevtseva.fx.ImageSizeUtil.setImageWidthPreservingRatio;

@RequiredArgsConstructor
public class ActorFilmsController implements FxController {
    private static final Size GRID_SIZE = new Size(720, 768);
    private static final int BLOCK_WIDTH = 152;
    private final Actor actor;
    private BlockGridController<Film> filmGridController;
    @FXML
    private Pane contentPane;

    @FXML
    private void initialize() {
        adjustBlockListController();
        updateContent();
    }

    private void adjustBlockListController() {
        ComponentBuilder.Component<BlockGridController<Film>> component =
                new ComponentBuilder().setSize(GRID_SIZE).getBlockGridComponent(BLOCK_WIDTH,
                        null, null, this::getFilmNode);
        contentPane.getChildren().add(component.getNode());
        filmGridController = component.getController();
    }

    private Node getFilmNode(Film film, int blockWidth) {
        Label nameLabel = new Label(film.getTitleAndYear());
        FxUtils.setWidth(nameLabel, blockWidth);
        nameLabel.setWrapText(true);
        nameLabel.setAlignment(Pos.BASELINE_CENTER);

        VBox vBox = new VBox();
        vBox.getChildren().addAll(
                getPaneWithHeight(8),
                nameLabel,
                getPaneWithHeight(8));

        if (film.getPosterState() == PosterState.LOADED) {
            ImageView imageView = new ImageView(PosterFileManager.getPoster(film).getImage());
            setImageWidthPreservingRatio(imageView, blockWidth);
            vBox.getChildren().addAll(imageView, getPaneWithHeight(8));
        }

        HBox hBox = new HBox();
        hBox.getChildren().addAll(getPaneWithWidth(8), vBox, getPaneWithWidth(8));
        hBox.setStyle(Styler.getBlackBorderStyle());
        return hBox;
    }

    private void updateContent() {
        List<Film> films = Dao.findRoles(actor)
                .stream()
                .map(Role::getFilm)
                .collect(Collectors.toList());

        filmGridController.setContent(films);
    }
}
