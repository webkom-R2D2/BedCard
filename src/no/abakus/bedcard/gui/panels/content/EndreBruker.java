package no.abakus.bedcard.gui.panels.content;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import no.abakus.bedcard.gui.BedCard;
import no.abakus.bedcard.gui.utils.SpringUtilities;
import no.abakus.bedcard.logic.Logic;
import no.abakus.bedcard.storage.domainmodel.Student;

public class EndreBruker extends ContentPanelMother {
	private JTextField brukernavn;
	private Student student;
	private JLabel brukernavnChoosen;
	private JLabel info;
	private JTextField firstName;
	private JTextField lastName;
	private JTextField magnetCardNumber;
	private JTextField rfidCardNumber;
	private JButton lagreBtn;
	private JButton tilbakestillBtn;
	private JButton hentBtn;
	
	public EndreBruker(BedCard bedcard, Logic logic, ContentPanel contentPanel) {
		super(bedcard, logic, contentPanel);
		Font fontBold = new Font("Tahoma", Font.BOLD, 16);
		Font font = new Font("Tahoma", Font.PLAIN, 16);
		width = bedcard.getDrawWidth();
		height = bedcard.getDrawHeight()-BedCard.BottomStatusPanelHeight-BedCard.MenuPanelHeight;
		setPanelSizes();
		
		int boxHeight = 400;
		BoxPanel boxPanel = new BoxPanel(bedcard, "Endre bruker", 450, boxHeight);
		int topPadding = (bedcard.getDrawHeight()-BedCard.BottomStatusPanelHeight-BedCard.MenuPanelHeight-boxHeight)/2;
		setLayout(new FlowLayout(FlowLayout.CENTER, 0,topPadding));
		
		
		boxPanel.getInternPanel().setLayout(new FlowLayout(FlowLayout.CENTER, 0,30));
		
		JPanel midPanel = new JPanel();
		midPanel.setLayout(new BorderLayout(0, 20));
		midPanel.setBackground(boxPanel.getInternPanel().getBackground());
		add(boxPanel);
		boxPanel.getInternPanel().add(midPanel);
		
		//Lag undermenyen
		Font knappFont = new Font("Tahoma", Font.PLAIN, 12);
		lagreBtn = new JButton("Lagre");
		lagreBtn.addActionListener(new EndreBrukerActionListener());
		lagreBtn.setBackground(boxPanel.getBottomPanel().getBackground());
		lagreBtn.setFont(knappFont);
		tilbakestillBtn = new JButton("Tilbakestill");		
		tilbakestillBtn.addActionListener(new EndreBrukerActionListener());
		tilbakestillBtn.setBackground(boxPanel.getBottomPanel().getBackground());
		tilbakestillBtn.setFont(knappFont);
		hentBtn = new JButton("Hent");
		hentBtn.addActionListener(new EndreBrukerActionListener());
		hentBtn.setBackground(boxPanel.getBottomPanel().getBackground());
		hentBtn.setFont(knappFont);
		
		boxPanel.getBottomPanel().add(hentBtn);
		boxPanel.getBottomPanel().add(lagreBtn);
		boxPanel.getBottomPanel().add(tilbakestillBtn);
		boxPanel.getBottomPanel().setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 12));		
			
		//Legg på finn bruker (topp-feltet)
		JPanel top = new JPanel();
		info = new JLabel(" ");
		info.setBackground(boxPanel.getInternPanel().getBackground());
		info.setFont(font);
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		infoPanel.setBackground(boxPanel.getInternPanel().getBackground());
		infoPanel.add(info);
		top.setBackground(boxPanel.getInternPanel().getBackground());
		top.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
		
		JPanel filter1 = new JPanel();
		filter1.setBackground(boxPanel.getInternPanel().getBackground());
		JLabel label = new JLabel("Brukernavn: ");
		label.setFont(fontBold);
		filter1.add(label);
		brukernavn = new JTextField(10);
		//brukernavn.setPreferredSize(new Dimension(100,20));
		brukernavn.addKeyListener(new EnterKeyListener());
		brukernavn.setFont(font);
		filter1.add(brukernavn);
		top.add(filter1);
		
		JPanel toppen = new JPanel();
		toppen.setBackground(boxPanel.getInternPanel().getBackground());
		toppen.setLayout(new BorderLayout(0,0));
		toppen.add(top, BorderLayout.NORTH);
		toppen.add(infoPanel, BorderLayout.CENTER);
		
		midPanel.add(toppen, BorderLayout.NORTH);
		
		//Legg på brukerpanelet som viser studenten.
		JPanel studentPanel = new JPanel();
		SpringLayout layout = new SpringLayout();
        studentPanel.setLayout(layout);
		studentPanel.setBackground(boxPanel.getInternPanel().getBackground());
		
		brukernavnChoosen = new JLabel(" ");
		brukernavnChoosen.setFont(font);
		JLabel textBrukernavn = new JLabel("Brukernavn:");
		textBrukernavn.setFont(fontBold);
		JLabel textFirstName = new JLabel("Fornavn:");
		textFirstName.setFont(fontBold);
		JLabel textLastName = new JLabel("Etternavn:");
		textLastName.setFont(fontBold);
		JLabel textMagnetCardNumber = new JLabel("Kortnr. (magnet): ");
		textMagnetCardNumber.setFont(fontBold);
		JLabel textRfidCardNumber = new JLabel("Kortnr. (rfid): ");
		textRfidCardNumber.setFont(fontBold);
		
		firstName = new JTextField(10);
		textFirstName.setLabelFor(firstName);
		firstName.setFont(font);
		lastName = new JTextField(10);
		textLastName.setLabelFor(lastName);
		lastName.setFont(font);
		magnetCardNumber = new JTextField(10);
		textMagnetCardNumber.setLabelFor(magnetCardNumber);
		magnetCardNumber.setFont(font);
		rfidCardNumber = new JTextField(10);
		textRfidCardNumber.setLabelFor(rfidCardNumber);
		rfidCardNumber.setFont(font);
				
        studentPanel.add(textBrukernavn);
        studentPanel.add(brukernavnChoosen);
        studentPanel.add(textFirstName);
        studentPanel.add(firstName);
        studentPanel.add(textLastName);
        studentPanel.add(lastName);
        studentPanel.add(textMagnetCardNumber);
        studentPanel.add(magnetCardNumber);
        studentPanel.add(textRfidCardNumber);
        studentPanel.add(rfidCardNumber);
        SpringUtilities.makeCompactGrid(studentPanel, 5, 2, 0, 0, 15, 15);
        
        midPanel.add(studentPanel, BorderLayout.CENTER);
		
        disableInputs();
	}
	public void refreshData(){
		if(student != null){
			info.setText(" ");
			brukernavnChoosen.setText(student.getUsername());
			firstName.setText(student.getFirstname());
			lastName.setText(student.getLastname());
			if(student.getCardNumber() != null)
				magnetCardNumber.setText(student.getCardNumber()+"");
			else
				magnetCardNumber.setText("");
			if(student.getRfidCardNumber() != null)
				rfidCardNumber.setText(student.getRfidCardNumber()+"");
			else
				rfidCardNumber.setText("");
			enableInputs();
			logic.setEditingUser(student);
		} else {
			if(brukernavn.getText().length()>0){
				info.setText("Kunne ikke finne studenten");
			} else {
				info.setText("");
			}
			brukernavnChoosen.setText("");
			firstName.setText("");
			lastName.setText("");
			magnetCardNumber.setText("");
			rfidCardNumber.setText("");
			disableInputs();
			logic.setEditingUser(null);
		}
	}
	private void hentStudent(){
		Student hentet = logic.getStudent(brukernavn.getText());
		if(hentet!=null){
			student = hentet.clone();
		} else {
			student = null;
		}
		refreshData();
	}
	private final class EndreBrukerActionListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {	
			log.debug("Action performed i endre bruker: " + arg0.getActionCommand());
			if(arg0.getSource() == tilbakestillBtn || arg0.getSource() == hentBtn){
				hentStudent();
			} else if(arg0.getSource() == lagreBtn){
				if(student != null){
					info.setText(" ");
					boolean ok = true;

					student.setFirstname(firstName.getText());
					student.setLastname(lastName.getText());
					try {
						student.setCardNumber(Integer.parseInt(magnetCardNumber.getText()));
					} catch (NumberFormatException e) {
						if(magnetCardNumber.getText().length()==0){
							student.setCardNumber(null);
							ok = true;
						} else {
							info.setText("Kortnr. (magnet) ikke noe tall");
							ok = false;
						}
					}
					try {
						student.setRfidCardNumber(Long.parseLong(rfidCardNumber.getText()));
					} catch (NumberFormatException e) {
						if(rfidCardNumber.getText().length()==0){
							student.setRfidCardNumber(null);
							ok = true;
						} else {
							info.setText("Kortnr. (rfid) ikke noe tall");
							ok = false;
						}
					}
					if(ok){
						logic.saveStudent(student);
						student = null;
						refreshData();
						info.setText("Lagret");
						brukernavn.setText("");
					}
				} else {
					info.setText("Hent bruker først");
				}
			}
		}
	}
	private void disableInputs(){
		brukernavnChoosen.setEnabled(false);
		firstName.setEnabled(false);
		lastName.setEnabled(false);
		magnetCardNumber.setEnabled(false);
		rfidCardNumber.setEnabled(false);
		lagreBtn.setEnabled(false);
		tilbakestillBtn.setEnabled(false);
	}
	private void enableInputs(){
		brukernavnChoosen.setEnabled(true);
		firstName.setEnabled(true);
		lastName.setEnabled(true);
		magnetCardNumber.setEnabled(true);
		rfidCardNumber.setEnabled(true);
		lagreBtn.setEnabled(true);
		tilbakestillBtn.setEnabled(true);
		
	}
	private final class EnterKeyListener implements KeyListener{

		@Override
		public void keyPressed(KeyEvent e) {
			if(KeyEvent.VK_ENTER == e.getKeyCode()){
				if(brukernavn.getText().length()>0){
					hentStudent();
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
	
	public void entered(){
		brukernavn.grabFocus();
	}
	public void exit(){
		student = null;
		brukernavn.setText("");
		refreshData();
	}
	@Override
	public void fading(boolean state) {
		log.debug("EndreBruker fading: " + state);
		if(state){
			disableInputs();
			brukernavn.setEnabled(false);
			hentBtn.setEnabled(false);
		} else {
			if(student != null)
				enableInputs();
			brukernavn.setEnabled(true);
			hentBtn.setEnabled(true);
		}
		
	}
}
