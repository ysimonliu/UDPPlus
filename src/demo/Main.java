package demo;

import java.util.Scanner;

import reedSolomon.ReedSolomonException;

import forwardErrorCorrection.FECInterface;
import forwardErrorCorrection.Parity2DImpl;
import forwardErrorCorrection.ReedSolomonImpl;
import forwardErrorCorrectionException.NotAsciiCodeExpcetion;
import forwardErrorCorrectionException.UndecodableException;
import forwardErrorCorrectionException.UnlocatableErrorException;

public class Main {

	public static void main(String args[]) {

		Scanner scanner = new Scanner(System.in);

		FECInterface fecAlgorithm;

		while (true) {
			System.out.println("Please enter a message or enter Q to quit: ");
			String message = scanner.nextLine();
			if (message.equalsIgnoreCase("Q")) {
				break;
			}

			System.out.println("From the following choices");
			System.out.println("1. 2-Dimensional Parity");
			System.out.println("2. Reed-Solomon");
			System.out.println("Please pick a encode/decode mechanism: ");

			int algorithm = scanner.nextInt();
			scanner.nextLine();

			System.out.println("How many faulty bit do you want to inject?: ");
			int numFaults = scanner.nextInt();
			scanner.nextLine();

			String encodedMessage;
			System.out.println("This is your message: ");
			if (algorithm == 1) {
				fecAlgorithm = new Parity2DImpl();
				printMessageInBits(message);
				encodedMessage = fecAlgorithm.encode(message);
			} else {
				fecAlgorithm = new ReedSolomonImpl(numFaults);
				encodedMessage = fecAlgorithm.encode(message);
				printMessageInRSBits(message, encodedMessage, numFaults);
			}

			StringBuilder faultyMessage = new StringBuilder(message);
			StringBuilder faultyEncodedMessage = new StringBuilder(
					encodedMessage);

			int count = 1;
			while (numFaults > 0) {
				System.out.println("Fault #" + count++);
				System.out.println("Which character: ");
				int charPos = scanner.nextInt();
				scanner.nextLine();
				System.out
						.println("Which bit position (leftmost bit is zero): ");
				int bitPos = scanner.nextInt();
				scanner.nextLine();

				faultyMessage = injectFault(message, charPos - 1, bitPos);
				if (algorithm == 1) {
					faultyEncodedMessage = injectFault(encodedMessage,
							charPos - 1, bitPos - 1);
				} else {
					int errorPos = (charPos - 1) * 8 + bitPos;
					char tmp = encodedMessage.charAt(errorPos);
					faultyEncodedMessage.deleteCharAt(errorPos);
					if (tmp == '1') {
						faultyEncodedMessage.insert(errorPos, '0');
					} else {
						faultyEncodedMessage.insert(errorPos, '1');
					}

				}

				numFaults--;
			}

			try {
				String decodedMessage = fecAlgorithm
						.decode(faultyEncodedMessage.toString());
				System.out.println("Message successfully decoded");
				System.out.println("You entered " + message);
				if (algorithm == 1) {
					System.out
							.println("The receiver received " + faultyMessage);
				} else {
					System.out
							.println("The receiver received "
									+ ((ReedSolomonImpl) fecAlgorithm)
											.convertBytesToString(ReedSolomonImpl
													.parseStringToIntArray(faultyEncodedMessage
															.toString())));
				}
				System.out.println("The receiver corrected the error and got "
						+ decodedMessage);
			} catch (UnlocatableErrorException e) {
				System.out
						.println("2D Parity can only correct at most 1-bit error.");
				System.out
						.println("More than 2-bit errors are detected. Need retransmission.");
			} catch (NotAsciiCodeExpcetion | ReedSolomonException | UndecodableException e) {
				System.out.println("Reed-Solomon cannot decode the given string. Need retransmission.");
			} 
		}

		System.out.println("END");
	}

	private static void printMessageInBits(String message) {

		for (int i = 0; i < message.length(); ++i) {
			char c = message.charAt(i);

			System.out.println("character " + (i + 1) + ": " + c + " = "
					+ Integer.toBinaryString(c));
		}
	}

	private static StringBuilder injectFault(String message, int charPos,
			int bitPos) {
		StringBuilder faultyMessage = new StringBuilder(message);
		char tmp = message.charAt(charPos);
		tmp = (char) (tmp ^ (1 << bitPos));
		faultyMessage.deleteCharAt(charPos);
		faultyMessage.insert(charPos, tmp);
		return faultyMessage;
	}

	private static void printMessageInRSBits(String message,
			String encodedMessage, int ecBytes) {
		for (int i = 0; i < message.length() + 2 * ecBytes; ++i) {
			if (i < message.length()) {
				char c = message.charAt(i);
				System.out.println("Byte " + (i + 1) + ": " + c + " = "
						+ encodedMessage.substring(i * 8, i * 8 + 8));
			} else {
				System.out.println("Byte " + (i + 1) + ": " + "parity "
						+ (i + 1 - message.length()) + " = "
						+ encodedMessage.substring(i * 8, i * 8 + 8));
			}
		}

	}
}
