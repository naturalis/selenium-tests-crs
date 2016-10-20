package nl.naturalis.selenium.crs.tests;

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
import nl.naturalis.selenium.crs.utils.Report;


public class FirstTest extends AbstractTest {

	private static String projectID = "CRS";
	private static String testID = "Test 01";
	
	private static TestParameters testParameters;
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
	private static void initalize() {
		initializeDatabase();
		initializeConfiguration(projectID);
		initializeTestParameters();
		initializeDriver();
		initializeLogging();
		driver.get(config.getStartUrl());
		//Report.LogTestStart();
		homePage = new HomePage(driver);
	}

	@AfterClass
	private static void cleanUp() {
		tearDown();
		//Report.LogTestEnd();
	}
	
	@Test(priority=1)
	public void homePageOpen() {
		homePage = new HomePage(driver);
		Assert.assertEquals(driver.getCurrentUrl(),homePage.getPageURL());
	}
	
	@Test(priority=2)
	public void homePageTitle() {
		Assert.assertEquals(driver.getTitle().trim(),homePage.getPageTitle());
	}

	@Test(priority=2)
	public void homePageScriptSupport() {
		Assert.assertTrue(homePage.hasJavascriptOkIcon());
		Assert.assertTrue(homePage.hasAjaxOkIcon());
		Assert.assertTrue(homePage.hasCookieOkIcon());
	}

	@Test(priority=2)
	public void homePageLoginElements() {
		Assert.assertTrue(homePage.hasUsernameInput());
		Assert.assertTrue(homePage.hasPasswordInput());
		Assert.assertTrue(homePage.hasLoginButton());
	}
	
	@Test(priority=3)
	public void homePageDoLogin() {
		startPage = homePage.doLogin(config.getUsername(), config.getPassword());
		Assert.assertEquals(driver.getCurrentUrl(),startPage.getPageURL());
	}

	@Test(priority=4)
	public void startPageTitle() {
		Assert.assertEquals(driver.getTitle().trim(),startPage.getPageTitle());
	}

	@Test(priority=4)
	public void checkStartPageMenus() {
		for (MenuItems menuItem : StartPageMenuItemsCollection) {
			checkStartPageMenu(menuItem);
		}
	}

	@Test(priority=5)
	public void detailPageOpen() {
		detailBeschrijvingenPage = new DetailBeschrijvingenPage(driver);
		detailBeschrijvingenPage.setPageUrlQueryString(DetailBeschrijvingenPageQueryStringOk);
		driver.get(detailBeschrijvingenPage.getCompletePageURL());
		Assert.assertEquals(driver.getCurrentUrl(),detailBeschrijvingenPage.getCompletePageURL());
	}

	@Test(priority=6)
	public void detailPageTitle() {
		assertThat(driver.getTitle().trim(),containsString(detailBeschrijvingenPage.getPageTitle()));
	}
	
	@Test(priority=6)
	public void detailPageNumberOfResults() {
		assertThat(DetailBeschrijvingenPageExpectedResultNumber,equalTo(detailBeschrijvingenPage.getNumberOfResults()));
	}

	@Test(priority=7)
	public void detailPageOpenUnallowed() {
		detailBeschrijvingenPage.setPageUrlQueryString(DetailBeschrijvingenPageQueryStringNok);
		driver.get(detailBeschrijvingenPage.getCompletePageURL());
		// illegal ID, page automatically redirects to login (= HomePage)  
		Assert.assertNotEquals(driver.getCurrentUrl(),detailBeschrijvingenPage.getCompletePageURL());
	}

	@Test(priority=8)
	public void unallowedPageOpen() {
		homePage = new HomePage(driver);
		assertThat(Constants.NO_AUTHORISATION_MESSAGE,equalTo(homePage.getAuthorizationFailureMessage()));
	}

	@Test(priority=9)
	public void unallowedPageMouseToLogOff() {
		assertThat(homePage.mouseToLogOffLink(),equalTo(true));
	}

	@Test(priority=10)
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

		testParameters = new TestParameters();
		testParameters.setConnection(connection);
		testParameters.setProjectID(projectID);
		testParameters.setTestId(testID);
		
		JSONObject parameters = testParameters.getTestParameters();

		// DetailBeschrijvingenPage
        JSONObject queryStrings = (JSONObject) parameters.get("detail_page_query_strings");
		DetailBeschrijvingenPageQueryStringOk=queryStrings.get("ok").toString();
		DetailBeschrijvingenPageQueryStringNok=queryStrings.get("nok").toString();
		DetailBeschrijvingenPageExpectedResultNumber=Integer.valueOf(parameters.get("detail_page_ok_result_number").toString());

		// StartPage
        JSONObject pMenus = (JSONObject) parameters.get("menus");

        List<String> pMenuNames = Arrays.asList("SearchMenu", "AddMenu");

        for(String pMenuName : pMenuNames) {
            JSONObject pMenu = (JSONObject) pMenus.get(pMenuName);
            JSONArray pMenuItems = (JSONArray) pMenu.get("items");
            Iterator<String> iterator = pMenuItems.iterator();
            List<String> items = new ArrayList<String>();
            while (iterator.hasNext()) {
            	items.add(iterator.next().toString());
            }
    		MenuItems MenuItem = new MenuItems(items,Integer.valueOf(pMenu.get("end_point_sibling_count").toString()));
    		StartPageMenuItemsCollection.add(MenuItem);
        }

		// logged out
		LoggedOutUrl = config.getDomain() + parameters.get("url_logged_out").toString();

	}

}