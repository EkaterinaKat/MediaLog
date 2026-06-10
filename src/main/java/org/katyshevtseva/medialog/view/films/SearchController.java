package org.katyshevtseva.medialog.view.films;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.component.ComponentBuilder;
import com.katyshevtseva.fx.component.controller.BlockGridController2;
import com.katyshevtseva.fx.dialog.StandardDialogBuilder;
import com.katyshevtseva.fx.switchcontroller.SectionController;
import com.katyshevtseva.general.GeneralUtils;
import org.katyshevtseva.medialog.core.films.FilmSearchService;
import org.katyshevtseva.medialog.core.films.ToWatchService;
import org.katyshevtseva.medialog.core.films.web.model.FilmResponse;
import org.katyshevtseva.medialog.core.films.web.model.PosterResponse;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

import java.util.List;

import static com.katyshevtseva.fx.FxUtils.getPaneWithHeight;
import static com.katyshevtseva.fx.ImageSizeUtil.setImageWidthPreservingRatio;
import static com.katyshevtseva.general.GeneralUtils.isEmpty;

public class SearchController implements SectionController {
    private static final int POSTER_WIDTH = 200;
    private static final int GRID_WIDTH = 1180;
    @FXML
    private Button searchButton;
    @FXML
    private TextField searchTextField;
    @FXML
    private Pane contentPane;

    @FXML
    private void initialize() {
        searchTextField.setOnAction(event -> updateContent());
        searchButton.setOnAction(event -> updateContent());
    }

    private void updateContent() {
        if (isEmpty(searchTextField.getText())) {
            return;
        }
        contentPane.getChildren().clear();

        try {
            List<FilmResponse> films = FilmSearchService.search(searchTextField.getText());
            contentPane.getChildren().add(getFilmGridNode(films));
        } catch (Exception e) {
            Label label = new Label(e.getMessage());
            label.setWrapText(true);
            FxUtils.setWidth(label, 800);
            contentPane.getChildren().add(label);
        }
    }

    private Node getFilmGridNode(List<FilmResponse> films) {
        if (GeneralUtils.isEmpty(films)) {
            return new Label("Nothing found");
        } else {
            ComponentBuilder.Component<BlockGridController2<FilmResponse>> component =
                    new ComponentBuilder().getBlockGridComponent2(POSTER_WIDTH, GRID_WIDTH, this::getFilmNode);
            component.getController().setContent(films);
            return component.getNode();
        }
    }

    private Node getFilmNode(FilmResponse film, int blockWidth) {
        Label nameLabel = new Label(film.getTitleAndYear());
        FxUtils.setWidth(nameLabel, blockWidth);
        FxUtils.setHeight(nameLabel, 50);
        nameLabel.setWrapText(true);
        nameLabel.setTextAlignment(TextAlignment.CENTER);
        nameLabel.setAlignment(Pos.BASELINE_CENTER);

        VBox vBox = new VBox();
        vBox.getChildren().addAll(
                getPaneWithHeight(10),
                nameLabel,
                getPaneWithHeight(10));

        PosterResponse poster = film.getPoster();
        if (poster != null && poster.getUrl() != null) {
            ImageView imageView = new ImageView(new Image(poster.getUrl()));
            setImageWidthPreservingRatio(imageView, blockWidth);
            vBox.getChildren().addAll(imageView, getPaneWithHeight(10));
        }

        HBox hBox = new HBox();
        hBox.getChildren().add(vBox);
        hBox.setOnContextMenuRequested(event -> showContextMenu(hBox, event, film));

        return hBox;
    }

    private void showContextMenu(Node node, ContextMenuEvent event, FilmResponse film) {
        ContextMenu menu = new ContextMenu();

        MenuItem item = new MenuItem("Want to watch");
        item.setOnAction(event1 -> saveToWatchFilm(film));
        menu.getItems().add(item);

        menu.show(node, event.getScreenX(), event.getScreenY());
    }

    private void saveToWatchFilm(FilmResponse film) {
        try {
            ToWatchService.saveToWatchFilm(film);
        } catch (Exception e) {
            new StandardDialogBuilder().setSize(700).openInfoDialog("Ашипка! " + e);
        }
    }
}
