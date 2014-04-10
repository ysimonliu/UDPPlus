package forwardErrorCorrectionException;

/**
 * This is the exception for an integer that is not a valid ASCII code
 * @author Simon Liu
 *
 */
public class NotAsciiCodeExpcetion extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 640034515906343995L;

	public NotAsciiCodeExpcetion(String message) {
		super(message);
	}
}
