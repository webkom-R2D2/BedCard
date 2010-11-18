package no.abakus.bedcard.storage.DAO;

import java.util.ArrayList;
import java.util.Date;

import no.abakus.bedcard.storage.domainmodel.BedCardSaveState;
import no.abakus.bedcard.storage.domainmodel.Event;
import no.abakus.bedcard.storage.domainmodel.SavedState;
import no.abakus.bedcard.storage.filemanager.FileManagerException;
import no.abakus.bedcard.storage.ws.AbakusNoException;
import no.abakus.naut.entity.news.Type;

/**
 * Interface for the logic. 
 * Database Access Object
 * 
 * @author ormestoy
 * @author albretse
 */
public interface BedcardDAO {
	public ArrayList<Type> getAllTypes() throws AbakusNoException;
	public ArrayList<Type> getAllSavedTypes() throws FileManagerException;
	public BedCardSaveState loadFromFile(Long eventId, Type type) throws FileManagerException;
	public BedCardSaveState loadFromAbakus(Long eventId) throws AbakusNoException;
	public SavedState save(BedCardSaveState save);
	public boolean saveToFile(BedCardSaveState save) throws FileManagerException;
	public ArrayList<Event> getListOfEvents(Date from, Date to, Type type) throws AbakusNoException;
	public ArrayList<BedCardSaveState> getListOfSavedEvents(Type type) throws FileManagerException;
	public boolean connect(String username, String password) throws AbakusNoException;
}
