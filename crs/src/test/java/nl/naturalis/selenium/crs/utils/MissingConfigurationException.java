package nl.naturalis.selenium.crs.utils;

/*
 * example usage:
 * throw new MissingConfigurationException("missing browser type");
 */

public class MissingConfigurationException extends Exception {

	public MissingConfigurationException(String message){
		super(message);
	}

}

