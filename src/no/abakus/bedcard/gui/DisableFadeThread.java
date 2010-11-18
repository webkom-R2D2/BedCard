package no.abakus.bedcard.gui;

import no.abakus.bedcard.gui.BedCard;
import no.abakus.bedcard.logic.Logic;
import no.abakus.bedcard.logic.LogicException;

public class DisableFadeThread implements Runnable {
	private BedCard bedcard;
	
	public DisableFadeThread(BedCard bedcard) {
		super();
		this.bedcard = bedcard;
	}

	@Override
	public void run() {
		bedcard.fadeDisable();		
	}

}
