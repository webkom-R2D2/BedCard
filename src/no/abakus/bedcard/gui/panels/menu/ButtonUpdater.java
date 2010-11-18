package no.abakus.bedcard.gui.panels.menu;

import java.util.TimerTask;

import org.apache.log4j.Logger;

public class ButtonUpdater extends TimerTask {
	protected static Logger log = Logger.getLogger(ButtonUpdater.class);
	BedCardButton btn;
	public ButtonUpdater(BedCardButton btn) {
		super();
		this.btn = btn;
	}

	public void run() {
		btn.updatePosition();
	}

}
