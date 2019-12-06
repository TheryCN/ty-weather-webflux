package com.github.therycn.tyweatherwebflux.api.service;

import com.github.therycn.tyweatherwebflux.api.entity.DailyWeather;
import com.github.therycn.tyweatherwebflux.api.exception.DailyWeatherNotFoundException;
import com.github.therycn.tyweatherwebflux.api.repository.DailyWeatherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

class DailyWeatherServiceTest {

    private DailyWeatherRepository dailyWeatherRepository;

    private DailyWeatherService dailyWeatherService;

    @BeforeEach
    void setUp() {
        dailyWeatherRepository = Mockito.mock(DailyWeatherRepository.class);
        dailyWeatherService = new DailyWeatherService(dailyWeatherRepository);
    }

    @Test
    void whenGetAll_thenReturnDailyWeatherFlux() {
        // Given
        List<DailyWeather> dailyWeatherList = Arrays.asList(new DailyWeather());
        Flux<DailyWeather> expectedDailyWeatherFlux = Flux.fromIterable(dailyWeatherList);
        Mockito.when(dailyWeatherRepository.findAll()).thenReturn(expectedDailyWeatherFlux);

        // When
        Flux<DailyWeather> dailyWeatherFlux = dailyWeatherService.getAll();

        // Then
        StepVerifier.create(dailyWeatherFlux)
                .expectNextMatches(dailyWeather -> dailyWeather.equals(dailyWeatherList.get(0)))
                .expectNextCount(0l)
                .verifyComplete();
    }

    @Test
    void whenGetAll_thenReturnEmptyFlux() {
        // Given
        Mockito.when(dailyWeatherRepository.findAll()).thenReturn(Flux.empty());

        // When
        Flux<DailyWeather> dailyWeatherFlux = dailyWeatherService.getAll();

        // Then
        StepVerifier.create(dailyWeatherFlux)
                .expectNextCount(0l)
                .verifyComplete();
    }

    @Test
    void whenGet_thenReturnDailyWeatherMono() {
        // Given
        String dailyWeatherId = "first";
        DailyWeather dailyWeather = new DailyWeather();
        Mockito.when(dailyWeatherRepository.findById(dailyWeatherId)).thenReturn(Mono.just(dailyWeather));

        // When
        Mono<DailyWeather> dailyWeatherMono = dailyWeatherService.get(dailyWeatherId);

        // Then
        StepVerifier.create(dailyWeatherMono)
                .expectNext(dailyWeather)
                .verifyComplete();
    }

    @Test
    void whenGet_thenReturnNotFoundMono() {
        // Given
        String dailyWeatherId = "first";
        Mockito.when(dailyWeatherRepository.findById(dailyWeatherId)).thenReturn(Mono.empty());

        // When
        Mono<DailyWeather> dailyWeatherMono = dailyWeatherService.get(dailyWeatherId);

        // Then
        StepVerifier.create(dailyWeatherMono)
                .expectErrorMatches(exception -> exception instanceof DailyWeatherNotFoundException)
                .verify();
    }

    @Test
    void whenSave_thenReturnDailyWeatherSaved() {
        // Given
        DailyWeather dailyWeatherToSave = new DailyWeather(LocalDate.now(), "Grenoble", 0, 6, 0, 0);
        DailyWeather dailyWeatherSaved = new DailyWeather("1", LocalDate.now(), "Grenoble", 0, 6, 0, 0, null, null);

        Mockito.when(dailyWeatherRepository.save(dailyWeatherToSave)).thenReturn(Mono.just(dailyWeatherSaved));
        // When
        Mono<DailyWeather> dailyWeatherMono = dailyWeatherService.save(dailyWeatherToSave);

        // Then
        StepVerifier.create(dailyWeatherMono)
                .expectNext(dailyWeatherSaved)
                .verifyComplete();
    }

    @Test
    void whenGetToday_thenReturnDailyWeatherMono() {
        // Given
        DailyWeather dailyWeather = new DailyWeather();
        Mockito.when(dailyWeatherRepository.findFirstByDay(LocalDate.now())).thenReturn(Mono.just(dailyWeather));

        // When
        Mono<DailyWeather> dailyWeatherMono = dailyWeatherService.getToday();

        // Then
        StepVerifier.create(dailyWeatherMono)
                .expectNext(dailyWeather)
                .verifyComplete();
    }

    @Test
    void whenGetToday_thenReturnNotFoundMono() {
        // Given
        Mockito.when(dailyWeatherRepository.findFirstByDay(LocalDate.now())).thenReturn(Mono.empty());

        // When
        Mono<DailyWeather> dailyWeatherMono = dailyWeatherService.getToday();

        // Then
        StepVerifier.create(dailyWeatherMono)
                .expectErrorMatches(exception -> exception instanceof DailyWeatherNotFoundException)
                .verify();
    }
}