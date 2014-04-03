package demo;

import java.util.Scanner;

import forwardErrorCorrection.Parity2DImpl;
import forwardErrorCorrection.UnlocatableErrorException;

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
			
			System.out.println("This is your message: ");
			printMessageInBits(message);
			
			
			System.out.println("How many faulty bit do you want to inject?: ");
			int numFaults = scanner.nextInt();
			scanner.nextLine();
			
			// my code was not modular in the first place =.=
			// now if I wanna inject fault, I have to modify the encoded message directly
			// fml. Bad design! So bad! OMG! I cannot even remember how I encode =.=
			if(algorithm == 1) {
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
					 * leftmost bit as parity bit. Ok this is a very bad design. 
					 * Sorry. 
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
				
			} else { // TODO(mingju): Reed-Solomon
				
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

}
