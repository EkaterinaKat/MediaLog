package org.katyshevtseva.medialog.core.series.model;

import com.katyshevtseva.general.GeneralUtils;
import lombok.Data;
import lombok.NonNull;

import javax.persistence.*;
import java.util.Date;

import static com.katyshevtseva.date.DateUtils.READABLE_DATE_FORMAT;

@Data
@Entity
public class Series {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Enumerated(EnumType.STRING)
    private SeriesState state;

    @Enumerated(EnumType.STRING)
    private SeriesGrade grade;

    private String comment;

    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Temporal(TemporalType.DATE)
    private Date finishDate;

    private Boolean isAnime;

    @NonNull
    public Boolean getIsAnime() {
        return isAnime != null && isAnime;
    }

    public void setValues(String title,
                          SeriesState state,
                          SeriesGrade grade,
                          String comment,
                          Date startDate,
                          Date finishDate,
                          boolean isAnime) {
        this.title = title;
        this.state = state;
        this.grade = grade;
        this.comment = comment;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.isAnime = isAnime;
    }

    public String getFullInfo() {
        StringBuilder sb = new StringBuilder();

        if (!GeneralUtils.isEmpty(comment)) {
            sb.append(comment).append("\n\n");
        }

        if (startDate != null || finishDate != null) {
            sb.append(startDate != null ? READABLE_DATE_FORMAT.format(startDate) : "*")
                    .append(" - ")
                    .append(finishDate != null ? READABLE_DATE_FORMAT.format(finishDate) : "*");
        }

        return sb.toString();
    }

    public boolean hasDetails() {
        return !GeneralUtils.isEmpty(comment) || startDate != null || finishDate != null;
    }
}
