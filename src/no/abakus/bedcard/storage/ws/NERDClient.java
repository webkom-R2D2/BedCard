package no.abakus.bedcard.storage.ws;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import no.abakus.naut.entity.event.enums.RegistrationStatus;
import no.abakus.naut.entity.news.Type;
import no.abakus.naut.ws.bedcard.EventDto;
import no.abakus.naut.ws.bedcard.UserDto;

public class NERDClient implements AbakusNoBedCardService {

	// public final static String baseUrl = "http://nerddev.abakus.no/";
	public final static String baseUrl = "http://localhost:8000/";
	
	private String cookie = ""; 
	
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
			
			return response.indexOf("OK") > 0;
		}
		catch (IOException e) {
			throw new AbakusNoException();
		}
	}

	@Override
	public EventDto getEvent(Long id) throws AbakusNoException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EventDto> findEvents(Type type, Date from, Date to)
			throws AbakusNoException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserDto> getRegistrants(Long eventId, RegistrationStatus status)
			throws AbakusNoException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setRegistrantPresence(Long eventId, Boolean present,
			List<Long> registrants) throws AbakusNoException {
		// TODO Auto-generated method stub

	}

	@Override
	public List<UserDto> getAllActiveStudents(Long eventId)
			throws AbakusNoException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateStudent(UserDto user) throws AbakusNoException {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Type> getAllTypes() throws AbakusNoException {
		try {
			HttpURLConnection conn = getConnection("event/types/");
			
			conn.setRequestMethod("GET");
			
			String response = makeRequest(conn);
			
			response = response.substring(1, response.length()-2);
			String[] types = response.split(", ");
			ArrayList<Type> t = new ArrayList<Type>();
			
			for (String type : types)
				t.add(new Type(0L, "Webkom eier Abakus", type.replaceAll("\"", "")));
			
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
