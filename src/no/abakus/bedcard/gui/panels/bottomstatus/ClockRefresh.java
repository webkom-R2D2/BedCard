package no.abakus.bedcard.gui.panels.bottomstatus;

import java.util.TimerTask;

import org.apache.log4j.Logger;

public class ClockRefresh extends TimerTask {
	protected static Logger log = Logger.getLogger(ClockRefresh.class);
	BottomStatusPanel panel;
	public ClockRefresh(BottomStatusPanel panel) {
		super();
		this.panel = panel;
	}

	public void run() {
		log.debug("Clock Refreshed");
		panel.repaint();
	}

}
