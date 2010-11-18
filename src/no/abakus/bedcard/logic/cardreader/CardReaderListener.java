/*
 * cardReaderListener.java
 *
 * Created on 16. mars 2007, 13:59
 *
 */

package no.abakus.bedcard.logic.cardreader;

/**
 * 
 *  @author Stian Sønderland
 */

public interface CardReaderListener {
    public void receiveOldCardNumber(int cardNumber);
    public void receiveRFIDCardNumber(long cardNumber);
    public void notifyReaderStatusChange(CardReader reader);
}
