package no.abakus.bedcard.gui.panels.bottomstatus;

import java.awt.Graphics2D;
import java.awt.Image;

import org.apache.log4j.Logger;

public class Orb {
	private static Logger log = Logger.getLogger(Orb.class);
	private Image[] images;
	private int first;
	private int second;
	private Image noData;

	public Orb(Image noData, Image[] images) {
		super();
		this.images = images;
		this.noData = noData;
	}
	
	public void setFirst(int first) {
		this.first = first;
	}
	public void setValues(int first, int second) {
		this.first = first;
		this.second = second;
	}
	
	public void draw(Graphics2D g, int x, int y){
		if(second <= 0){
			g.drawImage(noData, x, y, null);
		} else {
			double ratio = (double)first/(double)second;
			if(first>second)
				ratio = 1.0;
			int imgPos = (int)Math.floor((double)(images.length-1)*ratio);
			g.drawImage(images[imgPos], x, y, null);
		}
	}
	
	
}
