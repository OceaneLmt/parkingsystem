package com.parkit.parkingsystem.constants;

/**
 * This class initializes vehicles parking rates per hour and per second
 */
public class Fare {
	public static final double BIKE_RATE_PER_HOUR = 1.0;
	public static final double CAR_RATE_PER_HOUR = 1.5;
	public static final double CAR_RATE_PER_SECOND = CAR_RATE_PER_HOUR / 3600;
	public static final double BIKE_RATE_PER_SECOND = BIKE_RATE_PER_HOUR / 3600;
}
