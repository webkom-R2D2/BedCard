package no.abakus.bedcard.gui.panels.content.list;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class FilterList<T> {
    private List<T> filteredEntries;
    private List<T> selection;

    public FilterList() {
        selection = Collections.emptyList();
        selection = Collections.unmodifiableList(selection);
        filteredEntries = new ArrayList<T>(1);
    }
        
    public List<T> getFilteredEntries() {
		return filteredEntries;
	}


	public void setFilteredEntries(List<T> filteredEntries) {
		this.filteredEntries = filteredEntries;
	}


	public final void setSelection(List<T> selection) {
        if (selection == null) {
            selection = Collections.emptyList();
        }
        this.selection = new ArrayList<T>(selection);
        this.selection = Collections.unmodifiableList(this.selection);
        filtersChanged();
    }
    
    public final List<T> getSelection() {
        return selection;
    }
    
    public void filtersChanged() {
        if (filteredEntries == null) {
        	filteredEntries = new ArrayList<T>();
        } else {
        	filteredEntries.clear();
        }
        for (T entry : selection) {
            if (includeEntry(entry)) {
                filteredEntries.add(entry);
            }
        }
    }

    protected abstract boolean includeEntry(T entry);
}
