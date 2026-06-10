package org.katyshevtseva.medialog.core.films.web;

import org.katyshevtseva.medialog.core.films.ActorFileManager;
import org.katyshevtseva.medialog.core.films.model.Actor;
import com.katyshevtseva.web.ImageDownloader;

import static org.katyshevtseva.medialog.core.films.ActorFileManager.ACTORS_IMAGE_LOCATION;
import static org.katyshevtseva.medialog.core.films.ActorFileManager.formImageFileName;

public class ActorPhotoLoader {

    public static void loadActorPhoto(Actor actor) {

        if (ActorFileManager.actorHasPhoto(actor)) {
            return;
        }

        try {
            ImageDownloader.download(ACTORS_IMAGE_LOCATION, formImageFileName(actor), actor.getPhotoUrl());
            System.out.println("Успешная загрузка " + actor.getNameNonNull());
        } catch (Exception e) {
            System.out.println("Неуспешная загрузка " + actor.getNameNonNull());
            throw new RuntimeException(e);
        }

    }
}
