package ocr.parser;

public class Token {
	/** Token id for the epsilon terminal */
	public static final int EPSILON = 0;
	/** Token id for plus or minus */
	public static final int PLUSMINUS = 1;
	/** Token id for multiplication or division */
	public static final int MULTDIV = 2;
	/** Token id for the exponentiation symbol */
	public static final int RAISED = 3;
	/** Token id for function names */
	public static final int FUNCTION = 4;
	/** Token id for opening brackets */
	public static final int OPEN_BRACKET = 5;
	/** Token id for closing brackets */
	public static final int CLOSE_BRACKET = 6;
	/** Token id for numbers */
	public static final int NUMBER = 7;
	/** Token id for variable names */
	public static final int VARIABLE = 8;
        /** Token id for equal sign */
        public static final int EQUAL = 9; 
	/** the token identifier */
	public final int token;
	/** the string that the token was created from */
	public final String sequence;
	/** the position of the token in the input string */
	public final int pos;

	/**
	 * Construct the token with its values
	 * 
	 * @param token
	 *            the token identifier
	 * @param sequence
	 *            the string that the token was created from
	 * @param pos
	 *            the position of the token in the input string
	 */
	public Token(int token, String sequence, int pos) {
		super();
		this.token = token;
		this.sequence = sequence;
		this.pos = pos;
	}
	public Token(Token t){
		super();
		this.token = t.token;
		this.sequence = t.sequence;
		this.pos = t.pos;
	}
}
