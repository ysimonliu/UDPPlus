package communication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import forwardErrorCorrection.FECInterface;

/**
 * The UDPPlusTransmitter is a protocol based on UDP, but is capable of encoding messages using the given FEC algorithms,
 * which when an {@link UDPPlusReceiver} receives the message, will decode, and correct any errors occurred during transmission
 * 
 * @author Simon Liu
 *
 */
public class UDPPlusTransmitter {
	
	public static final int EC_BYTES = 3;
	private DatagramSocket socket;
	private byte[] outBuf;
	private FECInterface fec;
	private InetAddress IPAddress;
	private int port;
	
	/**
	 * Constructs an UDPPlusTransmitter instance
	 * @param IPAddress - the IP address of the receiver
	 * @param port - the port of the receiver
	 * @param fec - the forward error correction algorithm
	 * @throws SocketException
	 */
	public UDPPlusTransmitter(InetAddress IPAddress, int port, FECInterface fec) throws SocketException {
		this.socket = new DatagramSocket();
		this.outBuf = new byte[65508];
		this.fec = fec;
		this.IPAddress = IPAddress;
		this.port = port;
	}
	
	/**
	 * Sends out a message, an instance of UDPPlusReceiver should be receiving before this method is called
	 * @param msg - the message to be sent over in plain text as a {@link String}}
	 * @throws IOException
	 */
	public void send(String msg) throws IOException{
		try {
			String encoded = fec.encode(msg);
			outBuf = encoded.getBytes();
			DatagramPacket outPacket = new DatagramPacket(outBuf, outBuf.length, IPAddress, port);
			socket.send(outPacket);
		} catch (IOException ex) {
			System.out.println("Transmitter failed to send message");
		} finally {
			socket.close();
		}
	}
}