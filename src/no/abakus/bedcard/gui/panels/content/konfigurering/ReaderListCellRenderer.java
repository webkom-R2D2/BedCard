package no.abakus.bedcard.gui.panels.content.konfigurering;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import org.apache.log4j.Logger;

import no.abakus.bedcard.gui.BedCard;
import no.abakus.bedcard.gui.panels.content.list.BedCardList;
import no.abakus.bedcard.gui.panels.content.list.ReaderPanel;
import no.abakus.bedcard.logic.cardreader.CardReader;

final class ReaderListCellRenderer extends JPanel implements ListCellRenderer {
	private static Logger log = Logger.getLogger(ReaderListCellRenderer.class);
    private JLabel tittel;

    private JLabel spaceLabel;
    private ReaderPanel savedPanel;

    
    
    ReaderListCellRenderer(BedCard bedcard) {

    	tittel = new JLabel(" ");
    	spaceLabel = new JLabel("");
    	savedPanel = new ReaderPanel(bedcard);
    	tittel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        
        
        
        spaceLabel.setPreferredSize(new Dimension(10, 20));
        setLayout(new FlowLayout(FlowLayout.LEFT, 8, 3));

        add(savedPanel);
        add(tittel);
        add(spaceLabel);
        setOpaque(true);
    }
    
    public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {
    	BedCardList bList = (BedCardList) list;
    	CardReader reader = (CardReader) value;
    	if(reader != null){
    		if(reader.getName() != null)
    			if (reader.isConnected())
    				tittel.setText(reader.getName() + " (" + reader.getPortName() + ")");
    			else 
    				tittel.setText(reader.getName());
    		savedPanel.setStatus(reader.isConnected());
    	} else {
    		tittel.setText("?");
    		savedPanel.setStatus(null);
    	}
        if(index%2==0){
        	if (!isSelected) {
        		adjustColors(bList.getBackground(), bList.getForeground(), this, tittel, spaceLabel, savedPanel);
            } else {
            	adjustColors(bList.getSelectionBackground(), bList.getSelectionForeground(), this, tittel, spaceLabel, savedPanel);
            }
        } else {
        	if (!isSelected) {
        		adjustColors(bList.getAlternateBackground(), bList.getAlternateForeground(), this, tittel, spaceLabel, savedPanel);
            } else {
            	adjustColors(bList.getSelectionAlternateBackground(), bList.getSelectionAlternateForeground(), this, tittel, spaceLabel, savedPanel);
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
