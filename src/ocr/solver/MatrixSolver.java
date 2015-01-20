package ocr.solver;

import java.util.ArrayList;

import ocr.parser.Parser;
import ocr.solver.Matrix.MatrixTypes;

public class MatrixSolver extends SolverBase{
	Parser parser;
	Matrix matrix;
	static Matrix previousMatrix;
	int option;
	public MatrixSolver(int option){
		parser = Parser.parser;
		this.option = option;
	}
	@Override
	public String solve(String[] OcrTexts){
		if(OcrTexts == null){
			throw new IllegalArgumentException();
		}
		try {
			prepare(OcrTexts);
			switch (option) {
			case 0:
				GaussJordan GJ = new GaussJordan(matrix, null);
				GJ.Solve();
				previousMatrix = GJ.matrix;
				return GJ.matrix.toString();
			case 1:
				if(matrix.numCols != matrix.numRows)
					throw new IllegalArgumentException("Matrix must be square");
				GaussJordan Inverse = new GaussJordan(matrix, new Matrix(matrix.numRows, matrix.numCols, MatrixTypes.EYE));
				Inverse.Solve();
				previousMatrix = Inverse.b_side;
				return Inverse.b_side.toString();
			case 2:				
				return String.valueOf(matrix.determinant());
			case 3:
				matrix.transpose();
				previousMatrix = matrix;
				return matrix.toString();
			case 4:
				if(previousMatrix != null){
					previousMatrix = previousMatrix.add(matrix);
					return previousMatrix.toString();
				}else{
					throw new IllegalArgumentException("Previous matrix not stored");
				}
			case 5:
				if(previousMatrix != null){
					previousMatrix = previousMatrix.subb(matrix);
					return previousMatrix.toString();
				}else{
					throw new IllegalArgumentException("Previous matrix not stored");
				}
			case 6:
				throw new NotImplementedException("Not implemented yet");	
			case 7:
				throw new NotImplementedException("Not implemented yet");		
			case 8:
				previousMatrix = matrix;
				break;
			}
			return matrix.toString();
		} catch (IllegalAccessException e) {
			throw new NotImplementedException(e.getMessage());
		}catch (NotImplementedException e) {
			throw new NotImplementedException(e.getMessage());
		}
	}
	public void prepare(String [] OcrTexts) throws IllegalAccessException{
		int column_num = 0;
		int row_num = 0;
		for(String string : OcrTexts){
			if(!string.equals("")){
				row_num++;
			}else{
				throw new IllegalArgumentException("Wrong input");
			}
		}
		for(int i = 0; i < OcrTexts.length; i++){
			String [] nums_in_cols = OcrTexts[i].split("\\s+");
			ArrayList<Double> nums_in_columns = new ArrayList<>();
			for(int j = 0; j < nums_in_cols.length; j++){
				try{
				 nums_in_columns.add(Double.parseDouble(nums_in_cols[j]));
				}catch(NumberFormatException e){
					throw new NotImplementedException("Not implemented yet");
				}
			}
			if(i == 0){
				column_num = nums_in_columns.size();
				matrix = new Matrix(row_num, column_num);
			}
			if(column_num != nums_in_columns.size())
				throw new IllegalAccessException("Uneven number of members");
			matrix.setRow(i, nums_in_columns);
		}
	}
	
	@Override
	public String getType() {
		return "MATRIX";
	}
}
