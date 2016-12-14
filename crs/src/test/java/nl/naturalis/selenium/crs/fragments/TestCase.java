package nl.naturalis.selenium.crs.fragments;

import java.util.ArrayList;
import java.util.List;

public class TestCase {

	private String collection;
	private String form;
	private String query;
	private static List<String> testsToRun = new ArrayList<String>();
	
	public TestCase(String collection, String form, String query) {
		this.setCollection(collection);
		this.setForm(form);
		this.setQuery(query);
	}
	
	public void setCollection(String item) {
		this.collection=item;
	}

	public void setForm(String item) {
		this.form=item;
	}

	public void setQuery(String item) {
		this.query=item;
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
	
	public String getForm() {
		return this.form;
	}
	
	public String getQuery() {
		return this.query;
	}
	
}
