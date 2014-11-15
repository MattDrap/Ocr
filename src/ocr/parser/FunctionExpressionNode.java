package ocr.parser;

public class FunctionExpressionNode implements ExpressionNode
	{
	/** function id for the sin function */
	public static final int SIN = 1;
	/** function id for the cos function */
	public static final int COS = 2;
	/** function id for the tan function */
	public static final int TAN = 3;
	/** function id for the asin function */
	public static final int ASIN = 4;
	/** function id for the acos function */
	public static final int ACOS = 5;
	/** function id for the atan function */
	public static final int ATAN = 6;
	/** function id for the sqrt function */
	public static final int SQRT = 7;
	/** function id for the exp function */
	public static final int EXP = 8;
	/** function id for the ln function */
	public static final int LN = 9;
	/** function id for the log function */
	public static final int LOG = 10;
	/** function id for the log2 function */
	public static final int LOG2 = 11;
	/** the id of the function to apply to the argument */
	private int function;
	/** the argument of the function */
	private ExpressionNode argument;
	/**
	* Construct a function by id and argument.
	*
	* @param function
	* the id of the function to apply
	* @param argument
	* the argument of the function
	*/
	public FunctionExpressionNode(int function, ExpressionNode argument)
	{
	super();
	this.function = function;
	this.argument = argument;
	}
	/**
	* Returns the type of the node, in this case ExpressionNode.FUNCTION_NODE
	*/
        @Override
	public int getType()
	{
	return ExpressionNode.FUNCTION_NODE;
	}
        public static String functionToString(int function){
            switch(function){
               case 1: return "sin";
               case 2: return "cos";
               case 3: return "tan";
               case 4: return "asin";
               case 5: return "acos";
               case 6: return "atan";
               case 7: return "sqrt";
               case 8: return "exp";
               case 9: return "ln";
               case 10: return "log";
               case 11: return "log2";                
            }
            throw new ParserException("Unexpected Function " + function + " found");
        }
	/**
	* Converts a string to a function id.
	*
	* If the function is not found this method throws an error.
	*
	* @param str
	* the name of the function
	* @return the id of the function
	*/
	public static int stringToFunction(String str)
	{
	if (str.equals("sin"))
            return FunctionExpressionNode.SIN;
	if (str.equals("cos"))
            return FunctionExpressionNode.COS;
	if (str.equals("tan"))
            return FunctionExpressionNode.TAN;
	if (str.equals("asin"))
            return FunctionExpressionNode.ASIN;
	if (str.equals("acos"))
            return FunctionExpressionNode.ACOS;
	if (str.equals("atan"))
            return FunctionExpressionNode.ATAN;
	if (str.equals("sqrt"))
            return FunctionExpressionNode.SQRT;
	if (str.equals("exp"))
            return FunctionExpressionNode.EXP;
	if (str.equals("ln"))
            return FunctionExpressionNode.LN;
	if (str.equals("log"))
            return FunctionExpressionNode.LOG;
	if (str.equals("log2"))
            return FunctionExpressionNode.LOG2;
	throw new ParserException("Unexpected Function " + str + " found");
	}
	/**
	* Returns a string with all the function names concatenated by a | symbol.
	*
	* This string is used in Tokenizer.createExpressionTokenizer to create a
	* regular expression for recognizing function names.
	*
	* @return a string containing all the function names
	*/
	public static String getAllFunctions()
	{
            return "sin|cos|tan|asin|acos|atan|sqrt|exp|ln|log|log2";
	}
	/**
	* Returns the value of the sub-expression that is rooted at this node.
	*
	* The argument is evaluated and then the function is applied to the resulting
	* value.
	*/
        @Override
	public String getValue()
	{
            double arg;
            try{
                arg = Double.parseDouble(argument.getValue());
                switch (function)
                {
                    case SIN:
                        return  String.valueOf(Math.sin(arg));
                    case COS:
                        return String.valueOf(Math.cos(arg));
                    case TAN:
                        return String.valueOf(Math.tan(arg));
                    case ASIN:
                        return String.valueOf(Math.asin(arg));
                    case ACOS:
                        return String.valueOf(Math.acos(arg));
                    case ATAN:
                        return String.valueOf(Math.atan(arg));
                    case SQRT:
                        return String.valueOf(Math.sqrt(arg));
                    case EXP:
                        return String.valueOf(Math.exp(arg));
                    case LN:
                        return String.valueOf(Math.log(arg));
                    case LOG:
                        return String.valueOf(Math.log(arg) * 0.43429448190325182765);
                    case LOG2:
                        return String.valueOf(Math.log(arg) * 1.442695040888963407360);
                }
                throw new EvaluationException("Invalid function id "+function+"!");
            }catch(NumberFormatException e){
                return functionToString(function)+"("+argument.getValue()+")";
            }
        }
	/**
	* Implementation of the visitor design pattern.
	*
	* Calls visit on the visitor and then passes the visitor on to the accept
	* method of the argument.
	*
	* @param visitor
	* the visitor
	*/
	public void accept(ExpressionNodeVisitor visitor)
	{
	visitor.visit(this);
	argument.accept(visitor);
	}
}
