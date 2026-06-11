package org.katyshevtseva.medialog.view.films;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.Size;
import com.katyshevtseva.fx.Styler;
import com.katyshevtseva.fx.component.ComponentBuilder;
import com.katyshevtseva.fx.component.controller.BlockGridController;
import com.katyshevtseva.fx.dialog.StandardDialogBuilder;
import com.katyshevtseva.fx.windowbuilder.FxController;
import com.katyshevtseva.fx.windowbuilder.WindowBuilder;
import com.katyshevtseva.general.GeneralUtils;
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
import org.katyshevtseva.medialog.core.films.ActorFileManager;
import org.katyshevtseva.medialog.core.films.model.Film;
import org.katyshevtseva.medialog.core.films.model.Role;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.katyshevtseva.fx.FxUtils.getPaneWithHeight;
import static com.katyshevtseva.fx.FxUtils.getPaneWithWidth;
import static com.katyshevtseva.fx.ImageSizeUtil.setImageWidthPreservingRatio;
import static org.katyshevtseva.medialog.view.utils.ViewConstants.ACTOR_FILMS_DIALOG;

@RequiredArgsConstructor
public class ActorsController implements FxController {
    private static final Size GRID_SIZE = new Size(720, 768);
    private static final int BLOCK_WIDTH = 152;
    private final Film film;
    private BlockGridController<Role> actorGridController;
    @FXML
    private Pane contentPane;

    @FXML
    private void initialize() {
        adjustBlockListController();
        updateContent();
    }

    private void adjustBlockListController() {
        ComponentBuilder.Component<BlockGridController<Role>> component =
                new ComponentBuilder().setSize(GRID_SIZE).getBlockGridComponent(BLOCK_WIDTH,
                        null, null, this::getRoleNode);
        contentPane.getChildren().add(component.getNode());
        actorGridController = component.getController();
    }

    private Node getRoleNode(Role role, int blockWidth) {
        Label nameLabel = new Label(role.getNameAndDescNonNull());
        FxUtils.setWidth(nameLabel, blockWidth);
        nameLabel.setWrapText(true);
        nameLabel.setAlignment(Pos.BASELINE_CENTER);

        VBox vBox = new VBox();
        vBox.getChildren().addAll(
                getPaneWithHeight(8),
                nameLabel,
                getPaneWithHeight(8));

        if (role.getActor().getHasLoadedPhoto()) {
            ImageView imageView = new ImageView(ActorFileManager.getActorPhoto(role.getActor()).getImage());
            setImageWidthPreservingRatio(imageView, blockWidth);
            vBox.getChildren().addAll(imageView, getPaneWithHeight(8));
        }

        HBox hBox = new HBox();
        hBox.getChildren().addAll(getPaneWithWidth(8), vBox, getPaneWithWidth(8));
        hBox.setStyle(Styler.getBlackBorderStyle());

        hBox.setOnMouseClicked(event ->
                WindowBuilder.openDialog(ACTOR_FILMS_DIALOG, new ActorFilmsController(role.getActor())));

        return hBox;
    }

    private void updateContent() {
        List<Role> roles = Dao.findRoles(film);
        if (GeneralUtils.isEmpty(roles)) {
            new StandardDialogBuilder().openInfoDialog("Актёров нет...");
            return;
        }

        Comparator<Role> comparator = Comparator
                .comparing(Role::actorDoesntHavePhoto)
                .thenComparing(Role::descriptionIsEmpty);

        List<Role> actors = roles.stream()
                .peek(role -> role.getActor().setHasLoadedPhoto(ActorFileManager.actorHasPhoto(role.getActor())))
                .sorted(comparator)
                .collect(Collectors.toList());

        actorGridController.setContent(actors);
    }
}
