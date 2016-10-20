package nl.naturalis.selenium.crs.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import nl.naturalis.selenium.crs.configuration.Configuration;

public class DetailBeschrijvingenPage extends AbstractPage {

	private WebDriver driver;

	private String PageName="DetailBeschrijvingenPage";
	private String PageTitle="NCB PL omgeving - ";
	private String PageURL="/AtlantisWeb/pages/medewerker/DetailBeschrijvingen.aspx";
	private String PageUrlQueryString="";

	@FindBy(id = "ctl00_masterContent_tbl_navigatie")
	private WebElement resultNumberTable;

	@FindBy(css = "tbody>tr>td>span>span.huidige")
	private WebElement numberSpan;

	@FindBy(id = "ctl00_masterContent_ddl_fomulieren")
	private WebElement formulierenSelect;

	
	public DetailBeschrijvingenPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
		this.PageURL=Configuration.getDomain() + this.PageURL;
	}
	
	public void setPageUrlQueryString(String queryString) {
		this.PageUrlQueryString=queryString;
	}

	public Integer getNumberOfResults() {
		return Integer.parseInt(numberSpan.getText().trim());	
	}
	
	public List<WebElement> clickFormulierenSelect() {
		Actions action = new Actions(driver);
		action.moveToElement(this.formulierenSelect).perform();
		this.formulierenSelect.click();
		Select select = new Select(this.formulierenSelect);
		return select.getOptions();
	}

	public void clickFormulierenSelectOption(String optionLabel) {
		Select select = new Select(this.formulierenSelect);
		select.selectByVisibleText(optionLabel);
		System.out.println(select.getFirstSelectedOption().getText());
		select.getFirstSelectedOption().click();
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

}
