package no.abakus.bedcard.storage.ws;

import java.util.Date;
import java.util.List;

import no.abakus.naut.entity.event.enums.RegistrationStatus;
import no.abakus.naut.entity.news.Type;
import no.abakus.naut.ws.bedcard.EventDto;
import no.abakus.naut.ws.bedcard.UserDto;

public interface AbakusNoBedCardService {
	boolean connect(String username, String password) throws AbakusNoException;
	EventDto getEvent(Long id) throws AbakusNoException;
	List<EventDto> findEvents(Type type, Date from, Date to) throws AbakusNoException;
	List<UserDto> getRegistrants(Long eventId, RegistrationStatus status) throws AbakusNoException;
	void setRegistrantPresence(Long eventId, Boolean present, List<Long> registrants) throws AbakusNoException;
	List<UserDto> getAllActiveStudents(Long eventId) throws AbakusNoException;
	void updateStudent(UserDto user) throws AbakusNoException;
	List<Type> getAllTypes() throws AbakusNoException;
}
