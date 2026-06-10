package org.katyshevtseva.medialog.core.films;

import com.katyshevtseva.date.DateUtils;
import org.katyshevtseva.medialog.core.Dao;
import org.katyshevtseva.medialog.core.films.model.Film;
import org.katyshevtseva.medialog.core.films.model.FilmGrade;
import org.katyshevtseva.medialog.core.films.model.PosterState;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.katyshevtseva.medialog.core.CoreConstants.fictitiousFirstViewDate;

public class Service {

    public static List<Film> getFilms(FilmGrade grade, String searchString) {
        return Dao.findFilms(grade, searchString)
                .stream().sorted(Comparator.comparing(Film::getTitle)).collect(Collectors.toList());
    }

    public static String getDateString(Date date) {
        if (date.equals(fictitiousFirstViewDate)) {
            return "**.**.****";
        } else {
            return DateUtils.READABLE_DATE_FORMAT.format(date);
        }
    }

    public static void updatePosterState(Film film, PosterState posterState) {
        film.setPosterState(posterState);
        Dao.saveEdited(film);
    }
}
