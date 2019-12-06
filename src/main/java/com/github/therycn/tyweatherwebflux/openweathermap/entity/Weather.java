package com.github.therycn.tyweatherwebflux.openweathermap.entity;

import lombok.Data;

/**
 * OpenWeatherMap Weather.
 * 
 * @author TheryLeopard
 *
 */
@Data
public class Weather {

	/** weather.id Weather condition id */
	private int id;

	/** weather.main Group of weather parameters (Rain, Snow, Extreme etc.) */
	private String main;

	/** weather.description Weather condition within the group */
	private String description;

	/** weather.icon Weather icon id */
	private String icon;

}
