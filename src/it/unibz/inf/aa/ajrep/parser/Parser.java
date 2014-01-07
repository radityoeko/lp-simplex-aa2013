package it.unibz.inf.aa.ajrep.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

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

    ArrayList<Double> maxMinF = new ArrayList<Double>();

    ArrayList<ArrayList<Double>> cMatrix = new ArrayList<ArrayList<Double>>();

    boolean maximize;

    /**
     * Parse variable from an input string. The variables are separated by
     * spaces by default.
     * 
     * @param input
     *            the input string containing variables separated by spaces
     * @throws Exception
     *             when the input string is not valid.
     */
    public void parseVariable(String input) throws IllegalVariableNameException {
	parseVariable(input, " ");
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
     * @throws Exception
     *             when the input string is not valid.
     */
    public void parseVariable(String input, String delimiter)
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
	return varNameList.toString();
    }
}
