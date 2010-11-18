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

import no.abakus.bedcard.gui.panels.content.list.BedCardList;
import no.abakus.bedcard.storage.domainmodel.Event;

final class EventListCellRenderer extends JPanel implements ListCellRenderer {
    private JLabel tittel;
    private JLabel dato;
    private JLabel spaceLabel;
    private SimpleDateFormat sdf;
    
    
    EventListCellRenderer() {
    	sdf = new SimpleDateFormat("dd.MM.yyyy");
    	tittel = new JLabel(" ");
    	spaceLabel = new JLabel("");
    	tittel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        dato = new JLabel(" ");
        dato.setFont(new Font("Tahoma", Font.PLAIN, 12));
        tittel.setPreferredSize(new Dimension(215, 20));
       // dato.setPreferredSize(new Dimension(10, 20));
        
        spaceLabel.setPreferredSize(new Dimension(10, 20));
        setLayout(new FlowLayout(FlowLayout.LEFT, 8, 3));

        add(tittel);
        add(spaceLabel);
        add(dato);
        setOpaque(true);
    }
    
    public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {
    	BedCardList bList = (BedCardList) list;
    	Event event = (Event)value;
    	if(event != null){
    		if(event.getTitle() != null)
    			tittel.setText(event.getTitle());
    		if(event.getStartTime() != null)
    			dato.setText(sdf.format(event.getStartTime()));
    	} else {
    		tittel.setText("?");
    		dato.setText("?");
    		
    	}
        if(index%2==0){
        	if (!isSelected) {
        		adjustColors(bList.getBackground(), bList.getForeground(), this, tittel, dato, spaceLabel);
            } else {
            	adjustColors(bList.getSelectionBackground(), bList.getSelectionForeground(), this, tittel, dato, spaceLabel);
            }
        } else {
        	if (!isSelected) {
        		adjustColors(bList.getAlternateBackground(), bList.getAlternateForeground(), this, tittel, dato, spaceLabel);
            } else {
            	adjustColors(bList.getSelectionAlternateBackground(), bList.getSelectionAlternateForeground(), this, tittel, dato, spaceLabel);
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
