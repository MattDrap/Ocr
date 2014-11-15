package ocr.parser;

public class ExponentiationExpressionNode implements ExpressionNode {
	/** the node containing the base */
	private ExpressionNode base;
	/** the node containing the exponent */
	private ExpressionNode exponent;

	/**
	 * Construct the ExponentiationExpressionNode with base and exponent
	 * 
	 * @param base
	 *            the node containing the base
	 * @param exponent
	 *            the node containing the exponent
	 */
	public ExponentiationExpressionNode(ExpressionNode base,
			ExpressionNode exponent) {
		this.base = base;
		this.exponent = exponent;
	}

	/**
	 * Returns the type of the node, in this case
	 * ExpressionNode.EXPONENTIATION_NODE
	 */
        @Override
	public int getType() {
		return ExpressionNode.EXPONENTIATION_NODE;
	}

	/**
	 * Returns the value of the sub-expression that is rooted at this node.
	 * 
	 * Calculates base^exponent
	 */
        @Override
	public String getValue() {
            double exp, bas;
            try{
                exp = Double.parseDouble(exponent.getValue());
                try{
                    bas = Double.parseDouble(base.getValue());
                    return String.valueOf(Math.pow(bas, exp));
                }catch(NumberFormatException e){
                    return base.getValue()+"^("+exponent.getValue()+")";
                }
            }catch(NumberFormatException e){
                return base.getValue()+"^("+exponent.getValue()+")";
            }
	}

	/**
	 * Implementation of the visitor design pattern.
	 * 
	 * Calls visit on the visitor and then passes the visitor on to the accept
	 * method of the base and the exponent.
	 * 
	 * @param visitor
	 *            the visitor
	 */
        @Override
	public void accept(ExpressionNodeVisitor visitor) {
		visitor.visit(this);
		base.accept(visitor);
		exponent.accept(visitor);
	}
}