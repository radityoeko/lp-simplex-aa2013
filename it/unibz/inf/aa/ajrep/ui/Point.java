package it.unibz.inf.aa.ajrep.ui;

import it.unibz.inf.aa.ajrep.core.Fraction;
import java.awt.geom.Ellipse2D;

/**
 *
 * @author Andrius
 */
public class Point extends Ellipse2D.Double {

    public Fraction x, y;

    public Point(double x, double y, double w, double h, Fraction[] point) {
        super(x, y, w, h);

        this.x = point[0];
        this.y = point[1];
    }
}
