package org.katyshevtseva.medialog.core;

import com.katyshevtseva.date.DateUtils;

import java.text.ParseException;
import java.util.Date;

public class CoreConstants {
    public static final String APP_NAME = "Kiki Notebook";
    public static final String HIGHEST_GRADE_COLOR = "#3DF0EF";
    public static final String AVERAGE_GRADE_COLOR = "#B2EC5D";
    public static final String GRAY = "#7A7A7A";

    public static final Date fictitiousFirstViewDate;

    static {
        try {
            fictitiousFirstViewDate = DateUtils.READABLE_DATE_FORMAT.parse("01.01.2000");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
