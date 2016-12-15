package nl.naturalis.selenium.crs.tests;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import nl.naturalis.selenium.crs.fragments.Link;
import nl.naturalis.selenium.crs.fragments.MenuItems;
import nl.naturalis.selenium.crs.fragments.TestCase;
import nl.naturalis.selenium.crs.configuration.*;
import nl.naturalis.selenium.crs.pages.*;
import nl.naturalis.selenium.crs.utils.MissingConfigurationException;
import nl.naturalis.selenium.crs.utils.Report;
import nl.naturalis.selenium.crs.utils.ToolKit;

public class Test12 extends AbstractTest {

	private static String projectID = "CRS";
	private static String testID = "Test 12";
	
	private static List<MenuItems> startPageMenuItemsCollection = new ArrayList<MenuItems>();
	private static String detailBeschrijvingenPageQueryString_01;
	private static HomePage homePage;
	private static StartPage startPage;
	private static DetailBeschrijvingenPage detailBeschrijvingenPage;
	private static List<TestCase> testCases = new ArrayList<TestCase>();
	private TestCase currentTestCase;

	@BeforeClass
	private static void initalize() throws MissingConfigurationException, SQLException {
		initializeDatabase();
		initializeConfiguration(projectID);
		initializeTestParameters();
		initializeDriver();
		initializeLogging();
		driver.get(Configuration.getStartUrl());
		Report.LogTestStart();
	}

	@AfterClass
	private static void cleanUp() throws SQLException {
		//tearDown();
		Report.LogTestEnd();
	}

	@Test(priority=1)
	public void homePageOpen() {
		homePage = new HomePage(driver);
		Assert.assertEquals(driver.getCurrentUrl(),homePage.getPageURL(),"1.1.1. home page URL");
	}

	@Test(priority=3, dependsOnMethods = { "homePageOpen" })
	public void homePageDoLogin() {
		startPage = homePage.doLogin(Configuration.getUsername(), Configuration.getPassword());
		Assert.assertEquals(driver.getCurrentUrl(),startPage.getPageURL(),"URL of home page");
	}

	@Test(priority=6, dependsOnMethods = { "homePageDoLogin" })
	public void runTestCases() {

		detailBeschrijvingenPage = new DetailBeschrijvingenPage(driver);
		
		for(TestCase testCase : testCases) {

			setCurrentTestCase(testCase);
			
			detailBeschrijvingenPage.setPageUrlQueryString(getCurrentTestCase().getQuery());
			driver.get(detailBeschrijvingenPage.getCompletePageURL());
			detailBeschrijvingenPage = detailBeschrijvingenPage.selectFormulierByName(getCurrentTestCase().getPage());

			contextTest();
			newSpecimenDNATests();
			newSpecimenOtherTests();
			molecularStorageUnitTests();
			geneiousOIATests();
		}
	}

	
	public void contextTest() {
		if (!getCurrentTestCase().hasTest("Context")) return;
		testContextExists();
		testContextMatchRelations();
		testContextFunctioningLinks();
	}
	
	private void testContextExists() {
		//Komt het context scherm voor in het formulier
		Assert.assertTrue(detailBeschrijvingenPage.getContextDisplayIsDisplayed(),"context.1 - " + getCurrentTestCase().getIdString());
	}

	private void testContextMatchRelations() {
		//Komen de relaties in het formulier overeen met wat er in het context scherm staat. Let ook specifiek op dat de relatienamen "current" en "usual" (storage unit / storage location) niet meer voorkomen
		
		List<Link> links = detailBeschrijvingenPage.getContextDisplayLinks();
		detailBeschrijvingenPage.clickRelationsTabButton();
	}
	
	

	private void testContextFunctioningLinks() {
		//Werken de links in het context scherm naar de relaties toe.
		System.out.println(detailBeschrijvingenPage.getContextDisplayObjectType());
		
	}

	
	
	
	
	

	public void newSpecimenDNATests() {
		if (!getCurrentTestCase().hasTest("New specimen DNA")) return;
		testNewSpecimenDNAPrefix();
		testNewSpecimenDNADisabledFields01();
		testNewSpecimenDNADisabledFields02();
		testNewSpecimenDNAExclusiveNCBN();
		testNewSpecimenDNASaves();
	}

	private void testNewSpecimenDNAPrefix() {
	}

	private void testNewSpecimenDNADisabledFields01() {
	}

	private void testNewSpecimenDNADisabledFields02() {
	}

	private void testNewSpecimenDNAExclusiveNCBN() {
	}

	private void testNewSpecimenDNASaves() {
	}

	
	

	public void newSpecimenOtherTests() {
		if (!getCurrentTestCase().hasTest("New specimen other")) return;
		testNewSpecimenOtherPrefix();
		testNewSpecimenOtherDisabledFields();
		testNewSpecimenOtherExclusiveNCBN();
		testNewSpecimenOtherSaves();
	}

	private void testNewSpecimenOtherPrefix() {
	}

	private void testNewSpecimenOtherDisabledFields() {
	}

	private void testNewSpecimenOtherExclusiveNCBN() {
	}

	private void testNewSpecimenOtherSaves() {
	}


	public void molecularStorageUnitTests() {
		if (!getCurrentTestCase().hasTest("Molecular storage unit")) return;
		testMolecularStorageUnitPrefixZeroes01();
		testMolecularStorageUnitPrefixZeroes02();
	}
	
	private void testMolecularStorageUnitPrefixZeroes01() {
	}

	private void testMolecularStorageUnitPrefixZeroes02() {
	}


	public void geneiousOIATests() {
		if (!getCurrentTestCase().hasTest("Geneious OIA")) return;
		testGeneiousOIAExtractPlates();
		testGeneiousOIADNAExtracts();
		testGeneiousOIASpecimen();
	}
	
	private void testGeneiousOIAExtractPlates() {
	}

	private void testGeneiousOIADNAExtracts() {
	}

	private void testGeneiousOIASpecimen() {
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private void setCurrentTestCase(TestCase testCase) {
		this.currentTestCase=testCase;
	}

	private TestCase getCurrentTestCase() {
		return this.currentTestCase;
	}

	private static void initializeTestParameters() {
	
		/*

		TestCase tmp = new TestCase("Entomology","Collection Entomological Specimen","xmlbeschrijvingid=20250966");
		tmp.addTest("Context");
		tmp.addTest("New specimen other");
		testCases.add(tmp);

		tmp = new TestCase("Entomology","Digistreet Entomological Specimen","xmlbeschrijvingid=20250966");
		tmp.addTest("Context");
		tmp.addTest("New specimen other");
		testCases.add(tmp);

		tmp = new TestCase("Entomology","General Entomological Specimen","xmlbeschrijvingid=20250966");
		tmp.addTest("Context");
		tmp.addTest("New specimen other");
		testCases.add(tmp);

		tmp = new TestCase("Entomology Storage unit","Digistreet 30M Entomological Storage unit","xmlbeschrijvingid=20250966");
		tmp.addTest("Context");
		tmp.addTest("New specimen other");
		testCases.add(tmp);

		tmp = new TestCase("Arts","FES","xmlbeschrijvingid=20250966");
		tmp.addTest("Context");
		tmp.addTest("New specimen other");
		testCases.add(tmp);

		tmp = new TestCase("Arts","General Specimen","xmlbeschrijvingid=20250966");
		tmp.addTest("Context");
		tmp.addTest("New specimen other");
		testCases.add(tmp);

		tmp = new TestCase("Arts","Zoological Specimen","xmlbeschrijvingid=20250966");
		tmp.addTest("Context");
		tmp.addTest("New specimen other");
		testCases.add(tmp);

		tmp = new TestCase("Arts Storage unit","Dynamisch","xmlbeschrijvingid=20250966");
		tmp.addTest("Context");
		tmp.addTest("New specimen other");
		testCases.add(tmp);

		tmp = new TestCase("Arts Storage unit","test","xmlbeschrijvingid=20250966");
		tmp.addTest("Context");
		tmp.addTest("New specimen other");
		testCases.add(tmp);

		tmp = new TestCase("Invertebrates","Collection Invertebrates Specimen","xmlbeschrijvingid=20250966");
		tmp.addTest("Context");
		tmp.addTest("New specimen other");
		testCases.add(tmp);

		tmp = new TestCase("Invertebrates","Digistreet Invertebrates Specimen","xmlbeschrijvingid=20250966");
		tmp.addTest("Context");
		tmp.addTest("New specimen other");
		testCases.add(tmp);

		tmp = new TestCase("Invertebrates","General Invertebrates Specimen","xmlbeschrijvingid=20250966");
		tmp.addTest("Context");
		tmp.addTest("New specimen other");
		testCases.add(tmp);

		tmp = new TestCase("Invertebrates Storage unit","Digistreet 30M Invertebrates Storage unit","xmlbeschrijvingid=20250966");
		tmp.addTest("Context");
		tmp.addTest("New specimen other");
		testCases.add(tmp);

		tmp = new TestCase("Mineralogy and Petrology","Collection MinandPetro Specimen","xmlbeschrijvingid=20250966");
		tmp.addTest("Context");
		tmp.addTest("New specimen other");
		testCases.add(tmp);

		tmp = new TestCase("Mineralogy and Petrology","Digistreet MinandPetro Specimen","xmlbeschrijvingid=20250966");
		tmp.addTest("Context");
		tmp.addTest("New specimen other");
		testCases.add(tmp);

		tmp = new TestCase("Mineralogy and Petrology","General MinandPetro Specimen","xmlbeschrijvingid=20250966");
		tmp.addTest("Context");
		tmp.addTest("New specimen other");
		testCases.add(tmp);

		tmp = new TestCase("Mineralogy and Petrology Storage unit","Digistreet 30M MinandPetro Storage unit","xmlbeschrijvingid=20250966");
		tmp.addTest("Context");
		tmp.addTest("New specimen other");
		testCases.add(tmp);

		tmp = new TestCase("Paleontology","Collection Paleontological Specimen","xmlbeschrijvingid=20250966");
		tmp.addTest("Context");
		tmp.addTest("New specimen other");
		testCases.add(tmp);

		tmp = new TestCase("Paleontology","Digistreet Paleontological Specimen","xmlbeschrijvingid=20250966");
		tmp.addTest("Context");
		tmp.addTest("New specimen other");
		testCases.add(tmp);

		tmp = new TestCase("Paleontology","General Paleontological Specimen","xmlbeschrijvingid=20250966");
		tmp.addTest("Context");
		tmp.addTest("New specimen other");
		testCases.add(tmp);

		tmp = new TestCase("Paleontology","Paleo Specimen Compact","xmlbeschrijvingid=20250966");
		tmp.addTest("Context");
		tmp.addTest("New specimen other");
		testCases.add(tmp);

		tmp = new TestCase("Paleontology Storage unit","Digistreet 30M Paleontological Storage unit","xmlbeschrijvingid=20250966");
		tmp.addTest("Context");
		tmp.addTest("New specimen other");
		testCases.add(tmp);

		tmp = new TestCase("Sounds","FES","xmlbeschrijvingid=20250966");
		tmp.addTest("Context");
		tmp.addTest("New specimen other");
		testCases.add(tmp);

		tmp = new TestCase("Sounds","General Specimen","xmlbeschrijvingid=20250966");
		tmp.addTest("Context");
		tmp.addTest("New specimen other");
		testCases.add(tmp);

		tmp = new TestCase("Sounds","Zoological Specimen","xmlbeschrijvingid=20250966");
		tmp.addTest("Context");
		tmp.addTest("New specimen other");
		testCases.add(tmp);

		tmp = new TestCase("Sounds Storage unit","Dynamisch","xmlbeschrijvingid=20250966");
		tmp.addTest("Context");
		tmp.addTest("New specimen other");
		testCases.add(tmp);
		*/

		TestCase tmp = new TestCase("Vertebrates","Collection Vertebrates Specimen","xmlbeschrijvingid=182568105","Collection Vertebrates Specimen");
		tmp.addTest("Context");
		tmp.addTest("New specimen other");
		testCases.add(tmp);

		/*
		tmp = new TestCase("Vertebrates","Digistreet Vertebrates Specimen","xmlbeschrijvingid=182568105","Digistreet Vertebrates Specimen");
		tmp.addTest("Context");
		tmp.addTest("New specimen other");
		testCases.add(tmp);

		tmp = new TestCase("Vertebrates","General Vertebrates Specimen","xmlbeschrijvingid=20250966");
		tmp.addTest("Context");
		tmp.addTest("New specimen other");
		testCases.add(tmp);

		tmp = new TestCase("Vertebrates Storage unit","Digistreet 30M Vertebrates Storage unit","xmlbeschrijvingid=20250966");
		tmp.addTest("Context");
		tmp.addTest("New specimen other");
		testCases.add(tmp);

		tmp = new TestCase("DNA Lab","Collection Entomological Specimen","xmlbeschrijvingid=20250966");
		tmp.addTest("Context");
		tmp.addTest("New specimen DNA");
		tmp.addTest("Geneious OIA");
		testCases.add(tmp);

		tmp = new TestCase("DNA Lab","Digistreet Entomological Specimen","xmlbeschrijvingid=20250966");
		tmp.addTest("Context");
		tmp.addTest("New specimen DNA");
		tmp.addTest("Geneious OIA");
		testCases.add(tmp);

		tmp = new TestCase("DNA Lab","Collection Vertebrates Specimen","xmlbeschrijvingid=20250966");
		tmp.addTest("Context");
		tmp.addTest("New specimen DNA");
		tmp.addTest("Geneious OIA");
		testCases.add(tmp);

		tmp = new TestCase("DNA Lab","DNA Lab Specimen","xmlbeschrijvingid=20250966");
		tmp.addTest("Context");
		tmp.addTest("New specimen DNA");
		tmp.addTest("Geneious OIA");
		testCases.add(tmp);

		tmp = new TestCase("Molecular Storage unit","Collection Molecular Storage unit","xmlbeschrijvingid=20250966");
		tmp.addTest("Context");
		tmp.addTest("Molecular storage unit");
		testCases.add(tmp);
		*/
	
		
	}
}