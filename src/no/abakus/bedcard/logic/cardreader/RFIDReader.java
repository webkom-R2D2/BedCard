/*
 * RFIDReader.java
 *
 * Created on 16. mars 2007, 14:00
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package no.abakus.bedcard.logic.cardreader;

/**
 *
 * @author Stian Sønderland
 */

public class RFIDReader extends CardReader {
    
    
    // constructor:
    public RFIDReader(CardReaderListener listener) {
        super(listener);
        readerName = "RFID";
        getNameChar = "N";
    }

    public void process(String message) {
    	// STX == 2
        // LF == 10
        // ESC == 27
        // CR == 13
        // Button pushed = 13 + 7
        try {
            if (!connected) {
                if (message.indexOf("PCR300A-02") != -1) {
                    sendMessage("B");
                    connected = true;
                }
            } else if (message.charAt(0) == 2) {
            	notifyCardNumber(message.substring(1, 11));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void notifyCardNumber(String number) {
    	Long cardNumber = Long.parseLong(number, 16);
    	l.receiveRFIDCardNumber(cardNumber);
    }
    
}
