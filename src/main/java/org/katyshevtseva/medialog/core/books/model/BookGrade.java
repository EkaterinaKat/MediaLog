package org.katyshevtseva.medialog.core.books.model;

import static org.katyshevtseva.medialog.core.CoreConstants.AVERAGE_GRADE_COLOR;
import static org.katyshevtseva.medialog.core.CoreConstants.HIGHEST_GRADE_COLOR;

public enum BookGrade {
    NOT_OK("#FD5E53"), OK("#FFFFFF"), NICE(AVERAGE_GRADE_COLOR), FAVOURITE(HIGHEST_GRADE_COLOR);

    private final String color;

    BookGrade(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}
