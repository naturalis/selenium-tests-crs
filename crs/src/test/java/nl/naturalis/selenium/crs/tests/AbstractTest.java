package nl.naturalis.selenium.crs.tests;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import com.mysql.jdbc.Connection;

import nl.naturalis.selenium.crs.utils.MissingConfigurationException;
import nl.naturalis.selenium.crs.utils.Report;
import nl.naturalis.selenium.crs.utils.YamlReader;
import nl.naturalis.selenium.crs.configuration.*;

public class AbstractTest {

	private static String configFile = "configuration/config.yaml";
	
	public static String testName;
	protected static WebDriver driver;
	protected static Connection connection;
	protected static Configuration config;
	
	protected static void initializeDatabase() throws SQLException
	{
		YamlReader yamlReader = new YamlReader();
		yamlReader.setFile(configFile);
		Map settings = yamlReader.getData();
		Map database = (Map) settings.get("database");
		
    	DriverManager.registerDriver(new com.mysql.jdbc.Driver());
    	connection = (Connection) DriverManager.getConnection(settings.get("jdbc-url").toString()+database.get("db-name").toString(),database.get("username").toString(),database.get("password").toString());
	}

	protected static void initializeDriver()
	{	
		String browserType = config.getBrowserType();
		
		if (browserType.equals("Firefox")) {
			if (Configuration.getBrowserPath()!=null) {
				System.setProperty("webdriver.firefox.bin",Configuration.getBrowserPath());
			}
			driver = new FirefoxDriver();
		}
		else
		if (browserType.equals("Gecko")) {
			if (Configuration.getBrowserPath()!=null) {
				System.setProperty("webdriver.gecko.driver",Configuration.getBrowserPath());
			}
			driver = new FirefoxDriver();
		}
		else
		if (browserType.equals("Chrome"))
		{
			if (Configuration.getBrowserPath()!=null) {
				System.setProperty("webdriver.chromedriver.bin",Configuration.getBrowserPath());
			}
			driver = new ChromeDriver();
		}
	
	}

	protected static void initializeWaiting(int seconds) {
		driver.manage().timeouts().implicitlyWait(seconds, TimeUnit.SECONDS);
	}
	
	protected static void initializeLogging()
	{
		Report.setCredentials(Configuration.getUsername(),Configuration.getPassword());
		Report.setTestName(getTestName());
	}

	protected static void initializeConfiguration(String projectId) throws MissingConfigurationException
	{
		config = new Configuration();
		config.setProjectID(projectId);
		config.setConnection(connection);
		config.populateConfiguration();
	}
	
	protected static void tearDown() throws SQLException
	{
		connection.close();	
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