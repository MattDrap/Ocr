package ocr.solver;

import java.util.LinkedList;

import ocr.parser.ExpressionNode;
import ocr.parser.Parser;
import ocr.parser.Token;
import android.util.Log;

public class Equation {
	protected LinkedList<Token> left_side;
	protected LinkedList<Token> right_side;
	protected Parser parser;
	protected Token sign;
	public Equation(LinkedList<Token> tokens){
		parser = new Parser();
		separateSides(tokens);
	}
	protected void separateSides(LinkedList<Token> tokens){
		int relation_position = findRelation(tokens);
		if(relation_position < 0){
			left_side = Utils.DeepCopySubList(tokens);
			ExpressionNode l_exp = parser.evaluate(left_side);
			left_side = parser.parse(l_exp.getValue());
			return;
		}
		try{
			left_side = Utils.DeepCopySubList(tokens, 0, relation_position);
			ExpressionNode exp = parser.evaluate(left_side);
			left_side = parser.parse(exp.getValue());
			
			if(relation_position < tokens.size()){
				sign = new Token(tokens.get(relation_position).token, tokens.get(relation_position).sequence, relation_position);
				right_side = Utils.DeepCopySubList(tokens, relation_position + 1, tokens.size());
				if(right_side.isEmpty()){
					right_side.add(new Token(Token.NUMBER, "0", tokens.size() - 1));
				}
				ExpressionNode right_exp = parser.evaluate(right_side);
				right_side = parser.parse(right_exp.getValue());
			}else{
				right_side = null;
			}
		}catch(Exception e){
			Log.e("Equation", e.getMessage());
		}
	}
	protected int findRelation(LinkedList<Token> tokens){
		for(int i = 0; i < tokens.size(); i++){
			if(tokens.get(i).token == Token.EQUAL){
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
	
}
