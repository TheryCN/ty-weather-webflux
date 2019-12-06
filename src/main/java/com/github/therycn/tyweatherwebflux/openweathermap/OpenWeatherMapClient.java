package com.github.therycn.tyweatherwebflux.openweathermap;

import com.github.therycn.tyweatherwebflux.openweathermap.entity.CurrentWeather;
import com.github.therycn.tyweatherwebflux.exception.FailureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class OpenWeatherMapClient {

    private String appId;

    private String queryBaseUrl;

    private WebClient webClient;

    public OpenWeatherMapClient(@Value("${openweathermap.appId}") String appId,
                                @Value("${openwheatermap.query.baseUrl}") String queryBaseUrl) {
        this.appId = appId;
        this.queryBaseUrl = queryBaseUrl;
        this.webClient = WebClient.builder().baseUrl(queryBaseUrl).build();
    }

    public Mono<CurrentWeather> getCurrentWeather() {
        return webClient.get()
                .uri("/weather?q={city}&units=metric&appid={appId}", "Grenoble", appId)
                .retrieve()
                .bodyToMono(CurrentWeather.class)
                .doOnError(error -> new FailureException(error.getMessage(), error));
    }

}
