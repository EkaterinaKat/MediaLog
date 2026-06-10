package org.katyshevtseva.medialog.core.films.web;

import org.katyshevtseva.medialog.core.films.model.Film;
import org.katyshevtseva.medialog.core.films.model.PosterState;
import com.katyshevtseva.web.ImageDownloader;

import static org.katyshevtseva.medialog.core.films.PosterFileManager.FILM_IMAGE_LOCATION;
import static org.katyshevtseva.medialog.core.films.PosterFileManager.formImageFileName;
import static org.katyshevtseva.medialog.core.films.Service.updatePosterState;
import static org.katyshevtseva.medialog.core.films.model.PosterState.*;

public class PosterLoader {

    public static void loadPosterBySavedUrl(Film film) {
        if (film.getPosterState() == PosterState.LOADED) {
            return;
        }
        if (film.getPosterUrl() == null) {
            updatePosterState(film, URL_NOT_FOUND);
            return;
        }

        try {
            ImageDownloader.download(FILM_IMAGE_LOCATION, formImageFileName(film), film.getPosterUrl());
            updatePosterState(film, LOADED);
        } catch (Exception e) {
            updatePosterState(film, ERROR);
        }
    }
}
