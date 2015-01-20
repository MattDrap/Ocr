package ocr.solver;

import java.util.ArrayList;
import java.util.LinkedList;

import ocr.parser.ExpressionNode;
import ocr.parser.Parser;
import ocr.parser.Token;
import ocr.solver.helper.ColumnAndTokens;
import ocr.solver.helper.RemovePair;
import ocr.solver.helper.TokenHelper;
import ocr.solver.helper.Utils;

public class LinearEquationsSolver implements IExampleSolver {
	private Matrix Matrix = null;
	private Matrix rightHandSide = null;
	Parser parser;
	ArrayList<Equation> list_equations;
	ArrayList<String> variables;

	public LinearEquationsSolver(ArrayList<Equation> list_equations) {
		parser = Parser.parser;
		this.list_equations = list_equations;
	}

	private ArrayList<String> analyze(ArrayList<Equation> equations) {
		ArrayList<String> variables = new ArrayList<>();
		ArrayList<String> eq_vars;
		for (Equation equation : equations) {
			if (equation.getRight_side() != null) {
				LinkedList<Token> temp = Utils.DeepCopySubList(equation
						.getLeft_side());
				LinkedList<Token> temp2 = Utils.DeepCopySubList(equation
						.getRight_side());
				temp.addAll(temp2);
				eq_vars = analyze(temp);
			} else {
				eq_vars = analyze(equation.getLeft_side());
			}
			for (String var : eq_vars) {
				if (find_Variable(variables, var) < 0) {
					variables.add(var);
				}
			}
		}
		return variables;
	}

	private ArrayList<String> analyze(LinkedList<Token> tokens) {
		ArrayList<String> strings = new ArrayList<>();
		for (Token token : tokens) {
			if (token.token == Token.VARIABLE) {
				strings.add(token.sequence);
			}
		}
		return strings;
	}

	private int find_Variable(ArrayList<String> vars, String var_sequence) {
		for (int i = 0; i < vars.size(); i++) {
			if (var_sequence.toLowerCase().compareToIgnoreCase(vars.get(i)) == 0) {
				return i;
			}
		}
		return -1;
	}
	private ColumnAndTokens detectLinearColumnMembers(
			LinkedList<Token> left_side, ArrayList<String> vars) {
		double[] column_mem = new double[vars.size()];
		ArrayList<RemovePair> list_of_removes = new ArrayList<RemovePair>();
		for (int i = 0; i < left_side.size(); i++) {
			if (left_side.get(i).token == Token.VARIABLE) {
				int index = find_Variable(vars, left_side.get(i).sequence);
				if (index >= 0) {
					int pos = TokenHelper.detectAdditionToken(left_side, i);
					if (pos >= 0) {
						list_of_removes.add(new RemovePair(pos, i + 1));
						LinkedList<Token> subtokens = Utils.DeepCopySubList(
								left_side, pos, i);
							subtokens.add(new Token(Token.NUMBER, "1", i));
						ExpressionNode exp = parser.evaluate(subtokens);
						String s = exp.getValue();
						double d = Double.parseDouble(s);
						column_mem[index] += d;
					}
				}
			}
		}
		left_side = TokenHelper.removeMembers(list_of_removes, left_side);
		return new ColumnAndTokens(left_side, null, column_mem);
	}
	
	private ColumnAndTokens detectLinearColumnMembers(
			LinkedList<Token> left_side, LinkedList<Token> right_side,
			ArrayList<String> vars) {
		double[] column_mem = new double[vars.size()];
		ArrayList<RemovePair> list_of_removes = new ArrayList<RemovePair>();
		for (int i = 0; i < left_side.size(); i++) {
			if (left_side.get(i).token == Token.VARIABLE) {
				int index = find_Variable(vars, left_side.get(i).sequence);
				if (index >= 0) {
					int pos = TokenHelper.detectAdditionToken(left_side, i);
					if (pos >= 0) {
						list_of_removes.add(new RemovePair(pos, i + 1));
						LinkedList<Token> subtokens = Utils.DeepCopySubList(
								left_side, pos, i);
						subtokens.add(new Token(Token.NUMBER, "1", i));
						ExpressionNode exp = parser.evaluate(subtokens);
						String s = exp.getValue();
						double d = Double.parseDouble(s);
						column_mem[index] += d;
					}
				}
			}
		}
		left_side = TokenHelper.removeMembers(list_of_removes, left_side);
		list_of_removes.clear();
		for (int i = 0; i < right_side.size(); i++) {
			if (right_side.get(i).token == Token.VARIABLE) {
				int index = find_Variable(vars, right_side.get(i).sequence);
				if (index >= 0) {
					int pos = TokenHelper.detectAdditionToken(right_side, i);
					if (pos >= 0) {
						list_of_removes.add(new RemovePair(pos, i + 1));
						LinkedList<Token> subtokens = Utils.DeepCopySubList(
								right_side, pos, i);
						subtokens.add(new Token(Token.NUMBER, "1", i));
						ExpressionNode exp = parser.evaluate(subtokens);
						String s = exp.getValue();
						double d = Double.parseDouble(s);
						column_mem[index] -= d;
					}
				}
			}
		}
		right_side = TokenHelper.removeMembers(list_of_removes, right_side);
		return new ColumnAndTokens(left_side, right_side, column_mem);
	}

	@Override
	public String calculate() throws NotImplementedException {	
		try{
			GaussJordan gj = new GaussJordan(Matrix, rightHandSide);
			gj.Solve();
			return gj.getSolution(variables);
		} catch (Exception e) {
			throw new NotImplementedException("Not implemented yet"
					+ e.getMessage());
		}
	}

	@Override
	public void prepare() {
		variables = analyze(list_equations);
		double[][] all_members = new double[list_equations.size()][variables
				.size()];
		double[] right_side = new double[list_equations.size()];
		for (int i = 0; i < list_equations.size(); i++) {
			ColumnAndTokens colAndtok;
			if (list_equations.get(i).getRight_side() != null) {
				colAndtok = detectLinearColumnMembers(list_equations.get(i)
						.getLeft_side(), list_equations.get(i).getRight_side(),
						variables);
				list_equations.get(i).setRight_side(colAndtok.Right_Tokens);
			} else {
				colAndtok = detectLinearColumnMembers(list_equations.get(i)
						.getLeft_side(), variables);
			}
			list_equations.get(i).setLeft_side(colAndtok.Left_Tokens);
			all_members[i] = colAndtok.Column;
			if (!list_equations.get(i).getLeft_side().isEmpty()) {
				ExpressionNode exp = parser.evaluate(list_equations.get(i)
						.getLeft_side());
				right_side[i] -= Double.parseDouble(exp.getValue());
			}
			if (list_equations.get(i).getRight_side() != null) {
				if (!list_equations.get(i).getRight_side().isEmpty()) {
					ExpressionNode exp = parser.evaluate(list_equations.get(i)
							.getRight_side());
					double member = Double.parseDouble(exp.getValue());
					right_side[i] += member;
				}
			}
		}
		Matrix = new Matrix(all_members);
		rightHandSide = new Matrix(right_side);	
	}
}
