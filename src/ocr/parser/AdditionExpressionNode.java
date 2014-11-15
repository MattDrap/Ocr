package ocr.parser;

public class AdditionExpressionNode extends SequenceExpressionNode {
	/**
	 * Default constructor.
	 */
	public AdditionExpressionNode() {
	}

	/**
	 * Constructor to create an addition with the first term already added.
	 * 
	 * @param node
	 *            the term to be added
	 * @param positive
	 *            a flag indicating whether the term is added or subtracted
	 */
	public AdditionExpressionNode(ExpressionNode node, boolean positive) {
		super(node, positive);
	}

	/**
	 * Returns the type of the node, in this case ExpressionNode.ADDITION_NODE
	 */
	public int getType() {
		return ExpressionNode.ADDITION_NODE;
	}

	/**
	 * Returns the value of the sub-expression that is rooted at this node.
	 * 
	 * All the terms are evaluated and added or subtracted from the total sum.
	 */
        @Override
	public String getValue() {
		double sum = 0.0;
                String s = "";
		for (Term t : terms) {
			if (t.positive)
                            try{
                                sum += Double.parseDouble(t.expression.getValue());
                            }catch(NumberFormatException e){
                                s += t.expression.getValue() + "+";
                            }
			else
                            try{
                                sum -= Double.parseDouble(t.expression.getValue());
                            }catch(NumberFormatException e){
                                s += t.expression.getValue() + "-";
                            }
		}
                if(sum != 0.0){
                    s += String.valueOf(sum);
                }else{
                    s = s.substring(0, s.length() - 1);
                }
		return s;
	}

	/**
	 * Implementation of the visitor design pattern.
	 * 
	 * Calls visit on the visitor and then passes the visitor on to the accept
	 * method of all the terms in the sum.
	 * 
	 * @param visitor
	 *            the visitor
	 */
	public void accept(ExpressionNodeVisitor visitor) {
		visitor.visit(this);
		for (Term t : terms)
			t.expression.accept(visitor);
	}
}
