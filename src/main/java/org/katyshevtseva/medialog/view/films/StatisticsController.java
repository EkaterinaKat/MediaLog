package org.katyshevtseva.medialog.view.films;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.Size;
import com.katyshevtseva.fx.switchcontroller.SectionController;
import org.katyshevtseva.medialog.core.films.StatisticsService;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.util.Map;

public class StatisticsController implements SectionController {
    private BarChart<String, Number> chart;
    @FXML
    private VBox root;

    @FXML
    private void initialize() {
        adjustChart();
        fillChart();
    }

    private void adjustChart() {
        final CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Year");
        final NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Film count");
        yAxis.setTickLabelFormatter(new IntegerStringConverter());
        chart = new BarChart<>(xAxis, yAxis);
        chart.setBarGap(0.1);
        chart.setMinHeight(520);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(chart);
        FxUtils.setSize(scrollPane, new Size(560, 1040));
        root.getChildren().add(scrollPane);
    }

    private void fillChart() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        Map<Integer, Integer> yearFilmCountMap = StatisticsService.getYearFilmCountMap();
        for (Map.Entry<Integer, Integer> entry : yearFilmCountMap.entrySet()) {
            series.getData().add(new XYChart.Data<>("" + entry.getKey(), entry.getValue()));
        }
        chart.getData().add(series);
        int chartWidth = yearFilmCountMap.size() * 24;
        chart.setMinWidth(chartWidth);
    }

    static class IntegerStringConverter extends StringConverter<Number> {

        public IntegerStringConverter() {
        }

        @Override
        public String toString(Number object) {
            if (object.intValue() != object.doubleValue())
                return "";
            return "" + (object.intValue());
        }

        @Override
        public Number fromString(String string) {
            Number val = Double.parseDouble(string);
            return val.intValue();
        }
    }
}
