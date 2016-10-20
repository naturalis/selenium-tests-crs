package nl.naturalis.selenium.crs.fragments;

import java.util.List;

public class MenuItems {

	private List<String> MenuItems;
	private Integer MaxLastItemAndSiblings;
	
	public MenuItems(List<String> list, Integer max) {
		this.setMenuItems(list);
		this.setMaxLastItemAndSiblings(max);
	}

	public List<String> getMenuItems() {
		return this.MenuItems;
	}
 
	public Integer getMaxLastItemAndSiblings() {
		return this.MaxLastItemAndSiblings;
	}
	
	private void setMenuItems(List<String> list) {
		this.MenuItems = list;
	}
 
	private void setMaxLastItemAndSiblings(Integer max) {
		this.MaxLastItemAndSiblings = max;
	}
}

