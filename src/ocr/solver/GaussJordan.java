package ocr.solver;

import java.util.ArrayList;

/**
 *
 * @author Matt
 */
public class GaussJordan {
    protected Matrix matrix;
    protected Matrix b_side;
    public GaussJordan(Matrix matrix, Matrix b){
        if(matrix == null)
            throw new IllegalArgumentException();
        this.matrix = matrix;
        if(b_side != null){
	        if(matrix.numCols != b.numRows){
	        	if(matrix.numCols != b.numCols){
	        		throw new IllegalArgumentException("Matrix sizes must match");
	        	}else{
	        		b.transpose();
	        	}
	        }
        }
        b_side = b;
    }
    protected void Interchange(Matrix.Coordinate a, Matrix.Coordinate b) {
		ArrayList<Double> temp = matrix.getRow(a.row);
		matrix.setRow(a.row, matrix.getRow(b.row));		
		matrix.setRow(b.row, temp);
                
                if(b_side != null){
                    temp = b_side.getRow(a.row);
                    b_side.setRow(a.row, b_side.getRow(b.row));
                    b_side.setRow(b.row, temp);
                }
		int t = a.row;
		a.row = b.row;
		b.row = t;
	} 
 
    protected void Scale(Matrix.Coordinate x, double d) {
		ArrayList<Double> row = matrix.getRow(x.row);
		for (int i = 0; i < matrix.numCols; i++) {
			row.set(i, row.get(i)* d);
		}
		matrix.setRow(x.row, row);
                if(b_side != null){
                    row = b_side.getRow(x.row);
                    for(int i = 0; i < b_side.numCols; i++){
                        row.set(i, row.get(i) * d);
                    }
                    b_side.setRow(x.row, row);
                }
	}
 
    protected void MultiplyAndAdd(Matrix.Coordinate to, Matrix.Coordinate from, double scalar) {
		ArrayList<Double> row = matrix.getRow(to.row);
		ArrayList<Double> rowMultiplied = matrix.getRow(from.row);
 
		for (int i = 0; i < matrix.numCols; i++) {
			row.set(i, row.get(i) + (rowMultiplied.get(i) * scalar));
		}
		matrix.setRow(to.row, row);
                
                if(b_side != null){
                    row = b_side.getRow(to.row);
                    rowMultiplied = b_side.getRow(from.row);

                    for (int i = 0; i < b_side.numCols; i++) {
                            row.set(i, row.get(i) + (rowMultiplied.get(i) * scalar));
                    }
                    b_side.setRow(to.row, row);
                }
	}
 
	public void Solve() {
                Matrix.Coordinate pivot = new Matrix.Coordinate(0,0);
 
		int submatrix = 0;
		for (int x = 0; x < matrix.numCols; x++) {
			pivot = new Matrix.Coordinate(pivot.row, x);
			//Step 1
				//Begin with the leftmost nonzero column. This is a pivot column. The pivot position is at the top.
				for (int i = x; i < matrix.numCols; i++) {
					if (!matrix.isColumnZeroes(pivot)) {
						break;	
					} else {
						pivot.col = i;
					}
				}
			//Step 2
				//Select a nonzero entry in the pivot column with the highest absolute value as a pivot. 
				pivot = findPivot(pivot);
 
				if (matrix.getCoordinate(pivot) == 0.0) {
					pivot.row++;
					continue;
				}
 
				//If necessary, interchange rows to move this entry into the pivot position.
				//move this row to the top of the submatrix
				if (pivot.row != submatrix) {
					Interchange(new Matrix.Coordinate(submatrix, pivot.col), pivot);
				}
 
				//Force pivot to be 1
				if (matrix.getCoordinate(pivot) != 1) {
					/*
					System.out.println(getCoordinate(pivot));
					System.out.println(pivot);
					System.out.println(matrix);
					*/
					double scalar = 1/matrix.getCoordinate(pivot);
					Scale(pivot, scalar);
				}
			//Step 3
				//Use row replacement operations to create zeroes in all positions below the pivot.
				//belowPivot = belowPivot + (Pivot * -belowPivot)
				for (int i = pivot.row; i < matrix.numRows; i++) {
					if (i == pivot.row) {
						continue;
					}
					Matrix.Coordinate belowPivot = new Matrix.Coordinate(i, pivot.col);
					double complement = (-matrix.getCoordinate(belowPivot)/(matrix.getCoordinate(pivot)));
					MultiplyAndAdd(belowPivot, pivot, complement);
				}
			//Step 5
				//Beginning with the rightmost pivot and working upward and to the left, create zeroes above each pivot.
				//If a pivot is not 1, make it 1 by a scaling operation.
					//Use row replacement operations to create zeroes in all positions above the pivot
				for (int i = pivot.row; i >= 0; i--) {
					if (i == pivot.row) {
						if (matrix.getCoordinate(pivot) != 1.0) {
							Scale(pivot, 1/matrix.getCoordinate(pivot));	
						}
						continue;
					}
					if (i == pivot.row) {
						continue;
					}
 
					Matrix.Coordinate abovePivot = new Matrix.Coordinate(i, pivot.col);
					double complement = (-matrix.getCoordinate(abovePivot)/(matrix.getCoordinate(pivot)));
					MultiplyAndAdd(abovePivot, pivot, complement);
				}
			//Step 4
				//Ignore the row containing the pivot position and cover all rows, if any, above it.
				//Apply steps 1-3 to the remaining submatrix. Repeat until there are no more nonzero entries.
				if ((pivot.row + 1) >= matrix.numRows || matrix.isRowZeroes(new Matrix.Coordinate(pivot.row+1, pivot.col))) {
					break;
				}
 
				submatrix++;
				pivot.row++;
		}
	}
        protected Matrix.Coordinate findPivot(Matrix.Coordinate a) {
		int first_row = a.row;
		Matrix.Coordinate pivot = new Matrix.Coordinate(a);
		Matrix.Coordinate current = new Matrix.Coordinate(a);	
 
		for (int i = current.row; i < (matrix.numRows - first_row); i++) {
			current.row = i;
			if (matrix.getCoordinate(current) != 0) {
				pivot.row = i;
				break;
			}
		}
 
 
		return pivot;	
	}
        public String getSolution(ArrayList<String> variables){
            StringBuilder sb = new StringBuilder();
            for(int i = b_side.numRows - 1; i >= 0; i--){
                Matrix.Coordinate coord = new Matrix.Coordinate(i,0);
                if(matrix.isRowZeroes(coord)){
                    if(b_side.isRowZeroes(coord)){
                        return "Infinitely many solutions";
                    }else{
                        return "No solution";
                    }
                }else{
                    sb.append(variables.get(i));
                    sb.append("=");
                    sb.append(b_side.getRow(i).get(0));
                    sb.append(" ");
                }
            }
            return sb.toString();
        }
}
