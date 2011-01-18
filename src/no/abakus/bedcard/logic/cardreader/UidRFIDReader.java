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

import java.nio.ByteBuffer;
import java.util.Calendar;

/**
 *
 * @author Stian S�nderland
 */

public class UidRFIDReader extends CardReader {
    
    // constructor:
    public UidRFIDReader(CardReaderListener listener) {
        super(listener);
        readerName = "RFID";
        getNameChar = "L";
    }

    public void process(String message) {
    	System.out.println("process("+ message +")");
    	System.out.println((int)message.charAt(0));
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
            	System.out.println("Lengde på string:" + message.length());
            	notifyCardNumber(message.substring(1, 11));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void notifyCardNumber(String number) {
    	System.out.println("notifyCardNumber("+number+")");
    	Long cardNumber = Long.parseLong(number, 16);
    	l.receiveRFIDCardNumber(cardNumber);
    }
    public void serialEvent(SerialPortEvent event) {
    	System.out.println("Test");
    	log.debug("serialEvent():"+ Calendar.getInstance().getTime());
        String c = "";
        byte[] b = new byte[1];
        try {
            isReady = false; 
            while (in.ready()) {
            	//if(connected)
            	//	for(int i = 0; i<3; i++)
            	//		in.read();
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
    
    int bytesToInt(byte[] intBytes){
    	ByteBuffer bb = ByteBuffer.wrap(intBytes);
    	return bb.getInt();
    }
}
