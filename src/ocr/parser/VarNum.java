package ocr.parser;


public class VarNum implements Comparable<VarNum>{
	double number;
	String variable;
	
	public double getNum() {
		return number;
	}
	public void setNum(double num) {
		number = num;
	}
	public String getVar() {
		return variable;
	}
	public void setVar(String var) {
		variable = var;
	}
        public String enpowerVar(double num){
            if(variable == null){
                return null;
            }
            int my_index = variable.indexOf('^');
            if(my_index == -1){
                if(num == 0){
                    return null;
                }
                if(num == 1){
                    return variable;
                }
                return String.format("%s^(%f)", variable, num);
            }else{
                double exp = Double.parseDouble(variable.substring(my_index+2, variable.length() - 1));
                if(exp*num == 0){
                    return null;
                }
                if(exp*num == 1){
                    return String.format("%s", variable.substring(0, my_index));
                }
                return String.format("%s^(%f)", variable.substring(0, my_index), exp*num);
            }
        }
        public double enpowerNum(double num){
            return Math.pow(number, num);
        }
        public String reduce(double num){
            int my_index = variable.indexOf("^");
            if(my_index == -1){
                return null;
            }else{
                double exp = Double.parseDouble(variable.substring(my_index+2, variable.length() - 1));
                return String.format("%s^(%f)", variable.substring(0, my_index), exp - num);
            }
        }
        public double distribute(double num, boolean positive){
            if(positive)
                return number*num;
            return number/num;
        }
        public String distribute(String var){
            if(variable == null && var == null)
                return null;
            if(variable == null)
                return var;
            if(var == null)
                return variable;
            
            int my_index = variable.indexOf('^');
            int outer_index = var.indexOf('^');
            if(my_index == -1){
                if(outer_index == -1){
                    if(variable.compareToIgnoreCase(var) == 0){
                        return String.format("%s^(2.0)", variable);
                    }
                }else{
                    if(variable.compareTo(var.substring(0, outer_index)) == 0){
                        double temp = Double.parseDouble(var.substring(outer_index+2, var.length() - 1));
                        temp++;
                        return String.format("%s^(%f)",variable, temp);
                    }
                }
            }else{
                if(outer_index == -1){
                    if(variable.substring(0, my_index).compareToIgnoreCase(var) == 0){
                        double temp = Double.parseDouble(variable.substring(my_index+2, variable.length() - 1));
                        return String.format("%s^(%f)", variable.substring(0,my_index) , temp + 1 );
                    }
                }else{
                    if(variable.substring(0, my_index).compareToIgnoreCase(var.substring(0, outer_index)) == 0){
                        double temp1 = Double.parseDouble(variable.substring(my_index+2,variable.length() - 1));
                        double temp2 = Double.parseDouble(var.substring(outer_index+2, var.length() - 1));
                        return String.format("%s^(%f)",variable.substring(0,my_index), temp1 + temp2);
                    }
                }
            }
            return String.format("%s*%s", variable, var);
        }
	public VarNum(double number, String variable){
		this.number = number;
		this.variable = variable;
	}
	public VarNum(){
		number = 1;
	}
	
	@Override
	public String toString(){
            String snum;
            if(number >= 0){
                snum = "+"+String.valueOf(number);
            }else{
                snum = String.valueOf(number);
            }
            if(variable != null){
		return String.format("%s*%s", snum,variable);
            }
            else{
                return snum;
            }
	}

    @Override
    public int compareTo(VarNum o) {
        if(variable == null && o.variable == null)
            return 0;
        if(variable == null)
            return 1;
        if(o.variable == null)
            return -1;
        int my_index = variable.indexOf("^");
        int outer_index = o.variable.indexOf("^");
        
        if(my_index == -1){
            if(outer_index == -1){
                return 0;
            }else{
                return 1;
            }
        }else{
            if(outer_index == -1){
                return -1;
            }else{
                double my_sup = Double.parseDouble(variable.substring(my_index+2, variable.length() - 1));
                double outer_sup = Double.parseDouble(o.variable.substring(outer_index + 2, o.variable.length() - 1));
                if(my_sup == outer_sup){
                    return 0;
                }
                if(my_sup > outer_sup){
                    return -1;
                }else{
                    return 1;
            }
                
            }
        }
    }
}

