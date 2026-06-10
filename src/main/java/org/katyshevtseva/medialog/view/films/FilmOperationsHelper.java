package org.katyshevtseva.medialog.view.films;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.dialog.StandardDialogBuilder;
import com.katyshevtseva.fx.dialogconstructor.DcComboBox;
import com.katyshevtseva.fx.dialogconstructor.DcDatePicker;
import com.katyshevtseva.fx.dialogconstructor.DialogConstructor;
import com.katyshevtseva.general.NoArgsKnob;
import org.katyshevtseva.medialog.core.films.StatusService;
import org.katyshevtseva.medialog.core.films.model.Film;
import org.katyshevtseva.medialog.core.films.model.FilmGrade;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

import java.util.Arrays;
import java.util.Date;

public class FilmOperationsHelper {

    static void fillButtonBox(Film film, HBox buttonBox, NoArgsKnob onUpdateDataListener) {
        switch (film.getStatus()) {
            case WATCHED:
                buttonBox.getChildren().addAll(
                        getWantToWatchButton(film),
                        FxUtils.getPaneWithWidth(10),
                        getWatchedButton(film, onUpdateDataListener)
                );
                break;
            case TO_WATCH:
            case WATCHED_AND_TO_WATCH:
                buttonBox.getChildren().addAll(
                        getDeleteFromToWatchButton(film, onUpdateDataListener),
                        FxUtils.getPaneWithWidth(10),
                        getWatchedButton(film, onUpdateDataListener)
                );
        }
    }

    private static Button getWatchedButton(Film film, NoArgsKnob onUpdateDataListener) {
        Button button = new Button("Watched");
        button.setOnMouseClicked(event -> openWatchResultDialog(film, onUpdateDataListener));
        return button;
    }

    private static Button getWantToWatchButton(Film film) {
        Button button = new Button("Want to watch");
        button.setOnMouseClicked(event -> StatusService.wantToWatchFilm(film));
        return button;
    }

    private static Button getDeleteFromToWatchButton(Film film, NoArgsKnob onDeleteListener) {
        Button button = new Button("Delete from to watch");
        button.setOnMouseClicked(event ->
                new StandardDialogBuilder().openQuestionDialog("Delete?", b -> {
                    if (b) {
                        StatusService.deleteFromToWatch(film);
                        onDeleteListener.execute();
                    }
                }));
        return button;
    }

    static void openWatchResultDialog(Film film, NoArgsKnob onUpdateDataListener) {
        DcComboBox<FilmGrade> gradeDcComboBox = new DcComboBox<>(
                true, null, Arrays.asList(FilmGrade.values()));
        DcDatePicker datePicker = new DcDatePicker(true, new Date());

        DialogConstructor.constructDialog(() -> {
            StatusService.watched(film, gradeDcComboBox.getValue(), datePicker.getValue());
            onUpdateDataListener.execute();
        }, gradeDcComboBox, datePicker);
    }
}
