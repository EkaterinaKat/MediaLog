package org.katyshevtseva.medialog.core.films.web.model;

import lombok.Data;

import java.util.List;

// ответ от эндпоинта /movie/{id}
@Data
public class AdditionalInfoResponse {

    private List<PersonResponse> persons;

    private VideosResponse videos;

    private List<GenreResponse> genres;

    private String description;

    private Integer movieLength;
}
