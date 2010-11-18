package no.abakus.bedcard.storage.filemanager;

import java.util.ArrayList;

import no.abakus.bedcard.storage.domainmodel.BedCardSaveState;
import no.abakus.naut.entity.news.Type;

public class MockFileManager implements FileManager{
	private BedCardSaveState mockState;
	public MockFileManager() {
		mockState = new BedCardSaveState();
	}
	@Override
	public BedCardSaveState loadFromFile(Long id, Type type) {
		return mockState;
	}

	@Override
	public boolean saveToFile(BedCardSaveState save) {
		return true;
	}
	@Override
	public ArrayList<BedCardSaveState> getListOfSavedEvents(Type type) {
		// TODO Auto-generated method stub
		return new ArrayList<BedCardSaveState>();
	}
	@Override
	public ArrayList<Type> getAllTypes() throws FileManagerException {
		// TODO Auto-generated method stub
		return null;
	}

}
