package no.abakus.bedcard.gui.panels.content;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import no.abakus.bedcard.gui.BedCard;
import no.abakus.bedcard.gui.panels.BedCardPanel;
import no.abakus.bedcard.gui.panels.content.konfigurering.Konfigurering;
import no.abakus.bedcard.gui.panels.content.pameldte.Pameldte;
import no.abakus.bedcard.gui.panels.content.registrering.Registrering;
import no.abakus.bedcard.gui.panels.content.registrering.RegistreringVenteliste;
import no.abakus.bedcard.gui.panels.content.venteliste.Venteliste;
import no.abakus.bedcard.logic.Logic;

public class ContentPanel extends BedCardPanel {
	private Logic logic;
	private Registrering registrering;
	private RegistreringVenteliste registreringVenteliste;
	private EndreBruker endreBruker;
	private Konfigurering konfigurering;
	private Pameldte pameldte;
	private Venteliste venteliste;

	public ContentPanel(BedCard bedcard, Logic logic) {
		super(bedcard);
		this.logic = logic;
		width = bedcard.getDrawWidth();
		height = bedcard.getDrawHeight()-BedCard.BottomStatusPanelHeight-BedCard.MenuPanelHeight;
		setPanelSizes();
		setLayout(new CardLayout(0,0));
		registrering = new Registrering(bedcard, logic, this);
		add(registrering, ContentType.registrering.toString());
		registreringVenteliste = new RegistreringVenteliste(bedcard, logic, this);
		add(registreringVenteliste, ContentType.registreringVenteliste.toString());
		endreBruker = new EndreBruker(bedcard, logic, this);
		add(endreBruker, ContentType.endreBruker.toString());
		konfigurering = new Konfigurering(bedcard, logic, this);
		add(konfigurering, ContentType.konfigurering.toString());
		pameldte = new Pameldte(bedcard, logic, this);
		add(pameldte, ContentType.pameldte.toString());
		venteliste = new Venteliste(bedcard, logic, this);
		add(venteliste, ContentType.venteliste.toString());
		((CardLayout)getLayout()).show(this, ContentType.konfigurering.toString());
		
	}
	public void paint(Graphics g){
		Graphics2D g2d = (Graphics2D)g;
		g2d.setColor(Color.white);
		g2d.fillRect(0,0,width, height);
   	 	paintChildren(g2d);
    }
	public Logic getLogic() {
		return logic;
	}
	public Registrering getRegistrering() {
		return registrering;
	}
	public RegistreringVenteliste getRegistreringVenteliste() {
		return registreringVenteliste;
	}
	public EndreBruker getEndreBruker() {
		return endreBruker;
	}
	public Konfigurering getKonfigurering() {
		return konfigurering;
	}
	public Pameldte getPameldte() {
		return pameldte;
	}
	public Venteliste getVenteliste() {
		return venteliste;
	}
	
}
