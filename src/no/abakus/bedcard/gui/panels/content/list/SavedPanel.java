package no.abakus.bedcard.gui.panels.content.list;

import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;

import no.abakus.bedcard.gui.BedCard;
import no.abakus.bedcard.gui.panels.BedCardPanel;

public class SavedPanel extends BedCardPanel {
	Image saved;
	Image notSaved;
	Boolean status;
	public SavedPanel(BedCard bedcard) {
		super(bedcard);
		status = false;
		saved = bedcard.getImageLoader().getImage("saved.png");
		notSaved = bedcard.getImageLoader().getImage("notSaved.png");
		width = saved.getWidth(null);
		height = saved.getHeight(null);
		setPanelSizes();
		setLayout(new FlowLayout(FlowLayout.CENTER, 0,0));
	}
	public void paint(Graphics g){
		if(status != null){
			if(status){
				g.drawImage(saved, 0,0,null);
			} else {
				g.drawImage(notSaved, 0,0,null);
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
