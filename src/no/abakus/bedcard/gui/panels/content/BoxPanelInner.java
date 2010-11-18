package no.abakus.bedcard.gui.panels.content;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import no.abakus.bedcard.gui.BedCard;
import no.abakus.bedcard.gui.panels.BedCardPanel;

public class BoxPanelInner extends BedCardPanel {
	private JPanel internPanel;
	private JPanel bottomPanel;
	private Image topBackground;
	private Image bottomBackground;
	private Image verticalBackgroundLeft;
	private Image verticalBackgroundRight;
	private Image bottomLeftCorner;
	private Image bottomRightCorner;
	private Image topLeftCorner;
	private Image topRightCorner;
	private String title;
	public static int boxPadding = 3;
	private int titleLength;
	private Font titleFont;
	
	public BoxPanelInner(BedCard bedcard, String title, int boxWidth, int boxHeight) {
		super(bedcard);
		this.title = title;
		this.width = boxWidth;
		this.height = boxHeight;
		setPanelSizes();
		setLayout(null);
		titleLength = 150;
		
		topBackground = bedcard.getImageLoader().getImage("boxpanel/horizontalBackgroundTop.png");
		bottomBackground = bedcard.getImageLoader().getImage("boxpanel/horizontalBackgroundBottom.png");
		verticalBackgroundLeft = bedcard.getImageLoader().getImage("boxpanel/verticalBackgroundLeft.png");
		verticalBackgroundRight = bedcard.getImageLoader().getImage("boxpanel/verticalBackgroundRight.png");
		bottomLeftCorner = bedcard.getImageLoader().getImage("boxpanel/bottomLeftCornerInner.png");
		bottomRightCorner = bedcard.getImageLoader().getImage("boxpanel/bottomRightCorner.png");
		topLeftCorner = bedcard.getImageLoader().getImage("boxpanel/topLeftCornerInner.png");
		topRightCorner = bedcard.getImageLoader().getImage("boxpanel/topRightCornerInner.png");
		titleFont = new Font("Tahoma", Font.PLAIN, 20);
		internPanel = new JPanel();
		internPanel.setBackground(new Color(227, 227, 227));
		int xPos = verticalBackgroundLeft.getWidth(this);
		int yPos = topBackground.getHeight(this)+boxPadding;
		internPanel.setBounds(xPos, yPos, width-verticalBackgroundRight.getWidth(this)-xPos, height-yPos-bottomBackground.getHeight(this)-boxPadding);
		this.add(internPanel);
		
		bottomPanel = new JPanel();
		bottomPanel.setBackground(new Color(44, 46, 55));
		xPos = verticalBackgroundLeft.getWidth(this)+boxPadding+titleLength;
		yPos = height - bottomBackground.getHeight(this)+2*boxPadding;
		bottomPanel.setBounds(xPos, yPos, width-verticalBackgroundRight.getWidth(this)-boxPadding-xPos, height-yPos-2);
		this.add(bottomPanel);
		
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(new Color(227,227,227));
		g.fillRect(0, 0, width, height);
		for(int i = 0; i<width; i++){
			g.drawImage(topBackground, i,0,this);
			g.drawImage(bottomBackground, i,height-bottomBackground.getHeight(this),this);
		}
		for(int i = 0; i<height; i++){
			g.drawImage(verticalBackgroundLeft, 0,i,this);
			g.drawImage(verticalBackgroundRight, width-verticalBackgroundRight.getWidth(this),i,this);
		}
		g.drawImage(topLeftCorner, 0,0,this);
		g.drawImage(topRightCorner, width-topRightCorner.getWidth(this),0,this);
		g.drawImage(bottomLeftCorner, 0,height-bottomLeftCorner.getHeight(this),this);
		g.drawImage(bottomRightCorner, width-bottomRightCorner.getWidth(this),height-bottomRightCorner.getHeight(this),this);
		g.setFont(titleFont);
		g.setColor(Color.white);

		Graphics2D g2d = (Graphics2D)g; 
		g2d.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
		
		g.drawString(title, 10, height-18);
		
		paintChildren(g);
	}

	public JPanel getInternPanel() {
		return internPanel;
	}

	public JPanel getBottomPanel() {
		return bottomPanel;
	}

	
	
	
	
	

}
