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
		System.out.println(encoded);
		String expected = "72, 69, 76, 76, 79, 201, 91, 109, 228, 248, 128";
		assertEquals(expected, encoded);
	}
	
	@Test
	public void testEncodeLowerCase() {
		String message = "hello";
		String encoded = rsImpl.encode(message);
		System.out.println(encoded);
		String expected = "104, 101, 108, 108, 111, 21, 60, 157, 171, 158, 216";
		assertEquals(expected, encoded);
	}
	
	@Test
	public void testEncodeMixedCase() {
		String message = "HElLo";
		String encoded = rsImpl.encode(message);
		System.out.println(encoded);
		String expected = "72, 69, 108, 76, 111, 28, 185, 240, 27, 6, 68";
		assertEquals(expected, encoded);
	}
	
	@Test
	public void testDecodedUpperCaseNoError() throws ReedSolomonException, NotAsciiCodeExpcetion {
		String encoded = "72, 69, 76, 76, 79, 201, 91, 109, 228, 248, 128";
		String message = rsImpl.decode(encoded);
		assertEquals(message, "HELLO");
	}
	
	@Test
	public void testDecodedLowerCaseNoError() throws ReedSolomonException, NotAsciiCodeExpcetion {
		String encoded = "104, 101, 108, 108, 111, 21, 60, 157, 171, 158, 216";
		String message = rsImpl.decode(encoded);
		assertEquals(message, "hello");
	}
	
	@Test
	public void testDecodedMixedCaseNoError() throws ReedSolomonException, NotAsciiCodeExpcetion {
		String encoded = "72, 69, 108, 76, 111, 28, 185, 240, 27, 6, 68";
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
		String correctIntArrayString = "72, 69, 76, 76, 79, 201, 91, 109, 228, 248, 128";
		for (int i = 1; i <= EXPECTED_EC_BYTES / 2; i++) {
			int[] encodedIntArrayWithErrors = messUpDataBits(ReedSolomonImpl.parseStringToIntArray(correctIntArrayString), i);
			String encodedStringWithErrors = ReedSolomonImpl.composeIntArrayString(encodedIntArrayWithErrors);
			String decoded = rsImpl.decode(encodedStringWithErrors);
			assertEquals("HELLO", decoded);
		}
	}
	
	@Test
	public void testDecodeUpperCaseECBitParityError() throws ReedSolomonException, NotAsciiCodeExpcetion {
		String correctIntArrayString = "72, 69, 76, 76, 79, 201, 91, 109, 228, 248, 128";
		for (int i = 1; i <= EXPECTED_EC_BYTES / 2; i++) {
			int[] encodedIntArrayWithErrors = messUpParityBits(ReedSolomonImpl.parseStringToIntArray(correctIntArrayString), i);
			String encodedStringWithErrors = ReedSolomonImpl.composeIntArrayString(encodedIntArrayWithErrors);
			String decoded = rsImpl.decode(encodedStringWithErrors);
			assertEquals("HELLO", decoded);
		}
	}

	@Test
	public void testDecodeLowerCaseECBitDataError() throws ReedSolomonException, NotAsciiCodeExpcetion {
		String correctIntArrayString = "104, 101, 108, 108, 111, 21, 60, 157, 171, 158, 216";
		for (int i = 1; i <= EXPECTED_EC_BYTES / 2; i++) {
			int[] encodedIntArrayWithErrors = messUpDataBits(ReedSolomonImpl.parseStringToIntArray(correctIntArrayString), i);
			String encodedStringWithErrors = ReedSolomonImpl.composeIntArrayString(encodedIntArrayWithErrors);
			String decoded = rsImpl.decode(encodedStringWithErrors);
			assertEquals("hello", decoded);
		}
	}
	
	@Test
	public void testDecodeLowerCaseECBitParityError() throws ReedSolomonException, NotAsciiCodeExpcetion {
		String correctIntArrayString = "104, 101, 108, 108, 111, 21, 60, 157, 171, 158, 216";
		for (int i = 1; i <= EXPECTED_EC_BYTES / 2; i++) {
			int[] encodedIntArrayWithErrors = messUpParityBits(ReedSolomonImpl.parseStringToIntArray(correctIntArrayString), i);
			String encodedStringWithErrors = ReedSolomonImpl.composeIntArrayString(encodedIntArrayWithErrors);
			String decoded = rsImpl.decode(encodedStringWithErrors);
			assertEquals("hello", decoded);
		}
	}
	
	@Test
	public void testDecodeMixedCaseECBitDataError() throws ReedSolomonException, NotAsciiCodeExpcetion {
		String correctIntArrayString = "72, 69, 108, 76, 111, 28, 185, 240, 27, 6, 68";
		for (int i = 1; i <= EXPECTED_EC_BYTES / 2; i++) {
			int[] encodedIntArrayWithErrors = messUpDataBits(ReedSolomonImpl.parseStringToIntArray(correctIntArrayString), i);
			String encodedStringWithErrors = ReedSolomonImpl.composeIntArrayString(encodedIntArrayWithErrors);
			String decoded = rsImpl.decode(encodedStringWithErrors);
			assertEquals("HElLo", decoded);
		}
	}
	
	@Test
	public void testDecodeMixedCaseECBitParityError() throws ReedSolomonException, NotAsciiCodeExpcetion {
		String correctIntArrayString = "72, 69, 108, 76, 111, 28, 185, 240, 27, 6, 68";
		for (int i = 1; i <= EXPECTED_EC_BYTES / 2; i++) {
			int[] encodedIntArrayWithErrors = messUpParityBits(ReedSolomonImpl.parseStringToIntArray(correctIntArrayString), i);
			String encodedStringWithErrors = ReedSolomonImpl.composeIntArrayString(encodedIntArrayWithErrors);
			String decoded = rsImpl.decode(encodedStringWithErrors);
			assertEquals("HElLo", decoded);
		}
	}
	
	@Test(expected = NotAsciiCodeExpcetion.class)
	public void testNotAsciiCodeException() throws ReedSolomonException, NotAsciiCodeExpcetion {
		String encoded = "72, 69, 108, 76, 111, 28, 185, 240, 27, 6, 992";
		String decoded = rsImpl.decode(encoded);
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
			modifiedArray[length - 1 - i] = generateDiffAsciiCode(originalIntArray[length - 1 - i]);
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
