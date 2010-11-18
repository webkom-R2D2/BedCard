package no.abakus.bedcard.gui.panels.content.list;

import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

import no.abakus.bedcard.gui.BedCard;
import no.abakus.bedcard.gui.panels.BedCardPanel;

public class ReaderPanel extends BedCardPanel {
	Image saved;
	Image notSaved;
	Boolean status;
	public ReaderPanel(BedCard bedcard) {
		super(bedcard);
		status = false;
		saved = bedcard.getImageLoader().getImage("readerUp.png");
		notSaved = bedcard.getImageLoader().getImage("readerDown.png");
		width = saved.getWidth(null);
		height = saved.getHeight(null);
		setPanelSizes();
		setLayout(new FlowLayout(FlowLayout.CENTER, 0,0));
	}
	public void paint(Graphics g){
		Graphics2D g2d = (Graphics2D)g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		if(status != null){
			if(status){
				g2d.drawImage(saved, 0,0,null);
			} else {
				g2d.drawImage(notSaved, 0,0,null);
			}
		} else {
			log.debug("unknown");
		}
    }
	public void setStatus(Boolean enabled) {
		this.status = enabled;
		repaint();
	}
	public Boolean getStatus() {
		return status;
	}
	
	

}
