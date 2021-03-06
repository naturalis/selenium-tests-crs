package nl.naturalis.selenium.crs.pages;

import nl.naturalis.selenium.crs.configuration.*;
import nl.naturalis.selenium.crs.pages.StartPage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * CRS HomePage page model 
 *
 * @author      Maarten Schermer <maarten.schermer@naturalis.nl>
 * @version     1.0
 * @since       1.0
 */
public class HomePage extends AbstractPage {

	private WebDriver driver;

	private String PageName="HomePage";
	private String PageTitle="NCB PL omgeving - Log in";
	private String PageURL="/AtlantisWeb/pages/publiek/Login.aspx";

	private String okAltText="Ok"; 
	
	@FindBy(id = "ctl00_masterContent_gebruikersnaam")
	private WebElement inputUsername;
	
	@FindBy(id = "ctl00_masterContent_wachtwoord")
	private WebElement inputPassword;
	
	@FindBy(id = "ctl00_masterContent_btnlogin")
	private WebElement buttonLogIn;

	@FindBy(css = "#javascriptLabel>img")
	private WebElement imgCheckJavascript;

	@FindBy(css = "#ajaxLabel>img")
	private WebElement imgCheckAjax;

	@FindBy(css = "#cookieLabel>img")
	private WebElement imgCheckCookie;
	
	@FindBy(id = "ctl00_masterContent_ingelogd_melding")
	private WebElement ingelogdMelding;

	@FindBy(id = "ctl00_LoginLink")
	private WebElement logOffMenu;


	public HomePage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
		this.PageURL=Configuration.getDomain() + this.PageURL;
	}

	public boolean hasUsernameInput() {
		return this.inputUsername.isDisplayed();
	}

	public boolean hasPasswordInput() {
		return this.inputPassword.isDisplayed();
	}
	
	public boolean hasLoginButton() {
		return this.buttonLogIn.isDisplayed();
	}

	public boolean hasJavascriptOkIcon() {
		return this.imgCheckJavascript.getAttribute("alt").equals(okAltText);
	}

	public boolean hasAjaxOkIcon() {
		return this.imgCheckAjax.getAttribute("alt").equals(okAltText);
	}

	public boolean hasCookieOkIcon() {
		return this.imgCheckCookie.getAttribute("alt").equals(okAltText);
	}

	public StartPage doLogin(String username, String password) {
		this.inputUsername.sendKeys(username);
		this.inputPassword.sendKeys(password);
		this.buttonLogIn.click();
		return new StartPage(this.driver);
	}
	
	public String getAuthorizationFailureMessage() {
		return ingelogdMelding.getText().toString().trim().replace("\r","");
	}

	public boolean mouseToLogOffLink() {
		Actions action = new Actions(this.driver);
		action.moveToElement(this.logOffMenu).perform();
		WebElement logOffLink = driver.findElement(By.id("ctl00_AfmeldenLink"));
		action.moveToElement(logOffLink).perform();
		return logOffLink.isDisplayed();
	}
	
	public void clickLogOffLink() {
		WebElement logOffLink = driver.findElement(By.id("ctl00_AfmeldenLink"));
		Actions action = new Actions(driver);
		action.moveToElement(logOffLink).perform();
		logOffLink.click();
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
