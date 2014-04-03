package forwardErrorCorrection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import forwardErrorCorrectionException.NotAsciiCodeExpcetion;

import reedSolomon.ReedSolomonException;


public class ReedSolomonImplTest {
	
	ReedSolomonImpl rsImpl;
	
	@Before
	public void setUp() {
		rsImpl = new ReedSolomonImpl(3);
		
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
	public void testEncodeDecodeUpperCase() throws ReedSolomonException, NotAsciiCodeExpcetion {
		String message = "HELLO";
		assertEquals(message, rsImpl.decode(rsImpl.encode(message)));
	}
	
	@Test
	public void testEncodeDecodeLowerCase() throws ReedSolomonException, NotAsciiCodeExpcetion {
		String message = "hello";
		assertEquals(message, rsImpl.decode(rsImpl.encode(message)));
	}
	
	@Test
	public void testEncodeDecodeMixedCase() throws ReedSolomonException, NotAsciiCodeExpcetion {
		String message = "HeLlO wOrLd";
		assertEquals(message, rsImpl.decode(rsImpl.encode(message)));
	}
	
	@Test
	public void testDecodeUpperCase1BitDataError() throws ReedSolomonException, NotAsciiCodeExpcetion {
		String encoded = "72, 69, 76, 76, 22, 168, 254, 255";
		String message = rsImpl.decode(encoded);
		assertEquals(message, "HELLO");
	}
	
	@Test
	public void testDecodeUpperCase1BitParityError() throws ReedSolomonException, NotAsciiCodeExpcetion {
		String encoded = "72, 69, 76, 76, 79, 168, 254, 1";
		String message = rsImpl.decode(encoded);
		assertEquals(message, "HELLO");
	}
	
	@Test
	public void testDecodeLowerCase1BitDataError() throws ReedSolomonException, NotAsciiCodeExpcetion {
		String encoded = "104, 12, 108, 108, 111, 98, 199, 222";
		String message = rsImpl.decode(encoded);
		assertEquals(message, "hello");
	}
	
	@Test
	public void testDecodeLowerCase1BitParityError() throws ReedSolomonException, NotAsciiCodeExpcetion {
		String encoded = "104, 101, 108, 108, 111, 98, 199, 245";
		String message = rsImpl.decode(encoded);
		assertEquals(message, "hello");
	}
	@Test
	public void testDecodeMixedCase1BitDataError() throws ReedSolomonException, NotAsciiCodeExpcetion {
		String encoded = "72, 33, 108, 76, 111, 121, 173, 243";
		String message = rsImpl.decode(encoded);
		assertEquals(message, "HElLo");
	}
	
	@Test
	public void testDecodeMixedCase1BitParityError() throws ReedSolomonException, NotAsciiCodeExpcetion {
		String encoded = "72, 69, 108, 76, 111, 121, 133, 243";
		String message = rsImpl.decode(encoded);
		assertEquals(message, "HElLo");
	}
}
