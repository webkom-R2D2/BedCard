package no.abakus.bedcard.gui.panels.content.venteliste;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import no.abakus.bedcard.gui.BedCard;
import no.abakus.bedcard.gui.panels.content.BoxPanel;
import no.abakus.bedcard.gui.panels.content.ContentPanel;
import no.abakus.bedcard.gui.panels.content.ContentPanelMother;
import no.abakus.bedcard.gui.panels.content.list.BedCardList;
import no.abakus.bedcard.gui.panels.content.list.StudentEntryFilterList;
import no.abakus.bedcard.logic.Logic;
import no.abakus.bedcard.storage.domainmodel.StudentEntry;


public class Venteliste extends ContentPanelMother{
	JList entryList;
	StudentEntryFilterList venteliste;
	JTextField brukernavn;
	JTextField navn;
	JRadioButton ikkeSluppetInn;
	
	JButton oppmote;
	JButton ikkeOppmote;
	JButton ukjent;
	JButton velgAlle;
	JButton fjern;
	
	public Venteliste(BedCard bedcard, Logic logic, ContentPanel contentPanel) {
		super(bedcard, logic, contentPanel);
		venteliste = new StudentEntryFilterList();
		entryList = new BedCardList();
		entryList.setCellRenderer(new StudentEntryListCellRenderer(700, bedcard));
		entryList.setPrototypeCellValue(new StudentEntry());
		entryList.setAutoscrolls(true);
		entryList.addMouseListener(new VentelisteMouseListener());
		entryList.addKeyListener(new EnterKeyListener());
		refreshData();
		BoxPanel boxPanel = new BoxPanel(bedcard, "Venteliste", 700, bedcard.getDrawHeight()-BedCard.BottomStatusPanelHeight-BedCard.MenuPanelHeight);
		boxPanel.getInternPanel().setLayout(new BorderLayout());
		add(boxPanel);
		
		//Lag bunnmeny
		Font knappFont = new Font("Tahoma", Font.PLAIN, 12);

		oppmote = new JButton("Oppmøte");
		oppmote.addActionListener(new FilterActionListener());
		oppmote.setBackground(boxPanel.getBottomPanel().getBackground());
		oppmote.setFont(knappFont);
		ikkeOppmote = new JButton("Frafall");
		ikkeOppmote.addActionListener(new FilterActionListener());
		ikkeOppmote.setBackground(boxPanel.getBottomPanel().getBackground());
		ikkeOppmote.setFont(knappFont);
		ukjent = new JButton("Ukjent status");
		ukjent.addActionListener(new FilterActionListener());
		ukjent.setBackground(boxPanel.getBottomPanel().getBackground());
		ukjent.setFont(knappFont);
		velgAlle = new JButton("Velg alle");
		velgAlle.addActionListener(new FilterActionListener());
		velgAlle.setBackground(boxPanel.getBottomPanel().getBackground());
		velgAlle.setFont(knappFont);
		fjern = new JButton("Fjern");		
		fjern.addActionListener(new FilterActionListener());
		fjern.setBackground(boxPanel.getBottomPanel().getBackground());
		fjern.setFont(knappFont);
		
		boxPanel.getBottomPanel().setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 12));
		boxPanel.getBottomPanel().add(oppmote);
		boxPanel.getBottomPanel().add(ikkeOppmote);
		boxPanel.getBottomPanel().add(ukjent);
		boxPanel.getBottomPanel().add(new JLabel("       "));
		boxPanel.getBottomPanel().add(fjern);
		boxPanel.getBottomPanel().add(velgAlle);
		
		
		//Lag toppmeny (filter)
		JPanel top = new JPanel();
		top.setBackground(boxPanel.getInternPanel().getBackground());
		top.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 0));
	
		//Adding filters
		JPanel filter1 = new JPanel();
		filter1.setBackground(boxPanel.getInternPanel().getBackground());
		filter1.add(new JLabel("Brukernavn: "));
		brukernavn = new JTextField("");
		brukernavn.setPreferredSize(new Dimension(100,20));
		brukernavn.getDocument().addDocumentListener(new FilterDocumentHandler());
		filter1.add(brukernavn);
		top.add(filter1);
		
		JPanel filter2 = new JPanel();
		filter2.setBackground(boxPanel.getInternPanel().getBackground());
		filter2.add(new JLabel("Navn: "));
		navn = new JTextField("");
		navn.setPreferredSize(new Dimension(100,20));
		navn.getDocument().addDocumentListener(new FilterDocumentHandler());
		filter2.add(navn);
		top.add(filter2);

		JPanel filter4 = new JPanel();
		filter4.setBackground(boxPanel.getInternPanel().getBackground());
		filter4.add(new JLabel("Kun ikke oppmøtte: "));
		ikkeSluppetInn = new JRadioButton();
		ikkeSluppetInn.setBackground(boxPanel.getInternPanel().getBackground());
		ikkeSluppetInn.setSelected(venteliste.isShowOnlyNotAdmitted());
		ikkeSluppetInn.addChangeListener(new FilterListChangeListener());
		filter4.add(ikkeSluppetInn);
		top.add(filter4);
		boxPanel.getInternPanel().add(top, BorderLayout.NORTH);
		
		//Lag midten
		JScrollPane scrollPane = new JScrollPane(entryList);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBorder(null);
		boxPanel.getInternPanel().add(scrollPane, BorderLayout.CENTER);
	}
	public void entered(){
		refreshData();
		resetFilter();
	}
	public void refreshData(){
		log.debug("Antall på ventelista: " + logic.getListOfParticipants().size());
		venteliste.setSelection(logic.getWaitingList());
		entryList.setListData(venteliste.getFilteredEntries().toArray());
		repaint();
		bedcard.updateInformation();
	}
	private void actionOnStudent(){
		int countOk = 0;
		int countNotOk = 0;
		for(int index : entryList.getSelectedIndices()){
			StudentEntry entry = venteliste.getFilteredEntries().get(index);
			if(entry.enteredIsNotFalseOrNull()){
				countOk++;
			} else {
				countNotOk++;
			}
		}
		boolean result = countOk>=countNotOk;
		if(countNotOk == 0)
			result = false;
		if(countOk == 0)
			result = true;
		for(int index : entryList.getSelectedIndices()){
			StudentEntry entry = venteliste.getFilteredEntries().get(index);
			entry.setEntered(result);
			logic.saveRegistrationStatus(entry);
		}
		resetFilter();
	}
	private void resetFilter(){
		entryList.setSelectedIndices(new int[0]);
		brukernavn.setText("");
		navn.setText("");
	}
	@Override
	public void fading(boolean state) {
		brukernavn.setEnabled(!state);
		navn.setEnabled(!state);
		ikkeSluppetInn.setEnabled(!state);
		oppmote.setEnabled(!state);
		ikkeOppmote.setEnabled(!state);
		ukjent.setEnabled(!state);
		velgAlle.setEnabled(!state);
		fjern.setEnabled(!state);		
	}
	
	private final class FilterActionListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {	
			log.debug("Action performed i påmeldte: " + arg0.getActionCommand());
			if(arg0.getSource() == oppmote){
				for(int i : entryList.getSelectedIndices()){
					StudentEntry entry = venteliste.getFilteredEntries().get(i);
					entry.setEntered(true);
					logic.saveRegistrationStatus(entry);
				}
				resetFilter();
				refreshData();
			} else if(arg0.getSource() == ikkeOppmote){
				for(int i : entryList.getSelectedIndices()){
					StudentEntry entry = venteliste.getFilteredEntries().get(i);
					entry.setEntered(false);
					logic.saveRegistrationStatus(entry);
				}
				resetFilter();
				refreshData();
			} else if(arg0.getSource() == ukjent){
				for(int i : entryList.getSelectedIndices()){
					StudentEntry entry = venteliste.getFilteredEntries().get(i);
					entry.setEntered(null);
					logic.saveRegistrationStatus(entry);
				}
				resetFilter();
				refreshData();
			}else if(arg0.getSource() == fjern){
				for(int i : entryList.getSelectedIndices()){
					StudentEntry entry = venteliste.getFilteredEntries().get(i);
					logic.removeStudentFromWaitinglist(entry.getStudent());
				}
				resetFilter();
				refreshData();
			} else if(arg0.getSource() == velgAlle){
				entryList.setSelectionInterval(0, venteliste.getFilteredEntries().size()-1);
			}
		}
	}

   
	private final class FilterDocumentHandler implements DocumentListener {
        public void insertUpdate(DocumentEvent e) {
        	venteliste.updateFilter(brukernavn.getText(), navn.getText(), ikkeSluppetInn.isSelected());
    		entryList.setListData(venteliste.getFilteredEntries().toArray());
        }

        public void removeUpdate(DocumentEvent e) {
        	venteliste.updateFilter(brukernavn.getText(), navn.getText(), ikkeSluppetInn.isSelected());
    		entryList.setListData(venteliste.getFilteredEntries().toArray());
        }

        public void changedUpdate(DocumentEvent e) {
        }
    }
    private final class FilterListChangeListener implements ChangeListener {
    	public void stateChanged(ChangeEvent arg0) {
    		if(venteliste.isShowOnlyNotAdmitted() != ikkeSluppetInn.isSelected()){
    			venteliste.setShowOnlyNotAdmitted(ikkeSluppetInn.isSelected());
    			entryList.setListData(venteliste.getFilteredEntries().toArray());
    		}
    	}
    }
	private final class VentelisteMouseListener implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2) {
				actionOnStudent();
				refreshData();
			}			
		}

		@Override
		public void mouseEntered(MouseEvent e) {}

		@Override
		public void mouseExited(MouseEvent e) {}

		@Override
		public void mousePressed(MouseEvent e) {}

		@Override
		public void mouseReleased(MouseEvent e) {}
		
	}
	private final class EnterKeyListener implements KeyListener{

		@Override
		public void keyPressed(KeyEvent e) {
			if(KeyEvent.VK_ENTER == e.getKeyCode()){
				if(e.getSource() == entryList){
					actionOnStudent();
					refreshData();
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
	
}
