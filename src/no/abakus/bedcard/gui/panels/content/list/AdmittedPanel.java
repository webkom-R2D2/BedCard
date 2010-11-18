package no.abakus.bedcard.gui.panels.content.list;

import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;

import no.abakus.bedcard.gui.BedCard;
import no.abakus.bedcard.gui.panels.BedCardPanel;

public class AdmittedPanel extends BedCardPanel {
	Image admitted;
	Image notAdmitted;
	Boolean status;
	public AdmittedPanel(BedCard bedcard) {
		super(bedcard);
		status = false;
		admitted = bedcard.getImageLoader().getImage("admitted.png");
		notAdmitted = bedcard.getImageLoader().getImage("notAdmitted.png");
		width = admitted.getWidth(null);
		height = admitted.getHeight(null);
		setPanelSizes();
		setLayout(new FlowLayout(FlowLayout.CENTER, 0,0));
	}
	public void paint(Graphics g){
		if(status != null){
			if(status){
				g.drawImage(admitted, 0,0,null);
			} else {
				g.drawImage(notAdmitted, 0,0,null);
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
