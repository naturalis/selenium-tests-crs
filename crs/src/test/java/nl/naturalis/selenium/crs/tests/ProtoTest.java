package nl.naturalis.selenium.crs.tests;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
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
import nl.naturalis.selenium.crs.fragments.StorageLocations;
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


	// Load the test record
	@Test(priority = 8, dependsOnMethods = { "selectSpecifiedForm" })
	public void loadTestRecord() {
		driver.switchTo().defaultContent();
		WebElement detailSearch = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.id("ctl00_QuickSearchTextBox")));
		detailSearch.sendKeys("TEST" + "." + ProtoTest.testNumber + ".se");
		detailSearch.sendKeys(Keys.ENTER);
		// WebElement myDynamicElement = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.id("registrationNumber")));
	}

	
	/**
	 * 
	 * New tests can be added from here
	 * 
	 */
	
	/**
	 * 2.1.26
	 * 
	 * Zijn na het opslaan van het nieuwe record  de iconen Copy, Delete, Create report, 
	 * Statistiek, Add to working set, erbij gekomen?
	 */
	@Test(priority = 44, dependsOnMethods = { "loadTestRecord" })
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
	@Test(priority = 45, dependsOnMethods = { "checkExtraIcons" })
	public void checkContextRegistrationNumber() {
		Assert.assertEquals(ProtoTest.detailBeschrijvingenPage.getContextDisplayRegistrationNumber(), "TEST" + "." + ProtoTest.testNumber + ".se");
	}
	
	/**
	 * 2.1.28
	 * 
	 * Wordt de datagroup correct getoond (blauwe balk)?
	 */
	@Test(priority = 46, dependsOnMethods = { "checkContextRegistrationNumber" })
	public void checkDataGroups() {
		Assert.assertTrue(ProtoTest.detailBeschrijvingenPage.isLegendDataGroupAvailable(), "Fout in 2.1.27 - Geen data group aanwezig.");
		Assert.assertTrue(ProtoTest.detailBeschrijvingenPage.isDataGroupStructureOK(), "Fout in 2.1.27 - Structuur data group niet OK.");
		Assert.assertTrue((ProtoTest.detailBeschrijvingenPage.numberDataGroups() >= 1), "Fout in 2.1.27 - Geen data group aanwezig.");
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
		Assert.assertTrue(ProtoTest.detailBeschrijvingenPage.dataGroupThesaurusLinks(), "Fout in 2.1.29 - Geen thesauruslinks aanwezig in data group.");
		Assert.assertTrue(ProtoTest.detailBeschrijvingenPage.dataGroupTestFirstTlink(), "Fout in 2.1.29 - Koppelen van thesaurus concept in data group heeft gefaald.");
	}

	/**
	 * 2.1.30
	 * 
	 * 1. Wordt auto-suggest getoond bij de thesaurusvelden? 
	 * 
	 */
	@Test(priority = 46, dependsOnMethods = { "checkDataGroupThesaurusIcons" })
	public void checkDataGroupThesaurusAutoSuggest() {
		Assert.assertTrue(ProtoTest.detailBeschrijvingenPage.dataGroupTestAutoSuggest(), "Fout in 2.1.30 - Geen autosuggest.");
		Assert.assertTrue(ProtoTest.detailBeschrijvingenPage.dataGroupTestAutoSuggestSelect(), "Fout in 2.1.30 - Selecteren van thesaurusterm mislukt.");
		
		// Save the record to prevent pop-ups when trying to leave the record
		ProtoTest.detailBeschrijvingenPage.saveDocument();
		
		
		// <div class="ui-widget-overlay" style="width: 1255px; height: 1310px; z-index: 1001;"></div>
		WebDriverWait wait = new WebDriverWait(driver, 15);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class='ui-widget-overlay']")));
	}
	
	/**
	 * Delete the test record created just now
	 * 
	 */
	@Test(priority = 47, dependsOnMethods = { "checkDataGroupThesaurusAutoSuggest" })
	public void deleteTestRecord() {
		driver.get("https://crspl.naturalis.nl/AtlantisWeb/default.aspx");
		driver.switchTo().defaultContent();
		WebDriverWait wait = new WebDriverWait(driver, 15);
		WebElement detailSearch = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("ctl00_QuickSearchTextBox")));
		detailSearch.sendKeys("TEST" + "." + ProtoTest.testNumber + ".se");
		detailSearch.sendKeys(Keys.ENTER);

		WebElement bin = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("btn_delete")));
		bin.click();
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		driver.switchTo().defaultContent();
		// wait = new WebDriverWait(driver, 5);
		int size = driver.findElements(By.xpath("//iframe")).size();
		System.out.println("# of frames: " + size);
				
		for (int n = 0; n < size; n++) {
			driver.switchTo().frame(n);
			System.out.println("n: " + n + " has: " + driver.findElements(By.xpath("//iframe")).size() + " frames.");
			List<WebElement> wantedElements = driver.findElements(By.tagName("title"));
			if (driver.findElements(By.xpath("//iframe")).size() > 0) {
				// driver.switchTo().frame(1);
				System.out.println(driver.getPageSource());
			}
			driver.switchTo().defaultContent();
		}


		
//		ProtoTest.detailBeschrijvingenPage.switchToFrameContainingElementID("innercontent");
//		System.out.println(driver.getTitle());
//		
//		WebElement deleteButton = driver.findElement(By.id("btn_verwijder_client"));
//		deleteButton.click();

		
		// Switching from main window to popup window
//		driver.switchTo().window(popupWindow);
//		System.out.println("Title popup: " + driver.getTitle());
//		return driver.switchTo().window(popupWindow);

	}
	
	

}