package forwardErrorCorrection;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import forwardErrorCorrectionException.UnlocatableErrorException;


public class Parity2DImplTest {

	Parity2DImpl parity2D;
	
	@Before
	public void setUp() {
		parity2D = new Parity2DImpl();
	}
	
	@Test
	public void testEncodeUpperCase() {
		String message = "HELLO";
		String encoded = parity2D.encode(message);
		
		int H = Integer.parseInt("10010001", 2);
		int E = Integer.parseInt("10001010", 2);
		int L = Integer.parseInt("10011000", 2);
		int O =  Integer.parseInt("10011110", 2);
		int last =  Integer.parseInt("00111101", 2);
		StringBuilder sb = new StringBuilder();
		sb.append((char)H);
		sb.append((char)E);
		sb.append((char)L);
		sb.append((char)L);
		sb.append((char)O);
		sb.append((char)last);
		assertEquals(sb.toString(), encoded);
	}
	
	@Test
	public void testEncodeLowerCase() {
		String message = "hello";
		String encoded = parity2D.encode(message);
		
		int h = Integer.parseInt("11010000", 2);
		int e = Integer.parseInt("11001011", 2);
		int l = Integer.parseInt("11011001", 2);
		int o = Integer.parseInt("11011111", 2);
		int last = Integer.parseInt("00011101", 2);;
		
		StringBuilder sb = new StringBuilder();
		sb.append((char)h);
		sb.append((char)e);
		sb.append((char)l);
		sb.append((char)l);
		sb.append((char)o);
		sb.append((char)last);
		assertEquals(sb.toString(), encoded);
	}
	
	@Test
	public void testEncodeMixedCase() {
		String message = "HElLo";
		String encoded = parity2D.encode(message);
		
		int H = Integer.parseInt("10010001", 2);
		int E = Integer.parseInt("10001010", 2);
		int l = Integer.parseInt("11011001", 2);
		int L = Integer.parseInt("10011000", 2);
		int o = Integer.parseInt("11011111", 2);
		int last = Integer.parseInt("00111101", 2);;
		
		StringBuilder sb = new StringBuilder();
		sb.append((char)H);
		sb.append((char)E);
		sb.append((char)l);
		sb.append((char)L);
		sb.append((char)o);
		sb.append((char)last);
		assertEquals(sb.toString(), encoded);
	}
	
	@Test
	public void testDecodedUpperCaseNoError() throws UnlocatableErrorException {
		
		int H = Integer.parseInt("10010001", 2);
		int E = Integer.parseInt("10001010", 2);
		int L = Integer.parseInt("10011000", 2);
		int O =  Integer.parseInt("10011110", 2);
		int last =  Integer.parseInt("00111101", 2);
		
		StringBuilder sb = new StringBuilder();
		sb.append((char)H);
		sb.append((char)E);
		sb.append((char)L);
		sb.append((char)L);
		sb.append((char)O);
		sb.append((char)last);
		
		String message = parity2D.decode(sb.toString());
		
		assertEquals(message, "HELLO");
	}
	
	@Test
	public void testDecodedLowerCaseNoError() throws UnlocatableErrorException {
		
		int h = Integer.parseInt("11010000", 2);
		int e = Integer.parseInt("11001011", 2);
		int l = Integer.parseInt("11011001", 2);
		int o = Integer.parseInt("11011111", 2);
		int last = Integer.parseInt("00011101", 2);;
		
		StringBuilder sb = new StringBuilder();
		sb.append((char)h);
		sb.append((char)e);
		sb.append((char)l);
		sb.append((char)l);
		sb.append((char)o);
		sb.append((char)last);
		
		String message = parity2D.decode(sb.toString());
		
		assertEquals(message, "hello");
	}
	
	@Test
	public void testDecodedMixedCaseNoError() throws UnlocatableErrorException {
		
		int H = Integer.parseInt("10010001", 2);
		int E = Integer.parseInt("10001010", 2);
		int l = Integer.parseInt("11011001", 2);
		int L = Integer.parseInt("10011000", 2);
		int o = Integer.parseInt("11011111", 2);
		int last = Integer.parseInt("00111101", 2);;
		
		StringBuilder sb = new StringBuilder();
		sb.append((char)H);
		sb.append((char)E);
		sb.append((char)l);
		sb.append((char)L);
		sb.append((char)o);
		sb.append((char)last);
		
		String message = parity2D.decode(sb.toString());
		
		assertEquals(message, "HElLo");
	}
	
	@Test
	public void testEncodeDecodeUpperCase() throws UnlocatableErrorException {
		String message = "HELLO";
		assertEquals(message, parity2D.decode(parity2D.encode(message)));
	}
	
	@Test
	public void testEncodeDecodeLowerCase() throws UnlocatableErrorException {
		String message = "hello";
		assertEquals(message, parity2D.decode(parity2D.encode(message)));
	}
	
	@Test
	public void testEncodeDecodeMixedCase() throws UnlocatableErrorException {
		String message = "HeLlO wOrLd";
		assertEquals(message, parity2D.decode(parity2D.encode(message)));
	}
	
	@Test
	public void testDecodeUpperCase1BitError() {
		
		int H = Integer.parseInt("10000001", 2);
		int E = Integer.parseInt("10001010", 2);
		int L = Integer.parseInt("10011000", 2);
		int O =  Integer.parseInt("10011110", 2);
		int last =  Integer.parseInt("00111101", 2);
		
		StringBuilder sb = new StringBuilder();
		sb.append((char)H);
		sb.append((char)E);
		sb.append((char)L);
		sb.append((char)L);
		sb.append((char)O);
		sb.append((char)last);
		
		String message = "";
		
		try {
			message = parity2D.decode(sb.toString());
		} catch (UnlocatableErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertEquals(message, "HELLO");
	}
	
	@Test
	public void testDecodeLowerCase1BitError() {
		int h = Integer.parseInt("11110000", 2);
		int e = Integer.parseInt("11001011", 2);
		int l = Integer.parseInt("11011001", 2);
		int o = Integer.parseInt("11011111", 2);
		int last = Integer.parseInt("00011101", 2);;
		
		StringBuilder sb = new StringBuilder();
		sb.append((char)h);
		sb.append((char)e);
		sb.append((char)l);
		sb.append((char)l);
		sb.append((char)o);
		sb.append((char)last);
		
		String message = "";
		try {
			message = parity2D.decode(sb.toString());
		} catch (UnlocatableErrorException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		assertEquals(message, "hello");
	}
	
	@Test
	public void testDecodeMixedCase1BitError() {
		int H = Integer.parseInt("10010001", 2);
		int E = Integer.parseInt("10001010", 2);
		int l = Integer.parseInt("11011001", 2);
		int L = Integer.parseInt("10011000", 2);
		int o = Integer.parseInt("11111111", 2);
		int last = Integer.parseInt("00111101", 2);;
		
		StringBuilder sb = new StringBuilder();
		sb.append((char)H);
		sb.append((char)E);
		sb.append((char)l);
		sb.append((char)L);
		sb.append((char)o);
		sb.append((char)last);
		
		String message = "";
		try {
			message = parity2D.decode(sb.toString());
		} catch (UnlocatableErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertEquals(message, "HElLo");
		
	}
	
	@Test
	public void testDecodeUpperCase2BitErrorDiffRow() {
		int H = Integer.parseInt("10000001", 2);
		int E = Integer.parseInt("10011010", 2);
		int L = Integer.parseInt("10011000", 2);
		int O =  Integer.parseInt("10011110", 2);
		int last =  Integer.parseInt("00111101", 2);
		
		StringBuilder sb = new StringBuilder();
		sb.append((char)H);
		sb.append((char)E);
		sb.append((char)L);
		sb.append((char)L);
		sb.append((char)O);
		sb.append((char)last);
		
		try {
			parity2D.decode(sb.toString());
			fail("UnlocatableErrorException not thrown. i.e.: errors not detected!");
		} catch (UnlocatableErrorException e) {
		}
	}
	
	@Test
	public void testDecodeUpperCase2BitErrorSameRow() {
		int H = Integer.parseInt("10001001", 2);
		int E = Integer.parseInt("10001010", 2);
		int L = Integer.parseInt("10011000", 2);
		int O =  Integer.parseInt("10011110", 2);
		int last =  Integer.parseInt("00111101", 2);
		
		StringBuilder sb = new StringBuilder();
		sb.append((char)H);
		sb.append((char)E);
		sb.append((char)L);
		sb.append((char)L);
		sb.append((char)O);
		sb.append((char)last);
		
		try {
			parity2D.decode(sb.toString());
			fail("UnlocatableErrorException not thrown. i.e.: errors not detected!");
		} catch (UnlocatableErrorException e) {
		}
	}
	
	@Test
	public void testDecodeLowerCase2BitErrorSameRow() {
		
		int h = Integer.parseInt("11010000", 2);
		int e = Integer.parseInt("11111011", 2);
		int l = Integer.parseInt("11011001", 2);
		int o = Integer.parseInt("11011111", 2);
		int last = Integer.parseInt("00011101", 2);;
		
		StringBuilder sb = new StringBuilder();
		sb.append((char)h);
		sb.append((char)e);
		sb.append((char)l);
		sb.append((char)l);
		sb.append((char)o);
		sb.append((char)last);
		
		try {
			parity2D.decode(sb.toString());
			fail("UnlocatableErrorException not thrown. i.e.: errors not detected!");
		} catch (UnlocatableErrorException e1) {
		}
	}
	
	@Test
	public void testDecodeMixedCase2BitErrorSameRow() {
		int H = Integer.parseInt("10010001", 2);
		int E = Integer.parseInt("10001010", 2);
		int l = Integer.parseInt("11011001", 2);
		int L = Integer.parseInt("10011000", 2);
		int o = Integer.parseInt("11101111", 2);
		int last = Integer.parseInt("00111101", 2);;
		
		StringBuilder sb = new StringBuilder();
		sb.append((char)H);
		sb.append((char)E);
		sb.append((char)l);
		sb.append((char)L);
		sb.append((char)o);
		sb.append((char)last);
		
		try {
			parity2D.decode(sb.toString());
			fail("UnlocatableErrorException not thrown. i.e.: errors not detected!");
		} catch (UnlocatableErrorException e1) {
		}
	}
	
	@Test
	public void testDecodeMixedCase2BitErrorDiffRow() {
		int H = Integer.parseInt("10010001", 2);
		int E = Integer.parseInt("10001010", 2);
		int l = Integer.parseInt("11111001", 2);
		int L = Integer.parseInt("10111000", 2);
		int o = Integer.parseInt("11011111", 2);
		int last = Integer.parseInt("00111101", 2);
		
		StringBuilder sb = new StringBuilder();
		sb.append((char)H);
		sb.append((char)E);
		sb.append((char)l);
		sb.append((char)L);
		sb.append((char)o);
		sb.append((char)last);
		
		try {
			parity2D.decode(sb.toString());
			fail("UnlocatableErrorException not thrown. i.e.: errors not detected!");
		} catch (UnlocatableErrorException e1) {
		}
	}
}
