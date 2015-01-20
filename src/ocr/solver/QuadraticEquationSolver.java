package ocr.solver;

import java.util.LinkedList;

import ocr.parser.Token;
import ocr.solver.helper.MemberAndTokens;
import ocr.solver.helper.TokenHelper;

public class QuadraticEquationSolver extends LinearEquationSolver{

	protected double quadratic_member;

	public QuadraticEquationSolver(Equation equation) {
		super(equation);
	}

	public LinkedList<Token> detectQuadMember(LinkedList<Token> tokens,
			boolean left) {
		MemberAndTokens memAndTok = TokenHelper.detectNthPowermember(2, tokens, left, parser);
		if(memAndTok.Changed){
			quadratic_member += memAndTok.Member;
			return memAndTok.Tokens;
		}
		return tokens;
		
	}

	@Override
	public String calculate() throws NotImplementedException {
		if (quadratic_member == 0) {
			return this.calculate();
		}
		double Discriminant = linear_member * linear_member - 4 * quadratic_member
				* member;
		if (Discriminant < 0) {
			Discriminant *= -1;

			String s_x1 = String.format("(%f + √%fi)/%f", -1 * linear_member,
					Discriminant, 2*quadratic_member);
			String s_x2 = String.format("(%f - √%fi)/%f", -1 * linear_member,
					Discriminant, 2*quadratic_member);
			return String.format("x1 = %s, x2 = %s", s_x1, s_x2);
		} else {
			double sqrtdisctiminant = Math.sqrt(Discriminant);
			double x1 = 0, x2 = 0;
			
				x1 = (-1 * linear_member + sqrtdisctiminant) / (2
						* quadratic_member);
				x2 = (-1 * linear_member - sqrtdisctiminant) / (2
						* quadratic_member);
			if (x1 == x2) {
				return String.format("x = %f", x1);
			} else {
				return String.format("x1 = %f, x2 = %f", x1, x2);
			}
		}
	}

	@Override
	public void prepare() {
		if (!equation.getLeft_side().isEmpty()) {
			equation.setLeft_side(detectQuadMember(equation.getLeft_side(),
					true));
		}
		if (equation.getRight_side() != null) {
			if (!equation.getRight_side().isEmpty()) {
				equation.setRight_side(detectQuadMember(
						equation.getRight_side(), false));
			}
		}
		super.prepare();
	}

}