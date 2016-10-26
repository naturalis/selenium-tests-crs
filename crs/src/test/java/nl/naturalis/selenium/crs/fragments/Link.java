package nl.naturalis.selenium.crs.fragments;

public class Link {

	private String href;
	private String text;
	private String target;
	
	public Link(String href, String text) {
		this.setHref(href);
		this.setText(text);
		this.setTarget(target);
	}
	
	public void setHref(String item) {
		this.href=item;
	}

	public void setText(String item) {
		this.text=item;
	}

	public void setTarget(String item) {
		this.target=item;
	}
	
	public String getHref() {
		return this.href;
	}
	
	public String getText() {
		return this.text;
	}
	
	public String getTarget() {
		return this.target;
	}
	
}
