package no.abakus.bedcard.gui.panels.content.registrering;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import no.abakus.bedcard.gui.BedCard;
import no.abakus.bedcard.gui.panels.content.ContentPanel;
import no.abakus.bedcard.gui.panels.content.ContentPanelMother;
import no.abakus.bedcard.gui.panels.content.LogoPanel;
import no.abakus.bedcard.logic.Logic;

public class Registrering extends ContentPanelMother {
	public static int unknownUsernamePopupLength = 10000;
	private JPanel panelTextBrukernavn;
	private JTextField brukernavn;
	private JPanel panelTextEnter;
	private HighlightBox boxPanel;
	private boolean highlighted;
	private JLabel textBrukernavn;
	private LogoPanel eventTitle;
	private JPanel toppen;
	private Timer unknownUsernameCancelTimer;
	
	public Registrering(BedCard bedcard, Logic logic, ContentPanel contentPanel) {
		super(bedcard, logic, contentPanel);
		width = bedcard.getDrawWidth();
		height = bedcard.getDrawHeight()-BedCard.BottomStatusPanelHeight-BedCard.MenuPanelHeight;
		setPanelSizes();
		
		
		toppen = new JPanel();
		int heigthLogo = 160;
		int heigthUsername = 150;
		int heightTrekkKort = 60;
		
		setLayout(new FlowLayout(FlowLayout.CENTER, 0,(height-heigthUsername-heigthLogo-heightTrekkKort)/4));
		toppen.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
		toppen.setBackground(Color.white);		
		toppen.setBorder(new EmptyBorder(0,0,0,0));
		toppen.setPreferredSize(new Dimension(width, heigthLogo));
		add(toppen);
		
		JPanel velkommenTrekkKort = new JPanel();
		velkommenTrekkKort.setBackground(Color.white);
		velkommenTrekkKort.setPreferredSize(new Dimension(width, heightTrekkKort));
		JLabel textVelkommenTrekkKort = new JLabel("Bruk studentkort eller skriv inn brukernavn");
		textVelkommenTrekkKort.setFont(new Font("Tahoma", Font.PLAIN, 40));
		velkommenTrekkKort.add(textVelkommenTrekkKort);
		add(velkommenTrekkKort);
		
		boxPanel = new HighlightBox(bedcard);
		add(boxPanel);
		
		panelTextBrukernavn = new JPanel();
		textBrukernavn = new JLabel("Tast brukernavn");
		panelTextBrukernavn.add(textBrukernavn);
		brukernavn = new JTextField(10);
		panelTextEnter = new JPanel();
		JLabel textEnter = new JLabel("Trykk enter");
		panelTextEnter.add(textEnter);
		panelTextBrukernavn.setPreferredSize(new Dimension(boxPanel.getWidth()-20, 30));
		panelTextEnter.setPreferredSize(new Dimension(boxPanel.getWidth()-20, 20));
		textBrukernavn.setFont(new Font("Tahoma", Font.BOLD, 15));
		textEnter.setFont(new Font("Tahoma", Font.BOLD, 10));
		brukernavn.setFont(new Font("Tahoma", Font.BOLD, 13));
		brukernavn.addKeyListener(new EnterKeyListener());			
		boxPanel.getInternPanel().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		boxPanel.getInternPanel().add(panelTextBrukernavn);
		boxPanel.getInternPanel().add(brukernavn);
		boxPanel.getInternPanel().add(panelTextEnter);
		panelTextBrukernavn.setBackground(boxPanel.getInternPanel().getBackground());
		panelTextEnter.setBackground(boxPanel.getInternPanel().getBackground());
		
		eventTitle = new LogoPanel(bedcard, logic);
		
		toppen.add(eventTitle);
		
		refreshData();
		setHighlightedUsername(false);
	}
	
	public void setHighlightedUsername(boolean highlighted){
		this.highlighted = highlighted;
		boxPanel.setHighlighted(highlighted);
		if(highlighted){
			textBrukernavn.setText("Tast brukernavn");
			textBrukernavn.setFont(new Font("Tahoma", Font.BOLD, 23)); 
			panelTextBrukernavn.setPreferredSize(new Dimension(boxPanel.getWidth()-20, 40));
		} else {
			textBrukernavn.setFont(new Font("Tahoma", Font.BOLD, 15));
			panelTextBrukernavn.setPreferredSize(new Dimension(boxPanel.getWidth()-20, 30));
		}
	}
	public void entered(){
		log.debug("Registrering - entered");
		brukernavn.grabFocus();	
		refreshData();
	}
	public void refreshData(){
		textBrukernavn.setText("Tast brukernavn");
		eventTitle.updateData();
	}
	@Override
	public void fading(boolean state) {
		brukernavn.setEnabled(!state);		
	}
	
	private final class EnterKeyListener implements KeyListener{

		@Override
		public void keyPressed(KeyEvent e) {
			if(KeyEvent.VK_ENTER == e.getKeyCode()){
				if(brukernavn.getText().length()>0){
					boolean ok = false;
					if(highlighted){
						ok = bedcard.returnUnknownUsernameToCard(brukernavn.getText().toLowerCase());
					} else {
						ok = logic.checkInStudent(brukernavn.getText().toLowerCase());
					}
					
					if(!ok){
						textBrukernavn.setText("Ukjent brukernavn, tast brukernavn");
						if(unknownUsernameCancelTimer != null){
							unknownUsernameCancelTimer.cancel();
						}
						unknownUsernameCancelTimer = new Timer();
						Calendar cal = Calendar.getInstance();
						cal.setTimeInMillis(cal.getTimeInMillis()+unknownUsernamePopupLength);
						unknownUsernameCancelTimer.schedule(new UnknownUsernameCancel(), cal.getTime());
						
					} else {
						textBrukernavn.setText("Tast brukernavn");
					}
					brukernavn.setText("");
				}
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			
		}

		@Override
		public void keyTyped(KeyEvent e) {
			
		}
		
	}
	public JTextField getBrukernavn() {
		return brukernavn;
	}
	private class UnknownUsernameCancel extends TimerTask {
		public void run() {
			refreshData();
		}

	}
	
}
