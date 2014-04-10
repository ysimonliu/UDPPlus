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
/**
 * The UDPPlusReceiver is a protocol based on UDP, but is capable of correcting errors occurred during transmission when decoding a message
 * which was sent over by {@link UDPPlusTransmitter}
 * 
 * @author Simon Liu
 *
 */
public class UDPPlusReceiver {

	byte[] inBuf;
	DatagramPacket inPacket;
	FECInterface fec;
	int port;
	private DatagramSocket serverSocket;
	
	/**
	 * Constructs an UDPPlusReceiver instance
	 * @param IPAddress - the IP address of the receiver
	 * @param port - the port of the receiver
	 * @param fec - the forward error correction algorithm
	 * @throws SocketException
	 */
	public UDPPlusReceiver(InetAddress IPAddress, int port,  FECInterface fec) throws SocketException {
		this.fec = fec;
		this.port = port;
		this.serverSocket = new DatagramSocket(port, IPAddress);
	}
	
	/**
	 * Receives a message from UDPPlusTransmitter, and decodes the message
	 * @return
	 * @throws IOException
	 */
	public String receive() throws IOException{
				
		while (true) {
    	  //Receive reversed message from server
    	  this.inBuf = new byte[65508];
    	  this.inPacket = new DatagramPacket(inBuf, inBuf.length);
    	  serverSocket.receive(inPacket);
    	  String data = new String(inPacket.getData(), 0, inPacket.getLength());
    	  if (!data.trim().isEmpty()) {
	    	  try {
	    		  String decodedText = fec.decode(data);
	    		  return decodedText;
	    	  } catch (ReedSolomonException | NotAsciiCodeExpcetion | UndecodableException | UnlocatableErrorException ex) {
	    		  System.out.println("Receiver received some message but not by a UDPPlusTransmitter with the same FEC type.");
	    	  } finally {
	    		  serverSocket.close();
	    	  }
    	  }
		}
	}
}