package visitors;


import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

import net.sf.jsqlparser.expression.AllComparisonExpression;
import net.sf.jsqlparser.expression.AnyComparisonExpression;
import net.sf.jsqlparser.expression.CaseExpression;
import net.sf.jsqlparser.expression.DateValue;
import net.sf.jsqlparser.expression.DoubleValue;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.InverseExpression;
import net.sf.jsqlparser.expression.JdbcParameter;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.TimeValue;
import net.sf.jsqlparser.expression.TimestampValue;
import net.sf.jsqlparser.expression.WhenClause;
import net.sf.jsqlparser.expression.operators.arithmetic.Addition;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseAnd;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseOr;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseXor;
import net.sf.jsqlparser.expression.operators.arithmetic.Concat;
import net.sf.jsqlparser.expression.operators.arithmetic.Division;
import net.sf.jsqlparser.expression.operators.arithmetic.Multiplication;
import net.sf.jsqlparser.expression.operators.arithmetic.Subtraction;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.Between;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExistsExpression;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.GreaterThan;
import net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.IsNullExpression;
import net.sf.jsqlparser.expression.operators.relational.ItemsListVisitor;
import net.sf.jsqlparser.expression.operators.relational.LikeExpression;
import net.sf.jsqlparser.expression.operators.relational.Matches;
import net.sf.jsqlparser.expression.operators.relational.MinorThan;
import net.sf.jsqlparser.expression.operators.relational.MinorThanEquals;
import net.sf.jsqlparser.expression.operators.relational.NotEqualsTo;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.FromItemVisitor;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectVisitor;
import net.sf.jsqlparser.statement.select.SubJoin;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.statement.select.Union;
import operators.DuplicateEliminationOperator;
import operators.JoinOperator;
import operators.Operator;
import operators.ProjectOperator;
import operators.ScanOperator;
import operators.SelectOperator;
import operators.SortOperator;

public class BasicVisitor implements SelectVisitor, FromItemVisitor, ItemsListVisitor, ExpressionVisitor{
	
	
	private Operator rootOp = null;
	private Deque<Operator> joinStack = new LinkedList<Operator>() ;
	private Map<OrderedTable, Operator[]> tableDirectory = new HashMap<>();
	private LinkedList<OrderedTable> currentTable = new LinkedList<>();
	
	public Operator getQueryPlan(Select select) {
		select.getSelectBody().accept(this);
		return rootOp;
	}

	public void visit(PlainSelect ps) {
		ps.getFromItem().accept(this);
		if (ps.getJoins()!=null) {
			for(int i = 0; i < ps.getJoins().size(); i++) {
				Object j = ps.getJoins().get(i);
				String tableInfo = j.toString();
				ScanOperator scanOp = new ScanOperator(tableInfo);

				Operator[] rootEnd = new Operator[2];
				rootEnd[0] = scanOp;
				rootEnd[1] = scanOp;
				
				tableDirectory.put(new OrderedTable(scanOp.getTableAliase(), i+1), rootEnd);
 
			}
		}

		if (ps.getWhere() != null) {
			ps.getWhere().accept(this);
		}
		HashSet<Operator> operatorChecker = new HashSet<Operator>();
		for(OrderedTable s : tableDirectory.keySet()) {
			Operator candiOp = tableDirectory.get(s)[1];
			if (!operatorChecker.contains(candiOp)) {
				joinStack.addFirst(candiOp);;
				operatorChecker.add(candiOp);
			}
		}
		
		while (joinStack.size() > 1) {
			Operator right = joinStack.pop();
			Operator left = joinStack.pop();
			JoinOperator newJoin = new JoinOperator(left, right);
			joinStack.addFirst(newJoin);
		}
		Operator finalJoinOperator = joinStack.pop();
		ProjectOperator proOp = new ProjectOperator(ps, finalJoinOperator);
		SortOperator sortOp = new SortOperator(ps, proOp);
		DuplicateEliminationOperator dOp= new DuplicateEliminationOperator(ps, sortOp);
		rootOp = dOp;
	}
	
	public void visit(Table table) {
		ScanOperator scanOp = new ScanOperator(table.getWholeTableName(), table.getAlias());
		Operator[] rootEnd = new Operator[2];
		rootEnd[0] = scanOp;
		rootEnd[1] = scanOp;

		tableDirectory.put(new OrderedTable(scanOp.getTableAliase(), 0), rootEnd);		
		
	}
	
	@Override
	public void visit(Column column) {
		String[] tableNameIndices = column.getWholeColumnName().split("\\.");
		String tableAlias = tableNameIndices[0];
		OrderedTable tableNameIndex = null;
		for (OrderedTable s : tableDirectory.keySet()) {
			if (s.tableAlias.equals(tableAlias)) {
				tableNameIndex = s;
			}
		}	
		currentTable.add(tableNameIndex);
		
	}
	
	@Override
	public void visit(LongValue longV) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void visit(AndExpression and) {
		and.getLeftExpression().accept(this);
		and.getRightExpression().accept(this);
		
	}
	
	@Override
	public void visit(EqualsTo equals) {
		Expression left = equals.getLeftExpression();
		Expression right = equals.getRightExpression();
		int expressionType = getExpressionType(left, right);
		left.accept(this);
		right.accept(this);
		if (expressionType == 1) {
			OrderedTable tableNameIndex = currentTable.getLast();
			currentTable.removeLast();
			addSelectOprator(tableNameIndex, equals);
		}else {
			OrderedTable table2 = currentTable.getLast();
			currentTable.removeLast();
			OrderedTable table1 = currentTable.getLast();
			currentTable.removeLast();
			addJoinOperator(table1, table2, equals);
		}
				
	}

	@Override
	public void visit(GreaterThan greater) {
		Expression left = greater.getLeftExpression();
		Expression right = greater.getRightExpression();
		int expressionType = getExpressionType(left, right);
		left.accept(this);
		right.accept(this);
		if (expressionType == 1) {
			OrderedTable tableNameIndex = currentTable.getLast();
			currentTable.removeLast();
			addSelectOprator(tableNameIndex, greater);
		}else {
			OrderedTable table2 = currentTable.getLast();
			currentTable.removeLast();
			OrderedTable table1 = currentTable.getLast();
			currentTable.removeLast();
			addJoinOperator(table1, table2, greater);
		}
		
	}

	@Override
	public void visit(GreaterThanEquals greaterEquals) {
		Expression left = greaterEquals.getLeftExpression();
		Expression right = greaterEquals.getRightExpression();
		int expressionType = getExpressionType(left, right);
		left.accept(this);
		right.accept(this);
		if (expressionType == 1) {
			OrderedTable tableNameIndex = currentTable.getLast();
			currentTable.removeLast();
			addSelectOprator(tableNameIndex, greaterEquals);
		}else {
			OrderedTable table2 = currentTable.getLast();
			currentTable.removeLast();
			OrderedTable table1 = currentTable.getLast();
			currentTable.removeLast();
			addJoinOperator(table1, table2, greaterEquals);
		}
		
	}
	
	@Override
	public void visit(MinorThan minor) {
		Expression left = minor.getLeftExpression();
		Expression right = minor.getRightExpression();
		int expressionType = getExpressionType(left, right);
		left.accept(this);
		right.accept(this);
		if (expressionType == 1) {
			OrderedTable tableNameIndex = currentTable.getLast();
			currentTable.removeLast();
			addSelectOprator(tableNameIndex, minor);
		}else {
			OrderedTable table2 = currentTable.getLast();
			currentTable.removeLast();
			OrderedTable table1 = currentTable.getLast();
			currentTable.removeLast();
			addJoinOperator(table1, table2, minor);
	
		}
		
	}

	@Override
	public void visit(MinorThanEquals minorEquals) {
		Expression left = minorEquals.getLeftExpression();
		Expression right = minorEquals.getRightExpression();
		int expressionType = getExpressionType(left, right);
		left.accept(this);
		right.accept(this);
		if (expressionType == 1) {
            OrderedTable tableNameIndex = currentTable.getLast();
			currentTable.removeLast();
			addSelectOprator(tableNameIndex, minorEquals);
		}else {
			OrderedTable table2 = currentTable.getLast();
			currentTable.removeLast();
			OrderedTable table1 = currentTable.getLast();
			currentTable.removeLast();
			addJoinOperator(table1, table2, minorEquals);

		}
		
	}

	@Override
	public void visit(NotEqualsTo notEquals) {
		Expression left = notEquals.getLeftExpression();
		Expression right = notEquals.getRightExpression();
		int expressionType = getExpressionType(left, right);
		left.accept(this);
		right.accept(this);
		if (expressionType == 1) {
			OrderedTable tableNameIndex = currentTable.getLast();
			currentTable.removeLast();
			addSelectOprator(tableNameIndex, notEquals);
		}else {

			OrderedTable rightTable = currentTable.getLast();
			currentTable.removeLast();
			OrderedTable leftTable = currentTable.getLast();
			currentTable.removeLast();
			addJoinOperator(leftTable, rightTable, notEquals);
		}
		
	}
	
	//Classify the expressions
	//Type 1: don't need join, Type 2: need join
	private int getExpressionType(Expression e1, Expression e2) {
		if (e1 instanceof Column && e2 instanceof Column) return 2;
		return 1;
	}
	
	private void addSelectOprator(OrderedTable tableNameIndex, Expression ex) {
		Operator root = tableDirectory.get(tableNameIndex)[0];
		Operator end = tableDirectory.get(tableNameIndex)[1];
		SelectOperator selectOp = new SelectOperator(tableNameIndex.tableAlias, ex, root);
		
		if (root.equals(end)) {

			Operator[] newValue = tableDirectory.get(tableNameIndex);
			root.setParent(selectOp);
			newValue[1] = selectOp;			
			tableDirectory.put(tableNameIndex, newValue);
			
		} else {
			
			if (root.getParent() instanceof JoinOperator) {
				if (root.equals(root.getParent().getChild().get(0))) {
					updateChildParentState(root, selectOp, 0);
				}else {
					updateChildParentState(root, selectOp, 1);
				}
			}else {
				updateChildParentState(root, selectOp, 0);				
			}
		}
		
	}
	
	private void updateChildParentState(Operator root, Operator newOp, int ind) {
		LinkedList<Operator> newChild = root.getParent().getChild();
		newChild.remove(ind);
		if (ind == 0) {
			newChild.addFirst(newOp);
		} else {
			newChild.add(newOp);
		}
		
		root.getParent().setChild(newChild);
		newOp.setParent(root.getParent());
		root.setParent(newOp);
		
	}

	private void addJoinOperator(OrderedTable table1, OrderedTable table2, Expression ex) {
		if (table1.index > table2.index) {
			OrderedTable temp = table1;
			table1 = table2;
			table2 = temp;
		}
		Operator end1 = tableDirectory.get(table1)[1];
		Operator end2 = tableDirectory.get(table2)[1];

		JoinOperator joinOp = new JoinOperator(end1, end2);
		SelectOperator selectOp = new SelectOperator(ex, joinOp);
		joinOp.setParent(selectOp);
		
		Operator[] newValue1 = tableDirectory.get(table1);
		newValue1[1].setParent(joinOp);
		newValue1[1] = selectOp;
		
		Operator[] newValue2 = tableDirectory.get(table2);
		newValue2[1].setParent(joinOp);
		newValue2[1] = selectOp;
		
		
		tableDirectory.put(table1, newValue1);
		tableDirectory.put(table2, newValue2);
	}
	
	@Override
	public void visit(ExpressionList expressionList) {
		// TODO Auto-generated method stub
		
	}
	
	public void visit(Union arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visit(SubSelect arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visit(SubJoin arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(NullValue arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Function arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(InverseExpression arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(JdbcParameter arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(DoubleValue arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void visit(DateValue arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(TimeValue arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(TimestampValue arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Parenthesis arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(StringValue arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Addition arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Division arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Multiplication arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Subtraction arg0) {
		// TODO Auto-generated method stub
		
	}

	

	@Override
	public void visit(OrExpression arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Between arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void visit(InExpression arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(IsNullExpression arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(LikeExpression arg0) {
		// TODO Auto-generated method stub
		
	}

	
	public void visit(CaseExpression arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(WhenClause arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ExistsExpression arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(AllComparisonExpression arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(AnyComparisonExpression arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Concat arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Matches arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(BitwiseAnd arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(BitwiseOr arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(BitwiseXor arg0) {
		// TODO Auto-generated method stub
		
	}

}

class OrderedTable {
	String tableAlias;
    int index;
    
    OrderedTable(String str, int ind) {
    	tableAlias = str;
    	index = ind;
    }
    
    @Override
	public int hashCode(){
    	return tableAlias.hashCode();
    }
    
    @Override
    public boolean equals(Object o) {
    	return this.tableAlias.equals(o);
    }
}
