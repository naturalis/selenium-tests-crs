package nl.naturalis.selenium.crs.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.mysql.jdbc.Connection;

public class Report {
	
	private static String testName;
	private static String username;
	public enum LogLevel {
	    INFO, TRIVIAL, SEVERE, FATAL 
	}

	public static void setCredentials(String username, String password) {
		Report.username=username;
	}

	public static void setTestName(String name) {
		Report.testName=name;
	}

	public static void LogTestStart() {
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		System.out.println("[" + timeStamp + "] STARTED " + Report.testName);
	}

	public static void LogTestEnd() {
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		System.out.println("[" + timeStamp + "] FINISHED " + Report.testName);
	}

	public static void LogLine(String message) {
		System.out.println(Report.testName + ": "+  message );
	}

	public static void LogLine(String page, String message, String expectedvalue, String actualvalue, LogLevel severity) {
		System.out.println(Report.testName + ": "+ page + " {"+Report.username+"}: " + message + " (expected: " + expectedvalue + "; actual: " + actualvalue + ") [" + severity +"]");
	}

	public static void setTestName(Connection connection) {
	}
          
}
