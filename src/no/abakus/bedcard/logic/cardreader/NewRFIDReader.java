/*
 * RFIDReader.java
 *
 * Created on 16. mars 2007, 14:00
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package no.abakus.bedcard.logic.cardreader;

import gnu.io.SerialPortEvent;
import java.util.Calendar;

/**
 *
 * @author Stian Sønderland
 * @author Christian Askeland
 */

public class NewRFIDReader extends CardReader {
    
    // constructor:
    public NewRFIDReader(CardReaderListener listener) {
        super(listener);
        readerName = "RFID (ny)";
        getNameChar = "L";
    }

    public void process(String message) {
    	//System.out.println((int)message.charAt(0));
        // STX == 2
        // LF == 10
        // ESC == 27
        // CR == 13
        // Button pushed = 13 + 7
        try {
            if (!connected) {
                if (message.indexOf("S") != -1) {
                    sendMessage("c");
                    connected = true;
                }
            } else if (message.charAt(0) == 85) {
            	if(message.length()>=12) {
            		notifyCardNumber(message.substring(1, 11));
            	}	
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void notifyCardNumber(String number) {
    	Long cardNumber = Long.parseLong(number, 16);
    	//System.out.println("Translate: " + translate(cardNumber));
    	l.receiveRFIDCardNumber(translate(cardNumber));
    }
   /* 
    public void serialEvent(SerialPortEvent event) {
    	System.out.println("Test");
    	log.debug("serialEvent():"+ Calendar.getInstance().getTime());
        String c = "";
        byte[] b = new byte[1];
        try {
            isReady = false;
            while (in.ready()) {
                b[0] = (byte)in.read();
                c = c + new String(b, "ASCII");
                Thread.sleep(5);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("feil i lesing av bytes fra kortleser");
        }
        isReady = true;
        log.debug("Data fra kortleser (" + this.getName() + "):" + c);
        process(c);
    }
  */  
    /**
	 * Translate card number from card reader
	 * 
	 * For each byte, reverse the bit order, making LSB become MSB.
	 * 
	 * Example:
	 *           Value: 01110000 00000000 00101111 11100010 00000010 
	 *   Translated to: 00001110 00000000 11110100 01000111 01000000
	 *   
	 *           Value: abcdefgh abcdefgh abcdefgh abcdefgh abcdefgh
	 *   Translated to: hgfedcba hgfedcba hgfedcba hgfedcba hgfedcba
	 * 
	 * @param oldValue Card number value
	 * @return Translated card number value
	 */
	public Long translate(Long oldValue) { 
		long newValue = 0;
		for (int part=0; part<8; part++) {
			long oldPart = ( (oldValue >> part*8) & 0xFF);
			long newPart = 0;
			for (int i=0; i<8; i++) {
				boolean bitSet = (((oldPart & (int) Math.pow(2, i)) & 0xFF) > 0 );
				if (bitSet) {
					newPart += (int) Math.pow(2, (7-i));
				} 
			}
			newValue += (newPart << part*8);
		}
		return newValue;
	}
}
