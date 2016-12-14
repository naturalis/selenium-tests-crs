package nl.naturalis.selenium.crs.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
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
import nl.naturalis.selenium.crs.fragments.Link;

public class DetailBeschrijvingenPage extends AbstractPage {

/*
 * prefix
 * 	input conceptfield="PREFIX"
 * 
 * storage
 * 	name=
 * 		CURRENTCOLLECTIONUNIT
 * 		CURRENTSTORAGELOCATION
 * 		USUALCOLLECTIONUNIT
 * 		USUALSTORAGELOCATION
 * 			VerifyNotEditable 
 * 
 * conceptfield="PRESERVEDPART"
 * 
 *  
 * iconSaveDocument
 * 
 * CONTEXT
 * ctl00_masterContent_iframe_1
 *   div#scrolldiv
 *		 p#scrollcontextinhoud
 */

	@FindBy(css ="input[conceptfield=PREFIX]")
	private WebElement prefixInput;

	@FindBy(css ="input[name=CURRENTCOLLECTIONUNIT]")
	private WebElement currentCollectionUnitInput;

	@FindBy(css ="input[name=CURRENTSTORAGELOCATION]")
	private WebElement currentStorageLocationInput;

	@FindBy(css ="input[name=USUALCOLLECTIONUNIT]")
	private WebElement usualCollectionUnitInput;

	@FindBy(css ="input[name=USUALSTORAGELOCATION]")
	private WebElement usualStorageLocationInput;



	
	
	private WebDriver driver;

	private String PageName = "DetailBeschrijvingenPage";
	private String PageTitle = "NCB PL omgeving - ";
	private String PageURL = "/AtlantisWeb/pages/medewerker/DetailBeschrijvingen.aspx";
	private String PageUrlQueryString = "";
	
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

	@FindBy(id = "uniqueID321918013")
	private WebElement SourceInstitute;
	
	@FindBy(id = "uniqueID0219179827")//uniqueID021917980
	private WebElement currentCollectionName;
	
	@FindBy(xpath = "//input[@id='uniqueID0219179827']/parent::*/span[@class='CSContainer']/input[2]")
	private WebElement clearCurrentCollectionName;

	@FindBy(xpath = "//input[@id='uniqueID0219179827']/parent::*/span[@class='CSContainer']/a[@class='conceptLink']")
	private WebElement tlinkCurrentCollectionName;

	@FindBy(xpath = "//input[@id='uniqueID1219179928']/parent::*/span[@class='CSContainer']/input[2]")
	private WebElement clearPrefix;
	
	@FindBy(id = "uniqueID3219180130")
	private WebElement basisOfRecord;
	
	@FindBy(xpath = "//input[@id='uniqueID3219180130']/parent::*/span[@class='CSContainer']/input[2]")
	private WebElement clearBasisOfRecord;

	@FindBy(xpath = "//input[@id='uniqueID3219180130']/parent::*/span[@class='CSContainer']/a[@class='conceptLink']")
	private WebElement tlinkBasisOfRecord;

	@FindBy(css = "input#uniqueID4219180231")
	private WebElement mount;

	@FindBy(xpath = "//input[@id='uniqueID4219180231']/parent::*/span[@class='CSContainer']/input[2]")
	private WebElement clearMount;

	@FindBy(xpath = "//input[@id='uniqueID4219180231']/parent::*/span[@class='CSContainer']/a[@class='conceptLink']")
	private WebElement tlinkMount;

	@FindBy(css ="input#uniqueID6219180333")
	private WebElement preservedPart;

	@FindBy(xpath = "//input[@id='uniqueID6219180333']/parent::*/span[@class='CSContainer']/input[2]")
	private WebElement clearPreservedPart;

	@FindBy(xpath = "//input[@id='uniqueID6219180333']/parent::*/span[@class='CSContainer']/a[@class='conceptLink']")
	private WebElement tlinkPreservedPart;
	
	@FindBy(xpath = "//input[@name='ko_unique_3']")
	private WebElement number;	
	
	@FindBy(xpath = "//input[@name='ko_unique_4']")
	private WebElement suffix;	
	
	@FindBy(id = "ko_unique_2")
	private WebElement suffixErrorIcon;	

	
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

	// #9 Thesaurus icon Basis of record
	@FindBy(css = "input#select3")
	private WebElement iconThesaurusBasisOfRecord;

	// #10 Bin icon Basis of record
	@FindBy(xpath = ".//*[@id='select3']/../input[2]")
	private WebElement iconBinBasisOfRecord;

	// #11 Thesaurus icon Data Group Type
	@FindBy(css = "input#select9")
	private WebElement iconThesaurusDataGroupType;

	// #12 Bin icon Data Group Type
	@FindBy(xpath = ".//*[@id='select9']/../input[2]")
	private WebElement iconBinDataGroupType;

	// #13 Thesaurus icon Gathering Site Country
	@FindBy(css = "input#select21")
	private WebElement iconThesaurusGatheringSiteCountry;

	// #14 Bin icon Gathering Site Country
	@FindBy(xpath = ".//*[@id='select21']/../input[2]")
	private WebElement iconBinGatheringSiteCountry;

	// #15 Warning
	@FindBy(css = "img#ko_unique_58")
	private WebElement invalidCollectionName;

	// #16 Warning Basis of Record
	@FindBy(css = "img#ko_unique_64")
	private WebElement invalidBasisOfRecord;
	
	// #17 Warning Mount
	@FindBy(css = "img#SPECIMENMOUNT")
	private WebElement invalidMount;

	// #18 Warning Preserved Part
	@FindBy(css = "img#PRESERVEDPART")
	private WebElement invalidPreservedPart;
	
	
	@FindBy(id = "scrolldiv")
	private WebElement contextDisplay;

	@FindBy(css = "span.ui-icon.ui-icon-triangle-1-e")
	private WebElement contextDisplayMoreButton;

	@FindBy(css = "input[atfid=\"add_ncrs_gatheringsites_dialog?dialog\"]")
	private WebElement buttonAddGatheringSite;

	@FindBy(css = "*[title=\"Close\"")
	private WebElement closeButton;
	
	public DetailBeschrijvingenPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(this.driver, this);
		this.PageURL=Configuration.getDomain() + this.PageURL;
	}	
	
	public void setPageUrlQueryString(String queryString) {
		this.PageUrlQueryString = queryString;
	}

	public void setCurrentCollectionName(String enterText, String selectText) {
		this.switchToFrame_1();
		this.currentCollectionName.click();
		this.currentCollectionName.sendKeys(enterText);
		this.currentCollectionName.sendKeys(Keys.DOWN);
		driver.switchTo().defaultContent();
		driver.switchTo().frame("ctl00_masterContent_iframe_1");
		driver.findElement(By.partialLinkText(selectText)).click();
	}

	public String getCurrentCollectionNameTlink() {
		this.switchToFrame_1();
		return this.tlinkCurrentCollectionName.getText();
	}

	public void deleteCurrentCollectionName() {
		this.switchToFrame_1();
		this.currentCollectionName.click();
		this.clearCurrentCollectionName.click();
	}
	
	public void deletePrefix() {
		this.clearPrefix.click();
	}
	
	public String getNumber() {
		return number.getText();
	}

	public void setNumber(String text) {
		this.switchToFrame_1();
		this.number.sendKeys(text);
	}

	public String getPrefix() {
		return number.getText();
	}

	public void setPrefix(String text) {
		this.switchToFrame_1();
		this.number.sendKeys(text);
	}
	
	
	public void setBasisOfRecord(String enterText, String selectText) {
		this.switchToFrame_1();
		this.basisOfRecord.click();
		this.basisOfRecord.sendKeys(enterText);
		this.basisOfRecord.sendKeys(Keys.DOWN);
		driver.switchTo().defaultContent();
		driver.switchTo().frame("ctl00_masterContent_iframe_1");
		driver.findElement(By.partialLinkText(selectText)).click();
	}

	public String getBasisOfRecordTlink() {
		this.switchToFrame_1();
		return this.tlinkBasisOfRecord.getText();
	}

	public void deleteBasisOfRecord() {
		this.switchToFrame_1();
		this.basisOfRecord.click();
		this.clearBasisOfRecord.click();
	}
	
	public void setMount(String enterText, String selectText) {
		this.switchToFrame_1();
		this.mount.click();
		this.mount.sendKeys(enterText);
		this.mount.sendKeys(Keys.DOWN);
		driver.switchTo().defaultContent();
		driver.switchTo().frame("ctl00_masterContent_iframe_1");
		driver.findElement(By.partialLinkText(selectText)).click();
	}

	public String getMountTlink() {
		this.switchToFrame_1();
		return this.tlinkMount.getText();
	}

	public void deleteMount() {
		this.switchToFrame_1();
		this.mount.click();
		this.clearMount.click();
	}
	
	public void setPreservedPart(String enterText, String selectText) {
		this.switchToFrame_1();
		this.preservedPart.click();
		this.preservedPart.sendKeys(enterText);
		this.preservedPart.sendKeys(Keys.DOWN);
		driver.switchTo().defaultContent();
		driver.switchTo().frame("ctl00_masterContent_iframe_1");
		driver.findElement(By.partialLinkText(selectText)).click();
	}

	public String getPreservedPartTlink() {
		this.switchToFrame_1();
		return this.tlinkPreservedPart.getText();
	}
	
	public void deletePreservedPart() {
		this.switchToFrame_1();
		this.preservedPart.click();
		this.preservedPart.click();
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

	public void enterValueToField(String fieldname, String value) {
		this.switchToMainFrame();
		this.driver.switchTo().frame("ctl00_masterContent_iframe_1");
		WebElement field = null;

		switch (fieldname) {         
        case "currentcollectionname":
			field = currentCollectionName;
			break;
        case "basisofrecord":
        	field = basisOfRecord;
        	break;
        case "mount":
        	field = mount;
        	break;
        case "preservedpart":
        	field = preservedPart;
        	break;
        default:
        	field = null;
        }

		// Position the cursor in the input field ...
		Actions builder = new Actions(driver);
		builder.moveToElement(field).build().perform();
		if (value == "TAB") {
			// TAB to the next field
			field.sendKeys(Keys.TAB);
		} else if (value == "CLEAR") {
			// Enter the value
			field.clear();
		} else {
			// Enter the value
			field.sendKeys(value);
		}
	}
	
	public String readValueFromField(String fieldname) {
		WebElement field = null;
		switch (fieldname) {         
        case "basisofrecord":
        	field = basisOfRecord;
        	break;
        case "currentcollectionname":
			field = currentCollectionName;
			break;
        case "preservedpart":
			field = preservedPart;
			break;
        case "mount":
        	field = mount;
        	break;
        default:
        	field = null;
        }
		return field.getAttribute("value");
		
	}
	
	public void switchToMainFrame() {
		driver.switchTo().defaultContent();
	}

	public void switchToMasterContentFrame() {
		WebDriverWait wait = new WebDriverWait(this.driver, 5);
		wait.until(ExpectedConditions.visibilityOfElementLocated((By.id("ctl00_masterContent_iframe_1"))));
		this.driver.switchTo().frame("ctl00_masterContent_iframe_1");
	}

	public void switchToFrame_1() {
		driver.switchTo().defaultContent();
		this.driver.switchTo().frame("iframe_1");
	}

	
	/**
	 * 
	 */
	public void switchToGatheringSitesFrame() {
		buttonAddGatheringSite.click();
		driver.switchTo().defaultContent();

		int size = driver.findElements(By.tagName("iframe")).size();
		for (int n = 0; n < size; n++) {
			driver.switchTo().frame(n);
			List<WebElement> wantedElements = driver.findElements(By.id("select21"));
			if (wantedElements.size() > 0) {
				return;
			}
			driver.switchTo().defaultContent();
		}
	}

	/**
	 * This method switches between all available iframes until it finds one
	 * that contains the specified elementID.
	 */
	public void switchToFrameContainingElementID(String elementID) {
		driver.switchTo().defaultContent();
		int size = driver.findElements(By.tagName("iframe")).size();
		for (int n = 0; n < size; n++) {
			driver.switchTo().frame(n);
			List<WebElement> wantedElements = driver.findElements(By.id("elementID"));
			if (wantedElements.size() > 0) {
				return;
			}
			driver.switchTo().defaultContent();
		}
	}

	public String findSelectedFormulier() {
		driver.switchTo().defaultContent();
		Select dropDown = new Select(formulierenSelect);
		String selected = dropDown.getFirstSelectedOption().getText();
		return selected;
	}

	public String getRegistrationNumber() {
		WebElement registrationNumber = driver.findElement(By.id("registrationNumber"));
		return registrationNumber.getAttribute("value");
	}

	public void doDetailSearch(String searchterm) {
		this.QuickSearchTextBox.sendKeys(searchterm);
		this.QuickSearchButton.click();
	}

	public String getCompletePageURL() {
		if (this.PageUrlQueryString == "") {
			return this.PageURL;
		} else {
			return this.PageURL + '?' + this.PageUrlQueryString;
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
		switch (choice) {
		case "icon1":
			icon = iconAddMultimedia;
			break;
		case "icon2":
			icon = iconAttachAllMultimediaFromGlobalSelection;
			break;
		case "icon3":
			icon = iconMoveAllMultimediaFromGlobalSelection;
			break;
		case "icon4":
			icon = iconNewDocument;
			break;
		case "icon5":
			icon = iconSaveDocument;
			break;
		case "icon6":
			icon = iconSaveAddNewDocument;
			break;
		case "icon7":
			icon = iconSaveDefaults;
			break;
		case "icon8":
			icon = iconLoadDefaults;
			break;
		case "icon9":
			icon = iconThesaurusBasisOfRecord;
			break;
		case "icon10":
			icon = iconBinBasisOfRecord;
			break;
		case "icon11":
			icon = iconThesaurusDataGroupType;
			break;
		case "icon12":
			icon = iconBinDataGroupType;
			break;
		case "icon13":
			icon = iconThesaurusGatheringSiteCountry;
			break;
		case "icon14":
			icon = iconBinGatheringSiteCountry;
			break;
		case "icon15":
			icon = invalidCollectionName;
			break;
		case "icon16":
			icon = invalidBasisOfRecord;
			break;
		case "icon17":
			icon = invalidMount;
			break;
		case "icon18":
			icon = invalidPreservedPart;
			break;
		default:
			icon = null;
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

	public Boolean getContextDisplayIsDisplayed() {
		driver.switchTo().defaultContent();
		driver.switchTo().frame("ctl00_masterContent_iframe_1");
		try {
			return contextDisplay.isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}
	
	/*
	public List<Link> getContextDisplayLinks() {
		
		driver.switchTo().defaultContent();
		driver.switchTo().frame("ctl00_masterContent_iframe_1");
		
	}
	*/

	public String getContextDisplayObjectType() {
		driver.switchTo().defaultContent();
		driver.switchTo().frame("ctl00_masterContent_iframe_1");
		return contextDisplay.getText().trim();
	}

	public void clickFirstIdentificationEditIcon() {
		driver.switchTo().defaultContent();
		driver.switchTo().frame("ctl00_masterContent_iframe_1");
		List<WebElement> fieldsets = this.driver.findElements(By.cssSelector("fieldset"));
		for (WebElement fieldset : fieldsets) {
			WebElement legend = fieldset.findElement(By.cssSelector("legend"));
			if (legend.getText().trim().equals("Identifications")) {
				List<WebElement> images = fieldset.findElements(By.cssSelector("input[type=image]"));
				for (WebElement image : images) {
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
		for (WebElement fieldset : fieldsets) {
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
		for (WebElement fieldset : fieldsets) {
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
		for (WebElement a : as) {
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
