package nl.naturalis.selenium.crs.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.By.ByCssSelector;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import nl.naturalis.selenium.crs.configuration.Configuration;
import nl.naturalis.selenium.crs.fragments.EditIcon;

public class DetailBeschrijvingenPage extends AbstractPage {

	private WebDriver driver;

	private String PageName="DetailBeschrijvingenPage";
	private String PageTitle="NCB PL omgeving - ";
	private String PageURL="/AtlantisWeb/pages/medewerker/DetailBeschrijvingen.aspx";
	private String PageUrlQueryString="";

	@FindBy(id = "ctl00_masterContent_tbl_navigatie")
	private WebElement resultNumberTable;

	@FindBy(css = "tbody>tr>td>span>span.huidige")
	private WebElement numberSpan;

	@FindBy(id = "ctl00_masterContent_ddl_fomulieren")
	private WebElement formulierenSelect;
	
	@FindBy(id = "ctl00_masterContent_btn_formulieren")
	private WebElement buttonFormulierenSelect;
	
	@FindBy(id = "ctl00_QuickSearchTextBox")
	private WebElement QuickSearchTextBox;

	@FindBy(id = "ctl00_QuickSearchButton")
	private WebElement QuickSearchButton;
	
	@FindBy(id = "registrationNumber")
	private WebElement registrationNumber;
	
	// Iconen / buttons
	
	// #1 Add multimedia
	@FindBy(css = "span#ctl00_masterContent_UpdatePanel1 input")
	private WebElement iconAddMultimedia;

	// #2 Attach all multimedia ...
	@FindBy(id = "ctl00_masterContent_imgBtnMultimediaCopy")
	private WebElement iconAttachAllMultimediaFromGlobalSelection;

	// #3 Move all multimedia ...
	@FindBy(id = "ctl00_masterContent_imgBtnMultimediaCut")
	private WebElement iconMoveAllMultimediaFromGlobalSelection;

	// #4 New
	@FindBy(id = "ctl00_masterContent_btn_nieuw")
	private WebElement iconNewDocument;

	// #5 Save
	@FindBy(id = "btn_opslaan")
	private WebElement iconSaveDocument;

	// #6 Save and Add new
	@FindBy(id = "btn_opslaanAddNew")
	private WebElement iconSaveAddNewDocument;

	// #7 Save defaults
	@FindBy(className = "saveDefaults")
	private WebElement iconSaveDefaults;

	// #8 Load defaults
	@FindBy(css = "div#ctl00_masterContent_UpdatePanel4 div.tabcontent table.maxwidth tbody tr td img:last-of-type")
	private WebElement iconLoadDefaults;

	@FindBy(id = "scrolldiv")
	private WebElement contextDisplay;

	@FindBy(css = "span.ui-icon.ui-icon-triangle-1-e")
	private WebElement contextDisplayMoreButton;
	
	public DetailBeschrijvingenPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(this.driver, this);
		this.PageURL=Configuration.getDomain() + this.PageURL;
	}
	
	public void setPageUrlQueryString(String queryString) {
		this.PageUrlQueryString=queryString;
	}

	public Integer getNumberOfResults() {
		return Integer.parseInt(numberSpan.getText().trim());	
	}
	
	public List<WebElement> clickFormulierenSelect() {
		Actions action = new Actions(driver);
		action.moveToElement(this.formulierenSelect).perform();
		this.formulierenSelect.click();
		Select select = new Select(this.formulierenSelect);
		return select.getOptions();
	}

	public void clickFormulierenSelectOption(String optionLabel) {
		Select select = new Select(this.formulierenSelect);
		select.selectByVisibleText(optionLabel);
		select.getFirstSelectedOption().click();
		buttonFormulierenSelect.click();
	}

	public void switchToMasterContentFrame() {
		WebDriverWait wait = new WebDriverWait(this.driver, 5);
		wait.until(ExpectedConditions.visibilityOfElementLocated((By.id("ctl00_masterContent_iframe_1"))));		
		this.driver.switchTo().frame( "ctl00_masterContent_iframe_1" );
	}

	public String findSelectedFormulier() {
		Select dropDown = new Select(formulierenSelect);           
		String selected = dropDown.getFirstSelectedOption().getText();
		return selected;
	}
	
	public String getRegistrationNumber() {
		return this.registrationNumber.getAttribute("value");
	}

	public void doDetailSearch(String searchterm) {
		this.QuickSearchTextBox.sendKeys(searchterm);
		this.QuickSearchButton.click();
	}
	

	public String getCompletePageURL() {	
		if (this.PageUrlQueryString=="") {
			return this.PageURL; 
		}
		else {
			return this.PageURL+'?'+this.PageUrlQueryString;
		}
	}
	
	public String[] getIconAddMultimedia() {
		String[] iconValues = new String[2];
		iconValues[0] = iconAddMultimedia.getAttribute("src").trim();
		iconValues[1] = iconAddMultimedia.getAttribute("title").trim();
		return iconValues;
	}
	
	public String[] getIconAttachAllMultimediaFromGlobalSelection() {
		String[] iconValues = new String[2];
		iconValues[0] = iconAttachAllMultimediaFromGlobalSelection.getAttribute("src").trim();
		iconValues[1] = iconAttachAllMultimediaFromGlobalSelection.getAttribute("title").trim();
		return iconValues;
	}

	public EditIcon getIconInfo(String choice) {
		WebElement icon = null;

		if (choice == "icon1") {
			icon = iconAddMultimedia;
		} else if (choice == "icon2") {
			icon = iconAttachAllMultimediaFromGlobalSelection;
		} else if (choice == "icon3") {
			icon = iconMoveAllMultimediaFromGlobalSelection;
		} else if (choice == "icon4") {
			icon = iconNewDocument;
		} else if (choice == "icon5") {
			icon = iconSaveDocument;
		} else if (choice == "icon6") {
			icon = iconSaveAddNewDocument;
		} else if (choice == "icon7") {
			icon = iconSaveDefaults;
		} else if (choice == "icon8") {
			icon = iconLoadDefaults;
		}
		
		EditIcon thisIcon = new EditIcon();
		thisIcon.getSrc(icon.getAttribute("src").trim());
		thisIcon.getAlt(icon.getAttribute("alt").trim());
		thisIcon.getTitle(icon.getAttribute("title").trim());
		return thisIcon;
	}

	public Boolean isContextDisplayAvailable() {
		driver.switchTo().defaultContent();
		driver.switchTo().frame("ctl00_masterContent_iframe_1");
		// contextDisplayMoreButton.click();
		return contextDisplayMoreButton.isEnabled();
	}
	
	public String getContextDisplayObjectType() {
		driver.switchTo().defaultContent();
		driver.switchTo().frame("ctl00_masterContent_iframe_1");
		return contextDisplay.getText().trim();
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
