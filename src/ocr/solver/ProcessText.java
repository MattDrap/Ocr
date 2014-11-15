package ocr.solver;

import java.util.Arrays;

import ocr.parser.ExpressionNode;
import ocr.parser.Parser;
import Jama.LUDecomposition;
import Jama.Matrix;

public class ProcessText {
	private static Parser p = new Parser();
	
	public static String Process(String mathtext){
		StringBuilder result = new StringBuilder();
		mathtext = mathtext.replace(" ", "");
		for(int i = 0; i < mathtext.length(); i++){
			if(Character.isLetter(mathtext.charAt(i))){
				result.append(mathtext.charAt(i));
				int j = i + 1;
				StringBuilder power = new StringBuilder();
				boolean firstpower = true;
				for(;j < mathtext.length() && Character.isDigit(mathtext.charAt(j)); j++){
					if(firstpower){
						power.append("^(");
						firstpower = false;
					}
					power.append(mathtext.charAt(j));
				}
				if(i != j - 1){
					power.append(")");
					result.append(power.toString());
					i = j - 1;
				}
			}else{
				result.append(mathtext.charAt(i));
			}
			
		}
		return result.toString();
	}
	public boolean match(String s1, String s2){
		return s1.equalsIgnoreCase(s2);
	}
	public String LU(double A [][], double B[]){
		Matrix m = new Matrix(A);
		Matrix b = new Matrix(B,1);
		Matrix solved = null;
		LUDecomposition LU = new LUDecomposition(m);
		try{
			solved = LU.solve(b);
		}catch(IllegalArgumentException e){
			
		}catch(RuntimeException e){
			
		}
		return Arrays.deepToString(solved.getArray());
	}
	public boolean isAlpha(String s){
		char[] chars = s.toCharArray();

	    for (char c : chars) {
	        if(!Character.isLetter(c)) {
	            return false;
	        }
	    }

	    return true;
	}
	public boolean isDigit(String s){
		char[] chars = s.toCharArray();

	    for (char c : chars) {
	        if(!Character.isDigit(c)) {
	            return false;
	        }
	    }
	    return true;
	}
	public boolean isAlphaDigit(String s){
		char[] chars = s.toCharArray();

	    for (char c : chars) {
	        if(!Character.isLetterOrDigit(c)) {
	            return false;
	        }
	    }
	    return true;
	}
	public static String wolphramquery(String inputText, String PID){	
		return "http://api.wolframalpha.com/v2/query?input="+inputText+"&appid="+PID;
	}
}
