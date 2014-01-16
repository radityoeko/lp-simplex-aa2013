package it.unibz.inf.aa.ajrep.parser;

import it.unibz.inf.aa.ajrep.core.Fraction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class represents the parser of the program. It defines the methods to
 * read the input from the user that is passed from <code>UI</code> and parse it
 * to be later processed accordingly by the <code>Core</code>.
 * 
 * @author Radityo
 * @version 1.0
 * 
 */
public class Parser {

	/*
	 * Each variable has 3 attributes: (1) Name, given by the input. (2) Index;
	 * each variable is associated with a unique index in integer. (3) Value.
	 */

	/**
	 * Map variable name to its index.
	 */
	public HashMap<String, Integer> varMapper = new HashMap<String, Integer>();

	/**
	 * Used to obtain variable name, given its index.
	 */
	public ArrayList<String> varNameList = new ArrayList<String>();

	/**
	 * Used to obtain variable value, given its index.
	 */
	public ArrayList<Fraction> varValueList = new ArrayList<Fraction>();

	public ArrayList<Fraction> objectiveF;

	public ArrayList<ArrayList<Fraction>> cMatrix = new ArrayList<ArrayList<Fraction>>();
	
	public ArrayList<ArrayList<Fraction>> negativeTrivial = new ArrayList<ArrayList<Fraction>>();
	
	public ArrayList<ArrayList<Fraction>> positiveTrivial = new ArrayList<ArrayList<Fraction>>();
	
	public ArrayList<ArrayList<Fraction>> allTrivial = new ArrayList<ArrayList<Fraction>>();
	
	public ArrayList<Integer> indexOfPositiveTrivial = new ArrayList<Integer>();
	
	public ArrayList<Integer[]> indexOfNonZeroTrivial = new ArrayList<Integer[]>();
	
//	public ArrayList<Fraction> valueOfNonZeroTrivial = new ArrayList<Fraction>();
	
	public ArrayList<ArrayList<Fraction>> allConstraintMatrix  = new ArrayList<ArrayList<Fraction>>();

	public boolean maximize = false;

	public int varCount;

	/**
	 * Parse variable from an input string. The variables are separated by
	 * commas by default.
	 * 
	 * @param input
	 *            the input string containing variables separated by commas
	 * @throws IllegalVariableNameException
	 *             when the input string contains variable with illegal name.
	 */
	public void parseVariable(String input) throws IllegalVariableNameException {
		parseVariable(input, ",");
	}

	/**
	 * Parse variable from an input string. The variables are separated by a
	 * special delimiter.
	 * 
	 * Each variable is read and stored in the <code>varNameList</code>. Each
	 * variable is then stored in the mapper <code>varMapper</code> in order to
	 * obtain variable from <code>varNameList</code> based on its index.
	 * 
	 * @param input
	 *            the input string containing variables separated by spaces
	 * @param delimiter
	 *            the delimiter string
	 * @throws IllegalVariableNameException
	 *             when the input string contains variable with illegal name.
	 */
	private void parseVariable(String input, String delimiter)
			throws IllegalVariableNameException {
		input = input.replaceAll("\\s", ""); // remove all whitespaces first
		StringTokenizer st = new StringTokenizer(input, delimiter);
		
		int varCounter = 0; // counter represents index
		while (st.hasMoreTokens()) {
			String s = st.nextToken();
			if (!isAValidVarName(s))
				throw new IllegalVariableNameException(s
						+ " is not a valid variable name!");
			varNameList.add(s);
			varMapper.put(s, varCounter);
			varCounter++;
		}

		varCount = varCounter;
	}

	/**
	 * Check whether a variable name is valid
	 * 
	 * @param v
	 *            String containing the variable name.
	 * @return a boolean representing the wanted result.
	 */
	private boolean isAValidVarName(String v) {
		return v.matches("[a-zA-Z]+[0-9]*");
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Variables: " + varNameList.toString() + "\n");
		sb.append("Objective function: " + objectiveF.toString() + "\n");
		sb.append("All Constraints: " + allConstraintMatrix.toString());
		return sb.toString();
	}

	/**
	 * This method parses objective function into
	 * <code>ArrayList objectiveF</code> of the coefficient of each variables
	 * appearing in the objective function. The coefficient is sorted in
	 * <code>objectiveF</code> based on the index of its corresponding variable.
	 * The variables not appearing in the objective function have their
	 * coefficient assigned to 0.
	 * 
	 * @param f
	 *            the objective function as a string
	 * @throws NullPointerException
	 *             when there is undeclared variable in the formula
	 */
	public void parseObjectiveFunction(String f) throws NullPointerException {
		Fraction[] d = new Fraction[varCount+1];
		Arrays.fill(d, new Fraction(0,1));
		
		f = f.replaceAll("\\s", ""); // remove all whitespaces first

		Pattern pWholeEq = Pattern.compile("-?([0-9]+(\\.[0-9]+)?)*[a-zA-Z]+[0-9]*");
		Matcher mWholeEq = pWholeEq.matcher(f);

		while (mWholeEq.find()) {
			/*
			 * for each pair of coefficient and variable, we extract the
			 * coefficient as a Fraction and variable as a string using the
			 * simple inner class <code>SimpleExpression</code>
			 */
			SimpleExpression se = new SimpleExpression(mWholeEq.group(),
					"objective function");

			d[varMapper.get(se.varName)] = se.coefficient;
		}

		objectiveF = new ArrayList<Fraction>(Arrays.asList(d));
	}

	/**
	 * See documentation on
	 * <code>parseConstraints(String c, String delim)</code> This is an
	 * overloaded function, with <code>delim</code> is set to ","
	 * 
	 * @param c
	 * @throws NullPointerException
	 */
	public void parseConstraints(String c) throws NullPointerException {
		parseConstraints(c, ",");
	}

	/**
	 * This method parse the constraint expressions into two-dimensional
	 * <code>ArrayList</code>
	 * 
	 * @param c
	 *            String containing the whole constraints, separated by
	 *            delimiter <code>delim</code>
	 * @param delim
	 *            the delimiter in String format
	 * @throws NullPointerException
	 *             is thrown when there is an undeclared variable
	 */
	private void parseConstraints(String c, String delim)
			throws NullPointerException {
		c = c.replaceAll("\\s", "");

		StringTokenizer st = new StringTokenizer(c, delim);

		while (st.hasMoreTokens()) {
			String s = st.nextToken();
			Fraction[] d = new Fraction[varCount + 1];
			Arrays.fill(d, new Fraction(0,1));

//			System.out.println((new ArrayList<Fraction>(Arrays.asList(d))).toString());
			Pattern pWholeEq = Pattern.compile("-?([0-9]+(\\.[0-9]+)?)*[a-zA-Z]+[0-9]*");
			Matcher mWholeEq = pWholeEq.matcher(s);

			ArrayList<Integer> varAppearing = new ArrayList<Integer>();
			while (mWholeEq.find()) {
				/*
				 * for each pair of coefficient and variable, we extract the
				 * coefficient as a Fraction and variable as a string using the
				 * simple inner class <code>SimpleExpression</code>
				 */
				SimpleExpression se = new SimpleExpression(mWholeEq.group(),
						"objective function");

				d[varMapper.get(se.varName)] = se.coefficient;
				varAppearing.add(varMapper.get(se.varName));
			}

			pWholeEq = Pattern.compile("[><]?=-?([0-9]+(\\.[0-9]+)?)+");
			mWholeEq = pWholeEq.matcher(s);
			mWholeEq.find();
			String t = mWholeEq.group();
			pWholeEq = Pattern.compile("-?([0-9]+(\\.[0-9]+)?)+");
			mWholeEq = pWholeEq.matcher(t);
			mWholeEq.find();
			String t2 = mWholeEq.group();
			d[varCount] = new Fraction(Double.parseDouble(t2));
			if (!t.contains(">")) {
//				System.out.println(" <= " + (new ArrayList<Fraction>(Arrays.asList(d))).toString());
				cMatrix.add(new ArrayList<Fraction>(Arrays.asList(d)));
				
			}
			if (!t.contains("<")) {
				for (int i = 0; i < varCount + 1; i++) {
					d[i] = d[i].mul(Fraction.MIN);
				}
				if(varAppearing.size() > 1) {
//					System.out.println(" >= " + (new ArrayList<Fraction>(Arrays.asList(d))).toString());
					cMatrix.add(new ArrayList<Fraction>(Arrays.asList(d)));
				}
				else {
					
					if(objectiveF.get(varAppearing.get(0)).isPositive()) {
						positiveTrivial.add(new ArrayList<Fraction>(Arrays.asList(d)));
						indexOfPositiveTrivial.add(varAppearing.get(0));
						if(!d[varCount].isZero()) {
							indexOfNonZeroTrivial.add(new Integer[]{varAppearing.get(0),0,positiveTrivial.size()-1});
//							valueOfNonZeroTrivial.add(d[varCount].mul(Fraction.MIN));
						}
					}
					else {
						negativeTrivial.add(new ArrayList<Fraction>(Arrays.asList(d)));
						if (!d[varCount].isZero()) {
							indexOfNonZeroTrivial.add(new Integer[] {
									varAppearing.get(0), 1,
									negativeTrivial.size() - 1 });
//							valueOfNonZeroTrivial.add(d[varCount]);
						}
					}
				}
			}
		}
		
		allConstraintMatrix.addAll(cMatrix);
		allConstraintMatrix.addAll(negativeTrivial);
		allConstraintMatrix.addAll(positiveTrivial);
		
	}

	/**
	 * Correspond to a pair of coefficient and variable. e.g: 5x, -10y, 101var5
	 * 
	 * @author Radityo
	 * 
	 */
	private class SimpleExpression {
		Fraction coefficient;
		String varName;

		/**
		 * This constructor assign the coefficient and variable name extracted
		 * from a given expression in string format.
		 * 
		 * 
		 * @param exp
		 *            expression in string format
		 * @param parentExp
		 *            where the expression is contained (objective function or
		 *            constraint). This is used for exception reporting.
		 */
		SimpleExpression(String exp, String parentExp)
				throws NullPointerException {

			Pattern pEachVar = Pattern.compile("[a-zA-Z]+[0-9]*");
			Matcher mEachVar = pEachVar.matcher(exp);

			mEachVar.find();
			String varName = mEachVar.group();
			if (varMapper.get(varName) == null) {
				throw new NullPointerException("Variable " + varName + " in "
						+ parentExp + " is undeclared");
			}
			this.varName = varName;

			pEachVar = Pattern.compile("-?([0-9]+(\\.[0-9]+)?)*");
			mEachVar = pEachVar.matcher(exp);
			mEachVar.find();
			String coefficient = mEachVar.group();

			if (coefficient.equals(""))
				this.coefficient = new Fraction(1,1);
			else if (coefficient.equals("-"))
				this.coefficient = new Fraction(-1,1);
			else
				this.coefficient = new Fraction(Double.parseDouble(coefficient));
		}
	}

}
