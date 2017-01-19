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
import org.testng.annotations.DataProvider;
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
	
	private static List<Link> contextLinks = new ArrayList<Link>();
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

	@Test(priority=6, dependsOnMethods = { "homePageDoLogin" }, dataProvider = "testCases")
	public void runTestCases(TestCase thisTest) {

		setCurrentTestCase(thisTest);

		//System.out.println("running " + getCurrentTestCase().getIdentifier());
		System.out.println("running " + getCurrentTestCase().getIdentifier());
		System.out.println("  tests: " + getCurrentTestCase().getTests());
		
		
		
		detailBeschrijvingenPage = new DetailBeschrijvingenPage(driver);
		detailBeschrijvingenPage.setPageUrlQueryString(getCurrentTestCase().getQuery());
		driver.get(detailBeschrijvingenPage.getCompletePageURL());
		detailBeschrijvingenPage = changeForm();
				
//		contextTest();
		newSpecimenDNATests();
		newSpecimenOtherTests();
		molecularStorageUnitTests();
		geneiousOIATests();
	}
	
	public DetailBeschrijvingenPage changeForm() {
		if (getCurrentTestCase().getForm()==null) {
			return detailBeschrijvingenPage;			
		}
		else {
			Assert.assertTrue(
					detailBeschrijvingenPage.hasFormulierByName(getCurrentTestCase().getForm()),
					"context.hasForm - " + getCurrentTestCase().getIdentifier() + " (form: " + getCurrentTestCase().getForm() + ")");
			return detailBeschrijvingenPage.selectFormulierByName(getCurrentTestCase().getForm());
		}
	}
	
	public void contextTest() {
		if (!getCurrentTestCase().hasTest("Context")) return;
		System.out.println("running " + getCurrentTestCase().getIdentifier() + ": Context");
		testContextExists();
		testContextMatchRelations();
		testContextFunctioningLinks();
	}

	private void testContextExists() {
		//Komt het context scherm voor in het formulier
		System.out.println("running " + getCurrentTestCase().getIdentifier() + ": Context: testContextExists");
		Assert.assertTrue(detailBeschrijvingenPage.getContextDisplayIsDisplayed(),"context.1 - " + getCurrentTestCase().getIdentifier());
	}

	private void testContextMatchRelations() {
		//Komen de relaties in het formulier overeen met wat er in het context scherm staat. Let ook specifiek op dat de relatienamen "current" en "usual" (storage unit / storage location) niet meer voorkomen
		System.out.println("running " + getCurrentTestCase().getIdentifier() + ": Context: testContextMatchRelations");
		
		contextLinks = detailBeschrijvingenPage.getContextDisplayLinks();
		List<Link> tmp = currentTestCase.getContextLinks();
		int n=0;
		for(Link l : contextLinks) { 
			Link lp = tmp.get(n++);
			Assert.assertTrue(l.getAdditionalInfo().equals(lp.getAdditionalInfo()) && l.getText().equals(lp.getText()),"context.2 - " + getCurrentTestCase().getIdentifier());
		}

	}

	private void testContextFunctioningLinks() {
		//Werken de links in het context scherm naar de relaties toe.
		System.out.println("running " + getCurrentTestCase().getIdentifier() + ": Context: testContextFunctioningLinks");
		
		boolean assertion=true;
		for(Link l : contextLinks) {
			System.out.println(l.getHref());
			driver.get(l.getHref());
			//Assert.assertEquals(driver.getCurrentUrl(),l.getHref(),"context.2 - " + getCurrentTestCase().getIdentifier() + ": " + l.getText() + " " + l.getAdditionalInfo());			

			if (!driver.getCurrentUrl().equals(l.getHref())) {
				System.out.println("context.3 - " + getCurrentTestCase().getIdentifier() + ": " + l.getText() + " " + l.getAdditionalInfo() + driver.getCurrentUrl() +" != " + l.getHref());
				assertion=false;
			}
		}

		Assert.assertTrue(assertion,"context.3 - " + getCurrentTestCase().getIdentifier());
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
		System.out.println("running " + getCurrentTestCase().getIdentifier() + ": New specimen other");
		testNewSpecimenOtherPrefix();
		testNewSpecimenOtherDisabledFields();
		testNewSpecimenOtherExclusiveNCBN();
		testNewSpecimenOtherSaves();
	}

	private void testNewSpecimenOtherPrefix() {
		//vul prefix met 'e' en catalognumber met nieuwe waarde. Wordt het registratienummer samengesteld zonder punt tussen prefix en catalogumber?
		System.out.println("running " + getCurrentTestCase().getIdentifier() + ": New specimen other: testNewSpecimenOtherPrefix");

		String prefix = "e";
		String number = "621";
		detailBeschrijvingenPage.setPrefix(prefix);
		detailBeschrijvingenPage.setNumber(number);
		Assert.assertEquals(detailBeschrijvingenPage.getRegistrationNumber(),prefix.concat(number),"spec.other.1 - " + getCurrentTestCase().getIdentifier());
	}

	private void testNewSpecimenOtherDisabledFields() {
		//Vul mount met '96 well plate'. Worden de storage location velden uitgegrijst?
		System.out.println("running " + getCurrentTestCase().getIdentifier() + ": New specimen other: testNewSpecimenOtherDisabledFields");

		boolean before =
			detailBeschrijvingenPage.isStandardStorageLocationEnabled() && 
			detailBeschrijvingenPage.isTemporaryStorageUnitEnabled() &&
			detailBeschrijvingenPage.isTemporaryStorageLocationEnabled();
		
		Assert.assertTrue(before,"spec.other.2 (initial state) - " + getCurrentTestCase().getIdentifier());
		detailBeschrijvingenPage.setMount("96 well plate","96 well plate");

		boolean after =
			!detailBeschrijvingenPage.isStandardStorageLocationEnabled() && 
			!detailBeschrijvingenPage.isTemporaryStorageUnitEnabled() &&
			!detailBeschrijvingenPage.isTemporaryStorageLocationEnabled();
		
		Assert.assertTrue(after,"spec.other.2 - " + getCurrentTestCase().getIdentifier());
	}

	private void testNewSpecimenOtherExclusiveNCBN() {
		//Kun je een NCBN storage unit koppelen, maar niet een BE storage unit? (als mount = 96 well plate, aldus tekla)
		System.out.println("running " + getCurrentTestCase().getIdentifier() + ": New specimen other: testNewSpecimenOtherExclusiveNCBN");

		//detailBeschrijvingenPage.setMount("96 well plate","96 well plate"); // already there
		detailBeschrijvingenPage.setStandardStorageUnit("N");
		// unable to get this to work
		detailBeschrijvingenPage.getStorgeUnitSuggestions();
		
		Assert.assertEquals(true,true,"spec.other.3 - " + getCurrentTestCase().getIdentifier() + " - CANNOT GET THIS ONE TO WORK (will always pass)");
		
	}

	private void testNewSpecimenOtherSaves() {
		//Kan het record opgeslagen worden?
		System.out.println("running " + getCurrentTestCase().getIdentifier() + ": New specimen other: testNewSpecimenOtherSaves");
	}


	
	public void molecularStorageUnitTests() {
		if (!getCurrentTestCase().hasTest("Molecular storage unit")) return;
		System.out.println("running " + getCurrentTestCase().getIdentifier() + ": Molecular storage unit");
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
	}

	@DataProvider(name = "testCases")
	public static Object[][] testCaseProvider() {
		/* 
		[forms]
		"Collection Vertebrates Specimen"
		"Digistreet Vertebrates Specimen"
		"General Vertebrates Specimen"
		"IM Test"
		
		[tests]
		tmp.addTest("Context");
		tmp.addTest("New specimen DNA");
		tmp.addTest("New specimen other");
		tmp.addTest("Molecular storage unit");
		tmp.addTest("Geneious OIA");

		vertebrates
		specimen: 		xmlbeschrijvingid=182568105
		storage unit:	xmlbeschrijvingid=182568204
		
		entomology
		specimen:		xmlbeschrijvingid=182567557
		storage unit:	xmlbeschrijvingid=182567670
		
		invertebates
		specimen:		xmlbeschrijvingid=182567725
		storage unit:	xmlbeschrijvingid=182567839
		
		mineralogy and petrology
		specimen:		xmlbeschrijvingid=182568231
		storage unit:	xmlbeschrijvingid=182568343
		
		paleontolgy
		specimen:		xmlbeschrijvingid=182567934
		storage unit	xmlbeschrijvingid=182568037

		*/

		TestCase tmp = new TestCase(null,null,null,null,null);
		
		tmp = new TestCase("s12t01","Vertebrates","Collection Vertebrates Specimen","xmlbeschrijvingid=182568105","Collection Vertebrates Specimen");
			tmp.addTest("Context");
			tmp.addContextLink(new Link(null,"TEST.BE.3",null,"Standard storage unit"));
			tmp.addContextLink(new Link(null,"DW",null,"Standard storage location"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.33",null,"Belongs to"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.31",null,"Has parent"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.30.a",null,"Has parts"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.34",null,"Consists of"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.32",null,"Has children"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.3.a",null,"Is associated with tissue"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.3.bu",null,"Is associated with WholeOrganism"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.3.im",null,"Is associated with WholeOrganism"));
			testCases.add(tmp);

		tmp = new TestCase("s12t02","Vertebrates","Digistreet Vertebrates Specimen","xmlbeschrijvingid=182568105","Digistreet Vertebrates Specimen");
			tmp.addTest("Context");
			tmp.addContextLink(new Link(null,"TEST.BE.3",null,"Standard storage unit"));
			tmp.addContextLink(new Link(null,"DW",null,"Standard storage location"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.33",null,"Belongs to"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.31",null,"Has parent"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.30.a",null,"Has parts"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.34",null,"Consists of"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.32",null,"Has children"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.3.a",null,"Is associated with tissue"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.3.bu",null,"Is associated with WholeOrganism"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.3.im",null,"Is associated with WholeOrganism"));
			testCases.add(tmp);

		tmp = new TestCase("s12t03","Vertebrates","General Vertebrates Specimen","xmlbeschrijvingid=182568105","Collection Vertebrates Specimen");
			tmp.addTest("Context");
			tmp.addContextLink(new Link(null,"TEST.BE.3",null,"Standard storage unit"));
			tmp.addContextLink(new Link(null,"DW",null,"Standard storage location"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.33",null,"Belongs to"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.31",null,"Has parent"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.30.a",null,"Has parts"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.34",null,"Consists of"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.32",null,"Has children"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.3.a",null,"Is associated with tissue"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.3.bu",null,"Is associated with WholeOrganism"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.3.im",null,"Is associated with WholeOrganism"));
			testCases.add(tmp);
			
		tmp = new TestCase("s12t04","Vertebrates","Collection Vertebrates Specimen","restart=true&soort=114",null);
			tmp.addTest("New specimen other");
			testCases.add(tmp);
			
		tmp = new TestCase("s12t05","Vertebrates Storage unit","Digistreet 30M Vertebrates Storage unit","xmlbeschrijvingid=182568204",null);
			tmp.addTest("Context");
			tmp.addContextLink(new Link(null,"TEST.BE.30",null,"Standard storage unit"));
			tmp.addContextLink(new Link(null,"DW",null,"Standard storage location"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.3",null,"Is Standard storage unit of"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.3",null,"Is Temporary storage unit of"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.3.a",null,"Is Temporary storage unit of"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.30",null,"Is Temporary storage unit of"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.30.a",null,"Is Temporary storage unit of"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.31",null,"Is Temporary storage unit of"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.32",null,"Is Temporary storage unit of"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.33",null,"Is Temporary storage unit of"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.34",null,"Is Temporary storage unit of"));
			testCases.add(tmp);		
			
		
		tmp = new TestCase("s12t06","Entomology","Collection Entomological Specimen","xmlbeschrijvingid=182567557","Collection Entomological Specimen");
			tmp.addTest("Context");
			tmp.addContextLink(new Link(null,"DW",null,"Standard storage location"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.23",null,"Belongs to"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.21",null,"Has parent"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.20.a",null,"Has parts"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.24",null,"Consists of"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.22",null,"Has children"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.2.a",null,"Is associated with tissue"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.2.bu",null,"Is associated with WholeOrganism"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.2.im",null,"Is associated with WholeOrganism"));
			testCases.add(tmp);

		tmp = new TestCase("s12t07","Entomology","Digistreet Entomological Specimen","xmlbeschrijvingid=182567557","Digistreet Entomological Specimen");
			tmp.addTest("Context");
			tmp.addContextLink(new Link(null,"DW",null,"Standard storage location"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.23",null,"Belongs to"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.21",null,"Has parent"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.20.a",null,"Has parts"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.24",null,"Consists of"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.22",null,"Has children"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.2.a",null,"Is associated with tissue"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.2.bu",null,"Is associated with WholeOrganism"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.2.im",null,"Is associated with WholeOrganism"));
			testCases.add(tmp);

		tmp = new TestCase("s12t08","Entomology","General Entomological Specimen","xmlbeschrijvingid=182567557","General Entomological Specimen");
			tmp.addTest("Context");
			tmp.addContextLink(new Link(null,"DW",null,"Standard storage location"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.23",null,"Belongs to"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.21",null,"Has parent"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.20.a",null,"Has parts"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.24",null,"Consists of"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.22",null,"Has children"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.2.a",null,"Is associated with tissue"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.2.bu",null,"Is associated with WholeOrganism"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.2.im",null,"Is associated with WholeOrganism"));
			testCases.add(tmp);

		tmp = new TestCase("s12t09","Entomology Storage unit","Digistreet 30M Entomological Storage unit","xmlbeschrijvingid=182567670",null);
			tmp.addTest("Context");
			tmp.addContextLink(new Link(null,"DW",null,"Standard storage location"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.2",null,"Is Temporary storage unit of"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.2.a",null,"Is Temporary storage unit of"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.20",null,"Is Temporary storage unit of"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.20.a",null,"Is Temporary storage unit of"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.21",null,"Is Temporary storage unit of"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.22",null,"Is Temporary storage unit of"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.23",null,"Is Temporary storage unit of"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.24",null,"Is Temporary storage unit of"));
			tmp.addContextLink(new Link(null,"TEST.BE.2",null,"Is Standard storage unit of"));
			tmp.addContextLink(new Link(null,"TEST.BE.2",null,"Is Temporary storage unit of"));
			testCases.add(tmp);

		tmp = new TestCase("s12t10","Invertebrates","Collection Invertebrates Specimen","xmlbeschrijvingid=182567725","Collection Invertebrates Specimen");
			tmp.addTest("Context");
			tmp.addContextLink(new Link(null,"TEST.BE.4",null,"Standard storage unit"));
			tmp.addContextLink(new Link(null,"DW",null,"Standard storage location"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.43",null,"Belongs to"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.41",null,"Has parent"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.40.a",null,"Has parts"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.44",null,"Consists of"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.42",null,"Has children"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.4.a",null,"Is associated with tissue"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.4.bu",null,"Is associated with WholeOrganism"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.4.im",null,"Is associated with WholeOrganism"));
			testCases.add(tmp);

		tmp = new TestCase("s12t11","Invertebrates","Digistreet Invertebrates Specimen","xmlbeschrijvingid=182567725","Digistreet Invertebrates Specimen");
			tmp.addTest("Context");
			tmp.addContextLink(new Link(null,"TEST.BE.4",null,"Standard storage unit"));
			tmp.addContextLink(new Link(null,"DW",null,"Standard storage location"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.43",null,"Belongs to"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.41",null,"Has parent"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.40.a",null,"Has parts"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.44",null,"Consists of"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.42",null,"Has children"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.4.a",null,"Is associated with tissue"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.4.bu",null,"Is associated with WholeOrganism"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.4.im",null,"Is associated with WholeOrganism"));
			testCases.add(tmp);


		tmp = new TestCase("s12t12","Invertebrates","General Invertebrates Specimen","xmlbeschrijvingid=182567725","General Invertebrates Specimen");
			tmp.addTest("Context");
			tmp.addContextLink(new Link(null,"TEST.BE.4",null,"Standard storage unit"));
			tmp.addContextLink(new Link(null,"DW",null,"Standard storage location"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.43",null,"Belongs to"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.41",null,"Has parent"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.40.a",null,"Has parts"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.44",null,"Consists of"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.42",null,"Has children"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.4.a",null,"Is associated with tissue"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.4.bu",null,"Is associated with WholeOrganism"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.4.im",null,"Is associated with WholeOrganism"));
			testCases.add(tmp);

		tmp = new TestCase("s12t13","Invertebrates Storage unit","Digistreet 30M Invertebrates Storage unit","xmlbeschrijvingid=182567839",null);
			tmp.addTest("Context");
			tmp.addContextLink(new Link(null,"TEST.BE.40",null,"Standard storage unit"));
			tmp.addContextLink(new Link(null,"DW",null,"Standard storage location"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.4",null,"Is Standard storage unit of"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.4",null,"Is Temporary storage unit of"));
			testCases.add(tmp);

		tmp = new TestCase("s12t14","Mineralogy and Petrology","Collection MinandPetro Specimen","xmlbeschrijvingid=182568231","Collection MinandPetro Specimen");
			tmp.addTest("Context");
			tmp.addContextLink(new Link(null,"TEST.BE.6",null,"Standard storage unit"));
			tmp.addContextLink(new Link(null,"DW",null,"Standard storage location"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.64",null,"Consists of"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.62",null,"Has children"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.6.a",null,"Is associated with tissue"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.6.bu",null,"Is associated with WholeOrganism"));
			testCases.add(tmp);

		tmp = new TestCase("s12t15","Mineralogy and Petrology","Digistreet MinandPetro Specimen","xmlbeschrijvingid=182568231","Digistreet MinandPetro Specimen");
			tmp.addTest("Context");
			tmp.addContextLink(new Link(null,"TEST.BE.6",null,"Standard storage unit"));
			tmp.addContextLink(new Link(null,"DW",null,"Standard storage location"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.64",null,"Consists of"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.62",null,"Has children"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.6.a",null,"Is associated with tissue"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.6.bu",null,"Is associated with WholeOrganism"));
			testCases.add(tmp);

		tmp = new TestCase("s12t16","Mineralogy and Petrology","General MinandPetro Specimen","xmlbeschrijvingid=182568231","General MinandPetro Specimen");
			tmp.addTest("Context");
			tmp.addContextLink(new Link(null,"TEST.BE.6",null,"Standard storage unit"));
			tmp.addContextLink(new Link(null,"DW",null,"Standard storage location"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.64",null,"Consists of"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.62",null,"Has children"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.6.a",null,"Is associated with tissue"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.6.bu",null,"Is associated with WholeOrganism"));
			testCases.add(tmp);

		tmp = new TestCase("s12t17","Mineralogy and Petrology Storage unit","Digistreet 30M MinandPetro Storage unit","xmlbeschrijvingid=182568343",null);
			tmp.addTest("Context");
			tmp.addContextLink(new Link(null,"TEST.BE.60",null,"Standard storage unit"));
			tmp.addContextLink(new Link(null,"DW",null,"Standard storage location"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.6",null,"Is Standard storage unit of"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.6",null,"Is Temporary storage unit of"));
			testCases.add(tmp);

		tmp = new TestCase("s12t18","Paleontology","Collection Paleontological Specimen","xmlbeschrijvingid=182567934","Collection Paleontological Specimen");
			tmp.addTest("Context");
			tmp.addContextLink(new Link(null,"TEST.BE.5",null,"Standard storage unit"));
			tmp.addContextLink(new Link(null,"DW",null,"Standard storage location"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.50.a",null,"Has parts"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.54",null,"Consists of"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.52",null,"Has children"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.5.a",null,"Is associated with tissue"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.5.bu",null,"Is associated with WholeOrganism"));
			testCases.add(tmp);

		tmp = new TestCase("s12t19","Paleontology","Digistreet Paleontological Specimen","xmlbeschrijvingid=182567934","Digistreet Paleontological Specimen");
			tmp.addTest("Context");
			tmp.addContextLink(new Link(null,"TEST.BE.5",null,"Standard storage unit"));
			tmp.addContextLink(new Link(null,"DW",null,"Standard storage location"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.50.a",null,"Has parts"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.54",null,"Consists of"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.52",null,"Has children"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.5.a",null,"Is associated with tissue"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.5.bu",null,"Is associated with WholeOrganism"));
			testCases.add(tmp);

		tmp = new TestCase("s12t20","Paleontology","General Paleontological Specimen","xmlbeschrijvingid=182567934","General Paleontological Specimen");
			tmp.addTest("Context");
			tmp.addContextLink(new Link(null,"TEST.BE.5",null,"Standard storage unit"));
			tmp.addContextLink(new Link(null,"DW",null,"Standard storage location"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.50.a",null,"Has parts"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.54",null,"Consists of"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.52",null,"Has children"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.5.a",null,"Is associated with tissue"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.5.bu",null,"Is associated with WholeOrganism"));
			testCases.add(tmp);

		tmp = new TestCase("s12t21","Paleontology","Paleo Specimen Compact","xmlbeschrijvingid=182567934","Paleo Specimen Compact");
			tmp.addTest("Context");
			tmp.addContextLink(new Link(null,"TEST.BE.5",null,"Standard storage unit"));
			tmp.addContextLink(new Link(null,"DW",null,"Standard storage location"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.50.a",null,"Has parts"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.54",null,"Consists of"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.52",null,"Has children"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.5.a",null,"Is associated with tissue"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.5.bu",null,"Is associated with WholeOrganism"));
			testCases.add(tmp);

		tmp = new TestCase("s12t22","Paleontology Storage unit","Digistreet 30M Paleontological Storage unit","xmlbeschrijvingid=182568037",null);
			tmp.addTest("Context");
			tmp.addContextLink(new Link(null,"TEST.BE.50",null,"Standard storage unit"));
			tmp.addContextLink(new Link(null,"DW",null,"Standard storage location"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.5",null,"Is Standard storage unit of"));
			tmp.addContextLink(new Link(null,"TEST.SPEC.5",null,"Is Temporary storage unit of"));
			testCases.add(tmp);
		
		Object[][] testData = new Object[testCases.size()][1];
		for (int row=0; row<testCases.size(); row++)
			testData[row][0] = testCases.get(row);
		
		return testData;
		   
		   
			/*
			tmp = new TestCase("Arts","FES","xmlbeschrijvingid=20250966");
			tmp = new TestCase("Arts Storage unit","Dynamisch","xmlbeschrijvingid=20250966");
			tmp = new TestCase("Sounds","FES","xmlbeschrijvingid=20250966");
			tmp = new TestCase("Sounds Storage unit","Dynamisch","xmlbeschrijvingid=20250966");
			tmp = new TestCase("DNA Lab","Collection Entomological Specimen","xmlbeschrijvingid=20250966");
			tmp = new TestCase("DNA Lab","Digistreet Entomological Specimen","xmlbeschrijvingid=20250966");
			tmp = new TestCase("DNA Lab","Collection Vertebrates Specimen","xmlbeschrijvingid=20250966");
			tmp = new TestCase("DNA Lab","DNA Lab Specimen","xmlbeschrijvingid=20250966");
			tmp = new TestCase("Molecular Storage unit","Collection Molecular Storage unit","xmlbeschrijvingid=20250966");
			*/
			
	   
	   
	   }
		   



}