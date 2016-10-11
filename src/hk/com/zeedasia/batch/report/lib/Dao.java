package hk.com.zeedasia.batch.report.lib;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import hk.com.zeedasia.batch.report.constant.BatchConstants;
import hk.com.zeedasia.framework.util.PropertiesUtils;

public class Dao {
	private String host;
	private String schema;
	private String user;
	private String password;

	protected Connection connection = null;

	public Dao() {
		this(null);
	}

	public Dao(Properties prop) {
		if (prop == null) {
			try {
				prop = PropertiesUtils.getProperties(BatchConstants.DAO_PROPERTIES_FILE);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		host = prop.getProperty(BatchConstants.PROP_DB_HOST);
		schema = prop.getProperty(BatchConstants.PROP_DB_SCHEMA);
		user = prop.getProperty(BatchConstants.PROP_DB_USER);
		password = prop.getProperty(BatchConstants.PROP_DB_PASSWORD);

		try {
			// Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			// Open a connection
			String url = "jdbc:mysql://" + host + "/" + schema + "?zeroDateTimeBehavior=convertToNull";
			connection = DriverManager.getConnection(url, user, password);
			connection.setSchema(schema);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
