package no.abakus.bedcard.gui.panels.content.list;

import no.abakus.bedcard.storage.domainmodel.StudentEntry;

public class StudentEntryFilterList extends FilterList<StudentEntry> {
	private String brukernavn;
	private String navn;
	private boolean showOnlyNotAdmitted;
	
	public StudentEntryFilterList() {
		super();
		this.brukernavn = "";
		this.navn = "";
		showOnlyNotAdmitted = false;
	}
	@Override
	protected boolean includeEntry(StudentEntry entry) {
		boolean matchBrukernavn = true;
		boolean matchNavn = true;
		boolean matchAdmitted = true;
		
		if(brukernavn.length()>0)
			matchBrukernavn = entry.getStudent().getUsername().toLowerCase().contains(brukernavn.toLowerCase());
		if(navn.length()>0)
			matchNavn = (entry.getStudent().getFirstname() + " "  + entry.getStudent().getLastname()).toLowerCase().contains(navn.toLowerCase());
		if(showOnlyNotAdmitted)
			matchAdmitted = !entry.enteredIsNotFalseOrNull();
		
		return matchBrukernavn && matchNavn && matchAdmitted;
	}
	public void setBrukernavn(String brukernavn) {
		this.brukernavn = brukernavn;
		filtersChanged();
	}
	public void setNavn(String navn) {
		this.navn = navn;
		filtersChanged();
	}
	public void updateFilter(String brukernavn, String navn,
			boolean showAll) {
		this.brukernavn = brukernavn;
		this.navn = navn;
		this.showOnlyNotAdmitted = showAll;
		filtersChanged();
	}
	public boolean isShowOnlyNotAdmitted() {
		return showOnlyNotAdmitted;
	}
	public void setShowOnlyNotAdmitted(boolean showOnlyNotAdmitted) {
		this.showOnlyNotAdmitted = showOnlyNotAdmitted;
		filtersChanged();
	}
	public String getBrukernavn() {
		return brukernavn;
	}
	public String getNavn() {
		return navn;
	}
}
