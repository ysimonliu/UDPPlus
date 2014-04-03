package forwardErrorCorrection;

import reedSolomon.ReedSolomonException;

public interface FECInterface {
	
	public String encode(String plainText);
	
	public String decode(String encodedText) throws UnlocatableErrorException, ReedSolomonException;
	
}
