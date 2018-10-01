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
	private String tableName;
	private String tableAliase;

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
		String tableInfo = ps.getFromItem().toString();
		String[] aimTable = tableInfo.split("\\s+");
		if (aimTable.length<1) {
			this.child = null;
			return;
		}
		this.child = op;
		this.expression = ps.getWhere();
		this.tableName = aimTable[0];
		this.tableAliase = aimTable[aimTable.length-1];
	}
	
	public SelectOperator(String tableName, String tableAliase, Expression ex, Operator op) {
		this.child = op;
		this.expression = ex;
		this.tableName = tableName;
		this.tableAliase = tableAliase;
	}
	
	public SelectOperator(String tableAliase, Expression ex, Operator op) {
		this.child = op;
		this.expression = ex;
		this.tableName = tableAliase;
		this.tableAliase = tableAliase;
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

	public String getTableName() {
		return tableName;
	}

	public void setTable_name(String tableName) {
		this.tableName = tableName;
	}

	public String getTable_aliase() {
		return tableAliase;
	}

	public void setTableAliase(String tableAliase) {
		this.tableAliase = tableAliase;
	}
	
	

}
