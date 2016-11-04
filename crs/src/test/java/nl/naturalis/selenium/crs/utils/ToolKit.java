package nl.naturalis.selenium.crs.utils;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ToolKit {

	public void ListIFrames(WebDriver driver) {
		List<WebElement> iframes = driver.findElements(By.cssSelector("iframe"));
	    for(WebElement iframe : iframes) {
	    	System.out.println(iframe.getAttribute("id"));
	    }
	}
	
	public void removeDomElement(WebDriver driver, String elementName) {
		JavascriptExecutor js = null;
		if (driver instanceof JavascriptExecutor) {
		    js = (JavascriptExecutor) driver;
		}
		js.executeScript("return document.getElementById('"+elementName+"').remove();");
	
	}


	


}
