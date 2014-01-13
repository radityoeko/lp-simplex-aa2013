package it.unibz.inf.aa.ajrep.core;

import it.unibz.inf.aa.ajrep.parser.Parser;

import java.util.ArrayList;

public class Core {
	public Parser parser;
	Fraction solution = Fraction.ZERO;

	public FractionMatrix cMatrix;
	public FractionMatrix positiveTrivial;
	public FractionMatrix negativeTrivial;
	public FractionMatrix objectiveF;
	public FractionMatrix staticTrivial;
	public Integer varCount;

	public Core(Parser p) {
		parser = p;
		cMatrix = new FractionMatrix(p.cMatrix);
		positiveTrivial = new FractionMatrix(p.positiveTrivial);
		negativeTrivial = new FractionMatrix(p.negativeTrivial);
		ArrayList<ArrayList<Fraction>> of = new ArrayList<ArrayList<Fraction>>();
		of.add(p.objectiveF);
		objectiveF = new FractionMatrix(of);
		varCount = new Integer(p.varCount);

		staticTrivial = new FractionMatrix();
		for (int i = 0; i < varCount; i++) {
			FractionVector fv = new FractionVector();
			for (int j = 0; j < varCount + 1; j++) {
				if (j != i)
					fv.add(Fraction.ZERO);
				else
					fv.add(Fraction.MIN);
			}
			staticTrivial.addConstraint(fv);
		}
	}

	public void executeSimplexSearch() {

		System.out.println("Cmatrix: " + cMatrix);
		System.out.println("objectiveF: " + objectiveF);
		int j = parser.cMatrix.size() - 1;
		while (!isAllNegative() && j >= 0) {
			int i = parser.indexOfPositiveTrivial.size() - 1;
			int res = findTightPositive(parser.indexOfPositiveTrivial.get(i));

			if (res == -1) {
				System.out
						.println("This linear programming problem has unbounded solution.");
				return;
			}

			FractionVector subVector = createSubtitutionVector(
					parser.indexOfPositiveTrivial.get(i), res);

			cMatrix.substitute(parser.indexOfPositiveTrivial.get(i), subVector);
			objectiveF.substitute(parser.indexOfPositiveTrivial.get(i),
					subVector);

			System.out.println("Step: " + 1);
			System.out.println("Variable: "
					+ parser.indexOfPositiveTrivial.get(i));
			System.out.println("Constraint: " + res);
			System.out.println("Subtitution vector: " + subVector);


			
			updateConstraints(parser.indexOfPositiveTrivial.get(i), res);
			
			System.out.println("Cmatrix: " + cMatrix);
			System.out.println("objectiveF: " + objectiveF);
			System.out.println("/////////////");

			j--;
		}
		
		if(j>=0)
		{solution = objectiveF.get(0).getLastElement();
		System.out.println("Solution is " + solution.inverse().toDouble());}
		else {
			System.out
			.println("This linear programming problem has unbounded solution.");
		}
		
	}

	private void updateConstraints(Integer varIndex, int cIndex) {
		cMatrix.removeConstraint(cIndex);
		negativeTrivial.removeAllConstraints();
		positiveTrivial.removeAllConstraints();
		parser.indexOfPositiveTrivial.clear();

		for (int i = 0; i < varCount; i++) {
			if (objectiveF.get(0).get(i).isPositive()) {
				positiveTrivial.addConstraint(staticTrivial.get(i));
				parser.indexOfPositiveTrivial.add(i);
			} else {
				negativeTrivial.addConstraint(staticTrivial.get(i));
			}
		}

	}

	public FractionVector createSubtitutionVector(int varIndex, int cIndex) {
		ArrayList<Fraction> vector = new ArrayList<Fraction>();
		for (int i = 0; i < varCount; i++) {
			if (i != varIndex) {
				vector.add(Fraction.MIN.mul(cMatrix.get(cIndex).get(i)).div(
						cMatrix.get(cIndex).get(varIndex)));
			} else {
				vector.add(Fraction.MIN.div(cMatrix.get(cIndex).get(varIndex)));
			}
		}

		// System.out.println(cMatrix.get(cIndex).get(varCount) + " "
		// + cMatrix.get(cIndex).get(varIndex));
		vector.add(cMatrix.get(cIndex).get(varCount)
				.div(cMatrix.get(cIndex).get(varIndex)));
		return new FractionVector(vector);
	}

	private boolean isAllNegative() {
		for (int i = 0; i <= varCount; i++) {
			if (objectiveF.get(0).get(i).isPositive())
				return false;
		}

		return true;
	}

	/**
	 * Functions finds first tight constraint, which has positive selected variable.
	 * 
	 * @param varIndex
	 * @param constraintIndex
	 * @return index of constraint (if -1, then first positive not exists)
	 */
	public int findTightPositive(int varIndex) {
		Fraction max = new Fraction(Integer.MAX_VALUE,1);
		int indexMax = -1;
		for (int i = cMatrix.getRow()-1; i >= 0; i--) {
//			System.out.println(cMatrix.getRow());
			ArrayList<Fraction> constraint = cMatrix.get(i).getVector();
			Fraction variable = constraint.get(varIndex);
//			System.out.println(constraint);
			if (variable.isPositive()) {
				if(constraint.get(varCount).div(variable).lt(max)) {
//					System.out.println(constraint.get(varCount) + " " + max);
					max = constraint.get(varCount).div(variable);
					indexMax = i;
				}
			}
		}

		return indexMax;
	}

	public String toString() {
		return cMatrix.toString() + "\n" + objectiveF.toString();
	}
}
