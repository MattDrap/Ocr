package ocr.solver;

import java.util.ArrayList;
import java.util.LinkedList;

import ocr.parser.Token;

public class LinearEquationsSolver implements ExampleSolver {
	private Jama.Matrix Matrix = null;
	private Jama.Matrix rightHandSide = null;
	public LinearEquationsSolver(ArrayList<LinkedList<Token>> list_tokens) {
		ArrayList<ArrayList<Double>> all_members = new ArrayList<ArrayList<Double>>();
		for(LinkedList<Token> tokens : list_tokens){
			ArrayList<Double> column_members = detectLinearMembers(tokens);
			all_members.add(column_members);
		}
		Matrix = fillToMatrix(all_members, list_tokens.size());
		
	}
    public static ArrayList<Double> detectLinearMembers(LinkedList<Token> tokens){
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
                }
            }
        }
        if(tokens.get(tokens.size() - 1).token == Token.VARIABLE){
            list.add(1.0);
        }
        return list;
    }
    public static Jama.Matrix fillToMatrix(ArrayList<ArrayList<Double>> members, int rows) throws IllegalArgumentException{
        if(rows < 1)
            throw new IllegalArgumentException();
        int columns = members.get(0).size();
        if(columns < 1)
            throw new IllegalArgumentException();
        double [][] double_matrix = new double[rows][columns];
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; i++){
                double_matrix[i][j] = members.get(i).get(j);
            }
        }
        return new Jama.Matrix(double_matrix);
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
			Jama.Matrix ret = LU.solve(rightHandSide);
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
