package ocr.parser;

public class StringOrNumber {
    String s = "";
    double d = 0;
    boolean changedNumber = false;
    void setNumber(double number){
        d = number;
        s = String.valueOf(d);
        changedNumber = true;
    }
    void setText(String text){
        s = text;
        changedNumber = false;
    }
    public static StringOrNumber parseExpression(ExpressionNode exp){
            StringOrNumber son = new StringOrNumber();
            String value = exp.getValue();
                try{
                    son.setNumber(Double.parseDouble(value));
                }catch(NumberFormatException e){
                    son.setText(value);
                }
            return son;
        }
}
