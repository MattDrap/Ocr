package ocr.solver;

import java.util.LinkedList;

import ocr.parser.Token;
import ocr.solver.helper.MemberAndTokens;
import ocr.solver.helper.TokenHelper;

public class LinearEquationSolver extends SimpleAlgebraSolver{

	protected double linear_member;

	public LinearEquationSolver(Equation tokens) {
		super(tokens);
	}

	@Override
	public String calculate() throws NotImplementedException {
		if(linear_member == 0){
			return super.calculate();
		}
		double res = -this.member/this.linear_member;
		return String.format("x = %f", res);
	}

	@Override
	public void prepare() {
		if (!this.equation.getLeft_side().isEmpty()) {
			this.equation.setLeft_side(detectLinearMember(
					this.equation.getLeft_side(), true));
		}
		if (this.equation.getRight_side() != null) {
			if (!this.equation.getRight_side().isEmpty()) {
				this.equation.setRight_side(detectLinearMember(
						this.equation.getRight_side(), false));
			}
		}
		super.prepare();
	}

	public LinkedList<Token> detectLinearMember(LinkedList<Token> tokens,
			boolean left) {
		MemberAndTokens memAndTok = TokenHelper.detectNthPowermember(1, tokens, left, parser);
		if(memAndTok.Changed){
			linear_member += memAndTok.Member;
			return memAndTok.Tokens;
		}
		return tokens;
	}

}
