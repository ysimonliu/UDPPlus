package forwardErrorCorrectionException;

/**
 * This is the exception raised when a 2-D parity cannot locate the error bit
 * @author Simon Liu
 *
 */
public class UnlocatableErrorException extends Exception {

	private static final long serialVersionUID = -5913580002982489428L;

	public UnlocatableErrorException(String message) {
		super(message);
	}

}
