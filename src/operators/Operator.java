package operators;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import data.Constants;
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
		String output_path = Constants.OUTPUT_PATH;
		File file = new File(output_path+"dump.txt");
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			Tuple tuple = getNextTuple();
			while (tuple!=null) {
				System.out.println(tuple.toString());
				bw.write(tuple.toString());
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
