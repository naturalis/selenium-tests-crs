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

	/*
	public static void main(String[] args) {
		initializeDatabase();
		initializeConfiguration(projectID);
		initializeTestParameters();
	}
	*/
	
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
	private static void cleanUp() throws SQLException {
		tearDown();
		Report.LogTestEnd();
	}
	
	@Test(priority=1)
	public void homePageOpen() {
		homePage = new HomePage(driver);
		Assert.assertEquals(driver.getCurrentUrl(),homePage.getPageURL());
	}
	
	@Test(priority=2, dependsOnMethods = { "homePageOpen" })
	public void homePageTitle() {
		Assert.assertEquals(driver.getTitle().trim(),homePage.getPageTitle());
	}

	@Test(priority=2, dependsOnMethods = { "homePageOpen" })
	public void homePageScriptSupport() {
		Assert.assertTrue(homePage.hasJavascriptOkIcon());
		Assert.assertTrue(homePage.hasAjaxOkIcon());
		Assert.assertTrue(homePage.hasCookieOkIcon());
	}

	@Test(priority=2, dependsOnMethods = { "homePageOpen" })
	public void homePageLoginElements() {
		Assert.assertTrue(homePage.hasUsernameInput());
		Assert.assertTrue(homePage.hasPasswordInput());
		Assert.assertTrue(homePage.hasLoginButton());
	}
	
	@Test(priority=3, dependsOnMethods = { "homePageLoginElements" })
	public void homePageDoLogin() {
		startPage = homePage.doLogin(config.getUsername(), config.getPassword());
		Assert.assertEquals(driver.getCurrentUrl(),startPage.getPageURL());
	}

	@Test(priority=4, dependsOnMethods = { "homePageDoLogin" })
	public void startPageTitle() {
		Assert.assertEquals(driver.getTitle().trim(),startPage.getPageTitle());
	}

	@Test(priority=4, dependsOnMethods = { "homePageDoLogin" })
	public void checkStartPageMenus() {
		for (MenuItems menuItem : StartPageMenuItemsCollection) {
			checkStartPageMenu(menuItem);
		}
	}

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

	private static void initializeTestParameters() {
		DetailBeschrijvingenPageQueryStringOk="xmlbeschrijvingid=20250966";
		DetailBeschrijvingenPageQueryStringNok="xmlbeschrijvingid=23838308";
		DetailBeschrijvingenPageExpectedResultNumber=1;

		StartPageMenuItemsCollection.add(new MenuItems(Arrays.asList("Employee","Search","Specimen","Vertebrates"),1));
		StartPageMenuItemsCollection.add(new MenuItems(Arrays.asList("Employee","Add","Specimen","Vertebrates"),1));

		LoggedOutUrl = config.getDomain() + "/AtlantisWeb/pages/publiek/Login.aspx?restart=true&action=afmelden";
	}

}