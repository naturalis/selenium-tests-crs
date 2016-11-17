package nl.naturalis.selenium.crs.pages;

import nl.naturalis.selenium.crs.configuration.*;
import nl.naturalis.selenium.crs.pages.StartPage;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * CRS HomePage page model 
 *
 * @author      Maarten Schermer <maarten.schermer@naturalis.nl>
 * @version     1.0
 * @since       1.0
 */
public class SimpleSearchPage extends AbstractPage {

	private WebDriver driver;

	private String PageName="SimpleSearchPage";
	private String PageTitle="NCB PL omgeving - Search on";
	private String PageURL="/AtlantisWeb/pages/medewerker/Zoeken.aspx";
	private String PageUrlQueryString="";

	@FindBy(id = "ctl00_masterContent_eenvoudigfiltertext")
	private WebElement eenvoudigFilterText;
	
	@FindBy(id = "ctl00_masterContent_eenvoudigfilterconceptclearConceptSelector")
	private WebElement eenvoudigFilterClear;	

	@FindBy(id = "ctl00_masterContent_BTN_eenvoudigzoeken")
	private WebElement eenvoudigzoekenButton;	
	
	@FindBy(id = "ctl00_masterContent_EenvoudigSpellingsVariant")
	private WebElement eenvoudigSpellingsVariantCheckbox;	

	@FindBy(id = "ctl00_masterContent_TB_eenvoudigzoeken")
	private WebElement eenvoudigzoekenInput;	

	@FindBy(id = "ctl00_masterContent_RD_eenvoudigBevatMultimedia_0")
	private WebElement multimediaNoRadioButton;	
		
	@FindBy(id = "ctl00_masterContent_RD_eenvoudigBevatMultimedia_1")
	private WebElement multimediaYesRadioButton;	

	@FindBy(id = "ctl00_masterContent_RD_eenvoudigBevatMultimedia_2")
	private WebElement multimediaMaybeRadioButton;	

	@FindBy(id = "ctl00_masterContent_RD_eenvoudigModeratieStatus_0")
	private WebElement moderatedNoRadioButton;	
		
	@FindBy(id = "ctl00_masterContent_RD_eenvoudigModeratieStatus_1")
	private WebElement moderatedYesRadioButton;	

	@FindBy(id = "ctl00_masterContent_RD_eenvoudigModeratieStatus_2")
	private WebElement moderatedMaybeRadioButton;	

	@FindBy(id = "ctl00_masterContent_EenvoudigSearchEnvironment_0")
	private WebElement environmentVerbatim;	

	@FindBy(id = "ctl00_masterContent_EenvoudigSearchEnvironment_1")
	private WebElement environmentThesaurus;	

	@FindBy(id = "ctl00_masterContent_EenvoudigSearchEnvironment_2")
	private WebElement environmentBoth;	

	@FindBy(id = "ctl00_masterContent_ddlTransformatiesEenvoudig")
	private WebElement transformationSelect;	

	

	public SimpleSearchPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
		this.PageURL=Configuration.getDomain() + this.PageURL;
	}

	public void setPageUrlQueryString(String queryString) {
		this.PageUrlQueryString=queryString;
	}

	public void clearEenvoudigFilterText() {
		eenvoudigFilterClear.click();
	}

	public void setEenvoudigFilterText(String text) {
		eenvoudigFilterText.sendKeys(text);
	}

	public void setEenvoudigFilterText(Keys key) {
		//Keys.RETURN
		eenvoudigFilterText.sendKeys(key);
	}

	public void setEenvoudigInputText(String text) {
		eenvoudigzoekenInput.sendKeys(text);
	}

	public void clearEenvoudigInputText() {
		eenvoudigzoekenInput.clear();
	}

	public String getEenvoudigInputText() {
		return eenvoudigzoekenInput.getAttribute("value");
	}

	public SimpleSearchResultsPage clickEenvoudigZoeken() {
		eenvoudigzoekenButton.click();
		return new SimpleSearchResultsPage(this.driver);
	}
	
	public void toggleSpellingVariants() {
		eenvoudigSpellingsVariantCheckbox.click();
	}

	public void toggleSpellingVariants(Boolean state) {
		if (!state.equals(this.getSpellingVariantsSelected())) {
			eenvoudigSpellingsVariantCheckbox.click();
		}
	}

	public boolean getSpellingVariantsSelected() {
		return eenvoudigSpellingsVariantCheckbox.isSelected();
	}

	public String getSelectedTransformationName() {
		Select select = new Select(transformationSelect);
		WebElement option = select.getFirstSelectedOption();
		return option.getText().trim();
	}

	public void selectTransformationByName(String name) {
		Select select = new Select(transformationSelect);
		select.selectByVisibleText(name);
	}
	
	public void setMultimedia(Integer state) {
		if (state.equals(0)) {
			multimediaNoRadioButton.click();
		} else
		if (state.equals(1)) {
			multimediaYesRadioButton.click();
		} else {
			multimediaMaybeRadioButton.click();
		}
	}
	
	public void setModeration(Integer state) {
		if (state.equals(0)) {
			moderatedNoRadioButton.click();
		} else
		if (state.equals(1)) {
			moderatedYesRadioButton.click();
		} else {
			moderatedMaybeRadioButton.click();
		}
	}

	public void setEnvironment(String state) {
		if (state.equals("Verbatim")) {
			environmentVerbatim.click();
		} else
		if (state.equals("Thesaurus")) {
			environmentThesaurus.click();
		} else {
			environmentBoth.click();
		}
	}
	
	public String getCompletePageURL() {	
		if (this.PageUrlQueryString=="") {
			return this.PageURL; 
		} else {
			return this.PageURL+'?'+this.PageUrlQueryString;
		}
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
