package no.abakus.bedcard.logic;

import no.abakus.bedcard.logic.cardreader.CardReader;
import no.abakus.bedcard.storage.domainmodel.Student;

public interface LogicListener {
	public void admitted(Student student);
	public void addedToWaitingList(Student student);
	public void unknownUsernameToCard();
	public void notifyReaderStatusChange(CardReader reader);
	public void popupMessage(String title, String description);
	public void errorMessage(String title, String description);
	public void editUserCardNrUpdated();
}
