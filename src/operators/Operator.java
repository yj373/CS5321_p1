package operators;

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
		
	}

}
