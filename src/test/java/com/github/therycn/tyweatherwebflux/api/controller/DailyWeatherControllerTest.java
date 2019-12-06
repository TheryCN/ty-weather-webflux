package com.github.therycn.tyweatherwebflux.api.controller;

import com.github.therycn.tyweatherwebflux.api.entity.DailyWeather;
import com.github.therycn.tyweatherwebflux.api.service.DailyWeatherService;
import com.github.therycn.tyweatherwebflux.openweathermap.OpenWeatherMapClient;
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

    private DailyWeatherController dailyWeatherController;

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

}