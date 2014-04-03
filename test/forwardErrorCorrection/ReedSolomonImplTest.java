package forwardErrorCorrection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import forwardErrorCorrectionException.NotAsciiCodeExpcetion;

import reedSolomon.ReedSolomonException;


public class ReedSolomonImplTest {
	
	ReedSolomonImpl rsImpl;
	private static final int EXPECTED_EC_BYTES = 3;
	
	@Before
	public void setUp() {
		int expectedECBytes = EXPECTED_EC_BYTES;
		if (expectedECBytes >= 10) {
			expectedECBytes = 10;
		}
		this.rsImpl = new ReedSolomonImpl(expectedECBytes);
	}
	
	@Test
	public void testEncodeUpperCase() {
		String message = "HELLO";
		String encoded = rsImpl.encode(message);
		String expected = "72, 69, 76, 76, 79, 168, 254, 255";
		assertEquals(expected, encoded);
	}
	
	@Test
	public void testEncodeLowerCase() {
		String message = "hello";
		String encoded = rsImpl.encode(message);
		String expected = "104, 101, 108, 108, 111, 98, 199, 222";
		assertEquals(expected, encoded);
	}
	
	@Test
	public void testEncodeMixedCase() {
		String message = "HElLo";
		String encoded = rsImpl.encode(message);
		String expected = "72, 69, 108, 76, 111, 121, 173, 243";
		assertEquals(expected, encoded);
	}
	
	@Test
	public void testDecodedUpperCaseNoError() throws ReedSolomonException, NotAsciiCodeExpcetion {
		String encoded = "72, 69, 76, 76, 79, 168, 254, 255";
		String message = rsImpl.decode(encoded);
		assertEquals(message, "HELLO");
	}
	
	@Test
	public void testDecodedLowerCaseNoError() throws ReedSolomonException, NotAsciiCodeExpcetion {
		String encoded = "104, 101, 108, 108, 111, 98, 199, 222";
		String message = rsImpl.decode(encoded);
		assertEquals(message, "hello");
	}
	
	@Test
	public void testDecodedMixedCaseNoError() throws ReedSolomonException, NotAsciiCodeExpcetion {
		String encoded = "72, 69, 108, 76, 111, 121, 173, 243";
		String message = rsImpl.decode(encoded);
		assertEquals(message, "HElLo");
	}
	
	@Test
	public void testEncodeDecodeUpperCaseNoError() throws ReedSolomonException, NotAsciiCodeExpcetion {
		String message = "HELLO";
		assertEquals(message, rsImpl.decode(rsImpl.encode(message)));
	}
	
	@Test
	public void testEncodeDecodeLowerCaseNoError() throws ReedSolomonException, NotAsciiCodeExpcetion {
		String message = "hello";
		assertEquals(message, rsImpl.decode(rsImpl.encode(message)));
	}
	
	@Test
	public void testEncodeDecodeMixedCaseNoError() throws ReedSolomonException, NotAsciiCodeExpcetion {
		String message = "HeLlO wOrLd";
		assertEquals(message, rsImpl.decode(rsImpl.encode(message)));
	}
	
	@Test
	public void testDecodeUpperCaseECBitDataError() throws ReedSolomonException, NotAsciiCodeExpcetion {
		String correctIntArrayString = "72, 69, 76, 76, 79, 168, 254, 255";
		for (int i = 1; i <= EXPECTED_EC_BYTES / 2; i++) {
			int[] encodedIntArrayWithErrors = messUpDataBits(ReedSolomonImpl.parseStringToIntArray(correctIntArrayString), i);
			String encodedStringWithErrors = ReedSolomonImpl.composeIntArrayString(encodedIntArrayWithErrors);
			System.out.println(encodedStringWithErrors);
			String decoded = rsImpl.decode(encodedStringWithErrors);
			assertEquals("HELLO", decoded);
		}
	}

	@Test
	public void testDecodeUpperCaseECBitParityError() throws ReedSolomonException, NotAsciiCodeExpcetion {
		String encoded = "72, 69, 76, 76, 79, 168, 254, 1";
		String message = rsImpl.decode(encoded);
		assertEquals(message, "HELLO");
	}
	
	@Test
	public void testDecodeLowerCaseECBitDataError() throws ReedSolomonException, NotAsciiCodeExpcetion {
		String encoded = "104, 12, 108, 108, 111, 98, 199, 222";
		String message = rsImpl.decode(encoded);
		assertEquals(message, "hello");
	}
	
	@Test
	public void testDecodeLowerCaseECBitParityError() throws ReedSolomonException, NotAsciiCodeExpcetion {
		String encoded = "104, 101, 108, 108, 111, 98, 199, 245";
		String message = rsImpl.decode(encoded);
		assertEquals(message, "hello");
	}
	@Test
	public void testDecodeMixedCaseECBitDataError() throws ReedSolomonException, NotAsciiCodeExpcetion {
		String encoded = "72, 33, 108, 76, 111, 121, 173, 243";
		String message = rsImpl.decode(encoded);
		assertEquals(message, "HElLo");
	}
	
	@Test
	public void testDecodeMixedCaseECBitParityError() throws ReedSolomonException, NotAsciiCodeExpcetion {
		String encoded = "72, 69, 108, 76, 111, 121, 133, 243";
		String message = rsImpl.decode(encoded);
		assertEquals(message, "HElLo");
	}
	
	// below are helper functions
	private int[] messUpDataBits(int[] originalIntArray, int noBitsToMessUp) {
		int[] modifiedArray = originalIntArray;
		for (int i = 0; i < noBitsToMessUp; i++) {
			modifiedArray[i] = generateDiffAsciiCode(originalIntArray[i]);
		}
		return modifiedArray;
	}
	
	private int[] messUpParityBits(int[] originalIntArray, int noBitsToMessUp) {
		int[] modifiedArray = originalIntArray;
		int length = originalIntArray.length;
		for (int i = 0; i < noBitsToMessUp; i++) {
			modifiedArray[length - i] = generateDiffAsciiCode(originalIntArray[length - i]);
		}
		return modifiedArray;
	}
	
	private int generateDiffAsciiCode(int original) {
		Random r = new Random();
		int randomAsciiCode = r.nextInt(255);
		while (randomAsciiCode == original) {
			randomAsciiCode = r.nextInt(255);
		}
		return randomAsciiCode;
	}
}
