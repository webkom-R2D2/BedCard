package no.abakus.bedcard.logic.cardreader;

public class TestCard implements CardReaderListener {

	private static CardReader rfid;
	
	@Override
	public void notifyReaderStatusChange(CardReader reader) {
		System.out.println(reader.getName() + (reader.isConnected() ? " ble koblet til" : " ble koblet fra"));
	}

	@Override
	public void receiveOldCardNumber(int cardNumber) {
		System.out.println("Fikk gammelt kortnummer: " + cardNumber);
	}

	@Override
	public void receiveRFIDCardNumber(long cardNumber) {
		System.out.println("Fikk nytt kortnummer: " + cardNumber);
		//System.out.println("Stopper kortleser");
		//rfid.stop();
		//System.out.println("Starter kortleser");
		//rfid.start();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TestCard ts = new TestCard();
		rfid = new NewRFIDReader(ts);
		//rfid = new RFIDReader(ts);
		rfid.start();
		System.out.println(rfid);
	}

}
