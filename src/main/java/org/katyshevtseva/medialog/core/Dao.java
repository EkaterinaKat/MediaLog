package org.katyshevtseva.medialog.core;

import com.katyshevtseva.general.GeneralUtils;
import com.katyshevtseva.hibernate.CoreDao;
import com.katyshevtseva.hibernate.HasId;
import org.katyshevtseva.medialog.core.books.model.Author;
import org.katyshevtseva.medialog.core.books.model.Book;
import org.katyshevtseva.medialog.core.films.model.*;
import org.katyshevtseva.medialog.core.music.entity.Album;
import org.katyshevtseva.medialog.core.music.entity.Genre;
import org.katyshevtseva.medialog.core.music.entity.Singer;
import org.katyshevtseva.medialog.core.series.model.Series;
import org.katyshevtseva.medialog.core.series.model.SeriesState;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.util.Date;
import java.util.List;

public class Dao {
    private static final CoreDao coreDao = new CoreDao();

    public static <T> void saveNew(T t) {
        coreDao.saveNew(t);
    }

    public static <T> void saveEdited(T t) {
        coreDao.update(t);
    }

    public static <T> void delete(T t) {
        coreDao.delete(t);
    }

    public static <T> Long saveNewAndGetId(T t) {
        return (Long) coreDao.saveNewAndGetId(t);
    }

    public static <T extends HasId> T saveNewAndGetResult(Class<T> tClass, T entity) {
        return coreDao.saveNewAndGetResult(tClass, entity);
    }

    /////////////////////////////////////////////// MUSIC ///////////////////////////////////////////////

    public static List<Album> getAllAlbum() {
        return coreDao.getAll(Album.class.getSimpleName());
    }

    public static List<Singer> getAllSinger() {
        return coreDao.getAll(Singer.class.getSimpleName());
    }

    public static List<Genre> getAllGenre() {
        return coreDao.getAll(Genre.class.getSimpleName());
    }

    /////////////////////////////////////////////// FILMS ///////////////////////////////////////////////

    public static List<Film> getAllFilms() {
        return coreDao.getAll(Film.class.getSimpleName());
    }

    public static List<FilmGenre> getAllFilmGenres() {
        return coreDao.getAll(FilmGenre.class.getSimpleName());
    }

    public static FilmGenre findFilmGenreByTitle(String title) {
        List<FilmGenre> genres = coreDao.find(FilmGenre.class, Restrictions.eq("title", title));
        if (genres.size() > 1) {
            throw new RuntimeException("Более одно жанра в бд имеют одинаковые названия");
        }
        return genres.isEmpty() ? null : genres.get(0);
    }

    public static FilmGenre saveNewGenre(FilmGenre genre) {
        return coreDao.saveNewAndGetResult(FilmGenre.class, genre);
    }

    public static Film saveNewFilm(Film film) {
        return coreDao.saveNewAndGetResult(Film.class, film);
    }

    public static Film findFilmByKpId(Long kpId) {
        List<Film> films = coreDao.find(Film.class, Restrictions.eq("kpId", kpId));
        if (films.size() > 1) {
            throw new RuntimeException("Более одно фильма в бд имеют одинаковые kpId");
        }
        return films.isEmpty() ? null : films.get(0);
    }

    public static List<Film> findFilms(FilmGrade grade, String searchString) {
        if (GeneralUtils.isEmpty(searchString)) {
            return coreDao.find(Film.class, Restrictions.eq("grade", grade));
        }

        String sqlString = "select * from film " +
                "where grade = :grade " +
                "and (upper(title) like :search_string " +
                "or CAST(year AS varchar) like :search_string ) ; ";
        return coreDao.findByQuery(session -> session.createSQLQuery(sqlString)
                .addEntity(Film.class)
                .setParameter("search_string", "%" + searchString.toUpperCase() + "%")
                .setParameter("grade", grade.toString()));
    }


    public static List<Film> findFilmsViewedInYear(int year) {
        String sqlString = "select distinct f.* from film f " +
                "join film_dates d on f.id = d.film_id " +
                "where EXTRACT(YEAR FROM d.date_)=:year ; ";
        return coreDao.findByQuery(session -> session.createSQLQuery(sqlString)
                .addEntity(Film.class)
                .setParameter("year", year));
    }

    public static List<Date> getAllDatesWithFilmViews() {
        String sqlString = "select date_ from film_dates ; ";
        return coreDao.findByQuery(session -> session.createSQLQuery(sqlString));
    }

    public static List<Trailer> findTrailers(Film film) {
        return coreDao.find(session -> {
            Criteria criteria = session.createCriteria(Trailer.class, "trailer");
            criteria.createAlias("trailer.film", "film");
            criteria.add(Restrictions.eq("film.id", film.getId()));
            return criteria;
        });
    }

    public static Actor findActorByKpId(Long kpId) {
        List<Actor> actors = coreDao.find(Actor.class, Restrictions.eq("kpId", kpId));
        if (actors.size() > 1) {
            throw new RuntimeException("Более одно актёра в бд имеют одинаковые kpId");
        }
        return actors.isEmpty() ? null : actors.get(0);
    }

    public static List<Actor> getAllActors() {
        return coreDao.getAll(Actor.class.getSimpleName());
    }

    public static void saveNewRole(Role role) {
        coreDao.saveNew(role);
        coreDao.refresh(role.getFilm());
    }

    public static Actor getRefreshed(Actor actor) {
        return coreDao.getRefreshed(Actor.class, actor);
    }

    /////////////////////////////////////////////// SERIES ///////////////////////////////////////////////
    public static List<Series> findSeries(SeriesState state, String searchString) {
        if (GeneralUtils.isEmpty(searchString)) {
            return coreDao.find(Series.class, Restrictions.eq("state", state));
        }

        String sqlString = "select * from series " +
                "where state = :state " +
                "and upper(title) like :search_string ; ";
        return coreDao.findByQuery(session -> session.createSQLQuery(sqlString)
                .addEntity(Series.class)
                .setParameter("search_string", "%" + searchString.toUpperCase() + "%")
                .setParameter("state", state.toString()));
    }

    /////////////////////////////////////////////// BOOKS ///////////////////////////////////////////////
    public static List<Author> getAllAuthor() {
        return coreDao.getAll(Author.class.getSimpleName());
    }

    public static List<Author> findAuthors(String searchString) {
        String sqlString = "select distinct a.* from author a " +
                "join book b on b.author_id = a.id " +
                "where upper(a.name) like :search_string " +
                "or upper(a.surname) like :search_string " +
                "or upper(b.name) like :search_string ; ";
        return coreDao.findByQuery(session -> session.createSQLQuery(sqlString)
                .addEntity(Author.class)
                .setParameter("search_string", "%" + searchString.toUpperCase() + "%"));
    }

    public static List<Book> findBooks(Author author, String searchString) {
        if (GeneralUtils.isEmpty(searchString)) {
            return coreDao.find(Book.class, Restrictions.eq("author", author));
        }

        String sqlString = "select * from book " +
                "where author_id = :author_id " +
                "and upper(name) like :search_string ; ";
        return coreDao.findByQuery(session -> session.createSQLQuery(sqlString)
                .addEntity(Book.class)
                .setParameter("search_string", "%" + searchString.toUpperCase() + "%")
                .setParameter("author_id", author.getId()));
    }
}
