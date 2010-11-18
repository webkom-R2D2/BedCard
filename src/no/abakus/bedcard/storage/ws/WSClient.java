package no.abakus.bedcard.storage.ws;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import no.abakus.naut.entity.event.enums.RegistrationStatus;
import no.abakus.naut.entity.news.Type;
import no.abakus.naut.ws.bedcard.BedCardService;
import no.abakus.naut.ws.bedcard.EventDto;
import no.abakus.naut.ws.bedcard.UserDto;

import org.apache.log4j.Logger;
import org.codehaus.xfire.XFireRuntimeException;
import org.codehaus.xfire.fault.XFireFault;

public class WSClient implements AbakusNoBedCardService{
	private static Logger log = Logger.getLogger(WSClient.class);
	private BedCardService service;
	
	//private final static String serviceURL = "https://abakus.no/services/BedCardService";
	private final static String serviceURL = "http://test.abakus.no/naut/services/BedCardService";

	
	@Override
	public boolean connect(String username, String password) throws AbakusNoException {
		/*
	 	 * The code is taken from http://www.vorburger.ch/blog1/2006/10/propagating-acegis-security-context-in.html.
		 * If acegi integration is not required, the following suffices:
		 * Service serviceModel = new ObjectServiceFactory().create(BedCardService.class);
		 * service = (BedCardService) new XFireProxyFactory().create(serviceModel, serviceURL);
		 */
		boolean isConnected = false;
		log.debug("Kobler til");
		try {
			XFireAcegiWSSPropagatingClientFactoryBean bean = new XFireAcegiWSSPropagatingClientFactoryBean(username, password);
			bean.setServiceClass(BedCardService.class);
			bean.setUrl(serviceURL);
			bean.afterPropertiesSet();
			service = (BedCardService) bean.getObject();
			service.getAllTypes();
			isConnected = true;
		}  catch (XFireRuntimeException e) {
			log.error(e.getMessage());
			if (e.getCause() instanceof XFireFault) {
				XFireFault xff = (XFireFault) e.getCause();
				throw new AbakusNoException("Web service fault: " + xff.getMessage());
			} else {
				throw new AbakusNoException("Web service exception");
			}
		} catch (Exception e){
			log.error(e.getMessage());
			throw new AbakusNoException("Feil under oppkobling mot Abakus");
		}
		log.debug("Connected: " + isConnected);
		return isConnected;
	}



	@Override
	public List<EventDto> findEvents(Type type, Date from, Date to) throws AbakusNoException {
		log.debug("Finn eventer : \n" + "Type: " + type + "\n from: " + from + "to: " + to);
		try {
			return service.findEvents(type, from, to);
		} catch (XFireRuntimeException e) {
			log.error(e.getMessage());
			if (e.getCause() instanceof XFireFault) {
				XFireFault xff = (XFireFault) e.getCause();
				throw new AbakusNoException("Web service fault: " + xff.getMessage());
			} else {
				throw new AbakusNoException("Web service exception");
			}
		}
	}



	@Override
	public List<UserDto> getAllActiveStudents(Long eventId) throws AbakusNoException {
		log.debug("Henter alle aktive studenter");
		try {
			return service.getAllActiveStudents(eventId);
		} catch (XFireRuntimeException e) {
			log.error(e.getMessage());
			if (e.getCause() instanceof XFireFault) {
				XFireFault xff = (XFireFault) e.getCause();
				throw new AbakusNoException("Web service fault: " + xff.getMessage());
			} else {
				throw new AbakusNoException("Web service exception");
			}
		}
	}



	@Override
	public List<Type> getAllTypes() throws AbakusNoException {
		try {
			List<Type> typer = service.getAllTypes(); 
			log.debug("Henter alle typer, fant: " + typer.size());
			return typer;
		} catch (XFireRuntimeException e) {
			log.error(e.getMessage());
			if (e.getCause() instanceof XFireFault) {
				XFireFault xff = (XFireFault) e.getCause();
				throw new AbakusNoException("Web service fault: " + xff.getMessage());
			} else {
				throw new AbakusNoException("Web service exception");
			}
		}
	}



	@Override
	public EventDto getEvent(Long id) throws AbakusNoException {
		log.debug("Henter event: " + id);
		try {
			return service.getEvent(id);
		} catch (XFireRuntimeException e) {
			log.error(e.getMessage());
			if (e.getCause() instanceof XFireFault) {
				XFireFault xff = (XFireFault) e.getCause();
				throw new AbakusNoException("Web service fault: " + xff.getMessage());
			} else {
				throw new AbakusNoException("Web service exception");
			}
		}
	}



	@Override
	public List<UserDto> getRegistrants(Long eventId, RegistrationStatus status) throws AbakusNoException {
		log.debug("Henter studenter fra liste: " + status);
		try {
			return service.getRegistrants(eventId, status);
		} catch (XFireRuntimeException e) {
			log.error(e.getMessage());
			if (e.getCause() instanceof XFireFault) {
				XFireFault xff = (XFireFault) e.getCause();
				throw new AbakusNoException("Web service fault: " + xff.getMessage());
			} else {
				throw new AbakusNoException("Web service exception");
			}
		}
	}



	@Override
	public void setRegistrantPresence(Long eventId, Boolean present,
			List<Long> registrants) throws AbakusNoException{
		log.debug("Setter oppm�te p� bruker");
		try {
			service.setRegistrantPresence(eventId, present, registrants);
		} catch (XFireRuntimeException e) {
			log.error(e.getMessage());
			if (e.getCause() instanceof XFireFault) {
				XFireFault xff = (XFireFault) e.getCause();
				throw new AbakusNoException("Web service fault: " + xff.getMessage());
			} else {
				throw new AbakusNoException("Web service exception");
			}
		}
	}

	@Override
	public void updateStudent(UserDto user) throws AbakusNoException{
		log.debug("Oppdaterer bruker");
		try {
			service.updateStudent(user);
		} catch (XFireRuntimeException e) {
			log.error(e.getMessage());
			if (e.getCause() instanceof XFireFault) {
				XFireFault xff = (XFireFault) e.getCause();
				throw new AbakusNoException("Web service fault: " + xff.getMessage());
			} else {
				throw new AbakusNoException("Web service exception");
			}
		}
	}
}
