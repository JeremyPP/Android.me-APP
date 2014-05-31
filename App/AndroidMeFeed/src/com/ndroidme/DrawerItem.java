package com.ndroidme;

public class DrawerItem {

	private boolean bold;
	private String text;
	private int icon;
	
	public DrawerItem(String text, boolean bold, int icon) {
		this.bold = bold;
		this.text = text;
		this.icon = icon;
	}
	
	public boolean isBold() {
		return bold;
	}
	public void setBold(boolean bold) {
		this.bold = bold;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getIcon() {
		return icon;
	}
	public void setIcon(int icon) {
		this.icon = icon;
	}
	
}
