package org.katyshevtseva.medialog.view.books;

import com.katyshevtseva.image.ImageContainer;
import com.katyshevtseva.image.ImageContainerCache;
import org.katyshevtseva.medialog.core.books.model.Author;

public class AuthorImageUtils {
    private static final String AUTHOR_IMAGE_LOCATION = "D:\\onedrive\\central_image_storage\\authors\\";
    private static final ImageContainerCache icc = ImageContainerCache.getInstance();


    public static ImageContainer getImageContainer(Author author) {
        return icc.getImageContainer(author.getId() + ".jpg", AUTHOR_IMAGE_LOCATION, 400);
    }
}
