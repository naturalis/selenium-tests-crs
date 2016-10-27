package nl.naturalis.selenium.crs.fragments;

public class EditIcon {
	private String src;
	private String alt;
	private String title;

	public EditIcon() {
		this.src = "";
		this.alt = "";
		this.title = "";
	}
	
	public String getSrc() {
		return this.src;
	}
	public String getAlt() {
		return this.alt;
	}
	public String getTitle() {
		return this.title;
	}
	
	public void getSrc(String src) {
		this.src = src;
	}
	public void getAlt(String alt) {
		this.alt = alt;
	}
	public void getTitle(String title) {
		this.title = title;
	}

	public String toString() {
		return "src=\"" + this.src + "\", alt=\"" + this.alt + "\", title=\"" + this.title + "\"";
	}

}
