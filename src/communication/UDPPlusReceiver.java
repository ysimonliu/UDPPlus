package communication;

import java.net.DatagramPacket;

import reedSolomon.ReedSolomonException;
import forwardErrorCorrection.FECInterface;
import forwardErrorCorrection.ReedSolomonImpl;
import forwardErrorCorrectionException.NotAsciiCodeExpcetion;
import forwardErrorCorrectionException.UndecodableException;
import forwardErrorCorrectionException.UnlocatableErrorException;

public class UDPPlusReceiver {

	byte[] inBuf;
	DatagramPacket inPacket;
	FECInterface fec;
	
	public UDPPlusReceiver(FECInterface fec) {
		this.fec = fec;
	}
	
	public String receive() throws UndecodableException, UnlocatableErrorException{
				
		while (true) {
    	  //Receive reversed message from server
    	  this.inBuf = new byte[256];
    	  this.inPacket = new DatagramPacket(inBuf, inBuf.length);
    	  String data = new String(inPacket.getData(), 0, inPacket.getLength());
    	  System.out.println("Received data from Transmitter : " + data);
    	  try {
    		  return fec.decode(data);
    	  } catch (ReedSolomonException | NotAsciiCodeExpcetion e) {
    		  System.out.println("Receiver Failed to decode the data using Reed Solomon");
    	  }
		}
	}
}