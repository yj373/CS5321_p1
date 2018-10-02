package operators;


import java.util.LinkedList;

import data.DataBase;
import data.OrderedTable;
import data.Tuple;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.statement.select.PlainSelect;
import visitors.BasicExpressionVisitor;

//Deal with Select * From Table Where Table.A<...
public class SelectOperator extends Operator{
	
	private Expression expression;
	private LinkedList<OrderedTable> tableAliase;
	private int expressionType;

	@Override
	public Tuple getNextTuple() {
		// TODO Auto-generated method stub
		Tuple t = getChild().getFirst().getNextTuple();
		if(expression!=null) {
			while (t!=null) {
				BasicExpressionVisitor bev = new BasicExpressionVisitor(t);
				expression.accept(bev);
				boolean res = bev.getResult().getLast();
				if(res) break;
				t = getChild().getFirst().getNextTuple();
			}
						
		}
		return t;
	}

	@Override
	public void reset() {
		getChild().getFirst().reset();
		
	}
	
	//Constructors
	public SelectOperator(PlainSelect ps, Operator op) {
		String tableInfo = ps.getFromItem().toString();
		String[] aimTable = tableInfo.split("\\s+");
		if (aimTable.length<1) {
			return;
		}
		setChild(op);
		this.expression = ps.getWhere();
	}
	
//	public SelectOperator(String tName, String tAliase, Expression ex, Operator op, int exType) {
//		setChild(op);
//		this.expression = ex;
//		tableName = new LinkedList<String>();
//		tableName.add(tName);
//		tableAliase = new LinkedList<String>();
//		tableAliase.add(tAliase);
//		this.expressionType = exType;
//	}
	
	public SelectOperator(OrderedTable tAliase, Expression ex, Operator op, int exType) {
		setChild(op);
		this.expression = ex;
		this.tableAliase = new LinkedList<OrderedTable>();
		this.tableAliase.add(tAliase);
		this.expressionType = exType;
	}
	
	public SelectOperator(Expression ex, Operator op, int exType, LinkedList<OrderedTable> tAliase) {
		setChild(op);
		this.expression = ex;
		this.expressionType = exType;
		this.tableAliase = tAliase;
	}
	
	public SelectOperator() {
		
	}
	
	
    //Getters and Setters

	public Expression getExpression() {
		return expression;
	}

	public void setExpression(Expression expression) {
		this.expression = expression;
	}

	public LinkedList<OrderedTable> getTableAliase() {
		return tableAliase;
	}

	public void setTableAliase(OrderedTable tAliase) {
		LinkedList<OrderedTable> aliase = new LinkedList<OrderedTable>();
		aliase.add(tAliase);
		this.tableAliase = aliase;
	}

	public int getExpressionType() {
		return expressionType;
	}

	public void setExpressionType(int expressionType) {
		this.expressionType = expressionType;
	}
	
	
	
	

}
