package no.abakus.bedcard.gui.panels.content.registrering;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

import no.abakus.bedcard.gui.BedCard;
import no.abakus.bedcard.gui.panels.BedCardPanel;

public class HighlightBox extends BedCardPanel {
	private JPanel internPanel;
	private Image background;
	private Image backgroundHL;
	public static int boxPadding = 8;
	private boolean highlighted;
	private Color color;
	
	public HighlightBox(BedCard bedcard) {
		super(bedcard);
		background = bedcard.getImageLoader().getImage("unknownUsernamePopup.png");
		backgroundHL = bedcard.getImageLoader().getImage("unknownUsernamePopupHL.png");
		this.width = background.getWidth(this);
		this.height = background.getHeight(this);
		setPanelSizes();
		setLayout(new FlowLayout(FlowLayout.CENTER, boxPadding, boxPadding));
		color = new Color(197, 197, 196);
		
		internPanel = new JPanel();
		internPanel.setSize(width-2*boxPadding, height-2*boxPadding);
		internPanel.setPreferredSize(new Dimension(width-2*boxPadding, height-2*boxPadding));
		internPanel.setBackground(color);
		setHighlighted(false);
				
		this.add(internPanel);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.white);
		g.fillRect(0, 0, width, height);
		if(highlighted){
			g.drawImage(backgroundHL, 0,0,this);
		} else {
			g.drawImage(background, 0,0,this);
		}

		

		paintChildren(g);
	}

	public JPanel getInternPanel() {
		return internPanel;
	}

	public void setHighlighted(boolean highlighted) {
		this.highlighted = highlighted;
		/*if(highlighted){
			color = new Color(220,220,220);
		} else {
			color = Color.WHITE;
		}*/
		//internPanel.setBackground(color);
		repaint();
	}
	
	public int getWidth(){
		return width;
	}
	
	
	
	
	

}
