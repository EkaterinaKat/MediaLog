package org.katyshevtseva.medialog.core.films.model;

public enum Type {
    CARTOON(false), MOVIE(true), ANIME(false), TV_SERIES(true);

    public final boolean hasActors;

    Type(boolean hasActors) {
        this.hasActors = hasActors;
    }

    public static Type getByResponseString(String s) {
        if (s.equals("movie")) {
            return MOVIE;
        }
        if (s.equals("cartoon")) {
            return CARTOON;
        }
        if (s.equals("anime")) {
            return ANIME;
        }
        if (s.equals("tv-series")) {
            return TV_SERIES;
        }
        throw new RuntimeException("Неизвестный тип фильма: " + s);
    }
}
