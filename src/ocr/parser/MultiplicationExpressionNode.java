package ocr.parser;

public class MultiplicationExpressionNode extends SequenceExpressionNode {
	/**
	 * Default constructor.
	 */
	public MultiplicationExpressionNode() {
	}

	/**
	 * Constructor to create a multiplication with the first term already added.
	 * 
	 * @param node
	 *            the term to be added
	 * @param positive
	 *            a flag indicating whether the term is multiplied or divided
	 */
	public MultiplicationExpressionNode(ExpressionNode a, boolean positive) {
		super(a, positive);
	}

	/**
	 * Returns the type of the node, in this case
	 * ExpressionNode.MULTIPLICATION_NODE
	 */
	@Override
	public int getType() {
		return ExpressionNode.MULTIPLICATION_NODE;
	}

	/**
	 * Returns the value of the sub-expression that is rooted at this node.
	 * 
	 * All the terms are evaluated and multiplied or divided to the product.
	 */
	@Override
	public String getValue() {
		double prod = 1.0;
		StringBuilder s = new StringBuilder();
		for (Term t : terms) {
			if (t.positive)
				try {
					prod *= Double.parseDouble(t.expression.getValue());
				} catch (NumberFormatException e) {
					s.append("*");
					s.append(t.expression.getValue());
				}
			else
				try {
					prod /= Double.parseDouble(t.expression.getValue());
				} catch (NumberFormatException e) {
					s.append("/");
					s.append(t.expression.getValue());
				}
		}
		if (prod != 1.0) {
			return String.valueOf(prod) + s.toString();
		} else {
			return s.substring(1, s.length());
		}
	}

	/**
	 * Implementation of the visitor design pattern.
	 * 
	 * Calls visit on the visitor and then passes the visitor on to the accept
	 * method of all the terms in the product.
	 * 
	 * @param visitor
	 *            the visitor
	 */
	@Override
	public void accept(ExpressionNodeVisitor visitor) {
		visitor.visit(this);
		for (Term t : terms)
			t.expression.accept(visitor);
	}
}
