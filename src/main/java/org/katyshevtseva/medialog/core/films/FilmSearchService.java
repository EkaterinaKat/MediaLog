package org.katyshevtseva.medialog.core.films;

import org.katyshevtseva.medialog.core.films.web.FilmSearchEngine;
import org.katyshevtseva.medialog.core.films.web.model.FilmResponse;

import java.util.List;

public class FilmSearchService {

    public static List<FilmResponse> search(String str) throws Exception {
        return FilmSearchEngine.findByTitle(str).getDocs();
    }
}
