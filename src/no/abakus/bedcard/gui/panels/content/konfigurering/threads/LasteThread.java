package no.abakus.bedcard.gui.panels.content.konfigurering.threads;

import no.abakus.bedcard.gui.BedCard;
import no.abakus.bedcard.logic.Logic;
import no.abakus.bedcard.logic.LogicException;

public class LasteThread implements Runnable {
	private BedCard bedcard;
	private Logic logic;
	private long id; 
		
	
	public LasteThread(BedCard bedcard, Logic logic, long id) {
		super();
		this.bedcard = bedcard;
		this.logic = logic;
		this.id = id;
	}

	

	public LasteThread(BedCard bedcard, Logic logic) {
		super();
		this.bedcard = bedcard;
		this.logic = logic;
	}



	public LasteThread(BedCard bedcard, long id) {
		super();
		this.bedcard = bedcard;
		this.id = id;
	}



	public void setId(long id) {
		this.id = id;
	}



	@Override
	public void run() {
		boolean ok = true;
		String feilmelding = "";
		try {
			logic.loadFromAbakus(id);
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
