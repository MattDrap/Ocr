package ocr.parser;

import java.math.BigInteger;
import java.util.ArrayList;

import ocr.solver.helper.TokenHelper;

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
                double exp;
                try{
                    exp = Double.parseDouble(exponent.getValue());
                    ArrayList<VarNum> base_varnums = TokenHelper.transformIntoVarNums(base, Parser.parser);
                    if(base_varnums.size() == 2){
                        ArrayList<BigInteger> coefs = binomialcoef((int) exp);
                        ArrayList<VarNum> results = new ArrayList<>();
                        for(int i = 0; i < coefs.size(); i++){
                            VarNum vn = new VarNum();
                            boolean first = true;
                            for(VarNum inner_varnum : base_varnums){
                                if(first){
                                    vn.setNum(inner_varnum.enpowerNum(exp - i));
                                    vn.setVar(inner_varnum.enpowerVar(exp - i));
                                    first = false;
                                }else{
                                    vn.setNum(vn.distribute(inner_varnum.enpowerNum(i), true));
                                    if(inner_varnum.enpowerVar(i) != null){
                                        vn.setVar(vn.distribute(inner_varnum.enpowerVar(i)));
                                    }
                                }
                            }
                            vn.setNum(vn.distribute(coefs.get(i).doubleValue(), true));
                            results.add(vn);
                        }
                        StringBuilder sb = new StringBuilder();
                        for(VarNum var_num : results){
                            sb.append(var_num.toString());
                        }
                        return sb.toString();
                    }
                    else{
                        double d_base;
                        try{
                            d_base = Double.parseDouble(base.getValue());
                            return String.valueOf(Math.pow(d_base, exp));
                        }catch(NumberFormatException e){
                            return base.getValue()+"^("+exponent.getValue()+")";
                        }
                    }
                }catch(NumberFormatException e){
                    return base.getValue()+"^("+exponent.getValue()+")";
                }
    	}
            protected ArrayList<BigInteger> binomialcoef(int n){
                BigInteger coef = BigInteger.ONE;
                ArrayList<BigInteger> alist = new ArrayList<>();
                alist.add(coef);
                for(int k = 0; k < n; k++){
                    coef = coef.multiply(BigInteger.valueOf(n-k)).divide(BigInteger.valueOf(k+1));
                    alist.add(coef);
                }
                return alist;
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