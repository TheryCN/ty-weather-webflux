package com.github.therycn.tyweatherwebflux.api.controller;

import com.github.therycn.tyweatherwebflux.api.entity.DailyWeather;
import com.github.therycn.tyweatherwebflux.api.exception.DailyWeatherNotFoundException;
import com.github.therycn.tyweatherwebflux.api.service.DailyWeatherService;
import com.github.therycn.tyweatherwebflux.openweathermap.OpenWeatherMapClient;
import com.github.therycn.tyweatherwebflux.openweathermap.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@WebFluxTest(DailyWeatherController.class)
class DailyWeatherControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private DailyWeatherService dailyWeatherService;

    @MockBean
    private OpenWeatherMapClient openWeatherMapClient;

    @BeforeEach
    void setUp() {
        webTestClient = webTestClient.mutate().baseUrl("/").build();
    }

    @Test
    public void whenGetAll_thenReturnDailyWeatherFlux() {
        // Given
        List<DailyWeather> dailyWeatherList = Arrays.asList(new DailyWeather());
        Flux<DailyWeather> expectedDailyWeatherFlux = Flux.fromIterable(dailyWeatherList);
        Mockito.when(dailyWeatherService.getAll()).thenReturn(expectedDailyWeatherFlux);

        // When + Then
        webTestClient.get().exchange().expectStatus().isOk()
                .expectBodyList(DailyWeather.class)
                .isEqualTo(dailyWeatherList);
    }

    @Test
    void whenGet_thenReturnDailyWeatherResponseEntityMonoOk() {
        // Given
        String dailyWeatherId = "1";
        DailyWeather existingDailyWeather = new DailyWeather(dailyWeatherId, LocalDate.now(), "Grenoble", 0, 6, 0, 0, null, null);
        Mockito.when(dailyWeatherService.get(dailyWeatherId)).thenReturn(Mono.just(existingDailyWeather));

        // When + Then
        webTestClient.get().uri("/{id}", dailyWeatherId)
                .exchange().expectStatus().isOk()
                .expectBody(DailyWeather.class)
                .isEqualTo(existingDailyWeather);
    }

    @Test
    void whenGet_thenReturnDailyWeatherResponseEntityMonoNotFound() {
        // Given
        String dailyWeatherId = "1";
        Mockito.when(dailyWeatherService.get(dailyWeatherId)).thenReturn(Mono.error(new DailyWeatherNotFoundException()));

        // When + Then
        webTestClient.get().uri("/{id}", dailyWeatherId)
                .exchange().expectStatus().isNotFound();
    }

    @Test
    void whenGetTodayInDb_thenReturnDailyWeatherResponseEntityMonoOk() {
        // Given
        DailyWeather existingDailyWeather = new DailyWeather("1", LocalDate.now(), "Grenoble", 0, 6, 0, 0, null, null);
        Mockito.when(dailyWeatherService.getToday()).thenReturn(Mono.just(existingDailyWeather));

        // When + Then
        webTestClient.get().uri("/today")
                .exchange().expectStatus().isOk()
                .expectBody(DailyWeather.class)
                .isEqualTo(existingDailyWeather);
    }

    @Test
    void whenGetTodayNotInDb_thenReturnDailyWeatherResponseEntityMonoOk() {
        // Given
        Mockito.when(dailyWeatherService.getToday()).thenReturn(Mono.error(new DailyWeatherNotFoundException()));

        CurrentWeather currentWeather = new CurrentWeather("Grenoble", new Coordinate(), Arrays.asList(new Weather()), new Main(0, 0,0,0,0), new Wind());
        Mockito.when(openWeatherMapClient.getCurrentWeather()).thenReturn(Mono.just(currentWeather));

        DailyWeather dailyWeather = new DailyWeather(null, LocalDate.now(), "Grenoble", 0, 0, 0, 0, null, null);
        DailyWeather expectedDailyWeather = new DailyWeather("1", LocalDate.now(), "Grenoble", 0, 0, 0, 0, null, null);
        Mockito.when(dailyWeatherService.save(dailyWeather)).thenReturn(Mono.just(expectedDailyWeather));

        // When + Then
        webTestClient.get().uri("/today")
                .exchange().expectStatus().isOk()
                .expectBody(DailyWeather.class)
                .isEqualTo(expectedDailyWeather);
    }


    @Test
    void whenGetTodayNotInDb_thenReturnDailyWeatherResponseEntityMonoKo() {
        // Given
        Mockito.when(dailyWeatherService.getToday()).thenReturn(Mono.error(new DailyWeatherNotFoundException()));

        CurrentWeather currentWeather = new CurrentWeather("Grenoble", new Coordinate(), Arrays.asList(new Weather()), new Main(0, 0,0,0,0), new Wind());
        Mockito.when(openWeatherMapClient.getCurrentWeather()).thenReturn(Mono.error(new DailyWeatherNotFoundException()));

        // When + Then
        webTestClient.get().uri("/today")
                .exchange().expectStatus().isNotFound();
    }
}