package org.katyshevtseva.medialog.core.music.entity;

import com.katyshevtseva.general.GeneralUtils;
import org.katyshevtseva.medialog.core.music.AlbumGrade;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

import static com.katyshevtseva.general.GeneralUtils.isEmpty;

@Data
@Entity
@NoArgsConstructor
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String comment;

    private String imageName;

    private Integer year;

    @Temporal(TemporalType.DATE)
    private Date listeningDate;

    @ManyToOne
    @JoinColumn(name = "singer_id")
    private Singer singer;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "album_genres",
            joinColumns = @JoinColumn(name = "album_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private List<Genre> genres;

    private Integer numOfTracks;

    private Integer duration;

    @Enumerated(EnumType.STRING)
    private AlbumGrade grade;

    public String getFullInfo() {
        StringBuilder sb = new StringBuilder();
        if (singer != null || year != null) {
            sb.append(singer).append(" (").append(year).append(")\n");
        }
        if (genres != null) {
            sb.append(genres).append("\n");
        }
        if (listeningDate != null) {
            sb.append("date: ").append(listeningDate).append("\n");
        }
        if (numOfTracks != null || duration != null) {
            sb.append("Tracks: ").append(numOfTracks == null ? "-" : numOfTracks)
                    .append("    Duration: ").append(duration == null ? "-" : duration).append("\n");
        }

        if (!isEmpty(comment)) {
            sb.append("\n").append(GeneralUtils.crop(comment, 70)).append("\n");
        }

        return sb.toString();
    }

    public Album(String title, String comment, String imageName, Integer year, Date listeningDate,
                 Singer singer, List<Genre> genres, Integer numOfTracks, Integer duration, AlbumGrade grade) {
        this.title = title;
        this.comment = comment;
        this.imageName = imageName;
        this.year = year;
        this.listeningDate = listeningDate;
        this.singer = singer;
        this.genres = genres;
        this.numOfTracks = numOfTracks;
        this.duration = duration;
        this.grade = grade;
    }

    public void setValues(String title, String comment, String imageName, Integer year, Date listeningDate,
                          Singer singer, List<Genre> genres, Integer numOfTracks, Integer duration, AlbumGrade grade) {
        this.title = title;
        this.comment = comment;
        this.imageName = imageName;
        this.year = year;
        this.listeningDate = listeningDate;
        this.singer = singer;
        this.genres = genres;
        this.numOfTracks = numOfTracks;
        this.duration = duration;
        this.grade = grade;
    }
}
