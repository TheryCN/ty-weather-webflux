package com.github.therycn.tyweatherwebflux.openweathermap.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Current Weather Response.
 * 
 * @author THERY
 *
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class CurrentWeather {

	/** name City name */
	private String name;

	/** coord */
	private Coordinate coord;

	/** weather (more info Weather condition codes) */
	@JsonProperty("weather")
	private List<Weather> weatherList;

	/** main */
	private Main main;

	/** wind */
	private Wind wind;

}
