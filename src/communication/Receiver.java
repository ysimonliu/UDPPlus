package communication;

import java.net.DatagramPacket;

import reedSolomon.ReedSolomonException;

import forwardErrorCorrection.ReedSolomonImpl;
import forwardErrorCorrectionException.NotAsciiCodeExpcetion;

public class Receiver {

	public static void main(String[] args){
		
		ReedSolomonImpl rsImpl = new ReedSolomonImpl(Transmitter.EC_BYTES);
		
		while (true) {
    	  //Receive reversed message from server
    	  byte[] inBuf = new byte[256];
    	  DatagramPacket inPacket = new DatagramPacket(inBuf, inBuf.length);
    	  String data = new String(inPacket.getData(), 0, inPacket.getLength());
    	  System.out.println("Received data from Transmitter : " + data);
    	  try {
    		  System.out.println("Decoded data: " + rsImpl.decode(data));
    	  } catch (ReedSolomonException | NotAsciiCodeExpcetion e) {
    		  System.out.println("Receiver Failed to decode the data using Reed Solomon");
    	  }
		}
	}
}
