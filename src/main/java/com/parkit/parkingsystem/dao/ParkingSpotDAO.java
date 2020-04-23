package com.parkit.parkingsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;

public class ParkingSpotDAO {
	private static final Logger logger = LogManager.getLogger("ParkingSpotDAO");

	public DataBaseConfig dataBaseConfig = new DataBaseConfig();

	/**
	 * Counts how many times a vehicle registration number is stored in the DB
	 * 
	 * Used to know recurring customers
	 * 
	 * @param vehicleRegNumber
	 * @return result
	 */
	public int getRowsCountWithSameVehiculeNumber(String vehicleRegNumber) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int result = 0;
		try {
			con = dataBaseConfig.getConnection();
			ps = con.prepareStatement(DBConstants.COUNT_RECURRENT_VEHICULE);
			ps.setString(1, vehicleRegNumber);
			rs = ps.executeQuery();
			while (rs.next()) {
				result = rs.getInt("count");
			}
			return result;
		} catch (Exception ex) {
			logger.error("Error fetching next available slot", ex);
		} finally {
			dataBaseConfig.closeConnection(con);
			dataBaseConfig.closePreparedStatement(ps);
			dataBaseConfig.closeResultSet(rs);
		}
		return result;
	}

	/**
	 * Gives the next available parking slot
	 * 
	 * @param parkingType
	 * @return the next available parking slot
	 */
	public int getNextAvailableSlot(ParkingType parkingType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int result = -1;
		try {
			con = dataBaseConfig.getConnection();
			ps = con.prepareStatement(DBConstants.GET_NEXT_PARKING_SPOT);
			ps.setString(1, parkingType.toString());
			rs = ps.executeQuery();
			if (rs.next()) {
				result = rs.getInt(1);
			}
			dataBaseConfig.closeResultSet(rs);
			dataBaseConfig.closePreparedStatement(ps);
		} catch (Exception ex) {
			logger.error("Error fetching next available slot", ex);
		} finally {
			dataBaseConfig.closeConnection(con);
			dataBaseConfig.closePreparedStatement(ps);
			dataBaseConfig.closeResultSet(rs);
		}
		return result;
	}

	/**
	 * Update the availability for that parking slot
	 * 
	 * @param parkingSpot
	 */
	public boolean updateParking(ParkingSpot parkingSpot) {
		Connection con = null;
		PreparedStatement ps = null;
		boolean result = false;
		try {
			con = dataBaseConfig.getConnection();
			ps = con.prepareStatement(DBConstants.UPDATE_PARKING_SPOT);
			ps.setBoolean(1, parkingSpot.isAvailable());
			ps.setInt(2, parkingSpot.getId());
			int updateRowCount = ps.executeUpdate();
			result = (updateRowCount == 1);
		} catch (Exception ex) {
			logger.error("Error updating parking info", ex);

		} finally {
			dataBaseConfig.closeConnection(con);
			dataBaseConfig.closePreparedStatement(ps);
		}
		return result;
	}
}
