package org.katyshevtseva.medialog.core.films;

import com.katyshevtseva.image.ImageContainer;
import com.katyshevtseva.image.ImageContainerCache;
import org.katyshevtseva.medialog.core.films.model.Actor;

import java.io.File;

public class ActorFileManager {
    public static final String ACTORS_IMAGE_LOCATION = "D:\\onedrive\\central_image_storage\\actors\\";
    private static final ImageContainerCache icc = ImageContainerCache.getInstance();

    public static ImageContainer getActorPhoto(Actor actor) {
        return icc.getImageContainer(formImageFileName(actor), ACTORS_IMAGE_LOCATION, 185);
    }

    public static boolean actorHasPhoto(Actor actor) {
        File file = new File(ACTORS_IMAGE_LOCATION + formImageFileName(actor));
        return file.exists();
    }

    public static String formImageFileName(Actor actor) {
        return actor.getKpId() + ".jpg";
    }
}
