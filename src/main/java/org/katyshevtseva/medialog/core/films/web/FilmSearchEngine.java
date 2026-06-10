package org.katyshevtseva.medialog.core.films.web;

import org.katyshevtseva.medialog.core.films.web.model.AdditionalInfoResponse;
import org.katyshevtseva.medialog.core.films.web.model.FilmArrayResponse;
import com.katyshevtseva.web.HttpHelper;
import com.katyshevtseva.web.Response;

import static com.katyshevtseva.web.WebUtils.encodeStringUTF8;

public class FilmSearchEngine {
    private static final String BASE_URL = "https://api.kinopoisk.dev/v1.4/movie/";
    private static final String TITLE_SEARCH_URL = "search?page=1&limit=10&query=%s&token=%s";
    private static final String ID_SEARCH_URL = "%d?token=%s";
    private static final String TOKEN = "2BW30XT-0E84FXT-PC1P59Z-1BW2MWB";

    public static FilmArrayResponse findByTitle(String titleQuery) throws Exception {
        String query = BASE_URL + String.format(TITLE_SEARCH_URL, encodeStringUTF8(titleQuery), TOKEN);
        Response response = HttpHelper.get(query);
        if (response.codeIsPositive()) {
            return response.parceBody(FilmArrayResponse.class);
        } else {
            throw new RuntimeException("Не удалось загрузить фильмы");
        }
    }

    public static AdditionalInfoResponse findByKpId(Long kpId) throws Exception {
        String query = BASE_URL + String.format(ID_SEARCH_URL, kpId, TOKEN);
        Response response = HttpHelper.get(query);
        if (response.codeIsPositive()) {
            return response.parceBody(AdditionalInfoResponse.class);
        } else {
            throw new RuntimeException("Не удалось загрузить фильм по id=" + kpId);
        }
    }
}
