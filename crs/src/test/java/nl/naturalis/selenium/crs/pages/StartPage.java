package nl.naturalis.selenium.crs.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import nl.naturalis.selenium.crs.configuration.Configuration;

public class StartPage extends AbstractPage {

	private WebDriver driver;

	private String PageName="StartPage";
	private String PageTitle="NCB PL omgeving - Start";
	private String PageURL="/AtlantisWeb/default.aspx";

	@FindBy(id = "ctl00_UppMenu")
	private WebElement UppMenu;

	public StartPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
		this.PageURL=Configuration.getDomain() + this.PageURL;
	}
	
	public WebElement getUppMenu() { 
		return UppMenu;
	}
	
	@Override
	public String getPageName() {
		return this.PageName;
	}

	@Override
	public String getPageTitle() {
		return this.PageTitle;
	}

	@Override
	public String getPageURL() {
		return this.PageURL;
	}

}
