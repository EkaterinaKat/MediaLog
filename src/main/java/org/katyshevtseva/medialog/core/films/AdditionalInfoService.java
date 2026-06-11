package org.katyshevtseva.medialog.core.films;

import org.katyshevtseva.medialog.core.Dao;
import org.katyshevtseva.medialog.core.films.model.Actor;
import org.katyshevtseva.medialog.core.films.model.Film;
import org.katyshevtseva.medialog.core.films.model.Role;
import org.katyshevtseva.medialog.core.films.model.Trailer;
import org.katyshevtseva.medialog.core.films.web.ActorPhotoLoader;
import org.katyshevtseva.medialog.core.films.web.FilmSearchEngine;
import org.katyshevtseva.medialog.core.films.web.model.AdditionalInfoResponse;
import org.katyshevtseva.medialog.core.films.web.model.PersonResponse;
import org.katyshevtseva.medialog.core.films.web.model.TrailerResponse;

import java.util.List;
import java.util.stream.Collectors;

public class AdditionalInfoService {

    public static void loadAdditionalInfo(Film film) throws Exception {
        AdditionalInfoResponse response = FilmSearchEngine.findByKpId(film.getKpId());

        Integer numOfActors = saveActors(film, response);
        Integer numOfTrailers = saveTrailers(film, response);
        film.setNumOfActors(numOfActors);
        film.setNumOfTrailers(numOfTrailers);

        Dao.saveEdited(film);
    }

    public static Integer saveTrailers(Film film, AdditionalInfoResponse response) {
        if (response.getVideos() == null) {
            return 0;
        }

        List<TrailerResponse> trailers = response.getVideos().getTrailers();
        for (TrailerResponse trailerResponse : trailers) {
            Trailer trailer = new Trailer(
                    null,
                    trailerResponse.getUrl(),
                    trailerResponse.getName(),
                    trailerResponse.getSite(),
                    trailerResponse.getType(),
                    film
            );
            Dao.saveNew(trailer);
        }
        return trailers.size();
    }

    public static Integer saveActors(Film film, AdditionalInfoResponse response) {
        if (!film.getType().hasActors) {
            return 0;
        }

        List<PersonResponse> actors = response.getPersons().stream()
                .filter(personResponse -> personResponse.getProfession().equals("актеры"))
                .collect(Collectors.toList());

        for (PersonResponse personResponse : actors) {
            Actor existingActor = Dao.findActorByKpId(personResponse.getId());
            if (existingActor != null) {
                createRole(film, existingActor, personResponse);
                loadPhotoIfNeeded(existingActor);
            } else {
                Actor actor = createActor(personResponse);
                createRole(film, actor, personResponse);
            }
        }

        return actors.size();
    }

    private static void loadPhotoIfNeeded(Actor actor) {
        Actor refreshed = Dao.getRefreshed(actor);
        if (Dao.findRoles(refreshed).size() > 1) {
            ActorPhotoLoader.loadActorPhoto(actor);
        }
    }

    private static Actor createActor(PersonResponse personResponse) {
        Actor actor = new Actor(
                null,
                personResponse.getId(),
                personResponse.getPhoto(),
                personResponse.getName(),
                personResponse.getEnName(),
                null,
                null
        );
        return Dao.saveNewAndGetResult(Actor.class, actor);
    }

    private static void createRole(Film film, Actor actor, PersonResponse personResponse) {
        Role role = new Role(
                null,
                actor,
                film,
                personResponse.getDescription()
        );
        Dao.saveNewRole(role);
    }
}
