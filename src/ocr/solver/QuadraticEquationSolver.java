package ocr.solver;

import java.util.LinkedList;

import ocr.parser.Token;

public class QuadraticEquationSolver implements ExampleSolver {

	private double quadratic_member;
	private double linear_member;
	private double member;
	private double Discriminant;
	public QuadraticEquationSolver(LinkedList<Token> tokens) {
		detectMembers(tokens);
		Discriminant = linear_member*linear_member - 4 * quadratic_member * member;
	}
	public void detectMembers(LinkedList<Token> tokens){
		if(tokens.size() < 4){
			throw new IllegalArgumentException();
		}
		Token var = tokens.get(0);
		Token mult_or_raised = tokens.get(1);
		Token parenthesis = tokens.get(2);
		
		for(int i = 0; i < tokens.size(); i++){
			if(var.token == Token.VARIABLE && mult_or_raised.token == Token.RAISED
					&& parenthesis.token == Token.OPEN_BRACKET && tokens.get(i).token == Token.NUMBER){
				if(tokens.get(i).sequence.equals("2.0")){
					if(i - 5 < 0){
						quadratic_member = 1;
					}else{
						quadratic_member = Double.parseDouble(tokens.get(i-5).sequence);
					}
				}
			}
			else if(mult_or_raised.token == Token.VARIABLE && parenthesis.token == Token.MULTDIV && tokens.get(i).token == Token.NUMBER){
				linear_member = Double.parseDouble(tokens.get(i).sequence);
			}
			else if(tokens.get(i).token == Token.NUMBER){
				member = Double.parseDouble(tokens.get(i).sequence);
			}
				
			var = parenthesis;
			parenthesis = mult_or_raised;
			mult_or_raised = tokens.get(i);
			
		}
	}
	@Override
	public String calculate() throws NotImplementedException {
		if(Discriminant < 0){
			Discriminant *= -1;
			double sqrtdiscriminant = Math.sqrt(Discriminant);
			//To-do complex roots
		}else{
			double sqrtdisctiminant = Math.sqrt(Discriminant);
			double x1 = (-1*linear_member+sqrtdisctiminant)/2*quadratic_member;
			double x2 = (-1*linear_member-sqrtdisctiminant)/2*quadratic_member;
			if(x1 == x2){
				return String.format("x = %f", x1);
			}else{
				return String.format("x1 = %f, x2 = %f", x1,x2);
			}
		}
		return null;
	}

}
