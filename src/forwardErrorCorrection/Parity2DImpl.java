package forwardErrorCorrection;

/**
 * 
 * Use 2D parity checksum to encode/decode message
 * The class should detect and correct all 1-bit errors, 
 * detect all 2-bit errors. 
 * We assume all messages are in ascii, and there 
 * are at most 2-bit errors
 * 
 * @author Valentine 
 */
public class Parity2DImpl implements FECInterface {
	
	/**
	 * Use 2D odd parity scheme to encode message. 
	 * ASCII code uses 7 bits; thus, the algorithm left shift
	 * every char and appends a parity bit at its end.
	 * A last row is added to the encoded message for the 
	 * horizontal parity, counting number of 1's bit in every column.
	 * The most significant of the horizontal bit is zero. 
	 */
	@Override
	public String encode(String plainText) {
		
		int[] colCount = new int[7];
		
		StringBuilder sb = new StringBuilder();
		
		for(int i = 0; i < plainText.length(); ++i) {
			
			int tmp = plainText.charAt(i);
			
			int count = 0;
			// ascii's most significant bit is zero
			for(int j = 0; j < 7; ++j) {
				if((tmp & (1 << j)) != 0) {
					++count;
					++colCount[(colCount.length - 1) - j];
				}
			}
			
			tmp = (tmp << 1);
			// if there are even number of bits
			if(count % 2 == 0) {
				tmp = (tmp | (1));
			}
			sb.append((char)tmp);
		}
		
		int last = 0x0; 
		// get vertical parity
		for(int i = 0; i < 7; i++) {
			if(colCount[i] % 2 == 0) {
				last = (last | (1 << (6 - i)));
			}
		}
		
		sb.append((char)last);
		
		return sb.toString();
	}

	@Override
	public String decode(String encodedText) throws UnlocatableErrorException {
		
		StringBuilder sb = new StringBuilder();
		int[] y1BitCount = new int[encodedText.length() - 1]; 
		int[] x1BitCount = new int[7];
		int [] yParityBits = new int[encodedText.length() - 1];
		char[] message = new char[encodedText.length() - 1];
		
		int last = encodedText.charAt(encodedText.length() - 1);
		int[] xParityBits = new int[7];
		
		for(int i = 0; i < xParityBits.length; ++i) {
			xParityBits[i] = (last & (1 << (xParityBits.length - 1) - i)) != 0 ? 1 : 0;				
		}
		
		for(int i = 0; i < encodedText.length() - 1; ++i) {
			int tmp = encodedText.charAt(i);
			message[i] = (char) (tmp >> 1); 
			yParityBits[i] = (tmp & 1) == 1 ? 1 : 0;
			
			for(int j = 0; j < 7; ++j) {
				if((message[i] & (1 << j)) != 0) {
					++y1BitCount[i];
					++x1BitCount[7 - 1 - j];
				}
			}
		}
		
		int numErrorsY = 0;
		for(int i = 0; i < message.length; ++i) {
			boolean even = y1BitCount[i] % 2 == 0; 
			if(even && yParityBits[i] == 0) {
				++numErrorsY;
				
				if(numErrorsY > 1) {
					throw new UnlocatableErrorException("An unlocatable error in the message is detected");
				} else {
					message[i] = forwardErrorCorrect(x1BitCount, xParityBits, message[i]);
				}
			} else if(!even && yParityBits[i] == 1){
				++numErrorsY;
				
				if(numErrorsY > 1) {
					throw new UnlocatableErrorException("An unlocatable error in the message is detected");
				} else {
					message[i] = forwardErrorCorrect(x1BitCount, xParityBits, message[i]);
				}
			} 
			
			sb.append(message[i]);
		}
		
		int numErrorsX = 0;
		for(int i = 0; i < xParityBits.length; ++i) {
			boolean even = x1BitCount[i] % 2 == 0; 
			if(even && xParityBits[i] == 0) {
				++numErrorsX;
				
				if(numErrorsX > 1) {
					throw new UnlocatableErrorException("An unlocatable error in the message is detected");
				}
			} else if(!even && xParityBits[i] == 1){
				++numErrorsX;
				
				if(numErrorsX > 1) {
					throw new UnlocatableErrorException("An unlocatable error in the message is detected");
				}
			} 
		}
		
		
		return sb.toString();
	}
	
	private char forwardErrorCorrect(int[] bitCounts, 
									 int[] parityBits, 
									 char messageBit) {
		for(int j = 0; j < bitCounts.length; ++j) {
			if(bitCounts[j] % 2 == 0) {
				if(parityBits[j] == 0) {
					messageBit = (char)(messageBit ^ (1 << (bitCounts.length - 1) - j));
				}
			} else {
				if(parityBits[j] == 1) {
					messageBit = (char)(messageBit ^ (1 << (bitCounts.length - 1) - j));
				}
			}
		}
		
		return messageBit;
	}
}
