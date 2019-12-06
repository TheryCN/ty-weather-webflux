package com.github.therycn.tyweatherwebflux.api.controller;

import com.github.therycn.tyweatherwebflux.api.entity.DailyWeather;
import com.github.therycn.tyweatherwebflux.api.service.DailyWeatherService;
import com.github.therycn.tyweatherwebflux.openweathermap.OpenWeatherMapClient;
import com.github.therycn.tyweatherwebflux.openweathermap.entity.CurrentWeather;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@RestController
@Slf4j
public class DailyWeatherController {

    private DailyWeatherService dailyWeatherService;

    private OpenWeatherMapClient openWeatherMapClient;

    public DailyWeatherController(DailyWeatherService dailyWeatherService, OpenWeatherMapClient openWeatherMapClient) {
        this.dailyWeatherService = dailyWeatherService;
        this.openWeatherMapClient = openWeatherMapClient;
    }

    @GetMapping
    public Flux<DailyWeather> getAll() {
        return dailyWeatherService.getAll();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<DailyWeather>> get(@PathVariable String id) {
        return dailyWeatherService.get(id)
                .map(dailyWeather -> ResponseEntity.ok(dailyWeather))
                .onErrorReturn(ResponseEntity.notFound().build());
    }

    @GetMapping("/todaydb")
    public Mono<ResponseEntity<DailyWeather>> getTodayDb() {
        return dailyWeatherService.getToday()
                .map(dailyWeather -> ResponseEntity.ok(dailyWeather))
                .onErrorReturn(ResponseEntity.notFound().build());
    }

    @GetMapping("/today")
    public Mono<ResponseEntity<DailyWeather>> getToday() {
        return dailyWeatherService.getToday()
                .map(ResponseEntity::ok)
                .onErrorResume(error -> {
                    log.warn("Daily weather not found", error);
                    return getDailyWeatherResponseEntityMono();
                })
                .log();
    }

    private Mono<ResponseEntity<DailyWeather>> getDailyWeatherResponseEntityMono() {
        return openWeatherMapClient.getCurrentWeather()
                .flatMap(this::map)
                .flatMap(dailyWeather -> dailyWeatherService.save(dailyWeather))
                .map(dailyWeather -> ResponseEntity.ok(dailyWeather))
                .onErrorReturn(ResponseEntity.notFound().build())
                .log();
    }

    private Mono<DailyWeather> map(CurrentWeather currentWeather) {
        return Mono.just(new DailyWeather(LocalDate.now(), currentWeather.getName(), currentWeather.getMain().getTempMin(),
                currentWeather.getMain().getTempMax(), currentWeather.getMain().getPressure(), currentWeather.getMain().getHumidity()));
    }
}
