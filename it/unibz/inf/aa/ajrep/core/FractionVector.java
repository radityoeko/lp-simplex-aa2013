package it.unibz.inf.aa.ajrep.core;

import java.util.ArrayList;

public class FractionVector {
	
	private ArrayList<Fraction> vector = new ArrayList<Fraction>();
	
	public ArrayList<Fraction> getVector() {
		return vector;
	}

	public void setVector(ArrayList<Fraction> vector) {
		this.vector = vector;
	}

	public FractionVector( ArrayList<Fraction> vector) {
		this.vector = new ArrayList<Fraction>(vector);
	}
	
	public FractionVector() {
		// TODO Auto-generated constructor stub
	}

	public int getColumn() {
		return vector.size();
	}
	
	public Fraction get(int i) {
		return vector.get(i);
	}
	
	public Fraction getLastElement() {
		return vector.get(vector.size()-1);
	}
	
	public void set(int i, Fraction f) {
		vector.set(i, f);
	}
	
	public void add(Fraction f) {
		vector.add(f);
	}
	
	public String toString() {
		return vector.toString();
	}
}
