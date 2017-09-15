package nl.naturalis.selenium.crs.fragments;

import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class StorageLocations {
	
	private WebDriver driver;
	private Actions action;

	public StorageLocations() {
		this.driver = null;
	}
	
	public StorageLocations(WebDriver driver) {
		this.driver = driver;
		this.action = new Actions(driver);
	}

	public void setDriver(WebDriver driver) {
		this.driver = driver;
		this.action = new Actions(driver);
	}
	
	public String getTitle() {
		return driver.getTitle();
	}
	
	public void closeWindow() {
		driver.close();
	}
	
	public void clickDW() {
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		WebElement DW = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(text(), ' DW')]/parent::div/span[@class='rtPlus']")));
		DW.click();
	}

	public void clickDW_E() {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Boolean branchAvailable = driver.findElements(By.xpath("//span[contains(text(), ' DW.E')]/parent::div/span[@class='rtPlus']")).size() > 0;
		if (!branchAvailable) {
			this.clickDW();
		}
		WebElement DW_E = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(text(), ' DW.E')]/parent::div/span[@class='rtPlus']")));
		DW_E.click();
	}

	public void clickDW_E_01() {
//		try {
//			Thread.sleep(1500);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}

		Boolean branchAvailable = driver.findElements(By.xpath("//span[contains(text(), ' DW.E.01')]/parent::div/span[@class='rtPlus']")).size() > 0;
		if (!branchAvailable) {
			this.clickDW_E();
		}
		WebElement DW_E_01 = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(text(), ' DW.E.01')]/parent::div/span[@class='rtPlus']")));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", DW_E_01);
		DW_E_01.click();
	}

	public void clickDW_E_01_017() {
//		try {
//			Thread.sleep(1500);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}

		Boolean branchAvailable = driver.findElements(By.xpath("//span[contains(text(), ' DW.E.01.017')]/parent::div/span[@class='rtPlus']")).size() > 0;
		if (!branchAvailable) {
			this.clickDW_E_01();
		}
		WebElement DW_E_01_017 = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(text(), ' DW.E.01.017')]/parent::div/span[@class='rtPlus']")));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", DW_E_01_017);
		DW_E_01_017.click();
	}

	public List<WebElement> getBoomPagerLinks() {
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[text()=' DW.E.01.017']/parent::div/parent::li/parent::ul/*//ul[@class='rtUL']")));
		return driver.findElements(By.xpath("//a[@class='boomPagerLink']"));
	}
	
	public List<WebElement> getStorageLocations() {
		List<WebElement> listStorageLocations = driver.findElements(By.xpath("//li[contains(@class,'rtLI')]/div/span[@class='rtIn']"));
		return listStorageLocations;
	}

	public String getCurrentPage() {
		WebElement currentPage = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@class='rtIn']/b")));
		return currentPage.getText();
	}
	
	public Boolean searchBoxAvailable() {
	    try {
	    	driver.findElement(By.name("tree_searcher"));
	    	} catch (NoSuchElementException e) {
	    		return false;
	    		}
	    return true;
	    }
	
	public Boolean selectStorageLocation() {
		// When there is a page, this method selects the 10th storage location
		// Actions action = new Actions(driver);
		WebElement selectLocation = driver.findElement(By.xpath("//a[@class='boomPagerLink']/../../../../li[10]/div/span[@class='rtIn']"));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", selectLocation);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
//		selectLocation = driver.findElement(By.xpath("//a[@class='boomPagerLink']/../../../../li[10]/div/span[@class='rtIn']"));
//		selectLocation.click();
		action.doubleClick(selectLocation).perform();
		if (selectLocation.getCssValue("background-color").equals("rgba(130, 130, 130, 1)")) {
			return true;
		} else {
			return false;
		}
	}
	
	public void koppelStorageLocation() {
		Set<String> AllWindowHandles = driver.getWindowHandles();
		String mainWindow = (String) AllWindowHandles.toArray()[0];
		// String popupWindow = (String) AllWindowHandles.toArray()[1];	

		// Clicking the koppel button, will also kill the popup window
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.id("img_Koppel")));
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		driver.findElement(By.id("img_Koppel")).click();
		
		// So we need to switch back to the main window
		driver.switchTo().window(mainWindow);
	}
}
