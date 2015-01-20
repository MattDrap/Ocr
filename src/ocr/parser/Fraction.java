package ocr.parser;


import java.math.BigInteger;

/**
 *
 * @author Matt
 */
public class Fraction {
    public static Fraction ZERO = new Fraction(0,1);
    public static Fraction ONE = new Fraction(1,1);
    BigInteger nominator;
    BigInteger denominator;
    
    Fraction(){
        nominator = BigInteger.ONE;
        denominator = BigInteger.ONE;
    }
    Fraction(long num, long denom){
        nominator = BigInteger.valueOf(num);
        denominator = BigInteger.valueOf(denom);
        if(denominator == BigInteger.ZERO){
            throw new IllegalArgumentException();
        }
    }
    Fraction(String value) throws NumberFormatException{
        String[] split = value.split("\\.");
        if(split.length > 2){
            throw new NumberFormatException();
        }
        if(split.length == 1){
            nominator = new BigInteger(split[0]);
        }else{
            //Parse
            nominator = new BigInteger(split[0]);
            BigInteger tempnom = new BigInteger(split[1]);
            BigInteger tempdenom = BigInteger.TEN;
            tempdenom = tempdenom.pow(split[1].length());
            //Add
            denominator = tempdenom;
            nominator = nominator.multiply(tempdenom);
            nominator = nominator.add(BigInteger.ONE.multiply(tempnom));
            //Simplify
            BigInteger gcd = nominator.gcd(denominator);
            if(gcd != BigInteger.ONE){
                nominator = nominator.divide(gcd);
                denominator = denominator.divide(gcd);
            }
        }
    }
    
    Fraction mult(BigInteger num){
        Fraction f = new Fraction();
        f.nominator = f.nominator.multiply(num);
        f.simplify();
        return f;
    }
    Fraction mult(Fraction fract){
        Fraction f = new Fraction();
        f.nominator = nominator.multiply(fract.nominator);
        f.denominator = denominator.multiply(fract.denominator);
        f.simplify();
        return f;
    }
    Fraction div(BigInteger num){
        Fraction f = new Fraction();
        f.denominator = denominator.multiply(num);
        f.simplify();
        return f;
    }
    Fraction div(Fraction fract){
        Fraction f = new Fraction();
        f.nominator = nominator.multiply(fract.denominator);
        f.denominator = denominator.multiply(fract.nominator);
        f.simplify();
        return f;
    }
    Fraction plus(BigInteger num){
        Fraction f = new Fraction();
         f.nominator = nominator.add(denominator.multiply(num));
         f.simplify();
         return f;
    }
    Fraction plus(Fraction fract){
        Fraction f = new Fraction();
        BigInteger old_denom = denominator;
        f.denominator = denominator.multiply(fract.denominator);
        f.nominator = nominator.multiply(fract.denominator);
        f.nominator = f.nominator.add(old_denom.multiply(fract.nominator));
        f.simplify();
        return f;
    }
    Fraction sub(BigInteger num){
         return plus(num.negate());
    }
    Fraction sub(Fraction fract){
        fract.nominator = fract.nominator.negate();
        return plus(fract);
    }
    BigInteger gcd(BigInteger a,BigInteger b){
        return a.gcd(b);
    }
    void simplify(){
        BigInteger gcd = gcd(nominator, denominator);
        if(gcd != BigInteger.ONE){
            nominator = nominator.divide(gcd);
            denominator = denominator.divide(gcd);
        }
    }
    public boolean equals(Object o){
        if(o instanceof Fraction){
            Fraction f = (Fraction) o;
            return f.nominator.equals(nominator) && f.denominator.equals(denominator);
        }
        return false;
    }
    @Override
    public String toString(){
        return nominator.toString() + "/" + denominator.toString();
    }
    public double toDouble(){
        return nominator.doubleValue()/denominator.doubleValue();
    }
    public static Fraction parseFraction(String s) throws NumberFormatException{
        String [] fracts = s.split("/");
        Fraction f = new Fraction();
        boolean odd = true;
        for(String temp : fracts){
            try{
                BigInteger b = new BigInteger(temp);
                if(odd){    
                    f.mult(b);
                }else{
                    f.div(b);
                }
            }catch(NumberFormatException e){
                Fraction fin = new Fraction(temp);
                if(odd){    
                    f.mult(fin);
                }else{
                    f.div(fin);
                }
            }
            
            odd = !odd;
        }
        return f;
    }
}
