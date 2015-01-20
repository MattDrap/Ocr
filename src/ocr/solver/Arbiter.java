package ocr.solver;

import java.util.ArrayList;
import java.util.LinkedList;

import ocr.parser.Token;

public final class Arbiter {
	public static enum TYPE_OF_EXAMPLE {
		UNDECIDED, SIMPLE_ALGEBRA, LINEAR_EQUATION, QUADRATIC_EQUATION, NTHPOWER_EQUATION, LINEAR_EQUATIONS;

		@Override
		public String toString() {
			return this.name();
		}
	}
	
	private Arbiter() {
		new AssertionError();
	}

	public static boolean isMatrix(ArrayList<Equation> eq_list){
		for (Equation eq : eq_list) {
			TYPE_OF_EXAMPLE left_type = Arbiter.JudgeMatrix(eq.left_side);
			if (eq.right_side != null) {
				TYPE_OF_EXAMPLE right_type = Arbiter.JudgeMatrix(eq.right_side);
				if (left_type != TYPE_OF_EXAMPLE.LINEAR_EQUATIONS
						&& right_type != TYPE_OF_EXAMPLE.LINEAR_EQUATIONS) {
					return false;
				}
			} else {
				if (left_type != TYPE_OF_EXAMPLE.LINEAR_EQUATIONS) {
					return false;
				}
			}
		}
		return true;
	}
	protected static TYPE_OF_EXAMPLE JudgeMatrix(LinkedList<Token> tokens){
		if (isQuadraticEquation(tokens)) {
			return TYPE_OF_EXAMPLE.QUADRATIC_EQUATION;
		}
		if (isLinearEquations(tokens)) {
			return TYPE_OF_EXAMPLE.LINEAR_EQUATIONS;
		}
		return TYPE_OF_EXAMPLE.SIMPLE_ALGEBRA;
	}
	public static TYPE_OF_EXAMPLE Judge(LinkedList<Token> tokens) {
		if (isQuadraticEquation(tokens)) {
			return TYPE_OF_EXAMPLE.QUADRATIC_EQUATION;
		}
		if (isLinearEquations(tokens)) {
			return TYPE_OF_EXAMPLE.LINEAR_EQUATION;
		}
		return TYPE_OF_EXAMPLE.SIMPLE_ALGEBRA;
	}
	protected static boolean isLinearEquation(LinkedList<Token> tokens){
		boolean first = false;
		for (Token token : tokens) {
			if (token.token == Token.VARIABLE) {
				if(!first){
					first = true;
				}else{
					return false;
				}
			}
		}
		return first;
	}
	protected static boolean isLinearEquations(LinkedList<Token> tokens) {
		for (Token token : tokens) {
			if (token.token == Token.VARIABLE) {
				return true;
			}
		}
		return false;
	}
	
	protected static boolean isQuadraticEquation(LinkedList<Token> tokens) {
		if (tokens.size() < 4)
			return false;
		Token preToken = tokens.get(0);
		Token raised = tokens.get(1);
		Token parenthesis = tokens.get(2);
		for (int i = 3; i < tokens.size(); i++) {
			if (preToken.token == Token.VARIABLE
					&& raised.token == Token.RAISED
					&& parenthesis.token == Token.OPEN_BRACKET){
				try{
					double d = Double.parseDouble(tokens.get(i).sequence);
					if(d - 2.0 < 0.0000001){
						return true;
					}
				}catch(NumberFormatException e){
					
				}
			}
			preToken = raised;
			raised = parenthesis;
			parenthesis = tokens.get(i);
		}
		return false;
	}
	protected static boolean isNthpowerEquation(LinkedList<Token> tokens, double power){
		if (tokens.size() < 4)
			return false;
		Token preToken = tokens.get(0);
		Token raised = tokens.get(1);
		Token parenthesis = tokens.get(2);
		for (int i = 3; i < tokens.size(); i++) {
			if (preToken.token == Token.VARIABLE
					&& raised.token == Token.RAISED
					&& parenthesis.token == Token.OPEN_BRACKET){
				try{
					double d = Double.parseDouble(tokens.get(i).sequence);
					if(d - power < 0.0000001){
						return true;
					}
				}catch(NumberFormatException e){
					
				}
			}
			preToken = raised;
			raised = parenthesis;
			parenthesis = tokens.get(i);
		}
		return false;
	}
	protected static double NthpowerOfEquation(LinkedList<Token> tokens){
		if (tokens.size() < 4){
			if(isLinearEquation(tokens)){
				return 1;
			}
			return 0;
		}
		double max_power = 1;
		Token preToken = tokens.get(0);
		Token raised = tokens.get(1);
		Token parenthesis = tokens.get(2);
		for (int i = 3; i < tokens.size(); i++) {
			if (preToken.token == Token.VARIABLE
					&& raised.token == Token.RAISED
					&& parenthesis.token == Token.OPEN_BRACKET) {
				try{
					double temp = Double.parseDouble(tokens.get(i).sequence);
					if(temp > max_power){
						max_power = temp;
					}
				}catch(NumberFormatException exp){
					throw new NotImplementedException("Not yet implemented");
				}
			}
			preToken = raised;
			raised = parenthesis;
			parenthesis = tokens.get(i);
		}
		return max_power;
	}
}
