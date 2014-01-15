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
                //core.executeSimplexSearch();
                out.setVisible(true);
            }
        });
	
    }

}
