package nl.naturalis.selenium.crs.configuration;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;

public class EventListener extends AbstractWebDriverEventListener {

	public void beforeNavigateTo(String url, WebDriver driver) {
		System.out.println("Before navigating to: '" + url + "'");
	}

	public void afterNavigateTo(String url, WebDriver driver) {
		System.out.println("Navigated to:'" + url + "'");
	}

	public void beforeClickOn(WebElement element, WebDriver driver) {
		System.out.println("Trying to click on: " + element.toString());
	}

	public void afterClickOn(WebElement element, WebDriver driver) {
		System.out.println("Clicked on: " + element.toString());
	}

	public void onException(Throwable error, WebDriver driver) {
		System.out.println("Error occurred" + error);
		String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		try {
			org.apache.commons.io.FileUtils.copyFile(scrFile, new File("test-output/screenshot-" + timeStamp + ".png"));
		} catch (Exception e) {
			System.out.println("Unable to Save");
		}
	}

}