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

	public boolean updateParking(ParkingSpot parkingSpot) {
		// update the availability for that parking slot
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = dataBaseConfig.getConnection();
			ps = con.prepareStatement(DBConstants.UPDATE_PARKING_SPOT);
			ps.setBoolean(1, parkingSpot.isAvailable());
			ps.setInt(2, parkingSpot.getId());
			int updateRowCount = ps.executeUpdate();
			return (updateRowCount == 1);
		} catch (Exception ex) {
			logger.error("Error updating parking info", ex);
			return false;
		} finally {
			dataBaseConfig.closeConnection(con);
			if (ps != null) {
				dataBaseConfig.closePreparedStatement(ps);
			}
		}
	}
}
