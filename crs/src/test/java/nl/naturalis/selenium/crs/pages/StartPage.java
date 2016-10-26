package nl.naturalis.selenium.crs.pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import nl.naturalis.selenium.crs.configuration.Configuration;

public class StartPage extends AbstractPage {

	private WebDriver driver;

	private String PageName="StartPage";
	private String PageTitle="NCB PL omgeving - Start";
	private String PageURL="/AtlantisWeb/default.aspx";

	@FindBy(id = "ctl00_UppMenu")
	private WebElement UppMenu;

	@FindBy(id = "ctl00_QuickSearchTextBox")
	private WebElement QuickSearchTextBox;

	@FindBy(id = "ctl00_QuickSearchButton")
	private WebElement QuickSearchButton;

	@FindBy(id = "ctl00_QuickSearchPopUpPanel")
	private WebElement QuickSearchPopUpPanel;

	@FindBy(id = "ctl00_QuickSearchErrorPopupButton")
	private WebElement QuickSearchErrorPopupButton;

	
	
	public StartPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
		this.PageURL=Configuration.getDomain() + this.PageURL;
	}
	
	public WebElement getUppMenu() { 
		return UppMenu;
	}

	public void doDetailSearch(String searchterm) {
		this.QuickSearchTextBox.sendKeys(searchterm);
		this.QuickSearchButton.click();
	}

	public void switchToMasterContentFrame() {
		WebDriverWait wait = new WebDriverWait(this.driver, 5);
		wait.until(ExpectedConditions.visibilityOfElementLocated((By.id("ctl00_masterContent_iframe_1"))));		
		this.driver.switchTo().frame("ctl00_masterContent_iframe_1");
	}


	// not working!
	public void quickSearchErrorPopupButtonClick() {
		QuickSearchErrorPopupButton.click();
	}
	
	public String getSearchFailureMessage() {
		return QuickSearchPopUpPanel.getAttribute("textContent").trim();
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
