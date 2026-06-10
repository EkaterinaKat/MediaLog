package org.katyshevtseva.medialog.core.films.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Trailer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;

    private String name;

    private String site;

    private String type;

    @ManyToOne
    @JoinColumn(name = "film_id")
    private Film film;

    @Override
    public String toString() {
        return name;
    }

    public String getLabelInfo() {
        return "\uD83D\uDDC7" + type + ": " + name + " (" + site + ")";
    }
}
