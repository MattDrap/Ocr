package ocr.solver;

import java.util.ArrayList;

public class Matrix {

	public static class Coordinate {
		int row;
		int col;

		Coordinate(int r, int c) {
			row = r;
			col = c;
		}

		Coordinate(Coordinate coordinate) {
			this(coordinate.row, coordinate.col);
		}
	}
	public static enum MatrixTypes{
		EYE,
		ZERO
	}

	double [][] matrix;
	int numRows;
	int numCols;

	public ArrayList<Double> getRow(int i) {
		if (i < 0 || i > numRows)
			throw new IllegalArgumentException();
		ArrayList<Double> row = new ArrayList<>(numCols);
		for(double d : matrix[i]){
			row.add(d);
		}
		return row;
	}

	public void setRow(int i, ArrayList<Double> row) {
		if (i < 0 || i > numRows)
			throw new IllegalArgumentException();
		if (row.size() != numCols)
			throw new IllegalArgumentException();
		int j = 0;
		for(double d : row){
			matrix[i][j++] = d;
		}
	}

	public void set(int i, int j, double value) {
		if (i < 0 || i > numRows || j < 0 || j > numCols)
			throw new IllegalArgumentException();
		matrix[i][j] = value;
	}

	public double get(int i, int j) {
		if (i < 0 || i > numRows || j < 0 || j > numCols)
			throw new IllegalArgumentException();
		return matrix[i][j];
	}

	public ArrayList<Double> getColumn(int j) {
		if (j < 0 || j > numCols)
			throw new IllegalArgumentException();
		ArrayList<Double> column = new ArrayList<>(numRows);
		for(int i = 0; i < numRows; i++){
			column.add(matrix[i][j]);
		}
		return column;
	}

	public void setColumn(int j, ArrayList<Double> column) {
		if (j < 0 || j > numCols)
			throw new IllegalArgumentException();
		if (column.size() != numRows)
			throw new IllegalArgumentException();
		int t = 0;
		for(int i = 0; i < numRows; i++){
			matrix[i][j] = column.get(t++);
		}
	}

	public void transpose() {
		if (numRows == numCols) {
			for (int i = 0; i < numRows; i++) {
				for (int j = i + 1; j < numCols; j++) {
					double temp = get(i, j);
					
					set(i, j, get(j, i));
					set(j, i, temp);
				}
			}
		} else {
			double [][] temp_matrix = new double [numCols][numRows];
			for (int i = 0; i < numRows; i++) {
				for (int j = 0; j < numCols; j++) {
					temp_matrix[j][i] = get(i,j);
				}
			}
			matrix = temp_matrix;
		}
	}

	Matrix(int rows, int cols, MatrixTypes type){
		if (rows < 1 || cols < 1){
			throw new IllegalArgumentException();
		}
		numRows = rows;
		numCols = cols;
		matrix = new double [rows][cols];
		if(type == MatrixTypes.EYE){
			for(int i = 0; i < numRows; i++){
				for(int j = 0; j < numCols; j++){
					if(i == j){
						matrix[i][j] = 1.0;
					}else{
						matrix[i][j] = 0.;
					}
				}
			}
		}
	}
	Matrix(int rows, int cols){
		if (rows < 1 || cols < 1){
			throw new IllegalArgumentException();
		}
		numRows = rows;
		numCols = cols;
		matrix = new double [rows][cols];
		for(int i = 0; i < numRows; i++){
			for(int j = 0; j < numCols; j++){
				matrix[i][j] = 0.;
			}
		}
	}
	Matrix(double[][] m) {
		numRows = m.length;
		numCols = m[0].length;
		matrix = m;
	}
	Matrix(double [] m){
		numRows = m.length;
		numCols = 1;
		matrix = new double[][]{m};
	}
	public boolean isColumnZeroes(Coordinate a) {
		for (int i = 0; i < numRows; i++) {
			if (matrix[i][a.col] != 0.0) {
				return false;
			}
		}

		return true;
	}

	public boolean isRowZeroes(Coordinate a) {
		for (int i = 0; i < numCols; i++) {
			if (matrix[a.row][i] != 0.0) {
				return false;
			}
		}

		return true;
	}

	public double getCoordinate(Coordinate a) {
		return matrix[a.row][a.col];
	}
	
	public Matrix add(Matrix m){
		if(numRows != m.numRows || numCols != m.numCols)
			throw new IllegalArgumentException();
		Matrix result = new Matrix(numRows, numCols);
		for(int i = 0; i < numRows; i++){
			for(int j = 0; j < numCols; j++){
				result.set(i, j, get(i, j) + m.get(i, j));
			}
		}
		return result;
	}
	public Matrix subb(Matrix m){
		if(numRows != m.numRows || numCols != m.numCols)
			throw new IllegalArgumentException();
		Matrix result = new Matrix(numRows, numCols);
		for(int i = 0; i < numRows; i++){
			for(int j = 0; j < numCols; j++){
				result.set(i, j, get(i, j) - m.get(i, j));
			}
		}
		return result;
	}
	public Matrix inverse(){
		Matrix result = new Matrix(numRows, numCols, MatrixTypes.EYE);
		GaussJordan GJ = new GaussJordan(this, result);
		GJ.Solve();
		return GJ.b_side;
	}
	public double determinant(){
		if(numCols != numRows)
			throw new IllegalArgumentException();
		if(numCols == 3)
			return determinant3x3(matrix);
		if(numCols == 2)
			return matrix[0][0]*matrix[1][1] - matrix[0][1]*matrix[1][0];
		if(numCols == 1)
			return matrix[0][0];
		return subdeterminant(matrix);
	}
	private double subdeterminant(double [][] sub_matrix){
		if(sub_matrix.length == 3)
			return determinant3x3(sub_matrix);
		double determinant = 0;
		int flip = 1;
		for(int i = 0; i < numCols; i++){
			if(sub_matrix[0][i] != 0){
				double [][] sub_sub_matrix = new double [sub_matrix.length - 1][sub_matrix.length - 1];
				int rowin = 0, colin = 0;
				for(int j = 1; j < sub_matrix.length; j++){
					for(int k = 0; k < sub_matrix.length; k++){
						if(k == i)
							continue;
						sub_sub_matrix[rowin++][colin++] = sub_matrix[j][k];
					}
				}
				determinant += sub_matrix[0][i]*flip*subdeterminant(sub_sub_matrix);
			}
			flip *= -1;
		}
		return determinant;
	}
	private double determinant3x3(double [][] matrix){
		return  matrix[0][0] * matrix[1][1] * matrix[2][2] +
				matrix[0][1] * matrix[1][2] * matrix[2][0] +
				matrix[0][2] * matrix[1][0] * matrix[2][1] -
				matrix[0][2] * matrix[1][1] * matrix[2][0] - 
				matrix[0][1] * matrix[1][0] * matrix[2][2] -
				matrix[0][0] * matrix[1][2] * matrix[2][1];
	}
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < numRows; i++){
			for(int j = 0; j < numCols; j++){
				sb.append(matrix[i][j]);
				sb.append(" ");
			}
			sb.append("\n");
		}
		return sb.toString();
	}
}
