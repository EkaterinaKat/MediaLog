package org.katyshevtseva.medialog.core.books.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import static com.katyshevtseva.general.GeneralUtils.isEmpty;

@Data
@Entity
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String surname;

    public Author(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public void setValues(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public Author() {
    }

    public String getFullName() {
        return name + (surname != null ? " " + surname : "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Author author = (Author) o;

        return id.equals(author.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public String getSortingString() {
        if (!isEmpty(surname))
            return surname;
        if (!isEmpty(name))
            return name;
        throw new RuntimeException();
    }
}
