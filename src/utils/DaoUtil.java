package utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

import oracle.jdbc.pool.OracleDataSource;

public class DaoUtil {
public static OracleDataSource getDataSource(){
	InputStream inputStream = DaoUtil.class.getResourceAsStream("/jdbcEval.properties");
	Properties properties = new Properties();
	try {
		properties.load(inputStream);
	} catch (IOException e) {
		e.printStackTrace();
	}
	OracleDataSource dataSource;
	try {
		dataSource = new OracleDataSource();
		dataSource.setDriverType(properties.getProperty("driverClassName"));
		dataSource.setURL(properties.getProperty("url"));
		dataSource.setUser(properties.getProperty("username"));
		dataSource.setPassword(properties.getProperty("password"));
		dataSource.setExplicitCachingEnabled(true);
		Properties orclProperties = new Properties();
		orclProperties.setProperty("MinLimit", "2");
		orclProperties.setProperty("InitialLimit", "2");
		orclProperties.setProperty("MaxLimit", "40");
		dataSource.setConnectionProperties(orclProperties);
		return dataSource;
	} catch (SQLException e) {
		e.printStackTrace();
		return null;
	}
}
}
