package it.unibz.inf.aa.ajrep.ui;

import it.unibz.inf.aa.ajrep.core.Fraction;
import it.unibz.inf.aa.ajrep.core.FractionCalculator;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JPanel;

/**
 *
 * @author Andrius
 */
public class Output extends JFrame {
    
    public Output(int varCount, ArrayList<Fraction> objectiveF, ArrayList<ArrayList<Fraction>> cMatrix) {
        this.setSize(500, 600);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setTitle("AA Linear Programming");

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);

        DrawPane drawPane = new DrawPane(varCount, objectiveF, cMatrix);
        drawPane.setLayout(null);
        drawPane.setBounds(5, 5, 500, 300);
        mainPanel.add(drawPane);

        this.add(mainPanel);
    }
    
    class Function {
        Fraction fx, fy, fa;
        FractionCalculator calculator;
        
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
    }

    class DrawPane extends JPanel {
        int varCount;
        ArrayList<Fraction> objectiveF;
        ArrayList<ArrayList<Fraction>> cMatrix;
        
        public DrawPane(int varCount, ArrayList<Fraction> objectiveF, ArrayList<ArrayList<Fraction>> cMatrix) {
            this.varCount = varCount;
            this.objectiveF = objectiveF;
            this.cMatrix = cMatrix;
        }

        public double round(double value) {
            return (double)Math.round(value * 10) / 10;
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            //x line
            g.drawLine(10, 150, 480, 150);
            
            //y line
            g.drawLine(100, 10, 100, 250);

            //x arow
            g.drawLine(470, 145, 480, 150);
            g.drawLine(470, 155, 480, 150);
            
            //y arrow
            g.drawLine(95, 20, 100, 10);
            g.drawLine(105, 20, 100, 10);
            
            //zero coordinates
            final int zeroX = 100;
            final int zeroY = 150;
            final int scale = 30; //10px = 1

            g.drawString("X", 470, 140);
            g.drawString("Y", 110, 20);
            
            //variables of function
            Fraction fx;
            Fraction fy;
            Fraction fa;
            Function f;
            ArrayList<Fraction> function;
            if (this.varCount == 2) {
                for (int i = 0; i < this.cMatrix.size(); i++) {
                    //select function to draw
                    function = cMatrix.get(i);
                    
                    System.out.println("i: "+i);
                    System.out.println(function.get(0).toString());
                    System.out.println(function.get(1).toString()); 
                    System.out.println(function.get(2).toString());
                    
                    fx = function.get(0);
                    fy = function.get(1);
                    fa = function.get(2);
                    f = new Function(fx, fy, fa);
                    double X1 = 0, Y1 = 0, X2 = 0, Y2 = 0;

                    if (!fx.isZero() && !fy.isZero()) {
                        //const point selecting
                        if (fx.isPositive() && fy.isPositive() && fa.isPositive() ||
                                fx.isNegative() && fy.isNegative() && fa.isNegative()) {
                            Y1 = -5;
                            X2 = -5;
                        } else if (fx.isNegative()&& fy.isNegative()&& fa.isPositive() ||
                                fx.isPositive() && fy.isPositive()&& fa.isNegative()) {
                            Y1 = 5;
                            X2 = 5;
                        } else if (fx.isNegative() && fy.isPositive() && fa.isPositive() ||
                                fx.isPositive()&& fy.isNegative()&& fa.isPositive()) {
                            Y1 = -5;
                            X2 = 5;
                        } else if (fx.isPositive()&& fy.isNegative()&& fa.isNegative() ||
                                fx.isNegative()&& fy.isPositive()&& fa.isNegative()) {
                            Y1 = 5;
                            X2 = -5;
                        }

                        // y = const
                        Fraction x1 = f.getX(new Fraction(Y1));
                        if (!x1.isZero()) {
                            X1 = round((double)(x1.getNumerator() * 1.0 / x1.getDenomintor()));
                        }
                        
                        // x = const
                        Fraction y2 = f.getY(new Fraction(X2));
                        if (!y2.isZero()) {
                            Y2 = round((double)(y2.getNumerator() * 1.0 / y2.getDenomintor()));
                        }
                    } else if (fx.isZero() && !fy.isZero()) {
                        X1 = -9;
                        X2 = 36;
                        Fraction y = f.getY(new Fraction(0));
                        if (!y.isZero()) {
                            Y1 = Y2 = round((double)(y.getNumerator() * 1.0 / y.getDenomintor()));
                        }
                    } else if (fy.isZero() && !fx.isZero()) {
                        Y1 = -10;
                        Y2 = 13;
                        Fraction x = f.getX(new Fraction(0));
                        if (!x.isZero()) {
                            X1 = X2 = round((double)(x.getNumerator() * 1.0 / x.getDenomintor()));
                        }
                    }

                    //calculate drawing coordinates
                    int coordX1 = (int) (X1 * scale) + zeroX;
                    int coordY1 = (int) (Y1 * -1 * scale) + zeroY;
                    int coordX2 = (int) (X2 * scale) + zeroX;
                    int coordY2 = (int) (Y2 * -1 * scale) + zeroY;
                    g.setColor(Color.red);
                    g.drawLine(coordX1, coordY1, coordX2, coordY2);
                    g.setColor(Color.GRAY);
                    //g.drawString(fx.toString() + "x + " + fy.toString() + "y <= " + fa.toString(), coordX1, coordY1);
                    g.drawString("("+(i+1)+")", coordX2-5, coordY2-5);
                    //System.out.println("("+X1+";"+Y1+") ("+X2+";"+Y2+")");
                }
            }
        }
    }
}
