package org.katyshevtseva.medialog.core.films.model;

import lombok.Getter;

@Getter
public enum PosterState {
    LOADED(null),
    NOT_LOADED("not_loaded.jpg"),
    URL_NOT_FOUND("url_not_found.jpg"),
    ERROR("error.jpg");

    private final String imageName;

    PosterState(String imageName) {
        this.imageName = imageName;
    }
}
