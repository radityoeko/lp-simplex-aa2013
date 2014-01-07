package it.unibz.inf.aa.ajrep.test;

import it.unibz.inf.aa.ajrep.parser.Parser;

public class Test {

    public static void main(String[] args) {
	Parser parser = new Parser();
	
	try {
	    parser.parseVariable("a:b:c:d", ":");
	    System.out.println(parser.toString());
	} catch (Exception e) {
	    e.printStackTrace();
	}
	
    }

}
