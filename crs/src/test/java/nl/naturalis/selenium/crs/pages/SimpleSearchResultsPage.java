package nl.naturalis.selenium.crs.pages;

import nl.naturalis.selenium.crs.configuration.*;
import nl.naturalis.selenium.crs.fragments.Link;
import nl.naturalis.selenium.crs.pages.StartPage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * CRS HomePage page model 
 *
 * @author      Maarten Schermer <maarten.schermer@naturalis.nl>
 * @version     1.0
 * @since       1.0
 */
public class SimpleSearchResultsPage extends AbstractPage {

	private WebDriver driver;

	private String PageName="SimpleSearchResultsPage";
	private String PageTitle="NCB PL omgeving - Results";
	private String PageURL="/AtlantisWeb/pages/medewerker/Zoekresultaten.aspx";
	private String PageUrlQueryString="";
	private int waitUntil = 0; // seconds
	private int resultRows=0;
	private int resultRowsWithMultiMedia=0;
	private int resultRowsWithModeration=0;

	@FindBy(id = "ctl00_ctl00_masterContent_SpnAantalDocumenten")
	private WebElement masterContent_SpnAantalDocumenten;

	@FindBy(id = "ctl00_ctl00_MasterSiteMapPath")
	private WebElement siteMapPath;

	@FindBy(id = "ctl00_ctl00_masterContent_ResultatenLijst")
	private WebElement resultatenLijst;
	
	@FindBy(id = "ctl00_ctl00_masterContent_dropdownlistResultTemplates")
	private WebElement resultatenTemplatesSelect;

	public SimpleSearchResultsPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
		this.PageURL=Configuration.getDomain() + this.PageURL;
	}

	public void setPageUrlQueryString(String queryString) {
		this.PageUrlQueryString=queryString;
	}

	public void setWaitUntil(Integer seconds) {
		this.waitUntil=seconds;
		this.driver.manage().timeouts().implicitlyWait(this.waitUntil, TimeUnit.SECONDS);
	}

	public int getNumberOfFoundDocuments() {
		return Integer.valueOf(this.masterContent_SpnAantalDocumenten.getText());
	}

	public int getResultRowCount() {
		this.countResultRows();
		return this.resultRows;
	}

	public int getResultRowWithMultiMediaCount() {
		this.countResultRows();
		return this.resultRowsWithMultiMedia;
	}
	
	public int getResultRowWithModerationCount() {
		this.countResultRows();
		return this.resultRowsWithModeration;
	}
	
	public List<Link> getBreadcrumbTrail() {
		List<WebElement> spans = siteMapPath.findElements(By.cssSelector("span"));
		List<Link> links = new ArrayList<Link>(); 
		for(WebElement e : spans) {
			if(!e.getText().trim().equals("›")) {
		        try {
					WebElement ele = e.findElement(By.cssSelector("a"));
					links.add(new Link(ele.getAttribute("href"),ele.getText()));
		        } catch (Exception exc) {
					links.add(new Link(null,e.getText()));
		        }
			}
		}
		return links;
	}

	public String getSelectedTemplateName() {
		Select select = new Select(resultatenTemplatesSelect);
		WebElement option = select.getFirstSelectedOption();
		return option.getText().trim();
	}
	
	public SimpleSearchResultsPage selectTemplateByName(String name) {

		/*
		 * we remove the resultlist from the current page so the driver will wait for the
		 * same element in the next page, and findElement() not be tripped by the one in the
		 * current page.    
		 */
		if (this.waitUntil>0) {
			JavascriptExecutor js = null;
			if (driver instanceof JavascriptExecutor) {
			    js = (JavascriptExecutor) driver;
			}
			js.executeScript("return document.getElementById('ctl00_ctl00_masterContent_ResultatenLijst').remove();");
		}

		Select select = new Select(resultatenTemplatesSelect);
		select.selectByVisibleText(name);

		if (this.waitUntil>0) {
			WebElement myDynamicElement = driver.findElement(By.id("ctl00_ctl00_masterContent_ResultatenLijst"));
		}
				
		return new SimpleSearchResultsPage(this.driver);
	}

	public List<String> getResultTableHeaders() {
		WebElement headerRow  = driver.findElement(By.cssSelector("tr.Header"));
		List<WebElement> headerCells = headerRow.findElements(By.cssSelector("th"));
		List<String> tableHeaders = new ArrayList<String>();
		for(WebElement headerCell : headerCells) {
			if (!headerCell.getText().trim().equals("")) {
				tableHeaders.add(headerCell.getText().trim());
			}
		}
		return tableHeaders;
    } 
	
	public String getCompletePageURL() {	
		if (this.PageUrlQueryString=="") {
			return this.PageURL; 
		}
		else {
			return this.PageURL+'?'+this.PageUrlQueryString;
		}
	}
	
	@Override
	public String getPageName() {
		return this.PageName;
	}

	@Override
	public String getPageTitle() {
		return this.PageTitle;
	}

	@Override
	public String getPageURL() {
		return this.PageURL;
	}

	private void countResultRows() {
        List<WebElement> rows = resultatenLijst.findElements(By.xpath("id('ctl00_ctl00_masterContent_ResultatenLijst')/tbody/tr"));
        
        this.resultRows=0;
        this.resultRowsWithMultiMedia=0;
        this.resultRowsWithModeration=0;

        for(WebElement row : rows) {
            if (!row.getAttribute("class").toLowerCase().contains("header") && !row.getAttribute("class").toLowerCase().contains("pager")) {
            	boolean hasMM = false;
            	boolean hasModeration = false;
            	List<WebElement> images = row.findElements(By.cssSelector("img"));
            	for(WebElement image : images) {
            		if (image.getAttribute("title").trim().equals("One multimedia object") || image.getAttribute("title").trim().equals("Multiple multimedia objects")) {
            			hasMM = true;
            		}
            		if (image.getAttribute("title").trim().equals("Document is moderated")) {
            			hasModeration = true;
            		}
            	}
            	if (hasMM) {
            		this.resultRowsWithMultiMedia++;
            	}
            	if (hasModeration) {
            		this.resultRowsWithModeration++;
            	}
            	this.resultRows++;
            }
        } 
	}


}
