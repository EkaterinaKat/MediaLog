package org.katyshevtseva.medialog.view.music;

import com.katyshevtseva.image.ImageContainer;
import com.katyshevtseva.image.ImageContainerCache;
import org.katyshevtseva.medialog.core.music.entity.Album;

import java.io.File;

public class AlbumImageUtils {
    private static final String ALBUM_IMAGE_LOCATION = "D:\\onedrive\\central_image_storage\\albums\\";
    private static final String ICON_LOCATION = "D:\\onedrive\\central_image_storage\\icons\\";
    private static final ImageContainerCache icc = ImageContainerCache.getInstance();


    public static ImageContainer getImageContainer(Album album) {
        return getImageContainer(album == null ? null : album.getImageName());
    }

    public static ImageContainer getImageContainer(String imageName) {
        if (imageName == null) {
            return icc.getImageContainer("no_image.png", ICON_LOCATION, 400);
        }

        if (!new File(ALBUM_IMAGE_LOCATION, imageName).exists()) {
            return icc.getImageContainer("image_not_found.jpg", ICON_LOCATION, 400);
        }

        return icc.getImageContainer(imageName, ALBUM_IMAGE_LOCATION, 400);
    }
}
