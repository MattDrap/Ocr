package ocr.solver;

import java.util.LinkedList;

import ocr.parser.ExpressionNode;
import ocr.parser.Parser;
import ocr.parser.Token;
import ocr.solver.helper.Utils;

public class Equation {
	protected LinkedList<Token> left_side;
	protected LinkedList<Token> right_side;
	protected Parser parser;
	protected Token sign;
	protected boolean superscripts;

	public Equation(LinkedList<Token> tokens, boolean superscripts) {
		parser = Parser.parser;
		this.superscripts = superscripts;
		separateSides(tokens);
		
	}

	protected LinkedList<Token> correctFirst(LinkedList<Token> tokens) {
		if (tokens.size() < 1) {
			return null;
		}
		if (tokens.get(0).token != Token.PLUSMINUS) {
			tokens.add(0, new Token(Token.PLUSMINUS, "+", 0));
		}
		return tokens;
	}

	protected LinkedList<Token> correctTokens(LinkedList<Token> tokens, boolean superscripts) {
		if (tokens.size() < 2) {
			return null;
		}
		Token pretoken = tokens.get(0);
		if(pretoken.token == Token.VARIABLE && superscripts){
			tokens = correctPower(0, tokens);
		}
		for (int i = 1; i < tokens.size(); i++) {
			if(tokens.get(i).token == Token.VARIABLE){
				if(superscripts){
					tokens = correctPower(i, tokens);
				}
				if (pretoken.token == Token.NUMBER){
					tokens.add(i, new Token(Token.MULTDIV, "*", i));
				}
			}
			if(pretoken.token == Token.VARIABLE){
				if(tokens.get(i).token == Token.OPEN_BRACKET){
					tokens.add(i, new Token(Token.MULTDIV, "*", i));
				}
				if(tokens.get(i).token == Token.VARIABLE){
					tokens.add(i, new Token(Token.MULTDIV, "*", i));
				}
			}
			else if(pretoken.token == Token.CLOSE_BRACKET){
				if(tokens.get(i).token == Token.OPEN_BRACKET){
					tokens.add(i, new Token(Token.MULTDIV, "*", i));
				}
				if(tokens.get(i).token == Token.NUMBER && superscripts){
					String seq = tokens.get(i).sequence;
					tokens.remove(i);
					tokens.add(i, new Token(Token.CLOSE_BRACKET, ")", 1));
					tokens.add(i, new Token(Token.NUMBER, seq, 1));
					tokens.add(i, new Token(Token.OPEN_BRACKET, "(",1));
					tokens.add(i, new Token(Token.RAISED, "^", 1));
				}
			}
			pretoken = tokens.get(i);
		}
		return tokens;
	}
	protected LinkedList<Token> correctPower(int i, LinkedList<Token> tokens){
		Token token = tokens.get(i);
		int power = correctPowerSub(token.sequence);
		if(power != -1){
			tokens.add(i+1, new Token(Token.CLOSE_BRACKET, ")", 1));
			tokens.add(i+1, new Token(Token.NUMBER, token.sequence.substring(power), 1));
			tokens.add(i+1, new Token(Token.OPEN_BRACKET, "(",1));
			tokens.add(i+1, new Token(Token.RAISED, "^", 1));
			String var_name = token.sequence.substring(0, power);
			tokens.remove(i);
			tokens.add(i, new Token(Token.VARIABLE, var_name, 0));
			return tokens;
		}else{
			return tokens;
		}
		
	}
	protected int correctPowerSub(String sequence) {
		for(int i = 0; i < sequence.length(); i++){
			if(Character.isDigit(sequence.charAt(i)))
					return i;
		}
		return -1;
	}

	protected void separateSides(LinkedList<Token> tokens) {
		int relation_position = findRelation(tokens);
		if (relation_position < 0) {
			left_side = Utils.DeepCopySubList(tokens);
			
			 										//IF USER DONT WANT TO TRANSFORM NUMBERS AFTER VARIABLE TO SUPERSCRIPT 
			LinkedList<Token> temp = correctTokens(left_side, superscripts);
			if (temp != null) {
				left_side = temp;
			}
			
			ExpressionNode exp = parser.evaluate(left_side);
			left_side = parser.parse(exp.getValue());
			if (!left_side.isEmpty()) {
				left_side = correctFirst(left_side);
			}
			return;
		}
		try {
			left_side = Utils.DeepCopySubList(tokens, 0, relation_position);
													//IF USER DONT WANT TO TRANSFORM NUMBERS AFTER VARIABLE TO SUPERSCRIPT 
			LinkedList<Token> temp = correctTokens(left_side, superscripts);
			if (temp != null) {
				left_side = temp;
			}
			ExpressionNode exp = parser.evaluate(left_side);
			left_side = parser.parse(exp.getValue());

			if (relation_position < tokens.size()) {
				sign = new Token(tokens.get(relation_position).token,
						tokens.get(relation_position).sequence,
						relation_position);
				right_side = Utils.DeepCopySubList(tokens,
						relation_position + 1, tokens.size());
				if (right_side.isEmpty()) {
					right_side.add(new Token(Token.NUMBER, "0",
							tokens.size() - 1));
				}
								//IF USER DONT WANT TO TRANSFORM NUMBERS AFTER VARIABLE TO SUPERSCRIPT
				temp = correctTokens(right_side, superscripts);
				if (temp != null) {
					right_side = temp;
				}
				
				ExpressionNode right_exp = parser.evaluate(right_side);
				right_side = parser.parse(right_exp.getValue());
				if (!right_side.isEmpty()) {
					right_side = correctFirst(right_side);
				}
			} else {
				right_side = null;
			}
		} catch (Exception e) {
			System.out.println("Equation" + e.getMessage());
		}
	}

	protected int findRelation(LinkedList<Token> tokens) {
		for (int i = 0; i < tokens.size(); i++) {
			if (tokens.get(i).token == Token.EQUAL) {
				return i;
			}
		}
		return -1;
	}

	public LinkedList<Token> getLeft_side() {
		return left_side;
	}

	public LinkedList<Token> getRight_side() {
		return right_side;
	}

	public void setLeft_side(LinkedList<Token> left_side) {
		this.left_side = left_side;
	}

	public void setRight_side(LinkedList<Token> right_side) {
		this.right_side = right_side;
	}

}
