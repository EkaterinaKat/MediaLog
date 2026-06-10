package org.katyshevtseva.medialog.core.films.web.model;

import lombok.Data;

@Data
public class PersonResponse {
    private Long id;
    private String photo;
    private String name;
    private String enName;
    private String profession;
    private String description;
}
