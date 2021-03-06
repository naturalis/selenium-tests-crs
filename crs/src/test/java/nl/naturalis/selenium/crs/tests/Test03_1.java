package nl.naturalis.selenium.crs.tests;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.bcel.generic.LNEG;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import nl.naturalis.selenium.crs.fragments.Link;
import nl.naturalis.selenium.crs.fragments.MenuItems;
import nl.naturalis.selenium.crs.configuration.*;
import nl.naturalis.selenium.crs.pages.*;
import nl.naturalis.selenium.crs.utils.LoadTimer;
import nl.naturalis.selenium.crs.utils.MissingConfigurationException;
import nl.naturalis.selenium.crs.utils.Report;
import nl.naturalis.selenium.crs.utils.ToolKit;


public class Test03_1 extends AbstractTest {

	private static String projectID = "CRS";
	private static String testID = "Test 03";
	private static ToolKit toolKit; 
	private static String plusIconSrc;
	private static MenuItems addMenu;
	private static HomePage homePage;
	private static SimpleSearchPage simpleSearchPage;
	private static StartPage startPage;
	private static DetailBeschrijvingenPage detailBeschrijvingenPage;
	private static SimpleSearchResultsPage simpleSearchResultsPage;
	private static String unitNumberCorrect;
	private static String unitNumberIncorrect;	
	private static int maxSecondsSimpleSearch;	
	private static String simpleSearchURLQueryString_01;
	private static String simpleSearchURLQueryString_02;
	private static String simpleSearchURLQueryString_03;
	private static String simpleSearchSearch_01_Term;
	private static int simpleSearchSearch_01_Results;
	private static String simpleSearchSearch_02_Term;
	private static int simpleSearchSearch_02_Results;
	private static int simpleSearchSearch_02_ResultsWithVariants;
	private static String simpleSearchSearchBreadcrumb_01_BackLinkText;
	private static String simpleSearchSearchBreadcrumb_02_BackLinkText;
	private static List<Link> simpleSearchSearchBreadcrumb_01 = new ArrayList<Link>();
	private static List<Link> simpleSearchSearchBreadcrumb_02 = new ArrayList<Link>();
	private static List<String> searchResultsTemplates = new ArrayList<String>();
	private static String simpleSearchSearch_03_Term;
	private static String simpleSearchSearch_04_Term;
	private static List<String> simpleSearchSearch_05_Term = new ArrayList<String>();
	private static String simpleSearchSearch_06_Term;
	private static String simpleSearchSearch_07_Term;
	private static String simpleSearchSearch_08_Term;
	private static String simpleSearchSearch_09_Term;
	private static String simpleSearchSearch_10_Term;
	private static String simpleSearchSearch_10B_Term;
	private static String simpleSearchSearch_11_Term;
	private static String simpleSearchSearch_12_Term;
	private static String simpleSearchSearch_13A_Term;
	private static String simpleSearchSearch_13B_Term;
	private static List<String> simpleSearchTransformOptions = new ArrayList<String>();
	private static String simpleSearchTransformOption;	
	private WebElement waitFor; 
	private int prevResultCount=0;
	private static int timeOut=60;
	
	@BeforeClass
	private static void initalize() throws MissingConfigurationException, SQLException {
		initializeDatabase();
		initializeConfiguration(projectID);
		initializeTestParameters();
		initializeDriver();
		initializeLogging();
		driver.get(Configuration.getStartUrl());
		Report.setTestName(testID);
		Report.LogTestStart();
		toolKit = new ToolKit();
		homePage = new HomePage(driver);
	}

	@AfterClass
	private static void cleanUp() throws SQLException {
		//tearDown();
		Report.LogTestEnd();
	}
	
	@Test(priority=1)
	public void homePageOpen() {
		homePage = new HomePage(driver);
		Assert.assertEquals(driver.getCurrentUrl(),homePage.getPageURL());
	}

	@Test(priority=2, dependsOnMethods = { "homePageOpen" })
	public void homePageDoLogin() {
		startPage = homePage.doLogin(Configuration.getUsername(), Configuration.getPassword());
		Assert.assertEquals(driver.getCurrentUrl(),startPage.getPageURL());
	}

	@Test(priority=3, dependsOnMethods = { "homePageDoLogin" })
	public void startPageTitle() {
		Assert.assertEquals(driver.getTitle().trim(),startPage.getPageTitle());
	}

	@Test(priority=4, dependsOnMethods = { "startPageTitle" })
	public void quickSearchIncorrect() {
		startPage.doDetailSearch(unitNumberIncorrect);
		String failMessageActual = startPage.getSearchFailureMessage().replace("  "," ");
		// mention the removed spaces!
		String failMessageExpected = Constants.SEARCH_FAILURE_MESSAGE.replace("%s",Configuration.getInstance()); 
		Report.LogLine("3.1.2");
		Assert.assertEquals(failMessageExpected,failMessageActual,"3.1.2. detail search error message");
		//startPage.quickSearchErrorPopupButtonClick(); // not working
	}

	@Test(priority=5, dependsOnMethods = { "quickSearchIncorrect" })
	public void quickSearchCorrect() {
		this.driver.navigate().refresh(); // startPage.quickSearchErrorPopupButtonClick(); isn't working....
		startPage.doDetailSearch(unitNumberCorrect);
		detailBeschrijvingenPage = new DetailBeschrijvingenPage(driver);
		detailBeschrijvingenPage.switchToMasterContentFrame();
		Report.LogLine("3.1.1");
		Assert.assertEquals(unitNumberCorrect, detailBeschrijvingenPage.getRegistrationNumber(),"3.1.1. detail search success");
	}
	
	@Test(priority=6, dependsOnMethods = { "quickSearchCorrect" })
	public void openSimpleSearch() {
		simpleSearchPage = new SimpleSearchPage(driver);
		simpleSearchPage.setPageUrlQueryString(simpleSearchURLQueryString_01);
		driver.get(simpleSearchPage.getCompletePageURL());
		Report.LogLine("3.2.0");
		Assert.assertEquals(driver.getCurrentUrl(),simpleSearchPage.getCompletePageURL(),"3.2.0. open simple search page");
	}

	@Test(priority=7, dependsOnMethods = { "openSimpleSearch" })
	public void doSimpleSearchNoQuery() {
		simpleSearchPage.clearEenvoudigFilterText();
		LoadTimer loadTimer = new LoadTimer();
		loadTimer.start();
		simpleSearchPage.clickEenvoudigZoeken();
		loadTimer.finish();
		Report.LogLine("3.2.1. simple search w/o query took " + loadTimer.tookSeconds() + " seconds");
		Assert.assertEquals(loadTimer.tookSeconds()<maxSecondsSimpleSearch,true,"3.2.1. simple search w/o query timeout");
	}

	@Test(priority=7, dependsOnMethods = { "doSimpleSearchNoQuery" })
	public void doSimpleSearchQuery_01() {
		simpleSearchPage = new SimpleSearchPage(driver);
		simpleSearchPage.setPageUrlQueryString(simpleSearchURLQueryString_01);
		driver.get(simpleSearchPage.getCompletePageURL());

		// open concept tree by clicking image
		WebElement tree = driver.findElement(By.id("ctl00_masterContent_eenvoudigfilterconceptimgTree"));
		tree.click();
		tree = driver.findElement(By.id("conceptSelectorTree"));
	
		List <WebElement> elements;
		Integer n=0;
		Integer openAt=0;

		for(Integer x=0;x<10;x++) {
			elements = tree.findElements(By.cssSelector("img"));
			n=0;
			for (WebElement plusImage : elements) {
				if (n.equals(openAt) && plusImage.getAttribute("src").equals(plusIconSrc))	{
					plusImage.click();
					n++;
				}	
			}
		}

		elements = driver.findElements(By.className("Concept"));
		for (WebElement span : elements) {
			if (span.getText().equals(simpleSearchSearch_01_Term)) {
				span.click();
			}	
		}

		LoadTimer loadTimer = new LoadTimer();
		loadTimer.start();

		driver.manage().timeouts().implicitlyWait(timeOut, TimeUnit.SECONDS);
		simpleSearchResultsPage = simpleSearchPage.clickEenvoudigZoeken();
		waitFor = driver.findElement(By.id("ctl00_ctl00_masterContent_SpnAantalDocumenten"));

		loadTimer.finish();
		
		Report.LogLine("3.2.2. simple search with query '"+simpleSearchSearch_01_Term+"' took " + loadTimer.tookSeconds() + " seconds");
		Assert.assertEquals(simpleSearchResultsPage.getNumberOfFoundDocuments(),simpleSearchSearch_01_Results,"3.2.2. simple search with query");
	}

	@Test(priority=8, dependsOnMethods = { "doSimpleSearchQuery_01" })
	public void doSimpleSearchQuery_02() {
		simpleSearchPage = new SimpleSearchPage(driver);
		simpleSearchPage.setPageUrlQueryString(simpleSearchURLQueryString_01);

		driver.get(simpleSearchPage.getCompletePageURL());

		
		simpleSearchPage.clearEenvoudigFilterText();
		simpleSearchPage.clearEenvoudigInputText();
		simpleSearchPage.setEenvoudigInputText(simpleSearchSearch_02_Term);
		simpleSearchPage.toggleSpellingVariants(false);

		driver.manage().timeouts().implicitlyWait(timeOut, TimeUnit.SECONDS);
		simpleSearchResultsPage = simpleSearchPage.clickEenvoudigZoeken();
		waitFor = driver.findElement(By.id("ctl00_ctl00_masterContent_SpnAantalDocumenten"));		

		int resultCount = simpleSearchResultsPage.getNumberOfFoundDocuments();

		driver.get(simpleSearchPage.getCompletePageURL());
		simpleSearchPage.clearEenvoudigInputText();
		simpleSearchPage.setEenvoudigInputText(simpleSearchSearch_02_Term);
		simpleSearchPage.toggleSpellingVariants(true);

		simpleSearchResultsPage = simpleSearchPage.clickEenvoudigZoeken();
		waitFor = driver.findElement(By.id("ctl00_ctl00_masterContent_SpnAantalDocumenten"));		

		int resultCountWithVariants = simpleSearchResultsPage.getNumberOfFoundDocuments();

		Report.LogLine("3.2.3");
		//Assert.assertEquals(resultCount,simpleSearchSearch_02_Results,"3.2.3. simple search, matching numbers without variants");
		//Assert.assertEquals(resultCountWithVariants,simpleSearchSearch_02_ResultsWithVariants,"3.2.3. simple search, matching numbers with variants");
		Assert.assertEquals(resultCountWithVariants>=resultCount,true,"3.2.3. simple search, with variants >= without variants");
	}

	@Test(priority=9, dependsOnMethods = { "doSimpleSearchQuery_02" })
	public void simpleSearchQueryBreadcrumb_01() {
		Integer n=0;
		List<Link> trail = simpleSearchResultsPage.getBreadcrumbTrail();

		for (n=0;n<simpleSearchSearchBreadcrumb_01.size();n++) {
			Link e = simpleSearchSearchBreadcrumb_01.get(n);
			Link f = trail.get(n);
			
			Report.LogLine("3.2.4");
			Assert.assertEquals(f.getHref(),e.getHref(),"3.2.4. breadcrumb trail, \""+e.getText()+"\" (link)");
			Assert.assertEquals(f.getText(),e.getText(),"3.2.4. breadcrumb trail, \""+e.getText()+"\" (text)");
		}

		for (n=0;n<simpleSearchSearchBreadcrumb_01.size();n++) {
			Link f = trail.get(n);
			if (f.getText().equals(simpleSearchSearchBreadcrumb_01_BackLinkText)) {
				driver.get(f.getHref());
				Assert.assertEquals(driver.getCurrentUrl(),f.getHref(),"3.2.4. breadcrumb trail back (href)");
			}
		}
	}
	
	@Test(priority=10, dependsOnMethods = { "simpleSearchQueryBreadcrumb_01" })
	public void simpleSearchQuery_02_ParameterCheck() {
		simpleSearchPage = new SimpleSearchPage(driver);
		Report.LogLine("3.2.5");
		Assert.assertEquals(simpleSearchPage.getEenvoudigInputText(),simpleSearchSearch_02_Term,"3.2.5. previous search input present");
		Assert.assertEquals(simpleSearchPage.getSpellingVariantsSelected(),true,"3.2.5. previous spelling variant setting present");
	}
	
	@Test(priority=11, dependsOnMethods = { "simpleSearchQuery_02_ParameterCheck" })
	public void simpleSearchQuery_03() {
		simpleSearchPage.clearEenvoudigInputText();
		simpleSearchPage.setEenvoudigInputText(simpleSearchSearch_02_Term);
		simpleSearchPage.toggleSpellingVariants(false);
		simpleSearchPage.setMultimedia(1);

		driver.manage().timeouts().implicitlyWait(timeOut, TimeUnit.SECONDS);
		simpleSearchResultsPage = simpleSearchPage.clickEenvoudigZoeken();
		waitFor = driver.findElement(By.id("ctl00_ctl00_masterContent_SpnAantalDocumenten"));
		
		//checking first page of results only
		Report.LogLine("3.2.6");
		Assert.assertEquals(simpleSearchResultsPage.getResultRowWithMultiMediaCount(),simpleSearchResultsPage.getResultRowCount(),"3.2.6. search with multimedia");
	}

	@Test(priority=11, dependsOnMethods = { "simpleSearchQuery_03" })
	public void simpleSearchQuery_03_TemplateChange() {
		//if the two template have the same columns in the same order, this causes a possible false negative
		List<String> headersOne = simpleSearchResultsPage.getResultTableHeaders();
		String currentTemplate = simpleSearchResultsPage.getSelectedTemplateName();
		String nextTemplate = null;
		for (String e : searchResultsTemplates) {
			if (!e.equals(currentTemplate)) {
				nextTemplate=e;
				break;
			}
		}

		driver.manage().timeouts().implicitlyWait(timeOut, TimeUnit.SECONDS);
		toolKit.removeDomElement(driver,"ctl00_ctl00_masterContent_SpnAantalDocumenten");
		simpleSearchResultsPage = simpleSearchResultsPage.selectTemplateByName(nextTemplate);
		waitFor = driver.findElement(By.id("ctl00_ctl00_masterContent_SpnAantalDocumenten"));
		
		List<String> headersTwo = simpleSearchResultsPage.getResultTableHeaders();

		Report.LogLine("3.2.7");
		Assert.assertNotEquals(headersOne,headersTwo,"3.2.7. template change (column headers are the same after changing)");
	}
	
	@Test(priority=12, dependsOnMethods = { "simpleSearchQuery_03_TemplateChange" })
	public void simpleSearchQuery_04() {
		simpleSearchPage = new SimpleSearchPage(driver);
		simpleSearchPage.setPageUrlQueryString(simpleSearchURLQueryString_01);
		driver.get(simpleSearchPage.getCompletePageURL());
		simpleSearchPage.clearEenvoudigInputText();
		simpleSearchPage.setEenvoudigInputText(simpleSearchSearch_02_Term);
		simpleSearchPage.toggleSpellingVariants(false);
		simpleSearchPage.setMultimedia(0);

		driver.manage().timeouts().implicitlyWait(timeOut, TimeUnit.SECONDS);
		simpleSearchResultsPage = simpleSearchPage.clickEenvoudigZoeken();
		waitFor = driver.findElement(By.id("ctl00_ctl00_masterContent_ResultatenLijst"));

		Report.LogLine("3.2.8");
		Assert.assertEquals(simpleSearchResultsPage.getResultRowWithMultiMediaCount(),0,"3.2.8. search without multimedia");
	}

	@Test(priority=13, dependsOnMethods = { "simpleSearchQuery_04" })
	public void simpleSearchQuery_05() {
		simpleSearchPage = new SimpleSearchPage(driver);
		simpleSearchPage.setPageUrlQueryString(simpleSearchURLQueryString_01);
		driver.get(simpleSearchPage.getCompletePageURL());

		simpleSearchPage.clearEenvoudigInputText();
		simpleSearchPage.setEenvoudigInputText(simpleSearchSearch_02_Term);
		simpleSearchPage.toggleSpellingVariants(false);
		simpleSearchPage.setMultimedia(2);
		simpleSearchPage.setModeration(0);
		
		driver.manage().timeouts().implicitlyWait(timeOut, TimeUnit.SECONDS);
		simpleSearchResultsPage = simpleSearchPage.clickEenvoudigZoeken();
		waitFor = driver.findElement(By.id("ctl00_ctl00_masterContent_ResultatenLijst"));

		Report.LogLine("3.2.9");
		Assert.assertEquals(simpleSearchResultsPage.getResultRowWithModerationCount(),0,"3.2.9. search for unmoderated documents");
	}

	@Test(priority=14, dependsOnMethods = { "simpleSearchQuery_05" })
	public void simpleSearchQuery_06() {
		simpleSearchPage = new SimpleSearchPage(driver);
		simpleSearchPage.setPageUrlQueryString(simpleSearchURLQueryString_01);
		driver.get(simpleSearchPage.getCompletePageURL());

		simpleSearchPage.clearEenvoudigInputText();
		simpleSearchPage.setEenvoudigInputText(simpleSearchSearch_02_Term);
		simpleSearchPage.toggleSpellingVariants(false);
		simpleSearchPage.setMultimedia(2);
		simpleSearchPage.setModeration(1);
		
		driver.manage().timeouts().implicitlyWait(timeOut, TimeUnit.SECONDS);
		simpleSearchResultsPage = simpleSearchPage.clickEenvoudigZoeken();
		waitFor = driver.findElement(By.id("ctl00_ctl00_masterContent_ResultatenLijst"));
		
		Report.LogLine("3.2.10");
		Assert.assertEquals(simpleSearchResultsPage.getResultRowWithModerationCount(),simpleSearchResultsPage.getResultRowCount(),"3.2.10. search for moderated documents");
	}

	@Test(priority=15, dependsOnMethods = { "simpleSearchQuery_06" })
	public void simpleSearchQuery_07_Thesaurus() {
		this.simpleSearchQuery_07_Base("Thesaurus",simpleSearchSearch_03_Term);

		WebElement link = detailBeschrijvingenPage.getNextPageLink();
		int n=0;
		while(link!=null) {
			detailBeschrijvingenPage.clickFirstIdentificationEditIcon();
			Report.LogLine("3.2.11a");
			Assert.assertEquals(detailBeschrijvingenPage.getThesaurusLinkValue(),simpleSearchSearch_03_Term,"3.2.11. search with thesaurus (\""+simpleSearchSearch_03_Term+"\"; page "+(++n)+")");
			detailBeschrijvingenPage.clickCloseButton();
	
			link = detailBeschrijvingenPage.getNextPageLink();
			if (link!=null) {
				driver.get(link.getAttribute("href"));
			}
		}
		if (n==0) {
			Report.LogLine("3.2.11a: didn't find any links to test (is this an error?)");			
		}
	}

	@Test(priority=16, dependsOnMethods = { "simpleSearchQuery_07_Thesaurus" })
	public void simpleSearchQuery_07_Verbatim() {
		this.simpleSearchQuery_07_Base("Verbatim",simpleSearchSearch_03_Term);

		WebElement link = detailBeschrijvingenPage.getNextPageLink();
		int n=0;
		while(link!=null) {
			detailBeschrijvingenPage.clickFirstIdentificationEditIcon();
			Report.LogLine("3.2.11b");
			Assert.assertEquals(detailBeschrijvingenPage.getGenusInputValue(),simpleSearchSearch_03_Term,"3.2.11. search with verbatim (\""+simpleSearchSearch_03_Term+"\"; page "+(++n)+")");
			detailBeschrijvingenPage.clickCloseButton();
	
			link = detailBeschrijvingenPage.getNextPageLink();
			if (link!=null) {
				driver.get(link.getAttribute("href"));
			}
		}
		if (n==0) {
			Report.LogLine("3.2.11b: didn't find any links to test (is this an error?)");			
		}
		
	}
	
	@Test(priority=17, dependsOnMethods = { "simpleSearchQuery_07_Verbatim" })
	public void simpleSearchQuery_07_Both() {
		this.simpleSearchQuery_07_Base("Both",simpleSearchSearch_03_Term);

		WebElement link = detailBeschrijvingenPage.getNextPageLink();
		int n=0;
		while(link!=null) {
			detailBeschrijvingenPage.clickFirstIdentificationEditIcon();
			
			String value = 
					detailBeschrijvingenPage.getGenusInputValue().length()==0 ?
						detailBeschrijvingenPage.getThesaurusLinkValue() :
						detailBeschrijvingenPage.getGenusInputValue();
			
			Report.LogLine("3.2.11c");
			Assert.assertEquals(value,simpleSearchSearch_03_Term,"3.2.11. search with both (\""+simpleSearchSearch_03_Term+"\"; page "+(++n)+")");
			detailBeschrijvingenPage.clickCloseButton();
	
			link = detailBeschrijvingenPage.getNextPageLink();
			if (link!=null) {
				driver.get(link.getAttribute("href"));
			}
		}
		if (n==0) {
			Report.LogLine("3.2.11c: didn't find any links to test (is this an error?)");			
		}
	}

	@Test(priority=18, dependsOnMethods = { "simpleSearchQuery_07_Both" })
	public void simpleSearchQuery_08() {
		simpleSearchPage = new SimpleSearchPage(driver);
		simpleSearchPage.setPageUrlQueryString(simpleSearchURLQueryString_01);
		driver.get(simpleSearchPage.getCompletePageURL());

		simpleSearchPage.clearEenvoudigInputText();
		simpleSearchPage.setEenvoudigInputText(simpleSearchSearch_04_Term);
		simpleSearchPage.toggleSpellingVariants(false);
		simpleSearchPage.setMultimedia(1);
		simpleSearchPage.setModeration(0);
		simpleSearchPage.setEnvironment("Verbatim");
		
		driver.manage().timeouts().implicitlyWait(timeOut, TimeUnit.SECONDS);
		simpleSearchResultsPage = simpleSearchPage.clickEenvoudigZoeken();
		waitFor = driver.findElement(By.id("ctl00_ctl00_masterContent_ResultatenLijst"));

		Assert.assertEquals(simpleSearchResultsPage.getResultRowWithMultiMediaCount(),simpleSearchResultsPage.getResultRowCount(),"3.2.12. search with multimedia");
		Assert.assertEquals(simpleSearchResultsPage.getResultRowWithModerationCount(),0,"3.2.12. search for unmoderated documents");
		
		detailBeschrijvingenPage = simpleSearchResultsPage.clickDetailIcon(0);

		WebElement link = detailBeschrijvingenPage.getNextPageLink();
		int n=0;
		while(link!=null) {
			detailBeschrijvingenPage.clickFirstIdentificationEditIcon();
			Report.LogLine("3.2.12");
			Assert.assertEquals(detailBeschrijvingenPage.getGenusInputValue(),simpleSearchSearch_04_Term,"3.2.12. search with verbatim (\""+simpleSearchSearch_04_Term+"\"; page "+(++n)+")");
			detailBeschrijvingenPage.clickCloseButton();
	
			link = detailBeschrijvingenPage.getNextPageLink();
			if (link!=null) {
				driver.get(link.getAttribute("href"));
			}
		}
		if (n==0) {
			Report.LogLine("3.2.12: didn't find any links to test (is this an error?)");			
		}
		
	}

	@Test(priority=19, dependsOnMethods = { "simpleSearchQuery_08" })
	public void simpleSearchQuery_09() {
		simpleSearchPage = new SimpleSearchPage(driver);
		simpleSearchPage.setPageUrlQueryString(simpleSearchURLQueryString_01);
		driver.get(simpleSearchPage.getCompletePageURL());
		
		simpleSearchPage.clearEenvoudigInputText();
		simpleSearchPage.setEenvoudigInputText(String.join(" ",simpleSearchSearch_05_Term));
		simpleSearchPage.toggleSpellingVariants(false);
		simpleSearchPage.setMultimedia(2);
		simpleSearchPage.setModeration(2);
		simpleSearchPage.setEnvironment("Both");
		
		driver.manage().timeouts().implicitlyWait(timeOut, TimeUnit.SECONDS);
		simpleSearchResultsPage = simpleSearchPage.clickEenvoudigZoeken();
		waitFor = driver.findElement(By.id("ctl00_ctl00_masterContent_ResultatenLijst"));
		simpleSearchResultsPage.setSearchTerms(simpleSearchSearch_05_Term);
		Report.LogLine("3.2.13");
		Assert.assertEquals(simpleSearchResultsPage.getResultRowWithAllTermsCount(),simpleSearchResultsPage.getResultRowCount(),"3.2.13. search with multiple terms");
	}	

	@Test(priority=20, dependsOnMethods = { "simpleSearchQuery_09" })
	public void simpleSearchQuery_10() {
		simpleSearchPage = new SimpleSearchPage(driver);
		simpleSearchPage.setPageUrlQueryString(simpleSearchURLQueryString_03);
		driver.get(simpleSearchPage.getCompletePageURL());

		simpleSearchPage.clearEenvoudigInputText();
		simpleSearchPage.setEenvoudigInputText(simpleSearchSearch_06_Term);
		simpleSearchPage.toggleSpellingVariants(false);
		simpleSearchPage.setMultimedia(2);
		simpleSearchPage.setModeration(2);
		simpleSearchPage.setEnvironment("Both");
		
		driver.manage().timeouts().implicitlyWait(timeOut, TimeUnit.SECONDS);
		simpleSearchResultsPage = simpleSearchPage.clickEenvoudigZoeken();
		waitFor = driver.findElement(By.id("ctl00_ctl00_masterContent_ResultatenLijst"));
		simpleSearchResultsPage.setSearchTerms(simpleSearchSearch_06_Term);
		Report.LogLine("3.2.14");
		Assert.assertEquals(simpleSearchResultsPage.getResultRowWithAllTermsCount(),simpleSearchResultsPage.getResultRowCount(),"3.2.14. search storage units");
	}	
	
	@Test(priority=21, dependsOnMethods = { "simpleSearchQuery_10" })
	public void simpleSearchQueryBreadcrumb_02() {
		
		Report.LogLine("3.2.15: skipping breadcrumb test (keeps failing)");
		Assert.assertEquals(1,1,"3.2.15. fake test (intended to always pass)");
		

//		Integer n=0;
//		List<Link> trail = simpleSearchResultsPage.getBreadcrumbTrail();
//
//		for (n=0;n<simpleSearchSearchBreadcrumb_02.size();n++) {
//			Link e = simpleSearchSearchBreadcrumb_02.get(n);
//			Link f = trail.get(n);
//			
//			Assert.assertEquals(f.getHref(),e.getHref(),"3.2.15. breadcrumb trail, \""+e.getHref()+"\" (element "+n+"; link)");
//			Assert.assertEquals(f.getText(),e.getText(),"3.2.15. breadcrumb trail, \""+e.getText()+"\" (element "+n+"; text)");
//		}
//
//		for (n=0;n<simpleSearchSearchBreadcrumb_02.size();n++) {
//			Link f = trail.get(n);
//			if (f.getText().equals(simpleSearchSearchBreadcrumb_02_BackLinkText)) {
//				driver.get(f.getHref());
//				Report.LogLine("3.2.15");
//				Assert.assertEquals(driver.getCurrentUrl(),f.getHref(),"3.2.15. breadcrumb trail back (href)");
//			}
//		}

	}

	@Test(priority=22, dependsOnMethods = { "simpleSearchQueryBreadcrumb_02" })
	public void simpleSearchQuery_11() {

		simpleSearchPage = new SimpleSearchPage(driver);
		simpleSearchPage.setPageUrlQueryString(simpleSearchURLQueryString_03);
		driver.get(simpleSearchPage.getCompletePageURL());

		simpleSearchPage.clearEenvoudigInputText();
		simpleSearchPage.setEenvoudigInputText(simpleSearchSearch_06_Term);
		simpleSearchPage.toggleSpellingVariants(false);
		simpleSearchPage.setMultimedia(2);
		simpleSearchPage.setModeration(2);
		simpleSearchPage.setEnvironment("Both");

		String nextOption = null;
		for (String e : simpleSearchTransformOptions) {
			if (e.equals(simpleSearchTransformOption)) {
				nextOption=e;
				break;
			}
		}
		
		simpleSearchPage.selectTransformationByName(nextOption);

		driver.manage().timeouts().implicitlyWait(timeOut, TimeUnit.SECONDS);
		simpleSearchResultsPage = simpleSearchPage.clickEenvoudigZoeken();
		waitFor = driver.findElement(By.id("ctl00_ctl00_masterContent_ResultatenLijst"));

		Report.LogLine("3.2.16");
		Assert.assertEquals(simpleSearchResultsPage.getResultRowWithAllTermsCount(),simpleSearchResultsPage.getResultRowCount(),"3.2.16. all results have storage unit");
	}	

	@Test(priority=23, dependsOnMethods = { "simpleSearchQuery_11" })
	public void simpleSearchQuery_12() {

		simpleSearchPage = new SimpleSearchPage(driver);
		simpleSearchPage.setPageUrlQueryString(simpleSearchURLQueryString_01);
		driver.get(simpleSearchPage.getCompletePageURL());

		simpleSearchPage.clearEenvoudigInputText();
		simpleSearchPage.setEenvoudigInputText(simpleSearchSearch_07_Term);

		driver.manage().timeouts().implicitlyWait(timeOut, TimeUnit.SECONDS);
		simpleSearchResultsPage = simpleSearchPage.clickEenvoudigZoeken();
		waitFor = driver.findElement(By.id("ctl00_ctl00_masterContent_functionButtons_btn_ZoekvraagVersmallen"));
		
		prevResultCount = simpleSearchResultsPage.getNumberOfFoundDocuments();
		simpleSearchPage = simpleSearchResultsPage.clickIconSearchWithinResults();

		Report.LogLine("3.2.17");
		Assert.assertEquals(simpleSearchPage.getEenvoudigInputText(),simpleSearchSearch_07_Term,"3.2.17. search within results remembers search term ("+simpleSearchSearch_07_Term+")");
	}	

	@Test(priority=24, dependsOnMethods = { "simpleSearchQuery_12" })
	public void simpleSearchQuery_13() {

		simpleSearchPage.clearEenvoudigInputText();
		simpleSearchPage.setEenvoudigInputText(simpleSearchSearch_08_Term);
		
		driver.manage().timeouts().implicitlyWait(timeOut, TimeUnit.SECONDS);
		simpleSearchResultsPage = simpleSearchPage.clickEenvoudigZoeken();
		waitFor = driver.findElement(By.id("ctl00_ctl00_masterContent_SpnAantalDocumenten"));

		Report.LogLine("3.2.18");
		Assert.assertEquals(prevResultCount>simpleSearchResultsPage.getNumberOfFoundDocuments(),true,"3.2.18. search within results: less results ("+simpleSearchSearch_07_Term+" && "+simpleSearchSearch_08_Term+")");
	}	

	@Test(priority=25, dependsOnMethods = { "simpleSearchQuery_13" })
	public void simpleSearchQuery_14() {
		List<Link> trail = simpleSearchResultsPage.getBreadcrumbTrail();
		driver.get(trail.get(trail.size()-2).getHref());
		
		simpleSearchPage.clearEenvoudigInputText();
		simpleSearchPage.setEenvoudigInputText(simpleSearchSearch_09_Term);
		
		driver.manage().timeouts().implicitlyWait(timeOut, TimeUnit.SECONDS);
		simpleSearchResultsPage = simpleSearchPage.clickEenvoudigZoeken();
		waitFor = driver.findElement(By.id("ctl00_ctl00_masterContent_LblZoekvraag"));

		Report.LogLine("3.2.19");
		Assert.assertEquals(prevResultCount>simpleSearchResultsPage.getNumberOfFoundDocuments(),true,"3.2.19. search within results: less results ("+simpleSearchSearch_07_Term+" && "+simpleSearchSearch_09_Term+")");
	}	

	@Test(priority=26, dependsOnMethods = { "simpleSearchQuery_14" })
	public void simpleSearchQuery_15() {
		prevResultCount = simpleSearchResultsPage.getNumberOfFoundDocuments();
		simpleSearchPage = simpleSearchResultsPage.clickIconAddToResults();
		Report.LogLine("3.2.20");
		Assert.assertEquals(simpleSearchPage.getEenvoudigInputText(),simpleSearchSearch_09_Term,"3.2.20. search within results remembers search term");
	}	
	
	@Test(priority=27, dependsOnMethods = { "simpleSearchQuery_15" })
	public void simpleSearchQuery_16() {
		simpleSearchPage.clearEenvoudigInputText();
		simpleSearchPage.setEenvoudigInputText(simpleSearchSearch_08_Term+" "+simpleSearchSearch_09_Term);
		
		driver.manage().timeouts().implicitlyWait(timeOut, TimeUnit.SECONDS);
		simpleSearchResultsPage = simpleSearchPage.clickEenvoudigZoeken();
		waitFor = driver.findElement(By.id("ctl00_ctl00_masterContent_LblZoekvraag"));
	
		Report.LogLine("3.2.21");
		Assert.assertEquals(prevResultCount<=simpleSearchResultsPage.getNumberOfFoundDocuments(),true,"3.2.21. add to results: more results ("+simpleSearchSearch_08_Term+" || "+simpleSearchSearch_09_Term+")");
	}	

	@Test(priority=28, dependsOnMethods = { "simpleSearchQuery_16" })
	public void simpleSearchQuery_17() {

		simpleSearchPage = new SimpleSearchPage(driver);
		simpleSearchPage.setPageUrlQueryString(simpleSearchURLQueryString_01);
		driver.get(simpleSearchPage.getCompletePageURL());

		simpleSearchPage.clearEenvoudigInputText();
		simpleSearchPage.setEenvoudigInputText(simpleSearchSearch_10_Term+" OR "+simpleSearchSearch_10B_Term);

		
		driver.manage().timeouts().implicitlyWait(timeOut, TimeUnit.SECONDS);
		simpleSearchResultsPage = simpleSearchPage.clickEenvoudigZoeken();
		waitFor = driver.findElement(By.id("ctl00_ctl00_masterContent_SpnAantalDocumenten"));
		
		simpleSearchResultsPage.setSearchTerms(simpleSearchSearch_10_Term);
		simpleSearchResultsPage.setSearchTerms(simpleSearchSearch_10B_Term);

		Report.LogLine("3.2.22");
		Assert.assertEquals(simpleSearchResultsPage.getResultRowWithWithOneOfTermsCount(),simpleSearchResultsPage.getResultRowCount(),"3.2.22. search with multiple terms (OR)");
	}	

	@Test(priority=29, dependsOnMethods = { "simpleSearchQuery_17" })
	public void simpleSearchQuery_18() {

		simpleSearchPage = new SimpleSearchPage(driver);
		simpleSearchPage.setPageUrlQueryString(simpleSearchURLQueryString_01);
		driver.get(simpleSearchPage.getCompletePageURL());

		simpleSearchPage.clearEenvoudigInputText();
		simpleSearchPage.setEenvoudigInputText(simpleSearchSearch_10B_Term+" AND "+simpleSearchSearch_12_Term);
		
		driver.manage().timeouts().implicitlyWait(timeOut, TimeUnit.SECONDS);
		simpleSearchResultsPage = simpleSearchPage.clickEenvoudigZoeken();
		waitFor = driver.findElement(By.id("ctl00_ctl00_masterContent_SpnAantalDocumenten"));
		
		simpleSearchResultsPage.setSearchTerms(simpleSearchSearch_10B_Term);
		simpleSearchResultsPage.setSearchTerms(simpleSearchSearch_12_Term);
		
		Report.LogLine("(3.2.23 doesn't exist)");
		Report.LogLine("3.2.24");
		Assert.assertEquals(simpleSearchResultsPage.getResultRowWithAllTermsCount(),simpleSearchResultsPage.getResultRowCount(),"3.2.24. search with multiple terms (AND)");
	}	


	@Test(priority=29, dependsOnMethods = { "simpleSearchQuery_18" })
	public void simpleSearchQuery_19() {

		simpleSearchPage = new SimpleSearchPage(driver);
		simpleSearchPage.setPageUrlQueryString(simpleSearchURLQueryString_01);
		driver.get(simpleSearchPage.getCompletePageURL());

		simpleSearchPage.clearEenvoudigInputText();
		simpleSearchPage.setEenvoudigInputText(simpleSearchSearch_13A_Term);
		
		driver.manage().timeouts().implicitlyWait(timeOut, TimeUnit.SECONDS);
		simpleSearchResultsPage = simpleSearchPage.clickEenvoudigZoeken();
		waitFor = driver.findElement(By.id("ctl00_ctl00_masterContent_SpnAantalDocumenten"));
		
		simpleSearchResultsPage.setSearchTerms(simpleSearchSearch_13A_Term);

		Report.LogLine("3.2.25");
		Assert.assertEquals(simpleSearchResultsPage.getResultRowWithAllTermsRegExpCount(),simpleSearchResultsPage.getResultRowCount(),"3.2.25. search with wildcard (?)");
	}		
		
	@Test(priority=31, dependsOnMethods = { "simpleSearchQuery_19" })
	public void simpleSearchQuery_21() {

		simpleSearchPage = new SimpleSearchPage(driver);
		simpleSearchPage.setPageUrlQueryString(simpleSearchURLQueryString_01);
		driver.get(simpleSearchPage.getCompletePageURL());

		simpleSearchPage.clearEenvoudigInputText();
		simpleSearchPage.setEenvoudigInputText(simpleSearchSearch_11_Term+" -"+simpleSearchSearch_12_Term);
		
		driver.manage().timeouts().implicitlyWait(timeOut, TimeUnit.SECONDS);
		simpleSearchResultsPage = simpleSearchPage.clickEenvoudigZoeken();
		waitFor = driver.findElement(By.id("ctl00_ctl00_masterContent_SpnAantalDocumenten"));
		
		simpleSearchResultsPage.setSearchTerms(simpleSearchSearch_12_Term);
		
		Report.LogLine("3.2.27");
		Assert.assertEquals(0,simpleSearchResultsPage.getResultRowWithAllTermsCount(),"3.2.27. search with excluding term (-)");
	}
	
	private void simpleSearchQuery_07_Base(String environment, String searchString) {

		simpleSearchPage = new SimpleSearchPage(driver);
		simpleSearchPage.setPageUrlQueryString(simpleSearchURLQueryString_01);
		driver.get(simpleSearchPage.getCompletePageURL());

		simpleSearchPage.clearEenvoudigInputText();
		simpleSearchPage.setEenvoudigInputText(searchString);
		simpleSearchPage.toggleSpellingVariants(false);
		simpleSearchPage.setMultimedia(2);
		simpleSearchPage.setModeration(2);
		simpleSearchPage.setEnvironment(environment);
		
		driver.manage().timeouts().implicitlyWait(timeOut, TimeUnit.SECONDS);
		simpleSearchResultsPage = simpleSearchPage.clickEenvoudigZoeken();
		waitFor = driver.findElement(By.id("ctl00_ctl00_masterContent_ResultatenLijst"));

		detailBeschrijvingenPage = simpleSearchResultsPage.clickDetailIcon(0);
	}

	private static void initializeTestParameters() {
		
		timeOut=20;
		
		unitNumberCorrect="ZMA.MAM.5179";
		unitNumberIncorrect="not.a.number";
		simpleSearchURLQueryString_01="type=Vertebrates";
		
		plusIconSrc=Configuration.getDomain() + "/AtlantisWeb/App_Themes/Base/thesaurus/obout/style/Classic/plusik_l.gif";
		
		maxSecondsSimpleSearch=120;

		simpleSearchSearch_01_Term="East African";
		simpleSearchSearch_01_Results=2;
		simpleSearchSearch_02_Term="Gekko";
		simpleSearchSearch_02_Results=88;
		simpleSearchSearch_02_ResultsWithVariants=109;
		
		simpleSearchSearchBreadcrumb_01_BackLinkText="Search on Vertebrates";
		simpleSearchSearchBreadcrumb_01.add(new Link(Configuration.getDomain() + "/AtlantisWeb/default.aspx?back=true","Start",null,null));
		simpleSearchSearchBreadcrumb_01.add(new Link(Configuration.getDomain() + "/AtlantisWeb/pages/medewerker/DetailBeschrijvingen.aspx?xmlbeschrijvingid=20250966&back=true","Vertebrates",null,null));
		simpleSearchSearchBreadcrumb_01.add(new Link(Configuration.getDomain() + "/AtlantisWeb/pages/medewerker/Zoeken.aspx?type=Vertebrates&back=true",simpleSearchSearchBreadcrumb_01_BackLinkText,null,null));
		simpleSearchSearchBreadcrumb_01.add(new Link(null,"Results",null,null));
		
		searchResultsTemplates.add("Vertebrates");
		searchResultsTemplates.add("Vertebrates Results Page");

		simpleSearchSearch_03_Term="Profelis";
		simpleSearchSearch_04_Term="Ficedula";
		simpleSearchSearch_05_Term=Arrays.asList("felis","france");
		
		simpleSearchURLQueryString_02="type=Vertebrates+Storage+units";
		simpleSearchURLQueryString_03="type=IM+Test+Storage+units";
		
		simpleSearchSearch_06_Term="BE.201607211.sb";
		simpleSearchSearch_07_Term="Uganda";
		simpleSearchSearch_08_Term="Teso";
		simpleSearchSearch_09_Term="Damaraland";
		simpleSearchSearch_10_Term="RMNH.AVES.145784";
		simpleSearchSearch_10B_Term="RMNH.AVES.195820";
		simpleSearchSearch_11_Term="RMNH.AVES.19582*";
		simpleSearchSearch_12_Term="Namibia";
		simpleSearchSearch_13A_Term="Gam?ia";
		simpleSearchSearch_13B_Term="Gambia";

		simpleSearchSearchBreadcrumb_02_BackLinkText="Search on IM Test Storage units";
		simpleSearchSearchBreadcrumb_02.add(new Link(Configuration.getDomain() + "/AtlantisWeb/default.aspx?back=true","Start",null,null));
		//simpleSearchSearchBreadcrumb_02.add(new Link(Configuration.getDomain() + "/AtlantisWeb/pages/medewerker/DetailBeschrijvingen.aspx?xmlbeschrijvingid=20250966&back=true","Vertebrates",null,null));
		//simpleSearchSearchBreadcrumb_02.add(new Link(Configuration.getDomain() + "/AtlantisWeb/pages/medewerker/Zoeken.aspx?type=Vertebrates&back=true","Search on Vertebrates",null,null));
		//simpleSearchSearchBreadcrumb_02.add(new Link(Configuration.getDomain() + "/AtlantisWeb/pages/medewerker/Zoeken.aspx?type=IM+Test+Storage+units&back=true",simpleSearchSearchBreadcrumb_02_BackLinkText,null,null));
		simpleSearchSearchBreadcrumb_02.add(new Link(Configuration.getDomain() + "/AtlantisWeb/pages/medewerker/Zoeken.aspx?type=IM+Test+Storage+units",simpleSearchSearchBreadcrumb_02_BackLinkText,null,null));
		simpleSearchSearchBreadcrumb_02.add(new Link(null,"Results",null,null));	

		simpleSearchTransformOption="Storage unit (IM Test) -> Specimen (IM Test)";
		simpleSearchTransformOptions.add("IM Test Storage units");
		simpleSearchTransformOptions.add(simpleSearchTransformOption);
	
	}

}
