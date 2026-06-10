package org.katyshevtseva.medialog.view.music;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.Size;
import com.katyshevtseva.fx.component.ComponentBuilder;
import com.katyshevtseva.fx.component.controller.MultipleChoiceController;
import com.katyshevtseva.fx.dialogconstructor.DcTextField;
import com.katyshevtseva.fx.dialogconstructor.DialogConstructor;
import com.katyshevtseva.fx.windowbuilder.FxController;
import com.katyshevtseva.general.NoArgsKnob;
import org.katyshevtseva.medialog.core.music.AlbumGrade;
import org.katyshevtseva.medialog.core.music.MusicService;
import org.katyshevtseva.medialog.core.music.entity.Album;
import org.katyshevtseva.medialog.core.music.entity.Genre;
import org.katyshevtseva.medialog.core.music.entity.Singer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.List;

import static com.katyshevtseva.fx.FxUtils.*;
import static com.katyshevtseva.general.GeneralUtils.isEmpty;

public class AlbumDialogController implements FxController {
    private final Album existing;
    private final NoArgsKnob onSaveKnob;
    private MultipleChoiceController<Genre> genreChoiceController;
    @FXML
    private ImageView imageView;
    @FXML
    private TextField imageTextField;
    @FXML
    private TextField titleTextField;
    @FXML
    private TextArea commentTextArea;
    @FXML
    private TextField yearTextField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox<Singer> singerComboBox;
    @FXML
    private Button addSingerButton;
    @FXML
    private Pane genrePane;
    @FXML
    private Button addGenreButton;
    @FXML
    private Button saveButton;
    @FXML
    private ComboBox<AlbumGrade> gradeComboBox;
    @FXML
    private TextField tracksTextField;
    @FXML
    private TextField durationTextField;

    public AlbumDialogController(Album existing, NoArgsKnob onSaveKnob) {
        this.existing = existing;
        this.onSaveKnob = onSaveKnob;
    }

    @FXML
    private void initialize() {
        adjustGenreSection();
        adjustSingerSection();
        adjustImageSection();
        setComboBoxItems(gradeComboBox, AlbumGrade.values());
        disableNonNumericChars(yearTextField);
        disableNonNumericChars(tracksTextField);
        disableNonNumericChars(durationTextField);
        associateButtonWithControls(saveButton, titleTextField, singerComboBox, datePicker, gradeComboBox);
        saveButton.setOnAction(event -> saveButtonListener());
        setExistingValues();
    }

    private void setExistingValues() {
        if (existing != null) {
            imageTextField.setText(existing.getImageName());
            titleTextField.setText(existing.getTitle());
            commentTextArea.setText(existing.getComment());
            yearTextField.setText(existing.getYear() + "");
            FxUtils.setDate(datePicker, existing.getListeningDate());
            gradeComboBox.setValue(existing.getGrade());
            tracksTextField.setText(existing.getNumOfTracks() + "");
            durationTextField.setText(existing.getDuration() + "");
            singerComboBox.setValue(existing.getSinger());
        }
    }

    private void adjustImageSection() {
        imageView.setImage(AlbumImageUtils.getImageContainer(existing).getImage());
        imageTextField.setOnAction(event -> imageView.setImage(AlbumImageUtils.getImageContainer(imageTextField.getText()).getImage()));
    }

    private void adjustSingerSection() {
        FxUtils.setComboBoxItems(singerComboBox, MusicService.getSingers());
        addSingerButton.setOnAction(event -> {
            DcTextField textField = new DcTextField(true, "");
            DialogConstructor.constructDialog(() -> {
                MusicService.saveSinger(null, textField.getValue());
                FxUtils.setComboBoxItems(singerComboBox, MusicService.getSingers());
            }, textField);
        });
    }

    private void adjustGenreSection() {
        adjustGenreChoicePane();
        addGenreButton.setOnAction(event -> {
            DcTextField textField = new DcTextField(true, "");
            DialogConstructor.constructDialog(() -> {
                MusicService.saveGenre(null, textField.getValue());
                adjustGenreChoicePane();
            }, textField);
        });
    }

    private void adjustGenreChoicePane() {
        List<Genre> selectedGenres = existing == null ? null : existing.getGenres();

        genrePane.getChildren().clear();
        ComponentBuilder.Component<MultipleChoiceController<Genre>> toComponent = new ComponentBuilder()
                .setSize(new Size(120, 192))
                .getMultipleChoiceComponent(MusicService.getGenres(), selectedGenres, false);
        genreChoiceController = toComponent.getController();
        genrePane.getChildren().add(toComponent.getNode());
    }

    private void saveButtonListener() {
        MusicService.saveAlbum(
                existing,
                titleTextField.getText(),
                commentTextArea.getText(),
                imageTextField.getText(),
                isEmpty(yearTextField.getText()) ? null : Integer.parseInt(yearTextField.getText()),
                FxUtils.getDate(datePicker),
                singerComboBox.getValue(),
                genreChoiceController.getSelectedItems(),
                isEmpty(tracksTextField.getText()) ? null : Integer.parseInt(tracksTextField.getText()),
                isEmpty(durationTextField.getText()) ? null : Integer.parseInt(durationTextField.getText()),
                gradeComboBox.getValue()
        );

        onSaveKnob.execute();
        FxUtils.closeWindowThatContains(saveButton);
    }
}
