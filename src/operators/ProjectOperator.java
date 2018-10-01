package operators;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import data.Tuple;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectItem;

public class ProjectOperator extends Operator{
	
	/**child operator of current operator*/
	private Operator childOp = null;
	/**store information of needed attributes*/
	private List<SelectItem> selectItems;
	/**return all attributes or return specific attributes*/
	private boolean allColumns = false;
	
	/** 
	 * Get corresponding columns information and
	 * initialize childOp.
	 * 
	 */
	public ProjectOperator(PlainSelect plainSelect, Operator op) {
		childOp = op;
		selectItems = plainSelect.getSelectItems();
		if (selectItems.get(0).toString() == "*") {
			allColumns = true;
		} 
		
	}

	
	/**
	 * Get the next tuple after projection
	 * 
	 * @return next tuple
	 */
	@Override
	public Tuple getNextTuple() {
		
		Tuple current = childOp.getNextTuple();
		if (current != null && !allColumns) {
			//Assume there must be corresponding columns
			//in the given tuple
			long[] data = new long[selectItems.size()];
			Map<String, Integer> schema = new HashMap<String, Integer>();
			int index = 0;
			
			for (SelectItem expre : selectItems) {
				String attributeName = expre.toString();
				Integer dataIndex = current.getSchema().get(attributeName);
				if (dataIndex!=null) {
					data[index] = current.getData()[dataIndex];
					schema.put(attributeName, index);
					index++;
				}	
			}
			
			current = new Tuple(data, schema);
		}
		return current;
	}

	/**
	 * Reset project operator is to reset its child operator
	 */
	@Override
	public void reset() {
		childOp.reset();
		
	}

}
