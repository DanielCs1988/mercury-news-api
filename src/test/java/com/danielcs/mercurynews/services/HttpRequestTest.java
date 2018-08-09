package com.danielcs.mercurynews.services;

import com.danielcs.mercurynews.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static com.danielcs.mercurynews.services.HttpRequest.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HttpRequestTest {

    private HttpRequest service;
    private HttpRequestSender http;
    private Map<String, String> queryParams;

    @BeforeEach
    void setUp() {
        http = mock(HttpRequestSender.class);
        service = new HttpRequest();
        service.http = http;
        queryParams = new HashMap<>();
        queryParams.put("key", GEOLOC_API_KEY);
        queryParams.put("components", "country:hu");
    }

    @Test
    void fetchArticlesReturnsArticles() {
        NewsResultWrapper news = new NewsResultWrapper("OK", 20, Arrays.asList(
                new ArticleSource("this", "stub", "is", "very", "annoying",
                        "to", new Source("write", "fully")),
                new ArticleSource("this", "stub", "is", "also", "annoying",
                        "to", new Source("write", "down"))
        ));
        when(http.getJsonWithQuery(NEWS_API_ENDPOINT, queryParams)).thenReturn(news);
        List<ArticleDTO> result = service.fetchArticles(queryParams);
        List<ArticleDTO> expected = Arrays.asList(
                new ArticleDTO("this", "stub", "is", "very", "annoying", "to", "fully"),
                new ArticleDTO("this", "stub", "is", "also", "annoying", "to", "down")
        );
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    void fetchArticlesReturnsEmptyListWhenNullReply() {
        when(http.getJsonWithQuery(NEWS_API_ENDPOINT, queryParams)).thenReturn(null);
        List<ArticleDTO> result = service.fetchArticles(queryParams);
        assertEquals(new ArrayList<>().toString(), result.toString());
    }

    @Test
    void getGeolocByCountryCodeReturnsLocation() {
        String resp = "fluff formatted_address\": \"Sun\" random fluff location\": {\" lat\": 40.2121, \"lng\": 21.6798 more fluff";
        when(http.getWithQuery(HttpRequest.GEOLOC_API_ENDPOINT, queryParams)).thenReturn(resp);
        Geolocation geolocation = service.getGeolocByCountryCode("hu");
        Geolocation expected = new Geolocation("Sun", 40.2121, 21.6798);
        assertEquals(expected.toString(), geolocation.toString());
    }

    @Test
    void getGeolocByCountryCodeReturnsNullWhenJsonIsWrong() {
        String resp = "fluff formatted_address\": \"Sun\" random fluff location\": {\"lng\": 21.6798 more fluff";
        when(http.getWithQuery(HttpRequest.GEOLOC_API_ENDPOINT, queryParams)).thenReturn(resp);
        Geolocation geolocation = service.getGeolocByCountryCode("hu");
        assertNull(geolocation);
    }

    @Test
    void getWeatherByGeolocationReturnsWeather() {
        String resp = "currently fluff\"summary\": \"BURNING\",\"icon\": \"flames\", more fluff  temperature\": 104,";
        when(http.get(WEATHER_API_URL + "40.0,20.0")).thenReturn(resp);
        Weather weather = service.getWeatherByGeolocation("Here", 40, 20);
        Weather expected = new Weather("Here", "BURNING", "flames", 104);
        assertEquals(expected.toString(), weather.toString());
    }

    @Test
    void getWeatherByGeolocationReturnsNullWhenJsonIsWrong() {
        String resp = "currently fluff\"summary\": \"BURNING\",\"icon\": \"flames\", more fluff  temperature\": hundred,";
        when(http.get(WEATHER_API_URL + "40.0,20.0")).thenReturn(resp);
        Weather weather = service.getWeatherByGeolocation("Here", 40, 20);
        assertNull(weather);
    }

    @Test
    void getLocationByCoordsReturnsAddress() {
        queryParams = new HashMap<>();
        queryParams.put("key", GEOLOC_API_KEY);
        queryParams.put("latlng", "40.25,17.69");
        String resp = "formatted_address\"  : \"Some valid Address\"";
        when(http.getWithQuery(GEOLOC_API_ENDPOINT, queryParams)).thenReturn(resp);
        String location = service.getLocationByCoords(40.25, 17.69);
        String expected = "Some valid Address";
        assertEquals(expected, location);
    }

    @Test
    void getLocationByCoordsReturnsNullWhenJsonIsWrong() {
        queryParams = new HashMap<>();
        queryParams.put("key", GEOLOC_API_KEY);
        queryParams.put("latlng", "40.25,17.69");
        String resp = "nonsense info";
        when(http.getWithQuery(GEOLOC_API_ENDPOINT, queryParams)).thenReturn(resp);
        String location = service.getLocationByCoords(40.25, 17.69);
        assertNull(location);
    }
}