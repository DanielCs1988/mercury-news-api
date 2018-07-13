package com.danielcs.mercurynews.services;

import com.danielcs.mercurynews.httputils.HttpRequestAssembler;
import com.danielcs.mercurynews.models.ArticleDTO;
import com.danielcs.mercurynews.models.Geolocation;
import com.danielcs.mercurynews.models.NewsResultWrapper;
import com.danielcs.mercurynews.models.Weather;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class HttpRequest {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpRequest.class);
    private static final String NEWS_API_ENDPOINT = "https://newsapi.org/v2/top-headlines";
    private static final String NEWS_API_KEY = System.getenv("NEWS_API_KEY");
    private static final String GEOLOC_API_ENDPOINT = "https://maps.googleapis.com/maps/api/geocode/json";
    private static final String GEOLOC_API_KEY = System.getenv("GEOLOC_API_KEY");
    private static final String WEATHER_API_URL = System.getenv("WEATHER_API_URL");

    @HttpRequestAssembler
    private HttpRequestSender http;

    public List<ArticleDTO> fetchArticles(Map<String, String> options) {
        options.put("apiKey", NEWS_API_KEY);
        NewsResultWrapper result = http.getJsonWithQuery(NEWS_API_ENDPOINT, options);
        if (result != null) {
            return result.getArticles().stream().map(ArticleDTO::new).collect(Collectors.toList());
        }
        LOGGER.error("News API rejected the request!");
        return new ArrayList<>();
    }

    public Geolocation getGeolocByCountryCode(String code) {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("key", GEOLOC_API_KEY);
        queryParams.put("components", "country:" + code);
        String result = http.getWithQuery(GEOLOC_API_ENDPOINT, queryParams);
        Matcher matcher = getJsonMatcher(
                result, "formatted_address\":\"([a-zA-Z-]+)\".*location\":\\{\"lat\":([\\d.-]+),\"lng\":([\\d.-]+)", true
        );
        if (matcher.find()) {
            return new Geolocation(
                    matcher.group(1),  // full name of country
                    Double.valueOf(matcher.group(2)),  // latitude
                    Double.valueOf(matcher.group(3))  // longitude
            );
        }
        return null;
    }

    public Weather getWeatherByGeolocation(String location, double latitude, double longitude) {
        String result = http.get(WEATHER_API_URL + latitude + "," + longitude);
        Matcher matcher = getJsonMatcher(
                result, "currently.{1,30}\"summary\":\"([a-zA-Z-]+)\",\"icon\":\"([a-zA-Z-]+)\",.{1,100}temperature\":([\\d.]+),", true
        );
        if (matcher.find()) {
            return new Weather(
                    location,
                    matcher.group(1),  // weather state summary
                    matcher.group(2),  // weather state icon name for the frontend rendering
                    Double.valueOf(matcher.group(3))  // temperature in Fahrenheit, will be converted by the model
            );
        }
        return null;
    }

    public String getLocationByCoords(double latitude, double longitude) {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("key", GEOLOC_API_KEY);
        queryParams.put("latlng", latitude + "," + longitude);
        String result = http.getWithQuery(GEOLOC_API_ENDPOINT, queryParams);
        Matcher matcher = getJsonMatcher(result, "formatted_address\"\\s*:\\s*\"([^\"]+)\"", false);
        if (matcher.find()) {
            return matcher.group(1); // The location
        }
        return null;
    }

    private Matcher getJsonMatcher(String json, String regex, boolean shouldCompress) {
        if (shouldCompress) {
            json = json.replaceAll("\\s", "");
        }
        System.out.println(json);
        return Pattern.compile(regex).matcher(json);
    }

}
