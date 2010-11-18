package no.abakus.bedcard.gui.panels.content.konfigurering.threads;

import no.abakus.bedcard.gui.BedCard;
import no.abakus.bedcard.logic.Logic;
import no.abakus.bedcard.logic.LogicException;

public class ConnectThread implements Runnable {
	private BedCard bedcard;
	private Logic logic;
	private String username;
	private String password;
		
	public ConnectThread(BedCard bedcard, Logic logic, String username,
			String password) {
		super();
		this.bedcard = bedcard;
		this.logic = logic;
		this.username = username;
		this.password = password;
	}



	@Override
	public void run() {
		boolean ok = true;
		String feilmelding = "";
		try {
			logic.connect(username, password);
		} catch (LogicException e) {
			ok = false;
			feilmelding = e.getMessage();
		}
		bedcard.stopHaltMessage();
		if(!ok){
			bedcard.errorMessage("Feil", feilmelding);
		}
		
	}

}
