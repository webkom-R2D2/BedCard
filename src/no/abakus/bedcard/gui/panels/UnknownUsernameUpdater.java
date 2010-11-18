package no.abakus.bedcard.gui.panels;

import java.util.TimerTask;

import no.abakus.bedcard.gui.BedCardGUI;

import org.apache.log4j.Logger;

public class UnknownUsernameUpdater extends TimerTask {
	protected static Logger log = Logger.getLogger(UnknownUsernameUpdater.class);
	BedCardGUI bedcard;
	
	public UnknownUsernameUpdater(BedCardGUI bedcard) {
		super();
		this.bedcard = bedcard;
	}

	public void run() {
		bedcard.repaint();
	}

}
