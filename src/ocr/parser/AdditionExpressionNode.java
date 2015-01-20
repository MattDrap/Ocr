package ocr.parser;

public class AdditionExpressionNode extends BinaryExpressionNode {

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
		StringBuilder s = new StringBuilder();
        
        StringOrNumber leftyson = StringOrNumber.parseExpression(left);
        StringOrNumber rightyson = StringOrNumber.parseExpression(right);
        
       
        if(leftyson.changedNumber && rightyson.changedNumber){
              
                if(positive)
                    return String.valueOf(leftyson.d + rightyson.d);
                else
                    return String.valueOf(leftyson.d - rightyson.d);
        }
        s.append(leftyson.s);
        if(positive){
            s.append("+");
        }else{
            s.append("-");
        }
        s.append(rightyson.s);
        return s.toString();
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
		left.accept(visitor);
		right.accept(visitor);
	}
}
