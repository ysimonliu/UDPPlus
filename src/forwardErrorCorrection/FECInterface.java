package forwardErrorCorrection;

import forwardErrorCorrectionException.NotAsciiCodeExpcetion;
import forwardErrorCorrectionException.UndecodableException;
import forwardErrorCorrectionException.UnlocatableErrorException;
import reedSolomon.ReedSolomonException;

public interface FECInterface {
	
	public String encode(String plainText);
	
	public String decode(String encodedText) throws UnlocatableErrorException, NotAsciiCodeExpcetion, ReedSolomonException, UndecodableException;
	
}