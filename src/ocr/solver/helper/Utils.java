package ocr.solver.helper;

import java.util.LinkedList;
import java.util.List;

import ocr.parser.Token;

public class Utils {
	public static LinkedList<Token> DeepCopySubList(List<Token> list, int from, int to){
		LinkedList<Token> ret_list = new LinkedList<Token>();
		for(int i = from; i < to; i++){
			Token token = list.get(i);
			Token t = new Token(token);
			ret_list.add(t);
		}
		return ret_list;
	}
	public static LinkedList<Token> DeepCopySubList(List<Token> list){
		LinkedList<Token> ret_list = new LinkedList<Token>();
		for(Token token: list){
			Token t = new Token(token);
			ret_list.add(t);
		}
		return ret_list;
	}
}
