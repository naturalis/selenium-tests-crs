package nl.naturalis.selenium.crs.fragments;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class Thesaurus {

	private WebDriver driver;

	public Thesaurus(WebDriver driver) {
		this.driver = driver;
	}
	
	public void selectConcept(String concept) {
		this.driver.switchTo().defaultContent();
		this.driver.switchTo().frame("ctl00_masterContent_iframe_1");
		this.driver.findElement(By.partialLinkText(concept)).click();
	}	
}