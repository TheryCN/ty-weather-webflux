package com.github.therycn.tyweatherwebflux.openweathermap.entity;

import lombok.Data;

/**
 * Coordinate.
 * 
 * @author THERY
 *
 */
@Data
public class Coordinate {

	/** coord.lat City geo location, latitude */
	private double lat;

	/** coord.lon City geo location, longitude */
	private double lon;

}
