package no.abakus.bedcard.gui.panels.content.list;

import java.awt.Color;

import javax.swing.JList;

public class BedCardList extends JList {
	private Color alternateForeground;
	private Color alternateBackground;
	private Color selectionAlternateForeground;
	private Color selectionAlternateBackground;
	public BedCardList() {
		alternateBackground = new Color(220,220,220);
		alternateForeground = new Color(0,0,0);
		selectionAlternateForeground = new Color(0,0,0);
		selectionAlternateBackground = new Color(184,207,229);
		setForeground(new Color(0,0,0));
		setBackground(new Color(227,227,227));
		setSelectionForeground(new Color(0,0,0));
		setSelectionBackground(new Color(196,212,228));	
	}
	public Color getAlternateForeground() {
		return alternateForeground;
	}
	public void setAlternateForeground(Color alternateForeground) {
		this.alternateForeground = alternateForeground;
	}
	public Color getAlternateBackground() {
		return alternateBackground;
	}
	public void setAlternateBackground(Color alternateBackground) {
		this.alternateBackground = alternateBackground;
	}
	public Color getSelectionAlternateForeground() {
		return selectionAlternateForeground;
	}
	public void setSelectionAlternateForeground(Color selectionAlternateForeground) {
		this.selectionAlternateForeground = selectionAlternateForeground;
	}
	public Color getSelectionAlternateBackground() {
		return selectionAlternateBackground;
	}
	public void setSelectionAlternateBackground(Color selectionAlternateBackground) {
		this.selectionAlternateBackground = selectionAlternateBackground;
	}

}
