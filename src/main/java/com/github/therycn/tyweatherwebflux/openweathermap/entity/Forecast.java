package com.github.therycn.tyweatherwebflux.openweathermap.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * OpenWeatherMap Forecast.
 * 
 * @author TheryLeopard
 *
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Forecast {

	/** dt Time of data forecasted, unix, UTC */
	@JsonProperty("dt")
	private long time;

	/** main */
	private Main main;

	/** weather (more info Weather condition codes) */
	@JsonProperty("weather")
	private List<Weather> weatherList;

	/** wind */
	private Wind wind;

}
