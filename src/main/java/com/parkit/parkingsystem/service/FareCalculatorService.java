package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

	private ParkingSpotDAO parkingSpotDAO = new ParkingSpotDAO();

	public void calculateFareByType(Ticket ticket, double fareType) {
		double diff = ticket.getOutTime().getTime() - ticket.getInTime().getTime();
		double duration = diff / 1000;
		int freeTimeInSeconds = 1800;

		if (parkingSpotDAO.getRowsCountWithSameVehiculeNumber(ticket.getVehicleRegNumber()) > 0) {
			ticket.setPrice((duration * fareType) * (5d / 100));
		} else if (duration <= freeTimeInSeconds) {
			ticket.setPrice(0 * fareType);
		} else {
			ticket.setPrice(duration * fareType);
		}
	}

	public void calculateFare(Ticket ticket) {
		if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
			throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
		}
		switch (ticket.getParkingSpot().getParkingType()) {
		case CAR: {
			calculateFareByType(ticket, Fare.CAR_RATE_PER_SECOND);
			break;
		}
		case BIKE: {
			calculateFareByType(ticket, Fare.BIKE_RATE_PER_SECOND);
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