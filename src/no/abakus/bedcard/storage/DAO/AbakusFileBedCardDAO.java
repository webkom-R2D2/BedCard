package no.abakus.bedcard.storage.DAO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import no.abakus.bedcard.storage.domainmodel.BedCardSaveState;
import no.abakus.bedcard.storage.domainmodel.Event;
import no.abakus.bedcard.storage.domainmodel.SavedState;
import no.abakus.bedcard.storage.domainmodel.Student;
import no.abakus.bedcard.storage.domainmodel.StudentEntry;
import no.abakus.bedcard.storage.filemanager.BedCardFileManager;
import no.abakus.bedcard.storage.filemanager.FileManagerException;
import no.abakus.bedcard.storage.ws.AbakusNoBedCardService;
import no.abakus.bedcard.storage.ws.AbakusNoException;
import no.abakus.bedcard.storage.ws.NERDClient;
import no.abakus.bedcard.storage.ws.WSClient;
import no.abakus.naut.entity.event.enums.RegistrationStatus;
import no.abakus.naut.entity.news.Type;
import no.abakus.naut.ws.bedcard.EventDto;
import no.abakus.naut.ws.bedcard.UserDto;

import org.apache.log4j.Logger;

public class AbakusFileBedCardDAO implements BedcardDAO {
	
	private AbakusNoBedCardService client;
	private BedCardFileManager fileManager;
	private static Logger log = Logger.getLogger(AbakusFileBedCardDAO.class);
	
	public AbakusFileBedCardDAO() {
		client = new NERDClient();
		fileManager = new BedCardFileManager();
	}
	@Override
	public boolean connect(String username, String password) throws AbakusNoException {
		return client.connect(username, password);
	}

	@Override
	public ArrayList<Type> getAllTypes() throws AbakusNoException {
		ArrayList<Type> typer = new ArrayList<Type>(client.getAllTypes());
		Collections.sort(typer, new TypeComparator());
		return typer;
	}

	@Override
	public ArrayList<Event> getListOfEvents(Date from, Date to, Type type) throws AbakusNoException {
		ArrayList<Event> returnList = new ArrayList<Event>();
		
		List<EventDto> eventList = client.findEvents(type, from, to);
		
		for (EventDto e : eventList) {
			//TODO: Ugly checks for fields being null have been added.
			//The price fields are deprecated and should be removed from the naut codebase, 
			//and several of the other fields are guaranteed not to be null.
			//First, the naut codebase and database should be improved, then this code should be improved.
			Event event = new Event(e.getId(), e.getTitle(), e.getLocation(), 
					e.getDescription(), e.getSummary(), (e.getCapacity() == null ? 0 : e.getCapacity()), (e.getPrice() == null ? 0 : e.getPrice()), 
					(e.getMemberPrice() == null ? 0 : e.getMemberPrice()), (e.getVisible() == null ? false : e.getVisible()), (e.getBinding() == null ? false : e.getBinding()), e.getStartTime(),
					e.getEndTime(), (e.getDotAmount() == null ? 0 : e.getDotAmount()), (e.getConsidersDots() == null ? false : e.getConsidersDots()), (e.getAllowNonUsers() == null ? false : e.getAllowNonUsers()), 
					e.getType(), null, null, null, null, null);
			
			returnList.add(event);
		}
		
		return returnList;
	}

	@Override
	public ArrayList<BedCardSaveState> getListOfSavedEvents(Type type) throws FileManagerException {
		try {
			ArrayList<BedCardSaveState> list = fileManager.getListOfSavedEvents(type);
			Collections.sort(list, new BedCardSaveStateNewTimeTopComparator());
			return list;
		} catch(Exception e) {
			log.error("Feil under henting av eventer av spesiell type.\n Type: " + e.getClass() + "\n Melding: " + e.getMessage());
			throw new FileManagerException("Feil under henting av eventer av spesiell type");
		}
	}

	@Override
	public BedCardSaveState loadFromAbakus(Long eventId) throws AbakusNoException {
		BedCardSaveState state;
		HashMap<Long, StudentEntry> participants = new HashMap<Long, StudentEntry>();
		ArrayList<Student> netWaitingList = new ArrayList<Student>();
		ArrayList<Student> netWaitingListBanned = new ArrayList<Student>();
		Event event;
		HashMap<String, Student> allStudentsList = new HashMap<String, Student>();

		//Laster inn event
		EventDto e = client.getEvent(eventId);
		
		//Henter alle aktive studenter
		for (UserDto user : client.getAllActiveStudents(eventId)) {
			Student student = convertStudent(user);
			allStudentsList.put(student.getUsername(), student);
		}
		
		//Henter ut admitted students
		for (UserDto user : client.getRegistrants(eventId, RegistrationStatus.ADMITTED)) {
			Student student = allStudentsList.get(user.getUsername());
			log.debug("Legger til i admitted: " + user);
			log.debug("Hentet fra allUsers (" + user.getUsername() + ") - " + student);
			if(student == null) { // Shouldn't happen in theory, but sometimes people who are not in 1. to 5. grade, stipendiat or is an external user get registered somehow...
				student = convertStudent(user);
				student.setHasAccess(true); // This probably isn't needed, but I guess it makes sense that admitted users should have access
				allStudentsList.put(student.getUsername(), student);
				log.warn("Fant ikke bruker '" + student.getUsername() + "' i listen over alle studenter");
			}
			participants.put(student.getUserId(), new StudentEntry(student, null));
		}
		
		//Henter ut studenter i prim�rventek�
		for (UserDto user : client.getRegistrants(eventId, RegistrationStatus.PRIMARY_QUEUE)) {
			Student student = allStudentsList.get(user.getUsername());
			log.debug("Legger til i prim�rventek�: " + user);
			log.debug("Hentet fra allUsers (" + user.getUsername() + ") - " + student);
			if(student == null)
				throw new AbakusNoException("En som var p� venteliste finnes ikke blandt aktive studenter");
			netWaitingList.add(student);
		}
		
		//Henter ut studenter i sekund�rventek�
		for (UserDto user : client.getRegistrants(eventId, RegistrationStatus.SECONDARY_QUEUE)) {
			Student student = allStudentsList.get(user.getUsername());
			log.debug("Legger til i sekund�rventek�: " + user);
			log.debug("Hentet fra allUsers (" + user.getUsername() + ") - " + student);
			if(student == null)
				throw new AbakusNoException("En som var i sekund�r ventelisten finnes ikke blandt aktive studenter");
			netWaitingListBanned.add(student);
		}
		
		
		//Henter event
		//TODO: See comment at line 54
		event = new Event(e.getId(), e.getTitle(), e.getLocation(), 
				e.getDescription(), e.getSummary(), (e.getCapacity() == null ? 0 : e.getCapacity()), (e.getPrice() == null ? 0 : e.getPrice()), 
				(e.getMemberPrice() == null ? 0 : e.getMemberPrice()), (e.getVisible() == null ? false : e.getVisible()), (e.getBinding() == null ? false : e.getBinding()), e.getStartTime(),
				e.getEndTime(), (e.getDotAmount() == null ? 0 : e.getDotAmount()), (e.getConsidersDots() == null ? false : e.getConsidersDots()), (e.getAllowNonUsers() == null ? false : e.getAllowNonUsers()), 
				e.getType(), participants, netWaitingList, netWaitingListBanned, new HashMap<Long, StudentEntry>(), null);
		

		
		state = new BedCardSaveState(event, allStudentsList, new HashMap<Long, Student>(), false, false, false, false);
		
		return state;
	}

	@Override
	public BedCardSaveState loadFromFile(Long eventId, Type type) throws FileManagerException {
		return fileManager.loadFromFile(eventId, type);
	}

	@Override
	public SavedState save(BedCardSaveState saveState){
		//Lagre f�rst til Abakus, s� til fil
		boolean savedToAbakus = true;

		try {
			//Lagre til Abakus
			ArrayList<Long> okUsers = new ArrayList<Long>();
			ArrayList<Long> notOkUsers = new ArrayList<Long>();
			ArrayList<Long> notLetInFromWaitingListUsers = new ArrayList<Long>();
			//Participants
			for(StudentEntry entry : saveState.getEvent().getListOfParticipants().values()){
				if(entry.getEntered()!=null){
					if(entry.enteredIsNotFalseOrNull()){
						okUsers.add(entry.getStudent().getUserId());
					} else {
						notOkUsers.add(entry.getStudent().getUserId());
					}
				}
			}
			//WaitingList
			for(StudentEntry entry : saveState.getEvent().getWaitingList().values()){
				if(entry.getEntered()!=null){
					if(entry.enteredIsNotFalseOrNull()){
						okUsers.add(entry.getStudent().getUserId());
					} else if(entry.getEntered() != null){
						//Disse har man trykket nei p�, alts� fikk de mulighet men m�tte ikke opp.
						notOkUsers.add(entry.getStudent().getUserId());
					} else {
						notLetInFromWaitingListUsers.add(entry.getStudent().getUserId());
					}
				}
			}
			
			//Lagre status til abakus
			client.setRegistrantPresence(saveState.getEvent().getId(), true, okUsers);
			client.setRegistrantPresence(saveState.getEvent().getId(), false, notOkUsers);
			client.setRegistrantPresence(saveState.getEvent().getId(), null, notLetInFromWaitingListUsers);
			log.debug("Satt ok oppm�te p� " + okUsers.size() + " deltakere");
			log.debug("Satt IKKE ok oppm�te p� " + notOkUsers.size() + " deltakere");
			log.debug("Lagrer ny info om " + saveState.getAlteredStudents().values().size() + " brukere");
			//Lagre alle endringer gjort til brukere
			for (Student student : saveState.getAlteredStudents().values()) {
				UserDto userDto = new UserDto();
				userDto.setUserBeanId(student.getUserId());
				userDto.setCardNumber(student.getCardNumber());
				userDto.setRfidCardNumber(student.getRfidCardNumber());
				userDto.setCn(student.getFirstname());
				userDto.setSn(student.getLastname());
				userDto.setUsername(student.getUsername());
				log.debug("Lagrer info om bruker: " + userDto.getUsername());
				client.updateStudent(userDto);
				log.debug("Info lagret");
			}
			log.debug("I bunnen, saved: " + savedToAbakus);
		} catch (AbakusNoException e1) {
			log.error("Klarte ikke lagre til abakus:");
			log.error(e1.getMessage());
			savedToAbakus = false;
		}
		if(savedToAbakus){
			log.debug("Fikk lagret eventen: " + saveState.getEvent().getId() + ", til abakus");
			saveState.setSavedToAbakus(true);
		} else {
			log.debug("Fikk IKKE lagret eventen: " + saveState.getEvent().getId() + ", til abakus");
			saveState.setSavedToAbakus(false);
		}
		
		//Lagre til fil
		boolean savedToFile = false;
		try {
			savedToFile = saveToFile(saveState);
		} catch (FileManagerException e){
			savedToFile = false;
		}
		if(savedToFile&&savedToAbakus){
			return SavedState.savedToBoth;
		} else if(savedToFile){
			return SavedState.savedToFileOnly;
		} else if(savedToAbakus){
			return SavedState.savedToAbakusOnly;
		} else {
			return SavedState.savedToNone;
		}
	}

	@Override
	public boolean saveToFile(BedCardSaveState save) throws FileManagerException {
		return fileManager.saveToFile(save);
	}
	
	/*
	 * Metoder som brukes til � konvertere. Brukes rundt i koden.
	 */
	private Student convertStudent(UserDto user){
		log.debug("konverterer student: " + user);
		Student student = new Student();
		student.setUserId(user.getUserBeanId());
		student.setUsername(user.getUsername()); 
		student.setFirstname(user.getCn());
		student.setLastname(user.getSn());
		student.setCardNumber(user.getCardNumber());
		student.setRfidCardNumber(user.getRfidCardNumber()); 
		student.setHasAccess(user.isHasAccess());
		student.setVip(false);
		return student;
	}
	@Override
	public ArrayList<Type> getAllSavedTypes() throws FileManagerException {
		ArrayList<Type> typer = fileManager.getAllTypes();
		Collections.sort(typer, new TypeComparator());
		return typer;
	}
	

}
