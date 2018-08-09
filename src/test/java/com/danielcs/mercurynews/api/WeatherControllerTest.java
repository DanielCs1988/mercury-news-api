package com.danielcs.mercurynews.api;

import com.danielcs.mercurynews.models.Weather;
import com.danielcs.mercurynews.services.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WeatherControllerTest {

    private WeatherController controller;
    private WeatherService weatherService;
    private final Weather weather = new Weather("here", "SUPER hot", "blazing sun", 40.0);

    @BeforeEach
    void setUp() {
        weatherService = mock(WeatherService.class);
        controller = new WeatherController(weatherService);
    }

    @Test
    void getCurrentWeatherReturnsWeatherByCountryCode() {
        when(weatherService.getWeatherByCountryCode("hu")).thenReturn(weather);
        ResponseEntity<?> resp = controller.getCurrentWeather("hu", null, null);
        ResponseEntity<?> expected = new ResponseEntity<>(weather, HttpStatus.OK);
        assertEquals(expected, resp);
    }

    @Test
    void getCurrentWeatherReturnsWeatherByGeolocation() {
        when(weatherService.getWeatherByGeolocation(40.0, 20.0)).thenReturn(weather);
        ResponseEntity<?> resp = controller.getCurrentWeather(null, 40.0, 20.0);
        ResponseEntity<?> expected = new ResponseEntity<>(weather, HttpStatus.OK);
        assertEquals(expected, resp);
    }

    @Test
    void getCurrentWeatherThrowsWhenNoParamsReceived() {
        ResponseEntity<?> resp = controller.getCurrentWeather(null, null, null);
        assertEquals(new ResponseEntity<>(HttpStatus.BAD_REQUEST), resp);
    }
}