package org.katyshevtseva.medialog.core;

import org.katyshevtseva.medialog.core.films.PosterFileManager;
import org.katyshevtseva.medialog.core.films.model.Actor;
import org.katyshevtseva.medialog.core.films.model.Film;
import org.katyshevtseva.medialog.core.films.model.PosterState;
import org.katyshevtseva.medialog.core.films.web.ActorPhotoLoader;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.katyshevtseva.general.GeneralUtils.isEmpty;

public class Tests {

    public static void allFilmTests() {
        testFilmStatus();
        testPosterState();
        testNumOfTrailers();
        testKpIdUniqueness();
        testNumOfActorsInFilms();
    }

    public static void testNumOfActorsInFilms() {
        for (Film film : Dao.getAllFilms()) {
            assert_(
                    film,
                    film.getNumOfActors().equals(film.getRoles().size()),
                    "film.getNumOfActors().equals(roles.size())");
        }
        System.out.println("testNumOfActorsInFilms done");
    }

    public static void testNumOfTrailers() {
        for (Film film : Dao.getAllFilms()) {
            assert_(
                    film,
                    film.getNumOfTrailers().equals(Dao.findTrailers(film).size()),
                    "film.getNumOfTrailers().equals(Dao.findTrailers(film).size())");
        }
        System.out.println("testNumOfTrailers done");
    }

    public static void testFilmStatus() {
        for (Film film : Dao.getAllFilms()) {

            assert_(film, film.getKpId() != null, "film.getKpId()!=null");
            assert_(film, film.getPosterUrl() != null, "film.getPosterUrl()!=null");
            assert_(film, film.getTitle() != null, "film.getTitle()!=null");
            assert_(film, film.getYear() != null, "film.getYear()!=null");
            assert_(film, film.getType() != null, "film.getType()!=null");

            switch (film.getStatus()) {
                case WATCHED:
                    testWatchedFilmState(film);
                    break;
                case TO_WATCH:
                    testToWatchFilmState(film);
                    break;
                case WATCHED_AND_TO_WATCH:
                    testWatchedAndToWatchFilmState(film);
                    break;
            }

        }
        System.out.println("testFilmStatus done");
    }

    public static void testWatchedFilmState(Film film) {
        assert_(film, film.getGrade() != null, "WATCHED film.getGrade()!=null");
        assert_(film, !isEmpty(film.getDates()), "WATCHED !isEmpty(film.getDates())");
        assert_(film, film.getToWatchAddingDate() == null, "WATCHED film.getToWatchAddingDate()==null");
    }

    public static void testToWatchFilmState(Film film) {
        assert_(film, film.getGrade() == null, "TO_WATCH film.getGrade()==null");
        assert_(film, isEmpty(film.getDates()), "TO_WATCH isEmpty(film.getDates())");
        assert_(film, film.getToWatchAddingDate() != null, "WATCHED film.getToWatchAddingDate()!=null");
    }

    public static void testWatchedAndToWatchFilmState(Film film) {
        assert_(film, film.getGrade() != null, "WATCHED film.getGrade()!=null");
        assert_(film, !isEmpty(film.getDates()), "WATCHED !isEmpty(film.getDates())");
        assert_(film, film.getToWatchAddingDate() != null, "WATCHED film.getToWatchAddingDate()!=null");
    }

    private static void assert_(Film film, boolean b, String desc) {
        if (!b) {
            throw new RuntimeException(film.getDebugInfo() + ": " + desc);
        }
    }

    public static void testPosterState() {
        for (Film film : Dao.getAllFilms()) {
            boolean a = PosterFileManager.filmHasPoster(film);
            boolean b = film.getPosterState() == PosterState.LOADED;
            if (a != b) {
                throw new RuntimeException(film.getTitle() + " error!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            }
            if (!a) {
                throw new RuntimeException(film.getTitle() + " doesn't have poster");
            }
        }
        System.out.println("testPosterState done");
    }

    public static void testKpIdUniqueness() {
        List<Film> films = Dao.getAllFilms();
        List<Long> idList = films.stream()
                .map(Film::getKpId)
                .collect(Collectors.toList());
        idList = idList
                .stream()
                .filter(Objects::nonNull)
                .sorted()
                .collect(Collectors.toList());

        for (int i = 0; i < idList.size() - 1; i++) {
            long a = idList.get(i);
            long b = idList.get(i + 1);
            if (a == b) {
                System.out.println(a);
            }
        }

        Set<Long> idSet = new HashSet<>(idList);
        boolean duplicatesExists = (idSet.size() < idList.size());
        if (duplicatesExists) {
            throw new RuntimeException("Id list has duplicates");
        }
        System.out.println("testKpIdUniqueness done");
    }

    public static void printActorStatistics() {
        List<Actor> actors = Dao.getAllActors();

        System.out.println("All " + actors.size());

        for (int i = 0; i < 18; i++) {
            printActorsByNumOfFilms(actors, i);
        }
    }

    private static void printActorsByNumOfFilms(List<Actor> actors, int num) {
        List<Actor> filteredActors = actors.stream()
                .filter(actor -> actor.getRoles().size() == num)
                .collect(Collectors.toList());


        System.out.println(num + " " + filteredActors.size());
        if (filteredActors.size() < 10) {
            System.out.println(filteredActors);
        }
    }

    public static void loadActorPhotos() {
        List<Actor> actors = Dao.getAllActors();
        for (Actor actor : actors) {

            if (actor.getRoles().size() > 1) {
                ActorPhotoLoader.loadActorPhoto(actor);
            }
        }
    }

    public static void printTrailerStatistics() {
        List<Film> films = Dao.getAllFilms();
        for (int i = 0; i < 18; i++) {
            printFilmsByNumOfTrailers(films, i);
        }
    }

    private static void printFilmsByNumOfTrailers(List<Film> films, int num) {
        List<Film> filteredFilm = films.stream()
                .filter(film -> film.getNumOfTrailers() == num)
                .collect(Collectors.toList());

        System.out.println(num + " " + filteredFilm.size());
    }
}
