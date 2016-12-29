package nl.naturalis.selenium.crs.fragments;

import java.util.ArrayList;
import java.util.List;

public class TestCase {

	private String collection;
	private String variant;
	private String query;
	private String page;
	private static List<String> testsToRun = new ArrayList<String>();
	
	public TestCase(String collection, String variant, String query, String form) {
		this.setCollection(collection);
		this.setVariant(variant);
		this.setQuery(query);
		this.setPage(form);
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
	
	public void setPage(String item) {
		this.page=item;
	}

	public void addTest(String item) {
		this.testsToRun.add(item);
	}

	public boolean hasTest(String item) {
		return this.testsToRun.contains(item);
	}

	public String getCollection() {
		return this.collection;
	}
	
	public String getPage() {
		return this.page;
	}
	
	public String getQuery() {
		return this.query;
	}

	public String getVariant() {
		return this.variant;
	}
	

	public String getIdString() {
		return
			"collection: " + this.getCollection() + " / " +
			"page: " + this.getPage() + " / " +
			//"query: " + this.getQuery() + " / " +
			"form: " + this.getVariant();
	}
	
}
