package it.unibz.inf.aa.ajrep.core;

import it.unibz.inf.aa.ajrep.parser.Parser;
import java.util.ArrayList;

public class Core {
    public Parser parser;
    
    public Core() {
        parser = new Parser();
    }
    
    /**
     * Functions finds first constraint, which has positive selected variable.
     * 
     * @param varIndex
     * @param constraintIndex
     * @return index of constraint (if -1, then first positive not exists)
     */
    public int searchFirstPositive(int varIndex, int constraintIndex) {
        for (int i = constraintIndex - 1; i >= 0; i--) {
            ArrayList<Fraction> constraint = parser.cMatrix.get(i);
            Fraction variable = constraint.get(varIndex);
            if (!variable.isNegative() && !variable.isZero()) {
                return i;
            }
        }
        
        return -1;
    }
}
