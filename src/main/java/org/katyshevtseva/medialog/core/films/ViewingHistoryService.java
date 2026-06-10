package org.katyshevtseva.medialog.core.films;

import com.katyshevtseva.date.DateUtils;
import com.katyshevtseva.date.Month;
import org.katyshevtseva.medialog.core.Dao;
import org.katyshevtseva.medialog.core.films.model.Film;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ViewingHistoryService {

    public static Integer getIndexOfCurrentYear(List<Integer> years) {
        Integer currentYear = DateUtils.getYearDateBelongsTo(new Date());
        for (int i = 0; i < years.size(); i++) {
            Integer year = years.get(i);
            if (year.equals(currentYear))
                return i;
        }
        throw new RuntimeException("Year list doesn't contain current year");
    }

    public static List<Integer> getYearsForDatePicker() {
        Stream<Integer> viewYears = Dao.getAllDatesWithFilmViews().stream()
                .map(DateUtils::getYearDateBelongsTo);

        // добавляем в список текущий год чтобы он отображался даже если нет записей о просмотре
        return Stream.concat(viewYears, Stream.of(DateUtils.getYearDateBelongsTo(new Date())))
                .distinct()
                .sorted()
                .collect(Collectors.toList());

    }

    public static List<Month> getMonthsWithViews(Integer year) {
        return Dao.getAllDatesWithFilmViews().stream()
                .filter(date -> DateUtils.getYearDateBelongsTo(date).equals(year))
                .map(Month::findByDate)
                .distinct()
                .sorted(Comparator.comparing(Month::getIndex).reversed())
                .collect(Collectors.toList());
    }

    public static List<Film> getFilms(Integer year, Month month) {
        return Dao.findFilmsViewedInYear(year).stream()
                .filter(film -> filmWasViewedInThisYearAndMonth(film, year, month))
                .collect(Collectors.toList());
    }

    private static boolean filmWasViewedInThisYearAndMonth(Film film, Integer year, Month month) {
        for (Date date : film.getDates()) {
            Month viewMonth = Month.findByDate(date);
            int viewYear = DateUtils.getYearDateBelongsTo(date);
            if (viewMonth == month && viewYear == year) {
                return true;
            }
        }
        return false;
    }
}
