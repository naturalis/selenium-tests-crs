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
import org.openqa.selenium.support.ui.ExpectedConditions;
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
	private int resultRows=0;
	private int resultRowsWithMultiMedia=0;
	private int resultRowsWithModeration=0;
	private int resultRowsWithAllTerms=0;
	private int resultRowsWithOneOfTerms=0;
	private int resultRowsMatchSpecificColumn=0;
	private int columnToCheck=-1;
	
	
	private List<String> searchTerms = new ArrayList<String>();

	@FindBy(id = "ctl00_ctl00_masterContent_SpnAantalDocumenten")
	private WebElement masterContent_SpnAantalDocumenten;

	@FindBy(id = "ctl00_ctl00_MasterSiteMapPath")
	private WebElement siteMapPath;

	@FindBy(id = "ctl00_ctl00_masterContent_ResultatenLijst")
	private WebElement resultatenLijst;
	
	@FindBy(id = "ctl00_ctl00_masterContent_dropdownlistResultTemplates")
	private WebElement resultatenTemplatesSelect;
	
	@FindBy(id = "ctl00_ctl00_masterContent_functionButtons_btn_ZoekvraagVersmallen")
	private WebElement iconSearchWithinResults;

	@FindBy(id = "ctl00_ctl00_masterContent_functionButtons_btn_ZoekvraagVerbreden")
	private WebElement iconAddToResults;
	
	
	public SimpleSearchResultsPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
		this.PageURL=Configuration.getDomain() + this.PageURL;
	}

	public void setPageUrlQueryString(String queryString) {
		this.PageUrlQueryString=queryString;
	}

	public void setSearchTerms(String term) {
		this.searchTerms.add(term);
	}

	public void setSearchTerms(List<String> terms) {
		this.searchTerms=terms;
	}

	public void setColumnToCheck(int n) {
		this.columnToCheck=n;
	}

	public int getColumnToCheck() {
		return this.columnToCheck;
	}

	public int getNumberOfFoundDocuments() {
		this.driver.switchTo().defaultContent();
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

	public int getResultRowWithWithAllTermsCount() {
		this.countResultRows();
		return this.resultRowsWithAllTerms;
	}
	
	public int getResultRowWithWithOneOfTermsCount() {
		this.countResultRows();
		return this.resultRowsWithOneOfTerms;
	}

	public int getResultRowsWithMatchInSpecificColumn(String columnHeader) {
		this.setColumnToCheck(this.getResultTableHeaders().indexOf(columnHeader));
		this.countResultRows();
		return this.resultRowsMatchSpecificColumn;
	}
	
	public List<Link> getBreadcrumbTrail() {
		List<WebElement> spans = siteMapPath.findElements(By.cssSelector("span"));
		List<Link> links = new ArrayList<Link>(); 
		for(WebElement e : spans) {
			if(!e.getText().trim().equals("›")) {
		        try {
					WebElement ele = e.findElement(By.cssSelector("a"));
					links.add(new Link(ele.getAttribute("href"),ele.getText(),null,null));
		        } catch (Exception exc) {
					links.add(new Link(null,e.getText(),null,null));
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
		Select select = new Select(resultatenTemplatesSelect);
		select.selectByVisibleText(name);
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
	
	public DetailBeschrijvingenPage clickDetailIcon(int rowNumber) {
        List<WebElement> rows = resultatenLijst.findElements(By.xpath("id('ctl00_ctl00_masterContent_ResultatenLijst')/tbody/tr"));
        for(WebElement row : rows) {
        	if (!row.getAttribute("class").toLowerCase().contains("header") && !row.getAttribute("class").toLowerCase().contains("pager")) {
	        	List<WebElement> images = row.findElements(By.cssSelector("img"));
	            for(WebElement image : images) {
	            	if (image.getAttribute("title").equals("Show details")) {
	            		if (rowNumber--==0) {
            				image.click();
	            			return new DetailBeschrijvingenPage(this.driver);
	            		}
	            	}
	            }
        	}
        }
		return null; 
	}

	public SimpleSearchPage clickIconSearchWithinResults() {
		iconSearchWithinResults.click();
		return new SimpleSearchPage(this.driver);
	}
	
	public SimpleSearchPage clickIconAddToResults() {
		iconAddToResults.click();
		return new SimpleSearchPage(this.driver);
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

		this.resultRows=0;
        this.resultRowsWithMultiMedia=0;
        this.resultRowsWithModeration=0;
        this.resultRowsWithAllTerms=0;
        this.resultRowsWithOneOfTerms=0;
        this.resultRowsMatchSpecificColumn=0;

		try {
	        List<WebElement> rows = resultatenLijst.findElements(By.xpath("id('ctl00_ctl00_masterContent_ResultatenLijst')/tbody/tr"));
	
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
	            	
	            	boolean hasAllTerms = true;
	            	boolean hasOneOfTerms = false;

	            	String temp="";
	            	List<WebElement> tds = row.findElements(By.cssSelector("td"));
	            	Integer n=0;
	            	for(WebElement td : tds) {
	            		temp+=" "+td.getText().toLowerCase();

	            		if (n.equals(this.getColumnToCheck()+1)) {
	    	            	for(String e : searchTerms) {
	    	            		if (td.getText().toLowerCase().contains(e.toLowerCase())) {
	    	            			this.resultRowsMatchSpecificColumn++;
	    	            		}
	    	            	}
	            		}
            			n++;
	            	}
	
	            	for(String e : searchTerms) {
	            		if (!temp.contains(e.toLowerCase())) {
	            			hasAllTerms = false;
	            		}
	            		else {
	            			hasOneOfTerms = true;
	            		}
	            	}
	
	            	if (hasAllTerms) {
	            		this.resultRowsWithAllTerms++;
	            	}

	            	if (hasOneOfTerms) {
	            		this.resultRowsWithOneOfTerms++;
	            	}
	            	
	            	this.resultRows++;
	            }
	        }
		} catch (Exception e) {
			//
		}
	}


	
}
