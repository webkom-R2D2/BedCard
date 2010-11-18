package no.abakus.bedcard.logic.comparator;

import java.util.Comparator;

import org.apache.log4j.Logger;

import no.abakus.bedcard.logic.Logic;
import no.abakus.bedcard.storage.domainmodel.Student;
import no.abakus.bedcard.storage.domainmodel.StudentEntry;

public class WaitingComparator implements Comparator<StudentEntry> {
	private static Logger log = Logger.getLogger(WaitingComparator.class);
	private Logic logic;
	
	public WaitingComparator(Logic logic) {
		this.logic = logic;
	}



	@Override
	public int compare(StudentEntry arg0, StudentEntry arg1) {
		int score0;
		int score1;
		
		Student stud0 = arg0.getStudent();
		Student stud1 = arg1.getStudent();
		
		/*	
		 * Score:
		 * 1 - Studenten har egentlig ikke lov til å slippe inn
		 * 2 - Studenten har lov til å slippe inn
		 * 3 - Studenten har lov til å slippe inn og har satt seg på venteliste på nett
		 * 4 - Studenten er VIP
		 */
		
		
		// Score for student 0
 		if(!stud0.isHasAccess()){
			score0 = 1;
		}
		else if(stud0.isVip()){
			score0 = 4;
		}
		else if(logic.getEvent().getNetWaitingList().contains(stud0)){
			log.debug("Er på nettventelisten");
			score0 = 3;
		}
		else { // møtt opp og meldt seg på ventelista
			score0 = 2;
		}
 		
 		// Score for student 1
		if(!stud1.isHasAccess()){
			score1 = 1;
		}
		else if(stud1.isVip()){
			score1 = 4;
		}
		else if(logic.getEvent().getNetWaitingList().contains(stud1)){
			log.debug("Er på nettventelisten");
			score1 = 3;
		}
		else { // møtt opp og meldt seg på ventelista
			score1 = 2;
		}
		
		//Sjekker på dato eller venteliste alt ettersom hvis de er like.
		if(score1==score0){
			//De er like		
			if(score1 == 3){
				//Sorter på plass i ventelista
				return logic.getEvent().getNetWaitingList().indexOf(stud0)-logic.getEvent().getNetWaitingList().indexOf(stud1);
			} else {
				//Sorter på dato de var lagt til
				return arg0.getEnteredTime().compareTo(arg1.getEnteredTime());				
			}
		}
		
		
		return score1-score0;
	}



}
