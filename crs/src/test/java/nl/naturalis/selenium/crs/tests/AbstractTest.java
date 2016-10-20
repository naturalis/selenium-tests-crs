package nl.naturalis.selenium.crs.tests;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

import org.openqa.selenium.firefox.FirefoxDriver;
import com.mysql.jdbc.Connection;

import nl.naturalis.selenium.crs.utils.Report;
import nl.naturalis.selenium.crs.utils.YamlReader;
import nl.naturalis.selenium.crs.configuration.*;

public class AbstractTest {

	private static String configFile = "configuration/config.yaml";
	
	public static String testName;
	protected static FirefoxDriver driver;
	protected static Connection connection;
	protected static Configuration config;
	
	protected static void initializeDatabase()
	{
		
		YamlReader yamlReader = new YamlReader();
		yamlReader.setFile(configFile);
		Map settings = yamlReader.getData();
		Map database = (Map) settings.get("database");
		
        try {
        	DriverManager.registerDriver(new com.mysql.jdbc.Driver());
        	connection = (Connection) DriverManager.getConnection(settings.get("jdbc-url").toString()+database.get("db-name").toString(),database.get("username").toString(),database.get("password").toString());
        } catch (SQLException ex) {
        	Report.LogLine("initialization","failed to connect to database","connect to "+Database.getUsername()+":"+Database.getPassword()+"@"+Database.getUrl(),ex.getMessage(),Report.LogLevel.FATAL);
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