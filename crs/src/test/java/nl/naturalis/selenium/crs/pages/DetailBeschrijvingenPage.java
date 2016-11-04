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
		this.driver.switchTo().frame("ctl00_masterContent_iframe_1");
	}

	public String findSelectedFormulier() {
		Select dropDown = new Select(formulierenSelect);           
		String selected = dropDown.getFirstSelectedOption().getText();
		return selected;
	}
	
	public String getRegistrationNumber() {
		//this.switchToMasterContentFrame();
		WebElement registrationNumber = driver.findElement(By.id("registrationNumber"));
		return registrationNumber.getAttribute("value");
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
		thisIcon.setSrc(icon.getAttribute("src").trim());
		thisIcon.setAlt(icon.getAttribute("alt").trim());
		thisIcon.setTitle(icon.getAttribute("title").trim());

		return thisIcon;
	}

	public void clickFirstIdentificationEditIcon() {
		driver.switchTo().defaultContent();
		driver.switchTo().frame("ctl00_masterContent_iframe_1");
        List<WebElement> fieldsets = this.driver.findElements(By.cssSelector("fieldset"));
        for(WebElement fieldset : fieldsets) {
        	WebElement legend = fieldset.findElement(By.cssSelector("legend"));
        	if (legend.getText().trim().equals("Identifications")) {
        		List<WebElement> images = fieldset.findElements(By.cssSelector("input[type=image]"));
        		for(WebElement image : images) {
	        		if (image.getAttribute("src").contains("edit3.gif")) {
	        			image.click();
	        			break;
	        		}
        		}
        	}
        } 
	}
	
	public String getGenusInputValue() {
        List<WebElement> fieldsets = this.driver.findElements(By.cssSelector("fieldset"));
        for(WebElement fieldset : fieldsets) {
        	WebElement legend = fieldset.findElement(By.cssSelector("legend"));
        	if (legend.getText().trim().equals("Naming")) {
        		WebElement genusInput = fieldset.findElement(By.cssSelector("input[conceptfield=GENUS]"));
        		return genusInput.getAttribute("value");        		
        	}
        }
		return null;
	}
	
	public String getThesaurusLinkValue() {
        List<WebElement> fieldsets = this.driver.findElements(By.cssSelector("fieldset"));
        for(WebElement fieldset : fieldsets) {
        	WebElement legend = fieldset.findElement(By.cssSelector("legend"));
        	if (legend.getText().trim().equals("Naming")) {
        		WebElement thesaurusLink = fieldset.findElement(By.cssSelector("a[class=conceptLink]"));
        		return thesaurusLink.getText();        		
        	}
        }
		return null;
	}
	
	public WebElement getNextPageLink() {
		driver.switchTo().defaultContent();
        WebElement mainNav = this.driver.findElement(By.id("ctl00_masterContent_tbl_navigatie"));
    	List<WebElement> as = mainNav.findElements(By.cssSelector("a"));
    	for(WebElement a : as ) {
    		if (a.getText().trim().equals("›")) {
    			return a;
    		}
    	}
		return null;
	}

	public void clickCloseButton() {
		driver.switchTo().defaultContent();
		driver.switchTo().frame("ctl00_masterContent_iframe_1");
        WebElement closeButton = this.driver.findElement(By.cssSelector("input[title=Close]"));
        closeButton.click();
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
