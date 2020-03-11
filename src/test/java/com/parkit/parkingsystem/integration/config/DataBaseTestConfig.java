package com.parkit.parkingsystem.integration.config;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.parkit.parkingsystem.config.DataBaseConfig;

public class DataBaseTestConfig extends DataBaseConfig {

	private static final Logger logger = LogManager.getLogger("DataBaseTestConfig");
	private static final String dbConfigFilePath = "./DBConfig.properties";

	@Override
	public Connection getConnection() throws ClassNotFoundException, SQLException {
		logger.info("Create DB connection");
		Class.forName("com.mysql.cj.jdbc.Driver");
		return DriverManager.getConnection(dataBaseProperties().getProperty("db.url"),
				dataBaseProperties().getProperty("db.user"), dataBaseProperties().getProperty("db.password"));
	}

	@Override
	public void closeConnection(Connection con) {
		if (con != null) {
			try {
				con.close();
				logger.info("Closing DB connection");
			} catch (SQLException e) {
				logger.error("Error while closing connection", e);
			}
		}
	}

	@Override
	public void closePreparedStatement(PreparedStatement ps) {
		if (ps != null) {
			try {
				ps.close();
				logger.info("Closing Prepared Statement");
			} catch (SQLException e) {
				logger.error("Error while closing prepared statement", e);
			}
		}
	}

	@Override
	public void closeResultSet(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
				logger.info("Closing Result Set");
			} catch (SQLException e) {
				logger.error("Error while closing result set", e);
			}
		}
	}

	private Properties dataBaseProperties() {
		Properties prop = new Properties();
		try (OutputStream output = new FileOutputStream(dbConfigFilePath)) {

			// set the properties value
			prop.setProperty("db.url",
					"jdbc:mysql://localhost:3306/prod?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC");
			prop.setProperty("db.user", "root");
			prop.setProperty("db.password", "rootroot");

			// save properties to project root folder
			prop.store(output, null);
		} catch (IOException io) {
			logger.error("Failed to store data base properties", io);
		}
		return prop;
	}
}
