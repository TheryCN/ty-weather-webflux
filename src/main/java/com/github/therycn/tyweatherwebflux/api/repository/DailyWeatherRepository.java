package com.github.therycn.tyweatherwebflux.api.repository;

import com.github.therycn.tyweatherwebflux.api.entity.DailyWeather;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

public interface DailyWeatherRepository extends ReactiveMongoRepository<DailyWeather, String> {

    Mono<DailyWeather> findFirstByDay(LocalDate day);
}
