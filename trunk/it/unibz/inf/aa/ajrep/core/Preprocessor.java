package it.unibz.inf.aa.ajrep.core;

import it.unibz.inf.aa.ajrep.parser.Parser;

public class Preprocessor {
	Parser parser;
	int currentPositiveVar = 0;
	
	public Preprocessor(Parser p) {
		parser=p;
	}
	
	public void nextPositiveVar() {
		for(int i = currentPositiveVar + 1; i < parser.varCount; i++) {
			if(!parser.objectiveF.get(i).isNegative()) {
				currentPositiveVar = i;
				break;
			}
		}
				
	}
	
	
}
