package operators;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

import data.Dynamic_properties;
import data.Tuple;

public abstract class Operator {
	private Operator parent;
	private LinkedList<Operator> child;
	
	
	public LinkedList<Operator> getChild() {
		return child;
	}

	public void setChild(LinkedList<Operator> child) {
		this.child = child;
	}

	/*Return the next next tuple, if there are some available 
	 *output, otherwise, return null
	 * */
	public abstract Tuple getNextTuple();
	
	/*Reset the state of the operator, so that
	 * it will output from the beginning
	 * */
	public abstract void reset();
	
	/*Print out all the output tuple
	 * */
	public void dump() {
		reset();
		String output_path = Dynamic_properties.outputPath;
		new File (output_path).mkdirs();
		File file = new File(output_path + "/dump.txt");
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			Tuple tuple = getNextTuple();
			while (tuple != null) {
				tuple.printData();
				bw.write(tuple.getTupleData().toString() + '\n');
				tuple = getNextTuple();
			}
			bw.close();			
		}catch(IOException e) {
			e.printStackTrace();
			e.getMessage();
		}
		reset();
	}

	public Operator getParent() {
		return parent;
	}

	public void setParent(Operator parent) {
		this.parent = parent;
	}
	

}
