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
			System.out.println(Integer.toBinaryString(tmp));
			System.out.println((char)(tmp));
			
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
	public String decode(String encodedText) {
		
		return null;
	}
}
