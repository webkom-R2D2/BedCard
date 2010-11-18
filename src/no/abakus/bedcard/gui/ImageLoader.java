package no.abakus.bedcard.gui;

import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.JPanel;

import org.apache.log4j.Logger;

public class ImageLoader {
	protected static Logger log = Logger.getLogger(ImageLoader.class);
	private Hashtable<String, Image> bilder;
	private MediaTracker track;

	
	
	public ImageLoader() {
		bilder = new Hashtable<String, Image>();
		try {
			nyeBilder("/images/images.txt");
		} catch (FileNotFoundException e) {
			log.error("Kunne ikke laste bilder");
		} catch (IOException e) {
			log.error("Kunne ikke laste bilder");
		}
	}
	private Image getPicture(String filNavn) {
		String path = "/images/"+filNavn;
		URL url = getClass().getResource(path);
		if(url == null){
			log.error("Fant ikke path: " + path);
			return null;
		}
        return Toolkit.getDefaultToolkit().getImage(url);
	}
	/**
	 * Method for loading pics into hashtable. Use the class array
	 */
	public void hashPictures() {

	}

	public Image getImage(String key) {
		Image load =  bilder.get(key);
		if(load == null){
			log.error("Fant ikke bildet: " + key);
		}
		return load;
	}

	public void nyeBilder(String filNavn) throws IOException, FileNotFoundException {
		track = new MediaTracker(new JPanel());
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
			Image img = this.getPicture((String) fil);
			if (img == null){
				log.error("Bildet er null: " + fil);
			} else {
				track.addImage(img, 0);
				bilder.put(fil, img);
			}
		}
		try {
			if (track == null)
				log.debug("tracker er null");
			track.waitForAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
