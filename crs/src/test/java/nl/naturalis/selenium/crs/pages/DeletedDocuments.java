package nl.naturalis.selenium.crs.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import nl.naturalis.selenium.crs.configuration.Configuration;

public class DeletedDocuments extends AbstractPage {

	private WebDriver driver;

	private String PageName = "DeletedDocuments";
	private String PageTitle = "NCB  PL omgeving - Deleted documents";
	private String PageURL="/AtlantisWeb/pages/medewerker/ZoekenVerwijderdedocumenten.aspx";
	
//	private String PageUrlQueryString = "";
//	private String DataGroupTestThesaurusConcept;

	
	@FindBy(id = "ctl00_ctl00_masterContent_functionButtons_Beschrijvingssoorten")
	private WebElement selectListBeschrijvingsSoorten;
	
	@FindBy(id = "ctl00_ctl00_masterContent_functionButtons_btn_Search")
	private WebElement searchButton;
	
	@FindBy(tagName = "title")
	private WebElement pageTitle;
	
	@FindBy(id = "ctl00_ctl00_masterContent_functionButtons_btn_Restore_selected_documents")
	private WebElement restoreButton;

	public DeletedDocuments(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
		this.PageURL=Configuration.getDomain() + this.PageURL;
	}
	
	public void selectFormulierByName(String name) {
		WebDriverWait wait = new WebDriverWait(this.driver, 5);
		wait.until(ExpectedConditions.attributeContains(pageTitle, "innerHTML", this.PageTitle));		
		Select select = new Select(this.selectListBeschrijvingsSoorten);
		select.selectByVisibleText(name);
		searchButton.click();
	}

	public void switchToMasterContentFrame() {
		WebDriverWait wait = new WebDriverWait(this.driver, 5);
		wait.until(ExpectedConditions.visibilityOfElementLocated((By.id("ctl00_masterContent_iframe_1"))));		
		this.driver.switchTo().frame("ctl00_masterContent_iframe_1");
	}

	public void selectRecord(String registrationNumber) {
		WebDriverWait wait = new WebDriverWait(this.driver, 5);
		wait.until(ExpectedConditions.visibilityOfElementLocated((By.id("ctl00_ctl00_masterContent_LijstLink"))));
	
		WebElement checkBox = this.driver.findElement(By.xpath("//*[text()='" + registrationNumber + "']/parent::tr/td/input"));
		if (!checkBox.isSelected()) {
			checkBox.click();
		}
	}
	
	public void restoreRecord(String registrationNumber) {
		this.selectRecord(registrationNumber);
		this.restoreButton.click();
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
