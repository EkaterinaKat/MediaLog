package org.katyshevtseva.medialog.core.films;

import com.katyshevtseva.general.GeneralUtils;
import org.katyshevtseva.medialog.core.Dao;
import org.katyshevtseva.medialog.core.films.model.Film;
import org.katyshevtseva.medialog.core.films.model.FilmGrade;
import org.katyshevtseva.medialog.core.films.model.Role;
import org.katyshevtseva.medialog.core.films.model.Trailer;

import java.util.ArrayList;
import java.util.Date;

import static org.katyshevtseva.medialog.core.films.model.FilmStatus.*;

public class StatusService {

    public static boolean isToWatch(Film film) {
        return film.getStatus() == TO_WATCH || film.getStatus() == WATCHED_AND_TO_WATCH;
    }

    public static void deleteFromToWatch(Film film) {
        switch (film.getStatus()) {
            case WATCHED:
                throw new RuntimeException("Try to delete watched film from to watch list");
            case TO_WATCH:
                deleteFilm(film);
                break;
            case WATCHED_AND_TO_WATCH:
                film.setStatus(WATCHED);
                film.setToWatchAddingDate(null);
                Dao.saveEdited(film);
                break;
        }
    }

    private static void deleteFilm(Film film) {
        if (!GeneralUtils.isEmpty(film.getRoles())) {
            for (Role role : film.getRoles()) {
                Dao.delete(role);
            }
        }
        for (Trailer trailer : Dao.findTrailers(film)) {
            Dao.delete(trailer);
        }
        Dao.delete(film);
    }

    public static void wantToWatchFilm(Film film) {
        if (film.getStatus() == WATCHED) {
            film.setStatus(WATCHED_AND_TO_WATCH);
            film.setToWatchAddingDate(new Date());
            Dao.saveEdited(film);
        }
    }

    public static void watched(Film film, FilmGrade grade, Date date) {
        film.setGrade(grade);
        if (film.getDates() == null) {
            film.setDates(new ArrayList<>());
        }
        film.getDates().add(date);
        film.setToWatchAddingDate(null);
        film.setStatus(WATCHED);
        Dao.saveEdited(film);
    }
}
