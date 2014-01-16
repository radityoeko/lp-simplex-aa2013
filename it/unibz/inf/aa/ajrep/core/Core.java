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
	public FractionMatrix varAssignment;
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
		varAssignment = new FractionMatrix();
		for (int i = 0; i < varCount; i++) {
			FractionVector fv = new FractionVector();
			FractionVector fv2 = new FractionVector();
			for (int j = 0; j < varCount + 1; j++) {
				if (j != i) {
					fv.add(Fraction.ZERO);
					fv2.add(Fraction.ZERO);
				} else {
					fv.add(Fraction.MIN);
					fv2.add(Fraction.ONE);
				}
			}
			staticTrivial.addConstraint(fv);
			varAssignment.addConstraint(fv2);
		}
		
//		System.out.println(varAssignment);
	}

	/**
	 * This method execute the simplex algorithm.
	 */
	public String executeSimplexSearch() {

		preprocess();

		// int j = parser.cMatrix.size() - 1;
		while (!objfIsAllNegative() && constraintIsAllPositive()) {
			int i = parser.indexOfPositiveTrivial.size() - 1;
			int res = findTightPositive(parser.indexOfPositiveTrivial.get(i));

			if (res == -1) {
				System.out
						.println("This linear programming problem has unbounded solution.");
				return "This linear programming problem has unbounded solution.";
			}

			FractionVector subVector = createSubtitutionVector(
					parser.indexOfPositiveTrivial.get(i), res);

			cMatrix.substitute(parser.indexOfPositiveTrivial.get(i), subVector);
			objectiveF.substitute(parser.indexOfPositiveTrivial.get(i),
					subVector);
			varAssignment.substitute(parser.indexOfPositiveTrivial.get(i),
					subVector);

			System.out.println("/////////////");
			System.out.println("Variable: "
					+ parser.indexOfPositiveTrivial.get(i));
			System.out.println("Constraint: " + res);
			System.out.println("Subtitution vector: " + subVector);

			updateConstraints(res, subVector);

			System.out.println("Cmatrix: " + cMatrix);
			System.out.println("objectiveF: " + objectiveF);

			// j--;
		}

		if (!constraintIsAllPositive()) {
			System.out
					.println("This linear programming problem has no solution.");
			return "This linear programming problem has no solution.";
		}
		if (objfIsAllNegative()) {
			solution = objectiveF.get(0).getLastElement();
			
			solution = parser.maximize? solution.inverse() : solution;
			
			StringBuilder sb = new StringBuilder();
			for(int i = 0; i < varCount; i++) {
				sb.append(parser.varNameList.get(i) + ": " + varAssignment.get(i).get(varCount).inverse() + "<br/>");
			}
			
			System.out.println("Solution is " + solution+ "<br/>" + sb.toString());
			return "Solution is " + solution + "\n" + sb.toString();
			
		} else {
			System.out
					.println("This linear programming problem has unbounded solution.");
			return "This linear programming problem has unbounded solution.";
		}

	}

	private boolean constraintIsAllPositive() {
		for (int i = 0; i < cMatrix.getRow(); i++) {
			if (cMatrix.get(i).get(varCount).isNegative()) {
				boolean temp = true;
				for (int j = 0; j < varCount; j++) {
					temp = temp && !cMatrix.get(i).get(j).isNegative();
					if (temp == false)
						break;
				}
				if (temp)
					return false;
			}
		}

		return true;
	}

	/**
	 * This method preprocess the constraints before executing the main simplex
	 * algorithm The preprocessing includes: (1.) Dealing with the non-zero
	 * trivials. i.e. the constraints x > a with a != 0; We want to change these
	 * constraints into x' > 0, by substituting x = x' + a to all of the
	 * constraints and objective function. (2.) Dealing with MINIMIZE. Our
	 * algorithm is designed to work only for MAXIMIZE. We want to deal with
	 * MINIMIZE by inverting the objective functions.
	 */
	private void preprocess() {
		for (int i = 0; i < parser.indexOfNonZeroTrivial.size(); i++) {
			int varIndex = parser.indexOfNonZeroTrivial.get(i)[0];
			int posOrNeg = parser.indexOfNonZeroTrivial.get(i)[1];
			int trivialIndex = parser.indexOfNonZeroTrivial.get(i)[2];

			FractionVector fv = new FractionVector(
					posOrNeg == 0 ? positiveTrivial.get(trivialIndex)
							.getVector() : negativeTrivial.get(trivialIndex)
							.getVector());

			fv.set(varIndex, fv.get(varIndex).inverse());
			fv.set(varCount, fv.get(varCount).inverse());

			cMatrix.substitute(varIndex, fv);
			// System.out.println(varIndex + " " + fv);
			objectiveF.substitute(varIndex, fv);

			if (posOrNeg == 0) {
				positiveTrivial.get(trivialIndex).set(trivialIndex,
						Fraction.ZERO);
			} else {
				negativeTrivial.get(trivialIndex).set(trivialIndex,
						Fraction.ZERO);
			}

		}
		
		if(!parser.maximize) {
			for(int i=0; i<=varCount; i++) {
				objectiveF.get(0).set(i, objectiveF.get(0).get(i).inverse());
			}
			
			updateTrivials();
		}

		System.out.println("/////////////");
		System.out.println("Cmatrix: " + cMatrix);
		System.out.println("objectiveF: " + objectiveF);

	}

	/**
	 * Update the cMatrix, negativeTrivial, and positiveTrivial after each step
	 * of simplex.
	 * 
	 * @param cIndex
	 *            the used constraint
	 */
	private void updateConstraints(int cIndex, FractionVector fv) {
		cMatrix.removeConstraint(cIndex);
		

		for (int i = 0; i < fv.getColumn() - 1; i++) {
			fv.set(i, fv.get(i).inverse());
		}
		cMatrix.addConstraint(fv);
		
		updateTrivials();

	}

	private void updateTrivials() {
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

	/**
	 * Create substitution vector based on the variable that is wanted to be
	 * substituted using a certain constraint in which that variable is
	 * appearing.
	 * 
	 * @param varIndex
	 *            the index of variable wanted to be substituted.
	 * @param cIndex
	 *            the index of constraint in which the variable is appearing.
	 * @return the subtitution vector
	 */
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

		vector.add(cMatrix.get(cIndex).get(varCount)
				.div(cMatrix.get(cIndex).get(varIndex)));
		return new FractionVector(vector);
	}

	/**
	 * Check whether the objective function has negative coefficient on all its
	 * variables.
	 * 
	 * @return the expected boolean
	 */
	private boolean objfIsAllNegative() {
		for (int i = 0; i <= varCount; i++) {
			if (objectiveF.get(0).get(i).isPositive())
				return false;
		}

		return true;
	}

	/**
	 * Function that finds first tight constraint, which has positive selected
	 * variable.
	 * 
	 * @param varIndex
	 * @param constraintIndex
	 * @return index of constraint (if -1, then first positive not exists)
	 */
	public int findTightPositive(int varIndex) {
		Fraction max = new Fraction(Integer.MAX_VALUE, 1);
		int indexMax = -1;
		for (int i = cMatrix.getRow() - 1; i >= 0; i--) {
			ArrayList<Fraction> constraint = cMatrix.get(i).getVector();
			Fraction variable = constraint.get(varIndex);
			if (variable.isPositive()) {
				if (constraint.get(varCount).div(variable).lt(max)) {
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
