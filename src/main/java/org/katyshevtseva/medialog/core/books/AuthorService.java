package org.katyshevtseva.medialog.core.books;

import org.katyshevtseva.medialog.core.Dao;
import org.katyshevtseva.medialog.core.books.model.Author;

import java.util.List;

import static com.katyshevtseva.general.GeneralUtils.isEmpty;

public class AuthorService {

    public static List<Author> getAll() {
        return Dao.getAllAuthor();
    }

    public static List<Author> getAll(String searchString) {
        return isEmpty(searchString) ? Dao.getAllAuthor() : Dao.findAuthors(searchString);
    }

    public static void save(Author existing, String name, String surname) {
        if (name == null) {
            throw new RuntimeException();
        }

        name = name.trim();
        surname = surname.trim();
        if (existing == null) {
            existing = new Author(name, surname);
            Dao.saveNew(existing);
        } else {
            existing.setValues(name, surname);
            Dao.saveEdited(existing);
        }
    }
}
