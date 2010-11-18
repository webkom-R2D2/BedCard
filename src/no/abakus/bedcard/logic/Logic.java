package no.abakus.bedcard.logic;

import java.util.ArrayList;
import java.util.Date;

import no.abakus.bedcard.logic.cardreader.CardReader;
import no.abakus.bedcard.storage.domainmodel.BedCardSaveState;
import no.abakus.bedcard.storage.domainmodel.Event;
import no.abakus.bedcard.storage.domainmodel.SavedState;
import no.abakus.bedcard.storage.domainmodel.Student;
import no.abakus.bedcard.storage.domainmodel.StudentEntry;
import no.abakus.naut.entity.news.Type;

/**
 * @author chrino
 */
public interface Logic {
	
	public boolean checkInStudent(String username);
	public void loadFromFile(long eventID, Type type);
	public void loadFromAbakus(long eventID) throws LogicException;
	public void reconnectReaders();
	public void removeStudentFromWaitinglist(Student student);
	public void resetBedCard();
	public SavedState save();
	public void saveRegistrationStatus(StudentEntry studentEntry);
	public void saveStudent(Student student);
	public boolean saveToFile();
	public void updateMaxParticipants(int number);
	public ArrayList<CardReader> getAllReaders();
	public ArrayList<Type> getAllTypes();
	public ArrayList<Type> getAllSavedTypes();
	public Event getEvent();
	public boolean getFreeFlow();
	public ArrayList<Event> getListOfEvents(Date from, Date to, Type type);
	public ArrayList<BedCardSaveState> getListOfSavedEvents(Type type);
	public Student getStudent(String username);
	public boolean getWaitingListStatus();
	public int getNumberOfAdmitted();
	public int getNumberOfWaiting();
	public int getNumberOfNonAllowedWaiting();
	public void connect(String username, String password) throws LogicException;
	public boolean isAuthenticated();
	public void setFreeFlow(boolean on);
	public void setAllowEverybody(boolean allow);
	public boolean getAllowEverybody();
	public void setWaitinglistStatus(boolean opened);
	public void setLogicListener(LogicListener ll);
	public StudentEntry getNextInWaitingListLine();
	public boolean returnUnkownUsernameToCard(String username);
	public void connectReaders();
	public void reconnectReader(CardReader reader);
	public ArrayList<StudentEntry> getListOfParticipants();
	public ArrayList<StudentEntry> getWaitingList();
	public void setEditingUser(Student studentToEdit);
}
