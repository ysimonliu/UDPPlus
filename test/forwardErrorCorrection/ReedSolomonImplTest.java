package forwardErrorCorrection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import forwardErrorCorrectionException.NotAsciiCodeExpcetion;
import forwardErrorCorrectionException.UndecodableException;
import reedSolomon.ReedSolomonException;


public class ReedSolomonImplTest {
	
	ReedSolomonImpl rsImpl;
	private static final int EXPECTED_EC_BYTES = 3;
	private static final int MAX_EC_BYTES = 5;
	
	@Before
	public void setUp() {
		int expectedECBytes = EXPECTED_EC_BYTES;
		if (expectedECBytes >= MAX_EC_BYTES) {
			expectedECBytes = MAX_EC_BYTES;
		}
		this.rsImpl = new ReedSolomonImpl(expectedECBytes);
	}
	
	@Test
	public void testEncodeUpperCase() {
		String message = "HELLO";
		String encoded = rsImpl.encode(message);
		String expected = "0100100001000101010011000100110001001111110010010101101101101101111001001111100010000000";
		assertEquals(expected, encoded);
	}
	
	@Test
	public void testEncodeLowerCase() {
		String message = "hello";
		String encoded = rsImpl.encode(message);
		String expected = "0110100001100101011011000110110001101111000101010011110010011101101010111001111011011000";
		assertEquals(expected, encoded);
	}
	
	@Test
	public void testEncodeMixedCase() {
		String message = "HElLo";
		String encoded = rsImpl.encode(message);
		String expected = "0100100001000101011011000100110001101111000111001011100111110000000110110000011001000100";
		assertEquals(expected, encoded);
	}
	
	@Test
	public void testDecodedUpperCaseNoError() throws ReedSolomonException, NotAsciiCodeExpcetion, UndecodableException {
		String encoded = "0100100001000101010011000100110001001111110010010101101101101101111001001111100010000000";
		String message = rsImpl.decode(encoded);
		assertEquals(message, "HELLO");
	}
	
	@Test
	public void testDecodedLowerCaseNoError() throws ReedSolomonException, NotAsciiCodeExpcetion, UndecodableException {
		String encoded = "0110100001100101011011000110110001101111000101010011110010011101101010111001111011011000";
		String message = rsImpl.decode(encoded);
		assertEquals(message, "hello");
	}
	
	@Test
	public void testDecodedMixedCaseNoError() throws ReedSolomonException, NotAsciiCodeExpcetion, UndecodableException {
		String encoded = "0100100001000101011011000100110001101111000111001011100111110000000110110000011001000100";
		String message = rsImpl.decode(encoded);
		assertEquals(message, "HElLo");
	}
	
	@Test
	public void testEncodeDecodeUpperCaseNoError() throws ReedSolomonException, NotAsciiCodeExpcetion, UndecodableException {
		String message = "HELLO";
		assertEquals(message, rsImpl.decode(rsImpl.encode(message)));
	}
	
	@Test
	public void testEncodeDecodeLowerCaseNoError() throws ReedSolomonException, NotAsciiCodeExpcetion, UndecodableException {
		String message = "hello";
		assertEquals(message, rsImpl.decode(rsImpl.encode(message)));
	}
	
	@Test
	public void testEncodeDecodeMixedCaseNoError() throws ReedSolomonException, NotAsciiCodeExpcetion, UndecodableException {
		String message = "HeLlO wOrLd";
		assertEquals(message, rsImpl.decode(rsImpl.encode(message)));
	}
	
	@Test
	public void testDecodeUpperCaseECBitError() throws ReedSolomonException, NotAsciiCodeExpcetion, UndecodableException {
		String correctIntArrayString = "0100100001000101010011000100110001001111110010010101101101101101111001001111100010000000";
		for (int i = 1; i <= EXPECTED_EC_BYTES; i++) {
			String erroneousString = messUpBits(correctIntArrayString, i);
			String decoded = rsImpl.decode(erroneousString);
			assertEquals("HELLO", decoded);
		}
	}
	
	@Test
	public void testDecodeLowerCaseECBitError() throws ReedSolomonException, NotAsciiCodeExpcetion, UndecodableException {
		String correctIntArrayString = "0110100001100101011011000110110001101111000101010011110010011101101010111001111011011000";
		for (int i = 1; i <= EXPECTED_EC_BYTES; i++) {
			String erroneousString = messUpBits(correctIntArrayString, i);
			String decoded = rsImpl.decode(erroneousString);
			assertEquals("hello", decoded);
		}
	}

	@Test
	public void testDecodeMixedCaseECBitError() throws ReedSolomonException, NotAsciiCodeExpcetion, UndecodableException {
		String correctIntArrayString = "0100100001000101011011000100110001101111000111001011100111110000000110110000011001000100";
		for (int i = 1; i <= EXPECTED_EC_BYTES; i++) {
			String erroneousString = messUpBits(correctIntArrayString, i);
			String decoded = rsImpl.decode(erroneousString);
			assertEquals("HElLo", decoded);
		}
	}
	
	// below are helper functions
	private String messUpBits(String correctIntArrayString, int noBitsToMessUp) {
		StringBuilder sb = new StringBuilder(correctIntArrayString);
		int length = correctIntArrayString.length();
		for (int i = 0; i < noBitsToMessUp; i++) {
			int pos = generateRandomNumber(length - 1);
			sb.deleteCharAt(pos);
			if (sb.charAt(pos) == '0') {
				sb.insert(pos, '1');
			} else {
				sb.insert(pos, '0');
			}
		}
		return sb.toString();
	}
	
	private int generateRandomNumber(int range) {
		Random r = new Random();
		int randomAsciiCode = r.nextInt(range);
		return randomAsciiCode;
	}
}
