package no.abakus.bedcard.gui.panels.content.konfigurering;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

import no.abakus.bedcard.gui.BedCard;
import no.abakus.bedcard.gui.utils.ImageScaler;
import no.abakus.bedcard.logic.Logic;

import org.apache.log4j.Logger;

public class LogoButton extends JButton {
	private Logic logic;
	private Font font;
	private Image noLogo;
	private Color background;
	private Image logo;
	private int width;
	private int height;
	private static Logger log = Logger.getLogger(LogoButton.class);
	public LogoButton(String tittel, BedCard bedcard, Logic logic, Color background, int width, int height) {
		super(tittel);
		this.logic = logic;
		this.background = background;
		this.width = width;
		this.height = height;
		setMargin(new Insets(0,0,0,0));		
		setBorder(new EmptyBorder(0,0,0,0));
		setPreferredSize(new Dimension(width, height));
		noLogo = bedcard.getImageLoader().getImage("noLogo.png");
		updateData();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(background);
		g.fillRect(0, 0, width, height);
		
		Graphics2D g2d = (Graphics2D)g;
				
		if(logo != null){
			int xPos = (width-logo.getWidth(this))/2;
			int yPos = 5;
			if(xPos<0)
				xPos = 0;
			if(yPos<0)
				yPos = 0;
			log.debug("logo: (xPos: " + xPos + " yPos: " + yPos + ")" + logo);
			g.drawImage(logo, xPos, yPos, this);
		} else {
			int xPos = 0;
			int yPos = 5;
			if(xPos<0)
				xPos = 0;
			if(yPos<0)
				yPos = 0;
			g.drawImage(noLogo, xPos, yPos, this);
		}
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
   	 	g2d.setFont(new Font("Tahoma", Font.BOLD, 10));
		
   	 	String tekst = "Trykk for å endre";
   	 	FontMetrics fm = g2d.getFontMetrics();
   	 	int xPos = (width - g2d.getFontMetrics().stringWidth(tekst))/2;
   	 	int yPos = height-fm.getDescent()-5;
   	 	
   	 	g2d.setColor(Color.black);
		g2d.drawString(tekst, xPos, yPos);
		
	}
	public void updateData(){
		//Load logo
		Image img = null;
		if(logic.getEvent() != null)
			img = logic.getEvent().getImage();
		if(img != null){
			//Skaler bildet;
			log.debug("w: " + width + " h: " + height);
			logo = ImageScaler.scaleImageToFit(img, width, height-30);
			log.debug("Logo er nå satt til eventen sitt bilde");
		} else {
			logo = null;
		}
	}
}
