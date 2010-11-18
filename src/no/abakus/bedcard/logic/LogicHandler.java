package no.abakus.bedcard.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import no.abakus.bedcard.logic.cardreader.CardReader;
import no.abakus.bedcard.logic.cardreader.CardReaderListener;
import no.abakus.bedcard.logic.cardreader.MagnetReader;
import no.abakus.bedcard.logic.cardreader.NewRFIDReader;
import no.abakus.bedcard.logic.cardreader.RFIDReader;
import no.abakus.bedcard.logic.comparator.RegisteredComparator;
import no.abakus.bedcard.logic.comparator.WaitingComparator;
import no.abakus.bedcard.storage.DAO.AbakusFileBedCardDAO;
import no.abakus.bedcard.storage.DAO.BedcardDAO;
import no.abakus.bedcard.storage.domainmodel.BedCardSaveState;
import no.abakus.bedcard.storage.domainmodel.Event;
import no.abakus.bedcard.storage.domainmodel.SavedState;
import no.abakus.bedcard.storage.domainmodel.Student;
import no.abakus.bedcard.storage.domainmodel.StudentEntry;
import no.abakus.bedcard.storage.filemanager.FileManagerException;
import no.abakus.bedcard.storage.ws.AbakusNoException;
import no.abakus.naut.entity.news.Type;

import org.apache.log4j.Logger;

/**
 * 
 * @author Christian Nordahl
 * @author Sigve Albretsen
 *
 */
public class LogicHandler implements Logic, CardReaderListener {
	
	private static Logger log = Logger.getLogger(LogicHandler.class);
	private LogicListener ll;
	private BedcardDAO dao;
	private ArrayList<CardReader> readers;
	private RegisteredComparator regComp;
	private WaitingComparator waitComp;
	private boolean isAuth;
	private BedCardSaveState saveState;
	private Student editStudent;
	
	//HashMap for enkle søk.
	private HashMap<Long, Student> rfidStudents;
	private HashMap<Integer, Student> magnetStudents;
	
	//State: normal, hvorvidt du venter på brukernavn fra GUI, eller om man endrer en bruker
	private LogicState state;
	
	//Hjelpevariabler som holder ukjente nummer mens brukeren taster inn navnet i GUI 
	private Integer knownMagnetCardNumber;
	private Long knownRFIDCardNumber;
	
	public LogicHandler() {
		log.debug("Constructing LogicHandler");
		dao = new AbakusFileBedCardDAO();
		regComp = new RegisteredComparator();
		waitComp = new WaitingComparator(this);
		readers = new ArrayList<CardReader>();
		state = LogicState.normal;
	}

	@Override
	/**
	 * Returnerer true om brukernavnet er gyldig, false om ugyldig.
	 */
	public boolean checkInStudent(String username) {
		if(saveState != null){
			if(saveState.getAllStudents() != null){
				Student student = saveState.getAllStudents().get(username);
				if(student != null){
					admitStudent(student);
					return true;
				} else {
					return false;
				}
			}
		} else {
			ll.errorMessage("Ingen event", "Du må laste inn en event før du kan sjekke inn studenter");
			log.info("Feil i checkInStudent(): ingen innlastet event");
			return true;
		}
		return false;
	}


	@Override
	public void connectReaders() {
		readers = new ArrayList<CardReader>();
		//readers.add(new RFIDReader(this));
		readers.add(new MagnetReader(this));
		readers.add(new NewRFIDReader(this));
		reconnectReaders();
	}


	@Override
	public ArrayList<CardReader> getAllReaders() {
		return readers;
	}


	@Override
	public ArrayList<Type> getAllTypes() {
		if(isAuth){
			try {
				return dao.getAllTypes();
			} catch (AbakusNoException e) {
				isAuth = false;
				ll.errorMessage("Feil på nett", e.getMessage());
				log.error("Kunne ikke hente typer fra abakus DAO, selv om du var autentisert");
				return new ArrayList<Type>();
			}
		}
		log.debug("getAllTypes(): Brukeren er ikke autentisert");
		return new ArrayList<Type>();
	}


	@Override
	public Event getEvent() {
		if(saveState != null)
			return saveState.getEvent();
		return null;
	}


	@Override
	public boolean getFreeFlow() {
		if(saveState != null)
			return saveState.isFreeFlow();
		return false;
	}


	@Override
	public ArrayList<Event> getListOfEvents(Date from, Date to, Type type) {
		if(isAuth) {
			try {
				return dao.getListOfEvents(from, to, type);
			} catch (AbakusNoException e) {
				log.error("Feil i getListOfEvents(): "+e.getMessage());
				isAuth = false;
				ll.errorMessage("Feil på nett", e.getMessage());
				return new ArrayList<Event>();
			}
		}
		log.error("getListOfEvents(): Brukeren er ikke autentisert");
		ll.errorMessage("Ikke autentisert", "Autentiser deg med brukernavn og passord fra abakus.no");
		return new ArrayList<Event>();
	}


	@Override
	public ArrayList<BedCardSaveState> getListOfSavedEvents(Type type) {
		try {
			return dao.getListOfSavedEvents(type);
		} catch (FileManagerException e) {
			log.error("Kunne ikke hente liste av lagrede eventer fra DAO\n"
					+ e.getMessage());
			ll.errorMessage("Feil på filer", e.getMessage());
		}
		return new ArrayList<BedCardSaveState>();
	}


	@Override
	public StudentEntry getNextInWaitingListLine() {
		if(saveState != null){
			if(saveState.getEvent() != null){
				for(StudentEntry entry : getWaitingList()){
					if(entry.getEntered() == null)
						return entry;
				}
			}
		}
		return null;
	}


	@Override
	public int getNumberOfAdmitted() {
		int count = 0;
		if(saveState != null){
			if(saveState.getEvent() != null){
				for(StudentEntry entry : saveState.getEvent().getListOfParticipants().values())
					if(entry.enteredIsNotFalseOrNull())
						count++;
				for(StudentEntry entry : saveState.getEvent().getWaitingList().values())
					if(entry.enteredIsNotFalseOrNull())
						count++;
			}
		}
		return count;
	}


	@Override
	public Student getStudent(String username) {
		if(saveState == null){
			log.error("Feil i getStudent(): ingen innlastet event");
			ll.errorMessage("Ingen event", "Du må laste inn en event før du kan endre brukere");
			return null;
		}
		return saveState.getAllStudents().get(username);
	}

	
	@Override
	public boolean getWaitingListStatus() {
		if(saveState != null)
			return saveState.isWaitingListStatus();
		return false;
	}


	@Override
	public boolean isAuthenticated() {
		return isAuth;
	}
	

	@Override
	public void loadFromAbakus(long eventID) throws LogicException{
		if(isAuth) {
			try {
				saveState = dao.loadFromAbakus(eventID);
				buildSearchStudents();
			} catch (AbakusNoException e) {
				log.error(e);
				isAuth = false;
				throw new LogicException("Feil på nett: " + e.getMessage());
			}
		} else {
			log.debug("loadFromAbakus(): Brukeren er ikke autentisert");
			throw new LogicException("Ikke autentisert, autentiser deg med brukernavn og passord fra abakus.no");
		}
	}


	@Override
	public void loadFromFile(long eventID, Type type) {
		try {
			saveState = dao.loadFromFile(eventID, type);
			buildSearchStudents();
		} catch (FileManagerException e) {
			log.error("Filfeil i loadFromFile(): "+e);
			ll.errorMessage("Feil på filer" , e.getMessage());
		}		
	}

	@Override
	public void reconnectReader(CardReader reader) {
		for(CardReader cr : readers){
			if(reader == cr){
				cr.stop();
				cr.start();
			}
		}
	}

	@Override
	public void reconnectReaders() {
		for(CardReader cr : readers){
			cr.stop();
			cr.start();
		}
	}


	@Override
	public void removeStudentFromWaitinglist(Student student) {
		if(saveState != null){
			if(saveState.getEvent() != null){
				if(saveState.getEvent().getWaitingList() != null) {
					if(saveState.getEvent().getWaitingList().containsKey(student.getUserId())) {
						saveState.getEvent().getWaitingList().remove(student.getUserId());
					}
				}
			}
		}
	}


	@Override
	public void resetBedCard() {
		log.debug("Reset event");
		saveState = null;
		rfidStudents = null;
		magnetStudents = null;
		state = LogicState.normal;
		knownMagnetCardNumber = null;
		knownRFIDCardNumber = null;
		ll.popupMessage("Tilbakestillt", "BedCard er nå tilbakestillt");
	}


	@Override
	public boolean returnUnkownUsernameToCard(String username) {
		//Ikke gjør noe om ikke en event er lastet
		if(saveState == null){
			ll.errorMessage("Ingen event", "Du må laste inn en event før du kan slippe inn folk");
			log.info("Feil i returnUnknownUsernamToCard(): ingen innlastet event");
			state = LogicState.normal;
			return false;
		}
		if(state == LogicState.waitingForUnknownUsername){
			if(knownMagnetCardNumber!=null){
				if(saveState != null){
					Student student = saveState.getAllStudents().get(username);
					if(student != null){
						student.setCardNumber(knownMagnetCardNumber);
						if(!saveState.getAlteredStudents().containsKey(student.getUserId()))
							saveState.getAlteredStudents().put(student.getUserId(), student);
						if(!magnetStudents.containsValue(student)){
							magnetStudents.put(student.getCardNumber(), student);
						}
						knownMagnetCardNumber = null;
						admitStudent(student);
						state = LogicState.normal;
						return true;
					}
				}
				knownMagnetCardNumber = null;
			} else if(knownRFIDCardNumber!=null){
				if(saveState != null){
					Student student = saveState.getAllStudents().get(username);
					if(student != null){
						student.setRfidCardNumber(knownRFIDCardNumber);
						if(!saveState.getAlteredStudents().containsKey(student.getUserId()))
							saveState.getAlteredStudents().put(student.getUserId(), student);
						if(!rfidStudents.containsValue(student)){
							rfidStudents.put(student.getRfidCardNumber(), student);	
						}
						knownRFIDCardNumber = null;
						admitStudent(student);
						state = LogicState.normal;
						return true;
					}
				}
				knownRFIDCardNumber = null;
			} else {
				log.error("Vi har ikke lagret unna et kortnummer som vi skulle finne brukernavn til");	
			}
			state = LogicState.normal;
		} else {
			log.error("GUI gir oss brukernavn til ukjente kortnummer uten at vi mener at vi har spurt om det.");
			state = LogicState.normal;
		}
		return false;
	}


	@Override
	public SavedState save(){
		setNoShowsOnUnknown();
		if(isAuth) {
			log.debug("saving BedcardSavesState...");
			SavedState ok = dao.save(saveState);
			switch(ok){
			case savedToFileOnly:
			case savedToNone:
				isAuth = false;
				break;
			}
			return ok;
		}
		log.debug("save(): Brukeren er ikke autentisert");
		return SavedState.savedToNone;
	}


	@Override
	public void saveRegistrationStatus(StudentEntry studentEntry) {
		log.debug("lagrer / endrer RegistrationStatus for studentEntry...");
		Long id = studentEntry.getStudent().getUserId();
		Event event = saveState.getEvent();
		if(event.getListOfParticipants().containsKey(id))
			event.getListOfParticipants().get(id).setEntered(studentEntry.getEntered());
		else
			event.getWaitingList().get(id).setEntered(studentEntry.getEntered());
	}


	@Override
	public void saveStudent(Student student) {
		log.debug("saveStudent()");
		Student writeTo;
		if(saveState.getAlteredStudents().containsKey(student.getUserId())){
			log.debug("Finnes i altered");
			writeTo = saveState.getAlteredStudents().get(student.getUserId());
		} else {
			log.debug("Leter i allStudents");
			writeTo = saveState.getAllStudents().get(student.getUsername());
			saveState.getAlteredStudents().put(student.getUserId(), writeTo);
		}
		
		log.debug("student: " + writeTo.getFirstname() + " " + writeTo.getLastname());
		
		magnetStudents.remove(writeTo.getCardNumber());
		rfidStudents.remove(writeTo.getRfidCardNumber());
				
		writeTo.setFirstname(student.getFirstname());
		writeTo.setLastname(student.getLastname());
		writeTo.setRfidCardNumber(student.getRfidCardNumber());
		writeTo.setCardNumber(student.getCardNumber());
		
		if(writeTo.getRfidCardNumber() != null) {
			rfidStudents.put(writeTo.getRfidCardNumber(), writeTo);
			log.debug("student added in rfidStudents with number:" + writeTo.getRfidCardNumber());
		}
		if(writeTo.getCardNumber() != null) {
			magnetStudents.put(writeTo.getCardNumber(), writeTo);
			log.debug("student added in magnetStudents with number:" + writeTo.getCardNumber());
		}
	}

	@Override
	public boolean saveToFile() {
		try {
			return dao.saveToFile(saveState);
		} catch (FileManagerException e) {
			log.error("Filfeil i saveToFile():"+e);
			ll.errorMessage("Feil på filer" , e.getMessage());
		}
		return false;
	}


	@Override
	public void connect(String username, String password) throws LogicException{
		String melding = "";
		try {
			isAuth = dao.connect(username, password);
			System.out.println("Authenticated? " + isAuth);
		} catch (AbakusNoException e) {
			melding = "Oppkobling feilet: " + e.getMessage();
			isAuth = false;
		}
		log.debug("Authenticated: " + isAuth);
		if(!isAuth)
			throw new LogicException(melding);
	}


	@Override
	public void setFreeFlow(boolean on) {
		if(saveState != null)
			saveState.setFreeFlow(on);
	}


	@Override
	public void setLogicListener(LogicListener ll) {
		this.ll = ll;		
	}

	@Override
	public void setWaitinglistStatus(boolean opened) {
		if(saveState != null) {
			if(!opened) {
				boolean sjekk = true;
				for (StudentEntry studE : getWaitingList()) {
					if(studE.getEntered() != null) {
						sjekk = false;
						break;
					}
				}
				if(sjekk){
					saveState.setWaitingListStatus(opened);
				} else {
					log.info("Feil i setWaitingListStatus(): Ikke lov å lukke ventelista etter åpning");
					ll.errorMessage("Feil", "Kan ikke lukke ventelisten etter at du har sluppet inn eller nektet folk derifra");
				}
			} else {
				saveState.setWaitingListStatus(opened);
			}
		} else {
			log.info("Feil i setWaitingListStatus(): ingen innlastet event");
			ll.errorMessage("Ingen event", "Du må laste inn en event før du kan åpne ventelisten");
		}
	}


	@Override
	public void updateMaxParticipants(int number) {
		if(saveState != null){
			if(saveState.getEvent() != null){
				saveState.getEvent().setCapacity(number);
			}
		}
		
	}


	@Override
	public void notifyReaderStatusChange(CardReader reader) {
		log.debug("Reader changed: " + reader.getName());
		ll.notifyReaderStatusChange(reader);		
	}


	@Override
	public void receiveOldCardNumber(int cardNumber) {
		//Ikke gjør noe om ikke en event er lastet
		if(saveState == null){
			log.info("Feil i receiveOldCardNumber(): ingen innlastet event");
			ll.errorMessage("Ingen event", "Du må laste inn en event før du kan bruke leserene");
			return;
		}
		switch (state) {
		case normal:
			boolean found = false;
			if(magnetStudents != null){
				found = magnetStudents.containsKey(cardNumber);	
			} else {
				found = false;
			}
			if(found){
				admitStudent(magnetStudents.get(cardNumber));
			} else {
				state = LogicState.waitingForUnknownUsername;
				knownMagnetCardNumber = cardNumber;
				ll.unknownUsernameToCard();
			}
			break;
		case editingUser:
			editStudent.setCardNumber(cardNumber);
			ll.editUserCardNrUpdated();
			break;
		case waitingForUnknownUsername:
			// Do nothing, still waiting for another user to enter his name in the GUI
			break;
		}
	}


	@Override
	public void receiveRFIDCardNumber(long cardNumber) {
		//Ikke gjør noe om ikke en event er lastet
		if(saveState == null){
			log.info("Feil i receiveRFIDCardNumber(): ingen innlastet event");
			ll.errorMessage("Ingen event", "Du må laste inn en event før du kan bruke leserene");
			return;
		}
		
		switch (state) {
		case normal:
			boolean found = false;
			if(rfidStudents != null){
				found = rfidStudents.containsKey(cardNumber);	
			} else {
				found = false;
			}
			if(found){
				admitStudent(rfidStudents.get(cardNumber));
			} else {
				state = LogicState.waitingForUnknownUsername;
				knownRFIDCardNumber = cardNumber;
				ll.unknownUsernameToCard();
			}
			break;
		case editingUser:
			editStudent.setRfidCardNumber(cardNumber);
			ll.editUserCardNrUpdated();
			break;
		case waitingForUnknownUsername:
			// Do nothing, still waiting for another user to enter his name in the GUI
			break;
		}
	}

	@Override
	public ArrayList<StudentEntry> getListOfParticipants() {
		if(saveState != null){
			if(saveState.getEvent() != null){
				ArrayList<StudentEntry> list = new ArrayList<StudentEntry>(saveState.getEvent().getListOfParticipants().values());
				Collections.sort(list, regComp);
				return list;
			}
		}
		return new ArrayList<StudentEntry>();
	}

	@Override
	public ArrayList<StudentEntry> getWaitingList() {
		if(saveState != null){
			if(saveState.getEvent() != null){
				ArrayList<StudentEntry> list = new ArrayList<StudentEntry>(saveState.getEvent().getWaitingList().values());
				Collections.sort(list, waitComp);
				return list;
			}
		}
		return new ArrayList<StudentEntry>();
	}
	
	@Override
	public ArrayList<Type> getAllSavedTypes() {
		try {
			return dao.getAllSavedTypes();
		} catch (FileManagerException e) {
			return new ArrayList<Type>();
		}
	}
	
	@Override
	public void setAllowEverybody(boolean allow) {
		saveState.setAllowEverybody(allow);
		
	}

	@Override
	public boolean getAllowEverybody() {
		if(saveState != null)
			return saveState.isAllowEverybody();
		return false;
	}
	
	private void setNoShowsOnUnknown(){
		//Setter de som er null til false
		for(StudentEntry entry : saveState.getEvent().getListOfParticipants().values())
			if(!entry.enteredIsNotFalseOrNull())
				entry.setEntered(false);
	}
	private void buildSearchStudents(){
		rfidStudents = new HashMap<Long, Student>();
		magnetStudents = new HashMap<Integer, Student>();
		for(Student student : saveState.getAllStudents().values()){
			if(student.getRfidCardNumber() != null)
				rfidStudents.put(student.getRfidCardNumber(), student);
			if(student.getCardNumber() != null)
				magnetStudents.put(student.getCardNumber(), student);
		}
	}
	private void admitStudent(Student student){
		//Ikke gjør noe om ikke en event er lastet
		if(saveState == null){
			log.info("Feil i admitStudent(): ingen innlastet event");
			ll.errorMessage("Ingen event", "Du må laste inn en event før du kan bruke leserene");
			return;
		}
		// Sjekker om studenten er påmeldt, står på venteliste, eller må meldes på venteliste,
		// og hvorvidt det er plass, og om han har lov til å joine
		Event event = saveState.getEvent();
		Long id = student.getUserId();
		
		if(!getWaitingListStatus()) { // ventelista er lukket
			if(event.getListOfParticipants().containsKey(id)) { // påmeldt, se om han er admittet fra før
				if(!event.getListOfParticipants().get(id).enteredIsNotFalseOrNull()) {
					event.getListOfParticipants().get(id).setEntered(true);
					ll.admitted(student);
					log.debug("slapp inn :" + student.getFirstname() + " " + student.getLastname());
					return;
				} else {
					ll.errorMessage("Allerede innlogget", student.getFirstname()+ " " + student.getLastname() + " har sluppet inn allerede");
					log.info(student.getFirstname()+ " " + student.getLastname() + " har sluppet inn allerede");
				}
			} else { // settes på venteliste, sjekk om add'et fra før
				if(!event.getWaitingList().containsKey(id)) {
					event.getWaitingList().put(id, new StudentEntry(student,null));
					ll.addedToWaitingList(student);
				} else {
					ll.errorMessage("Allerede lagt til", student.getFirstname()+ " " + student.getLastname() + " er i ventelisten allerede");
					log.info(student.getFirstname()+ " " + student.getLastname() + " er i ventelisten allerede");
				}
			}
			
		} else { // ventelista er åpen
			int restPlasser = event.getCapacity()-getNumberOfAdmitted();
			log.debug("Restplasser før " + student.getFirstname()+ " " + student.getLastname() + " eventuelt slipper inn");
			if( getFreeFlow() || restPlasser > 0 ) {
				if(event.getListOfParticipants().containsKey(id)) { // påmeldt, se om han er admittet fra før
					if(!event.getListOfParticipants().get(id).enteredIsNotFalseOrNull()) {
						event.getListOfParticipants().get(id).setEntered(true);
						ll.admitted(student);
						if(restPlasser<=0)
							event.setCapacity(event.getCapacity()+1);
						log.debug("slapp inn :" + student.getFirstname() + " " + student.getLastname());
						return;
					} else {
						ll.errorMessage("Allerede innsluppet", student.getFirstname()+ " " + student.getLastname() + " har sluppet inn allerede");
						log.info(student.getFirstname()+ " " + student.getLastname() + " har sluppet inn allerede");
					}
				} else { // ikke påmeldt, og/eller står på venteliste
					if(!event.getWaitingList().containsKey(id)) { // ikke på ventelista, setter han på og fortsetter
						event.getWaitingList().put(id, new StudentEntry(student,null));
						//ll.addedToWaitingList(student);
						log.debug("satt :" + student.getFirstname() + " " + student.getLastname() + " på ventelista");
					}
					if(getNextInWaitingListLine() != null && getNextInWaitingListLine().getStudent().getUserId() == student.getUserId()) { 
						if(student.isHasAccess()) { // har tilgang, slipp inn
							event.getWaitingList().get(id).setEntered(true);
							if(restPlasser<=0)
								event.setCapacity(event.getCapacity()+1);
							ll.admitted(student);
							log.debug("slapp inn :" + student.getFirstname() + " " + student.getLastname());
							return;
						} else { // har ikke tilgang, men kan komme inn om det er tillatt for alle
							if(getAllowEverybody()) {
								event.getWaitingList().get(id).setEntered(true);
								if(restPlasser<= 0)
									event.setCapacity(event.getCapacity()+1);
								ll.admitted(student);
								log.debug("slapp inn :" + student.getFirstname() + " " + student.getLastname() + 
										", som egentlig ikke hadde tilgang. allowAll bør derfor være satt til true");
								return;
							} else {
								ll.errorMessage("Ikke tilgang", "Hør med bedriften om du kan tillate alle trinn");
								log.info(student.getFirstname()+ " " + student.getLastname() + " har ikke tilgang. Sjekk allowAll.");
							}
						}
					} else { // ikke først, noen andre har rett til å komme inn før han.
						if(saveState.getEvent().getWaitingList().get(student.getUserId()) != null && saveState.getEvent().getWaitingList().get(student.getUserId()).enteredIsNotFalseOrNull()){
							ll.errorMessage("Allerede innsluppet", student.getFirstname()+ " " + student.getLastname() + " har sluppet inn allerede");
							log.info(student.getFirstname()+ " " + student.getLastname() + " har sluppet inn allerede");
						} else if(saveState.getEvent().getListOfParticipants().get(student.getUserId()) != null && saveState.getEvent().getListOfParticipants().get(student.getUserId()).enteredIsNotFalseOrNull()){
							ll.errorMessage("Allerede innsluppet", student.getFirstname()+ " " + student.getLastname() + " har sluppet inn allerede");
							log.info(student.getFirstname()+ " " + student.getLastname() + " har sluppet inn allerede");							
						} else {
							ll.errorMessage("Ikke førstemann", student.getFirstname()+ " " + student.getLastname() + " står ikke først i ventelista");
							log.info(student.getFirstname()+ " " + student.getLastname() + " står ikke først i ventelista");
						}
					}
				}
			
			} else {
				log.info("Ikke plass til " + student.getFirstname()+ " " + student.getLastname() + ". Sjekk freeflow");
				ll.errorMessage("Fullt på arrangementet", "Kapasiteten må endres, eller \"fri flyt\" aktiveres. Hør med bedriften!");
			}	
		}
		return;
	}


	@Override
	public int getNumberOfWaiting() {
		int sum = 0;
		for(StudentEntry entry : getWaitingList())
			if(entry.getEntered()==null)
				sum++;
		return sum;
	}

	@Override
	public int getNumberOfNonAllowedWaiting() {
		int sum = 0;
		for(StudentEntry entry : getWaitingList())
			if(entry.getEntered()==null && !entry.getStudent().isHasAccess())
				sum++;
		return sum;
	}
	
	@Override
	public void setEditingUser(Student student) {
		if(student!=null){
			editStudent = student;
		} else {
			editStudent = null;
		}
		if(editStudent != null){
			state = LogicState.editingUser;
		} else {
			state = LogicState.normal;
		}
	}
}
