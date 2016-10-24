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


public class Test03 extends AbstractTest {

	private static String projectID = "CRS";
	private static String testID = "Test 03";

	
	private static MenuItems addMenu;
	private static HomePage homePage;
	private static StartPage startPage;
	private static DetailBeschrijvingenPage detailBeschrijvingenPage;
	private static String unitNumberCorrect;
	private static String unitNumberIncorrect;

		
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
		startPage = homePage.doLogin(config.getUsername(), config.getPassword());
		Assert.assertEquals(driver.getCurrentUrl(),startPage.getPageURL());
	}

	@Test(priority=3, dependsOnMethods = { "homePageDoLogin" })
	public void startPageTitle() {
		Assert.assertEquals(driver.getTitle().trim(),startPage.getPageTitle());
	}

	// mention removed spaces
	@Test(priority=4, dependsOnMethods = { "startPageTitle" })
	public void quickSearchIncorrect() {
		startPage.doDetailSearch(unitNumberIncorrect);
		String failMessageActual = startPage.getSearchFailureMessage().replace("  "," ");
		String failMessageExpected = Constants.SEARCH_FAILURE_MESSAGE.replace("%s",Configuration.getInstance()); 
		Assert.assertEquals(failMessageExpected,failMessageActual,"3.1.2. detail search error message");
		//startPage.quickSearchErrorPopupButtonClick();
	}
	
	@Test(priority=5, dependsOnMethods = { "quickSearchIncorrect" })
	public void quickSearchCorrect() {
		this.driver.navigate().refresh(); // startPage.quickSearchErrorPopupButtonClick(); isn't working....
		startPage.doDetailSearch(unitNumberCorrect);
		detailBeschrijvingenPage = new DetailBeschrijvingenPage(driver);
		detailBeschrijvingenPage.switchToMasterContentFrame();
		System.out.println(detailBeschrijvingenPage.getRegistrationNumber());
	}
	


	
	
/*



Simple search 
Voer een simple search uit zonder criteria om alle records binnen de beschrijvingssoort op te vragen. Lukt dit en is de tijd vergelijkbaar met huidige PROD?
Voer een search uit met filter. Worden alleen records met dat filter getoond?  Noteer de filter waarde en de benodigde tijd.
Voer een search uit waarin je kunt testen of het aanvinkvakje Incl. spelling variant naar behoren werkt. (voorbeeld zoeken op 'Guencko' moet opleveren Guincko en Ginkgo)
Staat bovenaan het juiste kruimelpad vermeld en kun je terug gaan via het kruimelpad naar de zoekopdracht?
Staat het gekozen filter nog vermeld in de zoekopdracht nadat je bent teruggekeerd vanuit het kruimeldpad?
Voer een search uit met  het vakje "Records with multimedia" aangevinkt Worden alleen de records met multimedia in het zoekresultaat opgenomen?
Wijzig het format voor de result template. Worden de kolommen nu aangepast aan het gekozen template?
Voer een search uit met  het vakje "Records without multimedia" aangevinkt. Worden alleen de records zonder multimedia in het zoekresultaat opgenomen?
Voor een search uit met het vakje "not moderated" aangevinkt. Wordt in het resultaat alleen niet gemodereerde records opgenomen?
Voor een search uit met het vakje "moderated" aangevinkt. Wordt in het resultaat alleen gemodereerde records opgenomen?
Voer een search uit met alleen zoeken òf verbatim, alleen thesaurus en beide. Is het zoekresultaat zoals verwacht? 
Voer een search uit met de filter not moderated, records with multimedia en thesaurus search uit (=verbatim). Krijg je als resultaat de niet gemodereerde records terug waaraan multimedia is gekoppeld en die niet (alleen!) gekoppeld zijn aan de thesaurus term?
Ga terug naar het zoekscherm en vul meer dan 1 zoekterm in. Klopt het dat het resultaat alleen records bevat die alle zoektermen bevat?
Ga naar Employee>Search> Storage units >vertebrates(/invertebrates). Vul een zoekwaarde in zonder een transformatiepad. Worden de storage units als resultaat weergegeven?
Wordt het kruimeldpad juist getoond? 
Ga via het kruimelpad terug naar de zoekopdracht. Wijzig nu het transformatiepad naar storageunit -> specimen en zoek opnieuw. Worden in het resultaatscherm de specimens getoond?   
Inperken zoekactie
"Voer een zoekactie uit.
Klik op het Search within icoontje in het scherm. 
Wordt vervolgens de simple search pagina getoond met laatste zoekopdracht?"
Vervang de laatste zoekopdracht door 1 of meerdere nieuwe termen en voer de zoekopdracht uit. Is het aantal resultaten lager zoals verwacht?
"Ga één stap terug in het kruimelpad, wijzig de zoekterm om het zoekresultaat in te perken, en voer de zoekactie uit.
Werkt dit?"
Klik op het Add to results icoontje. Wordt vervolgens de simple search pagina getoond met laatste zoekopdracht?
Vervang de laatste zoekopdracht met 1 of meerdere nieuwe termen en voer de zoekopdracht uit. Is het aantal resultaten hoger, zoals verwacht?
Start een nieuwe zoekopdracht en zoek naar twee registratienummers met daartussen  een OR. Is het resultaat van de zoekopdracht deze twee records?
Wordt de recorddata juist in het zoektemplate weergegeven?
Voer een zoekopdracht uit met de AND functie erin. Worden alleen  records weergegeven die beide waarden hebben?
Voer een zoekopdracht uit waarbij 1 letter uit je zoekopdracht is vervangen door een vraagteken, bv Neder?and. Werkt dit?
Voer een zoekopdracht uit waarbij er een min (geplaatst) wordt voor 1 van de termen. Klopt het dat die term niet terug te vinden is in het zoekresultaat?
Voer een zoekopdracht uit waarbij er een plus (geplaatst) wordt voor 1 van de termen. Klopt het dat die term terug te vinden is in elk zoekresultaat?

Advanced search
Voer een Advanced search uit met als Field Registrationnumber en Condition 'starts with' en geef een prefix op. Lukt dit en is de tijd vergelijkbaar met huidige PROD?
Voer een advanced search uit met 1 ingevuld veld en test het veld condition met een aantal mogelijkheden  (contains, starts with, equals etc.) . Werkt het veld condition naar behoren?
Voer een search uit met filter. Worden alleen records met dat filter getoond?  Noteer de filter waarde en de benodigde tijd.
Staat bovenaan het juiste kruimelpad vermeld en kun je terug gaan via het kruimelpad naar de zoekopdracht?
Staat het gekozen filter nog vermeld in de zoekopdracht nadat je bent teruggekeerd vanuit het kruimeldpad?
Wijzig het format voor de result template. Worden de kolommen nu aangepast aan het gekozen template?
Voer een search uit met  het vakje "Records with multimedia" aangevinkt Worden alleen de records met multimedia in het zoekresultaat opgenomen?
Voer een search uit met  het vakje "Records without multimedia" aangevinkt. Worden alleen de records zonder multimedia in het zoekresultaat opgenomen?
Voor een search uit met het vakje "not moderated" aangevinkt. Wordt in het resultaat alleen niet gemodereerde records opgenomen?
Voor een search uit met het vakje "moderated" aangevinkt. Wordt in het resultaat alleen gemodereerde records opgenomen?
Voer een zoekopdracht uit met de AND functie erin. Worden alleen  records weergegeven die beide waarden hebben?
Voer een zoekopdracht uit met de XOR functie erin, bv A XOR B. Bevat het resultaat van de zoekopdracht records die wel A of B hebben, maar niet A en B?
Voer een zoekopdracht uit met in het value veld twee woorden tussen dubbele aanhalingstekens, bv "nederlandse antillen". Geeft het zoekresultaat die records waarbij de twee woorden uit de string achter elkaar voorkomen?
Vernauw de zoekopdracht door gebruik te maken van het Search within icoontje in het scherm. Wordt na het klikken op dit icoontje de advanced search pagina getoond met laatste zoekopdracht?
Voeg een nieuwe regel met AND functie toe aan de advanced search. Is het aantal records in het resultaatscherm kleiner zoals verwacht?
Verbreed de zoekopdracht door gebruik te maken van het Add to results  icoontje. Wordt na het klikken op dit icoontje de advanced search pagina getoond met laatste zoekopdracht?
Voeg een regel met de OR functie toe. Is het aantal resultaten hoger, zoals verwacht?
Ga via het kruimelpad terug en pas de zoekopdracht aan door 1 van de zoekopdrachtregels te verwijderen door te klikken op het delete icooon. Wordt de regel verwijderd en is het zoekresultaat aangepast?.
Voer een advanced search uit met zoeken op een periode. Kies hiervoor een datum en conditie between. Wordt er een tweede veld getoond om de waarden in te voeren en klopt het zoekresultaat?
Voer een zoekopdracht waarbij in Field een veld wordt ingevuld dat gekoppeld is aan de thesaurus (bv Genus, Country). Selecteer onder condition Thesaurus. Worden de opties Include en Recursive getoond?
Vul nu een waarde in waarmee je in de thesaurus kunt zoeken (condition = thesuarus) en geef bij include aan dat je hierarchisch wil zoeken met child terms? Voer de zoekopdracht uit. Is het resultaat zoals verwacht.
Vink het veld recursive aan. Hiermee worden concepts die niet direct gerelateerd zijn aan het ingevoerde concept meegenomen in de zoekopdracht. Werkt dit?

Form search 
"Bevat het Form search scherm de filters voor?
- moderated/not moderated
- records with/without multimedia
- verbatim/thesaurus/both
- filter voor een thesarus veld
-een vinkvakje voor incl. spelling variants"
Vul 1 of meerdere termen in de velden in en klik op zoeken. Werkt dit?
Ga terug naar het Form search scherm via het kruimelpad. Wordt de laatste zoekopdracht getoond in het Form scherm?

Complex search
"Voer de volgende zoekopdracht uit:
SELECT s.Xmlbeschrijvingid
FROM   NCRS_SPECIMEN s
INNER JOIN NCRS_DATAGROUP dg ON dg.SPECIMENID = s.ID
INNER JOIN NCRS_DETERMINATION d ON d.DATAGROUP = dg.ID 
--INNER JOIN NCRS_HIGHERNAME h ON h.determinationid = d.ID
WHERE  (s.registrationnumber LIKE 'RMNH%' OR s.registrationnumber LIKE 'ZMA%')
AND    (s.basis_of_record = 'VirtualSpecimen' OR (s.basis_of_record = 'PreservedSpecimen' AND s.suffix IS NULL))
--AND    (h.RANKORCLASSIFICATION = 'family' AND h.NAME = 'Vespertilionidae') 
--AND    (h.RANKORCLASSIFICATION = 'order' AND h.NAME = 'Chiroptera')
AND    d.GENUS = 'Plecotus'
AND    d.EPITHET = 'auritus'
AND    REGEXP_LIKE(s.registrationnumber, '^(RMNH|ZMA).MAM.(0|1|2|3|4|5|6|7|8|9)+$')
"

Ga terug naar het complex search scherm via het kruimelpad. Bevat het complex scherm de laatste zoekopdracht?


Zoekresultaat 
"Bevat het zoekresultaatscherm de volgende onderdelen en werken de mouse over teksten?
- links in de pagina, results per page en sort
- aantal gevonden documenten gevolgd door de zoekterm(en) 
- global selection en geselecteerde documenten iconen
- een rij met iconen, zie veld screenshot
- Selectie voor veschillende result templates"
"Staat in het grijze blok bovenin en onderin van het zoekresultaatscherm:
- links vier iconen met daarin de verschillende views
- pagina weergave 1 tm 10
de iconen >, >>, >>>>
- Page 1 of..
- Page gevolgd door een invulvak en een pijltje
en werken de bijbehorende mouse over teksten?"
Pas het aantal resultaten per pagina aan en klik op het pijltje erachter. Wijzigt het aantal resultaten dat zichtbaar is in het resultaatscherm?
Klopt het aantal pagina's voor het aantal zoektresultaten?
Klopt het dat achter een zoekresultaat van meer dan 10.000 results staat vermeld: "Sorting is not possible with more than 10000 results"? 
Klik op sort results. Verschijnt er een pop-up met daarin Column en Direction?
Zijn de resultaatvelden beschikbaar in de uitklaplijst achter Column
Voeg een kolom toe en maak de keuze oplopend of aflopend sorteren en druk op het pijltje. Wordt het zoekresultaat hierop juist aangepast?
Staat nu achter Sort volgens welke kolom het zoekresultaat is gesorteerd?
Klik opnieuw achter Sort en pas de sortering aan door nu meer dan 1 kolom te gebruiken om te sorteren. Wordt het zoekresultaat hierop juist aangepast en staat achter soort juist beschreven hoe het resultaat is gesorteerd?
Klik op een kolom in het zoekresultaat. Wordt het zoekresultaat aangepast en gesorteerd op deze kolom en verschijnt achter Sort de naam van de kolom met Ascending?
Ga naar een ander paginanummer door te klikken op een pagina nummer, werkt dit?
Ga naar de volgende pagina door op het volgende icoon te klikken (>),werkt dit en wordt ook de paginanummering aangepast?
Ga naar de laatste pagina door op het laatste pagina icoon te klikken (>> >>),werkt dit en wordt ook de paginanummering aangepast?
Ga naar een willekeurige pagina door in het vakje achter page een nummer in te typen en op het pijltje achter het vak te klikken. Werkt dit en wordt ook de paginanummering aangepast?
Vul in het vakje achter Page een paginanummer in dat hoger is dan het laatste paginanummer en voer uit. Springt het CRS dan naar de laatste pagina?
Ga naar het icoon <<. Toont de mouseover het getal van de huidige pagina min 10? Bij uitvoer wordt er ook naar deze pagina gegaan?
Wijzig het format voor de result template naar een niet voorkeurs template (user preferences). Worden de kolommen nu aangepast aan het gekozen template?
Ga vanuit het zoekresultaat naar een detailscherm van een specimen. Bevat het kruimelpad de elementen Search.. en Results?
Ga terug via het kruimelpad naar Results. Klopt het dat het resultaatscherm getoond wordt volgens het laatst gekozen template?
Voer een nieuwe zoekopdracht uit. Wordt dit getoond in het laatst gekozen template?
Selecteer een aantal records. Wordt rechtsboven het aantal geselecteerde records juist getoond?
Onthoud de records die je hebt geselecteerd door de registratienummers ergens te noteren. Verander de moderated status van de geselecteerde documents. Wordt dit juist weergegeven? Zet daarna de moderated status weer terug.
Wissel tussen de verschillende views, list, gallery en systematic en edit grid van het resultaatscherm door op de iconen linksboven in het reultaat blok te klikken, zie ook printscreen. Werkt dit?
"Verwijder een record mbv van het prullenbakje rechts in beeld.
1. verdwijnt het record uit de resultatenlijst? (Let op: bij meer dan 1 pagina zoekresultaten, mag het ook niet naar een andere pagina verschuiven)"
"Verwijder een record mbv van het prullenbakje rechts in beeld. 
2. is het record ook verwijderd? (Controleer door het op te zoeken mbv het registratienummer)"

Stored searches
Maak een zoekopdracht en klik in het resultaatscherm op het save icoon Komt er een pop up waarin er een omschrijving kan worden gegeven en een vink vakje om de opgeslagen zoekopdracht wel/niet openbaar te maken. Geef een omschrijving en sla de zoekopdracht niet openbaar op. Lukt dit? 
Ga naar Employee> Stored search. Kun je je zojuist opgeslagen zoekopdracht hier terug vinden? Werkt de paginering binnen de lijst van stored searches?
Kun je de omschrijvging en de openbaarheid van de zoekopdracht aanpassen?
Klik op het Edit search icoon. Verschijnt nu het zoekopdracht scherm met de juiste query?
Ga terug via het menu Employee> Stored searches en open het resultaat van de zojuist opgeslagen stored search door op het blauwe pijltje te klikken. Lukt dit?
Pas de zoekopdracht aan door te verbreden of te versmallen. Lukt dit?
Ga naar stored searches en verwijder de zojuist aangemaakte stored search middel het delete icoon. Krijg je de melding: "Wilt u deze echt verwijderen?"
Klik op annuleren. Verdwiijnt de melding, maar bestaat de stored search nog steeds?
Klik op ok, is de stored search nu uit de lijst met werksets verdwenen?

Werksets
Selecteer een aantal records en kies 'add to working set'. Opent er nu een pop up met de mogelijkheid om te kiezen voor een bestaande workingset of een nieuwe workingset. Kies new working set en vink het vakje not public aan. Voeg de geselecteerde documenten aan de werkset toe en sla op. Krijg je de melding: "The documents were added to the working set"?
Ga naar Employee> Workingsets en zoek de werkset terug. Kun je de werkset terugvinden? Werkt de paginering binnen de lijst van werksets?
Kun je de naam en de openbaarheid van de werkset aanpassen door achter de werkset op het potlood icoon te klikken?
Open de werkset door op het blauwe pijltje te klikken. Zijn de geselecteerde documenten terug te vinden in de werkset?
Voer een nieuwe zoekopdracht uit en voeg een record to aan de werkset via het detailscherm middels het icoontje 'Add to working set'. Lukt dit en is het record terug te vinden in de werkset?
Verwijder een record uit de werkset vanuit het zoekresultaatscherm. Lukt dit?
Ga naar werksets en verwijder de zojuist aangemaakte werkset middels het delete icoon. Krijg je de melding: "Wilt u deze echt verwijderen?"
Klik op annuleren. Verdwijnt de melding, maar bestaat de werkset nog steeds?
Klik op ok, is de werkset nu uit de lijst met werksets verdwenen?

Thesaurus
Ga naar Administrator>Thesaurus>Thesaurus browser en voer in het search vakje een waarde in en klik op search. Wordt het juiste resultaat onder het vak getoond?
Klik op 1 van de zoekresultaten. Wordt het Thesaurus concept getoond?

Global selection 
Selecteer een aantal gevonden records en druk op de knop 'replace global selection with selected documents'. Kijk vervolgens onder Employee > Selection > Documents of alle documenten die je hebt toegevoegd ook daadwerkelijk hierin staan. 
Controleer of de records in alle views goed worden weergegeven (List view; Gallery; Systematic view)



 * */
	

	private static void initializeTestParameters() {
		unitNumberCorrect="ZMA.MAM.5179";
		unitNumberIncorrect="incorrect number";
	}


}