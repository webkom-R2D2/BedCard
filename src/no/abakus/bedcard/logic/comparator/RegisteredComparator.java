package no.abakus.bedcard.logic.comparator;

import java.util.Comparator;
import no.abakus.bedcard.storage.domainmodel.StudentEntry;

public class RegisteredComparator implements Comparator<StudentEntry> {

	public RegisteredComparator() {
	}

	@Override
	public int compare(StudentEntry arg0, StudentEntry arg1) {
		int ret = (arg0.getStudent().getFirstname()).compareTo(arg1.getStudent().getFirstname());
		if(ret == 0){
			ret = (arg0.getStudent().getLastname()).compareTo(arg1.getStudent().getLastname());
		}
		return ret;
	}
}
