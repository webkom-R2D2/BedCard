package no.abakus.bedcard.gui.panels.content.konfigurering;

import java.awt.GraphicsDevice;

public class Skjerm {
	private int nummer;
	private GraphicsDevice device;
	
	public Skjerm(int nummer, GraphicsDevice device) {
		super();
		this.nummer = nummer;
		this.device = device;
	}
	
	public String toString(){
		return "Skjerm " + nummer;
	}
}
