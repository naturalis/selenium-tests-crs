package tests;

import java.sql.DriverManager;
import java.sql.SQLException;
import org.openqa.selenium.firefox.FirefoxDriver;
import com.mysql.jdbc.Connection;

import utils.Report;
import configuration.*;

public class AbstractTest {

	public static String testName;
	protected static FirefoxDriver driver;
	protected static Connection connection;
	protected static Configuration config;
	
	protected static void initializeDatabase()
	{
        try {
        	connection = (Connection) DriverManager.getConnection(database.getUrl(), database.getUsername(), database.getPassword());
        } catch (SQLException ex) {
        	Report.LogLine("initialization","failed to connect to database","connect to "+database.getUsername()+":"+database.getPassword()+"@"+database.getUrl(),ex.getMessage(),Report.LogLevel.FATAL);
        }
	}

	protected static void initializeDriver()
	{
		if (Configuration.getBrowserPath()!=null) {
			System.setProperty("webdriver.firefox.bin",Configuration.getBrowserPath());
		}
		driver = new FirefoxDriver();
		//driver.manage().window().maximize();
	}

	protected static void initializeLogging()
	{
		Report.setCredentials(Configuration.getUsername(),Configuration.getPassword());
		Report.setTestName(getTestName());
	}

	protected static void initializeConfiguration(String projectId)
	{
		config = new Configuration();
		config.setProjectID(projectId);
		config.setConnection(connection);
		config.populateConfiguration();
	}
	
	protected static void tearDown()
	{
        try {
        	connection.close();	
        } catch (SQLException ex) {
        	//
        }
		driver.quit();	
	}

	protected static void setTestName(String name)
	{
		testName=name;
	}

	protected static String getTestName()
	{
		return testName;
	}
	
	protected static String getCurrentUrl()
	{
		return driver.getCurrentUrl();
	}

}