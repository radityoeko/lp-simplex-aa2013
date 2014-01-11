package it.unibz.inf.aa.ajrep.core;

import static java.lang.Math.abs;

/**
 * This class describes Fraction. Fractions are used in equation calculation.
 * 
 * @author Andrius
 */
public class Fraction {
    private int num, den;
    
    public Fraction () {
    }
    
    public Fraction (double d) {
        boolean sign = d < 0 ? true : false;
        d = abs(d);
        String s = String.valueOf(d);
        int digitsDec = s.length() - 1 - s.indexOf('.');        

        int denominator = 1;
        for(int i = 0; i < digitsDec; i++){
           d *= 10;
           denominator *= 10;
        }
        int numerator = (int) Math.round(d);
        
        this.num = sign ? (-1) * numerator : numerator;
        this.den = denominator;
    }
    
    /**
     * Sets numerator & denominator of fraction.
     * 
     * @param num
     * @param den 
     */
    public Fraction (int num, int den) {
        this.num = num;
        this.den = den;
    }
    
    /**
     * 
     * @return numerator of fraction
     */
    public int getNumerator() {
        return num;
    }
    
    /**
     * 
     * @return denominator of fraction
     */
    public int getDenomintor() {
        return den;
    }
    
    /**
     * Sets numerator of fraction.
     * 
     * @param num 
     */
    public void setNumerator(int num) {
        this.num = num;
    }
    
    /**
     * Sets denominator of fraction.
     * 
     * @param den 
     */
    public void setDenominator(int den) {
        this.den = den;
    }
}
