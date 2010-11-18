package no.abakus.bedcard.storage.domainmodel;

import java.util.Calendar;
import java.util.Date;

public class StudentEntry {
	private Student student;
	private Boolean entered;
	private Date enteredTime;

	public StudentEntry(Student student, Boolean entered) {
		super();
		this.student = student;
		this.entered = entered;
		this.enteredTime = Calendar.getInstance().getTime();
	}
	
	
	public StudentEntry(Student student, Boolean entered, Date enteredTime) {
		super();
		this.student = student;
		this.entered = entered;
		this.enteredTime = enteredTime;
	}


	public StudentEntry() {
		super();
	}

	public Student getStudent() {
		return student;
	}
	public void setStudent(Student student) {
		this.student = student;
	}
	public boolean enteredIsNotFalseOrNull() {
		if(entered == null)
			return false;
		return entered;
	}
	public void setEntered(Boolean entered) {
		this.entered = entered;
	}

	public Boolean getEntered() {
		return entered;
	}

	public Date getEnteredTime() {
		return enteredTime;
	}

	public void setEnteredTime(Date enteredTime) {
		this.enteredTime = enteredTime;
	}
	
}
