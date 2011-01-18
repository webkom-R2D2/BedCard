package no.abakus.bedcard.gui.panels.content.registrering;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import no.abakus.bedcard.gui.BedCard;
import no.abakus.bedcard.gui.panels.content.ContentPanel;
import no.abakus.bedcard.gui.panels.content.ContentPanelMother;
import no.abakus.bedcard.gui.panels.content.LogoPanel;
import no.abakus.bedcard.logic.Logic;
import no.abakus.bedcard.storage.domainmodel.StudentEntry;

public class RegistreringVenteliste extends ContentPanelMother {
	private JPanel panelTextBrukernavn;
	private HighlightBox boxPanel;
	private JPanel toppen;
	private StudentEntry student;
	private JButton letIn;
	private JButton dontLetIn;
	JLabel textVelkommenBruker;
	JLabel textVelkommenBrukernavn;
	private LogoPanel eventTitle;
	private boolean fading;
	private JTextField brukernavn;
	private JPanel panelTextEnter;
	private JLabel textBrukernavn;
	private Timer unknownUsernameCancelTimer;
	private boolean highlighted;
	public RegistreringVenteliste(BedCard bedcard, Logic logic, ContentPanel contentPanel) {
		super(bedcard, logic, contentPanel);
		width = bedcard.getDrawWidth();
		height = bedcard.getDrawHeight()-BedCard.BottomStatusPanelHeight-BedCard.MenuPanelHeight;
		setPanelSizes();
		
		
		toppen = new JPanel();
		int heigthLogo = 160;
		int heigthUsername = 60;
		int heightTrekkKort = 30;
		int heightName = 95;
		int heigthBoxPanel = 150;
		int heightLetDontLetIn = 25;
		
		setLayout(new FlowLayout(FlowLayout.CENTER, 0,(height-heigthLogo-heigthUsername-heightTrekkKort-heightName-heigthBoxPanel-heightLetDontLetIn)/6));
		toppen.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
		toppen.setBackground(Color.white);
		toppen.setPreferredSize(new Dimension(width, heigthLogo));
		add(toppen);
		JPanel brukerInfo = new JPanel();
		brukerInfo.setBackground(Color.white);
		brukerInfo.setLayout(new FlowLayout(FlowLayout.CENTER, 0,0));
		brukerInfo.setPreferredSize(new Dimension(width, heightName+heigthUsername));
		add(brukerInfo);
		
		JPanel velkommenBruker = new JPanel();
		velkommenBruker.setBackground(Color.white);
		velkommenBruker.setPreferredSize(new Dimension(width, heightName));
		textVelkommenBruker = new JLabel("");
		textVelkommenBruker.setFont(new Font("Tahoma", Font.PLAIN, 70));
		velkommenBruker.add(textVelkommenBruker);
		brukerInfo.add(velkommenBruker);
		
		JPanel velkommenBrukernavn = new JPanel();
		velkommenBrukernavn.setBackground(Color.white);
		velkommenBrukernavn.setPreferredSize(new Dimension(width, heigthUsername));
		textVelkommenBrukernavn = new JLabel("");
		textVelkommenBrukernavn.setFont(new Font("Tahoma", Font.PLAIN, 40));
		velkommenBrukernavn.add(textVelkommenBrukernavn);
		brukerInfo.add(velkommenBrukernavn);
		
		JPanel velkommenTrekkKort = new JPanel();
		velkommenTrekkKort.setBackground(Color.white);
		velkommenTrekkKort.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		velkommenTrekkKort.setPreferredSize(new Dimension(width, heightTrekkKort));
		JLabel textVelkommenTrekkKort = new JLabel("Bruk studentkort");
		textVelkommenTrekkKort.setFont(new Font("Tahoma", Font.PLAIN, 30));
		velkommenTrekkKort.add(textVelkommenTrekkKort);
		add(velkommenTrekkKort);

		//Let in or don't let in
		JPanel letdontletIn = new JPanel();
		letdontletIn.setBackground(Color.white);
		letdontletIn.setPreferredSize(new Dimension(width, heightLetDontLetIn));
		letdontletIn.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
		add(letdontletIn);
		Font knappFont = new Font("Tahoma", Font.PLAIN, 14);
		
		letIn = new JButton("Sjekk inn");
		letIn.addActionListener(new RegistreringVentelisteActionListener());
		letIn.setBackground(Color.white);
		letIn.setFont(knappFont);
		letdontletIn.add(letIn);
		dontLetIn = new JButton("Ikke sjekk inn");
		dontLetIn.addActionListener(new RegistreringVentelisteActionListener());
		dontLetIn.setBackground(Color.white);
		dontLetIn.setFont(knappFont);
		letdontletIn.add(dontLetIn);

		
		//Boxpanel fra registrering 
		boxPanel = new HighlightBox(bedcard);
		boxPanel.getInternPanel().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
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
		panelTextBrukernavn.setBackground(boxPanel.getInternPanel().getBackground());
		panelTextEnter.setBackground(boxPanel.getInternPanel().getBackground());
		
		boxPanel.getInternPanel().add(panelTextBrukernavn);
		boxPanel.getInternPanel().add(brukernavn);
		boxPanel.getInternPanel().add(panelTextEnter);

		setHighlightedUsername(false);
		eventTitle = new LogoPanel(bedcard, logic);
		toppen.add(eventTitle);
		
		refreshData();
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
		log.debug("Registrer fra venteliste: Entered");
		brukernavn.grabFocus();
		refreshData();
	}
	@Override
	public void fading(boolean state) {
		log.debug("RegistreringVenteliste - fading: " + state);
		fading = state;
		brukernavn.setEnabled(!state);
		if(state){
			letIn.setEnabled(false);
			dontLetIn.setEnabled(false);
		} else {
			if(student != null){
				letIn.setEnabled(true);
				dontLetIn.setEnabled(true);
			}
		}
		
	}
	
	public void refreshData(){
		updateUserText();
		textBrukernavn.setText("Tast brukernavn");
		eventTitle.updateData();		
	}
	public void updateUserText(){
		student = logic.getNextInWaitingListLine();
		if(student != null){
			if(!logic.getAllowEverybody() && !student.getStudent().isHasAccess()){
				//Ingen flere tillatte studenter.
				textVelkommenBruker.setText("Ingen flere tillatte studenter");
				textVelkommenBrukernavn.setText("");
				letIn.setEnabled(false);
				dontLetIn.setEnabled(false);
			} else if(logic.getEvent().getCapacity()-logic.getNumberOfAdmitted()<=0 && !logic.getFreeFlow()){
				//Det er ikke fler plasser
				textVelkommenBruker.setText("Ingen flere plasser");
				textVelkommenBrukernavn.setText("");
				letIn.setEnabled(false);
				dontLetIn.setEnabled(false);
			} else {
				textVelkommenBruker.setText(student.getStudent().getFirstname() + " " + student.getStudent().getLastname());
				textVelkommenBrukernavn.setText(student.getStudent().getUsername());
				if(!fading){
					letIn.setEnabled(true);
					dontLetIn.setEnabled(true);
				}
			}
		} else {
			textVelkommenBruker.setText("Ventelisten er nå tom");
			textVelkommenBrukernavn.setText("");
			letIn.setEnabled(false);
			dontLetIn.setEnabled(false);
		}
	}
	private final class RegistreringVentelisteActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			if(arg0.getSource() == letIn){
				logic.checkInStudent(student.getStudent().getUsername());
				refreshData();				
			} else if(arg0.getSource() == dontLetIn){
				student.setEntered(false);
				logic.saveRegistrationStatus(student);
				refreshData();
			}
			
		}
    	
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
						cal.setTimeInMillis(cal.getTimeInMillis()+Registrering.unknownUsernamePopupLength);
						unknownUsernameCancelTimer.schedule(new UnknownUsernameCancel(), cal.getTime());
						
					} else {
						textBrukernavn.setText("Tast brukernavn");
					}
					brukernavn.setText("");
				}
			}
		}
		private class UnknownUsernameCancel extends TimerTask {
			public void run() {
				refreshData();
			}

		}

		@Override
		public void keyReleased(KeyEvent e) {
			
		}

		@Override
		public void keyTyped(KeyEvent e) {
			
		}
		
	}
}
