package com.github.therycn.tyweatherwebflux.openweathermap.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * OpenWeatherMap forecast response.
 * 
 * @author TheryLeopard
 *
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherForecasts {

	/** city */
	private City city;

	/** list */
	@JsonProperty("list")
	private List<Forecast> forecastList;
}