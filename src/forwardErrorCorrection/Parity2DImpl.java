package forwardErrorCorrection;

import forwardErrorCorrectionException.UnlocatableErrorException;

/**
 * 
 * Use 2D parity checksum to encode/decode message
 * The class should detect and correct all 1-bit errors, 
 * detect all 2-bit errors. 
 * We assume all messages are in ascii, and there 
 * are at most 2-bit errors. Note that 2D parity can 
 * detect odd number of bit errors. However, we gave up
 * this feature in order to implement data recovery, 
 * i.e: forward error correction 
 * 
 * @author Valentine 
 */
public class Parity2DImpl implements FECInterface {
	
	/**
	 * Use 2D odd parity scheme to encode message. 
	 * ASCII code uses 7 bits; thus, the algorithm left shift 1 bit
	 * every char and appends a parity bit at its end.
	 * A last row is added to the encoded message for the 
	 * column parity bits, counting number of 1's bit in every column.
	 * The most significant of the column bit is zero.
	 * We did not add a parity bit to check all parity bits. 
	 * Hence, we assume parity bits do not get corrupted during 
	 * transmission.  
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

	/**
	 * Use 2D parity decoding scheme to decode message. 
	 * The method keeps count of the number of 1's bits for
	 * each row and column, and check against parity bits to
	 * verify if there is any error. If there is an 1-bit error,
	 * the method corrects the error. If there are more than 1,
	 * the method throws an UnlocatableErrorException.
	 *
	 */
	@Override
	public String decode(String encodedText) throws UnlocatableErrorException {
		
		StringBuilder message = new StringBuilder();
		int[] y1BitCount = new int[encodedText.length() - 1]; 
		int[] x1BitCount = new int[7];
		int [] yParityBits = new int[encodedText.length() - 1];
		int[] xParityBits = new int[7];
		int last = encodedText.charAt(encodedText.length() - 1);
		
		for(int i = 0; i < xParityBits.length; ++i) {
			xParityBits[i] = (last & (1 << (xParityBits.length - 1) - i)) != 0 ? 1 : 0;				
		}
		
		for(int i = 0; i < encodedText.length() - 1; ++i) {
			int tmp = encodedText.charAt(i);
			message.append((char) (tmp >> 1)); 
			yParityBits[i] = (tmp & 1) == 1 ? 1 : 0;
			
			for(int j = 0; j < 7; ++j) {
				if((message.charAt(i) & (1 << j)) != 0) {
					++y1BitCount[i];
					++x1BitCount[7 - 1 - j];
				}
			}
		}
		
		int yErrorBit = getErrorPosition(y1BitCount, yParityBits);		
		int xErrorBit = getErrorPosition(x1BitCount, xParityBits);
		
		// error correction
		if(yErrorBit >= 0 && xErrorBit >= 0) {
			char character = (char)(message.charAt(yErrorBit) ^ 
								(1 << (x1BitCount.length - 1) - xErrorBit));
			message.replace(yErrorBit, yErrorBit+1, String.valueOf(character));
		}
		
		return message.toString();
	}
	
	
	private int getErrorPosition(int[] bitCounts, int[] parityBits) throws UnlocatableErrorException {
		
		int position = -1;
		int errorCount = 0;
		for(int j = 0; j < bitCounts.length; ++j) {
			boolean even = bitCounts[j] % 2 == 0; 
			if(even && parityBits[j] == 0) {
				++errorCount;
				
				if(errorCount > 1) {
					throw new UnlocatableErrorException("An unlocatable error in the message is detected");
				} else {
					position = j;
				}
			} else if(!even && parityBits[j] == 1){
				++errorCount;
				
				if(errorCount > 1) {
					throw new UnlocatableErrorException("An unlocatable error in the message is detected");
				} else {
					position = j;
				}
			}
		}
		
		return position;
	}
}
