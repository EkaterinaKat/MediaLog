package org.katyshevtseva.medialog.core.films.web.model;

import lombok.Data;

import java.util.List;

//ответ из эндпоинта /movie/search
@Data
public class FilmResponse {
    private Long id;
    private String name;
    private int year;
    private PosterResponse poster;
    private String description;
    private List<GenreResponse> genres;
    private Integer movieLength;
    private String type;

    public String getTitleAndYear() {
        return name + " " + year;
    }
}
