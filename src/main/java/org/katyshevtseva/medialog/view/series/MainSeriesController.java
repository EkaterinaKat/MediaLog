package org.katyshevtseva.medialog.view.series;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.LabelBuilder;
import com.katyshevtseva.fx.Size;
import com.katyshevtseva.fx.Styler;
import com.katyshevtseva.fx.component.ComponentBuilder;
import com.katyshevtseva.fx.component.controller.BlockGridController;
import com.katyshevtseva.fx.dialogconstructor.*;
import com.katyshevtseva.fx.switchcontroller.SectionController;
import org.katyshevtseva.medialog.core.series.SeriesService;
import org.katyshevtseva.medialog.core.series.model.Series;
import org.katyshevtseva.medialog.core.series.model.SeriesGrade;
import org.katyshevtseva.medialog.core.series.model.SeriesState;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.katyshevtseva.fx.FxUtils.frame;
import static com.katyshevtseva.fx.FxUtils.getPaneWithHeight;
import static com.katyshevtseva.fx.Styler.ThingToColor.*;

public class MainSeriesController implements SectionController {
    private final Map<SeriesState, BlockGridController<Series>> seriesGridControllerMap = new HashMap<>();
    @FXML
    private Button newSeriesButton;
    @FXML
    private HBox seriesPane;
    @FXML
    private TextField searchTextField;
    @FXML
    private Button clearButton;
    @FXML
    private ComboBox<SeriesService.AnimeCategory> animeComboBox;

    @FXML
    private void initialize() {
        newSeriesButton.setOnAction(event -> openSeriesEditDialog(null));

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> updateContent());
        clearButton.setOnAction(event -> searchTextField.setText(""));

        FxUtils.setComboBoxItems(animeComboBox, SeriesService.AnimeCategory.values(), SeriesService.AnimeCategory.ALL);
        animeComboBox.setOnAction(event -> updateContent());

        adjustSeriesPane();
        updateContent();
    }

    private void openSeriesEditDialog(Series series) {
        boolean newSeries = series == null;
        DcTextField titleField = new DcTextField(true, newSeries ? "" : series.getTitle());
        DcComboBox<SeriesGrade> gradeDcComboBox = new DcComboBox<>(true, newSeries ? null : series.getGrade(),
                Arrays.asList(SeriesGrade.values()));
        DcComboBox<SeriesState> stateDcComboBox = new DcComboBox<>(true, newSeries ? null : series.getState(),
                Arrays.asList(SeriesState.values()));
        DcTextArea commentField = new DcTextArea(false, newSeries ? "" : series.getComment());
        DcDatePicker startDp = new DcDatePicker(false, newSeries ? null : series.getStartDate());
        DcDatePicker endDp = new DcDatePicker(false, newSeries ? null : series.getFinishDate());
        DcCheckBox animeCheckBox = new DcCheckBox(
                newSeries ? false :series.getIsAnime(),
                "Anime");

        DialogConstructor.constructDialog(() -> {
            SeriesService.save(series, titleField.getValue(), stateDcComboBox.getValue(), gradeDcComboBox.getValue(),
                    commentField.getValue(), startDp.getValue(), endDp.getValue(), animeCheckBox.getValue());
            updateContent();
        }, titleField, stateDcComboBox, gradeDcComboBox, commentField, startDp, endDp, animeCheckBox);
    }

    private void adjustSeriesPane() {
        int gridColumnWidth = 250;
        Size gridColumnSize = new Size(850, gridColumnWidth);
        int seriesBlockWidth = gridColumnWidth - 50;//BlockGridController FRAME_SIZE = 20; 50=20*2+10; 10 на scrollbar

        for (SeriesState state : SeriesState.values()) {
            ComponentBuilder.Component<BlockGridController<Series>> component =
                    new ComponentBuilder().setSize(gridColumnSize).getBlockGridComponent(seriesBlockWidth,
                            null, null, this::getSeriesNode);
            VBox vBox = new VBox();
            vBox.getChildren().addAll(new Label(state.toString()), component.getNode());
            seriesPane.getChildren().add(vBox);
            seriesGridControllerMap.put(state, component.getController());
        }
    }

    private Node getSeriesNode(Series series, int blockWidth) {
        SeriesBlock block = new SeriesBlock(series, blockWidth);
        block.fullNode.setOnContextMenuRequested(event -> showSeriesContextMenu(series, block.fullNode, event));
        return block.fullNode;
    }

    private void showSeriesContextMenu(Series series, Node node, ContextMenuEvent event) {
        ContextMenu menu = new ContextMenu();

        MenuItem editItem = new MenuItem("Edit");
        editItem.setOnAction(event1 -> openSeriesEditDialog(series));
        menu.getItems().add(editItem);

        menu.show(node, event.getScreenX(), event.getScreenY());
    }

    private void updateContent() {
        for (Map.Entry<SeriesState, BlockGridController<Series>> entry : seriesGridControllerMap.entrySet()) {
            entry.getValue().setContent(
                    SeriesService.getSeries(entry.getKey(), searchTextField.getText(), animeComboBox.getValue()));
        }
    }

    private static class SeriesBlock {
        private final VBox contentBox;
        private final Node fullNode;
        private final String SHOW_MORE_STR = "...";
        private final Label titleLabel;
        private final Label detailLabel;
        private final Label showMoreLabel;
        private final boolean hasDetails;
        private boolean detailsOpened = false;

        public SeriesBlock(Series series, int blockWidth) {
            hasDetails = series.hasDetails();
            contentBox = new VBox();
            fullNode = frame(contentBox, 5);

            fullNode.setStyle(fullNode.getStyle() + Styler.getColorfullStyle(BORDER, Styler.StandardColor.BLACK)
                    + Styler.getColorfullStyle(BACKGROUND, series.getGrade().getColor()));

            int labelWidth = blockWidth - 12;//12 на боковые отступы

            titleLabel = new LabelBuilder().text(series.getTitle()).width(labelWidth).setCenterAligment().build();
            titleLabel.setStyle(titleLabel.getStyle() + Styler.getColorfullStyle(TEXT, Styler.StandardColor.BLACK));
            detailLabel = new LabelBuilder().text(series.getFullInfo()).width(labelWidth).setCenterAligment().build();
            showMoreLabel = new LabelBuilder().text(SHOW_MORE_STR).width(labelWidth).setCenterAligment().build();

            if (hasDetails) {
                fullNode.setOnMouseClicked(event -> {
                    detailsOpened = !detailsOpened;
                    fillBox();
                });
            }
            fillBox();
        }

        private void fillBox() {
            contentBox.getChildren().clear();

            if (hasDetails) {
                if (detailsOpened) {
                    fillOpenedDetailsBox();
                } else {
                    fillClosedDetailsBox();
                }
            } else {
                fillNoDetailsBox();
            }
        }

        private void fillNoDetailsBox() {
            contentBox.getChildren().addAll(titleLabel);
        }

        private void fillClosedDetailsBox() {
            contentBox.getChildren().addAll(
                    titleLabel,
                    showMoreLabel);
        }

        private void fillOpenedDetailsBox() {
            contentBox.getChildren().addAll(
                    titleLabel,
                    getPaneWithHeight(10),
                    detailLabel);
        }
    }
}
