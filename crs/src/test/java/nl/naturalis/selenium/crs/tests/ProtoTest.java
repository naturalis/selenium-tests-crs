package nl.naturalis.selenium.crs.tests;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import nl.naturalis.selenium.crs.fragments.EditIcon;
import nl.naturalis.selenium.crs.fragments.InputGroup;
import nl.naturalis.selenium.crs.fragments.MenuItems;
import nl.naturalis.selenium.crs.configuration.*;
import nl.naturalis.selenium.crs.pages.*;
import nl.naturalis.selenium.crs.utils.MissingConfigurationException;
import nl.naturalis.selenium.crs.utils.Report;

public class ProtoTest extends AbstractTest {

	private static String projectID = "CRS";
	private static String testID = "Test 02";
	private static String testNumber = new SimpleDateFormat("yyyyMMdd").format(new Date()) + "01";

	private static MenuItems addMenu;
	private static HomePage homePage;
	private static StartPage startPage;
	private static DetailBeschrijvingenPage detailBeschrijvingenPage;
	private static List<String> formListLabels;

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
		driver.get(config.getStartUrl());
		Report.LogTestStart();
		homePage = new HomePage(driver);
	}

	@AfterClass
	private static void cleanUp() {
		// tearDown();
		Report.LogTestEnd();
	}
	
	
	/**
	 * Make a screenshot whenever a test fails.
	 */
	@AfterMethod
	public void takeScreenShotOnFailure(ITestResult testResult) throws IOException {
		if (testResult.getStatus() == ITestResult.FAILURE) {
			System.out.println("Test " + testResult.getName() + "failed.");
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
		startPage = homePage.doLogin(config.getUsername(), config.getPassword());
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
	 * 2.1.1 Kies via "Kies een alternatief formulier" het te testen formulier.
	 * (Als het te testen formulier default al geopend is, kies dan een andere.)
	 * Laadt deze?
	 */
	@Test(priority = 7, dependsOnMethods = { "openFormSelect" })
	public void selectSpecifiedForm() {
		detailBeschrijvingenPage.clickFormulierenSelectOption(formListLabels.get(0));
		Assert.assertEquals(detailBeschrijvingenPage.findSelectedFormulier(), formListLabels.get(0), "Fout in 2.1.1");
	}

	
	
	/**
	 * 2.1.9 Losse scripts (afhandeling registratienummer).
	 * 
	 * 1. Voer het veld Suffix in met een ongeldige waarde (geldige waarde: één of twee kleine letters). 
	 * 2. Verschijnt er direct een rood uitroepteken achter het veld?  
	 *   
	 */
	@Test(priority = 8, dependsOnMethods = { "selectSpecifiedForm" })
	public void checkSuffix() {
		ProtoTest.detailBeschrijvingenPage.switchToMasterContentFrame();
		ProtoTest.detailBeschrijvingenPage.setSuffix("abc");
		Assert.assertEquals(ProtoTest.detailBeschrijvingenPage.getSuffix(), "", "Fout in 2.1.9");
		ProtoTest.detailBeschrijvingenPage.deleteSuffix();
	}
	
	/**
	 * 2.1.10	"Losse scripts (afhandeling registratienummer). 
	 * 
	 * Voer het veld  Registratienummer in met: 
	 * 1. minimaal een ongeldige (a) prefix of (b) suffix of (c) zonder nummeriek deel. 
	 * 2. Verschijnt er een uitroepteken achter het veld?"
	 */
	@Test(priority = 9, dependsOnMethods = { "selectSpecifiedForm" })
	public void checkRegistrationNumber() {
//		ProtoTest.detailBeschrijvingenPage.switchToMasterContentFrame();
		// (a)
		ProtoTest.detailBeschrijvingenPage.setPrefix("abc");
		ProtoTest.detailBeschrijvingenPage.setNumber("1234");
		ProtoTest.detailBeschrijvingenPage.setRegistrationNumber("abc");
		ProtoTest.detailBeschrijvingenPage.setRemarks("hello there");
		ProtoTest.detailBeschrijvingenPage.deleteRemarks();
		Assert.assertEquals(ProtoTest.detailBeschrijvingenPage.getAltRegistrationNumberErrorIcon(), "Incorrect prefix", "Fout in 2.1.10");
		ProtoTest.detailBeschrijvingenPage.deletePrefix();
		ProtoTest.detailBeschrijvingenPage.deleteNumber();
		ProtoTest.detailBeschrijvingenPage.deleteRegistrationNumber();

		// (b)
		ProtoTest.detailBeschrijvingenPage.setPrefixThesaurus("TEST");
		ProtoTest.detailBeschrijvingenPage.setNumber("1234");
		ProtoTest.detailBeschrijvingenPage.setSuffix("abc");
		ProtoTest.detailBeschrijvingenPage.setRegistrationNumber("abc");
		ProtoTest.detailBeschrijvingenPage.setRemarks("hello there");
		ProtoTest.detailBeschrijvingenPage.deleteRemarks();
		Assert.assertEquals(ProtoTest.detailBeschrijvingenPage.getAltRegistrationNumberErrorIcon(), "Incorrect suffix", "Fout in 2.1.10");

		// (c)
		ProtoTest.detailBeschrijvingenPage.deleteNumber();
		ProtoTest.detailBeschrijvingenPage.deleteRegistrationNumber();
		ProtoTest.detailBeschrijvingenPage.deleteSuffix();
		ProtoTest.detailBeschrijvingenPage.setRegistrationNumber("abc");
		ProtoTest.detailBeschrijvingenPage.setRemarks("hello there");
		ProtoTest.detailBeschrijvingenPage.deleteRemarks();
		Assert.assertEquals(ProtoTest.detailBeschrijvingenPage.getAltRegistrationNumberErrorMessage(), "Invalid", "Fout in 2.1.10");
		ProtoTest.detailBeschrijvingenPage.deleteRegistrationNumber();
		ProtoTest.detailBeschrijvingenPage.deleteSuffix();
		ProtoTest.detailBeschrijvingenPage.deleteNumber();
		ProtoTest.detailBeschrijvingenPage.deletePrefix();
	}

	/**
	 *  2.1.11 Losse scripts (afhandeling registratienummer).
	 * 
	 *  Voer de velden Prefix, Number en Suffix in met een juiste waarde (Prefix anders dan 'e'). 
	 *  Wordt het veld Registrationnumber automatisch gevuld met de samengestelde waarde uit de 
	 *  hiervoor genoemde velden nadat deze velden zijn verlaten?
	 */
	@Test(priority = 31, dependsOnMethods = { "checkRegistrationNumber" })
	public void checkCreateRegistrationNumber() {
		String testNumber = new SimpleDateFormat("yyyyMMdd").format(new Date()) + "01";
		ProtoTest.detailBeschrijvingenPage.setCurrentCollectionName("Vert", "Vert");
		ProtoTest.detailBeschrijvingenPage.setPrefix("TEST");
		ProtoTest.detailBeschrijvingenPage.setPrefix("TAB");
		ProtoTest.detailBeschrijvingenPage.setNumber(testNumber);
		ProtoTest.detailBeschrijvingenPage.setSuffix("se"); // "se" : Selenium test suffix
		ProtoTest.detailBeschrijvingenPage.setRegistrationNumber("TAB");
		Assert.assertEquals(ProtoTest.detailBeschrijvingenPage.getRegistrationNumber(), "TEST" + "." + testNumber + "." + "se", "Fout in 2.1.11");
		Assert.assertEquals(ProtoTest.detailBeschrijvingenPage.getAltRegistrationNumberErrorMessage(), "Valid", "Fout in 2.1.11");
	}
	
	/**
	 *  2.1.12 Losse scripts (afhandeling registratienummer).
	 *  
	 *  Voer de velden Prefix en Number in waar Prefix een kleine letter e is, 
	 *  1. verschijnt er geen punt tussen de e en het nummeriek deel in het veld registratienummer?
	 */
	@Test(priority = 32, dependsOnMethods = { "checkCreateRegistrationNumber" })
	public void checkCreateRegistrationNumberWithE() {		
		String testNumber = new SimpleDateFormat("yyyyMMdd").format(new Date()) + "01";
		ProtoTest.detailBeschrijvingenPage.deletePrefix();
		ProtoTest.detailBeschrijvingenPage.setPrefix("TAB");
		ProtoTest.detailBeschrijvingenPage.setPrefix("e");
		ProtoTest.detailBeschrijvingenPage.setPrefix("TAB");
		Assert.assertEquals(ProtoTest.detailBeschrijvingenPage.getRegistrationNumber(), "e" + testNumber + "." + "se", "Fout in 2.1.12");
	}

	/**
	 * 2.1.13 Losse scripts (afhandeling registratienummer).
	 * 
	 *  Leeg het veld Registrationnumer en voer een juiste waarde in met prefix 'e'. 
	 *  Wordt dit juist uitelkaar getrokken naar de velden Prefix, Number en Suffix 
	 *  nadat het veld Registrationnumber is verlaten?
	 */
	@Test(priority = 32, dependsOnMethods = { "checkCreateRegistrationNumber" })
	public void checkEnterRegistrationNumberWithE() {
		ProtoTest.detailBeschrijvingenPage.deletePrefix();
		ProtoTest.detailBeschrijvingenPage.deleteNumber();
		ProtoTest.detailBeschrijvingenPage.deleteSuffix();
		ProtoTest.detailBeschrijvingenPage.deleteRegistrationNumber();
		ProtoTest.detailBeschrijvingenPage.setRegistrationNumber("e" + ProtoTest.testNumber + ".se");
		ProtoTest.detailBeschrijvingenPage.setRegistrationNumber("TAB");
		// Prefix
		Assert.assertEquals(ProtoTest.detailBeschrijvingenPage.getPrefix(), "e", "Fout in 2.1.13");

		// Catalog Number
		// The catalog number cannot be accessed directly and needs to be retrieved from the DOM
		// using JavaScript:
		// document.querySelectorAll('[atfid="add_ncrs_specimen_catalognumber?numberfield"]')[0].value
		JavascriptExecutor js = (JavascriptExecutor)driver;
		String catalogNumber = (String) js.executeScript("return document.querySelectorAll('[atfid=\"add_ncrs_specimen_catalognumber?numberfield\"]')[0].value;");
		Assert.assertEquals(catalogNumber, ProtoTest.testNumber, "Fout in 2.1.13");

		// Suffix 
		// document.querySelectorAll('[atfid="add_ncrs_specimen_suffix?suffixfield"]')[0].value
		String suffix = (String) js.executeScript("return document.querySelectorAll('[atfid=\"add_ncrs_specimen_suffix?suffixfield\"]')[0].value;");
		Assert.assertEquals(suffix, "se", "Fout in 2.1.13");
	}

	
}