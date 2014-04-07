package forwardErrorCorrection;

import java.util.Arrays;

import forwardErrorCorrectionException.NotAsciiCodeExpcetion;
import forwardErrorCorrectionException.UndecodableException;
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
	
	/**
	 * Constructor
	 * @param expectedECBytes the number of bits that is expected to have an error
	 */
	public ReedSolomonImpl(int expectedECBytes) {
		this.expectedECBytes = 2 * expectedECBytes;
		this.genericGF = DEFAULT_GENERIC_GF;
	}
	
	/**
	 * 
	 * @param expectedECBytes expectedECBytes the number of bits that is expected to have an error
	 * @param genericGF a generic GF for the Reed Solomon algorithm
	 */
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
	public String decode(String encodedText) throws ReedSolomonException, NotAsciiCodeExpcetion, UndecodableException {
		
		ReedSolomonDecoder rsDecoder = new ReedSolomonDecoder(genericGF);
		
		int[] toDecode = parseStringToIntArray(encodedText);

		rsDecoder.decode(toDecode, expectedECBytes);
		
		return convertBytesToString(toDecode);
	}
	
	public static String composeIntArrayString(int[] intArray) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < intArray.length; i++) {
			String tmp = Integer.toBinaryString(intArray[i]);
			for (int j = tmp.length(); j < 8; j++) {
				sb.append("0");
			}
			sb.append(tmp);
		}
		return sb.toString();
	}
	
	public String convertBytesToString(int[] toDecode) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < toDecode.length - expectedECBytes; i++) {
			sb.append((char)toDecode[i]);
		}
		return sb.toString();
	}
	
	public static int[] parseStringToIntArray(String intArrString) throws NotAsciiCodeExpcetion, UndecodableException {
		if (intArrString.length() % 8 != 0) {
			throw new UndecodableException("Encoded message is not correct");
		}
		int noBytes = intArrString.length() / 8;
		String[] codes = new String[noBytes];
		// first split by every 8 chars
		for (int i = 0; i < noBytes; i++) {
			codes[i] = intArrString.substring(8 * i, 8 * i + 8);
		}
		int[] toDecode = new int[codes.length];
		for (int j = 0; j < codes.length; j++) {
			int tmp = Integer.parseInt(codes[j].trim(), 2);
			if (tmp > 255 || tmp <= 0) throw new NotAsciiCodeExpcetion("Cannot decode non-ASCII code");
			toDecode[j] = tmp;
		}
		return toDecode;
	}

}
