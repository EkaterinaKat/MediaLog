package org.katyshevtseva.medialog.view.films;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.Styler;
import com.katyshevtseva.fx.component.ComponentBuilder;
import com.katyshevtseva.fx.component.controller.BlockGridController2;
import com.katyshevtseva.fx.switchcontroller.SectionController;
import com.katyshevtseva.fx.windowbuilder.WindowBuilder;
import org.katyshevtseva.medialog.core.films.PosterFileManager;
import org.katyshevtseva.medialog.core.films.ToWatchService;
import org.katyshevtseva.medialog.core.films.model.Film;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

import static com.katyshevtseva.fx.FxUtils.getPaneWithWidth;
import static com.katyshevtseva.fx.ImageSizeUtil.setImageWidthPreservingRatio;
import static org.katyshevtseva.medialog.view.utils.ViewConstants.FILM_DETAIL_DIALOG;

public class ToWatchController implements SectionController {
    private static final int POSTER_WIDTH = 200;
    private static final int DESC_WIDTH = 350;
    private static final int GRID_WIDTH = 1200;
    @FXML
    private VBox contentPane;

    @Override
    public void update() {
        updateContent();
    }

    private void updateContent() {
        contentPane.getChildren().clear();
        contentPane.getChildren().add(getFilmGridNode(ToWatchService.getFilmsToWatch()));
    }

    private Node getFilmGridNode(List<Film> films) {
        ComponentBuilder.Component<BlockGridController2<Film>> component = new ComponentBuilder()
                .getBlockGridComponent2(POSTER_WIDTH + DESC_WIDTH + 10, GRID_WIDTH, this::getFilmNode);
        component.getController().setContent(films);
        return component.getNode();
    }

    private Node getFilmNode(Film film, int blockWidth) {
        ImageView imageView = new ImageView(PosterFileManager.getPoster(film).getImage());
        setImageWidthPreservingRatio(imageView, POSTER_WIDTH);

        Label descLabel = new Label(film.getToWatchDesc());
        FxUtils.setWidth(descLabel, DESC_WIDTH);
        descLabel.setWrapText(true);

        HBox hBox = new HBox();
        hBox.setOnMouseClicked(event ->
                WindowBuilder.openDialog(FILM_DETAIL_DIALOG, new DetailsController(film, this::updateContent)));
        hBox.setStyle(Styler.getBlackBorderStyle());
        hBox.getChildren().addAll(
                imageView,
                getPaneWithWidth(10),
                descLabel);

        return hBox;
    }
}

