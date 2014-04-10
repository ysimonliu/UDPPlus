package demo;

import java.util.Scanner;

import forwardErrorCorrection.Parity2DImpl;
import forwardErrorCorrection.ReedSolomonImpl;
import forwardErrorCorrectionException.UnlocatableErrorException;

public class Main {
	
	public static void main(String args[]) {
		
		Scanner scanner = new Scanner(System.in);
		Parity2DImpl parity2D = new Parity2DImpl();
		
		while(true) {
			System.out.println("Please enter a message or enter Q to quit: ");
			String message = scanner.nextLine();
			if(message.equalsIgnoreCase("Q")) {
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
			
			if(algorithm == 1) {
				System.out.println("This is your message: ");
				printMessageInBits(message);
				String encodedMessage = parity2D.encode(message);
				StringBuilder faultyMessage = new StringBuilder(message);
				StringBuilder faultyEncodedMessage = new StringBuilder(encodedMessage);
				
				int count = 1;
				while(numFaults > 0) {
					System.out.println("Fault #" + count++);
					System.out.println("Which character: ");
					int charPos = scanner.nextInt();
					scanner.nextLine();
					System.out.println("Which bit position (leftmost bit is zero): ");
					int bitPos = scanner.nextInt();
					scanner.nextLine();
					
					char tmp = message.charAt(charPos - 1);
					tmp = (char) (tmp ^ (1 << bitPos));
					faultyMessage.deleteCharAt(charPos - 1);
					faultyMessage.insert(charPos - 1, tmp);
					
					/* TODO(mingju): bitPost += 1 b/c I assume Ascii is 7-bits 
					 * and I right shift the message by 1 but and use the 
					 * leftmost bit as parity bit.
					 */
					bitPos += 1;
					tmp = encodedMessage.charAt(charPos - 1);
					tmp = (char) (tmp ^ (1 << bitPos));
					faultyEncodedMessage.deleteCharAt(charPos - 1);
					faultyEncodedMessage.insert(charPos - 1, tmp);
					
					numFaults--;
				}
				
				try {
					String decodedMessage = parity2D.decode(faultyEncodedMessage.toString());
					System.out.println("Message successfully decoded");
					System.out.println("You entered " + message);
					System.out.println("The receiver received " + faultyMessage);
					System.out.println("The receiver corrected the error and got " + decodedMessage);
					
				} catch (UnlocatableErrorException e) {
					System.out.println("2D Parity can only correct at most 1-bit error.");
					System.out.println(">= 2-bit errors are detected. Need retransmission.");
				}
				
			} else {
				if (numFaults > 8) {
					System.out.println("ERROR: Your expected number of errors exceeded the length of a byte, so change your input to 8");
					numFaults = 8;
				}
				
				ReedSolomonImpl rsImpl = new ReedSolomonImpl(numFaults);
				
				String encodedMessage = rsImpl.encode(message);
				StringBuilder faultyEncodedMessage = new StringBuilder(encodedMessage);
				
				System.out.println("This is your message: ");
				printMessageInRSBits(message, encodedMessage, numFaults);
				
				int count = 1;
				while(numFaults > 0) {
					System.out.println("Fault #" + count++);
					System.out.println("Which character: ");
					int charPos = scanner.nextInt();
					scanner.nextLine();
					System.out.println("Which bit position (leftmost bit is zero): ");
					int bitPos = scanner.nextInt();
					scanner.nextLine();
					
					int errorPos = (charPos - 1) * 8 + bitPos;
					char tmp = encodedMessage.charAt(errorPos);
					faultyEncodedMessage.deleteCharAt(errorPos);
					if (tmp == '1') {
						faultyEncodedMessage.insert(errorPos, '0');
					} else {
						faultyEncodedMessage.insert(errorPos, '1');
					}
					
					numFaults--;
				}
				
				try {
					String decodedMessage = rsImpl.decode(faultyEncodedMessage.toString());
					System.out.println("Message successfully decoded");
					System.out.println("You entered " + message);
					System.out.println("The receiver received " + rsImpl.convertBytesToString(ReedSolomonImpl.parseStringToIntArray(faultyEncodedMessage.toString())));
					System.out.println("The receiver corrected the error and got " + decodedMessage);
					
				} catch (Exception e) {
					System.out.println("Reed-Solomon cannot decode the given string, sorry.");
				}
				
			}
			
		}
		
		System.out.println("END");
	}
	
	private static void printMessageInBits(String message) {
		
		for(int i = 0; i < message.length(); ++i) {
			char c = message.charAt(i);
			
			System.out.println("character " + (i + 1) + ": " +  c 
					+ " = " + Integer.toBinaryString(c));
		}
		
	}
	
	private static void printMessageInRSBits(String message, String encodedMessage, int ecBytes) {
		
		for(int i = 0; i < message.length() + 2*ecBytes; ++i) {
			if (i < message.length()) {
				char c = message.charAt(i);
				System.out.println("Byte " + (i + 1) + ": " +  c 
						+ " = " + encodedMessage.substring(i * 8, i * 8 + 8));
			} else {
				System.out.println("Byte " + (i + 1) + ": "
					+ "parity " + (i + 1 - message.length()) + " = " + encodedMessage.substring(i * 8, i * 8 + 8));
			}
		}
		
	}

}
