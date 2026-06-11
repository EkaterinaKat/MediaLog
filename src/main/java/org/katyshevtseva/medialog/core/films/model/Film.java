package org.katyshevtseva.medialog.core.films.model;

import com.katyshevtseva.general.GeneralUtils;
import com.katyshevtseva.hibernate.HasId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.katyshevtseva.medialog.core.films.Service;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.katyshevtseva.date.DateUtils.READABLE_DATE_FORMAT;
import static com.katyshevtseva.time.TimeUtil.getTimeStringByMinutes;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Film implements HasId {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long kpId;

    private String posterUrl;

    private String title;

    private Integer year;

    @Enumerated(EnumType.STRING)
    private FilmGrade grade;

    @Enumerated(EnumType.STRING)
    private FilmStatus status;

    @ElementCollection
    @CollectionTable(name = "film_dates", joinColumns = @JoinColumn(name = "film_id"))
    @Column(name = "date_")
    @Temporal(TemporalType.DATE)
    private List<Date> dates;

    @Column(name = "poster_state")
    @Enumerated(EnumType.STRING)
    private PosterState posterState;

    @Column(length = 2000)
    private String description;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "films_genres",
            joinColumns = @JoinColumn(name = "film_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private Set<FilmGenre> genres;

    private Integer length;

    @Column(name = "to_watch_adding_date")
    @Temporal(TemporalType.DATE)
    private Date toWatchAddingDate;

    @Enumerated(EnumType.STRING)
    private Type type;

    private Boolean processed;

    private Integer numOfActors;

    private Integer numOfTrailers;

    @OneToMany(mappedBy = "film", fetch = FetchType.LAZY)
    private Set<Role> roles;

    public String getTitleAndYear() {
        if (year == null)
            return title;
        return title + " (" + year + ")";
    }

    public String getDatesString() {
        if (GeneralUtils.isEmpty(dates)) {
            return "[]";
        }
        StringBuilder stringBuilder = new StringBuilder(" [");

        String prefix = "";
        for (Date date : dates.stream().sorted().collect(Collectors.toList())) {
            stringBuilder.append(prefix);
            prefix = ", ";
            stringBuilder.append(Service.getDateString(date));
        }
        return stringBuilder.append("]").toString();
    }

    public String getToWatchDesc() {
        String res = title + " (" + year + ")";

        if (!GeneralUtils.isEmpty(genres)) {
            res += ("\n\n" + genres);
        }

        if (length != null) {
            res += ("\n" + getTimeStringByMinutes(length));
        }

        if (description != null) {
            res += ("\n\n" + description);
        }

        return res += ("\n\n" + "Добавлено: " + READABLE_DATE_FORMAT.format(toWatchAddingDate));
    }

    public String getDetailsString() {
        String res = "grade: " + grade + "\n" +
                "status: " + status + "\n" +
                "dates: " + getDatesString() + "\n";

        if (!GeneralUtils.isEmpty(genres)) {
            res += ("genres: " + genres + "\n");
        }

        if (length != null) {
            res += ("length: " + getTimeStringByMinutes(length) + "\n");
        }

        if (toWatchAddingDate != null) {
            res += ("toWatchAddingDate: " + READABLE_DATE_FORMAT.format(toWatchAddingDate) + "\n");
        }

        if (description != null) {
            res += ("\n" + description);
        }

        return res;
    }

    @Override
    public String toString() {
        return title;
    }

    public String getDebugInfo() {
        return "id=" + id +
                ", kpId=" + kpId +
                ", title='" + title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Film film = (Film) o;

        return id.equals(film.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
