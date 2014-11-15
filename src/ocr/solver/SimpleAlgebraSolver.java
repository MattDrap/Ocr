package ocr.solver;

import java.util.LinkedList;

import ocr.parser.ExpressionNode;
import ocr.parser.Parser;
import ocr.parser.ParserException;
import ocr.parser.Token;

public class SimpleAlgebraSolver implements ExampleSolver {

	private Parser parser;
	private LinkedList<Token> tokens;
	private ExpressionNode expression;
	public SimpleAlgebraSolver(LinkedList<Token> tokens){
		parser = new Parser();
		this.tokens = tokens;
	}
	@Override
	public String calculate() throws NotImplementedException{
		try{
			expression = parser.evaluate(tokens);
			return String.valueOf(expression.getValue());
		}catch(ParserException e){
			throw new NotImplementedException("Fault in recognition of example");
		}
	}
	
}
