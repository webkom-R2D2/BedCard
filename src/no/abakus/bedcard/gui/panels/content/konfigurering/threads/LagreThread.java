package no.abakus.bedcard.gui.panels.content.konfigurering.threads;

import no.abakus.bedcard.gui.BedCard;
import no.abakus.bedcard.logic.Logic;
import no.abakus.bedcard.storage.domainmodel.SavedState;

public class LagreThread implements Runnable {
	private BedCard bedcard;
	private Logic logic;

	public LagreThread(BedCard bedcard, Logic logic) {
		super();
		this.bedcard = bedcard;
		this.logic = logic;
	}

	@Override
	public void run() {
		SavedState ok = logic.save();
		bedcard.stopHaltMessage();
		switch(ok){
		case savedToBoth:
			bedcard.popupMessage("Lagret", "Eventen er n� lagret til abakus.no og til fil");
			break;
		case savedToAbakusOnly:
			bedcard.errorMessage("Feil", "Klarte ikke � lagre til fil, kontakt Bedkom WEB");
			break;
		case savedToFileOnly:
			bedcard.errorMessage("Feil", "Klarte ikke � lagre til abakus.no, pr�v � logg p� igjen");
			break;
		case savedToNone:
			bedcard.errorMessage("Feil", "Klarte ikke � lagre til noe i det hele tatt, kontakt Bedkom WEB");
			break;
		}
	}
}
