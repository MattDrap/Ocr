package ocr.solver;


public abstract class SolverBase {
	public abstract String solve(String[] OcrTexts);
	public abstract String getType();
	
	public static SolverBase getSolver(boolean algebra, boolean superscripts, int option){
		if(algebra){
			return new AlgebraSolver(superscripts);
		}else{
			return new MatrixSolver(option);
		}
	}
}
