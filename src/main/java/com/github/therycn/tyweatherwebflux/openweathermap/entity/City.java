package com.github.therycn.tyweatherwebflux.openweathermap.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * OpenWeatherMap City.
 * 
 * @author TheryLeopard
 *
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class City {

	/** city.id City ID */
	private Long id;

	/** city.name City name */
	private String name;

	/** city.coord */
	private Coordinate coord;

	/** city.country Country code (GB, JP etc.) */
	private String country;

}
