/*
 * MagneticReader.java
 *
 * Created on 17. mars 2007, 03:47
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package no.abakus.bedcard.logic.cardreader;

/**
 *
 * @author Stian
 */

public class MagnetReader extends CardReader {
    
    
    // constructor:
    public MagnetReader(CardReaderListener listener) {
    	super(listener);
        readerName = "Magnetstripe";
        getNameChar = "R";
    }
    
    public void process(String message) {
        // VERSION: V
        // READY?: R
        // U? U
        try {
            if (!connected) {
            	// Strengen under gir meg feil, jeg får bare B21READY på mac|ern jaffal ...
            	// if (message.indexOf("UB2100READY") != -1 || message.indexOf("USB PROGRAMMABLE") != -1) {
            	if (message.indexOf("B2100READY") != -1 || message.indexOf("USB PROGRAMMABLE") != -1) {
                      connected = true;
                }
            } else {
            	log.debug("process(): Message length: " + message.length());
                if (message.length() == 26) notifyCardNumber(message.substring(13, 22));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void notifyCardNumber(String number) {
    	log.debug("notifyCardReader(): number before parse:"+number);
    	int cardNumber = Integer.valueOf(number);
    	log.debug("notifyCardReader(): sending number to LogicHandler:"+cardNumber);
    	l.receiveOldCardNumber(cardNumber);
    }
}
