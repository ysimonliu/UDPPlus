package forwardErrorCorrection;

import java.util.Arrays;

import forwardErrorCorrectionException.NotAsciiCodeExpcetion;

import reedSolomon.GenericGF;
import reedSolomon.ReedSolomonDecoder;
import reedSolomon.ReedSolomonEncoder;
import reedSolomon.ReedSolomonException;

/**
 * 
 * Use Reed-Solomon to encode/decode message
 * We assume all messages are in ascii
 * 
 * @author Simon 
 */
public class ReedSolomonImpl implements FECInterface {

	private final GenericGF DEFAULT_GENERIC_GF = GenericGF.DATA_MATRIX_FIELD_256;
	
	private int expectedECBytes;
	private GenericGF genericGF;
	
	public ReedSolomonImpl(int expectedECBytes) {
		this.expectedECBytes = expectedECBytes;
		this.genericGF = DEFAULT_GENERIC_GF;
	}
	
	public ReedSolomonImpl(int expectedECBytes, GenericGF genericGF) {
		this.expectedECBytes = expectedECBytes;
		this.genericGF = genericGF;
	}
	
	@Override
	public String encode(String plainText) {

		ReedSolomonEncoder rsEncoder = new ReedSolomonEncoder(genericGF);
		
		int [] toEncode = new int[plainText.length() + expectedECBytes];
		int i = 0;
		// fill the data bits
		for (i = 0; i < plainText.length(); i++) {
			toEncode[i] = (int) plainText.charAt(i);
		}
		// fill the rest with 0s
		for (int j = 0; j < expectedECBytes; j++) {
			toEncode[i + j] = 0x0;
		}
		
		rsEncoder.encode(toEncode, expectedECBytes);
		
		return composeIntArrayString(toEncode);
	}

	@Override
	public String decode(String encodedText) throws ReedSolomonException, NotAsciiCodeExpcetion {
		
		ReedSolomonDecoder rsDecoder = new ReedSolomonDecoder(genericGF);
		
		int[] toDecode = parseStringToIntArray(encodedText);

		rsDecoder.decode(toDecode, expectedECBytes);
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < toDecode.length - expectedECBytes; i++) {
			sb.append((char)toDecode[i]);
		}
		return sb.toString();
	}
	
	public static String composeIntArrayString(int[] intArray) {
		String result = Arrays.toString(intArray);
		return result.substring(1, result.length()-1);
	}
	
	public static int[] parseStringToIntArray(String intArrString) throws NotAsciiCodeExpcetion {
		String[] codes = intArrString.split(",");
		int[] toDecode = new int[codes.length];
		for (int i = 0; i < codes.length; i++) {
			int tmp = Integer.parseInt(codes[i].trim());
			if (tmp > 255 || tmp <= 0) throw new NotAsciiCodeExpcetion("Cannot decode non-ASCII code");
			toDecode[i] = tmp;
		}
		return toDecode;
	}

}
