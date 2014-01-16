package it.unibz.inf.aa.ajrep.ui;

import it.unibz.inf.aa.ajrep.core.Fraction;
import it.unibz.inf.aa.ajrep.core.FractionCalculator;

/**
 *
 * @author Andrius
 */
public class Function {

    public Fraction fx, fy, fa;
    private FractionCalculator calculator;

    public Function(Fraction x, Fraction y, Fraction a) {
        this.fx = x;
        this.fy = y;
        this.fa = a;
        calculator = new FractionCalculator();
    }

    public Fraction getY(Fraction x) {
        Fraction tmpX = calculator.mul(fx, x);
        Fraction tmpA = calculator.sub(fa, tmpX);
        Fraction y = calculator.div(tmpA, fy);
        y.simplify();
        return y;
    }

    public Fraction getX(Fraction y) {
        Fraction tmpY = calculator.mul(fy, y);
        Fraction tmpA = calculator.sub(fa, tmpY);
        Fraction x = calculator.div(tmpA, fx);
        x.simplify();
        return x;
    }

    @Override
    public String toString() {
        return fx + "x + " + fy + "y = " + fa;
    }
}
