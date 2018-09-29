package operators;

import java.io.File;
import java.util.LinkedList;

import data.Tuple;

public class ScanOperator extends Operator{
	
	private File table;
	private String table_name;
	private int read_pointer = 0;
	private LinkedList<String> table_aliase;
	private LinkedList<String> columns;

	@Override
	public Tuple getNextTuple() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}
	
	//Constructors
	public ScanOperator() {
		
	}
	public ScanOperator(String name) {
		
	}
	
	

}
