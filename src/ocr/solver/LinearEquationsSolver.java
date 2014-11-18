package ocr.solver;

import java.util.ArrayList;
import java.util.LinkedList;

import ocr.parser.ExpressionNode;
import ocr.parser.Parser;
import ocr.parser.Token;
import Jama.Matrix;

public class LinearEquationsSolver implements ExampleSolver {
	private Matrix Matrix = null;
	private Matrix rightHandSide = null;
	Parser parser;
	public LinearEquationsSolver(ArrayList<Equation> list_equations) {
		parser = new Parser();
		ArrayList<ArrayList<Double>> all_members = new ArrayList<ArrayList<Double>>();
		ArrayList<Double> right_side_members = new ArrayList<Double>();
		for(Equation equation : list_equations){
			ArrayList<Double> column_members = detectLinearMembers(equation.getLeft_side());
			if(equation.getRight_side() != null){
				ExpressionNode exp = parser.evaluate(equation.getRight_side());
				double member = Double.parseDouble(exp.getValue());
				right_side_members.add(member);
			}
			all_members.add(column_members);
		}
		Matrix = fillToMatrix(all_members, list_equations.size());
		rightHandSide = fillToVector(right_side_members);
		
	}
	private ArrayList<String> Analyze(LinkedList<Token> tokens){
		ArrayList<String> strings = new ArrayList<>();
		for(Token token : tokens){
			if(token.token == Token.VARIABLE){
				strings.add(token.sequence);
			}
		}
		return strings;
	}
	private int find_Variable(ArrayList<String> vars, String var_sequence){
		for(int i = 0; i < vars.size(); i++){
			if(var_sequence.toLowerCase().compareToIgnoreCase(vars.get(i)) == 0){
				return i;
			}
		}
		return -1;
	}
	private double [] detectLinearColumnMembers(LinkedList<Token> tokens, ArrayList<String> vars) throws IllegalArgumentException{
		if(vars.size() < 1){
			throw new IllegalArgumentException();
		}
		double [] column_mem = new double[vars.size()];
        for(int i = 0; i < tokens.size()-2; i++){
            if(tokens.get(i).token == Token.VARIABLE){
            	int index = find_Variable(vars, tokens.get(i).sequence);
            	if(index >= 0){
	                if(i < tokens.size() && tokens.get(i+1).token == Token.MULTDIV){
	                    try{
	                        double d = Double.parseDouble(tokens.get(i+2).sequence);
	                        column_mem[index] = d;
	                    }catch(NumberFormatException e){
	                        throw new IllegalArgumentException();
	                    }
	                }else{
	                	column_mem[index] = 1.0;
	                }
            	}
            }
        }
        if(tokens.get(tokens.size() - 1).token == Token.VARIABLE){
            int index = find_Variable(vars, tokens.get(tokens.size() - 1).sequence);
            if(index >= 0){
            	column_mem[index] = 1.0;
            }
        }
        return column_mem;
    }
    private Matrix fillToVector(ArrayList<Double> right_side_members) {
    	double [] array_vector = new double[right_side_members.size()];
    	for(int i = 0; i < right_side_members.size(); i++){
    		array_vector[i] = right_side_members.get(i);
    	}
		return new Matrix(array_vector, 1);
	}
	public ArrayList<Double> detectLinearMembers(LinkedList<Token> tokens){
        ArrayList<Double> list = new ArrayList<>();
        for(int i = 0; i < tokens.size()-1; i++){
            if(tokens.get(i).token == Token.VARIABLE){
                if(i < tokens.size() && tokens.get(i+1).token == Token.MULTDIV){
                    try{
                        Double d = Double.parseDouble(tokens.get(i+2).sequence);
                        list.add(d);
                    }catch(NumberFormatException e){
                        throw new IllegalArgumentException();
                    }
                }else{
                	list.add(1.0);
                }
            }
        }
        if(tokens.get(tokens.size() - 1).token == Token.VARIABLE){
            list.add(1.0);
        }
        return list;
    }
    public Matrix fillToMatrix(ArrayList<ArrayList<Double>> members, int rows) throws IllegalArgumentException{
        if(rows < 1)
            throw new IllegalArgumentException();
        int columns = members.get(0).size();
        if(columns < 1)
            throw new IllegalArgumentException();
        double [][] double_matrix = new double[rows][columns];
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){
                double_matrix[i][j] = members.get(i).get(j);
            }
        }
        Matrix local_matrix = null;
        try{
        	local_matrix = new Matrix(double_matrix);
        }catch(IllegalArgumentException e){
        	
        }
        catch(Exception e){
        	
        }
        return local_matrix;
    }
	@Override
	public String calculate() throws NotImplementedException {
		Jama.LUDecomposition LU = new Jama.LUDecomposition(Matrix);
		if(rightHandSide == null){
			int colCount = Matrix.getColumnDimension();
			double [] prerighthand = new double [colCount];
			for(int i = 0; i < colCount; i++){
				prerighthand[i] = 0;
			}
			rightHandSide = new Jama.Matrix(prerighthand, 1);
		}
		try{
			Matrix ret = LU.solve(rightHandSide.transpose());
			double [] [] dret = ret.getArray();
			StringBuilder sb = new StringBuilder();
			for(int i = 0; i < dret.length; i++){
				for(int j = 0; j < dret[i].length; j++){
					sb.append(dret[i][j]);
					sb.append(" | ");
				}
				sb.append("\n");
			}
			return sb.toString();
		}catch(Exception e){
			throw new NotImplementedException("Not implemented yet" + e.getMessage());
		}
	}
}
