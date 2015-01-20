package ocr.parser;

import java.util.ArrayList;
import java.util.Collections;

import ocr.solver.NotImplementedException;
import ocr.solver.helper.TokenHelper;

public class MultiplicationExpressionNode extends BinaryExpressionNode {

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
		Parser parser = Parser.parser;
		
        ArrayList<VarNum> varnums = null;
        ArrayList<VarNum> denominator = null;
        PolynomialFraction remainder = null;
		
        varnums = TokenHelper.transformIntoVarNums(left, parser);
        denominator = TokenHelper.transformIntoVarNums(right, parser);
        if(positive){
            varnums = distribute(varnums, denominator, true);
        }else{
            if(sameVariable(varnums)){
                String nom_var = getVariable(varnums);
                if(nom_var != null){
                    if(allNonVariable(denominator)){
                        varnums = distribute(varnums, denominator, false);
                    }
                    else if(sameVariable(denominator, nom_var)){
                        Collections.sort(varnums);
                        Collections.sort(denominator);
                        PolynomialFractionAndRemainder pfar = 
                                polynomial_division(varnums, denominator);
                        if(pfar != null){
                            if(pfar.polynomial != null){
                                varnums = pfar.polynomial.toVarNums();
                            }
                            if(pfar.remainder != null){
                                remainder = pfar.remainder;
                            }
                        }
                    }
                }
            }
            else{
                if(allNonVariable(denominator)){
                    varnums = distribute(varnums, denominator, false);
                }
            }
		}
        StringBuilder b = new StringBuilder();
        if(denominator == null){
            for(VarNum vn :varnums){
                b.append(vn.toString());
            }
            if(b.charAt(0) == '+'){
                b.deleteCharAt(0);
            }
        }else{
            b.append("(");
            for(VarNum vn :varnums){
                b.append(vn.toString());
            }
            if(b.charAt(1) == '+'){
                b.deleteCharAt(1);
            }
            b.append(")/(");
            for(VarNum vn : denominator){
                b.append(vn.toString());
            }
            b.append(")");
        }
        if(remainder != null){
            b.append("+(");
            b.append(remainder.nominator.toString());
            b.append(")/(");
            b.append(remainder.denominator.toString());
            b.append(")");
        }
        return b.toString();
        }
        protected ArrayList<VarNum> distribute(ArrayList<VarNum> exp1, ArrayList<VarNum> exp2, boolean positive){
            ArrayList<VarNum> alist = new ArrayList<>();
            for(VarNum varnum : exp1){
                for(VarNum varnum2 : exp2){
                    VarNum newVarNum = new VarNum();
                    double d = varnum.distribute(varnum2.getNum(), positive);
                    String s = varnum.distribute(varnum2.getVar());
                    newVarNum.setNum(d);
                    newVarNum.setVar(s);
                    alist.add(newVarNum);
                }
            }
            return alist;
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
		left.accept(visitor);
		right.accept(visitor);
	}

    private PolynomialFractionAndRemainder polynomial_division(ArrayList<VarNum> varnums, ArrayList<VarNum> othervarnums) {
        Polynomial p = new Polynomial((int) getSup(varnums.get(0).variable), getVariable(varnums));
        for(VarNum vn : varnums){
            p.addMember(new Fraction(String.valueOf(vn.number)), (int) getSup(vn.variable));
        }
        Polynomial p2 = new Polynomial((int) getSup(othervarnums.get(0).variable), getVariable(othervarnums));
        for(VarNum vn : othervarnums){
            p2.addMember(new Fraction(String.valueOf(vn.number)), (int) getSup(vn.variable));
        }
        try{
            return p.divide(p2);
        }catch(IllegalArgumentException e){
            throw new NotImplementedException("Not implemented yet");
        }
    }
    private boolean sameVariable(ArrayList<VarNum> varnum){
        String test = getVariable(varnum);
        return sameVariable(varnum, test);
        
    }
    private double getSup(String s){
        if(s == null)
            return 0;
        int my_index = s.indexOf("^");
        if(my_index == -1){
            return 1;
        }else{
            return Double.parseDouble(s.substring(my_index + 2, s.length() - 1));
        }
    }
    private String getVariable(ArrayList<VarNum> varnum){
        for(VarNum vn : varnum){
                if(vn.getVar() != null){
                    int index = vn.getVar().indexOf("^");
                    if(index == -1){
                        return vn.getVar();
                    }else{
                        return vn.getVar().substring(0, index);
                    }
                }
        }
        return null;
    }

    private boolean sameVariable(ArrayList<VarNum> varnum, String var){
        if(var == null)
            return false;
        for(VarNum vn : varnum){
            if(vn.getVar()!= null){
                int index = vn.getVar().indexOf("^");
                if(index == -1){
                    if(!vn.getVar().equalsIgnoreCase(var)){
                        return false;
                    }
                }else{
                    if(!vn.getVar().substring(0, index).equalsIgnoreCase(var)){
                        return false;
                    }
                }
            }
        }
        return true;  
    }
    private boolean allNonVariable(ArrayList<VarNum> varnum){
        for (VarNum vn : varnum) {
            if(vn.getVar() != null){
                return false;
            }
        }
        return true;
    }
}
