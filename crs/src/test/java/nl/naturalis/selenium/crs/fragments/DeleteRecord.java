package nl.naturalis.selenium.crs.fragments;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import nl.naturalis.selenium.crs.pages.StartPage;
import nl.naturalis.selenium.crs.pages.DeletedDocuments;

public class DeleteRecord {

	private WebDriver driver;
	private String registrationNumber;
	private String mainCollection;
	private static StartPage startPage;
	private static DeletedDocuments deletedDocumentsPage;
	
	public DeleteRecord() {
		this.driver = null;
	}

	public DeleteRecord(WebDriver driver) {
		this.driver = driver;
	}
	
	public void byRegistrationNumber(String registrationNumber) {		
		// Search record
		this.registrationNumber = registrationNumber;
		System.out.println("reg: " + this.registrationNumber);
		driver.get(startPage.getPageURL());
		driver.switchTo().defaultContent();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		WebElement detailSearch = wait
				.until(ExpectedConditions.presenceOfElementLocated(By.id("ctl00_QuickSearchTextBox")));
		detailSearch.sendKeys(this.registrationNumber);
		detailSearch.sendKeys(Keys.ENTER);
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
		WebElement collectionTab = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("ctl00_masterContent_UpdatePanelTab1")));
		this.mainCollection = collectionTab.getText();

		// Delete record
		driver.findElement(By.id("btn_delete")).click();
		driver.switchTo().frame(0); // This frame now contains one frame for user interaction
		driver.switchTo().frame(0); // and this contains the delete button
		driver.findElement(By.id("btn_verwijder_client")).click();

		Alert deleteAlert = driver.switchTo().alert();
		deleteAlert.accept();
		// Wait for the record to be deleted before continuing
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//*[@title='detail pagina publiek']")));
		
		// Remove the record from the deleted documents
		driver.get(deletedDocumentsPage.getPageURL());
		deletedDocumentsPage = new DeletedDocuments(driver);
		deletedDocumentsPage.selectFormulierByName(this.mainCollection);
		deletedDocumentsPage.removeRecord(this.registrationNumber);
	}
}
