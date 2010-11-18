package no.abakus.bedcard.gui;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.util.Calendar;
import java.util.Timer;

import no.abakus.bedcard.gui.panels.BedCardPanel;
import no.abakus.bedcard.gui.panels.FadeUpdater;
import no.abakus.bedcard.gui.panels.content.PopupType;
import no.abakus.bedcard.gui.panels.content.konfigurering.threads.LasteThread;
import no.abakus.bedcard.logic.cardreader.CardReader;
import no.abakus.bedcard.storage.domainmodel.Event;
import no.abakus.bedcard.storage.domainmodel.Student;

/**
 * @author Sigve Albretsen
 *
 */
public class BedCardGUI extends BedCardPanel  
{
	private int popupLength = 2000;
	private Timer popupTimer;
	protected int fadeStepTime = 60; 
	protected Timer timer;
	protected int nofFadeStates = 10;
	protected float fullFade = 0.90F;
	protected double fadeState;
	protected boolean fadeStatus;
	protected Image popup;
	protected PopupType popupType;
	private Student popupStudent;
	private boolean popupValuesChanged = false;
	private int popupFontSize = 70;
	private int generalPopupLargeFontSize = 70;
	private int generalPopupSmallFontSize = 20;
	private PopupMessage msg;
	
	public BedCardGUI(BedCard bedcard) {
		super(bedcard);
		popup = bedcard.getImageLoader().getImage("popup.png");
        setLayout(new BorderLayout());
        width = bedcard.getDrawWidth();
        height = bedcard.getDrawHeight();
        setPanelSizes();
	}
	
	public void fadeIn(){
		fadeStatus = false;
	}
	
	public void fadeOut(){
		bedcard.fadeDisable();
				
		fadeStatus = true;
		if(timer != null){
			timer.cancel();
			timer = null;
		}
		timer = new Timer();
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(cal.getTimeInMillis()+BedCard.fadeDelay);
		timer.schedule(new FadeUpdater(this), cal.getTime(), fadeStepTime);
		
		if(popupTimer != null){
			popupTimer.cancel();
		}
		popupTimer = new Timer();
		cal.setTimeInMillis(cal.getTimeInMillis()+popupLength);
		popupTimer.schedule(new PopupCancel(this), cal.getTime());
	}
	public void fadeOutWithHalt(){
		bedcard.fadeDisable();
		fadeStatus = true;
		if(timer != null){
			timer.cancel();
			timer = null;
		}
		timer = new Timer();
		timer.schedule(new FadeUpdater(this), Calendar.getInstance().getTime(), fadeStepTime);
		if(popupTimer != null){
			popupTimer.cancel();
		}
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if(fadeState > 0){
			Graphics2D g2d = (Graphics2D)g;
			g2d.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
			float optique1 = (float)fadeState/(float)nofFadeStates*fullFade;
			g2d.setColor(Color.black);
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, optique1));
			g2d.fillRect(0, 0, width, height);

   	 	
	   	 	if(popupType == PopupType.velkommen){
	   	 		drawWelcome(g2d);
	   	 	} else if(popupType == PopupType.twoLine){
	   	 		drawPopup(g2d);
	   	 	}
   	 	}
	}

	public void updatePosition(){
		if(fadeStatus){
			if(fadeState<nofFadeStates){
				fadeState+=3;
			}
		} else {
			if(fadeState>0){
				fadeState--;
			} else {
				timer.cancel();
				bedcard.fadeEnable();
			}
		}
		if(fadeState>nofFadeStates){
			fadeState = nofFadeStates;
		} else if(fadeState<0){
			fadeState = 0;
		}
		repaint();
	}

	public void drawWelcome(Graphics2D g2d){
   	 	
   	 	int xPos = (width-popup.getWidth(this))/2;
   	 	int yPos = (height-popup.getHeight(this))/2;
   	 	if(fadeState==nofFadeStates)
   	 		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0F));
   	 	g2d.drawImage(popup, xPos, yPos, this);
   	 	g2d.setColor(Color.black);

   	 	//For � finne riktig font for navnet (st�rrelsen, m� komme p� innsiden av boksen)
   	 	checkFontSize(g2d);
   	 	
   	 	Font font = new Font("Tahoma", Font.PLAIN, popupFontSize);
   	 	g2d.setFont(font);
   	 	
   	 	String navn = popupStudent.getFirstname() + " " + popupStudent.getLastname();
   	 	FontMetrics fm = g2d.getFontMetrics();
   	 	int navnXpos = (popup.getWidth(this) - g2d.getFontMetrics().stringWidth(navn))/2+xPos;
   	 	int navnYpos = height/2-fm.getDescent()+(fm.getAscent()+fm.getDescent())/2;
   	 	g2d.drawString(navn, navnXpos, navnYpos);
	}
	
	public void drawPopup(Graphics2D g2d){
		String top = msg.getTitle();
		String bottom = msg.getDescription();
		int xPos = (width-popup.getWidth(this))/2;
   	 	int yPos = (height-popup.getHeight(this))/2;
   	 	if(fadeState==nofFadeStates)
   	 		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0F));
   	 	g2d.drawImage(popup, xPos, yPos, this);
   	 	g2d.setColor(Color.black);
   	 	
   	 	//For � finne riktig font for tittel og beskrivelse (st�rrelsen, m� komme p� innsiden av boksen)
   	 	checkFontSizeTwoLines(g2d);
   	 	
   	 	Font font = new Font("Tahoma", Font.PLAIN, generalPopupLargeFontSize);
   	 	g2d.setFont(font);
   	   	
   	 	FontMetrics fm = g2d.getFontMetrics();
   	 	int padding = 20;
   	 	int totHeight = generalPopupSmallFontSize + padding + fm.getAscent()+fm.getDescent(); //Total h�yde
   	 	
   	 	int navnXpos = (popup.getWidth(this) - fm.stringWidth(top))/2+xPos;
   	 	int navnYpos = height/2-totHeight/2+fm.getAscent();

   	 	g2d.drawString(top, navnXpos, navnYpos);
   	 	
   	 	if (bottom == null)
   	 		bottom = "-";
   	 	
   	 	font = new Font("Tahoma", Font.PLAIN, generalPopupSmallFontSize);
   	 	g2d.setFont(font);
   	 	int tekstYpos = height/2+totHeight/2-fm.getDescent();
   	 	int tekstXpos = (popup.getWidth(this) - g2d.getFontMetrics().stringWidth(bottom))/2+xPos;
   	 	
   	 	g2d.drawString(bottom, tekstXpos, tekstYpos);
	}
	
	public void addedToWaitingList(Student student) {
		popupValuesChanged = true;
		msg = new PopupMessage(student.getFirstname() + " " + student.getLastname(), "Ikke påmeldt, lagt til i venteliste");
		popupType = PopupType.twoLine;
		fadeOut();		
	}

	public void admitted(Student student) {
		popupValuesChanged = true;
		popupStudent = student;
		popupType = PopupType.velkommen;
		fadeOut();
	}
	
	public void popupMessage(PopupMessage msg) {
		this.msg = msg;
		popupValuesChanged = true;
		popupType = PopupType.twoLine;
		fadeOut();
	}
	public void haltMessage(PopupMessage msg) {
		this.msg = msg;
		popupValuesChanged = true;
		popupType = PopupType.twoLine;
		fadeOutWithHalt();
	}

	public void notifyReaderStatusChange(CardReader reader) {
		popupValuesChanged = true;
		String tekst = "Status er endret";
	 	if(reader.isConnected()){
	 		tekst = "Leseren er klar";
	 	} else {
	 		tekst = "Leseren er IKKE klar";
	 	}
		this.msg = new PopupMessage(reader.getName(), tekst);
		popupType = PopupType.twoLine;
		fadeOut();
	}
	
	private void checkFontSize(Graphics2D g2d){
 		if(popupValuesChanged){
			popupValuesChanged = false;
			popupFontSize = 70;
	   	 	Font font = new Font("Tahoma", Font.PLAIN, popupFontSize);
	   	 	g2d.setFont(font);
	   	 	if(popupStudent != null){
		   	 	String navn = popupStudent.getFirstname() + " " + popupStudent.getLastname();
		 		while(g2d.getFontMetrics().stringWidth(navn)>popup.getWidth(this)-30){
		 			popupFontSize--;
		 			font = new Font("Tahoma", Font.PLAIN, popupFontSize);
		 			g2d.setFont(font);
		 			if(popupFontSize<=1)
		 				break;
		 		}
	   	 	}
 		}
	}
	private void checkFontSizeTwoLines(Graphics2D g2d){
 		if(popupValuesChanged){
			popupValuesChanged = false;
			generalPopupLargeFontSize = 70;
			
	   	 	Font font = new Font("Tahoma", Font.PLAIN, generalPopupLargeFontSize);
	   	 	g2d.setFont(font);
	   	 	if(msg != null){
		 		while(g2d.getFontMetrics().stringWidth(msg.getTitle())>popup.getWidth(this)-30){
		 			generalPopupLargeFontSize--;
		 			font = new Font("Tahoma", Font.PLAIN, generalPopupLargeFontSize);
		 			g2d.setFont(font);
		 			if(generalPopupLargeFontSize<=1)
		 				break;
		 		}
	   	 	}
	   	 	
	   	 	generalPopupSmallFontSize = 20;
	   	 	font = new Font("Tahoma", Font.PLAIN, generalPopupSmallFontSize);
	   	 	g2d.setFont(font);
	   	 	if(msg != null && msg.getDescription() != null){
		 		while(g2d.getFontMetrics().stringWidth(msg.getDescription())>popup.getWidth(this)-30){
		 			generalPopupSmallFontSize--;
		 			font = new Font("Tahoma", Font.PLAIN, generalPopupSmallFontSize);
		 			g2d.setFont(font);
		 			if(generalPopupSmallFontSize<=1)
		 				break;
		 		}
	   	 	}
 		}
	}
}

