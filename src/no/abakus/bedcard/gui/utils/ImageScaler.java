package no.abakus.bedcard.gui.utils;

import java.awt.Image;
import java.awt.MediaTracker;

import javax.swing.JPanel;

import org.apache.log4j.Logger;

public class ImageScaler {
	private static Logger log = Logger.getLogger(ImageScaler.class);
	public static Image scaleImageToFit(Image img, int maxWidth,  int maxHeight){
		int thumbWidth = 0;
	    int thumbHeight = 0;
	    
	    //Om bildet er mindre eller likt maks størrelse er det bare å returnere.
		if(img.getWidth(null)<= maxWidth && img.getHeight(null)<= maxHeight){
			thumbWidth = img.getWidth(null);
			thumbHeight = img.getHeight(null);
		} else {
			double imgRatio = (double)img.getWidth(null)/(double)img.getHeight(null);
		    double thumbMaxRatio = (double)maxWidth/(double)maxHeight;
		    
		    //height*ratio = width
		    if(imgRatio < thumbMaxRatio){
		    	//Vi må se på høyden, maxHeight er maks.
		    	thumbHeight = maxHeight;
		    	thumbWidth = (int)(maxHeight*imgRatio);
		    } else {
		    	//Vi må se på bredden, maxWidth er maks
		    	thumbWidth = maxWidth;
		    	thumbHeight = (int)(maxWidth/imgRatio);
		    }
		}   
	    log.debug("Scaling image (w: " + img.getWidth(null) + ", h: " + img.getHeight(null) + ") til image (w: " + thumbWidth + ", h: " + thumbHeight + ")");
	    MediaTracker track = new MediaTracker(new JPanel());
	    Image thumbImg = img.getScaledInstance(thumbWidth, thumbHeight, Image.SCALE_AREA_AVERAGING);
	    track.addImage(thumbImg, 1);
    	
    	try {
			if (track == null)
				log.debug("tracker er null");
			track.waitForAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	    return thumbImg;
	}
}
