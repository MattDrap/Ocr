package ocr.solver;

import java.util.LinkedList;

import ocr.parser.ExpressionNode;
import ocr.parser.Parser;
import ocr.parser.ParserException;
import ocr.parser.Token;

public class SimpleAlgebraSolver implements ExampleSolver {

	private Parser parser;
	private Equation equation;
	private ExpressionNode expression_left, expression_right;
	public SimpleAlgebraSolver(Equation tokens){
		parser = new Parser();
		this.equation = tokens;
	}
	@Override
	public String calculate() throws NotImplementedException{
		try{
			if(equation.left_side != null){
				expression_left = parser.evaluate(equation.left_side);
			}
			if(equation.right_side != null){
				expression_right = parser.evaluate(equation.right_side);
			}
			if(expression_left != null){
				if(expression_right != null){
					return String.format("%s = %s", String.valueOf(expression_left.getValue()), String.valueOf(expression_right.getValue()));
				}
				return String.valueOf(expression_left.getValue());
			}else{
				if(expression_right != null){
					return String.valueOf(expression_right.getValue());
				}
				throw new ParserException(null);
			}
		}catch(ParserException e){
			throw new NotImplementedException("Fault in recognition of example");
		}
	}
	
}
