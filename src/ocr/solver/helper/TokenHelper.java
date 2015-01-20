package ocr.solver.helper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import ocr.parser.ExpressionNode;
import ocr.parser.Parser;
import ocr.parser.Token;
import ocr.parser.VarNum;

/**
 * 
 * @author Matt
 */
public class TokenHelper {
	private TokenHelper() {
		throw new AssertionError();
	}

	public static MemberAndTokens detectNthPowermember(int n,
			LinkedList<Token> tokens, boolean left, Parser parser) {
		if (n == 1) {
			return detectLinearMember(tokens, left, parser);
		}
		if (tokens.size() < 4) {
			return new MemberAndTokens();
		}
		MemberAndTokens memAndTok = new MemberAndTokens();

		ArrayList<RemovePair> list_of_removes = new ArrayList<RemovePair>();

		Token var = tokens.get(0);
		Token mult_or_raised = tokens.get(1);
		Token parenthesis = tokens.get(2);

		for (int i = 3; i < tokens.size(); i++) {
			if (var.token == Token.VARIABLE
					&& mult_or_raised.token == Token.RAISED
					&& parenthesis.token == Token.OPEN_BRACKET
					&& tokens.get(i).token == Token.NUMBER) {
				try{
					double pow = Double.parseDouble(tokens.get(i).sequence);
					if(pow - n < 0.000001){					
						int pos = TokenHelper.detectAdditionToken(tokens, i);
						if (pos >= 0) {
							list_of_removes.add(new RemovePair(pos, i + 2));
							LinkedList<Token> subtokens = Utils.DeepCopySubList(
									tokens, pos, i - 3);
							subtokens.add(new Token(Token.NUMBER, "1", i));
							ExpressionNode exp = parser.evaluate(subtokens);
							String s = exp.getValue();
							double d = Double.parseDouble(s);
							if (!left)
								d *= -1;
							memAndTok.Member += d;
							memAndTok.Changed = true;
						}

					}
				}catch(NumberFormatException e){
					
				}
				
			}

			var = mult_or_raised;
			mult_or_raised = parenthesis;
			parenthesis = tokens.get(i);

		}
		memAndTok.Tokens = TokenHelper.removeMembers(list_of_removes, tokens);
		return memAndTok;
	}

	private static MemberAndTokens detectLinearMember(LinkedList<Token> tokens,
			boolean left, Parser parser) {
		MemberAndTokens memAndTok = new MemberAndTokens();
		ArrayList<RemovePair> list_of_removes = new ArrayList<RemovePair>();
		for (int i = 0; i < tokens.size(); i++) {
			if (tokens.get(i).token == Token.VARIABLE) {
				int pos = TokenHelper.detectAdditionToken(tokens, i);
				if (pos >= 0) {
					list_of_removes.add(new RemovePair(pos, i + 1));
					LinkedList<Token> subtokens = Utils.DeepCopySubList(tokens,
							pos, i);
					subtokens.add(new Token(Token.NUMBER, "1", i));
					ExpressionNode exp = parser.evaluate(subtokens);
					String s = exp.getValue();
					double d = Double.parseDouble(s);
					if (!left)
						d *= -1;
					memAndTok.Member += d;
					memAndTok.Changed = true;
				}
			}
		}
		memAndTok.Tokens = TokenHelper.removeMembers(list_of_removes, tokens);
		return memAndTok;
	}

	public static LinkedList<Token> removeMembers(
			ArrayList<RemovePair> removePairs, LinkedList<Token> tokens) {
		LinkedList<Token> rem_mems = new LinkedList<>();
		for (RemovePair pair : removePairs) {
			for (int i = pair.From; i < pair.To; i++) {
				rem_mems.add(tokens.get(i));
			}
		}
		tokens.removeAll(rem_mems);
		return tokens;
	}

	public static int detectAdditionToken(LinkedList<Token> tokens,
			int start_position) {
		for (int i = start_position; i >= 0; i--) {
			if (tokens.get(i).token == Token.PLUSMINUS) {
				return i;
			}
		}
		return -1;
	}
	public static ArrayList<VarNum> transformIntoVarNums(ExpressionNode exp,
			Parser parser) {
		LinkedList<Token> tokens = parser.parse(exp.getValue());
		return innertransformationVarNum(tokens);
	}

	protected static ArrayList<VarNum> innertransformationVarNum(
			LinkedList<Token> tokens) {
		ArrayList<VarNum> list = new ArrayList<>();
		VarNum varNum = new VarNum();
		boolean started = false;
		boolean superscript = false;
		for (Token token : tokens) {
			switch (token.token) {
			case Token.VARIABLE:
				if (varNum.getVar() != null) {
					varNum.setVar(varNum.getVar() + token.sequence);
				} else {
					varNum.setVar(token.sequence);
				}
				started = true;
				break;
			case Token.NUMBER:
				double t = Double.parseDouble(token.sequence);
				if (superscript) {
					varNum.setVar(varNum.getVar() + t);
				} else {
					varNum.setNum(varNum.distribute(t, true));
				}
				started = true;
				break;
			case Token.RAISED:
				varNum.setVar(varNum.getVar() + "^");
				superscript = true;
				break;
			case Token.OPEN_BRACKET:
				varNum.setVar(varNum.getVar() + "(");
				break;
			case Token.CLOSE_BRACKET:
				varNum.setVar(varNum.getVar() + ")");
				superscript = false;
				break;
			case Token.FUNCTION:
				if (varNum.getVar() != null) {
					varNum.setVar(varNum.getVar() + token.sequence);
				} else {
					varNum.setVar(token.sequence);
				}
				started = true;
				break;
			case Token.PLUSMINUS:
				if (superscript) {
					varNum.setVar(varNum.getVar() + token.sequence);
				} else if (started) {
					list.add(varNum);
					varNum = new VarNum();
				}
				if (token.sequence.equals("-")) {
					varNum.setNum(varNum.distribute(-1.0, true));
				}
				break;
			default:
				break;
			}
		}
		list.add(varNum);
		return list;
	}
	public static int findDivision(List<Token> list){
		for(int i = 0; i < list.size(); i++){
			if(list.get(i).sequence.equals("/")){
				return i;
			}
		}
		return -1;
	}
	public static List<Token> findDenominator(List<Token> list, int divisionIndex){
		int from = -1;
		int to = -1;
		int parenthesis_counter = 0;
		for(int i = divisionIndex + 1; i < list.size(); i++){
			if(list.get(i).token == Token.OPEN_BRACKET){
				if(from == -1){
					from = i;
				}
				parenthesis_counter++;
			}
			if(list.get(i).token == Token.CLOSE_BRACKET){
				to = i;
				parenthesis_counter--;
			}
			if(parenthesis_counter == 0)
				break;
		}
		if(from != -1 && to != -1){
			return Utils.DeepCopySubList(list, from, to);
		}
		return null;
	}
}
