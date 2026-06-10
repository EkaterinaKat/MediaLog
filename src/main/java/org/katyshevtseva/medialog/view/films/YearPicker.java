package org.katyshevtseva.medialog.view.films;

import com.katyshevtseva.general.OneArgKnob;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.List;

import static org.katyshevtseva.medialog.core.films.ViewingHistoryService.getIndexOfCurrentYear;
import static org.katyshevtseva.medialog.core.films.ViewingHistoryService.getYearsForDatePicker;

public class YearPicker {
    private final Button leftArrow;
    private final Label yearLabel;
    private final Button rightArrow;
    private final OneArgKnob<Integer> onYearSelectListener;

    List<Integer> years;
    private Integer selectedYearIndex;

    public YearPicker(Button leftArrow, Label yearLabel, Button rightArrow, OneArgKnob<Integer> onYearSelectListener) {
        this.leftArrow = leftArrow;
        this.yearLabel = yearLabel;
        this.rightArrow = rightArrow;
        this.onYearSelectListener = onYearSelectListener;
        tunePicker();
    }

    private void tunePicker() {
        years = getYearsForDatePicker();
        selectYear(getIndexOfCurrentYear(years));

        leftArrow.setOnMouseClicked(event -> selectYear(selectedYearIndex - 1));
        rightArrow.setOnMouseClicked(event -> selectYear(selectedYearIndex + 1));
    }

    private void selectYear(Integer index) {
        selectedYearIndex = index;
        Integer selectedYear = years.get(selectedYearIndex);
        yearLabel.setText(selectedYear.toString());
        onYearSelectListener.execute(selectedYear);
        leftArrow.setDisable(selectedYearIndex == 0);
        rightArrow.setDisable(selectedYearIndex == years.size() - 1);
    }
}
