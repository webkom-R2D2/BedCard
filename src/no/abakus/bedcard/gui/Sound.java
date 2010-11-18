package no.abakus.bedcard.gui;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

public class Sound {
	protected static Logger log = Logger.getLogger(Sound.class);
	private Hashtable<String, AudioClip> lyder;
	
	public Sound() {
		lyder = new Hashtable<String, AudioClip>();
		try {
			nyeLyder("/sounds/sounds.txt");
		} catch (FileNotFoundException e) {
			log.error("Kunne ikke laste lyder");
		} catch (IOException e) {
			log.error("Kunne ikke laste lyder");
		}
	}
	private AudioClip getSound(String filNavn) {
		String path = "/sounds/"+filNavn;
		URL url = getClass().getResource(path);
		if(url == null){
			log.error("Fant ikke path: " + path);
			return null;
		}
        return Applet.newAudioClip(url);//Toolkit.getDefaultToolkit().getImage(url);
	}


	public void nyeLyder(String filNavn) throws IOException, FileNotFoundException {
		ArrayList<String> alleFilNavn = new ArrayList<String>();
	    InputStream fi = getClass().getResourceAsStream(filNavn);
	    BufferedReader buffer = new BufferedReader(new InputStreamReader(fi));
		String lestLinje;
		try {
			while ((lestLinje = buffer.readLine()) != null) {
				alleFilNavn.add(lestLinje);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		buffer.close();
		
		for (String fil : alleFilNavn) {
			AudioClip sound = this.getSound(fil);
			if (sound == null){
				log.error("Lyden er null: " + fil);
			} else {
				lyder.put(fil, sound);
			}
		}
	}
	private void playOnceDelayed(String key){
		AudioClip lyd = (AudioClip) lyder.get(key);
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(cal.getTimeInMillis()+BedCard.fadeDelay);
		Timer timer = new Timer();
		timer.schedule(new SoundFadeTask(lyd), cal.getTime());
	}
	
	public void playPopupSound(){
		//playOnce("ECHOBEL2.WAV");
	}
	public void playErrorSound(){
		playOnceDelayed("ZING.WAV");
	}
	public void playAdmittedSound(){
		playOnceDelayed("CONGRATS.WAV");
	}
	public void playAddedToWaitingList(){
		playOnceDelayed("ECHOBEL2.WAV");
	}
	public void playUnknownUsernameToCard(){
		playOnceDelayed("SHRTALRM.WAV");
	}
	private class SoundFadeTask extends TimerTask {
		private AudioClip lyd;
		
		public SoundFadeTask(AudioClip lyd) {
			super();
			this.lyd = lyd;
		}

		@Override
		public void run() {
			lyd.play();	
		}
	}
}
