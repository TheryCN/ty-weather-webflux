package com.github.therycn.tyweatherwebflux.openweathermap.entity;

import lombok.Data;

/**
 * Wind.
 * 
 * @author THERY
 *
 */
@Data
public class Wind {

	/**
	 * wind.speed Wind speed. Unit Default: meter/sec, Metric: meter/sec, Imperial:
	 * miles/hour.
	 */
	private double speed;

	/** wind.deg Wind direction, degrees (meteorological) */
	private double deg;
}
