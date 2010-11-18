package no.abakus.bedcard.gui.panels.content.list;

import no.abakus.bedcard.storage.domainmodel.BedCardSaveState;

public class BedCardSaveStateFilterList extends FilterList<BedCardSaveState> {
	private boolean showOnlyNotSaved;
	
	public BedCardSaveStateFilterList() {
		super();
		showOnlyNotSaved = true;
	}
	@Override
	protected boolean includeEntry(BedCardSaveState entry) {
		boolean matchSaved = true;
		if(showOnlyNotSaved)
			matchSaved = !entry.isSavedToAbakus();
		return matchSaved;
	}

	public void updateFilter(boolean showOnlyNotSaved) {
		this.showOnlyNotSaved = showOnlyNotSaved;
		filtersChanged();
	}

}
