package org.katyshevtseva.medialog.core.series;

import org.katyshevtseva.medialog.core.Dao;
import org.katyshevtseva.medialog.core.series.model.Series;
import org.katyshevtseva.medialog.core.series.model.SeriesGrade;
import org.katyshevtseva.medialog.core.series.model.SeriesState;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class SeriesService {

    public static void save(Series existing, String title, SeriesState state, SeriesGrade grade,
                            String comment, Date start, Date end, boolean isAnime) {
        title = title.trim();
        comment = comment.trim();

        if (existing == null) {
            existing = new Series();
            existing.setValues(title, state, grade, comment, start, end, isAnime);
            Dao.saveNew(existing);
        }
        existing.setValues(title, state, grade, comment, start, end, isAnime);
        Dao.saveEdited(existing);
    }

    public static List<Series> getSeries(SeriesState state, String searchString, AnimeCategory animeCategory) {
        return Dao.findSeries(state, searchString)
                .stream()
                .filter(series -> {
                    switch (animeCategory) {
                        case ALL:
                            return true;
                        case NOT_ANIME:
                            return !series.getIsAnime();
                        case ANIME:
                            return series.getIsAnime();
                    }
                    throw new RuntimeException();
                })
                .sorted(Comparator.comparing(Series::getTitle))
                .collect(Collectors.toList());
    }

    public enum AnimeCategory {
        ANIME, NOT_ANIME, ALL
    }
}
