package org.katyshevtseva.medialog.core.films;

import com.katyshevtseva.general.GeneralUtils;
import com.katyshevtseva.hibernate.HasId;
import com.katyshevtseva.image.ImageContainer;
import com.katyshevtseva.image.ImageContainerCache;
import org.katyshevtseva.medialog.core.films.model.Film;
import org.katyshevtseva.medialog.core.films.model.PosterState;

import java.io.File;
import java.util.Objects;

public class PosterFileManager {
    public static final String FILM_IMAGE_LOCATION = "D:\\onedrive\\central_image_storage\\films\\";
    private static final ImageContainerCache icc = ImageContainerCache.getInstance();

    public static ImageContainer getPoster(Film film) {
        if (film.getPosterState() == PosterState.LOADED) {
            return icc.getImageContainer(formImageFileName(film), FILM_IMAGE_LOCATION, 222);
        } else {
            return icc.getImageContainer(film.getPosterState().getImageName(), FILM_IMAGE_LOCATION, 222);
        }
    }

    public static boolean filmHasPoster(Film film) {
        File[] files = Objects.requireNonNull(new File(FILM_IMAGE_LOCATION).listFiles());
        for (File file : files) {
            if (GeneralUtils.removeExtension(file.getName()).equals(film.getId().toString())) {
                return true;
            }
        }
        return false;
    }

    public static String formImageFileName(HasId hasId) {
        return hasId.getId() + ".jpg";
    }
}
