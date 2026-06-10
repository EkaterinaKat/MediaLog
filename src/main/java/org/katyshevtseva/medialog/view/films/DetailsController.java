package org.katyshevtseva.medialog.view.films;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.LabelBuilder;
import com.katyshevtseva.fx.Styler;
import com.katyshevtseva.fx.windowbuilder.FxController;
import com.katyshevtseva.fx.windowbuilder.WindowBuilder;
import com.katyshevtseva.general.GeneralUtils;
import com.katyshevtseva.general.NoArgsKnob;
import org.katyshevtseva.medialog.core.Dao;
import org.katyshevtseva.medialog.core.films.PosterFileManager;
import org.katyshevtseva.medialog.core.films.model.Film;
import org.katyshevtseva.medialog.core.films.model.Trailer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.katyshevtseva.fx.ImageSizeUtil.setImageWidthPreservingRatio;
import static org.katyshevtseva.medialog.view.utils.ViewConstants.ACTORS_DIALOG;

@RequiredArgsConstructor
public class DetailsController implements FxController {
    private final Film film;
    private final NoArgsKnob onUpdateDataListener;
    @FXML
    private Label titleLabel;
    @FXML
    private ImageView imageView;
    @FXML
    private Label detailsLabel;
    @FXML
    private HBox buttonBox;
    @FXML
    private Button actorsButton;
    @FXML
    private VBox trailerBox;

    @FXML
    private void initialize() {
        imageView.setImage(PosterFileManager.getPoster(film).getImage());
        setImageWidthPreservingRatio(imageView, 240);
        titleLabel.setText(film.getTitleAndYear());
        detailsLabel.setText(film.getDetailsString());
        detailsLabel.setWrapText(true);
        detailsLabel.setMaxWidth(480);
        FilmOperationsHelper.fillButtonBox(film, buttonBox, () -> {
            onUpdateDataListener.execute();
            FxUtils.closeWindowThatContains(titleLabel);
        });
        if (film.getType().hasActors) {
            actorsButton.setOnMouseClicked(event -> WindowBuilder.openDialog(ACTORS_DIALOG, new ActorsController(film)));
        } else {
            actorsButton.setVisible(false);
        }
        showTrailers();
    }

    private void showTrailers() {
        List<Trailer> trailers = Dao.findTrailers(film);
        if (trailers.size() > 5) {
            trailers = trailers.subList(0, 5);
        }
        for (Trailer trailer : trailers) {
            Label label = new LabelBuilder().text(trailer.getLabelInfo()).width(480).build();
            label.setOnMouseClicked(event -> GeneralUtils.saveToClipBoard(trailer.getUrl()));

            Styler.setHoverStyle(
                    label,
                    Styler.getColorfullStyle(Styler.ThingToColor.TEXT, "#EF47FF"));

            trailerBox.getChildren().addAll(label, FxUtils.getPaneWithHeight(12));
        }
    }
}
