package forwardErrorCorrection;

import reedSolomon.GenericGF;
import reedSolomon.ReedSolomonEncoder;

/**
 * 
 * Use Reed-Solomon to encode/decode message
 * We assume all messages are in ascii
 * 
 * @author Valentine 
 */
public class ReedSolomonImpl implements FECInterface {

	private final int PRIVATE_EC_BITS = 3;
	
	@Override
	public String encode(String plainText) {
		// FIXME: needs to study the constructor of Generic GF
		GenericGF field = new GenericGF(5, 3, 2);
		ReedSolomonEncoder rsEncoder = new ReedSolomonEncoder(field);
		
		char[] toEncodeChars = plainText.toCharArray();
		int [] toEncode = new int[toEncodeChars.length + PRIVATE_EC_BITS];
		int i = 0;
		for (i = 0; i < toEncodeChars.length; i++) {
			toEncode[i] = Character.getNumericValue(toEncodeChars[i]);
		}
		for (int j = 0; j < PRIVATE_EC_BITS; j++) {
			toEncode[i + j] = 0;
		}
		rsEncoder.encode(toEncode, PRIVATE_EC_BITS);
		
		return toEncode.toString();
	}

	@Override
	public String decode(String encodedText) throws UnlocatableErrorException {
		// TODO Auto-generated method stub
		return null;
	}

}
