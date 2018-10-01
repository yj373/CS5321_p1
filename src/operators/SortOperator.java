package operators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import data.Tuple;
import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.statement.select.PlainSelect;

public class SortOperator extends Operator{
	
	private Operator child;
	private Map<String, Integer> schema;
	private PlainSelect plainSelect;
	private String[] sortSequence;
	private List<Tuple> dataCollection;
	private int currentIndex;
	
	
	public SortOperator(PlainSelect plainSelect, Operator op) {
		this.child = op;
		this.plainSelect = plainSelect;
		
		
		List<Tuple> dataCollection = new ArrayList<Tuple>();
		Tuple current = child.getNextTuple();
		
		/**get sorting sequence*/
		if (current != null) {
			schema = current.getSchema();
		}
		Map<Integer, String> pairs = new HashMap<Integer, String>();
		for (String key : schema.keySet()) {
			pairs.put(schema.get(key), key);
		}
		String[] sortSequence = new String[pairs.size()];
		for (int i=0; i<pairs.size(); i++) {
			sortSequence[i] = pairs.get(i);
		}
		
		/**read all tuples*/
		while (current!=null) {
			dataCollection.add(current);
			current = child.getNextTuple();
		}
		
		Collections.sort(dataCollection, new TupleComparator());
		child.reset();

	}

	@Override
	public Tuple getNextTuple() {
		Tuple current = null;
		currentIndex = 0;
		if (currentIndex < dataCollection.size()) {
			current = dataCollection.get(currentIndex);
			currentIndex++;
		}
		return current;
	}

	@Override
	public void reset() {
		currentIndex = 0;
		
	}
	
	class TupleComparator implements Comparator<Tuple> {
		
		/**get the required sorting sequence */
		List<OrderByElement> list = plainSelect.getOrderByElements();
		public int compare(Tuple o1, Tuple o2) {
			for (int i=0; i<list.size(); i++) {
				Integer col = o1.getSchema().get(list.get(i).toString());
				if (o1.getData()[col] < o2.getData()[col]) {
					return -1;
				} else if (o1.getData()[col] > o2.getData()[col]){
					return 1;
				} 
				
			}
			
			/**
			 * break ties in the rest of attributes
			 * we can directly iterate over every element
			 * because we have checked all required attributes, which means
			 * the previous comparing results must be zero
			 */
			for (int i=0; i< sortSequence.length; i++) {
				Integer col = o1.getSchema().get(list.get(i).toString());
				if (o1.getData()[col] < o2.getData()[col]) {
					return -1;
				} else if (o1.getData()[col] > o2.getData()[col]){
					return 1;
				} 
			}
			
			return 0;
		}
	}

}
