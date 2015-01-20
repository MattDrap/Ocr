package ocr.parser;


import java.math.BigInteger;
import java.util.ArrayList;

/**
 *
 * @author Matt
 */
public class Polynomial {
    Fraction [] members;
    int degree;
    String variable;
    Polynomial(int max_polynom, String variable_name){
        if(variable_name == null)
            throw new IllegalArgumentException();
        members = new Fraction[max_polynom + 1];
        variable = variable_name;
        degree = max_polynom;
        for(int i = 0; i < degree + 1; i++){
            members[i] = Fraction.ZERO;
        }
    }
    void addMember(Fraction f, int degree){
        members[degree] = members[degree].plus(f);
    }
    void addMember(BigInteger t, int degree){
        members[degree] = members[degree].plus(t);
    }
    PolynomialFractionAndRemainder divide(Polynomial p){
        if(!variable.equalsIgnoreCase(p.variable))
            throw new IllegalArgumentException();
        if(p.degree > degree)
            throw new IllegalArgumentException();
        Fraction [] my_members = members;
        Polynomial result = new Polynomial(degree - p.degree, variable);
        int temp_degree = degree;
        while(temp_degree >= p.degree){
            Fraction t = my_members[temp_degree];
            if(t == Fraction.ZERO)
                continue;
            t = t.div(p.members[p.degree]);
            result.addMember(t, temp_degree - p.degree);
            ArrayList<Fraction> fracts = new ArrayList<>();
            for(int i = 0; i < p.degree + 1; i++){
                Fraction f2 = p.members[i];
                f2 = f2.mult(t);
                fracts.add(f2);
            }
            int fract_increaser = temp_degree - fracts.size() + 1;
            for(Fraction fract2 : fracts){
                my_members[fract_increaser] = my_members[fract_increaser].sub(fract2);
                fract_increaser++;
            }
            temp_degree--;
        }
        boolean first = true;
        Polynomial remainder = null;
        for(int i = degree; i >= 0; i--){
            if(!my_members[i].equals(Fraction.ZERO)){
                if(first){
                    remainder = new Polynomial(i, variable);
                    remainder.addMember(my_members[i], i);
                    first = false;
                }else{
                    remainder.addMember(my_members[i], i);
                }
            }
        }
        if(remainder != null){
            System.out.println("Remainder");
            System.out.println(remainder.toString());
            System.out.println("Result");
            System.out.println(result.toString());
            Polynomial remainder_denom = new Polynomial(p.degree, p.variable);
            remainder_denom.members = p.members;
            PolynomialFraction pf = new PolynomialFraction(remainder, remainder_denom);
            return new PolynomialFractionAndRemainder(result, pf);
        }else{
            return new PolynomialFractionAndRemainder(result, null);
        }
    }
    
    @Override
    public boolean equals(Object o){
        if(o instanceof Polynomial){
            Polynomial p = (Polynomial) o;
            if(variable.equalsIgnoreCase(p.variable) && degree == p.degree){
                for(int i = 0; i <= degree; i++){
                    if(members[i] != p.members[i])
                        return false;
                }
                return true;
            }
        }
        return false;
    }
    @Override
    public String toString(){
       StringBuilder sb = new StringBuilder();
       int deg = 2;
       for(int i = 0; i <= degree; i++){
           if(members[i].equals(Fraction.ZERO))
               continue;
           sb.append(members[i].toString());
           if(i > 0){
               sb.append("*");
               sb.append(variable);
            if(i > 1){
                sb.append("^(");
                sb.append(deg);
                sb.append(")");
                deg++;
            }
           }
           if(i + 1 <= degree){
            if(members[i + 1].nominator.compareTo(BigInteger.ZERO) >= 0){
                sb.append("+");
            }
           }
       }
       if(sb.length() > 0){
        return sb.toString();
       }
       return "";
    }
    public ArrayList<VarNum> toVarNums(){
        ArrayList<VarNum> result = new ArrayList<>();
        for(int i = 0; i <= degree; i++){
            VarNum vn = new VarNum();
            vn.setNum(members[i].toDouble());
            if(i > 0){
              vn.setVar(variable);
              if(i > 1){
                  double val = (double) i;
                  vn.setVar(vn.getVar()+"^("+val+")");
              }  
            }
            result.add(vn);      
        }
        return result;
    }
}

