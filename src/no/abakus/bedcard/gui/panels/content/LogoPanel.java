package no.abakus.bedcard.gui.panels.content;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import no.abakus.bedcard.gui.BedCard;
import no.abakus.bedcard.gui.panels.BedCardPanel;
import no.abakus.bedcard.logic.Logic;

public class LogoPanel extends BedCardPanel {
	Logic logic;
	Font font;
	public LogoPanel(BedCard bedcard, Logic logic) {
		super(bedcard);
		this.logic = logic;
		width = 1024;
		height = 160;
		setPanelSizes();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.white);
		g.fillRect(0, 0, width, height);
		
		Graphics2D g2d = (Graphics2D)g;
		boolean useImage = false;
		if(logic.getEvent() != null)
			useImage = logic.getEvent().getImage() != null;
		if(useImage){
			int xPos = (width-logic.getEvent().getImage().getWidth(this))/2;
			int yPos = (height-logic.getEvent().getImage().getHeight(this))/2;
			if(xPos<0)
				xPos = 0;
			if(yPos<0)
				yPos = 0;
			g.drawImage(logic.getEvent().getImage(), xPos, yPos, this);
		} else {
			g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
	   	 	if(font == null)
	   			checkFontSize(g2d);
	   	 	g2d.setFont(font);
			
	   	 	String title = "";
	   	 	if(logic.getEvent() != null)
	   	 		title = logic.getEvent().getTitle();
	   	 	FontMetrics fm = g2d.getFontMetrics();
	   	 	int xPos = (width - g2d.getFontMetrics().stringWidth(title))/2;
	   	 	int yPos = height/2-fm.getDescent()+(fm.getAscent()+fm.getDescent())/2;
	   	 	
	   	 	g2d.setColor(Color.black);
			g2d.drawString(title, xPos, yPos);
		}
	}
	
	public void updateData(){
		font = null;
	}
	private void checkFontSize(Graphics2D g2d){
		int fontSize = 120;
   	 	font = new Font("Tahoma", Font.PLAIN, fontSize);
   	 	g2d.setFont(font);
   	 	if(logic.getEvent() != null){
	   	 	String title = logic.getEvent().getTitle();
	 		while(g2d.getFontMetrics().stringWidth(title)>width){
	 			fontSize--;
	 			font = new Font("Tahoma", Font.PLAIN, fontSize);
	 			g2d.setFont(font);
	 			if(fontSize<=1)
	 				break;
	 		}
   	 	}
	}
}
