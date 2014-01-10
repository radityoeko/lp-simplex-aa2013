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
public class Fraction {
    private int num, den;
    
    public Fraction () {
    }
    
    public Fraction (int num, int den) {
        this.num = num;
        this.den = den;
    }
    
    public int getNumerator() {
        return num;
    }
    
    public int getDenomintor() {
        return den;
    }
    
    public void setNumerator(int num) {
        this.num = num;
    }
    
    public void setDenominator(int den) {
        this.den = den;
    }
}
