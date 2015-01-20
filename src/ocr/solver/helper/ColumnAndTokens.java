package ocr.solver.helper;

import java.util.LinkedList;

import ocr.parser.Token;


public class ColumnAndTokens {
    public LinkedList<Token> Left_Tokens;
    public LinkedList<Token> Right_Tokens;
    public double [] Column;
    public ColumnAndTokens(LinkedList<Token> left_tokens, LinkedList<Token> right_tokens, double [] column){
        Left_Tokens = left_tokens;
        Right_Tokens = right_tokens;
        Column = column;
    }
   
}
