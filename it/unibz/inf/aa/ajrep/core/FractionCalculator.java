package it.unibz.inf.aa.ajrep.core;

/**
 * This class is for make calculations with fractions.
 * 
 * @author Andrius
 */
public class FractionCalculator {
    public FractionCalculator() {
    }
    
    /**
     * Adds Fraction f1 & Fraction f2.
     * 
     * @param f1
     * @param f2
     * @return 
     */
    public Fraction add(Fraction f1, Fraction f2) {
        if (f1.isZero()) {
            return f2;
        } else if (f2.isZero()) {
            return f1;
        }
        
        Fraction f = new Fraction();
        f.setNumerator(f1.getNumerator() * f2.getDenomintor() + f2.getNumerator() * f1.getDenomintor());
        f.setDenominator(f1.getDenomintor() * f2.getDenomintor());
        
        f.simplify();
        return f;
    }
    
    /**
     * From Fraction f1 subtracts Fraction f2.
     * 
     * @param f1
     * @param f2
     * @return Fraction
     */
    public Fraction sub(Fraction f1, Fraction f2) {
        if (f1.isZero()) {
            return this.mul(Fraction.MIN, f2);
        } else if (f2.isZero()) {
            return f1;
        }
        
        Fraction f = new Fraction();
        f.setNumerator(f1.getNumerator() * f2.getDenomintor() - f2.getNumerator() * f1.getDenomintor());
        f.setDenominator(f1.getDenomintor() * f2.getDenomintor());
        
        f.simplify();
        return f;
    }
    
    /**
     * Multiplies Fraction f1 & Fraction f2.
     * 
     * @param f1
     * @param f2
     * @return Fraction
     */
    public Fraction mul(Fraction f1, Fraction f2) {
        Fraction f = new Fraction();
        f.setNumerator(f1.getNumerator() * f2.getNumerator());
        f.setDenominator(f1.getDenomintor() * f2.getDenomintor());
        
        f.simplify();
        return f;
    }
    
    /**
     * Divides Fraction f1 from Fraction f2
     * 
     * @param f1
     * @param f2
     * @return Fraction
     */
    public Fraction div(Fraction f1, Fraction f2) {
        Fraction f = new Fraction();
        f.setNumerator(f1.getNumerator() * f2.getDenomintor());
        f.setDenominator(f1.getDenomintor() * f2.getNumerator());
        
        f.simplify();
        return f;
    }
    
    public int compare(Fraction f1, Fraction f2) {
        int den = f1.getDenomintor() * f2.getDenomintor();
        int num1 = f1.getNumerator() * f2.getDenomintor();
        int num2 = f2.getNumerator() * f1.getDenomintor();
        
        if (num1 > num2) {
            return 1;
        } else if (num1 < num2) {
            return -1;
        } else {
            return 0;
        }
    }
}
