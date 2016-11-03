package nl.naturalis.selenium.crs.fragments;

public class EditIcon {
	private String src;
	private String alt;
	private String title;
	private String type;

	public EditIcon() {
		this.src = "";
		this.alt = "";
		this.title = "";
		this.type = "";
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
	public String getType() {
		return this.type;
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
	public void getType(String type) {
		this.type = type;
	}
	
	public String toString() {
		return "src=\"" + this.src + "\", alt=\"" + this.alt + "\", title=\"" + this.title + "\", type=\"" + this.type + "\"";
	}

}
