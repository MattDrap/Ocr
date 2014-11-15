package ocr.solver;

import java.util.LinkedList;

import ocr.parser.Token;

public class Arbiter {
	public static enum TYPE_OF_EXAMPLE{
		UNDECIDED,
		SIMPLE_ALGEBRA,
		LINEAR_EQUATIONS,
		QUADRATIC_EQUATION;
		
		@Override
		public String toString(){
			return this.name();
		}
	}
	private Arbiter(){
		new AssertionError();
	}
	public static TYPE_OF_EXAMPLE Judge(LinkedList<Token> tokens){
		if(isQuadraticEquation(tokens)){
			return TYPE_OF_EXAMPLE.QUADRATIC_EQUATION;
		}
		if(isLinearEquations(tokens)){
			return TYPE_OF_EXAMPLE.LINEAR_EQUATIONS;
		}
		return TYPE_OF_EXAMPLE.SIMPLE_ALGEBRA;
	}
	
	public static boolean isLinearEquations(LinkedList<Token> tokens){
	        if(tokens.size() < 3){
	            return false;
	        }
	        boolean isLinear = false;
	        Token pretoken = tokens.get(0);
	        Token operation = tokens.get(1);
	        for (int i = 2; i < tokens.size(); i++) {
	            if((tokens.get(i).token == Token.VARIABLE && operation.token == Token.MULTDIV && pretoken.token == Token.NUMBER)
	                    ||
	               (tokens.get(i).token == Token.NUMBER && operation.token == Token.MULTDIV && pretoken.token == Token.VARIABLE)){
	                isLinear = true;
	            }
	            pretoken = operation;
	            operation = tokens.get(i);
	        }
	        return isLinear;
	    }
	private static boolean isQuadraticEquation(LinkedList<Token> tokens){
		if(tokens.size() < 3)
			return false;
		Token preToken = tokens.get(0);
		Token raised = tokens.get(1);
		for(int i = 2; i < tokens.size(); i++){
			if(preToken.token == Token.VARIABLE && raised.token == Token.RAISED && tokens.get(i).sequence == "2"){
				return true;
			}
			preToken = raised;
			raised = tokens.get(i);
		}
		return false;
	}
}
