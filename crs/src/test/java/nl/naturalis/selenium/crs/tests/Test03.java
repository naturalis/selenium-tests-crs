package nl.naturalis.selenium.crs.tests;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

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


public class Test03 extends AbstractTest {

	private static String projectID = "CRS";
	private static String testID = "Test 03";
	private static ToolKit toolKit; 
	private static MenuItems addMenu;
	private static HomePage homePage;
	private static SimpleSearchPage simpleSearchPage;
	private static StartPage startPage;
	private static DetailBeschrijvingenPage detailBeschrijvingenPage;
	private static SimpleSearchResultsPage simpleSearchResultsPage;
	private static String unitNumberCorrect;
	private static String unitNumberIncorrect;	
	private static int maxSecondsSimpleSearch;	
	private static String simpleSearchURLQueryString;
	private static String simpleSearchSearch_01_Term;
	private static int simpleSearchSearch_01_Results;
	private static String simpleSearchSearch_02_Term;
	private static int simpleSearchSearch_02_Results;
	private static int simpleSearchSearch_02_ResultsWithVariants;
	private static String simpleSearchSearchBreadcrumbBackLinkText;
	private static List<Link> simpleSearchSearchBreadcrumb = new ArrayList<Link>();
	private static List<String> searchResultsTemplates = new ArrayList<String>();
	private static String simpleSearchSearch_03_Term;
	private WebElement waitFor; 
	
	
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

	public static void amain(String[] args) {
		try {
			initializeDatabase();
			initializeConfiguration(projectID);
			initializeTestParameters();
			initializeDriver();
			initializeLogging();
		} catch (Exception e) {
		}

		
		driver.get("http://localhost/tools/fuck.html");

		Select select = new Select(driver.findElement(By.id("fuck")));
		WebElement fuck = select.getFirstSelectedOption();
		System.out.println(fuck.getText());
	
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

	
//	@Test(priority=11, dependsOnMethods = { "homePageDoLogin" })
	public void tempStart() {
		/*
		simpleSearchPage = new SimpleSearchPage(driver);
		simpleSearchPage.setPageUrlQueryString(simpleSearchURLQueryString);
		driver.get(simpleSearchPage.getCompletePageURL());
		simpleSearchPage.clearEenvoudigInputText();
		simpleSearchPage.setEenvoudigInputText(simpleSearchSearch_02_Term);
		simpleSearchPage.toggleSpellingVariants(false);
		simpleSearchPage.setMultimedia(0);
		simpleSearchPage.setWaitUntil(maxSecondsSimpleSearch);
		simpleSearchResultsPage = simpleSearchPage.clickEenvoudigZoeken();
		simpleSearchPage.setWaitUntil(0);
		*/
	}


	@Test(priority=3, dependsOnMethods = { "homePageDoLogin" })
	public void startPageTitle() {
		Assert.assertEquals(driver.getTitle().trim(),startPage.getPageTitle());
	}

	// mention the removed spaces!
	@Test(priority=4, dependsOnMethods = { "startPageTitle" })
	public void quickSearchIncorrect() {
		startPage.doDetailSearch(unitNumberIncorrect);
		String failMessageActual = startPage.getSearchFailureMessage().replace("  "," ");
		String failMessageExpected = Constants.SEARCH_FAILURE_MESSAGE.replace("%s",Configuration.getInstance()); 
		Assert.assertEquals(failMessageExpected,failMessageActual,"3.1.2. detail search error message");
		//startPage.quickSearchErrorPopupButtonClick(); // not working
	}

	@Test(priority=5, dependsOnMethods = { "quickSearchIncorrect" })
	public void quickSearchCorrect() {
		this.driver.navigate().refresh(); // startPage.quickSearchErrorPopupButtonClick(); isn't working....
		startPage.doDetailSearch(unitNumberCorrect);
		detailBeschrijvingenPage = new DetailBeschrijvingenPage(driver);
		detailBeschrijvingenPage.switchToMasterContentFrame();
		Assert.assertEquals(unitNumberCorrect, detailBeschrijvingenPage.getRegistrationNumber(),"3.1.1. detail search success");
	}
	
	@Test(priority=6, dependsOnMethods = { "quickSearchCorrect" })
	public void openSimpleSearch() {
		simpleSearchPage = new SimpleSearchPage(driver);
		simpleSearchPage.setPageUrlQueryString(simpleSearchURLQueryString);
		driver.get(simpleSearchPage.getCompletePageURL());
		Assert.assertEquals(driver.getCurrentUrl(),simpleSearchPage.getCompletePageURL(),"3.2.0. open simple search page");
	}
/*
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
		simpleSearchPage.setPageUrlQueryString(simpleSearchURLQueryString);
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
				if (n.equals(openAt) && plusImage.getAttribute("src").equals("https://crspl.naturalis.nl/AtlantisWeb/App_Themes/Base/thesaurus/obout/style/Classic/plusik_l.gif"))	{
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

		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		simpleSearchResultsPage = simpleSearchPage.clickEenvoudigZoeken();
		waitFor = driver.findElement(By.id("ctl00_ctl00_masterContent_SpnAantalDocumenten"));

		loadTimer.finish();
		
		Report.LogLine("3.2.2. simple search with query '"+simpleSearchSearch_01_Term+"' took " + loadTimer.tookSeconds() + " seconds");
		Assert.assertEquals(simpleSearchResultsPage.getNumberOfFoundDocuments(),simpleSearchSearch_01_Results,"3.2.2. simple search with query");
	}


	@Test(priority=8, dependsOnMethods = { "doSimpleSearchQuery_01" })
	public void doSimpleSearchQuery_02() {
		simpleSearchPage = new SimpleSearchPage(driver);
		simpleSearchPage.setPageUrlQueryString(simpleSearchURLQueryString);

		driver.get(simpleSearchPage.getCompletePageURL());

		simpleSearchPage.clearEenvoudigInputText();
		simpleSearchPage.setEenvoudigInputText(simpleSearchSearch_02_Term);
		simpleSearchPage.toggleSpellingVariants(false);

		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
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

		Assert.assertEquals(resultCount,simpleSearchSearch_02_Results,"3.2.3. simple search, matching numbers without variants");
		Assert.assertEquals(resultCountWithVariants,simpleSearchSearch_02_ResultsWithVariants,"3.2.3. simple search, matching numbers with variants");
		Assert.assertEquals(resultCountWithVariants>resultCount,true,"3.2.3. simple search, with variants > without variants");
	}

	@Test(priority=9, dependsOnMethods = { "doSimpleSearchQuery_02" })
	public void simpleSearchQueryBreadcrumb() {
		Integer n=0;
		List<Link> trail = simpleSearchResultsPage.getBreadcrumbTrail();

		for (n=0;n<simpleSearchSearchBreadcrumb.size();n++) {
			Link e = simpleSearchSearchBreadcrumb.get(n);
			Link f = trail.get(n);
			
			Assert.assertEquals(f.getHref(),e.getHref(),"3.2.4. breadcrumb trail, \""+e.getText()+"\" (link)");
			Assert.assertEquals(f.getText(),e.getText(),"3.2.4. breadcrumb trail, \""+e.getText()+"\" (text)");
		}

		for (n=0;n<simpleSearchSearchBreadcrumb.size();n++) {
			Link f = trail.get(n);
			if (f.getText().equals(simpleSearchSearchBreadcrumbBackLinkText)) {
				driver.get(f.getHref());
				Assert.assertEquals(driver.getCurrentUrl(),f.getHref(),"3.2.4. breadcrumb trail back (href)");
			}
		}
	}

	
	@Test(priority=10, dependsOnMethods = { "simpleSearchQueryBreadcrumb" })
	public void simpleSearchQuery_02_ParameterCheck() {
		simpleSearchPage = new SimpleSearchPage(driver);
		Assert.assertEquals(simpleSearchPage.getEenvoudigInputText(),simpleSearchSearch_02_Term,"3.2.5. previous search input present");
		Assert.assertEquals(simpleSearchPage.getSpellingVariantsSelected(),true,"3.2.5. previous spelling variant setting present");
	}
	
	//checking first page of results only
	@Test(priority=11, dependsOnMethods = { "simpleSearchQuery_02_ParameterCheck" })
	public void simpleSearchQuery_03() {
		simpleSearchPage.clearEenvoudigInputText();
		simpleSearchPage.setEenvoudigInputText(simpleSearchSearch_02_Term);
		simpleSearchPage.toggleSpellingVariants(false);
		simpleSearchPage.setMultimedia(1);

		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		simpleSearchResultsPage = simpleSearchPage.clickEenvoudigZoeken();
		waitFor = driver.findElement(By.id("ctl00_ctl00_masterContent_SpnAantalDocumenten"));

		Assert.assertEquals(simpleSearchResultsPage.getResultRowWithMultiMediaCount(),simpleSearchResultsPage.getResultRowCount(),"3.2.6. search with multimedia");
	}


	//if the two template have the same columns in the same order, this causes a possible false negative 
	@Test(priority=11, dependsOnMethods = { "simpleSearchQuery_03" })
	public void simpleSearchQuery_03_TemplateChange() {
		List<String> headersOne = simpleSearchResultsPage.getResultTableHeaders();
		String currentTemplate = simpleSearchResultsPage.getSelectedTemplateName();
		String nextTemplate = null;
		for (String e : searchResultsTemplates) {
			if (!e.equals(currentTemplate)) {
				nextTemplate=e;
				break;
			}
		}

		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		toolKit.removeDomElement(driver,"ctl00_ctl00_masterContent_SpnAantalDocumenten");
		simpleSearchResultsPage = simpleSearchResultsPage.selectTemplateByName(nextTemplate);
		waitFor = driver.findElement(By.id("ctl00_ctl00_masterContent_SpnAantalDocumenten"));
		
		List<String> headersTwo = simpleSearchResultsPage.getResultTableHeaders();

		Assert.assertNotEquals(headersOne,headersTwo,"3.2.7. template change (column headers are the same after changing)");
	}
*/
	
//	@Test(priority=12, dependsOnMethods = { "simpleSearchQuery_03_TemplateChange" })
	@Test(priority=11, dependsOnMethods = { "openSimpleSearch" })
	public void simpleSearchQuery_04() {
		simpleSearchPage = new SimpleSearchPage(driver);
		simpleSearchPage.setPageUrlQueryString(simpleSearchURLQueryString);
		driver.get(simpleSearchPage.getCompletePageURL());
		simpleSearchPage.clearEenvoudigInputText();
		simpleSearchPage.setEenvoudigInputText(simpleSearchSearch_02_Term);
		simpleSearchPage.toggleSpellingVariants(false);
		simpleSearchPage.setMultimedia(0);

		//toolKit.removeDomElement(driver,"ctl00_ctl00_masterContent_ResultatenLijst");
		simpleSearchResultsPage = simpleSearchPage.clickEenvoudigZoeken();
		waitFor = driver.findElement(By.id("ctl00_ctl00_masterContent_ResultatenLijst"));

		Assert.assertEquals(simpleSearchResultsPage.getResultRowWithMultiMediaCount(),0,"3.2.8. search without multimedia");
	}

	/*
	@Test(priority=13, dependsOnMethods = { "simpleSearchQuery_04" })
	public void simpleSearchQuery_05() {
		simpleSearchPage = new SimpleSearchPage(driver);
		simpleSearchPage.setPageUrlQueryString(simpleSearchURLQueryString);
		driver.get(simpleSearchPage.getCompletePageURL());

		simpleSearchPage.clearEenvoudigInputText();
		simpleSearchPage.setEenvoudigInputText(simpleSearchSearch_02_Term);
		simpleSearchPage.toggleSpellingVariants(false);
		simpleSearchPage.setMultimedia(2);
		simpleSearchPage.setModeration(0);
		
		toolKit.removeDomElement(driver,"ctl00_ctl00_masterContent_ResultatenLijst");
		simpleSearchResultsPage = simpleSearchPage.clickEenvoudigZoeken();
		waitFor = driver.findElement(By.id("ctl00_ctl00_masterContent_ResultatenLijst"));

		Assert.assertEquals(simpleSearchResultsPage.getResultRowWithModerationCount(),0,"3.2.9. search for unmoderated documents");
	}

	@Test(priority=14, dependsOnMethods = { "simpleSearchQuery_05" })
	public void simpleSearchQuery_06() {
		simpleSearchPage = new SimpleSearchPage(driver);
		simpleSearchPage.setPageUrlQueryString(simpleSearchURLQueryString);
		driver.get(simpleSearchPage.getCompletePageURL());

		simpleSearchPage.clearEenvoudigInputText();
		simpleSearchPage.setEenvoudigInputText(simpleSearchSearch_02_Term);
		simpleSearchPage.toggleSpellingVariants(false);
		simpleSearchPage.setMultimedia(2);
		simpleSearchPage.setModeration(1);
		
		toolKit.removeDomElement(driver,"ctl00_ctl00_masterContent_ResultatenLijst");
		simpleSearchResultsPage = simpleSearchPage.clickEenvoudigZoeken();
		waitFor = driver.findElement(By.id("ctl00_ctl00_masterContent_ResultatenLijst"));
		
		Assert.assertEquals(simpleSearchResultsPage.getResultRowWithModerationCount(),simpleSearchResultsPage.getResultRowCount(),"3.2.10. search for moderated documents");
	}

	@Test(priority=15, dependsOnMethods = { "simpleSearchQuery_06" })
	public void simpleSearchQuery_07_Thesaurus() {
		this.simpleSearchQuery_07_Base("Thesaurus",simpleSearchSearch_03_Term);

		WebElement link = detailBeschrijvingenPage.getNextPageLink();
		int n=0;
		while(link!=null) {
			detailBeschrijvingenPage.clickFirstIdentificationEditIcon();
			Assert.assertEquals(detailBeschrijvingenPage.getThesaurusLinkValue(),simpleSearchSearch_03_Term,"3.2.11. search with thesaurus (\""+simpleSearchSearch_03_Term+"\"; page "+(++n)+")");
			detailBeschrijvingenPage.clickCloseButton();
	
			link = detailBeschrijvingenPage.getNextPageLink();
			if (link!=null) {
				driver.get(link.getAttribute("href"));
			}
		}
	}

	@Test(priority=15, dependsOnMethods = { "simpleSearchQuery_07_Thesaurus" })
	public void simpleSearchQuery_07_Verbatim() {
		this.simpleSearchQuery_07_Base("Verbatim",simpleSearchSearch_03_Term);

		WebElement link = detailBeschrijvingenPage.getNextPageLink();
		int n=0;
		while(link!=null) {
			detailBeschrijvingenPage.clickFirstIdentificationEditIcon();
			Assert.assertEquals(detailBeschrijvingenPage.getGenusInputValue(),simpleSearchSearch_03_Term,"3.2.11. search with verbatim (\""+simpleSearchSearch_03_Term+"\"; page "+(++n)+")");
			detailBeschrijvingenPage.clickCloseButton();
	
			link = detailBeschrijvingenPage.getNextPageLink();
			if (link!=null) {
				driver.get(link.getAttribute("href"));
			}
		}
	}
	

	@Test(priority=15, dependsOnMethods = { "simpleSearchQuery_07_Verbatim" })
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
			
			Assert.assertEquals(value,simpleSearchSearch_03_Term,"3.2.11. search with both (\""+simpleSearchSearch_03_Term+"\"; page "+(++n)+")");
			detailBeschrijvingenPage.clickCloseButton();
	
			link = detailBeschrijvingenPage.getNextPageLink();
			if (link!=null) {
				driver.get(link.getAttribute("href"));
			}
		}
	}

	private void simpleSearchQuery_07_Base(String environment, String searchString) {
		simpleSearchPage = new SimpleSearchPage(driver);
		simpleSearchPage.setPageUrlQueryString(simpleSearchURLQueryString);
		driver.get(simpleSearchPage.getCompletePageURL());

		simpleSearchPage.clearEenvoudigInputText();
		simpleSearchPage.setEenvoudigInputText(searchString);
		simpleSearchPage.toggleSpellingVariants(false);
		simpleSearchPage.setMultimedia(2);
		simpleSearchPage.setModeration(2);
		simpleSearchPage.setEnvironment(environment);
		
		toolKit.removeDomElement(driver,"ctl00_ctl00_masterContent_ResultatenLijst");
		simpleSearchResultsPage = simpleSearchPage.clickEenvoudigZoeken();
		waitFor = driver.findElement(By.id("ctl00_ctl00_masterContent_ResultatenLijst"));

		detailBeschrijvingenPage = simpleSearchResultsPage.clickDetailIcon(0);
	}
	
	
	
	
	


*/

	

	private static void initializeTestParameters() {
		unitNumberCorrect="ZMA.MAM.5179";
		unitNumberIncorrect="not.a.number";
		simpleSearchURLQueryString="type=Vertebrates";
		
		maxSecondsSimpleSearch=120;

		simpleSearchSearch_01_Term="East African";
		simpleSearchSearch_01_Results=2;
		simpleSearchSearch_02_Term="Gekko";
		simpleSearchSearch_02_Results=88;
		simpleSearchSearch_02_ResultsWithVariants=109;
		
		simpleSearchSearchBreadcrumbBackLinkText="Search on Vertebrates";
		simpleSearchSearchBreadcrumb.add(new Link(Configuration.getDomain() + "/AtlantisWeb/default.aspx?back=true","Start"));
		simpleSearchSearchBreadcrumb.add(new Link(Configuration.getDomain() + "/AtlantisWeb/pages/medewerker/DetailBeschrijvingen.aspx?xmlbeschrijvingid=20250966&back=true","Vertebrates"));
		simpleSearchSearchBreadcrumb.add(new Link(Configuration.getDomain() + "/AtlantisWeb/pages/medewerker/Zoeken.aspx?type=Vertebrates&back=true",simpleSearchSearchBreadcrumbBackLinkText));
		simpleSearchSearchBreadcrumb.add(new Link(null,"Results"));
		
		searchResultsTemplates.add("Vertebrates");
		searchResultsTemplates.add("Vertebrates Results Page");

		simpleSearchSearch_03_Term="Profelis";
	
	}


}

//driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);