package forwardErrorCorrection;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;


public class ReedSolomonImplTest {
	
	ReedSolomonImpl rsImpl;
	
	@Before
	public void setUp() {
		rsImpl = new ReedSolomonImpl(3);
		
	}
	
	@Test
	public void testEncodeUpperCase(){
		String message = "HELLO";
		System.out.println("DEBUG: original message: " + message);
		String encoded = rsImpl.encode(message);
		System.out.println("DEBUG: encoded message: " + encoded);
		try {
			String decoded = rsImpl.decode(encoded);
			System.out.println("DEBUG: decoded message: " + decoded);
		} catch (Exception ex) {
			System.out.print("Exception when decoding: " + ex);
		}
	}
}
