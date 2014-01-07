package it.unibz.inf.aa.ajrep.parser;

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
    HashMap<String, Integer> varMapper = new HashMap<String, Integer>();

    /**
     * Used to obtain variable name, given its index.
     */
    ArrayList<String> varNameList = new ArrayList<String>();

    /**
     * Used to obtain variable value, given its index.
     */
    ArrayList<Double> varValueList = new ArrayList<Double>();

    ArrayList<Double> objectiveF;

    ArrayList<ArrayList<Double>> cMatrix = new ArrayList<ArrayList<Double>>();

    boolean maximize;

    int varCount;

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
	sb.append("Constraints: " + cMatrix.toString());
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
	Double[] d = new Double[varCount];
	Arrays.fill(d, 0.0);

	f = f.replaceAll("\\s", ""); // remove all whitespaces first

	Pattern pWholeEq = Pattern.compile("-?[0-9]*[a-zA-Z]+[0-9]*");
	Matcher mWholeEq = pWholeEq.matcher(f);

	while (mWholeEq.find()) {
	    /*
	     * for each pair of coefficient and variable, we extract the
	     * coefficient as a double and variable as a string using the simple
	     * inner class <code>SimpleExpression</code>
	     */
	    SimpleExpression se = new SimpleExpression(mWholeEq.group(),
		    "objective function");

	    d[varMapper.get(se.varName)] = se.coefficient;
	}

	objectiveF = new ArrayList<Double>(Arrays.asList(d));
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
	    Double[] d = new Double[varCount + 1];
	    Arrays.fill(d, 0.0);

	    Pattern pWholeEq = Pattern.compile("-?[0-9]*[a-zA-Z]+[0-9]*");
	    Matcher mWholeEq = pWholeEq.matcher(s);

	    while (mWholeEq.find()) {
		/*
		 * for each pair of coefficient and variable, we extract the
		 * coefficient as a double and variable as a string using the
		 * simple inner class <code>SimpleExpression</code>
		 */
		SimpleExpression se = new SimpleExpression(mWholeEq.group(),
			"objective function");

		d[varMapper.get(se.varName)] = se.coefficient;
	    }

	    pWholeEq = Pattern.compile("[><]?=[0-9]+");
	    mWholeEq = pWholeEq.matcher(s);
	    mWholeEq.find();
	    String t = mWholeEq.group();
	    pWholeEq = Pattern.compile("[0-9]+");
	    mWholeEq = pWholeEq.matcher(t);
	    mWholeEq.find();
	    String t2 = mWholeEq.group();
	    d[varCount] = Double.parseDouble(t2);
	    if (!t.contains(">")) {
		cMatrix.add(new ArrayList<Double>(Arrays.asList(d)));
	    }
	    if (!t.contains("<")) {
		for (int i = 0; i < varCount + 1; i++) {
		    d[i] = -d[i];
		}
		cMatrix.add(new ArrayList<Double>(Arrays.asList(d)));
	    }
	}
    }

    /**
     * Correspond to a pair of coefficient and variable. e.g: 5x, -10y, 101var5
     * 
     * @author Radityo
     * 
     */
    private class SimpleExpression {
	double coefficient;
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
			+ parentExp + "is undeclared");
	    }
	    this.varName = varName;

	    pEachVar = Pattern.compile("-?[0-9]*");
	    mEachVar = pEachVar.matcher(exp);
	    mEachVar.find();
	    String coefficient = mEachVar.group();

	    if (coefficient.equals(""))
		this.coefficient = 1.0;
	    else if (coefficient.equals("-"))
		this.coefficient = -1.0;
	    else
		this.coefficient = Double.parseDouble(coefficient);
	}
    }

}
