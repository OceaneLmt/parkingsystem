package com.parkit.parkingsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;

@ExtendWith(MockitoExtension.class)
public class FareCalculatorServiceTest {

	private FareCalculatorService fareCalculatorService;
	private Ticket ticket;

	@Mock
	private static ParkingSpotDAO parkingSpotDAO = new ParkingSpotDAO();

	@BeforeEach
	private void setUpPerTest() {
		ticket = new Ticket();
		fareCalculatorService = new FareCalculatorService();
	}

	/**
	 * Set up for
	 * "Given_RecurringCustomer_When_FareIsCalculated_Then_ShouldHaveAFivePercentDiscount"
	 * test: Vehicle registration number is present twice in data base
	 */
	private void setUpMockito() {
		try {
			when(parkingSpotDAO.getRowsCountWithSameVehiculeNumber(null)).thenReturn(2);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to set up test mock objects");
		}

	}

	@Test
	public void Given_RecurringCustomer_When_FareIsCalculated_Then_ShouldHaveAFivePercentDiscount() {
		// GIVEN car parking time is 1 hour and vehicle registration number is already
		// known (recurring customer)
		setUpMockito();
		fareCalculatorService.setParkingSpotDAO(parkingSpotDAO);
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		// WHEN fare is calculated
		fareCalculatorService.calculateFare(ticket);
		// THEN car parking fare should equal 95% of car rate per hour
		assertEquals(ticket.getPrice(), Fare.CAR_RATE_PER_HOUR * (95d / 100));
	}

	/**
	 * Calculate car parking fare for one hour parking
	 */
	@Test
	public void Given_CarParkedForOneHour_When_FareIsCalculated_Then_ParkingFareShouldEqualCarRatePerHour() {
		// GIVEN car parking time is 1 hour
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		// WHEN fare is calculated
		fareCalculatorService.calculateFare(ticket);
		// THEN car parking fare should equal car rate per hour
		assertEquals(Fare.CAR_RATE_PER_HOUR, ticket.getPrice());
	}

	/**
	 * Calculate bike parking fare for one hour parking
	 */
	@Test
	public void Given_BikeParkedForOneHour_When_FareIsCalculated_Then_ParkingFareShouldEqualBikeRatePerHour() {
		// GIVEN bike parking time is 1 hour
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		// WHEN fare is calculated
		fareCalculatorService.calculateFare(ticket);
		// THEN bike parking fare should equal bike rate per hour
		assertEquals(Fare.BIKE_RATE_PER_HOUR, ticket.getPrice());
	}

	/**
	 * Calculate bike parking fare for less than one hour parking
	 */
	@Test
	public void Given_BikeParkedForFortyFiveMinutes_When_FareIsCalculated_Then_ParkingFareShouldEqualSeventyFivePercentOfBikeRatePerHour() {
		// GIVEN bike parking time is 45 minutes
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		// WHEN fare is calculated
		fareCalculatorService.calculateFare(ticket);
		// THEN bike parking fare should equal 75% of bike parking rate per hour
		assertEquals((0.75 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
	}

	/**
	 * Calculate car parking fare for less than one hour parking
	 */
	@Test
	public void Given_CarParkedForFortyFiveMinutes_When_FareIsCalculated_Then_ParkingFareShouldEqualSeventyFivePercentOfCarRatePerHour() {
		// GIVEN car parking time is 45minutes
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		// WHEN fare is calculated
		fareCalculatorService.calculateFare(ticket);
		// THEN car parking fare should equals 75% of car parking rate per hour
		assertEquals((0.75 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
	}

	/**
	 * Calculate car parking fare for less than 30 minutes parking
	 */
	@Test
	public void Given_CarParkedForTwentyFiveMinutes_When_FareIsCalculated_Then_ParkingFareShouldBeFree() {
		// GIVEN car parking time is 25 minutes
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (25 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		// WHEN fare is calculated
		fareCalculatorService.calculateFare(ticket);
		// THEN car parking fare should be free
		assertEquals((0 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());

	}

	/**
	 * Calculate bike parking fare for less than 30 minutes parking
	 */
	@Test
	public void Given_BikeParkedForTwentyFiveMinutes_When_FareIsCalculated_Then_ParkingFareShouldBeFree() {
		// GIVEN bike parking time is 25 minutes
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (25 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		// WHEN fare is calculated
		fareCalculatorService.calculateFare(ticket);
		// THEN bike parking fare should be free
		assertEquals((0 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
	}

	/**
	 * Calculate car parking fare for exactly 30 minutes parking
	 */
	@Test
	public void Given_CarParkedForThirtyMinutes_When_FareIsCalculated_Then_ParkingFareShouldBeFree() {
		// GIVEN car parking time is 30 minutes
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (30 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		// WHEN fare is calculated
		fareCalculatorService.calculateFare(ticket);
		// THEN car parking fare should be free
		assertEquals((0 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
	}

	/**
	 * Calculate bike parking fare for exactly 30 minutes parking
	 */
	@Test
	public void Given_BikeParkedForThirtyMinutes_When_FareIsCalculated_Then_ParkingFareShouldBeFree() {
		// GIVEN bike parking time is 30 minutes
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (30 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		// WHEN fare is calculated
		fareCalculatorService.calculateFare(ticket);
		// THEN bike parking fare should be free
		assertEquals((0 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
	}

	/**
	 * Calculate car parking fare for 24 hours parking
	 */
	@Test
	public void Given_CarParkedForTwentyFourHours_When_FareIsCalculated_Then_ParkingFareShouldEqualTwentyFourTimesCarRatePerHour() {
		// GIVEN car parking time is 24 hours
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (24 * 60 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		// WHEN fare is calculated
		fareCalculatorService.calculateFare(ticket);
		// THEN car parking fare should equal 24 * car rate per hour
		assertEquals((24 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
	}

	/**
	 * Asserts a {@link NullPointerException} is thrown when parking type is unknown
	 * (null).
	 */
	@Test
	public void Given_ParkingTypeIsUnknown_When_FareIsCalculated_Then_ShouldThrowANullPointerException() {
		// GIVEN parking time is 1hour and parking type is unknown (null)
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, null, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		// WHEN fare is calculated
		// THEN should throw a NullPointerException
		assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
	}

	/**
	 * Asserts an {@link IllegalArgumentException} is thrown when parking entrance
	 * time is wrong (future inTime).
	 */
	@Test
	public void Given_EntranceParkingTimeIsWrong_When_FareIsCalculated_Then_ShouldThrowAnIllegalArgumentException() {
		// GIVEN parking entrance time is 1 hour into future
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() + (60 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		// WHEN fare is calculated
		// THEN should throw an IllegalArgumentException
		assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
	}

	/**
	 * Asserts an {@link IllegalArgumentException} is thrown when parkingType is
	 * incorrect.
	 */
	@Test
	public void Given_ParkingTypeIsWrong_When_FareIsCalculated_Then_ShouldThrowAnIllegalArgumentException()
			throws Exception {
		// GIVEN parking time is 1hour and parking type is Boat
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BOAT, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		// WHEN fare is calculated
		// THEN should throw an IllegalArgumentException
		assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
	}

	/**
	 * Asserts a {@link NullPointerException} is thrown when parking exit time is
	 * null.
	 */
	@Test
	public void Given_ExitParkingTimeIsNull_When_FareIsCalculated_Then_ShouldThrowANullPointerException() {
		// GIVEN car exits parking time is null
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
		Date outTime = null;
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		// WHEN fare is calculated
		// THEN should throw a NullPointerException
		assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
	}
}
