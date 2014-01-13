package it.unibz.inf.aa.ajrep.core;

import java.util.ArrayList;

/**
 * Represents matrix of fraction as 2-dimensional <code>ArrayList</code>
 * 
 * @author RadityoEko
 * 
 */
public class FractionMatrix {

	/**
	 * The matrix
	 */
	private ArrayList<FractionVector> matrix;

	/**
	 * Constructor, simply set the matrix to the given parameter
	 * 
	 * @param matrix
	 */
	public FractionMatrix(ArrayList<ArrayList<Fraction>> matrix) {
		this.matrix = new ArrayList<FractionVector>();
		for (ArrayList<Fraction> alf : matrix) {
			this.matrix.add(new FractionVector(alf));
		}
	}

	/**
	 * Default constructor, initialize <code>matrix</code> as empty
	 * <code>ArrayList</code>
	 */
	public FractionMatrix() {
		matrix = new ArrayList<FractionVector>();
	}

	public ArrayList<FractionVector> getMatrix() {
		return matrix;
	}

	public void setMatrix(ArrayList<FractionVector> matrix) {
		this.matrix = matrix;
	}

	public int getRow() {
		return matrix.isEmpty() ? -1 : matrix.size();
	}

	public int getColumn() {
		return matrix.isEmpty() ? -1 : matrix.get(0).getColumn();
	}

	/**
	 * Substitute one variable in the matrix with a given expression and update
	 * the whole matrix.
	 * 
	 * @param subIndex
	 *            the index of the variable wanted to be substituted
	 * @param s
	 *            the vector representing the expression of substitution
	 */
	public void substitute(int subIndex, FractionVector s) {

		if (s.getColumn() != this.getColumn()) {
			System.out
					.println("Can only substitute with a vector matrix having the same number of rows!");
			return;
		}

		for (FractionVector fv : matrix) {
			Fraction subIndexCoef = fv.get(subIndex);
			for (int i = 0; i < fv.getColumn() - 1; i++) {
				if (i != subIndex) {
					// System.out.println(fv.get(i) + " " + subIndexCoef + " "+
					// s.get(i));
					fv.set(i, fv.get(i).add(subIndexCoef.mul(s.get(i))));
				} else {
					fv.set(i, subIndexCoef.mul(s.get(i)));
				}
			}
			fv.set(fv.getColumn() - 1,
					fv.get(fv.getColumn() - 1).sub(
							subIndexCoef.mul(s.get(fv.getColumn() - 1))));
		}

	}

	public void removeLastConstraint() {
		removeConstraint(matrix.size() - 1);
	}

	public void removeConstraint(int index) {
		matrix.remove(index);
	}

	public void addConstraint(FractionVector fv) {
		matrix.add(fv);
	}

	public void addConstraint(int index, FractionVector element) {
		matrix.add(index, element);
	}

	public String toString() {
		return "FractionMatrix: " + matrix.toString();
	}

	public FractionVector get(int i) {
		return matrix.get(i);
	}

	public void removeAllConstraints() {
		matrix.clear();
	}
}
