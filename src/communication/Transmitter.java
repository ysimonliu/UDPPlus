package communication;

import java.net.DatagramSocket;

import com.google.inject.Inject;

import forwardErrorCorrection.FECInterface;

public class Transmitter {
	private DatagramSocket socket;
	private FECInterface fec;

	@Inject
	public Transmitter(DatagramSocket socket, FECInterface fec) {
		this.socket = socket;
		this.fec = fec;
	}
	
		
}
