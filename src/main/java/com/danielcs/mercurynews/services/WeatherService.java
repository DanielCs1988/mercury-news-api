package com.danielcs.mercurynews.services;

import com.danielcs.mercurynews.models.Geolocation;
import com.danielcs.mercurynews.models.Weather;
import org.springframework.stereotype.Service;

@Service
public class WeatherService {

    private HttpRequest http;

    public WeatherService(HttpRequest http) {
        this.http = http;
    }

    public Weather getWeatherByCountryCode(String countryCode) {
        Geolocation geoloc = http.getGeolocByCountryCode(countryCode);
        return getWeatherByGeolocation(geoloc);
    }

    private Weather getWeatherByGeolocation(Geolocation geoloc) {
        return http.getWeatherByGeolocation(geoloc.getLocation(), geoloc.getLatitude(), geoloc.getLongitude());
    }

    public Weather getWeatherByGeolocation(double latitude, double longitude) {
        String location = http.getLocationByCoords(latitude, longitude);
        return http.getWeatherByGeolocation(location, latitude, longitude);
    }
}
