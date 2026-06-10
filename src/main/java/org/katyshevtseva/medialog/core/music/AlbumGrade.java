package org.katyshevtseva.medialog.core.music;

import static org.katyshevtseva.medialog.core.CoreConstants.*;

public enum AlbumGrade {
    NICE(HIGHEST_GRADE_COLOR), OK(AVERAGE_GRADE_COLOR), SOSO(GRAY);

    private final String color;

    AlbumGrade(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}
