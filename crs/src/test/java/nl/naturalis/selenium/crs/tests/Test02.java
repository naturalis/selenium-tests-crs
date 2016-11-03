package nl.naturalis.selenium.crs.tests;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import nl.naturalis.selenium.crs.fragments.EditIcon;
import nl.naturalis.selenium.crs.fragments.MenuItems;
import nl.naturalis.selenium.crs.configuration.*;
import nl.naturalis.selenium.crs.pages.*;
import nl.naturalis.selenium.crs.utils.MissingConfigurationException;
import nl.naturalis.selenium.crs.utils.Report;


public class Test02 extends AbstractTest {

	private static String projectID = "CRS";
	private static String testID = "Test 02";
	
	private static MenuItems addMenu;
	private static HomePage homePage;
	private static StartPage startPage;
	private static DetailBeschrijvingenPage detailBeschrijvingenPage;
	private static List<String> formListLabels;
		
	@BeforeClass
	private static void initalize() throws MissingConfigurationException, SQLException {
		initializeDatabase();
		initializeConfiguration(projectID);
		initializeTestParameters();
		initializeDriver();
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
	
	@Test(priority=1)
	public void homePageOpen() {
		homePage = new HomePage(driver);
		Assert.assertEquals(driver.getCurrentUrl(),homePage.getPageURL());
	}
	
	@Test(priority=2, dependsOnMethods = { "homePageOpen" })
	public void homePageDoLogin() {
		startPage = homePage.doLogin(config.getUsername(), config.getPassword());
		Assert.assertEquals(driver.getCurrentUrl(),startPage.getPageURL());
	}

	@Test(priority=3, dependsOnMethods = { "homePageDoLogin" })
	public void startPageTitle() {
		Assert.assertEquals(driver.getTitle().trim(),startPage.getPageTitle());
	}
	
	@Test(priority=4, dependsOnMethods = { "startPageTitle" })
	public void clickAddSpecimen() {
		WebElement UppMenu = startPage.getUppMenu();
		Actions action = new Actions(driver);

		for (String item : addMenu.getMenuItems()) {
			WebElement element = UppMenu.findElement(By.linkText(item));
			action.moveToElement(element).perform();
		}

		String lastItem = addMenu.getMenuItems().get(addMenu.getMenuItems().size()-1);
		WebElement LastItem = UppMenu.findElement(By.linkText(lastItem));
		Assert.assertEquals(LastItem.getText(),lastItem);
		LastItem.click();
	}

	@Test(priority=5, dependsOnMethods = { "clickAddSpecimen" })
	public void detailPageOpen() {
		detailBeschrijvingenPage = new DetailBeschrijvingenPage(driver);
		assertThat(driver.getTitle().trim(),containsString(detailBeschrijvingenPage.getPageTitle()));
	}

	@Test(priority=6, dependsOnMethods = { "detailPageOpen" })
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
	 * Kies via "Kies een alternatief formulier" het te testen formulier. (Als het te testen formulier default 
	 * al geopend is, kies dan een andere.) Laadt deze?
	 */
	@Test(priority=7, dependsOnMethods = { "openFormSelect" })
	public void selectSpecifiedForm() {
		detailBeschrijvingenPage.clickFormulierenSelectOption(formListLabels.get(0));
		Assert.assertEquals(detailBeschrijvingenPage.findSelectedFormulier(), formListLabels.get(0), "Fout in 2.1.1");
	}

	/**
	 * 2.1.2
	 * Staan op het te testen fomulier de iconen voor multimedia (linksboven), nieuw record, opslaan en 
	 * save defaults & load defaults op het scherm met een mouse over als je de muis erover heen beweegt?
	 * 
	 */

	// Test icon "Add multimedia"
	@Test(priority=8, dependsOnMethods ={ "selectSpecifiedForm" })
	public void checkIconInfo() {
		EditIcon thisIcon = null; 
		thisIcon = detailBeschrijvingenPage.getIconInfo("icon1");
		Assert.assertEquals(thisIcon.getSrc(), "https://crspl.naturalis.nl/AtlantisWeb/App_Themes/Flexible/images/buttons/multimedia_add.png", "Fout in 2.1.2");
		Assert.assertEquals(thisIcon.getAlt(), "Add multimedia", "Fout in 2.1.2");
		thisIcon = detailBeschrijvingenPage.getIconInfo("icon2");
		Assert.assertEquals(thisIcon.getSrc(), "https://crspl.naturalis.nl/AtlantisWeb/App_Themes/Flexible/images/buttons/multimedia_copy.png", "Fout in 2.1.2");
		Assert.assertEquals(thisIcon.getAlt(), "Attach all multimedia from the global selectie to this document", "Fout in 2.1.2");
		thisIcon = detailBeschrijvingenPage.getIconInfo("icon3");
		Assert.assertEquals(thisIcon.getSrc(), "https://crspl.naturalis.nl/AtlantisWeb/App_Themes/Flexible/images/buttons/multimedia_cut.png", "Fout in 2.1.2");
		Assert.assertEquals(thisIcon.getAlt(), "Move all multimedia from the global selectie to this document", "Fout in 2.1.2");
		thisIcon = detailBeschrijvingenPage.getIconInfo("icon4");
		Assert.assertEquals(thisIcon.getSrc(), "https://crspl.naturalis.nl/AtlantisWeb/App_Themes/Flexible/images/buttons/new.gif", "Fout in 2.1.2");
		Assert.assertEquals(thisIcon.getAlt(), "New", "Fout in 2.1.2");
		thisIcon = detailBeschrijvingenPage.getIconInfo("icon5");
		Assert.assertEquals(thisIcon.getSrc(), "https://crspl.naturalis.nl/AtlantisWeb/App_Themes/Flexible/images/buttons/save2.gif", "Fout in 2.1.2");
		Assert.assertEquals(thisIcon.getAlt(), "Save", "Fout in 2.1.2");
		thisIcon = detailBeschrijvingenPage.getIconInfo("icon6");
		Assert.assertEquals(thisIcon.getSrc(), "https://crspl.naturalis.nl/AtlantisWeb/App_Themes/Flexible/images/buttons/save_new2.gif", "Fout in 2.1.2");
		Assert.assertEquals(thisIcon.getAlt(), "Save and add a new document", "Fout in 2.1.2");
		thisIcon = detailBeschrijvingenPage.getIconInfo("icon7");
		Assert.assertEquals(thisIcon.getSrc(), "https://crspl.naturalis.nl/AtlantisWeb/App_Themes/Flexible/images/buttons/savedefault.gif", "Fout in 2.1.2");
		Assert.assertEquals(thisIcon.getTitle(), "Save defaults", "Fout in 2.1.2");
		// Todo: controle op buttons die AFWEZIG moeten zijn
		
	}	

	// Test Context Scherm
	@Test(priority=9, dependsOnMethods = { "selectSpecifiedForm" })
	public void checkContextDisplay() {
		Assert.assertTrue(detailBeschrijvingenPage.isContextDisplayAvailable(), "Contextscherm is afwezig (2.1.3).");
		Assert.assertEquals(detailBeschrijvingenPage.getContextDisplayObjectType(), "Vertebrates Specimen", "Fout in Contextscherm (2.1.3)." );
	}

	/**
	 * 2.1.4
	 * 
	 * Staat achter de velden Basis of record, Datagroup type en onder gathering site > Country 
	 * (in storage units valt dit onder identification) een icoon voor de thesaurus en een prullenbak?
	 * 
	 */
	@Test(priority=10, dependsOnMethods = { "selectSpecifiedForm" })
	public void checkThesaurusAndBinIcons() {
		EditIcon thisIcon = null; 

		// Basis of record
		thisIcon = detailBeschrijvingenPage.getIconInfo("icon9");
		Assert.assertEquals(thisIcon.getSrc(), "https://crspl.naturalis.nl/AtlantisWeb/App_Themes/Flexible/images/buttons/node-select-child.png", "Fout in 2.1.4");
		Assert.assertEquals(thisIcon.getTitle(), "Select", "Fout in 2.1.4");
		
		thisIcon = detailBeschrijvingenPage.getIconInfo("icon10");
		Assert.assertEquals(thisIcon.getSrc(), "https://crspl.naturalis.nl/AtlantisWeb/App_Themes/Flexible/images/buttons/delete7.gif", "Fout in 2.1.4");
		Assert.assertEquals(thisIcon.getTitle(), "Clear", "Fout in 2.1.4");

		// Data group type
		thisIcon = detailBeschrijvingenPage.getIconInfo("icon11");
		Assert.assertEquals(thisIcon.getSrc(), "https://crspl.naturalis.nl/AtlantisWeb/App_Themes/Flexible/images/buttons/node-select-child.png", "Fout in 2.1.4");
		Assert.assertEquals(thisIcon.getTitle(), "Select", "Fout in 2.1.4");
		
		thisIcon = detailBeschrijvingenPage.getIconInfo("icon12");
		Assert.assertEquals(thisIcon.getSrc(), "https://crspl.naturalis.nl/AtlantisWeb/App_Themes/Flexible/images/buttons/delete7.gif", "Fout in 2.1.4");
		Assert.assertEquals(thisIcon.getTitle(), "Clear", "Fout in 2.1.4");

		
		// Gathering Site > Country
		this.detailBeschrijvingenPage.switchToGatheringSitesFrame();

//		Algemenere methode: moet nog uitgewerkt worden ...		
//		this.detailBeschrijvingenPage.buttonAddGatheringSite.click();
//		this.detailBeschrijvingenPage.switchToFrameContainingElementID("select21");

		thisIcon = detailBeschrijvingenPage.getIconInfo("icon13");
		Assert.assertEquals(thisIcon.getSrc(), "https://crspl.naturalis.nl/AtlantisWeb/App_Themes/Flexible/images/buttons/node-select-child.png", "Fout in 2.1.4");
		Assert.assertEquals(thisIcon.getTitle(), "Select", "Fout in 2.1.4");

		thisIcon = detailBeschrijvingenPage.getIconInfo("icon14");
		Assert.assertEquals(thisIcon.getSrc(), "https://crspl.naturalis.nl/AtlantisWeb/App_Themes/Flexible/images/buttons/delete7.gif", "Fout in 2.1.4");
		Assert.assertEquals(thisIcon.getTitle(), "Clear", "Fout in 2.1.4");
		
		this.detailBeschrijvingenPage.clickCloseButton();
	}

	/**
	 * 2.1.5
	 * 
	 * Voer de verplichte velden Current Collection name, Basis of record, Mount, en Preserved part in met waarden 
	 * die niet voorkomen in de thesaurus. 
	 * 
	 * 1. Verschijnt er een rood uitroepteken achter het veld als je naar het volgende veld gaat en het veld leeg 
	 * laat of een 'verkeerde' waarde invoert?  
	 * 
	 * 2. Blijft de verkeerde waarde staan?
	 * 
	 */

	@Test(priority=11, dependsOnMethods = { "selectSpecifiedForm" })
	public void checkIncorrectSubmissionValues01(){
		// Current Collection name
		
		// Cursor in veld, veld leeg laten en met tab verder
		
		// Verkeerde waarde
		EditIcon thisIcon = null;
		this.detailBeschrijvingenPage.enterValueToField("currentcollectionname", "TAB");
		thisIcon = detailBeschrijvingenPage.getIconInfo("icon15");
		Assert.assertEquals(thisIcon.getSrc(), "https://crspl.naturalis.nl/AtlantisWeb/App_Themes/Base/images/buttons/alert.gif", "Fout in 2.1.5");
		Assert.assertEquals(thisIcon.getTitle(), "Invalid (sub)collection name", "Fout in 2.1.5");		
	}
	
	@Test(priority=12, dependsOnMethods = { "selectSpecifiedForm" })
	public void checkIncorrectSubmissionValues02(){
		EditIcon thisIcon = null;
		this.detailBeschrijvingenPage.enterValueToField("currentcollectionname", "illegal-text");
		thisIcon = detailBeschrijvingenPage.getIconInfo("icon15");
		Assert.assertEquals(thisIcon.getSrc(), "https://crspl.naturalis.nl/AtlantisWeb/App_Themes/Base/images/buttons/alert.gif", "Fout in 2.1.5");
		Assert.assertEquals(thisIcon.getTitle(), "Invalid (sub)collection name", "Fout in 2.1.5");		

		
		// Basis of record
		
		
		// Mount
		
		
		// Preserved part
		
	}
	
	
	
	
	
	
	
	
	
	
	
	/*
	
	@Test(priority=5, dependsOnMethods = { "startPageTitle" })
	public void detailPageOpen() {
		detailBeschrijvingenPage = new DetailBeschrijvingenPage(driver);
		detailBeschrijvingenPage.setPageUrlQueryString(DetailBeschrijvingenPageQueryStringOk);
		driver.get(detailBeschrijvingenPage.getCompletePageURL());
		Assert.assertEquals(driver.getCurrentUrl(),detailBeschrijvingenPage.getCompletePageURL());
	}

	@Test(priority=6, dependsOnMethods = { "detailPageOpen" })
	public void detailPageTitle() {
		assertThat(driver.getTitle().trim(),containsString(detailBeschrijvingenPage.getPageTitle()));
	}
	
	@Test(priority=6, dependsOnMethods = { "detailPageOpen" })
	public void detailPageNumberOfResults() {
		assertThat(DetailBeschrijvingenPageExpectedResultNumber,equalTo(detailBeschrijvingenPage.getNumberOfResults()));
	}

	@Test(priority=7, dependsOnMethods = { "detailPageTitle" })
	public void detailPageOpenUnallowed() {
		detailBeschrijvingenPage.setPageUrlQueryString(DetailBeschrijvingenPageQueryStringNok);
		driver.get(detailBeschrijvingenPage.getCompletePageURL());
		// illegal ID, page automatically redirects to login (= HomePage)  
		Assert.assertNotEquals(driver.getCurrentUrl(),detailBeschrijvingenPage.getCompletePageURL());
	}

	@Test(priority=8, dependsOnMethods = { "detailPageOpenUnallowed" })
	public void unallowedPageOpen() {
		homePage = new HomePage(driver);
		assertThat(Constants.NO_AUTHORISATION_MESSAGE,equalTo(homePage.getAuthorizationFailureMessage()));
	}

	@Test(priority=9, dependsOnMethods = { "unallowedPageOpen" })
	public void unallowedPageMouseToLogOff() {
		assertThat(homePage.mouseToLogOffLink(),equalTo(true));
	}

	@Test(priority=10, dependsOnMethods = { "unallowedPageMouseToLogOff" })
	public void logOff() {
		homePage.clickLogOffLink();
		assertThat(driver.getCurrentUrl(),equalTo(LoggedOutUrl));
	}
		
	private void checkStartPageMenu(MenuItems menuItem) {
		WebElement UppMenu = startPage.getUppMenu();
		Actions action = new Actions(driver);
			
		Integer n=0;
		for (String item : menuItem.getMenuItems()) {
			if (n++ < menuItem.getMenuItems().size()-1) {
				WebElement element = UppMenu.findElement(By.linkText(item));
				action.moveToElement(element).perform();
			}
		}
		
		String lastItem = menuItem.getMenuItems().get(menuItem.getMenuItems().size()-1);
		WebElement LastItem = UppMenu.findElement(By.linkText(lastItem));
		action.moveToElement(LastItem).perform();
		
		WebElement LastItemParentElement = LastItem.findElement(By.xpath("./../../.."));
		List<WebElement> LastItemAndSiblings = LastItemParentElement.findElements(By.tagName("tr"));
		
		Assert.assertEquals(Integer.valueOf(LastItemAndSiblings.size()),menuItem.getMaxLastItemAndSiblings(),String.join(" > ", menuItem.getMenuItems()));
	}
	*/

	private static void initializeTestParameters() {
        addMenu = new MenuItems(Arrays.asList("Employee","Add","Specimen","Vertebrates"),-1);
        formListLabels = Arrays.asList("Collection Vertebrates Specimen","Digistreet Vertebrates Specimen");
	}


}