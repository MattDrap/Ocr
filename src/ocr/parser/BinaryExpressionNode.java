package ocr.parser;

public abstract class BinaryExpressionNode implements ExpressionNode {
	
	protected ExpressionNode left;
    protected ExpressionNode right;
    protected boolean positive;

	/**
	 * Constructor to create a sequence with the first term already added.
	 * 
	 * @param node
	 *            the term to be added
	 * @param positive
	 *            a boolean flag
	 */
	public BinaryExpressionNode(ExpressionNode a, boolean positive) {
		left = a;
		this.positive = positive;
	}
	
	public BinaryExpressionNode(ExpressionNode left, boolean positive, ExpressionNode right){
        this.left = left;
        this.positive = positive;
        this.right = right;
    }
	/**
	 * Add another term to the sequence
	 * 
	 * @param node
	 *            the term to be added
	 * @param positive
	 *            a boolean flag
	 */
	public void addRight(ExpressionNode node) {
		right = node;
	}
}
