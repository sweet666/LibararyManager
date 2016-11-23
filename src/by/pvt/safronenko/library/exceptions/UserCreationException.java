package by.pvt.safronenko.library.exceptions;

/**
 * User creation exception.
 */
public class UserCreationException extends Exception {

	private static final long serialVersionUID = -3572793444175618651L;

	/**
	 * Constructs exception.
	 *
	 * @param message
	 *            Message.
	 * @param e
	 *            Exception.
	 */
	public UserCreationException(String message, Throwable e) {
		super(message, e);
	}

	/**
	 * Constructs exception.
	 *
	 * @param message
	 *            Message.
	 */
	public UserCreationException(String message) {
		super(message);
	}
}
