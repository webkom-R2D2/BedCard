package no.abakus.bedcard.gui.panels;

import java.util.TimerTask;

import no.abakus.bedcard.gui.BedCardGUI;

import org.apache.log4j.Logger;

public class FadeUpdater extends TimerTask {
	protected static Logger log = Logger.getLogger(FadeUpdater.class);
	BedCardGUI bedcard;
	
	public FadeUpdater(BedCardGUI bedcard) {
		super();
		this.bedcard = bedcard;
	}

	public void run() {
		bedcard.updatePosition();
	}

}
