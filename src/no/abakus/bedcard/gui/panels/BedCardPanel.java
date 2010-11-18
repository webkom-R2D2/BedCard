package no.abakus.bedcard.gui.panels;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import no.abakus.bedcard.gui.BedCard;

import org.apache.log4j.Logger;

public abstract class BedCardPanel extends JPanel {
	protected static Logger log = Logger.getLogger(BedCardPanel.class);
	protected BedCard bedcard;
	protected int height;
	protected int width;

	
	
	public BedCardPanel(BedCard bedcard){
		this.bedcard = bedcard;
		setBorder(new EmptyBorder(0,0,0,0));
	}
	
	protected void setPanelSizes(){
		setSize(width, height);
		setPreferredSize(new Dimension(width, height));
	}
	protected void wrapDraw(Image img, Graphics2D g, BedCardPanel panel, int x, int y){
		for(int i = x; i<width; i++){
			g.drawImage(img, x+i, y, panel);
		}
	}
	
}
