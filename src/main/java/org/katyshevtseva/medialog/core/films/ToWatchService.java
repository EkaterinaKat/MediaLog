package org.katyshevtseva.medialog.core.films;

import org.katyshevtseva.medialog.core.Dao;
import org.katyshevtseva.medialog.core.films.model.Film;
import org.katyshevtseva.medialog.core.films.model.FilmGenre;
import org.katyshevtseva.medialog.core.films.model.PosterState;
import org.katyshevtseva.medialog.core.films.model.Type;
import org.katyshevtseva.medialog.core.films.web.PosterLoader;
import org.katyshevtseva.medialog.core.films.web.model.FilmResponse;
import org.katyshevtseva.medialog.core.films.web.model.GenreResponse;

import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.katyshevtseva.medialog.core.films.model.FilmStatus.TO_WATCH;

public class ToWatchService {

    public static List<Film> getFilmsToWatch() {
        return Dao.getAllFilms()
                .stream()
                .filter(StatusService::isToWatch)
                .sorted(Comparator.comparing(Film::getToWatchAddingDate).reversed())
                .collect(Collectors.toList());
    }

    public static void saveToWatchFilm(FilmResponse response) throws Exception {
        Film existing = Dao.findFilmByKpId(response.getId());
        if (existing != null) {
            StatusService.wantToWatchFilm(existing);
        } else {
            Set<FilmGenre> genres = convertResponseGenresToEntity(response.getGenres());
            Film film = new Film(
                    null,
                    response.getId(),
                    response.getPoster().getUrl(),
                    response.getName(),
                    response.getYear(),
                    null,
                    TO_WATCH,
                    null,
                    PosterState.NOT_LOADED,
                    response.getDescription(),
                    genres,
                    response.getMovieLength(),
                    new Date(),
                    Type.getByResponseString(response.getType()),
                    false,
                    0,
                    0,
                    null
            );
            Film savedFilm = Dao.saveNewFilm(film);
            PosterLoader.loadPosterBySavedUrl(savedFilm);
            AdditionalInfoService.loadAdditionalInfo(film);
        }
    }

    public static Set<FilmGenre> convertResponseGenresToEntity(List<GenreResponse> genreResponses) {
        Set<FilmGenre> result = new HashSet<>();
        for (GenreResponse response : genreResponses) {
            FilmGenre genre = Dao.findFilmGenreByTitle(response.getName());
            if (genre == null) {
                genre = Dao.saveNewGenre(new FilmGenre(response.getName()));
            }
            result.add(genre);
        }
        if (result.size() != genreResponses.size()) {
            throw new RuntimeException("Что-то пошло не так с жанрами");
        }
        return result;
    }
}
