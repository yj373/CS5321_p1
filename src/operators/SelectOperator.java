package operators;


import java.util.LinkedList;

import data.DataBase;
import data.Tuple;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.statement.select.PlainSelect;

//Deal with Select * From Table Where Table.A<...
public class SelectOperator extends Operator{
	
	private Operator child;
	private Expression expression;
	private LinkedList<String> attributes;
	private String table_name;
	private String table_aliase;

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
	public SelectOperator(PlainSelect ps) {
		String table_info = ps.getFromItem().toString();
		String[] aim_table = table_info.split("\\s+");
		if (aim_table.length<1) {
			this.child = null;
			return;
		}
		this.child = new ScanOperator(table_info);
		this.expression = ps.getWhere();
		this.table_name = aim_table[0];
		this.table_aliase = aim_table[aim_table.length-1];
		this.attributes = DataBase.getInstance().getSchema(table_name);
	}

}
