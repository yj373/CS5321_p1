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
	private Map<String, LinkedList<Operator>> tableDirectory = new HashMap<String, LinkedList<Operator>>();
	private LinkedList<String> currentTable = new LinkedList<String>();
	
	public Operator getQueryPlan(Select select) {
		select.getSelectBody().accept(this);
		return rootOp;
	}

	public void visit(PlainSelect ps) {
		ps.getFromItem().accept(this);
		if (ps.getJoins()!=null) {
			for(Object j: ps.getJoins()) {
				String tableInfo = j.toString();
				ScanOperator scanOp = new ScanOperator(tableInfo);
				LinkedList<Operator> rootEnd = new LinkedList<Operator>();
				rootEnd.add(scanOp);
				rootEnd.add(scanOp);
				tableDirectory.put(scanOp.getTableAliase(), rootEnd);
 
			}
		}
		if (ps.getWhere() != null) {
			ps.getWhere().accept(this);
		}
		HashSet<Operator> operatorChecker = new HashSet<Operator>();
		for(String s : tableDirectory.keySet()) {
			Operator candiOp = tableDirectory.get(s).get(1);
			if (!operatorChecker.contains(candiOp)) {
				joinStack.addFirst(candiOp);;
				operatorChecker.add(candiOp);
			}
		}
		
		while (joinStack.size()>1) {
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
		LinkedList<Operator> rootEnd = new LinkedList<Operator>();
		rootEnd.add(scanOp);
		rootEnd.add(scanOp);
		tableDirectory.put(scanOp.getTableAliase(), rootEnd);		
		
	}
	
	@Override
	public void visit(Column column) {
		String tableAliase = column.getTable().getName();
		//String tableAliase = tableInfo;
		currentTable.add(tableAliase);
		
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
			String tableAliase = currentTable.getLast();
			currentTable.removeLast();
			addSelectOprator(tableAliase, equals);
		}else {
			String table1 = currentTable.getLast();
			currentTable.removeLast();
			String table2 = currentTable.getLast();
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
			String tableAliase = currentTable.getLast();
			currentTable.removeLast();
			addSelectOprator(tableAliase, greater);
		}else {
			String table1 = currentTable.getLast();
			currentTable.removeLast();
			String table2 = currentTable.getLast();
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
			String tableAliase = currentTable.getLast();
			currentTable.removeLast();
			addSelectOprator(tableAliase, greaterEquals);
		}else {
			String table1 = currentTable.getLast();
			currentTable.removeLast();
			String table2 = currentTable.getLast();
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
			String tableAliase = currentTable.getLast();
			currentTable.removeLast();
			addSelectOprator(tableAliase, minor);
		}else {
			String table1 = currentTable.getLast();
			currentTable.removeLast();
			String table2 = currentTable.getLast();
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
			String tableAliase = currentTable.getLast();
			currentTable.removeLast();
			addSelectOprator(tableAliase, minorEquals);
		}else {
			String table1 = currentTable.getLast();
			currentTable.removeLast();
			String table2 = currentTable.getLast();
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
			String tableAliase = currentTable.getLast();
			currentTable.removeLast();
			addSelectOprator(tableAliase, notEquals);
		}else {
			String rightTable = currentTable.getLast();
			currentTable.removeLast();
			String leftTable = currentTable.getLast();
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
	
	private void addSelectOprator(String tableAliase, Expression ex) {
		Operator root = tableDirectory.get(tableAliase).get(0);
		Operator end = tableDirectory.get(tableAliase).get(1);
		SelectOperator selectOp = new SelectOperator(tableAliase, ex, root);
		if (root.equals(end)) {
			LinkedList<Operator> newValue = tableDirectory.get(tableAliase);
			newValue.removeLast();
			newValue.addLast(selectOp);
			tableDirectory.put(tableAliase, newValue);
		}else {
			if (root.getParent() instanceof JoinOperator) {
				if (root.equals(root.getParent().getChild().get(0))) {
					updateChildParentState(tableAliase, root, selectOp, 0);
				}else {
					updateChildParentState(tableAliase, root, selectOp, 1);
				}
			}else {
				updateChildParentState(tableAliase, root, selectOp, 0);				
			}
		}
		
	}
	
	private void updateChildParentState(String tableAliase, Operator root, Operator newOp, int ind) {
		LinkedList<Operator> newChild = root.getParent().getChild();
		newChild.remove(ind);
		if (ind == 0) {
			newChild.addFirst(newOp);
		}else {
			newChild.add(newOp);
		}
		
		root.getParent().setChild(newChild);
		root.setParent(newOp);
		LinkedList<Operator> newValue = tableDirectory.get(tableAliase);
		newValue.removeFirst();
		newValue.addFirst(root);
		tableDirectory.put(tableAliase, newValue);
		
	}
	
	private void addJoinOperator(String table1, String table2, Expression ex) {
		Operator end1 = tableDirectory.get(table1).get(1);
		Operator end2 = tableDirectory.get(table2).get(1);
		JoinOperator joinOp = new JoinOperator(end1, end2);
		SelectOperator selectOp = new SelectOperator(ex, joinOp);
		
		LinkedList<Operator> newValue = tableDirectory.get(table1);
		newValue.removeLast();
		newValue.addLast(selectOp);
		tableDirectory.put(table1, newValue);
		tableDirectory.put(table2, newValue);
		
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
