package org.katyshevtseva.medialog.core.films;

import com.katyshevtseva.date.DateUtils;
import org.katyshevtseva.medialog.core.Dao;
import org.katyshevtseva.medialog.core.films.model.Film;

import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsService {

    public static Map<Integer, Integer> getYearFilmCountMap() {
        List<Film> films = Dao.getAllFilms();
        Map<Integer, Integer> map = new HashMap<>();

        int firstYear = films.stream().min(Comparator.comparing(Film::getYear)).get().getYear();
        int currentYear = DateUtils.getYearDateBelongsTo(new Date());
        for (int i = firstYear; i <= currentYear; i++) {
            map.put(i, 0);
        }

        for (Film film : films) {
            int count = map.getOrDefault(film.getYear(), 0);
            count++;
            map.put(film.getYear(), count);
        }

        int sum = map.values().stream().mapToInt(i -> i).sum();
        if (sum != films.size()) {
            throw new RuntimeException();
        }

        return map;
    }
}
