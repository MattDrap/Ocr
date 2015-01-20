package ocr.parser;
/**
 *
 * @author Matt
 */
public class PolynomialFractionAndRemainder {
    Polynomial polynomial;
    PolynomialFraction remainder;
    PolynomialFractionAndRemainder(Polynomial polynomial, PolynomialFraction remainder){
        this.polynomial = polynomial;
        this.remainder = remainder;
    }
}
