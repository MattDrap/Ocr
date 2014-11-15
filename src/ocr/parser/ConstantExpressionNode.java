package ocr.parser;

public class ConstantExpressionNode implements ExpressionNode {
	/** The value of the constant */
	private double value;

	/**
	 * Construct with the fixed value.
	 * 
	 * @param value
	 *            the value of the constant
	 */
	public ConstantExpressionNode(double value) {
		this.value = value;
	}

	/**
	 * Convenience constructor that takes a string and converts it to a double
	 * before storing the value.
	 * 
	 * @param value
	 *            the string representation of the value
	 */
	public ConstantExpressionNode(String value) {
		this.value = Double.valueOf(value);
	}

	/**
	 * Returns the value of the constant
	 */
        @Override
	public String getValue() {
		return String.valueOf(value);
	}

	/**
	 * Returns the type of the node, in this case ExpressionNode.CONSTANT_NODE
	 */
        @Override
	public int getType() {
		return ExpressionNode.CONSTANT_NODE;
	}

	/**
	 * Implementation of the visitor design pattern.
	 * 
	 * Calls visit on the visitor.
	 * 
	 * @param visitor
	 *            the visitor
	 */
        @Override
	public void accept(ExpressionNodeVisitor visitor) {
		visitor.visit(this);
	}
}