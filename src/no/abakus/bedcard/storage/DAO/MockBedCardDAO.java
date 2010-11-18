package no.abakus.bedcard.storage.DAO;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import no.abakus.bedcard.storage.domainmodel.BedCardSaveState;
import no.abakus.bedcard.storage.domainmodel.Event;
import no.abakus.bedcard.storage.domainmodel.SavedState;
import no.abakus.bedcard.storage.domainmodel.Student;
import no.abakus.bedcard.storage.domainmodel.StudentEntry;
import no.abakus.bedcard.storage.filemanager.FileManagerException;
import no.abakus.naut.entity.news.Type;

public class MockBedCardDAO implements BedcardDAO {
	
	private ArrayList<Type> typeList;
	private ArrayList<BedCardSaveState> eventList;
	private ArrayList<Event> events;
	private HashMap<String, Student> studentList;
	private HashMap<Long, StudentEntry> studentEntryList;
	private BedCardSaveState state = new BedCardSaveState();
	
	public MockBedCardDAO() {
		//Typeliste til bruk ved testing
		typeList = new ArrayList<Type>();
		Type type_bedpres = new Type();
		type_bedpres.setId(2356L);
		type_bedpres.setName("Bedpres");
		type_bedpres.setDescription("Bedriftspresentasjon(test)");
		
		Type type2 = new Type();
		type2.setId(1337L);
		type2.setName("Utflukt");
		type2.setDescription("Ut på tur, aldri sur");
		
		typeList.add(type_bedpres);
		typeList.add(type2);
		
		//Studentliste
		Student student1 = new Student();
		student1.setCardNumber(123);
		student1.setFirstname("Sigve");
		student1.setHasAccess(true);
		student1.setLastname("Albertsen");
		student1.setRfidCardNumber(456L);
		student1.setUserId(1337L);
		student1.setUsername("albretse");
		student1.setVip(true);
		
		Student student2 = new Student();
		student2.setCardNumber(789);
		student2.setFirstname("Christian");
		student2.setHasAccess(true);
		student2.setLastname("Snowboard");
		student2.setRfidCardNumber(101L);
		student2.setUserId(3531L);
		student2.setUsername("chrino");
		student2.setVip(false);
		
		studentList = new HashMap<String, Student>();
		studentList.put(student1.getUsername(), student1);
		studentList.put(student2.getUsername(), student2);
		
		//Studententry
		studentEntryList = new HashMap<Long, StudentEntry>();
		StudentEntry studE1 = new StudentEntry();
		studE1.setEntered(true);
		studE1.setStudent(student1);
		StudentEntry studE2 = new StudentEntry();
		studE2.setEntered(false);
		studE2.setStudent(student2);
		studentEntryList.put(1L,studE1);
		studentEntryList.put(1L,studE2);
		
		//Eventliste
		eventList = new ArrayList<BedCardSaveState>();
		Event event1 = new Event();
		event1.setListOfParticipants(studentEntryList);
		event1.setAllowNonUsers(false);
		event1.setBinding(true);
		event1.setCapacity(50);
		event1.setConsidersDots(true);
		event1.setDescription("Testevent til testing..doh");
		event1.setDotAmount(1);
		event1.setEndTime(new Date(10122007646843843L));
		event1.setId(666L);
		event1.setLocation("H3");
		event1.setMemberPrice(0);
		event1.setPrice(0);
		event1.setStartTime(new Date(05122007313154153L));
		event1.setSummary("Bla bla bra bedrift bla bla");
		event1.setTitle("Goohoo");
		event1.setType(type_bedpres);
		event1.setVisible(true);
		
		eventList.add(state);
		events = new ArrayList<Event>();
		events.add(event1);
		//BedCardSaveState
		
		state.setAllStudents(studentList);
		state.setEvent(event1);
		state.setFreeFlow(true);
		state.setSavedToAbakus(false);
		
	}
	
	@Override
	public boolean connect(String username, String password) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public ArrayList<Type> getAllTypes() {
		// TODO Auto-generated method stub
		return typeList;
	}

	@Override
	public ArrayList<Event> getListOfEvents(Date from, Date to, Type type) {
		// TODO Auto-generated method stub
		return events;
	}

	@Override
	public ArrayList<BedCardSaveState> getListOfSavedEvents(Type type) {
		// TODO Auto-generated method stub
		return eventList;
	}

	@Override
	public BedCardSaveState loadFromAbakus(Long eventId) {
		// TODO Auto-generated method stub
		return state;
	}

	@Override
	public BedCardSaveState loadFromFile(Long eventId, Type type) {
		// TODO Auto-generated method stub
		return state;
	}

	@Override
	public SavedState save(BedCardSaveState save) {
		// TODO Auto-generated method stub
		return SavedState.savedToBoth;
	}

	@Override
	public boolean saveToFile(BedCardSaveState save) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public ArrayList<Type> getAllSavedTypes() throws FileManagerException {
		// TODO Auto-generated method stub
		return null;
	}
	

}
