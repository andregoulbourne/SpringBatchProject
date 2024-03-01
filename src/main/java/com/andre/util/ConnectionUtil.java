package com.andre.util;

import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class ConnectionUtil {

		private static Connection conn = null;
		
		private static DataSource dataSource = null;
		
		private static final String PROPERTIESFILEPATH = "./src/main/resources/application.properties";
		
		private static final Logger log = Logger.getLogger(ConnectionUtil.class);
		
		private ConnectionUtil() {
			super();
		}
		
		// this is our getInstance() method
		public static Connection getConnection() {
		
			try {
				if (conn  != null && !conn.isClosed()) {
					return conn;
				}
				
				conn = getDataSource().getConnection();
			} catch (SQLException e) {
				log.error("We failed to reuse a Connection", e);
				return null;
			}



			return conn;
			
		}
		
		public static DataSource getDataSource(){
			if(dataSource != null)
				return dataSource;
			
			
			Properties prop = new Properties();

			String url= "";
			String username = "";
			String password = "";
			String driver = "";
			
			try(FileReader fileReader = new FileReader(new File(PROPERTIESFILEPATH));) {
				
				prop.load(fileReader);
				url = prop.getProperty("spring.datasource.url");
				username = prop.getProperty("spring.datasource.username");
				password = prop.getProperty("spring.datasource.password");
				driver = prop.getProperty("spring.datasource.driverClassName");
				
			    DriverManagerDataSource ds = new DriverManagerDataSource();
			    ds.setDriverClassName(driver);
			    ds.setUrl(url);
			    ds.setUsername(username);
			    ds.setPassword(password);
			    dataSource = ds;
			    log.info("Database connection extablished!");
			    return ds;
				
			} catch (Exception e) {
				log.error(Constants.EXCEPTION, e);
				return null;
			}
			
		}
		
}
