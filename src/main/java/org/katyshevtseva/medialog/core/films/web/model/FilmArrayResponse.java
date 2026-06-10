package org.katyshevtseva.medialog.core.films.web.model;

import lombok.Data;

import java.util.List;

@Data
public class FilmArrayResponse {
    private List<FilmResponse> docs;
}
