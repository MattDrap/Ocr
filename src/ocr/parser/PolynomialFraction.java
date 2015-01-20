package ocr.parser;

/**
 *
 * @author Matt
 */
public class PolynomialFraction {
    Polynomial nominator;
    Polynomial denominator;
    PolynomialFraction(Polynomial nominator, Polynomial denominator){
        this.nominator = nominator;
        this.denominator = denominator;
    }
}
