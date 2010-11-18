package no.abakus.bedcard.gui;

import java.util.TimerTask;

import org.apache.log4j.Logger;

public class PopupCancel extends TimerTask {
	protected static Logger log = Logger.getLogger(PopupCancel.class);
	BedCardGUI bedcard;
	

	public PopupCancel(BedCardGUI bedcard) {
		super();
		this.bedcard = bedcard;
	}


	public void run() {
		bedcard.fadeIn();
	}

}
