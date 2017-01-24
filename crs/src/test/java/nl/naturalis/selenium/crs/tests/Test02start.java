package nl.naturalis.selenium.crs.tests;

import org.testng.TestListenerAdapter;
import org.testng.TestNG;

import nl.naturalis.selenium.crs.configuration.Configuration;

public class Test02start extends AbstractTest {

	public static void main(String[] args) {
		TestListenerAdapter tla = new TestListenerAdapter();
		TestNG testng = new TestNG();
		testng.setTestClasses(new Class[] { Test02.class });
		System.out.println("adding TestNG listener");
		testng.addListener(tla);
		System.out.println("running TestNG");
		testng.run();
	}

}