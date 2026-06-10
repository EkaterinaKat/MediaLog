package org.katyshevtseva.medialog.view.books;

import com.katyshevtseva.fx.windowbuilder.FxController;
import com.katyshevtseva.general.NoArgsKnob;
import org.katyshevtseva.medialog.core.books.AuthorService;
import org.katyshevtseva.medialog.core.books.model.Author;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import static com.katyshevtseva.fx.FxUtils.associateButtonWithControls;
import static com.katyshevtseva.fx.FxUtils.closeWindowThatContains;

class AuthorDialogController implements FxController {
    private final Author existing;
    private final NoArgsKnob onSaveListener;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField surnameTextField;
    @FXML
    private Button saveButton;

    AuthorDialogController(Author existing, NoArgsKnob onSaveListener) {
        this.existing = existing;
        this.onSaveListener = onSaveListener;
    }

    @FXML
    private void initialize() {
        associateButtonWithControls(saveButton, nameTextField);
        saveButton.setOnAction(event -> save());
        setExistingPieceInfo();
    }

    private void setExistingPieceInfo() {
        if (existing != null) {
            nameTextField.setText(existing.getName());
            surnameTextField.setText(existing.getSurname());
        }
    }

    private void save() {
        AuthorService.save(existing, nameTextField.getText(), surnameTextField.getText());
        onSaveListener.execute();
        closeWindowThatContains(saveButton);
    }
}
