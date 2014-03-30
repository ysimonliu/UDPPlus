package communication;

import java.net.DatagramSocket;

import com.google.inject.Inject;

import forwardErrorCorrection.FECInterface;

public class Receiver {

	private DatagramSocket socket;
	private FECInterface fec;

	@Inject
	public Receiver(DatagramSocket socket, FECInterface fec) {
		this.socket = socket;
		this.fec = fec;
	}
}
