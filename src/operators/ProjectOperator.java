package operators;

import java.util.List;
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
	public ProjectOperator(Operator op, PlainSelect plainSelect) {
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
			//build new tuple
			//根据attribute 到对应的map里面找数据
			for (SelectItem expre : selectItems) {
				String attributeName = expre.toString();
					
			}
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
