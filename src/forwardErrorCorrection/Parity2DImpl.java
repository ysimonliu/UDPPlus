package forwardErrorCorrection;

/**
 * 
 * Use 2D parity checksum to encode/decode message
 * The class should detect and correct all 1-bit errors, 
 * detect all 2-bit and 3-bit errors. 
 * We assume all messages are in ascii
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


	// TODO(mingju): need to detect and correct errors
	//				 and need to throw an exception when errors
	//				 are detected but cannot be corrected.
	@Override
	public String decode(String encodedText) {
		
		StringBuilder sb = new StringBuilder();
		int last = encodedText.charAt(encodedText.length() - 1);
		int[] xParityBits = new int[7];
		
		for(int i = 0; i < xParityBits.length; ++i) {
			if((last & (1 << i)) == 1) {
				xParityBits[xParityBits.length - 1 - i] = 1;				
			} else {
				xParityBits[xParityBits.length - 1 - i] = 0;
			}
		}
		
		for(int i = 0; i < encodedText.length() - 1; ++i) {
			
			int tmp = encodedText.charAt(i);
			char m = (char) (tmp >> 1); 
			int parity = (tmp & 1) == 1 ? 1 : 0;
			
			int count = 0;
			for(int j = 0; j < 7; ++j) {
				if((tmp & (1 << j)) != 0) {
					++count;
				}
			}
			
			if(count % 2 == 0) {
				if(parity == 0) {
					// TODO(mingju): there is an error, do FEC
				}
			} else {
				if(parity == 1) {
					// TODO(mingju): there is an error, do FEC
				}
			}
			
			sb.append(m);
		}
		
		return sb.toString();
	}
}
