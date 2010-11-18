package no.abakus.naut.ws.bedcard;

import java.util.Date;
import java.util.List;

import no.abakus.naut.entity.event.enums.RegistrationStatus;
import no.abakus.naut.entity.news.Type;

/**
 * Web Service interface for communication between BedCardClient and NAUT (abakus.no) 
 * 
 * @author henjens
 * @author Erik Drolshammer
 * @author Åsmund Eldhuset
 */
public interface BedCardService {
	/**
	 * Loads a single event.
	 * @param id	The id of the event.
	 */
	EventDto getEvent(Long id);
	
	/**
	 * Loads all events of a certain type that have a start date in the range [from, to).
	 * @param type	The desired type. If null, all events in the range will be loaded.
	 * @param from	The start of the date range (the time part will be taken into account - if the date is 24.09.2007 15:00, an event that starts at 14:00 that day will not be loaded).
	 * @param to	The end of the date range (
	 * @return
	 */
	List<EventDto> findEvents(Type type, Date from, Date to);
	
	/**
	 * Loads all users that have registered for the given event and have the given registration status.
	 */
	List<UserDto> getRegistrants(Long eventId, RegistrationStatus status);
	
	/**
	 * Marks all the given users as being present or absent at the given event.
	 * @param eventId		The event in question.
	 * @param present		If true, the users will be marked as present, 
	 * 						and if any of the given users were not registered, 
	 * 						they will be registered as well.
	 * 						If false or null, the users will be marked as 
	 * 						absent or unknown, respectively.
	 * @param registrants	The list of users to mark.
	 */
	void setRegistrantPresence(Long eventId, Boolean present, List<Long> registrants);
	
	/**
	 * Loads all users of the Datateknikk-*, Komtek-* and Stipendiat groups.
	 * The hasAccess field of each returned user object will indicate whether 
	 * or not that user has permission to go to the given event.
	 * @param eventId	The id of the event to check permissions for.
	 */
	List<UserDto> getAllActiveStudents(Long eventId);
	
	/**
	 * Updates the database record for the given user with the data that is passed to the method.
	 * @param user	The data with which the database should be updated. Fields that are null will be ignored.
	 * 				The username will always be ignored (i.e., usernames cannot be changed through BedCard).
	 * 				For instance, if someone's card number is to be updated, the id and card number fields should be filled, 
	 * 				and everything else should be null.
	 * @throws	IllegalArgumentException	In case no user with the specified id exists.
	 */
	void updateStudent(UserDto user);
	
	List<Type> getAllTypes();
}
