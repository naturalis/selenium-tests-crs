package nl.naturalis.selenium.crs.fragments;

import java.util.ArrayList;
import java.util.List;

public class TestCase {

	private String identifier;
	private String collection;
	private String variant;
	private String query;
	private String form;
	private List<String> testsToRun = new ArrayList<String>();
	private List<Link> contextLinks = new ArrayList<Link>();
	
	public TestCase(String identifier, String collection, String variant, String query, String form) {
		this.setIdentifier(identifier);
		this.setCollection(collection);
		this.setVariant(variant);
		this.setQuery(query);
		this.setForm(form);
	}
	
	public void setIdentifier(String item) {
		this.identifier=item;
	}

	public void setCollection(String item) {
		this.collection=item;
	}

	public void setVariant(String item) {
		this.variant=item;
	}

	public void setQuery(String item) {
		this.query=item;
	}
	
	public void setForm(String item) {
		this.form=item;
	}

	public void addTest(String item) {
		this.testsToRun.add(item);
	}

	public void addContextLink(Link item) {
		this.contextLinks.add(item);
	}

	public boolean hasTest(String item) {
		return this.testsToRun.contains(item);
	}

	public List<String> getTests() {
		return this.testsToRun;
	}

	public String getIdentifier() {
		return this.identifier;
	}
	
	public String getCollection() {
		return this.collection;
	}
	
	public String getForm() {
		return this.form;
	}
	
	public String getQuery() {
		return this.query;
	}

	public String getVariant() {
		return this.variant;
	}

	public List<Link> getContextLinks() {
		return this.contextLinks;
	}

	public String getIdString() {
		return
			"collection: " + this.getCollection() + " / " +
			"variant: " + this.getVariant() + " / " +
			"query: " + this.getQuery() + " / " +
			"form: " + this.getForm();
	}
	
}
