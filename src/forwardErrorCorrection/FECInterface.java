package forwardErrorCorrection;

import forwardErrorCorrectionException.NotAsciiCodeExpcetion;
import forwardErrorCorrectionException.UndecodableException;
import forwardErrorCorrectionException.UnlocatableErrorException;
import reedSolomon.ReedSolomonException;

public interface FECInterface {
	
	/**
	 * Encodes plain text to encoded text using the corresponding Forward Error Correction algorithm
	 * @param plainText - plain text
	 * @return encoded message as a string
	 */
	
	public String encode(String plainText);
	/**
	 * Decodes encoded text to plain text using the corresponding Forward Error Correction algorithm
	 * @param encodedText - encoded text
	 * @return decoded message as a string
	 * @throws UnlocatableErrorException {@link UnlocatableErrorException}
	 * @throws NotAsciiCodeExpcetion {@link NotAsciiCodeExpcetion}
	 * @throws ReedSolomonException {@link ReedSolomonException}
	 * @throws UndecodableException {@link UndecodableException}
	 */
	public String decode(String encodedText) throws UnlocatableErrorException, NotAsciiCodeExpcetion, ReedSolomonException, UndecodableException;
	
}