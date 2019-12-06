package com.github.therycn.tyweatherwebflux.api.service;

import com.github.therycn.tyweatherwebflux.api.entity.DailyWeather;
import com.github.therycn.tyweatherwebflux.api.exception.DailyWeatherNotFoundException;
import com.github.therycn.tyweatherwebflux.api.repository.DailyWeatherRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class DailyWeatherService {

    private DailyWeatherRepository dailyWeatherRepository;

    public DailyWeatherService(DailyWeatherRepository dailyWeatherRepository) {
        this.dailyWeatherRepository = dailyWeatherRepository;
    }

    public Flux<DailyWeather> getAll(){
        return dailyWeatherRepository.findAll();
    }

    public Mono<DailyWeather> get(String id) {
        return dailyWeatherRepository.findById(id)
                .switchIfEmpty(Mono.error(new DailyWeatherNotFoundException()));
    }

    public Mono<DailyWeather> getToday() {
        return dailyWeatherRepository.findFirstByDay(LocalDate.now())
                .switchIfEmpty(Mono.error(new DailyWeatherNotFoundException()));
    }

    public Mono<DailyWeather> save(DailyWeather dailyWeather) {
        return dailyWeatherRepository.save(dailyWeather);
    }
}
