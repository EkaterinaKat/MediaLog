package org.katyshevtseva.medialog.core.music;

import org.katyshevtseva.medialog.core.Dao;
import org.katyshevtseva.medialog.core.music.entity.Album;
import org.katyshevtseva.medialog.core.music.entity.Genre;
import org.katyshevtseva.medialog.core.music.entity.Singer;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class MusicService {

    public static void saveAlbum(Album existing, String title, String comment, String imageName, Integer year, Date listeningDate,
                                 Singer singer, List<Genre> genres, Integer numOfTracks, Integer duration, AlbumGrade grade) {
        title = title.trim();
        comment = comment.trim();

        if (existing == null) {
            Dao.saveNew(new Album(title, comment, imageName, year, listeningDate, singer, genres, numOfTracks, duration, grade));
        } else {
            existing.setValues(title, comment, imageName, year, listeningDate, singer, genres, numOfTracks, duration, grade);
            Dao.saveEdited(existing);
        }
    }

    public static void saveGenre(Genre existing, String title) {
        title = title.trim();

        if (existing == null) {
            Dao.saveNew(new Genre(title));
        } else {
            existing.setTitle(title);
            Dao.saveEdited(existing);
        }
    }


    public static void saveSinger(Singer existing, String name) {
        name = name.trim();

        if (existing == null) {
            Dao.saveNew(new Singer(name));
        } else {
            existing.setName(name);
            Dao.saveEdited(existing);
        }
    }

    public static List<Album> getAlbums() {
        return Dao.getAllAlbum().stream().sorted(Comparator.comparing(Album::getId).reversed()).collect(Collectors.toList());
    }

    public static List<Singer> getSingers() {
        return Dao.getAllSinger().stream()
                .sorted(Comparator.comparing(singer -> singer.getName().toUpperCase()))
                .collect(Collectors.toList());
    }

    public static List<Genre> getGenres() {
        return Dao.getAllGenre().stream()
                .sorted(Comparator.comparing(genre -> genre.getTitle().toUpperCase()))
                .collect(Collectors.toList());
    }
}
