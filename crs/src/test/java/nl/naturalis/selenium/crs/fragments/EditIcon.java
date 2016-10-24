package nl.naturalis.selenium.crs.fragments;

public class EditIcon {

//	private static String src;
//	private static String alt;
//	private static String title;
	private String src;
	private String alt;
	private String title;

	public EditIcon() {
		this.src = "";
		this.alt = "";
		this.title = "";
	}
	
//	public static String getSrc() {
//		return src;
//	}
//	public static String getAlt() {
//		return alt;
//	}
//	public static String getTitle() {
//		return title;
//	}

	public String getSrc() {
		return this.src;
	}
	public String getAlt() {
		return this.alt;
	}
	public String getTitle() {
		return this.title;
	}

//	public static void setSrc(String item) {
//		src=item;
//	}
//	public static void setAlt(String item) {
//		alt=item;
//	}
//	public static void setTitle(String item) {
//		title=item;
//	}
	
	public void setSrc(String src) {
		this.src = src;
	}
	public void setAlt(String alt) {
		this.alt = alt;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public String toString() {
		return "src=\"" + this.src + "\", alt=\"" + this.alt + "\", title=\"" + this.title + "\"";
	}
	
//	public static void EditIcon() {
//
//	}

	
	
}
