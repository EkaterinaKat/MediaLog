package org.katyshevtseva.medialog.core.books.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

import static com.katyshevtseva.date.DateUtils.READABLE_DATE_FORMAT;
import static com.katyshevtseva.general.GeneralUtils.isEmpty;

@Data
@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    @Enumerated(EnumType.STRING)
    private BookAction action;

    @Temporal(TemporalType.DATE)
    private Date finishDate;

    private boolean favorite;

    @Enumerated(EnumType.STRING)
    private BookGrade grade;

    public Book() {
    }

    public Book(String name, Author author, BookAction action, Date finishDate, BookGrade grade) {
        this.name = name;
        this.author = author;
        this.action = action;
        this.finishDate = finishDate;
        this.grade = grade;
    }

    public void setValues(String name, BookAction action, Date finishDate, BookGrade grade) {
        this.name = name;
        this.action = action;
        this.grade = grade;
        this.finishDate = finishDate;
    }

    public String getListInfo() {
        StringBuilder stringBuilder = new StringBuilder(name);
        if (finishDate != null || !isEmpty(action.getShortInfo())) {
            stringBuilder
                    .append("\n")
                    .append(finishDate != null ? READABLE_DATE_FORMAT.format(finishDate) + " " : "")
                    .append(action.getShortInfo());
        }
        return stringBuilder.toString();
    }
}
