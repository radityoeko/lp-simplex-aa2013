package it.unibz.inf.aa.ajrep.core;

/**
 * This class describes Fraction. Fractions are used in equation calculation.
 * 
 * @author Andrius
 */
public class Fraction {
    private int num, den;
    
    public Fraction () {
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
