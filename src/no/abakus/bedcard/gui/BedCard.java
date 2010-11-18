package no.abakus.bedcard.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import no.abakus.bedcard.gui.panels.bottomstatus.BottomStatusPanel;
import no.abakus.bedcard.gui.panels.content.ContentPanel;
import no.abakus.bedcard.gui.panels.content.ContentType;
import no.abakus.bedcard.gui.panels.menu.MenuPanel;
import no.abakus.bedcard.logic.Logic;
import no.abakus.bedcard.logic.LogicHandler;
import no.abakus.bedcard.logic.LogicListener;
import no.abakus.bedcard.logic.cardreader.CardReader;
import no.abakus.bedcard.logic.cardreader.CardReaderListener;
import no.abakus.bedcard.storage.domainmodel.Student;

import org.apache.log4j.Logger;

/**
 * @author Sigve Albretsen
 *
 */
public class BedCard extends JFrame implements ActionListener, LogicListener 
{
	public static int fadeDelay = 0;
	public static int MenuPanelHeight = 119;
	public static int BottomStatusPanelHeight = 119;
	private static boolean debugging = false;

	private static Logger log = Logger.getLogger(BedCard.class);
	private int drawHeight;
	private int drawWidth;
	private MenuPanel menuPanel;
	private ContentPanel contentPanel;
	private BottomStatusPanel bottomStatusPanel;
	private Logic logic;
	private ImageLoader imageLoader;
	private BedCardGUI guiMain;
	private Sound sound;
	private GraphicsDevice device;
	private JDesktopPane desktop;
	private ContentType current;
	
	/**
	 * Vil opprette et vindu som f�lger oppl�sningen funnet som desktop-oppl�sning.
	 */
	public BedCard(){
		current = ContentType.konfigurering;
		imageLoader = new ImageLoader();
		sound = new Sound();
		logic = new LogicHandler();
		logic.setLogicListener(this);
		log.debug("Starting BedCard");
		setTitle("BedCard - Fordi webkom eier Abakus");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		desktop = new JDesktopPane();
		setContentPane(desktop);
		setDevice(getDefaultDevice());
		logic.connectReaders();
		
	}
	
	public void actionPerformed(ActionEvent arg0) {
		log.debug("Action performed: " + arg0.getActionCommand());
		if(current == ContentType.endreBruker)
			contentPanel.getEndreBruker().exit();
		if(arg0.getActionCommand().equals("registrering")){
			shiftToRegistrering();
		} else if(arg0.getActionCommand().equals("pameldte")){
			current = ContentType.pameldte;
			((CardLayout)contentPanel.getLayout()).show(contentPanel, current.toString());
			contentPanel.getPameldte().entered();
		} else if(arg0.getActionCommand().equals("venteliste")){
			current = ContentType.venteliste;
			((CardLayout)contentPanel.getLayout()).show(contentPanel, current.toString());
			contentPanel.getVenteliste().entered();
		} else if(arg0.getActionCommand().equals("ventelisteToggle")){
			//ToggleVenteliste
			boolean ventelisteStatus = logic.getWaitingListStatus();
			logic.setWaitinglistStatus(!logic.getWaitingListStatus());
			if(ventelisteStatus != logic.getWaitingListStatus()){
				menuPanel.waitingListStatusChanged();
				shiftToRegistrering();
			}
		} else if(arg0.getActionCommand().equals("endreBruker")){
			current = ContentType.endreBruker;
			((CardLayout)contentPanel.getLayout()).show(contentPanel, current.toString());
			contentPanel.getEndreBruker().entered();
		} else if(arg0.getActionCommand().equals("konfigurering")){
			current = ContentType.konfigurering;
			((CardLayout)contentPanel.getLayout()).show(contentPanel, current.toString());
			contentPanel.getKonfigurering().entered();
		} else if(arg0.getActionCommand().equals("avslutt")){
			String[] options = {"Ja", "Nei"};
			String tittel = "Er du sikker?";
			boolean safeAvslutt = logic.getEvent() == null;
			
			if(!safeAvslutt)
				safeAvslutt = JOptionPane.showInternalOptionDialog(this.getDesktop(), "Du vil miste alle endringer om du avslutter, er du sikker?", tittel, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, null) == JOptionPane.YES_OPTION;
			
			if(safeAvslutt)
				System.exit(0);
				
			menuPanel.resetMenu();
		} 
	}

	private void shiftToRegistrering(){
		if(logic.getWaitingListStatus()){
			current = ContentType.registreringVenteliste;
			((CardLayout)contentPanel.getLayout()).show(contentPanel, current.toString());
			contentPanel.getRegistreringVenteliste().entered();
		} else {
			current = ContentType.registrering;
			((CardLayout)contentPanel.getLayout()).show(contentPanel, current.toString());
			contentPanel.getRegistrering().entered();
		}	
	}
	public ImageLoader getImageLoader() {
		return imageLoader;
	}
	
	public void updateInformation(){
		if(bottomStatusPanel != null)
			bottomStatusPanel.repaint();
		if(menuPanel != null)
			menuPanel.waitingListStatusChanged();
	}
	
	@Override
	public void addedToWaitingList(Student student) {
		contentPanel.getRegistreringVenteliste().updateUserText();
		contentPanel.getVenteliste().refreshData();
		guiMain.addedToWaitingList(student);
		sound.playAddedToWaitingList();
	}

	@Override
	public void admitted(Student student) {
		contentPanel.getPameldte().refreshData();
		if(logic.getWaitingListStatus()){
			contentPanel.getRegistreringVenteliste().updateUserText();
			contentPanel.getVenteliste().refreshData();
		}
		guiMain.admitted(student);
		sound.playAdmittedSound();
	}

	@Override
	public void notifyReaderStatusChange(CardReader reader) {
		guiMain.notifyReaderStatusChange(reader);
		sound.playPopupSound();
		contentPanel.getKonfigurering().refreshData();
	}

	@Override
	public void unknownUsernameToCard() {
		if(logic.getWaitingListStatus()){
			((CardLayout)contentPanel.getLayout()).show(contentPanel, ContentType.registreringVenteliste.toString());
			current = ContentType.registreringVenteliste;
			contentPanel.getRegistreringVenteliste().entered();
			contentPanel.getRegistreringVenteliste().setHighlightedUsername(true);
		} else {
			((CardLayout)contentPanel.getLayout()).show(contentPanel, ContentType.registrering.toString());
			current = ContentType.registrering;
			contentPanel.getRegistrering().entered();
			contentPanel.getRegistrering().setHighlightedUsername(true);
		}
		sound.playUnknownUsernameToCard();
	}
	public void popupMessage(String title, String description) {
		PopupMessage msg = new PopupMessage(title, description);
		guiMain.popupMessage(msg);
		sound.playPopupSound();
	}
	
	public boolean returnUnknownUsernameToCard(String username){
		if(current==ContentType.registrering){
			contentPanel.getRegistrering().setHighlightedUsername(false);
		} else if(current==ContentType.registrering){
			contentPanel.getRegistreringVenteliste().setHighlightedUsername(false);
		} else {
			log.error("returnUnknownUsernameToCard kalt uten at vi er p� registrering eller registreringVenteliste");
			contentPanel.getRegistreringVenteliste().setHighlightedUsername(false);
			contentPanel.getRegistrering().setHighlightedUsername(false);
		}
		shiftToRegistrering();
		return logic.returnUnkownUsernameToCard(username);
	}
	public void setDevice(GraphicsDevice deviceNy){
		if(device != null)
			device.setFullScreenWindow(null);
		dispose();
		device = deviceNy;
				
		boolean isFullScreen = device.isFullScreenSupported();
		if(debugging)
			isFullScreen = false;
        setUndecorated(isFullScreen);
                
        if(isFullScreen){
        	drawWidth = device.getDisplayMode().getWidth();
        	drawHeight =  device.getDisplayMode().getHeight();
        	device.setFullScreenWindow(this);
        } else {
        	setExtendedState(this.getExtendedState()|JFrame.MAXIMIZED_BOTH);
        	setVisible(true);
        	int frameHeight = this.getInsets().bottom + this.getInsets().top;
        	int frameWidth = this.getInsets().left + this.getInsets().right;
        	log.debug("frameHeight: " + frameHeight);
        	log.debug("frameWidth: " + frameWidth);
        	drawWidth = this.getSize().width-frameWidth;
        	drawHeight = this.getSize().height-frameHeight;
        }
		log.debug("Desktop resolution found to be: width (" + drawWidth + "), height (" + drawHeight + ")");
				
		if(guiMain != null)
			desktop.remove(guiMain);
		//Legg p� alle panelene; meny, content og statusbar
		guiMain = new BedCardGUI(this);
		menuPanel = new MenuPanel(this, logic);
		contentPanel = new ContentPanel(this, logic);
		bottomStatusPanel = new BottomStatusPanel(this, logic);
		guiMain.add(menuPanel, BorderLayout.NORTH);
		guiMain.add(contentPanel, BorderLayout.CENTER);
		guiMain.add(bottomStatusPanel, BorderLayout.SOUTH);
		desktop.add(guiMain);
		
		//Klargj�r rammen for visning
        if (isFullScreen) {
            validate();
        } else {
            setVisible(true);
        }
        setResizable(false);
	}
	public GraphicsDevice[] getDevices(){
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		log.debug("antall skjermer: " + env.getScreenDevices().length);
		return env.getScreenDevices();
	}
	public GraphicsDevice getDefaultDevice(){
		return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	}
	public GraphicsDevice getCurrentDevice(){
		return device;		
	}
	public int getDrawHeight() {
		return drawHeight;
	}
	public int getDrawWidth() {
		return drawWidth;
	}	
	public boolean isNotFaded(){
		return guiMain.fadeState==0;
	}

	public JDesktopPane getDesktop(){
		return desktop;
	}
	public static void main( String[] args ){
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {}
    	new BedCard();    	
    }

	@Override
	public void errorMessage(String title, String description) {
		PopupMessage msg = new PopupMessage(title, description);
		guiMain.popupMessage(msg);
		sound.playErrorSound();		
	}
	public void haltMessage(String title, String description) {
		PopupMessage msg = new PopupMessage(title, description);
		guiMain.haltMessage(msg);
	}	
	public void stopHaltMessage(){
		guiMain.fadeIn();
		
		contentPanel.getKonfigurering().refreshAbakusTyper();
		contentPanel.getKonfigurering().refreshAbakus();
		contentPanel.getKonfigurering().refreshFilTyper();
		contentPanel.getKonfigurering().refreshFiler();		
		contentPanel.getKonfigurering().refreshData();
		updateInformation();
	}
	public void fadeDisable(){
		menuPanel.fadeDisable();
		setFading(true);
		repaint();
	}
	public void fadeEnable() {
		menuPanel.fadeEnable();
		setFading(false);
	}
	private void setFading(boolean state){
		switch(current){
		case konfigurering:
			contentPanel.getKonfigurering().fading(state);
			break;
		case endreBruker:
			contentPanel.getEndreBruker().fading(state);
			break;
		case pameldte:
			contentPanel.getPameldte().fading(state);
			break;
		case registrering:
			contentPanel.getRegistrering().fading(state);
			break;
		case registreringVenteliste:
			contentPanel.getRegistreringVenteliste().fading(state);
			break;
		case venteliste:
			contentPanel.getVenteliste().fading(state);
			break;
		}
	}

	@Override
	public void editUserCardNrUpdated(){
		contentPanel.getEndreBruker().refreshData();
	}
}


