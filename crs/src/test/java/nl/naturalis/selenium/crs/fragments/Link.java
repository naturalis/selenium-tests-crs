package nl.naturalis.selenium.crs.fragments;

public class Link {

	private String href;
	private String text;
	private String target;
	private String additionalInfo;
	
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
	
	public void setAdditionalInfo(String item) {
		this.additionalInfo=item;
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

	public String getAdditionalInfo() {
		return this.additionalInfo;
	}

}
