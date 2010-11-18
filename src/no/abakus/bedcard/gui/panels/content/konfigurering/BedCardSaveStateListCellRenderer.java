package no.abakus.bedcard.gui.panels.content.konfigurering;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.text.SimpleDateFormat;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import org.apache.log4j.Logger;

import no.abakus.bedcard.gui.BedCard;
import no.abakus.bedcard.gui.panels.content.list.BedCardList;
import no.abakus.bedcard.gui.panels.content.list.SavedPanel;
import no.abakus.bedcard.storage.domainmodel.BedCardSaveState;
import no.abakus.bedcard.storage.domainmodel.Event;

final class BedCardSaveStateListCellRenderer extends JPanel implements ListCellRenderer {
	private static Logger log = Logger.getLogger(BedCardSaveStateListCellRenderer.class);
    private JLabel tittel;
    private JLabel beskrivelse;
    private JLabel dato;
    private JLabel spaceLabel;
    private JLabel spaceLabel2;
    private SavedPanel savedPanel;
    private SimpleDateFormat sdf;
    
    
    BedCardSaveStateListCellRenderer(BedCard bedcard) {
    	sdf = new SimpleDateFormat("dd.MM.yyyy");
    	tittel = new JLabel(" ");
    	beskrivelse = new JLabel(" ");
    	spaceLabel = new JLabel("");
    	savedPanel = new SavedPanel(bedcard);
    	spaceLabel2 = new JLabel("");
    	tittel.setFont(new Font("Tahoma", Font.PLAIN, 14));
    	beskrivelse.setFont(new Font("Tahoma", Font.PLAIN, 14));
        dato = new JLabel(" ");
        dato.setFont(new Font("Tahoma", Font.PLAIN, 12));
        tittel.setPreferredSize(new Dimension(150, 20));
        beskrivelse.setPreferredSize(new Dimension(310, 20));
        
        
        spaceLabel.setPreferredSize(new Dimension(10, 20));
        setLayout(new FlowLayout(FlowLayout.LEFT, 8, 3));

        add(savedPanel);
        add(tittel);
        add(spaceLabel);
        add(beskrivelse);
        add(spaceLabel2);
        add(dato);        
        setOpaque(true);
    }
    
    public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {
    	BedCardList bList = (BedCardList) list;
    	
    	Event event = ((BedCardSaveState)value).getEvent();
    	if(event != null){
    		if(event.getTitle() != null)
    			tittel.setText(event.getTitle());
    		if(event.getStartTime() != null)
    			dato.setText(sdf.format(event.getStartTime()));
    		if(event.getDescription() != null)
    			beskrivelse.setText(event.getDescription());
    		savedPanel.setStatus(((BedCardSaveState)value).isSavedToAbakus());
    	} else {
    		tittel.setText("?");
    		dato.setText("?");
    		beskrivelse.setText("?");
    		savedPanel.setStatus(null);
    	}
        if(index%2==0){
        	if (!isSelected) {
        		adjustColors(bList.getBackground(), bList.getForeground(), this, tittel, dato, spaceLabel, savedPanel, beskrivelse, spaceLabel2);
            } else {
            	adjustColors(bList.getSelectionBackground(), bList.getSelectionForeground(), this, tittel, dato, spaceLabel, savedPanel, beskrivelse, spaceLabel2);
            }
        } else {
        	if (!isSelected) {
        		adjustColors(bList.getAlternateBackground(), bList.getAlternateForeground(), this, tittel, dato, spaceLabel, savedPanel, beskrivelse, spaceLabel2);
            } else {
            	adjustColors(bList.getSelectionAlternateBackground(), bList.getSelectionAlternateForeground(), this, tittel, dato, spaceLabel, savedPanel, beskrivelse, spaceLabel2);
            }
        }
        return this;
    }
    
    private void adjustColors(Color bg, Color fg, Component...components) {
        for (Component c : components) {
            c.setForeground(fg);
            c.setBackground(bg);
        }
    }
}
