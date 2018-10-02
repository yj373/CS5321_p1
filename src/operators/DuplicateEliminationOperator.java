package operators;

import data.Tuple;

public class DuplicateEliminationOperator extends Operator{
	
	private Operator child;
	private Tuple prevTuple;
	

	@Override
	public Tuple getNextTuple() {
		Tuple t = child.getNextTuple();
		if (prevTuple != null) {
			while (t!= null && isEqual(t, prevTuple)) {
				t = child.getNextTuple();
			}
		} else {
			prevTuple = t;
		}
		return t;
	}

	@Override
	public void reset() {
		child.reset();
		
	}
	
	//Constructors
	public DuplicateEliminationOperator() {
		
	}
	public DuplicateEliminationOperator(Operator op) {
		this.child = op;
	}
	//Getters and Setters

	public Operator getChild() {
		return child;
	}

	public void setChild(Operator child) {
		this.child = child;
	}

	public Tuple getPrevTuple() {
		return prevTuple;
	}

	public void setPrevTuple(Tuple prevTuple) {
		this.prevTuple = prevTuple;
	}
	
	//Helper function
	public boolean isEqual(Tuple t1, Tuple t2) {
		if (t1.getSize()!=t2.getSize()) return false;
		for (int i=0; i<t1.getSize(); i++) {
			if (t1.getData()[i] != t2.getData()[i]) return false;
		}
		return true;
		
	}
	
	

}
