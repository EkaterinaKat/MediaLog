package org.katyshevtseva.medialog.core.series.model;

import static org.katyshevtseva.medialog.core.CoreConstants.*;

public enum SeriesGrade {
    FAVOURITE(HIGHEST_GRADE_COLOR),
    NICE("#52E552"),
    OK(AVERAGE_GRADE_COLOR),
    SOSO("#D0B453"),
    DISLIKED(GRAY);

    private final String color;

    SeriesGrade(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}
