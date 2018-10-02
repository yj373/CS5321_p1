package operators;

import java.util.LinkedList;

import data.Tuple;
import net.sf.jsqlparser.statement.select.PlainSelect;

public class DuplicateEliminationOperator extends Operator{

	private Tuple prevTuple;
	boolean workState; 


	@Override
	public Tuple getNextTuple() {
		Operator child = super.getChild().get(0);
		Tuple t = child.getNextTuple();

		if(workState) {
			if (prevTuple != null) {
				while (t!= null && isEqual(t, prevTuple)) {
					t = child.getNextTuple();
				}
			}
		}
		prevTuple = t;

		return t;
	}

	@Override
	public void reset() {
		super.getChild().get(0).reset();

	}

	//Constructors
	public DuplicateEliminationOperator() {

	}
	public DuplicateEliminationOperator(PlainSelect ps, Operator op) {
		if (ps.getDistinct()!=null) {
			workState = true;
		}else {
			workState = false;
		}
		LinkedList<Operator> newChild = new LinkedList<Operator>();
		newChild.add(op);
		super.setChild(newChild);
	}
	//Getters and Setters


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
