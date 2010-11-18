package no.abakus.bedcard.logic;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import no.abakus.bedcard.gui.ImageLoader;
import no.abakus.bedcard.logic.cardreader.CardReader;
import no.abakus.bedcard.logic.cardreader.CardReaderListener;
import no.abakus.bedcard.logic.cardreader.MagnetReader;
import no.abakus.bedcard.logic.cardreader.RFIDReader;
import no.abakus.bedcard.storage.DAO.BedcardDAO;
import no.abakus.bedcard.storage.DAO.MockBedCardDAO;
import no.abakus.bedcard.storage.domainmodel.BedCardSaveState;
import no.abakus.bedcard.storage.domainmodel.Event;
import no.abakus.bedcard.storage.domainmodel.SavedState;
import no.abakus.bedcard.storage.domainmodel.Student;
import no.abakus.bedcard.storage.domainmodel.StudentEntry;
import no.abakus.bedcard.storage.ws.AbakusNoException;
import no.abakus.naut.entity.news.Type;

import org.apache.log4j.Logger;

public class MockLogic implements Logic, CardReaderListener {
	private static Logger log = Logger.getLogger(MockLogic.class);
	private boolean waitinglistStatus = false;
	LogicListener ll;
	Event event;
	Student Stian = new Student(85, "stiandeh", "Stian", "Sønderland", 8, 5L, true, false);
	private BedcardDAO dao;
	CardReader[] readers = {
			new RFIDReader(this),
			new MagnetReader(this)
	};
	boolean isAuth;
	
	
	public MockLogic() {
		isAuth = false;
		dao = new MockBedCardDAO();
		
	}

	@Override
	public int getNumberOfAdmitted() {
		int count = 0;
		if(event != null){
			for(StudentEntry entry : event.getListOfParticipants().values())
				if(entry.enteredIsNotFalseOrNull())
					count++;
			for(StudentEntry entry : event.getWaitingList().values())
				if(entry.enteredIsNotFalseOrNull())
					count++;
		}
		return count;
	}

	@Override
	public boolean getWaitingListStatus() {
		return waitinglistStatus;
	}



	@Override
	public void setWaitinglistStatus(boolean opened) {
		if(waitinglistStatus){
			boolean funnet = false;
			if(event != null){
				for(StudentEntry entry : event.getWaitingList().values()){
					if(entry.getEntered() != null){
						funnet = true;
						break;
					}
				}
			}
			if(funnet){
				ll.popupMessage("Feil ved lukking", "Det har blitt sluppet inn deltakere fra ventelisten");
			} else {
				waitinglistStatus = opened;	
			}
			
		} else {
			waitinglistStatus = opened;
		}
	}

	@Override
	public boolean checkInStudent(String username) {
		if(event != null){
			for(StudentEntry entry : event.getWaitingList().values()){
				if(entry.getStudent().getUsername().equals(username)){
					entry.setEntered(true);
					saveRegistrationStatus(entry);
					ll.admitted(entry.getStudent());
				}
			}
			
			if(!username.equals("albretse"))
				return false;		
			ll.admitted(getStudent("albretse"));		
			log.debug(username +" checked in");
		}
		return true;
	}

	@Override
	public ArrayList<CardReader> getAllReaders() {
		ArrayList<CardReader> readers = new ArrayList<CardReader>();
		for(int i = 0; i<4;i++){
			readers.add(new RFIDReader(this));
			readers.add(new MagnetReader(this));
		}
		return readers;
	}

	

	@Override
	public ArrayList<Type> getAllTypes() {
		/*ArrayList<Type> typer = new ArrayList<Type>();
		for(int i = 0; i<6; i++){
			Type type = new Type();
			type.setId(new Long(i));
			type.setName("Bedcard " + i);
			type.setDescription("Bedriftspresentasjon " + i);
			typer.add(type);
		}
		return typer;*/
		try {
			return dao.getAllTypes();
		} catch (AbakusNoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	

	@Override
	public Event getEvent() {
		return event;
	}



	@Override
	public boolean getFreeFlow() {
		return false;
	}

	@Override
	public ArrayList<Event> getListOfEvents(Date from, Date to, Type type) {
		log.debug("getListOfEvents");
		log.debug("Fra: " + from);
		log.debug("Til: " + to);
		log.debug("Type: " + type);	
		
		ArrayList<Event> events = new ArrayList<Event>();
		for(int i = 0; i<15; i++){
			Event evnt = new Event();
			evnt.setId(230L+i);
			evnt.setTitle("Title " + i);
			evnt.setStartTime(Calendar.getInstance().getTime());
			events.add(evnt);
		}
		return events;
	}



	@Override
	public ArrayList<BedCardSaveState> getListOfSavedEvents(Type type) {
		log.debug("getListOfSavedEvents");
		log.debug("Type: " + type);
		
		ArrayList<BedCardSaveState> events = new ArrayList<BedCardSaveState>();
		if(type.getId() != 1337L){
			for(int i = 0; i<15; i++){
				Event evnt = new Event();
				evnt.setId(230L+i);
				evnt.setTitle("Title sin stitøe title" + i);
				evnt.setDescription("Dette er en beskrivelse av eventen som vi kan vise ut");
				evnt.setStartTime(Calendar.getInstance().getTime());
				BedCardSaveState bss = new BedCardSaveState();
				bss.setEvent(evnt);
				bss.setSavedToAbakus(i>9);
				events.add(bss);
			}
		}
		return events;
	}

	@Override
	public Student getStudent(String username) {
		if(username.equals("albretse"))
			return new Student(90, "albretse", "Sigve", "Albretsen", 9, 8L, true, false);
		return null;
	}


	@Override
	public void loadFromAbakus(long eventID) {
		log.debug("Loaded from abakus eventID: " + eventID);
		Type type = new Type();
		type.setId(9L);
		type.setName("Bedpres");
		type.setDescription("Bedriftspresentasjon");

		HashMap<Long,StudentEntry> admitted;
		HashMap<Long,StudentEntry> venteliste;
		
		admitted = new HashMap<Long,StudentEntry>();
		admitted.put(81L, new StudentEntry(new Student(81, "albretse", "Sigve", "Albrichtsen", 9, 8L, true, false), null));
		admitted.put(82L, new StudentEntry(new Student(82, "ofsdahl", "Terje", "Huffsdahl", 9, 8L, true, false), null));
		admitted.put(83L, new StudentEntry(new Student(83, "chrino", "Christian", "Nuda", 9, 8L, true, false), null));
		admitted.put(84L, new StudentEntry(new Student(84, "ormstyl", "Tarjei", "Ormen", 9, 8L, true, false), null));
		admitted.put(85L, new StudentEntry(Stian, null));
		admitted.put(86L, new StudentEntry(new Student(86, "stiane", "Stian", "Erichson", 9, 8L, true, false), null));
		admitted.put(87L, new StudentEntry(new Student(87, "marihov", "Marianne", "Fru. Hoved", 9, 8L, true, false), null));
		
		for(int i = 1; i<110; i++){
			String brukernavn = "";
			if(i<30){
				brukernavn = brukernavn.concat("a");
				if(i<10){
					brukernavn = brukernavn.concat("a");
				} else if(i<20){
					brukernavn = brukernavn.concat("b");
				} else {
					brukernavn = brukernavn.concat("c");
				}
			} else if(i<60){
				brukernavn = brukernavn.concat("b");
				if(i<40){
					brukernavn = brukernavn.concat("a");
				} else if(i<50){
					brukernavn = brukernavn.concat("b");
				} else {
					brukernavn = brukernavn.concat("c");
				}
			} else {
				brukernavn = brukernavn.concat("c");
				if(i<70){
					brukernavn = brukernavn.concat("a");
				} else if(i<80){
					brukernavn = brukernavn.concat("b");
				} else {
					brukernavn = brukernavn.concat("c");
				}
			}
			admitted.put(90L+i, new StudentEntry(new Student(90+i, brukernavn+i, "Sigve"+i, "Albretsen", 9, 8L, true, false), i>5));
		}
		venteliste = new HashMap<Long, StudentEntry>();
		venteliste.put(81L, new StudentEntry(new Student(81, "albretse", "Sigve", "Albrichtsen", 9, 8L, true, false), null));
		venteliste.put(82L, new StudentEntry(new Student(82, "ofsdahl", "Terje", "Huffsdahl", 9, 8L, true, false), null));
		venteliste.put(83L, new StudentEntry(new Student(83, "chrino", "Christian", "Nuda", 9, 8L, true, false), null));
		venteliste.put(84L, new StudentEntry(new Student(84, "ormstyl", "Tarjei", "Ormen", 9, 8L, true, false), null));
		venteliste.put(85L, new StudentEntry(Stian, null));
		venteliste.put(86L, new StudentEntry(new Student(86, "stiane", "Stian", "Erichson", 9, 8L, true, false), null));
		venteliste.put(87L, new StudentEntry(new Student(87, "marihov", "Marianne", "Fru. Hoved", 9, 8L, true, false), null));

		ImageLoader loader = new ImageLoader();
		event = new Event(23, "Accenture", "H3", "Description", "summary", 120, 0, 0, true, true, Calendar.getInstance().getTime(), Calendar.getInstance().getTime(),
				1, true, false, type, admitted, null, null, venteliste, loader.getImage("accenture.jpg"));
		event = new Event(23, "Accenture", "H3", "Description", "summary", 120, 0, 0, true, true, Calendar.getInstance().getTime(), Calendar.getInstance().getTime(),
				1, true, false, type, admitted, null, null, venteliste, null);
	}



	@Override
	public void loadFromFile(long eventID, Type type) {
		log.debug("Loaded from file eventID: " + eventID);
		log.debug("Type: " + type);
	}



	@Override
	public void reconnectReader(CardReader reader) {
		log.debug("Reconnected reader");
	}

	@Override
	public void reconnectReaders() {
		log.debug("Reconnected all readers");
	}

	@Override
	public void removeStudentFromWaitinglist(Student student) {
		log.debug("Student " + student.getUsername() + " has been removed from waitinglist");
	}



	@Override
	public void resetBedCard() {
		log.debug("Event reset");
		event.setImage(null);
	}



	@Override
	public SavedState save() {
		log.debug("Saved");
		return SavedState.savedToBoth;
		
	}



	@Override
	public void saveRegistrationStatus(StudentEntry studentEntry) {
		log.debug("studententry saved");
	}



	@Override
	public void saveStudent(Student student) {
		log.debug("Student saved");
	}



	@Override
	public boolean saveToFile() {
		log.debug("Saved to file");
		return true;
		
	}



	@Override
	public void connect(String username, String password) {
		log.debug("auth set");
		isAuth = true;
	}



	@Override
	public void setFreeFlow(boolean on) {
		log.debug("freeflow set: " + on);
		
	}



	@Override
	public void setLogicListener(LogicListener ll) {
		this.ll = ll;
	}

	@Override
	public void updateMaxParticipants(int number) {
		if(event != null)
			event.setCapacity(number);
	}



	@Override
	public StudentEntry getNextInWaitingListLine() {
		if(event == null)
			return null;
		for(StudentEntry entry : event.getWaitingList().values()){
			if(entry.getEntered() == null)
				return entry;
		}
		return null;
	}

	@Override
	public void notifyReaderStatusChange(CardReader reader) {
		log.debug("reader changed");
		ll.notifyReaderStatusChange(reader);
	}

	@Override
	public void receiveOldCardNumber(int cardNumber) {
		log.debug("old Number recieved: " + cardNumber);
		ll.admitted(Stian);
	}

	@Override
	public void receiveRFIDCardNumber(long cardNumber) {
		log.debug("rfid Number recieved: " + cardNumber);
		ll.admitted(Stian);
	}

	@Override
	public boolean returnUnkownUsernameToCard(String username) {
		if(!username.equals("albretse"))
			return false;
	
		ll.addedToWaitingList(getStudent("albretse"));
		
		log.debug(username +" checked in");
		return true;
	}

	@Override
	public void connectReaders() {
		//for (CardReader cr : readers) cr.start();
		//Får denne: java.lang.UnsatisfiedLinkError: no rxtxSerial in java.library.path thrown while loading gnu.io.RXTXCommDriver
	}

	@Override
	public boolean isAuthenticated() {
		return isAuth;
		
	}

	@Override
	public ArrayList<StudentEntry> getListOfParticipants() {
		return new ArrayList<StudentEntry>(event.getListOfParticipants().values());
	}

	@Override
	public ArrayList<StudentEntry> getWaitingList() {
		return new ArrayList<StudentEntry>(event.getWaitingList().values());
	}
	
	@Override
	public ArrayList<Type> getAllSavedTypes() {
		// no need to mock, the proper LogicHandler is in place
		return null;
	}

	@Override
	public void setAllowEverybody(boolean allow) {
		// no need to mock, the proper LogicHandler is in place
	}

	@Override
	public boolean getAllowEverybody() {
		// no need to mock, the proper LogicHandler is in place
		return false;
	}
	
	@Override
	public int getNumberOfWaiting() {
		// no need to mock, the proper LogicHandler is in place
		return 0;
	}

	@Override
	public void setEditingUser(Student studentToEdit) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getNumberOfNonAllowedWaiting() {
		// TODO Auto-generated method stub
		return 0;
	}


}
