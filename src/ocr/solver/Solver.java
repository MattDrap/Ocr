package ocr.solver;

import java.util.ArrayList;
import java.util.LinkedList;

import ocr.parser.Parser;
import ocr.parser.Token;
import ocr.solver.Arbiter.TYPE_OF_EXAMPLE;

public class Solver {
	Parser parser;
	ExampleSolver solver;
	ArrayList<LinkedList<Token>> list_pre_tokens;
	ArrayList<LinkedList<Token>> list_tokens;
	TYPE_OF_EXAMPLE Type_of_example;
	public Solver(){
		parser = new Parser();
		Type_of_example = Arbiter.TYPE_OF_EXAMPLE.UNDECIDED;
		list_pre_tokens = new ArrayList<LinkedList<Token>>();
		list_tokens = new ArrayList<LinkedList<Token>>();
	}
	public String solve(String [] OcrTexts){
		list_pre_tokens.clear();
		list_tokens.clear();
		for(int i = 0; i < OcrTexts.length;  i++){
			LinkedList<Token> t = parser.parse(OcrTexts[i]);
			list_pre_tokens.add(t);
		}
		for(int i = 0; i < list_pre_tokens.size(); i++){
			list_tokens.add(parser.parse(parser.evaluate(list_pre_tokens.get(i)).getValue()));
		}
		list_pre_tokens.clear();
		
		Type_of_example = Arbiter.Judge(list_tokens.get(0));
		switch(Type_of_example){
			case LINEAR_EQUATIONS:
				solver = new LinearEquationsSolver(list_tokens);
				break;
			case QUADRATIC_EQUATION:
				solver = new QuadraticEquationSolver(list_tokens.get(0));
				break;
			default:
				solver = new SimpleAlgebraSolver(list_tokens.get(0));
				break;	
		}
		return solver.calculate();
	}
	public String getType(){
		return Type_of_example.toString();
	}
}
