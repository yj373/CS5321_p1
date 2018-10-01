package operators;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import data.Dynamic_properties;
import data.Tuple;

public abstract class Operator {
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
		String output_path = Dynamic_properties.OUTPUT_PATH;
		new File (output_path).mkdirs();
		File file = new File(output_path+"dump.txt");
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

}
