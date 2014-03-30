package forwardErrorCorrection;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class Parity2DImplTest {

	Parity2DImpl parity2D;
	
	@Before
	public void startUp() {
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
	public void testDecodedUpperCaseNoError() {
		
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
	public void testDecodedLowerCaseNoError() {
		
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
	public void testDecodedMixedCaseNoError() {
		
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
	public void testEncodeDecodeUpperCase() {
		String message = "HELLO";
		assertEquals(message, parity2D.decode(parity2D.encode(message)));
	}
	
	@Test
	public void testEncodeDecodeLowerCase() {
		String message = "hello";
		assertEquals(message, parity2D.decode(parity2D.encode(message)));
	}
	
	@Test
	public void testEncodeDecodeMixedCase() {
		String message = "HeLlO wOrLd";
		assertEquals(message, parity2D.decode(parity2D.encode(message)));
	}
	
	@Test
	public void testDecodeUpperCase1BitError() {
		
	}
	
	@Test
	public void testDecodeLowerCase1BitError() {
		
	}
	
	@Test
	public void testDecodeMixedCase1BitError() {
		
	}
	
	@Test
	public void testDecodeUpperCase2BitError() {
		
	}
	
	@Test
	public void testDecodeLowerCase2BitError() {
		
	}
	
	@Test
	public void testDecodeMixedCase2BitError() {
		
	}
}
