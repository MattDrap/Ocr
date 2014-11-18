package ocr.solver;

import java.util.ArrayList;
import java.util.LinkedList;

import ocr.parser.Parser;
import ocr.parser.Token;
import ocr.solver.Arbiter.TYPE_OF_EXAMPLE;

public class Solver {
	Parser parser;
	ExampleSolver solver;
	ArrayList<Equation> list_of_equations;
	TYPE_OF_EXAMPLE Type_of_example;
	public Solver(){
		parser = new Parser();
		Type_of_example = Arbiter.TYPE_OF_EXAMPLE.UNDECIDED;
		list_of_equations = new ArrayList<Equation>();
		
	}
	protected boolean is_all_linear(ArrayList<Equation> eq_list){
		for(Equation eq : eq_list){
			if(Arbiter.Judge(eq.left_side) != TYPE_OF_EXAMPLE.LINEAR_EQUATIONS){
				return false;
			}
			if(eq.right_side != null){
				if(Arbiter.Judge(eq.right_side) != TYPE_OF_EXAMPLE.SIMPLE_ALGEBRA){
					return false;
				}
			}
		}
		return true;
	}
	protected TYPE_OF_EXAMPLE decide(ArrayList<Equation> eq_list){
		if(eq_list.size() > 1){
			if(is_all_linear(eq_list))
				return TYPE_OF_EXAMPLE.LINEAR_EQUATIONS;
			throw new NotImplementedException("Not implemented yet");
		}
		if(eq_list.get(0).getRight_side() != null){
			if(Arbiter.Judge(eq_list.get(0).right_side) != TYPE_OF_EXAMPLE.SIMPLE_ALGEBRA)
				throw new NotImplementedException("Not implemented yet");
		}
		return Arbiter.Judge(eq_list.get(0).left_side);
	}
	public String solve(String [] OcrTexts){
		list_of_equations.clear();
		for(String Text : OcrTexts){
			LinkedList<Token> t = parser.parse(Text);
			Equation eq = new Equation(t);
			list_of_equations.add(eq);
		}
		Type_of_example = decide(list_of_equations);
		switch(Type_of_example){
			case LINEAR_EQUATIONS:
				solver = new LinearEquationsSolver(list_of_equations);
				break;
			case QUADRATIC_EQUATION:
				solver = new QuadraticEquationSolver(list_of_equations.get(0).left_side);
				break;
			default:
				solver = new SimpleAlgebraSolver(list_of_equations.get(0));
				break;	
		}
		return solver.calculate();
	}
	public String getType(){
		return Type_of_example.toString();
	}
}
