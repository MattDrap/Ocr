package ocr.solver;

import java.util.LinkedList;

import ocr.parser.Parser;
import ocr.parser.Token;
import ocr.solver.helper.TokenHelper;

public final class Corrector {
	private Corrector(){
		throw new IllegalArgumentException();
	}
	public static Equation correctEquation(Equation e){
		LinkedList<Token> tomove = null;
		do{
			int index = TokenHelper.findDivision(e.left_side);
			if(index != -1){
				tomove = (LinkedList<Token>) TokenHelper.findDenominator(e.left_side, index);
			}
			if(tomove != null){
				e = recuperate(tomove, e);
			}
		}while(tomove != null);
		return e;
	}
	private static Equation recuperate(LinkedList<Token> tomove, Equation e) {
		e.left_side.add(0, new Token(Token.OPEN_BRACKET, "(", 0));
		e.left_side.add(0, new Token(Token.MULTDIV, "*", 0));
		e.left_side.addAll(0, tomove);
		e.left_side.add(new Token(Token.CLOSE_BRACKET, ")", 0));
		String eval = Parser.parser.evaluate(e.left_side).getValue();
		e.left_side = Parser.parser.parse(eval);
		if(e.right_side != null){
			e.right_side.add(0, new Token(Token.OPEN_BRACKET, "(", 0));
			e.right_side.add(0, new Token(Token.MULTDIV, "*", 0));
			e.right_side.addAll(0, tomove);
			e.right_side.add(new Token(Token.CLOSE_BRACKET, ")", 0));
			eval = Parser.parser.evaluate(e.right_side).getValue();
			e.right_side = Parser.parser.parse(eval);
		}
		return e;
	}
	
}
