package no.abakus.bedcard.storage.filemanager;

import java.util.ArrayList;

import no.abakus.bedcard.storage.domainmodel.BedCardSaveState;
import no.abakus.naut.entity.news.*;

/**
 * Handles saving and loading from the file system
 * 
 * @author chrino
 *
 */
public interface FileManager {
	public boolean saveToFile(BedCardSaveState save) throws FileManagerException;
	public BedCardSaveState loadFromFile(Long id, Type type) throws FileManagerException;
	public ArrayList<BedCardSaveState> getListOfSavedEvents(Type type) throws FileManagerException;
	public ArrayList<Type> getAllTypes() throws FileManagerException;
}
