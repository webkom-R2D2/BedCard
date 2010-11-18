package no.abakus.bedcard.gui.panels.bottomstatus;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;

import no.abakus.bedcard.gui.BedCard;
import no.abakus.bedcard.gui.panels.BedCardPanel;
import no.abakus.bedcard.logic.Logic;

public class BottomStatusPanel extends BedCardPanel {
	private int leftWidth;
	private int rightWidth;
	private Image leftImg;
	private Image rightImg;
	private Logic logic;
	private Timer clockRefresh;
	private Orb orb;
	public BottomStatusPanel(BedCard bedcard, Logic logic) {
		super(bedcard);
		this.logic = logic;
		width = bedcard.getDrawWidth();
		height = BedCard.BottomStatusPanelHeight;
		setPanelSizes();
		leftImg = bedcard.getImageLoader().getImage("bottomLeft.png");
		leftWidth = leftImg.getWidth(this);
		rightImg = bedcard.getImageLoader().getImage("bottomRight.png");
		rightWidth = rightImg.getWidth(this);
		startClockRefresh();
		//Load orb to use on the right
		Image[] images = new Image[9];
		for(int i = 0; i<images.length; i++){
			images[i] = bedcard.getImageLoader().getImage("orb/" + (i+1) + ".png");
		}
		Image noData = bedcard.getImageLoader().getImage("orb/noData.png");
		orb = new Orb(noData, images);
	}
	
	public void paint(Graphics g){
		Graphics2D g2d = (Graphics2D)g;
   	 	g2d.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
   	 	g2d.setColor(Color.black);
   	 	g2d.fillRect(0,0,width, height);
   	 	wrapDraw(bedcard.getImageLoader().getImage("bottomBackground.png"), g2d, this, 0, 0);
   	 	drawLeftInfo(g2d);
		drawRightInfo(g2d);
		drawClock(g2d);
    }

	private void drawClock(Graphics2D g){
		g.setFont(new Font("Tahoma", Font.PLAIN, 20));
		g.setColor(Color.white);
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String time = sdf.format(Calendar.getInstance().getTime());
		g.drawString(time, bedcard.getWidth()/2-25, 75);
	}
	private void drawLeftInfo(Graphics2D g){
		g.drawImage(leftImg, 0, 0, this);
		g.setColor(Color.black);
		g.setFont(new Font("Tahoma", Font.BOLD, 14));
		int pameldte = 0;
		int ventende = 0;
		if(logic.getListOfParticipants() != null)
			pameldte = logic.getListOfParticipants().size();
		ventende = logic.getNumberOfWaiting();
		int ikkeTillateVentende = logic.getNumberOfNonAllowedWaiting();
		if(ikkeTillateVentende > 0){
			g.drawString("Påmeldte: " + pameldte, 95, 80);
			g.drawString("Ventende: " + ventende + " (" + ikkeTillateVentende + ")", 95, 97);
		} else {
			g.drawString("Påmeldte: " + pameldte, 111, 80);
			g.drawString("Ventende: " + ventende, 111, 97);
		}
	}
	private void drawRightInfo(Graphics2D g){
		g.drawImage(rightImg, bedcard.getWidth()-rightWidth, 0, this);
		g.setColor(Color.black);
		g.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		int first = 0;
		int second = 0;
		
		if(logic.getListOfParticipants() != null)
			first = logic.getNumberOfAdmitted();
		if(logic.getEvent() != null)
			second = logic.getEvent().getCapacity();
		
		g.drawString(first + "/" + second, bedcard.getWidth()-rightWidth+42, 87);
		orb.setValues(first, second);
		orb.draw(g, bedcard.getWidth()-rightWidth+105,22);
	}
	private void startClockRefresh(){
		clockRefresh = new Timer();
		Calendar now = Calendar.getInstance();
		now.set(Calendar.SECOND, 0);
		now.set(Calendar.MILLISECOND, 0);
		now.set(Calendar.MINUTE, now.get(Calendar.MINUTE)+1);
		int millisecondPerMinute = 60*1000;
		clockRefresh.schedule(new ClockRefresh(this), now.getTime(), millisecondPerMinute);
	}
	
}
