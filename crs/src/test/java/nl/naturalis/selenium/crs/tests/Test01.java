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


import nl.naturalis.selenium.crs.fragments.MenuItems;
import nl.naturalis.selenium.crs.configuration.*;
import nl.naturalis.selenium.crs.pages.*;
import nl.naturalis.selenium.crs.utils.MissingConfigurationException;
import nl.naturalis.selenium.crs.utils.Report;


/**
 * CRS Test 01, runs through scenario for logging in and out 
 * Runs as TestNG application only (no main())
 *
 * @author      Maarten Schermer <maarten.schermer@naturalis.nl>
 * @version     1.0
 * @since       1.0
 */
public class Test01 extends AbstractTest {

	private static String projectID = "CRS";
	private static String testID = "Test 01";
	
	private static List<MenuItems> StartPageMenuItemsCollection = new ArrayList<MenuItems>();
	private static String DetailBeschrijvingenPageQueryStringOk;
	private static String DetailBeschrijvingenPageQueryStringNok;
	private static Integer DetailBeschrijvingenPageExpectedResultNumber;
	private static String LoggedOutUrl;
	private static HomePage homePage;
	private static StartPage startPage;
	private static DetailBeschrijvingenPage detailBeschrijvingenPage;
	

	/**
	 * Initializes test, runs BeforeClass.
	 *
	 * @throws MissingConfigurationException, SQLException
	 * @since 1.0
	 */
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

	/**
	 * Clean up, runs AfterClass 
	 *
	 * @throws SQLException
	 * @since 1.0
	 */
	@AfterClass
	private static void cleanUp() throws SQLException {
		tearDown();
		Report.LogTestEnd();
	}
	
	/**
	 * Opens home page, asserts correct URL. 
	 *
	 * @since 1.0
	 */
	@Test(priority=1)
	public void homePageOpen() {
		homePage = new HomePage(driver);
		Assert.assertEquals(driver.getCurrentUrl(),homePage.getPageURL(),"home page URL");
	}
	
	/**
	 * Asserts correct page title. 
	 *
	 * @since 1.0
	 */
	@Test(priority=2, dependsOnMethods = { "homePageOpen" })
	public void homePageTitle() {
		Assert.assertEquals(driver.getTitle().trim(),homePage.getPageTitle(),"home page, page title");
	}

	/**
	 * Asserts presence of icons indicating correct working of Javascript, AJAX and cookies. 
	 *
	 * @since 1.0
	 */
	@Test(priority=2, dependsOnMethods = { "homePageOpen" })
	public void homePageScriptSupport() {
		Assert.assertTrue(homePage.hasJavascriptOkIcon(),"javascript ok icon");
		Assert.assertTrue(homePage.hasAjaxOkIcon(),"AJAX ok icon");
		Assert.assertTrue(homePage.hasCookieOkIcon(),"cookies ok icon");
	}

	/**
	 * Asserts presence of username and password inputs and login button. 
	 *
	 * @since 1.0
	 */
	@Test(priority=2, dependsOnMethods = { "homePageOpen" })
	public void homePageLoginElements() {
		Assert.assertTrue(homePage.hasUsernameInput(),"username input");
		Assert.assertTrue(homePage.hasPasswordInput(),"password input");
		Assert.assertTrue(homePage.hasLoginButton(),"login button");
	}

	/**
	 * Opens start page by logging in and asserts correct URL. 
	 *
	 * @since 1.0
	 */
	@Test(priority=3, dependsOnMethods = { "homePageLoginElements" })
	public void homePageDoLogin() {
		startPage = homePage.doLogin(Configuration.getUsername(), Configuration.getPassword());
		Assert.assertEquals(driver.getCurrentUrl(),startPage.getPageURL(),"URL of home page");
	}

	/**
	 * Asserts correct page title. 
	 *
	 * @since 1.0
	 */
	@Test(priority=4, dependsOnMethods = { "homePageDoLogin" })
	public void startPageTitle() {
		Assert.assertEquals(driver.getTitle().trim(),startPage.getPageTitle(),"title of start page");
	}

	/**
	 * Checks whether the menu contains the right (number of) items.  
	 *
	 * @since 1.0
	 */
	@Test(priority=4, dependsOnMethods = { "homePageDoLogin" })
	public void checkStartPageMenus() {
		for (MenuItems menuItem : StartPageMenuItemsCollection) {
			checkStartPageMenu(menuItem);
		}
	}

	/**
	 * Opens detail page, asserts correct URL.
	 * 
	 * @since 1.0
	 */
	@Test(priority=5, dependsOnMethods = { "startPageTitle" })
	public void detailPageOpen() {
		detailBeschrijvingenPage = new DetailBeschrijvingenPage(driver);
		detailBeschrijvingenPage.setPageUrlQueryString(DetailBeschrijvingenPageQueryStringOk);
		driver.get(detailBeschrijvingenPage.getCompletePageURL());
		Assert.assertEquals(driver.getCurrentUrl(),detailBeschrijvingenPage.getCompletePageURL(),"verifying URL detail page");
	}

	/**
	 * Asserts page title.
	 * 
	 * @since 1.0
	 */
	@Test(priority=6, dependsOnMethods = { "detailPageOpen" })
	public void detailPageTitle() {
		Assert.assertNotEquals(driver.getTitle().trim(),containsString(detailBeschrijvingenPage.getPageTitle()),"detail page, page title");
	}
	
	/**
	 * Asserts number of results.
	 * 
	 * @since 1.0
	 */
	@Test(priority=6, dependsOnMethods = { "detailPageOpen" })
	public void detailPageNumberOfResults() {
		Assert.assertNotEquals(DetailBeschrijvingenPageExpectedResultNumber,equalTo(detailBeschrijvingenPage.getNumberOfResults()),"detail page, number of results");
	}

	/**
	 * Attempts opening detail page, is redirected to home page due to illegal ID for this user. 
	 * Asserts URL is not that of the detail page requested. 
	 * 
	 * @since 1.0
	 */
	@Test(priority=7, dependsOnMethods = { "detailPageTitle" })
	public void detailPageOpenUnallowed() {
		detailBeschrijvingenPage.setPageUrlQueryString(DetailBeschrijvingenPageQueryStringNok);
		driver.get(detailBeschrijvingenPage.getCompletePageURL());  
		Assert.assertNotEquals(driver.getCurrentUrl(),detailBeschrijvingenPage.getCompletePageURL(),"URL should *not* be that of detail page");
	}

	/**
	 * Asserts not authorized message is being displayed.
	 * 
	 * @since 1.0
	 */
	@Test(priority=8, dependsOnMethods = { "detailPageOpenUnallowed" })
	public void unallowedPageOpen() {
		homePage = new HomePage(driver);
		Assert.assertEquals(Constants.NO_AUTHORISATION_MESSAGE,homePage.getAuthorizationFailureMessage(),"'not authorized' message");
	}

	/**
	 * Moves mouse to logoff link, asserts logoff link is displayed.
	 * 
	 * @since 1.0
	 */
	@Test(priority=9, dependsOnMethods = { "unallowedPageOpen" })
	public void unallowedPageMouseToLogOff() {
		Assert.assertEquals(homePage.mouseToLogOffLink().booleanValue(),true,"visibility of log off link");
	}

	/**
	 * Logs out, asserts correct URL.
	 * 
	 * @since 1.0
	 */
	@Test(priority=10, dependsOnMethods = { "unallowedPageMouseToLogOff" })
	public void logOff() {
		homePage.clickLogOffLink();
		Assert.assertEquals(driver.getCurrentUrl(),LoggedOutUrl,"URL after logging off");
	}

	/**
	 * Iterates through menu items, asserts whether the number of items in the
	 * last branch is correct.
	 * 
	 * @param menuItem
	 * @since 1.0
	 */
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
		
		Assert.assertEquals(Integer.valueOf(LastItemAndSiblings.size()),menuItem.getMaxLastItemAndSiblings(),"menu (" + String.join(" > ", menuItem.getMenuItems())+") item count");
	}

	/**
	 * Initializes specific test parameters:
	 * 
	 *   DetailBeschrijvingenPageQueryStringOk
	 *   DetailBeschrijvingenPageQueryStringNok
	 *   DetailBeschrijvingenPageExpectedResultNumber
	 *   MenuItems (SearchMenu, AddMenu)
	 *   LoggedOutUrl
	 * 
	 * @since 1.0
	 */
	private static void initializeTestParameters() {
		DetailBeschrijvingenPageQueryStringOk="xmlbeschrijvingid=20250966";
		DetailBeschrijvingenPageQueryStringNok="xmlbeschrijvingid=23838308";
		DetailBeschrijvingenPageExpectedResultNumber=1;

		StartPageMenuItemsCollection.add(new MenuItems(Arrays.asList("Employee","Search","Specimen","Vertebrates"),1));
		StartPageMenuItemsCollection.add(new MenuItems(Arrays.asList("Employee","Add","Specimen","Vertebrates"),1));

		LoggedOutUrl = config.getDomain() + "/AtlantisWeb/pages/publiek/Login.aspx?restart=true&action=afmelden";
	}

}