package it.unibz.inf.aa.ajrep.test;

import it.unibz.inf.aa.ajrep.core.Core;
import it.unibz.inf.aa.ajrep.core.Fraction;
import it.unibz.inf.aa.ajrep.core.FractionCalculator;
import it.unibz.inf.aa.ajrep.parser.IllegalVariableNameException;
import it.unibz.inf.aa.ajrep.parser.Parser;
import it.unibz.inf.aa.ajrep.ui.Output;
import it.unibz.inf.aa.ajrep.ui.UI;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Test {

    public static void main(String[] args) {
	try {
	    //parser.parseVariable("a:b:c:d", ":");
	    //System.out.println(parser.toString());
	} catch (Exception e) {
	    e.printStackTrace();
	}
        
        /*
        FractionCalculator calculator = new FractionCalculator();
        Fraction res = calculator.add(new Fraction(1,5), new Fraction(-0.2));
        //Fraction res = new Fraction(-1.6);
        System.out.println(res.getNumerator() + "/" + res.getDenomintor());
        */
        /*Core core = new Core();
        try {
            core.parser.parseVariable("a,b");
        } catch (IllegalVariableNameException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
        core.parser.parseObjectiveFunction("2a+5b");
        core.parser.parseConstraints("2a-b<=4, a+2b<=9, -a+b<=3, a>=0, b>= 0");
        int v = core.searchFirstPositive(1, 2);
        System.out.println(v);*/
        
        final UI gui = new UI();
        gui.setVisible(true);

        gui.execute.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final Parser parser = new Parser();
                try {
                    parser.parseVariable(gui.variablesTextArea.getText());
                    parser.parseObjectiveFunction(gui.functionTextArea.getText());
                    parser.parseConstraints(gui.constraintsTextArea.getText());
                    if (gui.choice.getSelectedItem() == "Maximize") {
                        parser.maximize = true;
                    } else {
                        parser.maximize = false;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                Core core = new Core(parser);
                gui.setVisible(false);
                System.out.println(core.parser.toString());
                Output out = gui.showOutput(parser.varCount, parser.objectiveF, parser.allConstraintMatrix);
                out.setVisible(true);
            }
        });
	
    }

}
