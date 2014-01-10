/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package it.unibz.inf.aa.ajrep.core;

/**
 *
 * @author Andrius
 */
public class FractionCalculator {
    public FractionCalculator() {
    }
    
    public Fraction add(Fraction f1, Fraction f2) {
        Fraction f = new Fraction();
        f.setNumerator(f1.getNumerator() * f2.getDenomintor() + f2.getNumerator() * f1.getDenomintor());
        f.setDenominator(f1.getDenomintor() * f2.getDenomintor());
        
        return this.simplify(f);
    }
    
    public Fraction sub(Fraction f1, Fraction f2) {
        Fraction f = new Fraction();
        f.setNumerator(f1.getNumerator() * f2.getDenomintor() - f2.getNumerator() * f1.getDenomintor());
        f.setDenominator(f1.getDenomintor() * f2.getDenomintor());
        
        return this.simplify(f);
    }
    
    public Fraction mul(Fraction f1, Fraction f2) {
        Fraction f = new Fraction();
        f.setNumerator(f1.getNumerator() * f2.getNumerator());
        f.setDenominator(f1.getDenomintor() * f2.getDenomintor());
        
        return this.simplify(f);
    }
    
    public Fraction div(Fraction f1, Fraction f2) {
        Fraction f = new Fraction();
        f.setNumerator(f1.getNumerator() * f2.getDenomintor());
        f.setDenominator(f1.getDenomintor() * f2.getNumerator());
        
        return this.simplify(f);
    }
    
    private Fraction simplify(Fraction f) {
        
        int num = f.getNumerator();
        int den = f.getDenomintor();
        
        int n = num < den ? num : den;
        
        for (int i = 1; i < (n / 2); i++) {
            if (num % i == 0 && den % i == 0) {
                num = num / i;
                den = den /i;
            }
        }
        
        if (num == 0) {den = 0;}
        if (num == den) {num = 1; den = 1;}
        
        return new Fraction(num, den);
    }
}
