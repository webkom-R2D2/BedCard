package no.abakus.bedcard.storage.ws;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.abakus.naut.entity.event.enums.RegistrationStatus;
import no.abakus.naut.entity.news.Type;
import no.abakus.naut.ws.bedcard.EventDto;
import no.abakus.naut.ws.bedcard.UserDto;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class NERDClient implements AbakusNoBedCardService {

	// public final static String baseUrl = "http://nerddev.abakus.no/";
	public final static String baseUrl = "http://localhost:8000/";
	
	private String cookie = ""; 
	
	//Translate between NAUT and Nerd RegistrationStatus
	private static final Map<RegistrationStatus, String> NAUT_TO_NERD_STATUS = 
        new HashMap<RegistrationStatus, String>() 
        {
            //Unnamed Block.
            {
                put(RegistrationStatus.ADMITTED, "1");
                put(RegistrationStatus.NOT_REGISTERED, "2");
                put(RegistrationStatus.PRIMARY_QUEUE, "3");
                put(RegistrationStatus.SECONDARY_QUEUE,"4");
            }
        };
        
    private static Map<Long, Type> TYPE_MAP = new HashMap<Long, Type>();
	
	@Override
	public boolean connect(String username, String password)
			throws AbakusNoException {
		String data = "username="+username+"&password="+password;
		
		try {
			HttpURLConnection conn = getConnection("user/login/?format=json");
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			
			conn.setRequestProperty("Content-Length", Integer.toString(data.getBytes().length));
			String response = makeRequest(conn, data);
			
			// Take care of cookie data
			String c = conn.getHeaderField("set-cookie");
			int pos = c.indexOf(";");
			cookie = c.substring(0, pos);
			response = response.trim();
			return "OK".equals(response);
		}
		catch (IOException e) {
			throw new AbakusNoException();
		}
	}

	private UserDto parseUser(Element djangoUser, boolean hasAccess){
		
		UserDto user = new UserDto();
		
		HashMap<String, String> userFields = new HashMap<String, String>();
		
		userFields.put("id", djangoUser.getAttribute("pk").getValue());

		List<Element> fields = djangoUser.getChildren();
		
		for (Element field : fields) {
			userFields.put(field.getAttribute("name").getValue(), field.getValue() );
		}
		
		user.setUsername(userFields.get("username"));
		user.setUserBeanId(Long.parseLong(userFields.get("id")));
		user.setCn(userFields.get("name"));
		user.setSn(userFields.get("surname"));
		if (userFields.get("rfid_card_number") == null || "".equals(userFields.get("rfid_card_number"))){
			user.setRfidCardNumber(null);
		}else{
			user.setRfidCardNumber(Long.parseLong(userFields.get("rfid_card_number")));
		}
			
		user.setHasAccess(hasAccess);
		
		return user;
	}
	
	private EventDto parseEvent(Element djangoEvent){
		SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		EventDto event = new EventDto();
		
		HashMap<String, String> eventFields = new HashMap<String, String>();
	
		eventFields.put("id", djangoEvent.getAttribute("pk").getValue());

		List<Element> fields = djangoEvent.getChildren();
		
		for (Element field : fields) {
			eventFields.put(field.getAttribute("name").getValue(), field.getValue() );
		}
	      
		try {
			event.setStartTime(timeFormat.parse(eventFields.get("start_date")));
			event.setEndTime(timeFormat.parse(eventFields.get("end_date")));
		} catch (ParseException e) {
			throw new RuntimeException("Error parsing date from event from django");
		}
		event.setId(Long.parseLong(eventFields.get("id")));
		event.setTitle(eventFields.get("name"));
		event.setType(TYPE_MAP.get(Long.parseLong(eventFields.get("type"))));
		event.setCapacity(Integer.parseInt(eventFields.get("capacity")));
		event.setDescription(eventFields.get("description"));
		event.setLocation(eventFields.get("location"));
		event.setSummary(eventFields.get("intro"));

		return event;
	      
	}
	
	@Override
	public EventDto getEvent(Long id) throws AbakusNoException {
		String response = "";
		Element djangoEvent = null;
		try {
			HttpURLConnection conn = getConnection("event/"+ id + "/?format=xml");
			
			conn.setRequestMethod("GET");
			
			response = makeRequest(conn);
			
			SAXBuilder saxBuilder = new SAXBuilder();
			Document jdomDocument;
			jdomDocument = saxBuilder.build(new StringReader(response));

			djangoEvent = jdomDocument.getRootElement().getChild("object");
			
		} catch (JDOMException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			throw new AbakusNoException();
		}
			
		EventDto event = parseEvent(djangoEvent);
		
		return event;
	}

	@Override
	public List<EventDto> findEvents(Type type, Date from, Date to)
			throws AbakusNoException {
		
		String response = "";
		
		List<EventDto> events = new ArrayList<EventDto>();
		if (type == null){
			return events;
		}
		try {
			HttpURLConnection conn = 
				getConnection("event/events/"+type.getName()+"/"+(from.getYear()+1900)+"-"+(from.getMonth()+1)+ "-" + from.getDate() 
					+"/" +(to.getYear()+1900)+"-"+(to.getMonth()+1)+ "-" + to.getDate() + "/?format=xml");
			
			conn.setRequestMethod("GET");
			
			response = makeRequest(conn);
		}
		catch (IOException e) {
			throw new AbakusNoException();
		}
		
		List<Element> djangoEvents = null;
		try {
			SAXBuilder saxBuilder = new SAXBuilder();
			Document jdomDocument = saxBuilder.build(new StringReader(response));
			djangoEvents = jdomDocument.getRootElement().getChildren();
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (Element djangoEvent : djangoEvents) {
			EventDto event = parseEvent(djangoEvent);
			events.add(event);
		}
		
		return events;
	}

	@Override
	public List<UserDto> getRegistrants(Long eventId, RegistrationStatus status)
			throws AbakusNoException {
		
		String regStatus = NAUT_TO_NERD_STATUS.get(status);
		
		List<UserDto> users = new ArrayList<UserDto>();
		
		String response = "";
		
		try {
			HttpURLConnection conn = 
				getConnection("event/"+ eventId + "/registrations/"+regStatus+"/?format=xml");
			
			conn.setRequestMethod("GET");
			
			response = makeRequest(conn);
		}
		catch (IOException e) {
			throw new AbakusNoException();
		}
		
		List<Element> djangoUsers = null;
		try {
			SAXBuilder saxBuilder = new SAXBuilder();
			Document jdomDocument = saxBuilder.build(new StringReader(response));
			djangoUsers = jdomDocument.getRootElement().getChildren();
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for (Element djangoUser : djangoUsers) {
			UserDto user = parseUser(djangoUser, true);
			users.add(user);
		}
		
		return users;
	}

	@Override
	public List<UserDto> getAllActiveStudents(Long eventId) throws AbakusNoException {
		String response = "";
		
		//Using map so that first enter all users, then overwrite those that have access
		HashMap<Long, UserDto> userMap = new HashMap<Long, UserDto>();
		
		
		//First get all users
		try {
			HttpURLConnection conn = 
				getConnection("event/"+ eventId + "/permitted_users/?format=xml&permitted=false");
			
			conn.setRequestMethod("GET");
			
			response = makeRequest(conn);
		}
		catch (IOException e) {
			throw new AbakusNoException();
		}
		
		List<Element> djangoUsers = null;
		try {
			SAXBuilder saxBuilder = new SAXBuilder();
			Document jdomDocument = saxBuilder.build(new StringReader(response));
			djangoUsers = jdomDocument.getRootElement().getChildren();
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for (Element djangoUser : djangoUsers) {
			UserDto user = parseUser(djangoUser, false);
			userMap.put(user.getUserBeanId(), user);
		}
		
		//Then get all users permitted to register for this event
		try {
			HttpURLConnection conn = 
				getConnection("event/"+ eventId + "/permitted_users/?format=xml&permitted=true");
			
			conn.setRequestMethod("GET");
			
			response = makeRequest(conn);
		}
		catch (IOException e) {
			throw new AbakusNoException();
		}
		
		try {
			SAXBuilder saxBuilder = new SAXBuilder();
			Document jdomDocument = saxBuilder.build(new StringReader(response));
			djangoUsers = jdomDocument.getRootElement().getChildren();
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for (Element djangoUser : djangoUsers) {
			UserDto user = parseUser(djangoUser, false);
			userMap.put(user.getUserBeanId(), user);
		}
		
		List<UserDto> users = new ArrayList<UserDto>();

		Collection<UserDto> mapVals = userMap.values();
		
		for (UserDto user : mapVals) {
			users.add(user);
		}
		
		return users;
	}

	@Override
	public void setRegistrantPresence(Long eventId, Boolean present,
			List<Long> registrants) throws AbakusNoException {
		
		String data = "";
		
		for (Long registrant : registrants) {
			data += registrant + ",";
		}
		if ( registrants.size() > 0){
			data = data.substring(0, data.length()-1); //Ugly as hell, to remove the last ","		
		}
		
		int presence = 0;
		data = "users=" + data;
		//TODO: What to do if presence is null, it's from users that still are in the waiting list or something
		if (present == null){
			presence = 0;
		}else{
			presence = present ? 1:2;
		}
		try {
			HttpURLConnection conn = getConnection("event/"+eventId+ "/register_users/" + presence+"/");
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("Content-Length", Integer.toString(data.getBytes().length));
			
			String response = makeRequest(conn, data);
			
		}
		catch (IOException e) {
			System.out.println(e);
			throw new AbakusNoException();
		}
	}


	@Override
	public void updateStudent(UserDto user) throws AbakusNoException {
		//Only accepts card number updates for now
		
		Long rfid_card_number = user.getRfidCardNumber();
		String username = user.getUsername();
		
		if (username == null || rfid_card_number == null){
			return;
		}
		
		String data = "rfid_card_number=" + rfid_card_number;
		
		try {
			HttpURLConnection conn = getConnection("user/"+username+ "/abacard_update/");
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			
			conn.setRequestProperty("Content-Length", Integer.toString(data.getBytes().length));
			
			String response = makeRequest(conn, data);
		}
		catch (IOException e) {
			throw new AbakusNoException();
		}
	}

	@Override
	public List<Type> getAllTypes() throws AbakusNoException {
		try {
			HttpURLConnection conn = getConnection("event/types/");
			
			conn.setRequestMethod("GET");
			
			String response = makeRequest(conn);
			
			List<Element> types = null;
			try {
				SAXBuilder saxBuilder = new SAXBuilder();
				Document jdomDocument = saxBuilder.build(new StringReader(response));
				
				types = jdomDocument.getRootElement().getChildren();
			
			} catch (JDOMException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
			ArrayList<Type> t = new ArrayList<Type>();
			
			for (Element type : types){
				Type temp = new Type(Long.parseLong(type.getChild("id").getValue()), type.getChild("name").getValue(), type.getChild("name").getValue()); 
				t.add(temp);
				TYPE_MAP.put(temp.getId(), temp);
			}
			return t;
		}
		catch (IOException e) {
			throw new AbakusNoException();
		}
		
	}
	
	private HttpURLConnection getConnection(String path) throws AbakusNoException {
		try {
			URL url = new URL(baseUrl + path);
			return (HttpURLConnection) url.openConnection();
		}
		catch (Exception e) {
			throw new AbakusNoException("Error connecting to abakus.no.");
		}
	}
	
	private String makeRequest(HttpURLConnection conn) throws IOException {
		return makeRequest(conn, null);
	}
	
	private String makeRequest(HttpURLConnection conn, String postData) throws IOException {
		conn.setUseCaches(false);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestProperty("Cookie", cookie);
		
		
		if (postData != null) {
			//Send request
			DataOutputStream wr = new DataOutputStream(
					conn.getOutputStream());
			wr.writeBytes(postData);
			wr.flush();
			wr.close();
		}
		else
			conn.connect();
		
		// Get Response
		InputStream is = conn.getInputStream();
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		String line;
		StringBuffer response = new StringBuffer();
		while ((line = rd.readLine()) != null) {
			response.append(line);
			response.append('\r');
		}
		rd.close();
		
		return response.toString();
	}

}
