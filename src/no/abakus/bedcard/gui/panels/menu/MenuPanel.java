package no.abakus.bedcard.gui.panels.menu;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

import no.abakus.bedcard.gui.BedCard;
import no.abakus.bedcard.gui.panels.BedCardPanel;
import no.abakus.bedcard.logic.Logic;

public class MenuPanel extends BedCardPanel {
	private Image menuBackground;
	BedCardButton btnRegistrering;
	BedCardButton btnPameldte;
	BedCardButton btnVenteliste;
	BedCardButton btnVentelisteToggle;
	BedCardButton btnEndreBruker;
	BedCardButton btnKonfigurering;
	BedCardButton btnAvslutt;
	Logic logic;
	public MenuPanel(BedCard bedcard, Logic logic) {
		super(bedcard);
		this.logic = logic;
		width = bedcard.getDrawWidth();
		height = BedCard.MenuPanelHeight;
		setPanelSizes();
		FlowLayout layout = new FlowLayout(FlowLayout.TRAILING, 0, 0);
		setLayout(layout);
	
		btnRegistrering = new BedCardButton("registrering", bedcard.getImageLoader().getImage("buttons/registreringHover.png"), bedcard.getImageLoader().getImage("buttons/registrering.png"));
		btnRegistrering.addActionListener(bedcard);
		add(btnRegistrering);
		btnPameldte = new BedCardButton("pameldte", bedcard.getImageLoader().getImage("buttons/pameldteHover.png"), bedcard.getImageLoader().getImage("buttons/pameldte.png"));
		btnPameldte.addActionListener(bedcard);
		add(btnPameldte);
		btnVenteliste = new BedCardButton("venteliste", bedcard.getImageLoader().getImage("buttons/ventelisteHover.png"), bedcard.getImageLoader().getImage("buttons/venteliste.png"));
		btnVenteliste.addActionListener(bedcard);
		add(btnVenteliste);
		btnVentelisteToggle = new BedCardButton("ventelisteToggle", bedcard.getImageLoader().getImage("buttons/ventelisteToggleApneHover.png"), bedcard.getImageLoader().getImage("buttons/ventelisteToggleApne.png"));
		btnVentelisteToggle.addActionListener(bedcard);
		add(btnVentelisteToggle);
		btnEndreBruker = new BedCardButton("endreBruker", bedcard.getImageLoader().getImage("buttons/endreBrukerHover.png"), bedcard.getImageLoader().getImage("buttons/endreBruker.png"));
		btnEndreBruker.addActionListener(bedcard);
		add(btnEndreBruker);
		btnKonfigurering = new BedCardButton("konfigurering", bedcard.getImageLoader().getImage("buttons/konfigureringHover.png"), bedcard.getImageLoader().getImage("buttons/konfigurering.png"));
		btnKonfigurering.addActionListener(bedcard);
		add(btnKonfigurering);
		btnAvslutt = new BedCardButton("avslutt", bedcard.getImageLoader().getImage("buttons/avsluttHover.png"), bedcard.getImageLoader().getImage("buttons/avslutt.png"));
		btnAvslutt.addActionListener(bedcard);
		add(btnAvslutt);
		
	}
	
	public void paint(Graphics g){
   	 	g.setColor(Color.black);
   	 	g.fillRect(0,0,width, height);
   	 	Graphics2D g2d = (Graphics2D)g;
   	 	g2d.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
   	 	wrapDraw(bedcard.getImageLoader().getImage("topBackground.png"), g2d, this, 0, 0);
   	 	drawMenu(g2d);
   	 	paintChildren(g);
    }
	
	private void drawMenu(Graphics2D g){
		g.drawImage(bedcard.getImageLoader().getImage("abakusLogo.png"), 0, 0, this);
	}
	public void resetMenu(){
		//Kunne reset alle knappene, trenger bare til Avslutt.
		btnAvslutt.reset();
	}
	public void waitingListStatusChanged(){
	 	if(logic.getWaitingListStatus()){
   	 		btnVentelisteToggle.setHover(bedcard.getImageLoader().getImage("buttons/ventelisteToggleLukkeHover.png"));
   	 		btnVentelisteToggle.setNormal(bedcard.getImageLoader().getImage("buttons/ventelisteToggleLukke.png"));
   	 	} else  {
   	 		btnVentelisteToggle.setHover(bedcard.getImageLoader().getImage("buttons/ventelisteToggleApneHover.png"));
   	 		btnVentelisteToggle.setNormal(bedcard.getImageLoader().getImage("buttons/ventelisteToggleApne.png"));
		}
	 	repaint();
	}
	public void fadeDisable() {
		log.debug("Fade: disable");
		fade(false);
	}
	public void fadeEnable() {
		log.debug("Fade: enable");
		fade(true);
	}
	private void fade(boolean state){
		btnAvslutt.setEnabled(state);
		btnEndreBruker.setEnabled(state);
		btnKonfigurering.setEnabled(state);
		btnPameldte.setEnabled(state);
		btnPameldte.setEnabled(state);
		btnRegistrering.setEnabled(state);
		btnVenteliste.setEnabled(state);
		btnVentelisteToggle.setEnabled(state);
	}
}
