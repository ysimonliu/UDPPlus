package forwardErrorCorrectionException;

/**
 * This exception is raised when a string is not multiples of 8 and thus cannot be decoded into decimal integers which should correspond to ASCII codes
 * @author Simon Liu
 *
 */
public class UndecodableException extends Exception {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = -7462024833018146044L;

	public UndecodableException(String message) {
		super(message);
	}

}