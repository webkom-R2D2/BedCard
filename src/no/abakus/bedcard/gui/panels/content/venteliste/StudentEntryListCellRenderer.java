package no.abakus.bedcard.gui.panels.content.venteliste;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import no.abakus.bedcard.gui.BedCard;
import no.abakus.bedcard.gui.panels.content.list.AdmittedPanel;
import no.abakus.bedcard.gui.panels.content.list.BedCardList;
import no.abakus.bedcard.storage.domainmodel.StudentEntry;

final class StudentEntryListCellRenderer extends JPanel implements ListCellRenderer {
    private JLabel navnLabel;
    private JLabel brukernavnLabel;
    private AdmittedPanel imageLabel;
    private JLabel spaceLabel;
    private JLabel tillatLabel;
    private JLabel spaceLabel2;
    private int width;
    
    StudentEntryListCellRenderer(int width, BedCard bedcard) {
    	this.width = width;
    	navnLabel = new JLabel(" ");
    	spaceLabel = new JLabel("");
    	spaceLabel2 = new JLabel("");
    	tillatLabel = new JLabel(" ");
    	navnLabel.setFont(new Font("Tahoma", Font.PLAIN, 17));
    	tillatLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
        brukernavnLabel = new JLabel(" ");
        brukernavnLabel.setFont(new Font("Tahoma", Font.PLAIN, 17));
        
        imageLabel = new AdmittedPanel(bedcard);
                
        navnLabel.setPreferredSize(new Dimension(250, 20));
        spaceLabel.setPreferredSize(new Dimension(10, 20));
        spaceLabel2.setPreferredSize(new Dimension(10, 20));
        brukernavnLabel.setPreferredSize(new Dimension(100, 20));
        setLayout(new FlowLayout(FlowLayout.LEFT, 8, 3));
        add(imageLabel);
        add(navnLabel);
        add(spaceLabel);
        add(brukernavnLabel);
        add(spaceLabel2);
        add(tillatLabel);
        setOpaque(true);
    }
    
    public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {
    	BedCardList bList = (BedCardList) list;
    	StudentEntry entry = (StudentEntry)value;
    	if(entry != null && entry.getStudent() != null){
    		navnLabel.setText(entry.getStudent().getFirstname() + " " + entry.getStudent().getLastname());
    		brukernavnLabel.setText(entry.getStudent().getUsername());
    		imageLabel.setStatus(entry.getEntered());
    		if(entry.getStudent().isHasAccess()){
    			tillatLabel.setText(" ");
    		} else {
    			tillatLabel.setText("ikke tilgang");
    		}
    	} else {
    		navnLabel.setText("?");
    		brukernavnLabel.setText("?");
    		imageLabel.setStatus(null);
    		tillatLabel.setText("?");
    	}
        if(index%2==1){
        	if (!isSelected) {
        		adjustColors(bList.getBackground(), bList.getForeground(), this, navnLabel, brukernavnLabel, imageLabel, spaceLabel, spaceLabel2, tillatLabel);
            } else {
            	adjustColors(bList.getSelectionBackground(), bList.getSelectionForeground(), this, navnLabel, brukernavnLabel, imageLabel, spaceLabel, spaceLabel2, tillatLabel);
            }
        } else {
        	if (!isSelected) {
        		adjustColors(bList.getAlternateBackground(), bList.getAlternateForeground(), this, navnLabel, brukernavnLabel, imageLabel, spaceLabel, spaceLabel2, tillatLabel);
            } else {
            	adjustColors(bList.getSelectionAlternateBackground(), bList.getSelectionAlternateForeground(), this, navnLabel, brukernavnLabel, imageLabel, spaceLabel, spaceLabel2, tillatLabel);
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
