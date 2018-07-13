package com.danielcs.mercurynews.api;

import com.danielcs.mercurynews.models.Weather;
import com.danielcs.mercurynews.services.WeatherService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    private WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping
    public ResponseEntity<Weather> getCurrentWeather(
            @RequestParam(name = "country", required = false) String countryCode,
            @RequestParam(name = "latitude", required = false) Double latitude,
            @RequestParam(name = "longitude", required = false) Double longitude
    ) {
        if (countryCode != null) {
            return new ResponseEntity<>(weatherService.getWeatherByCountryCode(countryCode), HttpStatus.OK);
        }
        if (latitude != null && longitude != null) {
            return new ResponseEntity<>(weatherService.getWeatherByGeolocation(latitude, longitude), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
