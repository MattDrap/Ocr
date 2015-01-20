package ocr.solver;

import ocr.parser.EvaluationException;
import ocr.parser.ExpressionNode;
import ocr.parser.Parser;
import ocr.parser.ParserException;

public class SimpleAlgebraSolver implements IExampleSolver {

	protected final Parser parser;
	protected final Equation equation;
	double member;

	public SimpleAlgebraSolver(Equation tokens) {
		parser = Parser.parser;
		this.equation = tokens;
	}

	@Override
	public String calculate() throws NotImplementedException {
		if (equation.getRight_side() != null) {
			return String.format("%f = 0", member);
		}
		return String.valueOf(member);
	}

	@Override
	public void prepare() {
		try {
			ExpressionNode expression_left = null, expression_right = null;
			if (!equation.getLeft_side().isEmpty()) {
				expression_left = parser.evaluate(equation.left_side);
				member += Double.parseDouble(expression_left.getValue());
			}
			if (equation.getRight_side() != null) {
				if (!equation.getRight_side().isEmpty()) {
					expression_right = parser.evaluate(equation.right_side);
					member -= Double.parseDouble(expression_right.getValue());
				}
			}
		} catch (ParserException e) {
			throw new NotImplementedException("Fault in recognition of example");
		} catch (EvaluationException e) {
			throw new NotImplementedException("Fault in recognition of example");
		} catch(NumberFormatException e){
			throw new NumberFormatException("Usage wrong was or Fault in application was");
		}
	}
}
