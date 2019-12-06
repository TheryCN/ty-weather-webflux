package com.github.therycn.tyweatherwebflux.openweathermap.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Main {

	/**
	 * main.temp Temperature. Unit Default: Kelvin, Metric: Celsius, Imperial:
	 * Fahrenheit.
	 */
	private float temp;

	/**
	 * main.temp_min Minimum temperature at the moment of calculation. This is
	 * deviation from 'temp' that is possible for large cities and megalopolises
	 * geographically expanded (use these parameter optionally). Unit Default:
	 * Kelvin, Metric: Celsius, Imperial: Fahrenheit.
	 */
	@JsonProperty("temp_min")
	private float tempMin;

	/**
	 * main.temp_max Maximum temperature at the moment of calculation. This is
	 * deviation from 'temp' that is possible for large cities and megalopolises
	 * geographically expanded (use these parameter optionally). Unit Default:
	 * Kelvin, Metric: Celsius, Imperial: Fahrenheit.
	 */
	@JsonProperty("temp_max")
	private float tempMax;

	/** main.pressure Atmospheric pressure on the sea level by default, hPa */
	private float pressure;

	/** main.humidity Humidity, % */
	private float humidity;

}