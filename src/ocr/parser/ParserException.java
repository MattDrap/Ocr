package ocr.parser;

public class ParserException extends RuntimeException {
	private static final long serialVersionUID = 4587575487444213755L;
	/** the token that caused the error */
	private Token token = null;

	/**
	 * Construct the evaluation exception with a message.
	 * 
	 * @param message
	 *            the message containing the cause of the exception
	 */
	public ParserException(String message) {
		super(message);
	}

	/**
	 * Construct the evaluation exception with a message and a token.
	 * 
	 * @param message
	 *            the message containing the cause of the exception
	 * @param token
	 *            the token that caused the exception
	 */
	public ParserException(String message, Token token) {
		super(message);
		this.token = token;
	}

	/**
	 * Get the token.
	 * 
	 * @return the token that caused the exception
	 */
	public Token getToken() {
		return token;
	}

	/**
	 * Overrides RuntimeException.getMessage to add the token information into
	 * the error message.
	 * 
	 * @return the error message
	 */
	public String getMessage() {
		String msg = super.getMessage();
		if (token != null) {
			msg = msg.replace("%s", token.sequence);
		}
		return msg;
	}
}
