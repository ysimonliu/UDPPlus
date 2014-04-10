package communication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import forwardErrorCorrection.FECInterface;

public class UDPPlusTransmitter {
	
	public static final int EC_BYTES = 3;
	private DatagramSocket socket;
	private byte[] outBuf;
	private FECInterface fec;
	private InetAddress IPAddress;
	private int port;
	
	public UDPPlusTransmitter(InetAddress IPAddress, int port, FECInterface fec) throws SocketException {
		this.socket = new DatagramSocket();
		this.outBuf = new byte[65508];
		this.fec = fec;
		this.IPAddress = IPAddress;
		this.port = port;
	}
	
	public void send(String msg) throws IOException{
		try {
			System.out.println("Original message is: " + msg);
			String encoded = fec.encode(msg);
			System.out.println("Encoded message is: " + encoded);
			outBuf = encoded.getBytes();
			DatagramPacket outPacket = new DatagramPacket(outBuf, outBuf.length, IPAddress, port);
			System.out.println("Now sending out message");
			socket.send(outPacket);
			System.out.println("Message sent");
		} catch (IOException ex) {
			System.out.println("Transmitter failed to send message");
		} finally {
			socket.close();
		}
	}
}