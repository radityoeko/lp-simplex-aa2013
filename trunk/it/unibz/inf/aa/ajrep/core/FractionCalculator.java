package it.unibz.inf.aa.ajrep.core;

import static java.lang.Math.abs;

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
        Fraction f = new Fraction();
        f.setNumerator(f1.getNumerator() * f2.getDenomintor() + f2.getNumerator() * f1.getDenomintor());
        f.setDenominator(f1.getDenomintor() * f2.getDenomintor());
        
        return this.simplify(f);
    }
    
    /**
     * From Fraction f1 subtracts Fraction f2.
     * 
     * @param f1
     * @param f2
     * @return Fraction
     */
    public Fraction sub(Fraction f1, Fraction f2) {
        Fraction f = new Fraction();
        f.setNumerator(f1.getNumerator() * f2.getDenomintor() - f2.getNumerator() * f1.getDenomintor());
        f.setDenominator(f1.getDenomintor() * f2.getDenomintor());
        
        return this.simplify(f);
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
        
        return this.simplify(f);
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
        
        return this.simplify(f);
    }
    
    /**
     * Simplify inputed Fraction.
     * 
     * @param f
     * @return Fraction
     */
    private Fraction simplify(Fraction f) {
        int num = f.getNumerator();
        int den = f.getDenomintor();
        
        boolean sign = false;
        if ((num < 0) ^ (den < 0)) {sign = true;}
        num = abs(num);
        den = abs(den);
        
        int n = num < den ? num : den;

        for (int i = n; i > 0; i--) {
            if (num % i == 0 && den % i == 0) {
                num = num / i;
                den = den /i;
                break;
            }
        }
        
        if (num == 0) {den = 0;}
        if (num == den) {num = 1; den = 1;}
        
        return sign ? new Fraction((-1)*num, den) : new Fraction(num, den);
    }
}
