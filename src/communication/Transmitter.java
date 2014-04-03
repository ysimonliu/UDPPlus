package communication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import forwardErrorCorrection.FECInterface;

public class Transmitter {
	
	public static final int EC_BYTES = 3;
	private DatagramSocket socket;
	private byte[] outBuf;
	private int port;
	private FECInterface fec;
	
	public Transmitter(int port, FECInterface fec) throws SocketException {
		this.socket = new DatagramSocket();
		this.outBuf = new byte[1024];
		this.port = port;
		this.fec = fec;
	}
	
	public void send(String msg) throws IOException{
		try {
			InetAddress IPAddress = InetAddress.getByName("localhost");
			String encoded = fec.encode(msg);
			System.out.println("Encoded message is: " + encoded);
			outBuf = encoded.getBytes();
			DatagramPacket outPacket = new DatagramPacket(outBuf, outBuf.length, IPAddress, port);
			socket.send(outPacket);
		} catch (IOException ex) {
			System.out.println("Transmitter failed to send message");
		}
	}
}
