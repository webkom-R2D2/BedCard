package no.abakus.bedcard.gui.panels.menu;

import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.util.Calendar;
import java.util.Timer;

import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

import org.apache.log4j.Logger;

public class BedCardButton extends JButton {
	private static Logger log = Logger.getLogger(BedCardButton.class);
	private Image hover;
	private Image normal;
	private double position;
	private static int topPosition = 10;
	private static int stepTime = 60;
	private boolean hoverStatus;
	private Timer timer;

	public BedCardButton(String arg0, Image hover, Image normal) {
		super(arg0);
		this.hover = hover;
		this.normal = normal;
		setPreferredSize(new Dimension(normal.getWidth(this),normal.getHeight(this)));
		hoverStatus = false;
		position = 0;
		setMargin(new Insets(0,0,0,0));		
		setBorder(new EmptyBorder(0,0,0,0));
		
	}

	public void paint(Graphics g){
		float optique1 = (float)position/(float)topPosition;
		float optique2 = 1.0f-optique1;
		Graphics2D g2d = (Graphics2D)g;
   	 	g2d.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, optique1));
		g2d.drawImage(hover, 0, 0, this);
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, optique2));
		g2d.drawImage(normal, 0, 0, this);
	}

	protected void processMouseEvent(MouseEvent arg0) {
		super.processMouseEvent(arg0);
		
			if(arg0.getID() == MouseEvent.MOUSE_ENTERED){
				changeHoverStatus(true);
				log.debug("Mouse entered: " + this.getActionCommand());
			} else if(arg0.getID() == MouseEvent.MOUSE_EXITED){
				changeHoverStatus(false);
				log.debug("Mouse exited: " + this.getActionCommand());
			}
		
	}
	private void changeHoverStatus(boolean status){
		hoverStatus = status;
		if(timer != null){
			timer.cancel();
			timer = null;
		}
		timer = new Timer();
		timer.schedule(new ButtonUpdater(this), Calendar.getInstance().getTime(), stepTime);
	}
	public void updatePosition(){
		if(isEnabled()){
			if(hoverStatus){
				if(position<topPosition){
					position = topPosition;
				} else {
					timer.cancel();
				}
			} else {
				if(position>0){
					position--;
				} else {
					timer.cancel();
				}
			}
			if(position>topPosition){
				position = topPosition;
			} else if(position<0){
				position = 0;
			}
			repaint();
		}
	}

	public void setHover(Image hover) {
		this.hover = hover;
	}

	public void setNormal(Image normal) {
		this.normal = normal;
	}
	
	public void reset(){
		changeHoverStatus(false);
	}
}
