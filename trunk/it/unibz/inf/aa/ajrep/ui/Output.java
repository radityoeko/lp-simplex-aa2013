package it.unibz.inf.aa.ajrep.ui;

import it.unibz.inf.aa.ajrep.core.Fraction;
import it.unibz.inf.aa.ajrep.core.FractionCalculator;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

/**
 *
 * @author Andrius
 */
public class Output extends JFrame {
    private FractionCalculator calculator;
    
    public Output(int varCount, ArrayList<Fraction> objectiveF, ArrayList<ArrayList<Fraction>> cMatrix) {
        this.setSize(500, 600);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setTitle("AA Linear Programming");

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);

        DrawPane drawPane = new DrawPane(varCount, objectiveF, cMatrix);
        drawPane.setLayout(null);
        drawPane.setBounds(5, 5, 485, 300);
        mainPanel.add(drawPane);

        this.add(mainPanel);
        
        this.calculator = new FractionCalculator();
    }
    
    class Function {
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
            return fx+"x + "+fy+"y = "+fa;
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
        
        /**
         * plots function on coordinate system by given scale and zero x & y points.
         * 
         * @param g 
         * @param f function to plot
         * @param scale
         * @param i number of function
         * @param zeroX
         * @param zeroY 
         */
        private void plotFunction(Graphics g, Function f, int scale, int i, int zeroX, int zeroY) {
            double X1 = 0, Y1 = 0, X2 = 0, Y2 = 0;

            if (!f.fx.isZero() && !f.fy.isZero()) {
                //const point selecting
                if (f.fx.isPositive() && f.fy.isPositive() && f.fa.isPositive() ||
                        f.fx.isNegative() && f.fy.isNegative() && f.fa.isNegative()) {
                    Y1 = -5;
                    X2 = -5;
                } else if (f.fx.isNegative() && f.fy.isNegative() && f.fa.isPositive() ||
                        f.fx.isPositive() && f.fy.isPositive() && f.fa.isNegative() ||
                        f.fx.isPositive() && f.fy.isPositive() && f.fa.isZero() ||
                        f.fx.isNegative() && f.fy.isNegative() && f.fa.isZero()) {
                    Y1 = 5;
                    X2 = 5;
                } else if (f.fx.isNegative() && f.fy.isPositive() && f.fa.isPositive() ||
                        f.fx.isPositive() && f.fy.isNegative() && f.fa.isPositive() ||
                        f.fx.isNegative() && f.fy.isPositive() && f.fa.isZero() ||
                        f.fx.isPositive() && f.fy.isNegative() && f.fa.isZero()) {
                    Y1 = -5;
                    X2 = 5;
                } else if (f.fx.isPositive() && f.fy.isNegative() && f.fa.isNegative() ||
                        f.fx.isNegative() && f.fy.isPositive() && f.fa.isNegative()) {
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
            } else if (f.fx.isZero() && !f.fy.isZero()) {
                X1 = -9;
                X2 = 36;
                Fraction y = f.getY(new Fraction(0));
                if (!y.isZero()) {
                    Y1 = Y2 = round((double)(y.getNumerator() * 1.0 / y.getDenomintor()));
                }
            } else if (f.fy.isZero() && !f.fx.isZero()) {
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
            g.setColor(new Color(212, 156, 232));
            g.drawLine(coordX1, coordY1, coordX2, coordY2);
            g.setColor(Color.GRAY);
            g.drawString("("+(i+1)+")", coordX1, coordY1);
        }
        
        public Fraction[] findPoint(Function f1, Function f2) {
            Fraction[] point = new Fraction[2];
            
            Fraction nom = calculator.sub(calculator.mul(f1.fx,f2.fa), calculator.mul(f2.fx,f1.fa));
            Fraction den = calculator.sub(calculator.mul(f2.fy,f1.fx), calculator.mul(f1.fy,f2.fx));
            Fraction y = calculator.div(nom, den);
            Fraction x = calculator.div(calculator.sub(f1.fa, calculator.mul(f1.fy, y)), f1.fx);
            point[0] = x;
            point[1] = y;
            
            return point;
        }
        
        public boolean isInFeasibileRegion(Fraction[] point) {
            Fraction result;
            ArrayList<Fraction> function;
            
            for (int i = 0; i < this.cMatrix.size(); i++) {
                function = this.cMatrix.get(i);
                result = calculator.add(calculator.mul(function.get(0),point[0]), calculator.mul(function.get(1),point[1]));
                if (calculator.compare(result, function.get(2)) == 1) {
                    return false;
                }
            }
            return true;
        }
        
        private void paintPoints(Graphics g, int scale, int zeroX, int zeroY) {
            for (int i = 0; i < this.cMatrix.size(); i++){
                for (int j = 1; j < this.cMatrix.size() && i != j; j++) {
                    Function f1 = new Function(this.cMatrix.get(i).get(0), this.cMatrix.get(i).get(1), this.cMatrix.get(i).get(2));
                    Function f2 = new Function(this.cMatrix.get(j).get(0), this.cMatrix.get(j).get(1), this.cMatrix.get(j).get(2));
                    
                    Fraction[] point = this.findPoint(f1, f2);
                    if (this.isInFeasibileRegion(point)) {
                        //System.out.println("("+point[0] +", "+point[1]+")"); 
                        double X = round((double)(point[0].getNumerator() * 1.0 / point[0].getDenomintor()));
                        double Y = round((double)(point[1].getNumerator() * 1.0 / point[1].getDenomintor()));
                        int coordX = (int) (X * scale) + zeroX;
                        int coordY = (int) (Y * -1 * scale) + zeroY;
                        g.setColor(Color.RED);
                        g.fillOval(coordX-3, coordY-3, 6, 6);
                    }
                }
            }
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            this.setBackground(Color.WHITE);
            Border line = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
            this.setBorder(BorderFactory.createTitledBorder(line, "2D graphs"));
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
            final int scale = 10; //scale px = 1

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
                    
                    //initialize function
                    fx = function.get(0);
                    fy = function.get(1);
                    fa = function.get(2);
                    f = new Function(fx, fy, fa);
                    
                    //plot function
                    this.plotFunction(g, f, scale, i, zeroX, zeroY);
                }
            }
            
            this.paintPoints(g, scale, zeroX, zeroY);
        }
    }
}
