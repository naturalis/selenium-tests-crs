package nl.naturalis.selenium.crs.configuration;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import nl.naturalis.selenium.crs.utils.MissingConfigurationException;
import nl.naturalis.selenium.crs.utils.Report;

public class Configuration {

	private Connection connection;
	private String projectID;

	private static String domain;
	private static String homepage;
	private static String crs_username;
	private static String crs_password;
	private static String crs_instance;
	private static String browser_type;
	private static String browser_path;

	
	final public void setProjectID(String projectID) {
		this.projectID = projectID;
	}
	
	final public void setConnection(Connection connection) {
		this.connection = connection;
	}
	
	final public void populateConfiguration() throws MissingConfigurationException {

        Statement st = null;
        ResultSet rs = null;
        
        try {
            
        	st = (Statement) connection.createStatement();
        	rs = st.executeQuery("select * from selenium_settings where project = '"+this.projectID+"'");

            while (rs.next()) {
                if(rs.getString("setting").equals("domain")) domain=rs.getString("value");
                if(rs.getString("setting").equals("homepage")) homepage=rs.getString("value");
                if(rs.getString("setting").equals("crs_username")) crs_username=rs.getString("value");
                if(rs.getString("setting").equals("crs_password")) crs_password=rs.getString("value");
                if(rs.getString("setting").equals("crs_instance")) crs_instance=rs.getString("value");
                if(rs.getString("setting").equals("browser_type")) browser_type=rs.getString("value");
                if(rs.getString("setting").equals("browser_path")) browser_path=rs.getString("value");
            }

        } catch (SQLException ex) {

        	Report.LogLine("initialization","SQLException",ex.getMessage(),"",Report.LogLevel.FATAL);        

        } finally {
         
            try {
                
                if (rs!=null) rs.close();
                if (st!=null) st.close();

            } catch (SQLException ex) {
                
            	Report.LogLine("initialization","SQLException",ex.getMessage(),"",Report.LogLevel.TRIVIAL);
            }
        }
        
        if (getStartUrl().isEmpty()) throw new MissingConfigurationException("start URL is missing");
        if (getDomain().isEmpty()) throw new MissingConfigurationException("general domain is missing");
        if (getUsername().isEmpty()) throw new MissingConfigurationException("CRS username is missing");
        if (getPassword().isEmpty()) throw new MissingConfigurationException("CRS password is missing");
        if (getInstance().isEmpty()) throw new MissingConfigurationException("CRS instance is missing");
        if (getBrowserType().isEmpty()) throw new MissingConfigurationException("browser type is missing");
	}
	
	final public static String getStartUrl() {
		return homepage;
	}

	final public static String getDomain() {
		return domain;
	}

	final public static String getUsername() {
		return crs_username;
	}

	final public static String getPassword() {
		return crs_password;
	}

	final public static String getInstance() {
		return crs_instance;
	}

	final public static String getBrowserType() {
		return browser_type;
	}

	final public static String getBrowserPath() {
		return browser_path;
	}


}
