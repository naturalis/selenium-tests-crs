package nl.naturalis.selenium.crs.fragments;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class InputGroup {

	private WebDriver driver;
	private String id;
	private WebElement inputGroup;
	private WebElement inputField;
	private WebElement binIcon;
	private WebElement tIcon;
	private WebElement conceptLink;
	private WebElement overlay;
	private WebElement errorIcon;

	public InputGroup() {
		this.driver = null;
		this.id = "";
		this.inputGroup = null;
		this.inputField = null;
		this.binIcon = null;
		this.tIcon = null;
		this.conceptLink = null;
		this.overlay = null;
		this.errorIcon = null;
	}

	/**
	 * An input group is enclosed by the table cell the input field is located.
	 *  
	 */
	public InputGroup(WebDriver driver, String id) {
		this.driver = driver;
		this.driver.switchTo().defaultContent();
		this.driver.switchTo().frame("iframe_1");

		this.id = id;
		this.inputField = driver.findElement(By.id(id));
		this.inputGroup = driver.findElement(By.xpath("//input[@id='" + id + "']/parent::*"));
		this.tIcon = driver.findElement(By.xpath("//input[@id='" + id + "']/parent::*//span/input[1]"));
		this.binIcon = driver.findElement(By.xpath("//input[@id='" + id + "']/parent::*//span/input[2]"));
		this.conceptLink = driver.findElement(By.xpath("//input[@id='" + id + "']/parent::*//span/a[@class='conceptLink']"));
	}

	public String toString() {
		return this.inputGroup.getAttribute("outerHTML");
	}
	
	public void enterText(String text) {
		this.inputField.sendKeys(text);
	}

	public String getValue() {
		return this.inputField.getText();
	}
	
	public String getLinkedConcept() {
		return this.conceptLink.getText();
	}
	
//	public String getWarningErrorIcon() {
//		this.errorIcon = driver.findElements(By.xpath("//input[@id='" + id + "']/parent::*/img"));
//		if (this.errorIcon.size() > 0) {
//			return this.errorIcon.get(0).getAttribute("alt");
//		} else {
//			return "No error";
//		}
//	}

	public String getWarningErrorIcon() {
		this.errorIcon = driver.findElement(By.xpath("//input[@id='" + id + "']/parent::*/img"));
		return this.errorIcon.getAttribute("alt");
	}

	public String getSrcErrorIconSrc() {
		this.errorIcon = driver.findElement(By.xpath("//input[@id='" + id + "']/parent::*/img"));
		return this.errorIcon.getAttribute("src");
	}

	public String getBinImageTitle() {
		this.errorIcon = driver.findElement(By.xpath("//input[@id='" + id + "']/parent::*/img"));
		return this.binIcon.getAttribute("title");
	}
	
	public String getThesaurusConcept() {
		this.conceptLink = driver.findElement(By.xpath("//input[@id='" + id + "']/parent::*//span/a[@class='conceptLink']"));
		return conceptLink.getText();
	}
	
	public void clickInputField() {
		this.inputField.click();
	}
	
	public void clickBin() {
		binIcon.click();
	}
	
	public void clickThesaurus() {
		this.tIcon.click();
	}

	public void enterThesaurusValue(String inputText, String thesaurusText) {
		this.inputField.sendKeys(inputText);
		this.inputField.sendKeys(Keys.DOWN);
		this.driver.switchTo().defaultContent();
		this.driver.switchTo().frame("ctl00_masterContent_iframe_1");
		this.driver.findElement(By.partialLinkText(thesaurusText)).click();
		this.driver.switchTo().defaultContent();
		this.driver.switchTo().frame("iframe_1");
	}
	
	public void clickOverlay() {
		// this.driver.switchTo().frame("ctl00_masterContent_iframe_1");
		// this.driver.switchTo().defaultContent();
		this.overlay = driver.findElement(By.className("ui-widget-overlay"));
		this.overlay.click();
	}
	
}