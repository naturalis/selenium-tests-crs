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
	
	/**
	 * 2.1.3
	 * 
	 * Test Context Scherm
	 * 
	 */
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
		Test02.detailBeschrijvingenPage.switchToGatheringSitesFrame();

//		Algemenere methode: moet nog uitgewerkt worden ...		
//		this.detailBeschrijvingenPage.buttonAddGatheringSite.click();
//		this.detailBeschrijvingenPage.switchToFrameContainingElementID("select21");

		thisIcon = detailBeschrijvingenPage.getIconInfo("icon13");
		Assert.assertEquals(thisIcon.getSrc(), "https://crspl.naturalis.nl/AtlantisWeb/App_Themes/Flexible/images/buttons/node-select-child.png", "Fout in 2.1.4");
		Assert.assertEquals(thisIcon.getTitle(), "Select", "Fout in 2.1.4");

		thisIcon = detailBeschrijvingenPage.getIconInfo("icon14");
		Assert.assertEquals(thisIcon.getSrc(), "https://crspl.naturalis.nl/AtlantisWeb/App_Themes/Flexible/images/buttons/delete7.gif", "Fout in 2.1.4");
		Assert.assertEquals(thisIcon.getTitle(), "Clear", "Fout in 2.1.4");
		
		Test02.detailBeschrijvingenPage.clickCloseButton();
	}

	/**
	 * 2.1.5
	 * 
	 * Voer de verplichte velden Current Collection name, Basis of record, Mount, en Preserved part in met waarden 
	 * die niet voorkomen in de thesaurus. 
	 * 
	 * A. Verschijnt er een rood uitroepteken achter het veld als je naar het volgende veld gaat, en het veld leeg laat of 
	 * B. een 'verkeerde' waarde invoert?  
	 * C. Blijft de verkeerde waarde staan?
	 * 
	 */
	// 1. Current Collection name
	@Test(priority=11, dependsOnMethods = { "checkThesaurusAndBinIcons" })
	public void checkIncorrectSubmissionValueTest1A(){
		// Cursor in veld, veld leeg laten en met tab verder
		EditIcon thisIcon = null;
		Test02.detailBeschrijvingenPage.enterValueToField("currentcollectionname", "TAB");
		thisIcon = detailBeschrijvingenPage.getIconInfo("icon15");
		Assert.assertEquals(thisIcon.getSrc(), "https://crspl.naturalis.nl/AtlantisWeb/App_Themes/Base/images/buttons/alert.gif", "Fout in 2.1.5");
		Assert.assertEquals(thisIcon.getTitle(), "Invalid (sub)collection name", "Fout in 2.1.5: TAB naar volgende veld geeft geen foutmelding!");		
	}
	
	@Test(priority=12, dependsOnMethods = { "checkThesaurusAndBinIcons" })
	public void checkIncorrectSubmissionValueTest1B(){
		// Verkeerde waarde
		EditIcon thisIcon = null;
		Test02.detailBeschrijvingenPage.enterValueToField("currentcollectionname", "illegal-text");
		thisIcon = detailBeschrijvingenPage.getIconInfo("icon15");
		Assert.assertEquals(thisIcon.getSrc(), "https://crspl.naturalis.nl/AtlantisWeb/App_Themes/Base/images/buttons/alert.gif", "Fout in 2.1.5");
		Assert.assertEquals(thisIcon.getTitle(), "Invalid (sub)collection name", "Fout in 2.1.5");
	}

	@Test(priority=13, dependsOnMethods = { "checkIncorrectSubmissionValueTest1B" })
	public void checkIncorrectSubmissionValueTest1C(){
		// Blijft de verkeerde waarde staan?
		Assert.assertEquals(Test02.detailBeschrijvingenPage.readValueFromField("currentcollectionname"), "illegal-text", "Fout in 2.1.5");
		Test02.detailBeschrijvingenPage.enterValueToField("currentcollectionname", "CLEAR");
	}
	
	// 2. Basis of record
	@Test(priority=14, dependsOnMethods = { "checkIncorrectSubmissionValueTest1C" })
	public void checkIncorrectSubmissionValueTest2A(){
		Test02.detailBeschrijvingenPage.switchToFrame_1();
		// Cursor in veld, veld leeg laten en met tab verder
		EditIcon thisIcon = null;
		Test02.detailBeschrijvingenPage.enterValueToField("basisofrecord", "TAB");
		thisIcon = detailBeschrijvingenPage.getIconInfo("icon16");
		Assert.assertEquals(thisIcon.getSrc(), "https://crspl.naturalis.nl/AtlantisWeb/App_Themes/Base/images/buttons/alert.gif", "Fout in 2.1.5");
		Assert.assertEquals(thisIcon.getTitle(), "Invalid basis of record", "Fout in 2.1.5");		
	}
	
	@Test(priority=15, dependsOnMethods = { "checkIncorrectSubmissionValueTest1C" })
	public void checkIncorrectSubmissionValueTest2B(){
		// Verkeerde waarde
		EditIcon thisIcon = null;
		Test02.detailBeschrijvingenPage.enterValueToField("basisofrecord", "illegal-text");
		thisIcon = detailBeschrijvingenPage.getIconInfo("icon16");
		Assert.assertEquals(thisIcon.getSrc(), "https://crspl.naturalis.nl/AtlantisWeb/App_Themes/Base/images/buttons/alert.gif", "Fout in 2.1.5");
		Assert.assertEquals(thisIcon.getTitle(), "May only contain thesaurus value!", "Fout in 2.1.5");
	}		

	@Test(priority=16, dependsOnMethods = { "checkIncorrectSubmissionValueTest1C" })
	public void checkIncorrectSubmissionValueTest2C(){
		// Blijft de verkeerde waarde staan?
		Assert.assertEquals(Test02.detailBeschrijvingenPage.readValueFromField("basisofrecord"), "illegal-text", "Fout in 2.1.5");
		Test02.detailBeschrijvingenPage.enterValueToField("basisofrecord", "CLEAR");
	}

	// 3. Mount
	@Test(priority=17, dependsOnMethods = { "checkIncorrectSubmissionValueTest2C" })
	public void checkIncorrectSubmissionValueTest3A(){
		Test02.detailBeschrijvingenPage.switchToFrame_1();
		// Cursor in veld, veld leeg laten en met tab verder
		EditIcon thisIcon = null;
		Test02.detailBeschrijvingenPage.enterValueToField("mount", "TAB");
		thisIcon = detailBeschrijvingenPage.getIconInfo("icon17");
		Assert.assertEquals(thisIcon.getSrc(), "https://crspl.naturalis.nl/AtlantisWeb/App_Themes/Base/images/buttons/alert.gif", "Fout in 2.1.5");
		Assert.assertEquals(thisIcon.getTitle(), "Invalid mount", "Fout in 2.1.5: TAB naar volgende veld geeft geen foutmelding!");		
	}
	
	@Test(priority=18, dependsOnMethods = { "checkIncorrectSubmissionValueTest2C" })
	public void checkIncorrectSubmissionValueTest3B(){
		// Verkeerde waarde
		EditIcon thisIcon = null;
		Test02.detailBeschrijvingenPage.enterValueToField("mount", "illegal-text");
		thisIcon = detailBeschrijvingenPage.getIconInfo("icon17");
		Assert.assertEquals(thisIcon.getSrc(), "https://crspl.naturalis.nl/AtlantisWeb/App_Themes/Base/images/buttons/alert.gif", "Fout in 2.1.5");
		Assert.assertEquals(thisIcon.getTitle(), "May only contain thesaurus value!", "Fout in 2.1.5");
	}		

	@Test(priority=19, dependsOnMethods = { "checkIncorrectSubmissionValueTest2C" })
	public void checkIncorrectSubmissionValueTest3C(){
		// Blijft de verkeerde waarde staan?
		Assert.assertEquals(Test02.detailBeschrijvingenPage.readValueFromField("mount"), "illegal-text", "Fout in 2.1.5");
		Test02.detailBeschrijvingenPage.enterValueToField("mount", "CLEAR");
	}
		
	
	// 4. Preserved part
	@Test(priority=20, dependsOnMethods = { "checkIncorrectSubmissionValueTest3C" })
	public void checkIncorrectSubmissionValueTest4A(){
		Test02.detailBeschrijvingenPage.switchToFrame_1();
		// Cursor in veld, veld leeg laten en met tab verder
		EditIcon thisIcon = null;
		Test02.detailBeschrijvingenPage.enterValueToField("preservedpart", "TAB");
		thisIcon = detailBeschrijvingenPage.getIconInfo("icon18");
		Assert.assertEquals(thisIcon.getSrc(), "https://crspl.naturalis.nl/AtlantisWeb/App_Themes/Base/images/buttons/alert.gif", "Fout in 2.1.5");
		Assert.assertEquals(thisIcon.getTitle(), "Invalid preserved part", "Fout in 2.1.5: TAB naar volgende veld geeft geen foutmelding!");		
	}
	
	@Test(priority=21, dependsOnMethods = { "checkIncorrectSubmissionValueTest3C" })
	public void checkIncorrectSubmissionValueTest4B(){
		// Verkeerde waarde
		EditIcon thisIcon = null;
		Test02.detailBeschrijvingenPage.enterValueToField("preservedpart", "illegal-text");
		thisIcon = detailBeschrijvingenPage.getIconInfo("icon18");
		Assert.assertEquals(thisIcon.getSrc(), "https://crspl.naturalis.nl/AtlantisWeb/App_Themes/Base/images/buttons/alert.gif", "Fout in 2.1.5");
		Assert.assertEquals(thisIcon.getTitle(), "May only contain thesaurus value!", "Fout in 2.1.5");
	}		

	@Test(priority=22, dependsOnMethods = { "checkIncorrectSubmissionValueTest3C" })
	public void checkIncorrectSubmissionValueTest4C(){
		// Blijft de verkeerde waarde staan?
		Assert.assertEquals(Test02.detailBeschrijvingenPage.readValueFromField("preservedpart"), "illegal-text", "Fout in 2.1.5");
		Test02.detailBeschrijvingenPage.enterValueToField("preservedpart", "CLEAR");
	}

	/**
	 * 2.1.6
	 * 
	 * Voer de verplichte velden Current Collection name, Basis of record, Mount en Preserved part 
	 * in met een waarde die voorkomt in de bijbehorende thesaurus. 
	 * 
	 * 1. Verschijnt er een autosuggest onder het veld als er een waarde uit de bijbehorende thesauruslijst 
	 * wordt getypt? en 
	 * 2. verschijnt er achter het prullenbakje de gekozen thesaurus term?
	 * 
	 */
	
	@Test(priority=23, dependsOnMethods = { "checkIncorrectSubmissionValueTest3C" })
	public void checkTextEntryCurrentCollectionName(){
		// Pisces(Vertebrates)
		String entryText = "p";
		String collectionName = "Pisces";
		Test02.detailBeschrijvingenPage.setCurrentCollectionName(entryText, collectionName);
		Assert.assertEquals(Test02.detailBeschrijvingenPage.getCurrentCollectionNameTlink(), collectionName, "Fout in 2.1.6: Current Collection name.");
	}

	@Test(priority=24, dependsOnMethods = { "checkIncorrectSubmissionValueTest3C" })
	public void checkTextEntryBasisOfRecord(){
		// Basis of record
		String entryText = "v";
		String specimenType = "VirtualSpecimen";
		Test02.detailBeschrijvingenPage.setBasisOfRecord(entryText, specimenType);
		Assert.assertEquals(Test02.detailBeschrijvingenPage.getBasisOfRecordTlink(), specimenType, "Fout in 2.1.6: Basis of Record.");
	}
	
	@Test(priority=25, dependsOnMethods = { "checkIncorrectSubmissionValueTest3C" })
	public void checkTextEntryMount(){
		// Mount
		String entryText = "n";
		String specimenType = "not applicable";
		Test02.detailBeschrijvingenPage.setMount(entryText, specimenType);
		Assert.assertEquals(Test02.detailBeschrijvingenPage.getMountTlink(), specimenType, "Fout in 2.1.6: Mount");
	}

	@Test(priority=26, dependsOnMethods = { "checkIncorrectSubmissionValueTest3C" })
	public void checkTextEntryPreservedPart(){
		// Preserved part
		String entryText = "n";
		String specimenType = "Not applicable";
		Test02.detailBeschrijvingenPage.setPreservedPart(entryText, specimenType);
		Assert.assertEquals(Test02.detailBeschrijvingenPage.getPreservedPartTlink(), specimenType, "Fout in 2.1.6: Preserved part.");
	}


	
	

	private static void initializeTestParameters() {
        addMenu = new MenuItems(Arrays.asList("Employee","Add","Specimen","Vertebrates"),-1);
        formListLabels = Arrays.asList("Collection Vertebrates Specimen","Digistreet Vertebrates Specimen");
	}


}