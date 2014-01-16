package it.unibz.inf.aa.ajrep.core;

import static java.lang.Math.abs;

/**
 * This class describes Fraction. Fractions are used in equation calculation.
 *
 * @author Andrius
 */
public class Fraction {

    private int num, den;
    public static final Fraction ONE = new Fraction(1, 1);
    public static final Fraction ZERO = new Fraction(0, 1);
    public static final Fraction MIN = new Fraction(-1, 1);

    public Fraction() {
    }

    /**
     * Creates Fraction from double variable.
     *
     * @param d
     */
    public Fraction(double d) {
        boolean sign = d < 0 ? true : false;
        d = abs(d);
        String s = String.valueOf(d);
        int digitsDec = s.length() - 1 - s.indexOf('.');

        int denominator = 1;
        for (int i = 0; i < digitsDec; i++) {
            d *= 10;
            denominator *= 10;
        }
        int numerator = (int) Math.round(d);

        this.num = sign ? (-1) * numerator : numerator;
        this.den = denominator;

        this.simplify();
    }

    /**
     * Sets numerator & denominator of fraction.
     *
     * @param num
     * @param den
     */
    public Fraction(int num, int den) {
        this.num = num;
        this.den = den;

        this.simplify();
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

    /**
     * Simplifies Fraction.
     *
     */
    public void simplify() {
        if (num == 0) {
            den = 1;
        } else {
            boolean sign = this.isNegative() ? true : false;
            num = abs(num);
            den = abs(den);

            int n = num < den ? num : den;

            for (int i = n; i > 0; i--) {
                if (num % i == 0 && den % i == 0) {
                    num = num / i;
                    den = den / i;
                    break;
                }
            }

            if (num == 0) {
                den = 0;
            }
            if (num == den && num != 0 && den != 0) {
                num = 1;
                den = 1;
            }

            num = sign ? (-1) * num : num;
        }
    }

    /**
     *
     * @return true if negative, false if not negative
     */
    public boolean isNegative() {
        if (num < 0 ^ den < 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     *
     * @return true if fraction is zero, false if fraction not zero
     */
    public boolean isZero() {
        if (num == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     *
     * @return true if negative, false if not negative
     */
    public boolean isPositive() {
        return !this.isNegative() && !this.isZero();
    }

    /**
     * Changes fraction sign.
     */
    public void changeSign() {
        num = num * -1;
    }

    /**
     * @return Fraction like string
     */
    public String toString() {
        if (num == 0) {
            return "0";
        } else if (num == den) {
            return "1";
        } else if (den == 1) {
            return num + "";
        } else {
            return num + "/" + den;
        }
    }

    /**
     * Adds Fraction this & Fraction f2.
     *
     * @param this
     * @param f2
     * @return
     */
    public Fraction add(Fraction f2) {
        if (this.isZero()) {
            return f2;
        } else if (f2.isZero()) {
            return this;
        }

        Fraction f = new Fraction();
        f.setNumerator(this.getNumerator() * f2.getDenomintor()
                + f2.getNumerator() * this.getDenomintor());
        f.setDenominator(this.getDenomintor() * f2.getDenomintor());

        f.simplify();
        return f;
    }

    /**
     * From Fraction this subtracts Fraction f2.
     *
     * @param this
     * @param f2
     * @return Fraction
     */
    public Fraction sub(Fraction f2) {
        if (this.isZero()) {
            f2.changeSign();
            return f2;
        } else if (f2.isZero()) {
            return this;
        }

        Fraction f = new Fraction();
        f.setNumerator(this.getNumerator() * f2.getDenomintor()
                - f2.getNumerator() * this.getDenomintor());
        f.setDenominator(this.getDenomintor() * f2.getDenomintor());

        f.simplify();
        return f;
    }

    /**
     * Multiplies Fraction this & Fraction f2.
     *
     * @param this
     * @param f2
     * @return Fraction
     */
    public Fraction mul(Fraction f2) {
        Fraction f = new Fraction();
        f.setNumerator(this.getNumerator() * f2.getNumerator());
        f.setDenominator(this.getDenomintor() * f2.getDenomintor());

        f.simplify();
        return f;
    }

    /**
     * Divides Fraction this from Fraction f2
     *
     * @param this
     * @param f2
     * @return Fraction
     */
    public Fraction div(Fraction f2) {
        Fraction f = new Fraction();
        f.setNumerator(this.getNumerator() * f2.getDenomintor());
        f.setDenominator(this.getDenomintor() * f2.getNumerator());

        f.simplify();
        return f;
    }

    public Fraction inverse() {
        return new Fraction(-this.num, this.den);
    }

    public Double toDouble() {
        return new Double((double) this.num / (double) this.den);
    }

    public boolean lt(Fraction max) {
        return this.toDouble() < max.toDouble();
    }

}
