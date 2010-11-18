package no.abakus.bedcard.gui;

import org.apache.log4j.Logger;

import no.abakus.bedcard.logic.LogicListener;
import no.abakus.bedcard.logic.cardreader.CardReader;
import no.abakus.bedcard.storage.domainmodel.Student;

public class MockLogicListener implements LogicListener {
	private static Logger log = Logger.getLogger(MockLogicListener.class);
	@Override
	public void addedToWaitingList(Student student) {
		log.debug("Added to waiting list: " + student);
	}

	@Override
	public void admitted(Student student) {
		log.debug("Admitted: " + student);
	}

	@Override
	public void notifyReaderStatusChange(CardReader reader) {
		log.debug("Reader changed: " + reader);
	}

	@Override
	public void unknownUsernameToCard() {
		log.debug("unknownUsernameToCard");
	}

	@Override
	public void popupMessage(String msg, String msg2) {
		log.debug("notifyWithWarning" + " msg: (" + msg + ", " + msg2+ ")");
		
	}

	@Override
	public void errorMessage(String title, String description) {
		// no need for mock, proper BLING-BLING mafakka GUI is in its right place!
	}

	@Override
	public void editUserCardNrUpdated(){		
		// no need for mock, proper BLING-BLING mafakka GUI is in its right place!
	}
}
