package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

	private ParkingSpotDAO parkingSpotDAO = new ParkingSpotDAO();

	/**
	 * Calculate parking fare depending on recurrence and duration
	 * 
	 * if duration <= 30 min then free parking
	 * 
	 * if recurring customer then 5% discount
	 * 
	 * @param ticket
	 * @param fareType
	 */
	public void calculateFareByType(Ticket ticket, double fareType) {
		double diff = ticket.getOutTime().getTime() - ticket.getInTime().getTime();
		double duration = diff / 3600000;
		double freeTime = 0.5;
		if (duration > freeTime
				&& parkingSpotDAO.getRowsCountWithSameVehiculeNumber(ticket.getVehicleRegNumber()) > 1) {
			ticket.setPrice((duration * fareType) * (95d / 100));
		} else if (duration <= freeTime) {
			ticket.setPrice(0 * fareType);
		} else {
			ticket.setPrice(duration * fareType);
		}

	}

	/**
	 * Calculate parking fare depending on vehicle type
	 * 
	 * @param ticket
	 */
	public void calculateFare(Ticket ticket) {
		if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
			throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
		}
		switch (ticket.getParkingSpot().getParkingType()) {
		case CAR: {
			calculateFareByType(ticket, Fare.CAR_RATE_PER_HOUR);
			break;
		}
		case BIKE: {
			calculateFareByType(ticket, Fare.BIKE_RATE_PER_HOUR);
			break;
		}
		default:
			throw new IllegalArgumentException("Unkown Parking Type");
		}
	}

	/**
	 * Setter needed to make this class easily testable
	 * 
	 * @param parkingSpotDAO
	 */
	public void setParkingSpotDAO(ParkingSpotDAO parkingSpotDAO) {
		this.parkingSpotDAO = parkingSpotDAO;
	}
}