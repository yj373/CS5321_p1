package operators;


import java.util.LinkedList;

import data.DataBase;
import data.Tuple;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.statement.select.PlainSelect;
import visitors.BasicExpressionVisitor;

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
		Tuple t = child.getNextTuple();
		if(expression!=null) {
			while (t!=null) {
				BasicExpressionVisitor bev = new BasicExpressionVisitor(t);
				expression.accept(bev);
				boolean res = bev.getResult().getLast();
				if(res) break;
				t = child.getNextTuple();
			}
						
		}
		return t;
	}

	@Override
	public void reset() {
		child.reset();
		
	}
	
	//Constructors
	public SelectOperator(PlainSelect ps, Operator op) {
		String table_info = ps.getFromItem().toString();
		String[] aim_table = table_info.split("\\s+");
		if (aim_table.length<1) {
			this.child = null;
			return;
		}
		this.child = op;
		this.expression = ps.getWhere();
		this.table_name = aim_table[0];
		this.table_aliase = aim_table[aim_table.length-1];
		this.attributes = DataBase.getInstance().getSchema(table_name);
	}
	
	
    //Getters and Setters
	public Operator getChild() {
		return child;
	}

	public void setChild(Operator child) {
		this.child = child;
	}

	public Expression getExpression() {
		return expression;
	}

	public void setExpression(Expression expression) {
		this.expression = expression;
	}

	public LinkedList<String> getAttributes() {
		return attributes;
	}

	public void setAttributes(LinkedList<String> attributes) {
		this.attributes = attributes;
	}

	public String getTable_name() {
		return table_name;
	}

	public void setTable_name(String table_name) {
		this.table_name = table_name;
	}

	public String getTable_aliase() {
		return table_aliase;
	}

	public void setTable_aliase(String table_aliase) {
		this.table_aliase = table_aliase;
	}
	
	

}
