package no.abakus.bedcard.storage.domainmodel;

import java.util.HashMap;

public class BedCardSaveState {
	private Event event;
	private HashMap<String, Student> allStudents;
	private HashMap<Long, Student> alteredStudents;
	private boolean freeFlow;
	private boolean waitingListStatus;
	private boolean savedToAbakus;
	private boolean allowEverybody;
	
	public BedCardSaveState() {
	}
	
	public BedCardSaveState(Event event, HashMap<String, Student> allStudents,
			HashMap<Long, Student> alteredStudents, boolean freeFlow,
			boolean waitingListStatus, boolean savedToAbakus,
			boolean allowEverybody) {
		super();
		this.event = event;
		this.allStudents = allStudents;
		this.alteredStudents = alteredStudents;
		this.freeFlow = freeFlow;
		this.waitingListStatus = waitingListStatus;
		this.savedToAbakus = savedToAbakus;
		this.allowEverybody = allowEverybody;
	}
	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public HashMap<String, Student> getAllStudents() {
		return allStudents;
	}

	public void setAllStudents(HashMap<String, Student> allStudents) {
		this.allStudents = allStudents;
	}

	public HashMap<Long, Student> getAlteredStudents() {
		return alteredStudents;
	}

	public void setAlteredStudents(HashMap<Long, Student> alteredStudents) {
		this.alteredStudents = alteredStudents;
	}

	public boolean isFreeFlow() {
		return freeFlow;
	}

	public void setFreeFlow(boolean freeFlow) {
		this.freeFlow = freeFlow;
	}

	public boolean isWaitingListStatus() {
		return waitingListStatus;
	}

	public void setWaitingListStatus(boolean waitingListStatus) {
		this.waitingListStatus = waitingListStatus;
	}

	public boolean isSavedToAbakus() {
		return savedToAbakus;
	}

	public void setSavedToAbakus(boolean savedToAbakus) {
		this.savedToAbakus = savedToAbakus;
	}

	public boolean isAllowEverybody() {
		return allowEverybody;
	}

	public void setAllowEverybody(boolean allowEverybody) {
		this.allowEverybody = allowEverybody;
	}
	
}
