package no.abakus.bedcard.gui.panels.content;

public enum ContentType {
	registrering("Registrering"),
	registreringVenteliste("RegistreringVenteliste"),
	pameldte("Pameldte"),
	venteliste("Venteliste"),
	endreBruker("EndreBruker"),
	konfigurering("Konfigurering");
	
	private String name;
	
	private ContentType(String name) {
		this.name = name;
	}
	
	public String toString(){
		return name;
	}
}
