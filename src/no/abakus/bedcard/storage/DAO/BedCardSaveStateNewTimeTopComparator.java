package no.abakus.bedcard.storage.DAO;

import java.util.Comparator;

import no.abakus.bedcard.storage.domainmodel.BedCardSaveState;

public class BedCardSaveStateNewTimeTopComparator implements Comparator<BedCardSaveState> {

	public BedCardSaveStateNewTimeTopComparator() {
	}

	@Override
	public int compare(BedCardSaveState arg0, BedCardSaveState arg1) {
		return arg1.getEvent().getStartTime().compareTo(arg0.getEvent().getStartTime());
	}
}
