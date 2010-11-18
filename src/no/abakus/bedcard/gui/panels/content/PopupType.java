package no.abakus.bedcard.gui.panels.content;

public enum PopupType {
	velkommen("Velkommen"),
	twoLine("TwoLine");
	
	private String name;
	
	private PopupType(String name) {
		this.name = name;
	}
	
	public String toString(){
		return name;
	}
}
