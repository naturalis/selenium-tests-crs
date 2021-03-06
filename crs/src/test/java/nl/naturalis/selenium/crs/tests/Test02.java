package nl.naturalis.selenium.crs.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import nl.naturalis.selenium.crs.configuration.Configuration;
import nl.naturalis.selenium.crs.fragments.EditIcon;
import nl.naturalis.selenium.crs.fragments.InputGroup;
import nl.naturalis.selenium.crs.fragments.MenuItems;
import nl.naturalis.selenium.crs.fragments.StorageLocations;
import nl.naturalis.selenium.crs.pages.DeletedDocuments;
import nl.naturalis.selenium.crs.pages.DetailBeschrijvingenPage;
import nl.naturalis.selenium.crs.pages.HomePage;
import nl.naturalis.selenium.crs.pages.StartPage;
import nl.naturalis.selenium.crs.utils.MissingConfigurationException;
import nl.naturalis.selenium.crs.utils.Report;


/**
 * Test02
 * 
 * @author Tom Gilissen <tom.gilissen@naturalis.nl>
 * @version 1.0
 *
 */
public class Test02 extends AbstractTest {

	private static String projectID = "CRS";
	private static String testNumber = new SimpleDateFormat("yyyyMMdd").format(new Date()) + "01";

	private static MenuItems addMenu;
	private static HomePage homePage;
	private static StartPage startPage;
	private static DetailBeschrijvingenPage detailBeschrijvingenPage;
	private static DeletedDocuments deletedDocumentsPage;
	private static List<String> formListLabels;
	private StorageLocations popupStorageLocations = new StorageLocations();

	private static void initializeTestParameters() {
		addMenu = new MenuItems(Arrays.asList("Employee", "Add", "Specimen", "Vertebrates"), -1);
		formListLabels = Arrays.asList("Collection Vertebrates Specimen", "Digistreet Vertebrates Specimen");
	}

	@BeforeClass
	private static void initalize() throws MissingConfigurationException, SQLException {
		initializeDatabase();
		initializeConfiguration(projectID);
		initializeTestParameters();
		initializeDriver();
		initializeWaiting(1);
		initializeLogging();
		driver.get(Configuration.getStartUrl());
		Report.LogTestStart();
		homePage = new HomePage(driver);
	}

	@AfterClass
	private static void cleanUp() throws SQLException {
		//tearDown();
		Report.LogTestEnd();
	}

	/**
	 * Save a screenshot whenever a test fails.
	 */
	@AfterMethod
	public void takeScreenShotOnFailure(ITestResult testResult) throws IOException {
		if (testResult.getStatus() == ITestResult.FAILURE) {
			System.out.println("Test " + testResult.getName() + " failed.");
			String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			File localFile = new File("test-output/screenshots/Test" + timeStamp + "-" + testResult.getName() + ".png");
			FileUtils.copyFile(scrFile, localFile);
		}
	}

	@Test(priority = 1)
	public void homePageOpen() {
		homePage = new HomePage(driver);
		Assert.assertEquals(driver.getCurrentUrl(), homePage.getPageURL());
	}

	@Test(priority = 2, dependsOnMethods = { "homePageOpen" })
	public void homePageDoLogin() {
		startPage = homePage.doLogin(Configuration.getUsername(), Configuration.getPassword());
		Assert.assertEquals(driver.getCurrentUrl(), startPage.getPageURL());
	}

	@Test(priority = 3, dependsOnMethods = { "homePageDoLogin" })
	public void startPageTitle() {
		Assert.assertEquals(driver.getTitle().trim(), startPage.getPageTitle());
	}

	@Test(priority = 4, dependsOnMethods = { "startPageTitle" })
	public void clickAddSpecimen() {
		WebElement UppMenu = startPage.getUppMenu();
		Actions action = new Actions(driver);

		for (String item : addMenu.getMenuItems()) {
			WebElement element = UppMenu.findElement(By.linkText(item));
			action.moveToElement(element).perform();
		}

		String lastItem = addMenu.getMenuItems().get(addMenu.getMenuItems().size() - 1);
		WebElement LastItem = UppMenu.findElement(By.linkText(lastItem));
		Assert.assertEquals(LastItem.getText(), lastItem);
		LastItem.click();
	}

	@Test(priority = 5, dependsOnMethods = { "clickAddSpecimen" })
	public void detailPageOpen() {
		detailBeschrijvingenPage = new DetailBeschrijvingenPage(driver);
		assertThat(driver.getTitle().trim(), containsString(detailBeschrijvingenPage.getPageTitle()));
	}

	@Test(priority = 6, dependsOnMethods = { "detailPageOpen" })
	public void openFormSelect() {
		List<WebElement> availableOptions = detailBeschrijvingenPage.clickFormulierenSelect();
		List<String> labels = new ArrayList<String>();

		for (WebElement e : availableOptions) {
			labels.add(e.getText());
		}

		Assert.assertEquals(labels, formListLabels);
	}

	/**
	 * 2.1.1 
	 * 
	 * Kies via "Kies een alternatief formulier" het te testen formulier.
	 * (Als het te testen formulier default al geopend is, kies dan een andere.)
	 * Laadt deze?
	 */
	@Test(priority = 7, dependsOnMethods = { "openFormSelect" })
	public void selectSpecifiedForm() {
		WebDriverWait wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("ctl00_masterContent_ddl_fomulieren")));
		detailBeschrijvingenPage.clickFormulierenSelectOption(formListLabels.get(0));
		Assert.assertEquals(detailBeschrijvingenPage.findSelectedFormulier(), formListLabels.get(0), "Fout in 2.1.1");
	}

	/**
	 * 2.1.2 
	 * 
	 * Staan op het te testen fomulier de iconen voor multimedia
	 * (linksboven), nieuw record, opslaan en save defaults & load defaults op
	 * het scherm met een mouse over als je de muis erover heen beweegt?
	 * 
	 */
	@Test(priority = 8, dependsOnMethods = { "openFormSelect" }) // dependsOnMethods = { "selectSpecifiedForm" })
	public void checkIconInfo() {
		EditIcon thisIcon = null;
		thisIcon = detailBeschrijvingenPage.getIconInfo("icon1");
		Assert.assertEquals(thisIcon.getSrc(),
				"https://crspl.naturalis.nl/AtlantisWeb/App_Themes/Flexible/images/buttons/multimedia_add.png",
				"Fout in 2.1.2");
		Assert.assertEquals(thisIcon.getTitle(), "Add multimedia", "Fout in 2.1.2");
		thisIcon = detailBeschrijvingenPage.getIconInfo("icon2");
		Assert.assertEquals(thisIcon.getSrc(),
				"https://crspl.naturalis.nl/AtlantisWeb/App_Themes/Flexible/images/buttons/multimedia_copy.png",
				"Fout in 2.1.2");
		Assert.assertEquals(thisIcon.getAlt(), "Attach all multimedia from the global selectie to this document",
				"Fout in 2.1.2");
		thisIcon = detailBeschrijvingenPage.getIconInfo("icon3");
		Assert.assertEquals(thisIcon.getSrc(),
				"https://crspl.naturalis.nl/AtlantisWeb/App_Themes/Flexible/images/buttons/multimedia_cut.png",
				"Fout in 2.1.2");
		Assert.assertEquals(thisIcon.getAlt(), "Move all multimedia from the global selectie to this document",
				"Fout in 2.1.2");
		thisIcon = detailBeschrijvingenPage.getIconInfo("icon4");
		Assert.assertEquals(thisIcon.getSrc(),
				"https://crspl.naturalis.nl/AtlantisWeb/App_Themes/Flexible/images/buttons/new.gif", "Fout in 2.1.2");
		Assert.assertEquals(thisIcon.getAlt(), "New", "Fout in 2.1.2");
		thisIcon = detailBeschrijvingenPage.getIconInfo("icon5");
		Assert.assertEquals(thisIcon.getSrc(),
				"https://crspl.naturalis.nl/AtlantisWeb/App_Themes/Flexible/images/buttons/save2.gif", "Fout in 2.1.2");
		Assert.assertEquals(thisIcon.getAlt(), "Save", "Fout in 2.1.2");
		thisIcon = detailBeschrijvingenPage.getIconInfo("icon6");
		Assert.assertEquals(thisIcon.getSrc(),
				"https://crspl.naturalis.nl/AtlantisWeb/App_Themes/Flexible/images/buttons/save_new2.gif",
				"Fout in 2.1.2");
		Assert.assertEquals(thisIcon.getAlt(), "Save and add a new document", "Fout in 2.1.2");
		thisIcon = detailBeschrijvingenPage.getIconInfo("icon7");
		Assert.assertEquals(thisIcon.getSrc(),
				"https://crspl.naturalis.nl/AtlantisWeb/App_Themes/Flexible/images/buttons/savedefault.gif",
				"Fout in 2.1.2");
		Assert.assertEquals(thisIcon.getTitle(), "Save defaults", "Fout in 2.1.2");
		// Todo: controle op buttons die AFWEZIG moeten zijn

	}

	/**
	 * 2.1.3
	 * 
	 * Test Context Scherm
	 * 
	 */
	@Test(priority = 9, dependsOnMethods = { "selectSpecifiedForm" })
	public void checkContextDisplay() {
		Assert.assertTrue(detailBeschrijvingenPage.isContextDisplayAvailable(), "Contextscherm is afwezig (2.1.3).");
		Assert.assertEquals(detailBeschrijvingenPage.getContextDisplayObjectType(), "Vertebrates Specimen",
				"Fout in Contextscherm (2.1.3).");
	}

	/**
	 * 2.1.4
	 * 
	 * Staat achter de velden Basis of record, Datagroup type en onder gathering
	 * site > Country (in storage units valt dit onder identification) een icoon
	 * voor de thesaurus en een prullenbak?
	 * 
	 */
	@Test(priority = 10, dependsOnMethods = { "selectSpecifiedForm" })
	public void checkThesaurusAndBinIcons() {
		EditIcon thisIcon = null;

		// Basis of record
		thisIcon = detailBeschrijvingenPage.getIconInfo("icon9");
		Assert.assertEquals(thisIcon.getSrc(),
				"https://crspl.naturalis.nl/AtlantisWeb/App_Themes/Flexible/images/buttons/node-select-child.png",
				"Fout in 2.1.4");
		Assert.assertEquals(thisIcon.getTitle(), "Select", "Fout in 2.1.4");

		thisIcon = detailBeschrijvingenPage.getIconInfo("icon10");
		Assert.assertEquals(thisIcon.getSrc(),
				"https://crspl.naturalis.nl/AtlantisWeb/App_Themes/Flexible/images/buttons/delete7.gif",
				"Fout in 2.1.4");
		Assert.assertEquals(thisIcon.getTitle(), "Clear", "Fout in 2.1.4");

		// Data group type
		thisIcon = detailBeschrijvingenPage.getIconInfo("icon11");
		Assert.assertEquals(thisIcon.getSrc(),
				"https://crspl.naturalis.nl/AtlantisWeb/App_Themes/Flexible/images/buttons/node-select-child.png",
				"Fout in 2.1.4");
		Assert.assertEquals(thisIcon.getTitle(), "Select", "Fout in 2.1.4");

		thisIcon = detailBeschrijvingenPage.getIconInfo("icon12");
		Assert.assertEquals(thisIcon.getSrc(),
				"https://crspl.naturalis.nl/AtlantisWeb/App_Themes/Flexible/images/buttons/delete7.gif",
				"Fout in 2.1.4");
		Assert.assertEquals(thisIcon.getTitle(), "Clear", "Fout in 2.1.4");

		// Gathering Site > Country
		Test02.detailBeschrijvingenPage.switchToGatheringSitesFrame();
		// Algemenere methode: moet nog uitgewerkt worden ...
		// this.detailBeschrijvingenPage.buttonAddGatheringSite.click();
		// this.detailBeschrijvingenPage.switchToFrameContainingElementID("select21");

		thisIcon = detailBeschrijvingenPage.getIconInfo("icon13");
		Assert.assertEquals(thisIcon.getSrc(),
				"https://crspl.naturalis.nl/AtlantisWeb/App_Themes/Flexible/images/buttons/node-select-child.png",
				"Fout in 2.1.4");
		Assert.assertEquals(thisIcon.getTitle(), "Select", "Fout in 2.1.4");

		thisIcon = detailBeschrijvingenPage.getIconInfo("icon14");
		Assert.assertEquals(thisIcon.getSrc(),
				"https://crspl.naturalis.nl/AtlantisWeb/App_Themes/Flexible/images/buttons/delete7.gif",
				"Fout in 2.1.4");
		Assert.assertEquals(thisIcon.getTitle(), "Clear", "Fout in 2.1.4");

		Test02.detailBeschrijvingenPage.clickCloseButton();
	}

	/**
	 * 2.1.5
	 * 
	 * Voer de verplichte velden Current Collection name, Basis of record,
	 * Mount, en Preserved part in met waarden die niet voorkomen in de
	 * thesaurus.
	 * 
	 * A. Verschijnt er een rood uitroepteken achter het veld als je naar het
	 * volgende veld gaat, en het veld leeg laat of B. een 'verkeerde' waarde
	 * invoert? C. Blijft de verkeerde waarde staan?
	 * 
	 */
	// 1. Current Collection name
	@Test(priority = 11, dependsOnMethods = { "checkThesaurusAndBinIcons" })
	public void checkIncorrectSubmissionValueTest1A() {
		// Scroll into view first
		((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");
		// wait a little while for this to happen
		try {
			Thread.sleep(250);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// Verkeerde waarde
		EditIcon thisIcon = null;
		Test02.detailBeschrijvingenPage.enterValueToField("currentcollectionname", "illegal-text");
		thisIcon = detailBeschrijvingenPage.getIconInfo("icon15");
		Assert.assertEquals(thisIcon.getSrc(),
				"https://crspl.naturalis.nl/AtlantisWeb/App_Themes/Base/images/buttons/alert.gif", "Fout in 2.1.5");
		Assert.assertEquals(thisIcon.getTitle(), "Invalid (sub)collection name", "Fout in 2.1.5");
	}

	@Test(priority = 12, dependsOnMethods = { "checkIncorrectSubmissionValueTest1A" })
	public void checkIncorrectSubmissionValueTest1B() {
		// Blijft de verkeerde waarde staan?
		Assert.assertEquals(Test02.detailBeschrijvingenPage.readValueFromField("currentcollectionname"), "illegal-text",
				"Fout in 2.1.5");
		Test02.detailBeschrijvingenPage.enterValueToField("currentcollectionname", "CLEAR");
	}

	// 2. Basis of record
	@Test(priority = 13, dependsOnMethods = { "checkIncorrectSubmissionValueTest1B" })
	public void checkIncorrectSubmissionValueTest2A() {
		// Verkeerde waarde
		EditIcon thisIcon = null;
		Test02.detailBeschrijvingenPage.enterValueToField("basisofrecord", "illegal-text");
		thisIcon = detailBeschrijvingenPage.getIconInfo("icon16");
		Assert.assertEquals(thisIcon.getSrc(),
				"https://crspl.naturalis.nl/AtlantisWeb/App_Themes/Base/images/buttons/alert.gif", "Fout in 2.1.5");
		Assert.assertEquals(thisIcon.getTitle(), "May only contain thesaurus value!", "Fout in 2.1.5");
	}

	@Test(priority = 14, dependsOnMethods = { "checkIncorrectSubmissionValueTest1B" })
	public void checkIncorrectSubmissionValueTest2B() {
		// Blijft de verkeerde waarde staan?
		Assert.assertEquals(Test02.detailBeschrijvingenPage.readValueFromField("basisofrecord"), "illegal-text",
				"Fout in 2.1.5");
		Test02.detailBeschrijvingenPage.enterValueToField("basisofrecord", "CLEAR");
	}

	// 3. Mount
	@Test(priority = 15, dependsOnMethods = { "checkIncorrectSubmissionValueTest2B" })
	public void checkIncorrectSubmissionValueTest3A() {
		// Verkeerde waarde
		EditIcon thisIcon = null;
		Test02.detailBeschrijvingenPage.enterValueToField("mount", "illegal-text");
		thisIcon = detailBeschrijvingenPage.getIconInfo("icon17");
		Assert.assertEquals(thisIcon.getSrc(),
				"https://crspl.naturalis.nl/AtlantisWeb/App_Themes/Base/images/buttons/alert.gif", "Fout in 2.1.5");
		Assert.assertEquals(thisIcon.getTitle(), "May only contain thesaurus value!", "Fout in 2.1.5");
	}

	@Test(priority = 19, dependsOnMethods = { "checkIncorrectSubmissionValueTest2B" })
	public void checkIncorrectSubmissionValueTest3B() {
		// Blijft de verkeerde waarde staan?
		Assert.assertEquals(Test02.detailBeschrijvingenPage.readValueFromField("mount"), "illegal-text",
				"Fout in 2.1.5");
		Test02.detailBeschrijvingenPage.enterValueToField("mount", "CLEAR");
	}

	// 4. Preserved part
	@Test(priority = 21, dependsOnMethods = { "checkIncorrectSubmissionValueTest3B" })
	public void checkIncorrectSubmissionValueTest4A() {
		// Verkeerde waarde
		EditIcon thisIcon = null;
		Test02.detailBeschrijvingenPage.enterValueToField("preservedpart", "illegal-text");
		thisIcon = detailBeschrijvingenPage.getIconInfo("icon18");
		Assert.assertEquals(thisIcon.getSrc(),
				"https://crspl.naturalis.nl/AtlantisWeb/App_Themes/Base/images/buttons/alert.gif", "Fout in 2.1.5");
		Assert.assertEquals(thisIcon.getTitle(), "May only contain thesaurus value!", "Fout in 2.1.5");
	}

	@Test(priority = 22, dependsOnMethods = { "checkIncorrectSubmissionValueTest3B" })
	public void checkIncorrectSubmissionValueTest4B() {
		// Blijft de verkeerde waarde staan?
		Assert.assertEquals(Test02.detailBeschrijvingenPage.readValueFromField("preservedpart"), "illegal-text",
				"Fout in 2.1.5");
		Test02.detailBeschrijvingenPage.enterValueToField("preservedpart", "CLEAR");
	}

	/**
	 * 2.1.6
	 * 
	 * Voer de verplichte velden Current Collection name, Basis of record, Mount
	 * en Preserved part in met een waarde die voorkomt in de bijbehorende
	 * thesaurus.
	 * 
	 * 1. Verschijnt er een autosuggest onder het veld als er een waarde uit de
	 * bijbehorende thesauruslijst wordt getypt? en 2. verschijnt er achter het
	 * prullenbakje de gekozen thesaurus term?
	 * 
	 */

	@Test(priority = 23, dependsOnMethods = { "checkIncorrectSubmissionValueTest3B" })
	public void checkTextEntryCurrentCollectionName() {
		// Pisces(Vertebrates)
		String entryText = "p";
		String collectionName = "Pisces";
		Test02.detailBeschrijvingenPage.setCurrentCollectionName(entryText, collectionName);
		Assert.assertEquals(Test02.detailBeschrijvingenPage.getCurrentCollectionNameTlink(), collectionName,
				"Fout in 2.1.6: Current Collection name.");
	}

	@Test(priority = 24, dependsOnMethods = { "checkIncorrectSubmissionValueTest3B" })
	public void checkTextEntryBasisOfRecord() {
		// Basis of record
		String entryText = "v";
		String specimenType = "VirtualSpecimen";
		// Test02.detailBeschrijvingenPage.setBasisOfRecord(entryText,
		// specimenType);
		// Assert.assertEquals(Test02.detailBeschrijvingenPage.getBasisOfRecordTlink(),
		// specimenType, "Fout in 2.1.6: Basis of Record.");
		InputGroup basisOfRecord = new InputGroup(driver, "uniqueID3219180130");
		basisOfRecord.enterThesaurusValue(entryText, specimenType);
		Assert.assertEquals(basisOfRecord.getThesaurusConcept(), specimenType, "Fout in 2.1.6: Basis of Record.");
	}

	@Test(priority = 25, dependsOnMethods = { "checkIncorrectSubmissionValueTest3B" })
	public void checkTextEntryMount() {
		// Mount
		String entryText = "n";
		String specimenType = "not applicable";
		Test02.detailBeschrijvingenPage.setMount(entryText, specimenType);
		Assert.assertEquals(Test02.detailBeschrijvingenPage.getMountTlink(), specimenType, "Fout in 2.1.6: Mount");
	}

	@Test(priority = 26, dependsOnMethods = { "checkIncorrectSubmissionValueTest3B" })
	public void checkTextEntryPreservedPart() {
		// Preserved part
		String entryText = "n";
		String specimenType = "Not applicable";
		Test02.detailBeschrijvingenPage.setPreservedPart(entryText, specimenType);
		Assert.assertEquals(Test02.detailBeschrijvingenPage.getPreservedPartTlink(), specimenType,
				"Fout in 2.1.6: Preserved part.");
	}

	/**
	 * 2.1.7 Losse scripts (afhandeling registratienummer).
	 * 
	 * Voer het veld Prefix in met een ongeldige waarde (waarde komt niet voor
	 * in thesaurus lijst prefix). 1. Verschijnt er direct een rood uitroepteken
	 * achter het veld? of 2. verschijnt deze dat bij het verlaten van het veld
	 * (als er een default is gedefineerd; thesaurus waarde staat achter
	 * prullenbak icoon)?
	 * 
	 */
	@Test(priority = 27, dependsOnMethods = { "checkIncorrectSubmissionValueTest3B" })
	public void checkPrefix() {
		// Test met InputGroupExt
		InputGroup prefix = new InputGroup(driver, "uniqueID1219179928");

		// 1. error icon?
		prefix.enterText("illegal-text");
		Assert.assertEquals(prefix.getWarningErrorIcon(), "Enter prefix out of the prefix thesaurus list");
		Assert.assertEquals(prefix.getSrcErrorIconSrc(),
				"https://crspl.naturalis.nl/AtlantisWeb/App_Themes/Base/images/buttons/alert.gif");

		// 2. thesaurus waarde?
		prefix.clickBin();
		prefix.enterThesaurusValue("n", "NCBN");
		Assert.assertEquals(prefix.getThesaurusConcept(), "NCBN");
	}

	/**
	 * 2.1.8 Losse scripts (afhandeling registratienummer).
	 * 
	 * 1. Voer het veld Number in met een alfanummerieke waarde. 2. Klopt het
	 * dat dit niet geaccepteerd wordt?
	 * 
	 */
	@Test(priority = 28, dependsOnMethods = { "checkIncorrectSubmissionValueTest3B" })
	public void checkNumber() {
		Test02.detailBeschrijvingenPage.setNumber("abc");
		Assert.assertEquals(Test02.detailBeschrijvingenPage.getNumber(), "", "Fout in 2.1.8");
	}

	/**
	 * 2.1.9 Losse scripts (afhandeling registratienummer).
	 * 
	 * 1. Voer het veld Suffix in met een ongeldige waarde (geldige waarde: één
	 * of twee kleine letters). 2. Verschijnt er direct een rood uitroepteken
	 * achter het veld?
	 * 
	 */
	@Test(priority = 29, dependsOnMethods = { "checkIncorrectSubmissionValueTest3B" })
	public void checkSuffix() {
		Test02.detailBeschrijvingenPage.setSuffix("abc");
		Assert.assertEquals(Test02.detailBeschrijvingenPage.getSuffix(), "", "Fout in 2.1.9");
		Test02.detailBeschrijvingenPage.deleteSuffix();
	}

	/**
	 * 2.1.10 "Losse scripts (afhandeling registratienummer).
	 * 
	 * Voer het veld Registratienummer in met: 1. minimaal een ongeldige (a)
	 * prefix of (b) suffix of (c) zonder nummeriek deel. 2. Verschijnt er een
	 * uitroepteken achter het veld?"
	 */
	@Test(priority = 30, dependsOnMethods = { "checkSuffix" })
	public void checkRegistrationNumber() {
		// ProtoTest.detailBeschrijvingenPage.switchToMasterContentFrame();
		// (a)
		Test02.detailBeschrijvingenPage.deletePrefix();
		Test02.detailBeschrijvingenPage.setPrefix("abc");
		Test02.detailBeschrijvingenPage.setNumber("1234");
		Test02.detailBeschrijvingenPage.setRegistrationNumber("abc");
		Test02.detailBeschrijvingenPage.setRegistrationNumber("TAB");
		Assert.assertEquals(Test02.detailBeschrijvingenPage.getAltRegistrationNumberErrorIcon(), "Incorrect prefix",
				"Fout in 2.1.10");
		Test02.detailBeschrijvingenPage.deletePrefix();
		Test02.detailBeschrijvingenPage.deleteNumber();
		Test02.detailBeschrijvingenPage.deleteRegistrationNumber();

		// (b)
		Test02.detailBeschrijvingenPage.setPrefixThesaurus("TEST");
		Test02.detailBeschrijvingenPage.setNumber("1234");
		Test02.detailBeschrijvingenPage.setSuffix("abc");
		Test02.detailBeschrijvingenPage.setRegistrationNumber("abc");
		Test02.detailBeschrijvingenPage.setRegistrationNumber("TAB");
		Assert.assertEquals(Test02.detailBeschrijvingenPage.getAltRegistrationNumberErrorIcon(), "Incorrect suffix",
				"Fout in 2.1.10");

		// (c)
		Test02.detailBeschrijvingenPage.deleteNumber();
		Test02.detailBeschrijvingenPage.deleteRegistrationNumber();
		Test02.detailBeschrijvingenPage.deleteSuffix();
		Test02.detailBeschrijvingenPage.setRegistrationNumber("abc");
		Test02.detailBeschrijvingenPage.setRemarks("hello there");
		Test02.detailBeschrijvingenPage.deleteRemarks();
		Assert.assertEquals(Test02.detailBeschrijvingenPage.getAltRegistrationNumberErrorMessage(), "Invalid",
				"Fout in 2.1.10");
	}

	/**
	 * 2.1.11 Losse scripts (afhandeling registratienummer).
	 * 
	 * Voer de velden Prefix, Number en Suffix in met een juiste waarde (Prefix
	 * anders dan 'e'). Wordt het veld Registrationnumber automatisch gevuld met
	 * de samengestelde waarde uit de hiervoor genoemde velden nadat deze velden
	 * zijn verlaten?
	 */
	@Test(priority = 31, dependsOnMethods = { "checkRegistrationNumber" })
	public void checkCreateRegistrationNumber() {

		String testNumber = new SimpleDateFormat("yyyyMMdd").format(new Date()) + "01";
		Test02.detailBeschrijvingenPage.deleteCurrentCollectionName();
		Test02.detailBeschrijvingenPage.setCurrentCollectionName("Vert", "Vert");
		Test02.detailBeschrijvingenPage.deletePrefix();
		Test02.detailBeschrijvingenPage.setPrefixExt("TEST");
		Test02.detailBeschrijvingenPage.setPrefixExt("TAB");
		Test02.detailBeschrijvingenPage.setNumber(testNumber);
		Test02.detailBeschrijvingenPage.setSuffix("se"); // "se" : Selenium test
															// suffix
		Test02.detailBeschrijvingenPage.setRegistrationNumber("TAB");
		Assert.assertEquals(Test02.detailBeschrijvingenPage.getRegistrationNumber(),
				"TEST" + "." + testNumber + "." + "se", "Fout in 2.1.11");
		Assert.assertEquals(Test02.detailBeschrijvingenPage.getAltRegistrationNumberErrorMessage(), "Valid",
				"Fout in 2.1.11");
	}

	/**
	 * 2.1.12 Losse scripts (afhandeling registratienummer).
	 * 
	 * Voer de velden Prefix en Number in waar Prefix een kleine letter e is, 1.
	 * verschijnt er geen punt tussen de e en het nummeriek deel in het veld
	 * registratienummer?
	 */
	@Test(priority = 32, dependsOnMethods = { "checkCreateRegistrationNumber" })
	public void checkCreateRegistrationNumberWithE() {
		String testNumber = new SimpleDateFormat("yyyyMMdd").format(new Date()) + "01";
		Test02.detailBeschrijvingenPage.deletePrefix();
		Test02.detailBeschrijvingenPage.setPrefixExt("TAB");
		Test02.detailBeschrijvingenPage.setPrefixExt("e");
		Test02.detailBeschrijvingenPage.setPrefixExt("TAB");
		Assert.assertEquals(Test02.detailBeschrijvingenPage.getRegistrationNumber(), "e" + testNumber + "." + "se",
				"Fout in 2.1.12");
	}

	/**
	 * 2.1.13 Losse scripts (afhandeling registratienummer).
	 * 
	 * Leeg het veld Registrationnumer en voer een juiste waarde in met prefix
	 * 'e'. Wordt dit juist uitelkaar getrokken naar de velden Prefix, Number en
	 * Suffix nadat het veld Registrationnumber is verlaten?
	 */
	@Test(priority = 32, dependsOnMethods = { "checkCreateRegistrationNumber" })
	public void checkEnterRegistrationNumberWithE() {
		Test02.detailBeschrijvingenPage.deletePrefix();
		Test02.detailBeschrijvingenPage.deleteNumber();
		Test02.detailBeschrijvingenPage.deleteSuffix();
		Test02.detailBeschrijvingenPage.deleteRegistrationNumber();
		Test02.detailBeschrijvingenPage.setRegistrationNumber("e" + Test02.testNumber + ".se");
		Test02.detailBeschrijvingenPage.setRegistrationNumber("TAB");
		/* Prefix */
		Assert.assertEquals(Test02.detailBeschrijvingenPage.getPrefixExt(), "e", "Fout in 2.1.13.01");

		/*
		 * Catalog Number The catalog number cannot be accessed directly and
		 * needs to be retrieved from the DOM using JavaScript:
		 * document.querySelectorAll('[atfid=
		 * "add_ncrs_specimen_catalognumber?numberfield"]')[0].value
		 */
		JavascriptExecutor js = (JavascriptExecutor) driver;
		String catalogNumber = (String) js.executeScript(
				"return document.querySelectorAll('[atfid=\"add_ncrs_specimen_catalognumber?numberfield\"]')[0].value;");
		Assert.assertEquals(catalogNumber, Test02.testNumber, "Fout in 2.1.13.02");

		/*
		 * Suffix document.querySelectorAll('[atfid=
		 * "add_ncrs_specimen_suffix?suffixfield"]')[0].value
		 */
		String suffix = (String) js.executeScript(
				"return document.querySelectorAll('[atfid=\"add_ncrs_specimen_suffix?suffixfield\"]')[0].value;");
		Assert.assertEquals(suffix, "se", "Fout in 2.1.13.03");
	}

	/**
	 * 2.1.14 Losse scripts (afhandeling registratienummer).
	 * 
	 * Leeg het veld Registrationnumer en voer een juiste waarde in met prefix
	 * anders dan 'e'. Wordt dit juist uit elkaar getrokken naar de velden
	 * Prefix, Number en Suffix nadat het veld Registrationnumber is verlaten?
	 */
	@Test(priority = 33, dependsOnMethods = { "checkEnterRegistrationNumberWithE" })
	public void checkEnterRegistrationNumberWithoutE() {
		Test02.detailBeschrijvingenPage.deletePrefix();
		Test02.detailBeschrijvingenPage.deleteNumber();
		Test02.detailBeschrijvingenPage.deleteSuffix();
		Test02.detailBeschrijvingenPage.deleteRegistrationNumber();
		Test02.detailBeschrijvingenPage.setRegistrationNumber("TEST" + "." + Test02.testNumber + ".se");
		Test02.detailBeschrijvingenPage.setRegistrationNumber("TAB");
		/* Prefix */
		Assert.assertEquals(Test02.detailBeschrijvingenPage.getPrefixExt(), "TEST", "Fout in 2.1.14.01");

		/*
		 * Catalog Number The catalog number cannot be accessed directly and
		 * needs to be retrieved from the DOM using JavaScript:
		 * document.querySelectorAll('[atfid=
		 * "add_ncrs_specimen_catalognumber?numberfield"]')[0].value
		 */
		JavascriptExecutor js = (JavascriptExecutor) driver;
		String catalogNumber = (String) js.executeScript(
				"return document.querySelectorAll('[atfid=\"add_ncrs_specimen_catalognumber?numberfield\"]')[0].value;");
		Assert.assertEquals(catalogNumber, Test02.testNumber, "Fout in 2.1.14.02");

		/*
		 * Suffix document.querySelectorAll('[atfid=
		 * "add_ncrs_specimen_suffix?suffixfield"]')[0].value
		 */
		String suffix = (String) js.executeScript(
				"return document.querySelectorAll('[atfid=\"add_ncrs_specimen_suffix?suffixfield\"]')[0].value;");
		Assert.assertEquals(suffix, "se", "Fout in 2.1.14.03");
	}

	/**
	 * 2.1.15
	 * 
	 * Vul Standard of Temporary storage unit in met een onjuiste waarde (juiste
	 * waarde is BE. gevolgd door een nummer). Verschijnt er een
	 * waarschuwingsteken met 'invalid value' bij het verlaten van het veld?
	 * 
	 */
	@Test(priority = 34, dependsOnMethods = { "checkEnterRegistrationNumberWithoutE" })
	public void checkIllegalValuesStorageUnit() {
		Test02.detailBeschrijvingenPage.setStandardStorageUnit("illegal-text");
		Test02.detailBeschrijvingenPage.setStandardStorageUnit("TAB");
		Assert.assertEquals(Test02.detailBeschrijvingenPage.getWarningStandardStorageUnitErrorIcon(), "Invalid value",
				"Fout in 2.1.15.01");

		Test02.detailBeschrijvingenPage.setTemporaryStorageUnit("illegal-text");
		Test02.detailBeschrijvingenPage.setTemporaryStorageUnit("TAB");
		Assert.assertEquals(Test02.detailBeschrijvingenPage.getWarningTemporaryStorageUnitErrorIcon(), "Invalid value",
				"Fout in 2.1.15.02");
	}

	/**
	 * 2.1.16
	 * 
	 * Vul Standard of Temporary storage unit in met een juiste waarde (juiste
	 * waarde is BE. gevolgd door een nummer). Verschijnt de autosuggest?
	 * 
	 */
	@Test(priority = 35, dependsOnMethods = { "checkIllegalValuesStorageUnit" })
	public void checkAutosuggestStorageUnit() {
		Test02.detailBeschrijvingenPage.deleteStandardStorageUnit();
		Test02.detailBeschrijvingenPage.deleteTemporaryStorageUnit();
		Assert.assertEquals(Test02.detailBeschrijvingenPage.numberOfSuggestsStandardStorageUnit("BE"), 10,
				"Fout in 2.1.16");
		Test02.detailBeschrijvingenPage.deleteStandardStorageUnit();
		Test02.detailBeschrijvingenPage.setStandardStorageUnit("TAB");
	}

	/**
	 * 2.1.17
	 * 
	 * Losse scripts (afhandeling Storage)
	 * 
	 * Zodra er een waarde ingevoerd is in Standard storage unit 1. kleurt het
	 * veld Standard storage location dan grijs en 2. kan niet worden ingevoerd?
	 * Of,
	 * 
	 * bij het invoeren van Temporary storage unit 3. kleurt dan Temporary
	 * storage location grijs en 4. kan niet worden ingevoerd?
	 */
	@Test(priority = 36, dependsOnMethods = { "checkAutosuggestStorageUnit" })
	public void checkEnterStorageUnits() {
		String standardStorageUnitTestValue = "BE.0000001";
		String temporaryStorageUnitTestValue = "BE.0000002";

		Test02.detailBeschrijvingenPage.selectStandardStorageUnit(standardStorageUnitTestValue);
		// 1. kleurt het veld Standard storage location grijs?
		Assert.assertEquals(Test02.detailBeschrijvingenPage.getAttributeStandardStorageLocation("background-color"),
				"rgba(128, 128, 128, 1)", "Fout in 2.1.17.01");
		// 2. kan niet worden ingevoerd?
		Assert.assertFalse(Test02.detailBeschrijvingenPage.isStandardStorageLocationEnabled(), "Fout in 2.1.17.02");

		Test02.detailBeschrijvingenPage.selectTemporaryStorageUnit(temporaryStorageUnitTestValue);
		// 3. kleurt het veld Temporary storage location grijs?
		Assert.assertEquals(Test02.detailBeschrijvingenPage.getAttributeTemporaryStorageLocation("background-color"),
				"rgba(128, 128, 128, 1)", "Fout in 2.1.17.03");
		// 4. kan niet worden ingevoerd?
		Assert.assertFalse(Test02.detailBeschrijvingenPage.isTemporaryStorageLocationEnabled(), "Fout in 2.1.17.04");
	}

	/**
	 * 2.1.18
	 * 
	 * Vul Standard of Temporary storage location in met een onjuiste waarde.
	 * Verschijnt er een uitroepteken met 'invalid value' achter het veld?
	 * 
	 */
	@Test(priority = 37, dependsOnMethods = { "checkEnterStorageUnits" })
	public void checkIllegalValuesStorageLocations() {
		String illegalTestValue = "illegal-text";
		Test02.detailBeschrijvingenPage.deleteStandardStorageUnit();
		Test02.detailBeschrijvingenPage.deleteTemporaryStorageUnit();

		Test02.detailBeschrijvingenPage.setStandardStorageLocation(illegalTestValue);
		Test02.detailBeschrijvingenPage.setStandardStorageLocation("TAB");
		Assert.assertEquals(Test02.detailBeschrijvingenPage.getWarningStandardStorageLocationErrorIcon(),
				"Invalid value", "Fout in 2.1.18.01");

		Test02.detailBeschrijvingenPage.setTemporaryStorageLocation(illegalTestValue);
		Test02.detailBeschrijvingenPage.setTemporaryStorageLocation("TAB");
		Assert.assertEquals(Test02.detailBeschrijvingenPage.getWarningTemporaryStorageLocationErrorIcon(),
				"Invalid value", "Fout in 2.1.18.02");

		Test02.detailBeschrijvingenPage.deleteStandardStorageLocation();
		Test02.detailBeschrijvingenPage.deleteTemporaryStorageLocation();
	}

	/**
	 * 2.1.19
	 * 
	 * Vul Standard of Temporary storage location in met een juiste waarde.
	 * Verschijnt de autosuggest ?
	 */
	@Test(priority = 38, dependsOnMethods = { "checkIllegalValuesStorageLocations" })
	public void checkAutosuggestStorageLocations() {
		Assert.assertEquals(Test02.detailBeschrijvingenPage.numberOfSuggestsStandardStorageLocation("DW"), 10,
				"Fout in 2.1.19.01");
		Assert.assertEquals(Test02.detailBeschrijvingenPage.numberOfSuggestsTemporaryStorageLocation("DW"), 10,
				"Fout in 2.1.19.01");

		Test02.detailBeschrijvingenPage.deleteStandardStorageLocation();
		Test02.detailBeschrijvingenPage.deleteTemporaryStorageLocation();
	}

	/**
	 * 2.1.20
	 * 
	 * Klik op het calculator Icoon achter 1 van de 2 storage location velden.
	 * 1. Verschijnt er een pop-up (storage location selectie scherm) a. met
	 * daarin de storage location boom b. met achter elke storage location de
	 * naam?
	 */
	@Test(priority = 39, dependsOnMethods = { "checkAutosuggestStorageLocations" })
	public void checkSelectButtonStorageLocations() {
		this.popupStorageLocations.setDriver(detailBeschrijvingenPage.selectStandardStorageLocation());
		
		WebDriverWait wait = new WebDriverWait(driver, 3);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("RadBoom")));

		// 1. Check wether there is a popup window with the title "Selecteer"
		Assert.assertEquals(popupStorageLocations.getTitle(), "Selecteer", "Fout in 2.1.20.01");

		// 2. Check wether there are storage locations
		List<WebElement> listStorageLocations = popupStorageLocations.getStorageLocations();
		int i = 0;
		for (WebElement element : listStorageLocations) {
			Assert.assertEquals((element.getText().length() > 0), true, "Fout in 2.1.20.02");
			i++;
		}
		Assert.assertEquals((i > 0), true);
	}

	/**
	 * 2.1.21
	 * 
	 * Bij het uitklappen van een grote reeks locaties in het storage location
	 * selectiescherm (dmv plus) 1. zijn er paginanummers aanwezig (rechts van
	 * storage location), en 2. werken deze? Tevens 3. Is er een snel zoeken box
	 * aanwezig?
	 * 
	 */
	@Test(priority = 40, dependsOnMethods = { "checkSelectButtonStorageLocations" })
	public void checkStorageLocationsPageing() {
		popupStorageLocations.clickDW_E_01_017();
		List<WebElement> pageLinks = popupStorageLocations.getBoomPagerLinks();
		Assert.assertTrue(pageLinks.size() > 0, "Fout in 2.1.21.01");
		pageLinks.get(0).click();
		pageLinks = popupStorageLocations.getBoomPagerLinks();
		Assert.assertEquals(popupStorageLocations.getCurrentPage(), "2", "Fout in 2.1.21.02");
		Assert.assertTrue(popupStorageLocations.searchBoxAvailable(), "Fout in 2.1.21.03");
	}

	/**
	 * 2.1.22
	 * 
	 * Klik in de pop-up van storage location door de boom heen en 
	 * selecteer een storage location door hierop te klikken. 
	 * 1. Wordt de storage location weergegeven in een grijs blok?
	 * 
	 */
	@Test(priority = 41, dependsOnMethods = { "checkStorageLocationsPageing" })
	public void checkSelectingStorageLocations() {
		Assert.assertTrue(popupStorageLocations.selectStorageLocation(), "Fout in 2.1.22");
	}

	/**
	 * 2.1.23
	 * 
	 * Klik in de pop-up van storage location op het icoon rechtsboven. 
	 * 1. Is de geselecteerde storage location nu ingevoerd in het gekozen veld 
	 * (Standard of Temporary) storage location?
	 * 
	 */

	@Test(priority = 42, dependsOnMethods = { "checkSelectingStorageLocations" })
	public void checkKoppelStorageLocation() {
		popupStorageLocations.koppelStorageLocation();

		// Let's now test whether the popup window has actually been closed
		Assert.assertEquals(Test02.detailBeschrijvingenPage.numberOfWindows(), 1, "Fout in 2.1.23.01");

		// Test if the Standard Storage Location is filled and starts with DW
		Test02.detailBeschrijvingenPage.switchToFrame_1();
		JavascriptExecutor js = (JavascriptExecutor) driver;
		String stringStandardStorageLocation = (String) js
				.executeScript("return document.querySelectorAll('[name=\"CURRENTSTORAGELOCATION\"]')[0].value;");
		Assert.assertTrue(stringStandardStorageLocation.substring(0, 2).equals("DW"), "Fout in 2.1.23.02");
	}

	/**
	 * 2.1.24
	 * 
	 * Losse scripts (afhandeling Storage)
	 * 
	 * Zodra er een waarde ingevoerd is in Standard storage location 
	 * 1. kleurt het veld Standard storage unit dan grijs, en 
	 * 2. kan niet worden ingevoerd? 
	 *    Of bij het invoeren van Temporary storage location 
	 * 3. kleurt dan Temporary storage unit grijs, en 
	 * 4. kan niet worden ingevoerd?
	 * 
	 */
	@Test(priority = 43, dependsOnMethods = { "checkKoppelStorageLocation" })
	public void checkInputStorageLocations() {
		// 1. kleurt het veld Standard storage unit dan grijs?
		Assert.assertEquals(Test02.detailBeschrijvingenPage.getAttributeStandardStorageUnit("background-color"),
				"rgba(128, 128, 128, 1)", "Fout in 2.1.24.01");
		// 2. kan niet worden ingevoerd?
		Assert.assertFalse(Test02.detailBeschrijvingenPage.isStandardStorageUnitEnabled(), "Fout in 2.1.24.02");

		// Of bij het invoeren van Temporary storage location
		Test02.detailBeschrijvingenPage.setTemporaryStorageLocation("DW.E.01.017.003");
		Test02.detailBeschrijvingenPage.setTemporaryStorageLocation("TAB");
		// 3. kleurt dan Temporary storage unit grijs en
		Assert.assertEquals(Test02.detailBeschrijvingenPage.getAttributeTemporaryStorageUnit("background-color"),
				"rgba(128, 128, 128, 1)", "Fout in 2.1.24.01");
		// 4. kan niet worden ingevoerd?
		Assert.assertFalse(Test02.detailBeschrijvingenPage.isTemporaryStorageUnitEnabled(), "Fout in 2.1.24.02");
	}

	/**
	 * 2.1.25
	 * 
	 * Voer in het veld Remarks de invoerwaarde testrecord in, en sla het record op. 
	 * 1. Lukt dit? 
	 * 2. Noteer in het veld invoerwaarde de tijd tussen het op save klikken en 
	 * het verdwijnen van het saving blok (het scherm hoeft dus niet opnieuw volledig geladen te zijn)
	 */
	@Test(priority = 43, dependsOnMethods = { "checkKoppelStorageLocation" })
	public void testFirstRecordSave() {
		// Voer in het veld Remarks de invoerwaarde testrecord in
		Test02.detailBeschrijvingenPage.setRemarks("Selenium testrecord");
		// sla het record op.
		long startTime = System.currentTimeMillis();
		Test02.detailBeschrijvingenPage.saveDocument();

		// 1. Lukt dit?
		WebElement url = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@title='detail pagina publiek']")));
		Assert.assertTrue(url.getAttribute("href").length() > 0);
		long elapsedTime = System.currentTimeMillis() - startTime;
		System.out.println("Registration number test record: " + "TEST" + "." + Test02.testNumber + ".se");
		System.out.println("Saving time: " + elapsedTime / 1000 + " secs");
	}
	
	/**
	 * 2.1.26
	 * 
	 * Zijn na het opslaan van het nieuwe record  de iconen Copy, Delete, Create report, 
	 * Statistiek, Add to working set, erbij gekomen?
	 */
	@Test(priority = 44, dependsOnMethods = { "testFirstRecordSave" })
	public void checkExtraIcons() {
		// Copy 
		EditIcon thisIcon = null;
		thisIcon = detailBeschrijvingenPage.getIconInfo("icon19");
		Assert.assertEquals(thisIcon.getAlt(), "Copy", "Fout in 2.1.26 - Copy icon");
		// Delete
		thisIcon = detailBeschrijvingenPage.getIconInfo("icon20");
		Assert.assertEquals(thisIcon.getAlt(), "Delete", "Fout in 2.1.26 - Delete icon");
		// Create report
		thisIcon = detailBeschrijvingenPage.getIconInfo("icon21");
		Assert.assertEquals(thisIcon.getAlt(), "Create report", "Fout in 2.1.26 - Create report icon");
		// Statistiek
		thisIcon = detailBeschrijvingenPage.getIconInfo("icon22");
		Assert.assertEquals(thisIcon.getAlt(), "Geef statistieken weer", "Fout in 2.1.26 - Statistiek icon");
		// Add to working set
		thisIcon = detailBeschrijvingenPage.getIconInfo("icon23");
		Assert.assertEquals(thisIcon.getAlt(), "Add to working set", "Fout in 2.1.26 - Add to working set icon");		
	}
	
	/** 
	 * 2.1.27
	 * 
	 * Verschijnt het registratienummer juist in het samenvattende lichtblauwe blok rechtsboven?
	 * (Vul het registratienummer in dit formulier in de kolom invoerwaarde in.)
	 */
	@Test(priority = 45, dependsOnMethods = { "testFirstRecordSave" })
	public void checkContextRegistrationNumber() {
		Assert.assertEquals(Test02.detailBeschrijvingenPage.getContextDisplayRegistrationNumber(), "TEST" + "." + Test02.testNumber + ".se");
	}
	
	/**
	 * 2.1.28
	 * 
	 * Wordt de datagroup correct getoond (blauwe balk)?
	 */
	@Test(priority = 46, dependsOnMethods = { "checkContextRegistrationNumber" })
	public void checkDataGroups() {
		Assert.assertTrue(Test02.detailBeschrijvingenPage.isLegendDataGroupAvailable(), "Fout in 2.1.27 - Legenda data group afwezig.");
		Assert.assertTrue(Test02.detailBeschrijvingenPage.isDataGroupStructureOK(), "Fout in 2.1.27 - Structuur data group niet OK.");
		Assert.assertTrue((Test02.detailBeschrijvingenPage.numberDataGroups() >= 1), "Fout in 2.1.27 - Geen data group aanwezig.");
	}
	
	/**
	 * 2.1.29
	 * 
	 * 1. Zijn de thesaurus icons aanwezig in de datagroup en 
	 * 2. werken deze?
	 * 
	 */
	@Test(priority = 46, dependsOnMethods = { "checkDataGroups" })
	public void checkDataGroupThesaurusIcons() {
		Assert.assertTrue(Test02.detailBeschrijvingenPage.dataGroupThesaurusLinks(), "Fout in 2.1.29 - Geen thesauruslinks aanwezig in data group.");
		Assert.assertTrue(Test02.detailBeschrijvingenPage.dataGroupTestFirstTlink(), "Fout in 2.1.29 - Koppelen van thesaurus concept in data group heeft gefaald.");
	}

	/**
	 * 2.1.30
	 * 
	 * 1. Wordt auto-suggest getoond bij de thesaurusvelden? 
	 * 
	 */
	@Test(priority = 46, dependsOnMethods = { "checkDataGroupThesaurusIcons" })
	public void checkDataGroupThesaurusAutoSuggest() {
		Assert.assertTrue(Test02.detailBeschrijvingenPage.dataGroupTestAutoSuggest(), "Fout in 2.1.30 - Geen autosuggest.");
		Assert.assertTrue(Test02.detailBeschrijvingenPage.dataGroupTestAutoSuggestSelect(), "Fout in 2.1.30 - Selecteren van thesaurusterm mislukt.");
		
		// Save the record to prevent pop-ups when trying to leave the record
		Test02.detailBeschrijvingenPage.saveDocument();
		
		// Wait for the overlay to disappear
		WebDriverWait wait = new WebDriverWait(driver, 15);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class='ui-widget-overlay']")));
	}
	
	/**
	 * 
	 * 2.7.a
	 * 
	 * Delete the test record
	 * 
	 */
	@Test(priority = 47, dependsOnMethods = { "checkDataGroupThesaurusAutoSuggest" })
	public void deleteTestRecord() {
		driver.get("https://crspl.naturalis.nl/AtlantisWeb/default.aspx");
		driver.switchTo().defaultContent();
		WebDriverWait wait = new WebDriverWait(driver, 15);
		WebElement detailSearch = wait
				.until(ExpectedConditions.presenceOfElementLocated(By.id("ctl00_QuickSearchTextBox")));
		detailSearch.sendKeys("TEST" + "." + Test02.testNumber + ".se");
		detailSearch.sendKeys(Keys.ENTER);

		driver.findElement(By.id("btn_delete")).click();
		driver.switchTo().frame(0); // This frame now contains one frame for user interaction
		driver.switchTo().frame(0); // and this contains the delete button
		driver.findElement(By.id("btn_verwijder_client")).click();
		Alert deleteAlert = driver.switchTo().alert();
		String alertText = deleteAlert.getText();
		Assert.assertEquals(alertText.trim(), "Weet u zeker dat u de geselecteerde onderdelen wilt verwijderen?",
				"Fout bij het verwijderen van een record: Waarschuwing klopt niet."); // Test 2.7.2

		// Now, either dismiss
		// deleteAlert.dismiss();

		// or accept deleting this record
		deleteAlert.accept();

		// Wait for the record to be deleted
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//*[@title='detail pagina publiek']")));

		// Test if we are back at the start page now?
		String url = driver.getCurrentUrl();
		Assert.assertEquals(url.trim(), startPage.getPageURL() + "?back=true");
	}
	
	/**
	 * 2.7.b
	 * 
	 * Restore the deleted test record
	 * 
	 */
	@Test(priority = 48, dependsOnMethods = { "deleteTestRecord" })
	public void restoreDeleteTestRecord() {

		driver.get(
				"https://crspl.naturalis.nl/AtlantisWeb/pages/medewerker/ZoekenVerwijderdedocumenten.aspx?restart=true");
		deletedDocumentsPage = new DeletedDocuments(driver);

		deletedDocumentsPage.selectFormulierByName("Vertebrates");
		deletedDocumentsPage.restoreRecord("TEST" + "." + Test02.testNumber + ".se");

		Assert.assertTrue(deletedDocumentsPage.findRegistrationNumber("TEST" + "." + Test02.testNumber + ".se"), "Record is niet restored");
	}

	/**
	 * 2.7.c
	 * 
	 * Delete and completely remove the test record
	 * 
	 */
	@Test(priority = 49, dependsOnMethods = { "restoreDeleteTestRecord" })
	public void removeTestRecord() {
		driver.get("https://crspl.naturalis.nl/AtlantisWeb/default.aspx");
		driver.switchTo().defaultContent();
		WebDriverWait wait = new WebDriverWait(driver, 15);
		WebElement detailSearch = wait
				.until(ExpectedConditions.presenceOfElementLocated(By.id("ctl00_QuickSearchTextBox")));
		// detailSearch.sendKeys("TEST" + "." + Test02.testNumber + ".se");
		detailSearch.sendKeys("TEST" + "." + testNumber + ".se");
		detailSearch.sendKeys(Keys.ENTER);

		driver.findElement(By.id("btn_delete")).click();
		driver.switchTo().frame(0); // This frame now contains one frame for user interaction
		driver.switchTo().frame(0); // and this contains the delete button
		driver.findElement(By.id("btn_verwijder_client")).click();
		Alert deleteAlert = driver.switchTo().alert();
		deleteAlert.accept();

		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("ctl00_LoginName")));
		driver.get("https://crspl.naturalis.nl/AtlantisWeb/pages/medewerker/ZoekenVerwijderdedocumenten.aspx");
		deletedDocumentsPage.selectFormulierByName("Vertebrates");
		deletedDocumentsPage.removeRecord("TEST" + "." + testNumber + ".se");

		// Wait for a short while to allow the record to be deleted
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// Return home
		driver.get(startPage.getPageURL());
		wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("ctl00_LoginName")));
	}

	
}