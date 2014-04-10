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
	
	private int expectedECBits;
	private GenericGF genericGF;
	
	/**
	 * Constructe an instance of ReedSolomonImpl with only the expected number of error bytes
	 * @param expectedECBits the number of bits that is expected to have an error
	 */
	public ReedSolomonImpl(int expectedECBits) {
		this.expectedECBits = 2 * expectedECBits;
		this.genericGF = DEFAULT_GENERIC_GF;
	}
	
	/**
	 * 
	 * @param expectedECBits the number of bits that is expected to have an error
	 * @param genericGF - the specific Galois Field used for the Reed Solomon algorithm
	 */
	public ReedSolomonImpl(int expectedECBits, GenericGF genericGF) {
		this.expectedECBits = expectedECBits;
		this.genericGF = genericGF;
	}
	
	/**
	 * Decodes encoded text into plain text using Reed-Solomon algorithm
	 */
	@Override
	public String encode(String plainText) {

		ReedSolomonEncoder rsEncoder = new ReedSolomonEncoder(genericGF);
		
		int [] toEncode = new int[plainText.length() + expectedECBits];
		
		int i = 0;
		// fill the data bits
		for (i = 0; i < plainText.length(); i++) {
			toEncode[i] = (int) plainText.charAt(i);
		}
		// fill the rest with 0s
		for (int j = 0; j < expectedECBits; j++) {
			toEncode[i + j] = 0x0;
		}
		
		rsEncoder.encode(toEncode, expectedECBits);
		
		return convertToBinaryCodeString(toEncode);
	}

	/**
	 * Encodes plain text into encoded text using Reed-Solomon algorithm
	 */
	@Override
	public String decode(String encodedText) throws ReedSolomonException, NotAsciiCodeExpcetion, UndecodableException {
		
		ReedSolomonDecoder rsDecoder = new ReedSolomonDecoder(genericGF);
		int[] toDecode = parseStringToIntArray(encodedText);

		rsDecoder.decode(toDecode, expectedECBits);		
		return convertBytesToString(toDecode);
	}
	
	/**
	 * This helper method converts an array of decimal integers to binary ones,
	 * concatenate all, and return as a string
	 * @param intArray an array of decimal integers
	 * @return
	 */
	public static String convertToBinaryCodeString(int[] intArray) {
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
	
	/**
	 * This helper method converts ASCII code into corresponding chars,
	 * concatenate all chars into a string and returns the string
	 * @param toDecode an array of ASCII codes
	 * @return
	 */
	public String convertBytesToString(int[] toDecode) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < toDecode.length - expectedECBits; i++) {
			sb.append((char)toDecode[i]);
		}
		return sb.toString();
	}
	
	/**
	 * This helper method parses a string of binary numbers into a list of integers,
	 * where each 8 bits in the array are treated as binary code, so converted to the corresponding decimal number
	 * @param intArrString the integer array consisting binary numbers
	 * @return parsed corresponding ASCII codes
	 * @throws NotAsciiCodeExpcetion {@link NotAsciiCodeExpcetion}}
	 * @throws UndecodableException {@link UndecodableException}}
	 */
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
