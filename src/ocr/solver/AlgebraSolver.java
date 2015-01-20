package ocr.solver;

import java.util.ArrayList;
import java.util.LinkedList;

import ocr.parser.Parser;
import ocr.parser.Token;
import ocr.solver.Arbiter.TYPE_OF_EXAMPLE;

public class AlgebraSolver extends SolverBase {
	Parser parser;
	IExampleSolver solver;
	ArrayList<Equation> list_of_equations;
	TYPE_OF_EXAMPLE Type_of_example;
	boolean superscripts;

	public AlgebraSolver(boolean superscripts) {
		parser = Parser.parser;
		Type_of_example = Arbiter.TYPE_OF_EXAMPLE.UNDECIDED;
		list_of_equations = new ArrayList<Equation>();
		this.superscripts = superscripts;
	}

	protected TYPE_OF_EXAMPLE decide(ArrayList<Equation> eq_list) {
		if (eq_list.size() > 1) {
			if (Arbiter.isMatrix(eq_list))
				return TYPE_OF_EXAMPLE.LINEAR_EQUATIONS;
			throw new NotImplementedException("Not implemented yet");
		}
		TYPE_OF_EXAMPLE left_type = Arbiter.Judge(eq_list.get(0).getLeft_side());
		if(eq_list.get(0).getRight_side() != null){
			TYPE_OF_EXAMPLE right_type = Arbiter.Judge(eq_list.get(0).getRight_side());
			if(left_type.compareTo(right_type) < 1){
				return right_type;
			}
			return left_type;
		}else{
			return left_type;
		}
	}

	@Override
	public String solve(String[] OcrTexts) {
		list_of_equations.clear();
		for (String Text : OcrTexts) {
			LinkedList<Token> t = parser.parse(Text);
			Equation eq = new Equation(t, superscripts);
			list_of_equations.add(eq);
		}
		for(Equation eq : list_of_equations){
			Corrector.correctEquation(eq);
		}
		Type_of_example = decide(list_of_equations);
		switch (Type_of_example) {
		case LINEAR_EQUATIONS:
			solver = new LinearEquationsSolver(list_of_equations);
			break;
		case QUADRATIC_EQUATION:
			solver = new QuadraticEquationSolver(list_of_equations.get(0));
			break;
		case LINEAR_EQUATION:
			solver = new LinearEquationSolver(list_of_equations.get(0));
			break;
		default:
			solver = new SimpleAlgebraSolver(list_of_equations.get(0));
			break;
		}
		solver.prepare();
		return solver.calculate();
	}

	public String getType() {
		return Type_of_example.toString();
	}
}
