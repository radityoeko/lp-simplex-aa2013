package it.unibz.inf.aa.ajrep.test;

import it.unibz.inf.aa.ajrep.parser.Parser;
import it.unibz.inf.aa.ajrep.ui.UI;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Test {

    public static void main(String[] args) {
	Parser parser = new Parser();
	
	try {
	    //parser.parseVariable("a:b:c:d", ":");
	    //System.out.println(parser.toString());
	} catch (Exception e) {
	    e.printStackTrace();
	}
        
        UI gui = new UI();
        gui.setVisible(true);
        
        gui.execute.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //call parser
            }
        });
	
    }

}
