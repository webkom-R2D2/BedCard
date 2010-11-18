/*
 * cardReaderListener.java
 *
 * Created on 16. mars 2007, 13:59
 *
 */

package no.abakus.bedcard.logic.cardreader;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Enumeration;
import gnu.io.*;

import org.apache.log4j.Logger;

/**
 * 
 *  @author Stian Sønderland
 */

public abstract class CardReader implements SerialPortEventListener {
	protected static Logger log = Logger.getLogger(CardReader.class);
    // global variables:
    CardReaderListener l;
    SerialPort sPort;
    OutputStream out;
    InputStreamReader in;
    CommPortIdentifier cpID;
    
    // status variables
    boolean isRunning = false;
    boolean isReady = true;
    boolean connected = false;
    
    // abstract
    String portname;
    String readerName = "DefaultReader";
    String getNameChar = "N";
    
    
    /** Constructor */
    public CardReader(CardReaderListener listener) {
        l = listener;
    }
    
    
    /** Public methods  */
    
    public void start() {
    	log.debug("Starter leser: " + readerName);
    	System.out.println("Start - connect");
    	autoDetectPort();
        l.notifyReaderStatusChange(this);
    }

    public void stop() {
    	if (!connected) return;
    	if (sPort != null) {
    		log.debug("Stopper leser: " + readerName);
    	    sPort.close();
    	}
    	connected = false;
    	l.notifyReaderStatusChange(this);
    }
    
    public boolean isConnected() {
        return connected;
    }
    
    public String getName() {
        return readerName;
    }
    
    public String getPortName() {
    	return portname;
    }
    
    public void setCardReaderListener(CardReaderListener listener) {
    	l = listener;
    }
    
    
    /** Defined by SerialPortEventListener  */
    
    public void serialEvent(SerialPortEvent event) {
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
    
    
    /** Other methods  */
    
    protected boolean sendMessage(String s) {
    	log.debug("sendMessage()");
        if (isReady) {
            byte[] b = s.getBytes();
            try {
                out.write(b);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        } else {
            log.debug("Error while sending: not ready to send message: " + s);
            try {
                Thread.sleep(200);
                sendMessage(s);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
    
    protected boolean setPort(CommPortIdentifier portId) {
        if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
            try {
            sPort = (SerialPort) portId.open(portId.getName(), 9600);
            sPort.addEventListener(this);
            out = sPort.getOutputStream();
            in = new InputStreamReader(sPort.getInputStream(), "ASCII");
            sPort.notifyOnDataAvailable(true);
            } catch (PortInUseException ex) {
            	log.debug(portId.getName() + " er opptatt...");
                return false;
            } catch (Exception e) {
            	log.debug("Exception i setPort()...");
                e.printStackTrace();
                return false;
            }
        }
        cpID = portId;
        return true;
    }
    
    @SuppressWarnings("unchecked")
    private boolean autoDetectPort() {
        	log.debug("Getting COMPortIdentifiers");
            Enumeration<CommPortIdentifier> e = null;
			try {
				e = CommPortIdentifier.getPortIdentifiers();
			} catch (java.lang.UnsatisfiedLinkError e2) {
				log.error("Finner ingen comporter. Installert driver? (rxtxSerial.dll)");
			} catch (Exception e3){}
            if(e != null){
	            while (e.hasMoreElements() && !connected) {
	                CommPortIdentifier cpi = ((CommPortIdentifier)e.nextElement());
	                if (cpi.getName().substring(0, 3).equals("COM")) {
	                	log.debug("COM-port funnet: " + cpi.getName());
	                    if (!cpi.isCurrentlyOwned()) {
	                        if (setPort(cpi)) {
	                            portname = cpi.getName();                     
	                            log.debug(getName() + " prøver å koble til " + portname + "...");
	                            sendMessage(getNameChar);
	                            try {
									Thread.sleep(400);
								} catch (InterruptedException e1) {
								}
	                            if (!connected) {
	                            	log.debug(getName() + " svarte ikke på " + portname + "... tid:"+ Calendar.getInstance().getTime());
	                                sPort.close();
	                            }
	                        }
	                    }
	                }
	            }
            }
        if (connected) {
            log.debug("---: " + this.getName() + " koblet til på " + portname);
            return true;
        } else {
        	log.debug("Reader: " + readerName + ": could not connect");
        	log.debug("If we can't connect the reader in time, i.e. the serialports close WHILE " +
        			"process() is running on the returned string from the reader, we will get " +
        			"an UN-connected USB-reader that thinks it's connected :P");
            return false;
        }    
    } 

	abstract protected void process (String message);
}
