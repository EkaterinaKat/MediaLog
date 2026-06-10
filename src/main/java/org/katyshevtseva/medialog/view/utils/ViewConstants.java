package org.katyshevtseva.medialog.view.utils;

import com.katyshevtseva.fx.Size;
import com.katyshevtseva.fx.windowbuilder.DialogInfo;
import com.katyshevtseva.fx.windowbuilder.NodeInfo;
import org.katyshevtseva.medialog.core.CoreConstants;

public class ViewConstants {
    public static final DialogInfo MAIN_DIALOG = new DialogInfo(
            "/fxml/main.fxml", new Size(800, 1360), CoreConstants.APP_NAME);
    public static final DialogInfo AUTHOR_DIALOG =
            new DialogInfo("/fxml/books/author_dialog.fxml", new Size(320, 320), "Author edit");
    public static final DialogInfo ALBUM_DIALOG =
            new DialogInfo("/fxml/music/album_dialog.fxml", new Size(512, 896), "Album edit");

    public static final NodeInfo SECTION_MAIN_NODE = new NodeInfo("/fxml/section_main.fxml");
    public static final NodeInfo BOOKS_NODE = new NodeInfo("/fxml/books/main_books.fxml");
    public static final NodeInfo SERIES_NODE = new NodeInfo("/fxml/series/main_series.fxml");

    // MUSIC
    public static final NodeInfo ALBUMS_NODE = new NodeInfo("/fxml/music/albums.fxml");
    public static final NodeInfo SINGERS_AND_GENRES_NODE = new NodeInfo("/fxml/music/singers_and_genres.fxml");

    // FILMS
    public static final DialogInfo FILM_DETAIL_DIALOG =
            new DialogInfo("/fxml/films/details.fxml", new Size(640, 640), "");
    public static final DialogInfo ACTORS_DIALOG =
            new DialogInfo("/fxml/empty_pane.fxml", new Size(640, 640), "");
    public static final DialogInfo ACTOR_FILMS_DIALOG =
            new DialogInfo("/fxml/empty_pane.fxml", new Size(640, 640), "");
    public static final NodeInfo FILM_LISTS_NODE = new NodeInfo("/fxml/films/film_lists.fxml");
    public static final NodeInfo FILM_STATISTICS_NODE = new NodeInfo("/fxml/vbox_container.fxml");
    public static final NodeInfo VIEWING_HISTORY_NODE = new NodeInfo("/fxml/films/viewing_history.fxml");
    public static final NodeInfo TO_WATCH_NODE = new NodeInfo("/fxml/films/to_watch.fxml");
    public static final NodeInfo FILM_SEARCH_NODE = new NodeInfo("/fxml/films/search.fxml");

}
