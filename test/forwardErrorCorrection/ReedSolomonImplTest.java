package forwardErrorCorrection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;


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
	
}
