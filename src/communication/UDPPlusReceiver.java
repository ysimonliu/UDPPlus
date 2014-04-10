package communication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import reedSolomon.ReedSolomonException;
import forwardErrorCorrection.FECInterface;
import forwardErrorCorrectionException.NotAsciiCodeExpcetion;
import forwardErrorCorrectionException.UndecodableException;
import forwardErrorCorrectionException.UnlocatableErrorException;

public class UDPPlusReceiver {

	byte[] inBuf;
	DatagramPacket inPacket;
	FECInterface fec;
	int port;
	private DatagramSocket serverSocket;
	
	public UDPPlusReceiver(InetAddress IPAddress, FECInterface fec, int port) throws SocketException {
		this.fec = fec;
		this.port = port;
		this.serverSocket = new DatagramSocket(port, IPAddress);
	}
	
	public String receive() throws IOException{
				
		String data = "";
		
		while (true) {
    	  //Receive reversed message from server
    	  this.inBuf = new byte[65508];
    	  this.inPacket = new DatagramPacket(inBuf, inBuf.length);
    	  serverSocket.receive(inPacket);
    	  data = new String(inPacket.getData(), 0, inPacket.getLength());
    	  System.out.println("Received data from Transmitter: " + data);
    	  System.out.println("Data is empty: " + data.trim().isEmpty());
    	  System.out.println("Data's length is: " + data.length());
    	  if (!data.trim().isEmpty()) {
	    	  try {
	    		  String decodedText = fec.decode(data);
	    		  System.out.println("Decoded Text: " + decodedText);
	    		  
	    		  return decodedText;
	    	  } catch (ReedSolomonException | NotAsciiCodeExpcetion | UndecodableException | UnlocatableErrorException ex) {
	    		  System.out.println("Receiver Failed to decode the data using Reed Solomon: " + ex);
	    	  } finally {
	    		  serverSocket.close();
	    	  }
    	  }
    	  System.out.println("Now next iteration!");
		}
	}
}