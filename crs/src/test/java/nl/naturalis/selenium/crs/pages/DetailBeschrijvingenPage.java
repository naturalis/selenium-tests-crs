package nl.naturalis.selenium.crs.pages;

import java.util.List;
import java.util.Set;

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

public class DetailBeschrijvingenPage extends AbstractPage {

	private WebDriver driver;

	private String PageName = "DetailBeschrijvingenPage";
	private String PageTitle = "NCB PL omgeving - ";
	private String PageURL = "/AtlantisWeb/pages/medewerker/DetailBeschrijvingen.aspx";
	private String PageUrlQueryString = "";

	@FindBy(id = "ctl00_masterContent_tbl_navigatie")
	private WebElement resultNumberTable;

	@FindBy(xpath = "//input[@atfid='add_ncrs_specimen_prefix?selectfield']")
	private WebElement prefix;

	@FindBy(xpath = "//input[@atfid='add_ncrs_specimen_prefix?selectfield']/parent::*/span[@class='CSContainer']/input[@title='Select']")
	private WebElement prefixThesaurusIcon;

	@FindBy(xpath = "//input[@atfid='add_ncrs_specimen_prefix?selectfield']/parent::*//span/input[1]")
	private WebElement prefixThesaurusList;

	@FindBy(xpath = "//input[@atfid='add_ncrs_specimen_prefix?selectfield']/parent::*/span[@class='CSContainer']/input[@title='Clear']")
	private WebElement prefixDeleteIcon;

	@FindBy(xpath = "//input[@atfid='add_ncrs_specimen_prefix?selectfield']/parent::*/span[@class='CSContainer']/a[@class='conceptLink']")
	private WebElement prefixConceptLink;

	@FindBy(css = "tbody>tr>td>span>span.huidige")
	private WebElement numberSpan;

	@FindBy(xpath = "//div[@id='ctl00_masterContent_txt_FormulierenUpdatePanel']/select")
	private WebElement formulierenSelect;

	@FindBy(name = "ctl00$masterContent$btn_formulieren")
	private WebElement buttonFormulierenSelect;

	@FindBy(name = "ctl00_QuickSearchTextBox")
	private WebElement QuickSearchTextBox;

	@FindBy(id = "ctl00_QuickSearchButton")
	private WebElement QuickSearchButton;

	@FindBy(xpath = "//input[@atfid='add_ncrs_specimen_catalognumber?numberfield']")
	private WebElement number;

	@FindBy(xpath = "//input[@atfid='add_ncrs_specimen_suffix?suffixfield']")
	private WebElement suffix;

	@FindBy(xpath = "//input[@atfid='add_ncrs_specimen_suffix?suffixfield']/parent::td/img[@class='errorImage']")
	private WebElement suffixErrorIcon;

	@FindBy(id = "registrationNumber")
	private WebElement registrationNumber;

	@FindBy(xpath = "//input[@id='regNrCheck']/parent::td/input")
	private WebElement registrationNumberErrorMessage;

	@FindBy(xpath = "//input[@id='regNrCheck']/parent::td/img")
	private WebElement registrationNumberErrorIcon;

	@FindBy(id = "//input[@conceptfield='SOURCEINSTITUTE']")
	private WebElement sourceInstitute;

	@FindBy(xpath = "//input[@atfid='add_ncrs_specimen_inst_coll_subcoll?selectfield']")
	private WebElement currentCollectionName;

	@FindBy(xpath = "//input[@atfid='add_ncrs_specimen_inst_coll_subcoll?selectfield']/parent::*/span[@class='CSContainer']/input[2]")
	private WebElement clearCurrentCollectionName;

	@FindBy(xpath = "//input[@atfid='add_ncrs_specimen_inst_coll_subcoll?selectfield']/parent::*/span[@class='CSContainer']/a[@class='conceptLink']")
	private WebElement tlinkCurrentCollectionName;

	@FindBy(name = "ko_unique_64")
	private WebElement basisOfRecord;

	@FindBy(xpath = "//input[@name='ko_unique_64']/parent::*/span[@class='CSContainer']/input[2]")
	private WebElement clearBasisOfRecord;

	@FindBy(xpath = "//input[@name='ko_unique_64']/parent::*/span[@class='CSContainer']/a[@class='conceptLink']")
	private WebElement tlinkBasisOfRecord;

	@FindBy(name = "SPECIMENMOUNT")
	private WebElement mount;

	@FindBy(xpath = "//input[@name='SPECIMENMOUNT']/parent::*/span[@class='CSContainer']/input[2]")
	private WebElement clearMount;

	@FindBy(xpath = "//input[@name='SPECIMENMOUNT']/parent::*/span[@class='CSContainer']/a[@class='conceptLink']")
	private WebElement tlinkMount;

	@FindBy(name = "PRESERVEDPART")
	private WebElement preservedPart;

	@FindBy(xpath = "//input[@name='PRESERVEDPART']/parent::*/span[@class='CSContainer']/input[2]")
	private WebElement clearPreservedPart;

	@FindBy(xpath = "//input[@name='PRESERVEDPART']/parent::*/span[@class='CSContainer']/a[@class='conceptLink']")
	private WebElement tlinkPreservedPart;

	@FindBy(xpath = "//textarea[@atfid='add_ncrs_specimen_remarks?defaultfield']")
	private WebElement remarks;

	@FindBy(name = "CURRENTCOLLECTIONUNIT")
	private WebElement standardStorageUnit;

	@FindBy(xpath = "//input[@name='CURRENTCOLLECTIONUNIT']/parent::td/input[@class='errorImage']")
	private WebElement standardStorageUnitErrorIcon;

	@FindBy(name = "USUALCOLLECTIONUNIT")
	private WebElement temporaryStorageUnit;

	@FindBy(xpath = "//input[@name='USUALCOLLECTIONUNIT']/parent::td/input[@type='image']")
	private WebElement temporaryStorageUnitErrorIcon;

	@FindBy(name = "CURRENTSTORAGELOCATION")
	private WebElement standardStorageLocation;

	@FindBy(xpath = "//input[@name='CURRENTSTORAGELOCATION']/parent::td/input[@title='Clear']")
	private WebElement standardStorageLocationBinIcon;

	@FindBy(xpath = "//input[@name='CURRENTSTORAGELOCATION']/parent::td/input[@title='Select']")
	private WebElement standardStorageLocationSelectIcon;

	@FindBy(xpath = "//input[@name='CURRENTSTORAGELOCATION']/parent::td/input[@class='errorImage']")
	private WebElement standardStorageLocationErrorIcon;

	@FindBy(name = "USUALSTORAGELOCATION")
	private WebElement temporaryStorageLocation;

	@FindBy(xpath = "//input[@name='USUALSTORAGELOCATION']/parent::td/input[@title='Clear']")
	private WebElement temporaryStorageLocationBinIcon;

	@FindBy(xpath = "//input[@name='USUALSTORAGELOCATION']/parent::td/input[@title='Select']")
	private WebElement temporaryStorageLocationSelectIcon;

	@FindBy(xpath = "//input[@name='USUALSTORAGELOCATION']/parent::td/input[@class='errorImage']")
	private WebElement temporaryStorageLocationErrorIcon;
	
	@FindBy(xpath = "*//legend[text()='DATAGROUP']/parent::fieldset")
	private WebElement fieldSetDatagroup;
	
//	@FindBy (xpath = "//a[@title='detail pagina publiek']")
//	private WebElement urlPublic;



	// Buttons, icons, ...

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
	@FindBy(xpath = "//input[@atfid='add_ncrs_specimen_inst_coll_subcoll?selectfield']/parent::span/parent::td/img[@class='errorImage']")
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
	
	// #19 Copy
	@FindBy(id = "btn_copy")
	private WebElement iconCopyDocument;

	// #20 Delete
	@FindBy(id = "btn_delete")
	private WebElement iconDeleteDocument;

	// #21 Create report
	@FindBy(id = "btn_genereerrapportage")
	private WebElement iconCreateReport;

	// #22 Show stats
	@FindBy(id = "btn_stats")
	private WebElement iconShowStats;

	// #23 Add to working set
	@FindBy(id = "ctl00_masterContent_btn_werkset")
	private WebElement iconAddToWorkingSet;

	
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
		case "icon19":
			icon = iconCopyDocument;
			break;
		case "icon20":
			icon = iconDeleteDocument;
			break;
		case "icon21":
			icon = iconCreateReport;
			break;
		case "icon22":
			icon = iconShowStats;
			break;
		case "icon23":
			icon = iconAddToWorkingSet;
			break;

		default:
			icon = null;
		}

		EditIcon thisIcon = new EditIcon();		
		
		WebDriverWait wait = new WebDriverWait(driver, 3);
		wait.until(ExpectedConditions.visibilityOf(icon));
		
		thisIcon.getSrc(icon.getAttribute("src").trim());
		thisIcon.getAlt(icon.getAttribute("alt").trim());
		thisIcon.getTitle(icon.getAttribute("title").trim());
		return thisIcon;
	}

	// .... End of Icons
	
	
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
		this.PageURL = Configuration.getDomain() + this.PageURL;
	}

	public void setPageUrlQueryString(String queryString) {
		this.PageUrlQueryString = queryString;
	}

	public void saveDocument() {
		driver.switchTo().defaultContent();
		this.iconSaveDocument.click();
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

	public String getPrefix() {
		if (prefix.getText().length() > 0) {
			return prefix.getText();
		} else {
			return prefix.getAttribute("lastval");
		}
	}

	public void setPrefix(String text) {
		this.switchToFrame_1();
		if (text == "TAB") {
			this.prefix.sendKeys(Keys.TAB);
		} else {
			this.prefix.sendKeys(text);
		}
	}

	public void setPrefixThesaurus(String text) {
		this.setPrefix(text);
		this.driver.switchTo().defaultContent();
		this.driver.switchTo().frame("ctl00_masterContent_iframe_1");
		this.driver.findElement(By.partialLinkText(text + "(Prefix)")).click();
		this.driver.switchTo().defaultContent();
		this.driver.switchTo().frame("iframe_1");
	}

	public void deletePrefix() {
		this.prefixDeleteIcon.click();
	}

	public String getNumber() {
		if (number.getText().length() > 0) {
			return number.getText();
		} else {
			return number.getAttribute("innerHTML");
		}
	}

	public void setNumber(String text) {
		this.switchToFrame_1();
		this.number.sendKeys(text);
	}

	public void deleteNumber() {
		this.number.clear();
	}

	public String getSuffix() {
		return suffix.getText();
	}

	public void setSuffix(String text) {
		this.switchToFrame_1();
		this.suffix.sendKeys(text);
	}

	public void deleteSuffix() {
		this.switchToFrame_1();
		this.suffix.clear();
	}

	public String getRegistrationNumber() {
		this.switchToFrame_1();
		return this.registrationNumber.getAttribute("lastval");
	}

	public void setRegistrationNumber(String text) {
		this.switchToFrame_1();
		if (text == "TAB") {
			this.registrationNumber.sendKeys(Keys.TAB);
		} else {
			this.registrationNumber.sendKeys(text);
		}
	}

	public void deleteRegistrationNumber() {
		this.switchToFrame_1();
		this.registrationNumber.clear();
	}

	public String getAltRegistrationNumberErrorMessage() {
		return this.registrationNumberErrorMessage.getAttribute("value");
	}

	public String getAltRegistrationNumberErrorIcon() {
		return this.registrationNumberErrorIcon.getAttribute("alt");
	}

	public String getSourceInstitute() {
		this.switchToFrame_1();
		return this.sourceInstitute.getText();
	}

	public void setSourceInstitute(String text) {
		this.switchToFrame_1();
		this.sourceInstitute.sendKeys(text);
	}

	public void deleteSourceInstitute() {
		this.switchToFrame_1();
		this.sourceInstitute.clear();
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

	public void deleteBasisOfRecord() {
		this.switchToFrame_1();
		this.basisOfRecord.click();
		this.clearBasisOfRecord.click();
	}

	public String getBasisOfRecordTlink() {
		this.switchToFrame_1();
		return this.tlinkBasisOfRecord.getText();
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

	public void deleteMount() {
		this.switchToFrame_1();
		this.mount.click();
		this.clearMount.click();
	}

	public String getMountTlink() {
		this.switchToFrame_1();
		return this.tlinkMount.getText();
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

	public void deletePreservedPart() {
		this.switchToFrame_1();
		this.preservedPart.click();
		this.preservedPart.click();
	}

	public String getPreservedPartTlink() {
		this.switchToFrame_1();
		return this.tlinkPreservedPart.getText();
	}

	public String getRemarks() {
		return remarks.getText();
	}

	public void setRemarks(String text) {
		this.switchToFrame_1();
		this.remarks.sendKeys(text);
	}

	public void deleteRemarks() {
		this.switchToFrame_1();
		this.remarks.clear();
	}

	// STORAGE

	// Standard Storage Unit

	public String getStandardStorageUnit() {
		return this.standardStorageUnit.getText();
	}

	public void setStandardStorageUnit(String text) {
		if (text == "TAB") {
			this.standardStorageUnit.sendKeys(Keys.TAB);
		} else {
			this.standardStorageUnit.sendKeys(text);
		}
	}

	public void selectStandardStorageUnit(String text) {
		this.standardStorageUnit.sendKeys(text);
		WebElement autoSuggest = (new WebDriverWait(driver, 3))
				.until(ExpectedConditions.presenceOfElementLocated(By.partialLinkText(text)));
		autoSuggest.click();
	}

	public void deleteStandardStorageUnit() {
		this.standardStorageUnit.clear();
	}
	
	public String getAttributeStandardStorageUnit(String attribute) {
		return this.standardStorageUnit.getCssValue(attribute);
	}
	
	public Boolean isStandardStorageUnitEnabled() {
		return this.standardStorageUnit.isEnabled();
	}

	public int numberOfSuggestsStandardStorageUnit(String text) {
		setStandardStorageUnit(text);
		List<WebElement> suggestsList = driver.findElements(By.xpath("*//ul[@role='listbox']/li[@role='menuitem']"));
		return suggestsList.size();
	}

	public String getWarningStandardStorageUnitErrorIcon() {
		return this.standardStorageUnitErrorIcon.getAttribute("alt");
	}

	// Standard Storage Location

	public String getStandardStorageLocation() {
		this.switchToFrame_1();
		return this.standardStorageLocation.getText();
	}

	public void setStandardStorageLocation(String text) {
		if (text == "TAB") {
			this.standardStorageLocation.sendKeys(Keys.TAB);
		} else {
			this.standardStorageLocation.sendKeys(text);
		}
	}

	public WebDriver selectStandardStorageLocation() {		
		// Click on the Button "New Message Window"
		this.standardStorageLocationSelectIcon.click();
		driver.switchTo().defaultContent();

		Set<String> AllWindowHandles = driver.getWindowHandles();
		String mainWindow = (String) AllWindowHandles.toArray()[0];
		String popupWindow = (String) AllWindowHandles.toArray()[1];
		
		// Switching from main window to popup window
//		driver.switchTo().window(popupWindow);
//		System.out.println("Title popup: " + driver.getTitle());
		return driver.switchTo().window(popupWindow);

		// Close the Pop Up window
		// driver.close();
	}
	
	public int numberOfWindows() {
		// It may take a while for a window to close, hence wait a short while ...
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// ... and now start checking the number of windows.
		driver.switchTo().defaultContent();
		Set<String> windowHandles = driver.getWindowHandles();
		return windowHandles.size();
	}

	public int numberOfSuggestsStandardStorageLocation(String text) {
		setStandardStorageLocation(text);
		List<WebElement> suggestsList = driver.findElements(By.partialLinkText("DW"));
		return suggestsList.size();
	}

	public void deleteStandardStorageLocation() {
		this.standardStorageLocationBinIcon.click();
	}

	public String getAttributeStandardStorageLocation(String attribute) {
		return this.standardStorageLocation.getCssValue(attribute);
	}

	public Boolean isStandardStorageLocationEnabled() {
		return this.standardStorageLocation.isEnabled();
	}

	public String getWarningStandardStorageLocationErrorIcon() {
		return this.standardStorageLocationErrorIcon.getAttribute("alt");
	}

	// Temporary Storage Unit

	public String getTemporaryStorageUnit() {
		return this.temporaryStorageUnit.getText();
	}

	public void setTemporaryStorageUnit(String text) {
		if (text == "TAB") {
			this.temporaryStorageUnit.sendKeys(Keys.TAB);
		} else {
			this.temporaryStorageUnit.sendKeys(text);
		}
	}

	public void selectTemporaryStorageUnit(String text) {
		this.temporaryStorageUnit.sendKeys(text);
		WebElement autoSuggest = (new WebDriverWait(driver, 3))
				.until(ExpectedConditions.presenceOfElementLocated(By.partialLinkText(text)));
		autoSuggest.click();
	}

	public void deleteTemporaryStorageUnit() {
		this.temporaryStorageUnit.clear();
	}

	public String getAttributeTemporaryStorageUnit(String attribute) {
		return this.temporaryStorageUnit.getCssValue(attribute);
	}
	
	public Boolean isTemporaryStorageUnitEnabled() {
		return this.temporaryStorageUnit.isEnabled();
	}
	
	public int numberOfSuggestsTemporaryStorageUnit(String text) {
		setStandardStorageUnit(text);
		List<WebElement> suggestsList = driver.findElements(By.xpath("*//ul[@role='listbox']/li[@role='menuitem']"));
		return suggestsList.size();
	}

	public String getWarningTemporaryStorageUnitErrorIcon() {
		return this.temporaryStorageUnitErrorIcon.getAttribute("alt");
	}

	// Temporary Storage Location

	public String getTemporaryStorageLocation() {
		return this.temporaryStorageLocation.getText();
	}

	public void setTemporaryStorageLocation(String text) {
		if (text == "TAB") {
			this.temporaryStorageLocation.sendKeys(Keys.TAB);
		} else {
			this.temporaryStorageLocation.sendKeys(text);
		}
	}

	public int numberOfSuggestsTemporaryStorageLocation(String text) {
		setTemporaryStorageLocation(text);
		List<WebElement> suggestsList = driver.findElements(By.partialLinkText("DW"));
		return suggestsList.size();
	}

	public void deleteTemporaryStorageLocation() {
		this.temporaryStorageLocationBinIcon.click();
	}

	public String getAttributeTemporaryStorageLocation(String attribute) {
		return this.temporaryStorageLocation.getCssValue(attribute);
	}

	public Boolean isTemporaryStorageLocationEnabled() {
		return this.temporaryStorageLocation.isEnabled();
	}

	public String getWarningTemporaryStorageLocationErrorIcon() {
		return this.standardStorageLocationErrorIcon.getAttribute("alt");
	}

	// ***

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
		WebElement testSelect = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='ctl00_masterContent_txt_FormulierenUpdatePanel']/select")));
		Select select = new Select(this.formulierenSelect);
		select.selectByVisibleText(optionLabel);
		select.getFirstSelectedOption().click();
		buttonFormulierenSelect.click();
	}

	public String findSelectedFormulier() {
		// driver.switchTo().defaultContent();
		switchToFrame_1();
		Select dropDown = new Select(formulierenSelect);
		WebElement options = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//select[@id='ctl00_masterContent_ddl_fomulieren']/option[@selected]")));
		String selected = dropDown.getFirstSelectedOption().getText();
		return selected;
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

	public void doDetailSearch(String searchterm) {
		driver.switchTo().defaultContent();
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
	
	public String getContextDisplayRegistrationNumber() {
		driver.switchTo().defaultContent();
		driver.switchTo().frame("ctl00_masterContent_iframe_1");
		return this.contextDisplay.findElement(By.xpath("*//label")).getText().trim();
	}

	public boolean isLegendDataGroupAvailable() {
		if (this.fieldSetDatagroup.findElement(By.tagName("legend")).getText().trim().equals("DATAGROUP")) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isDataGroupStructureOK() {
		List<WebElement> dataGroups = this.fieldSetDatagroup.findElements(By.xpath("//*[@id='accordion']"));
		boolean structure = false;
		for (WebElement dataGroup : dataGroups) {
			if ((dataGroup.findElement(By.cssSelector("*[role='tab']")) != null) && (dataGroup.findElement(By.cssSelector("*[role='tabpanel']")) != null )) {
				structure = true;
			}
			else {
				structure = false;
				break;
			}
		}
		return structure;	
	}
	
	public int numberDataGroups() {
		List<WebElement> dataGroups = this.fieldSetDatagroup.findElements(By.xpath("//*[@id='accordion']/h3"));
		return dataGroups.size(); 
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
