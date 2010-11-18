package no.abakus.bedcard.logic;

public enum LogicState {
	waitingForUnknownUsername("Waiting For Unknown Username"),
	normal("Normal State"),
	editingUser("Editing User");
	
	private String name;
	
	private LogicState(String name) {
		this.name = name;
	}
	
	public String toString(){
		return name;
	}
}
