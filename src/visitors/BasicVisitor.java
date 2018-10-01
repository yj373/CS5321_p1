package visitors;


import java.util.Deque;
import java.util.HashMap;
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
import operators.Operator;
import operators.ScanOperator;

public class BasicVisitor implements SelectVisitor, FromItemVisitor, ItemsListVisitor, ExpressionVisitor{
	
	
	private Operator rootOp = null;
	private Deque<Operator> joinStack = new LinkedList<Operator>() ;
	private Map<String, Operator[]> tableDirectory = new HashMap<String, Operator[]>();
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
				ScanOperator[] rootEnd = new ScanOperator[2];
				rootEnd[0] = scanOp;
				rootEnd[1] = scanOp;
				tableDirectory.put(scanOp.getTableAliase(), rootEnd);
 
			}
		}
		ps.getWhere().accept(this);
		
	}
	
	public void visit(Table table) {
		ScanOperator scanOp = new ScanOperator(table.getWholeTableName(), table.getAlias());
		ScanOperator[] rootEnd = new ScanOperator[2];
		rootEnd[0] = scanOp;
		rootEnd[1] = scanOp;
		tableDirectory.put(scanOp.getTableAliase(), rootEnd);		
		
	}
	
	@Override
	public void visit(Column column) {
		// TODO Auto-generated method stub
		
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
		if (expressionType == 1) {
			
		}
		
		
	}

	@Override
	public void visit(GreaterThan greater) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(GreaterThanEquals greaterEquals) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void visit(MinorThan minor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(MinorThanEquals minorEquals) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(NotEqualsTo notEquals) {
		// TODO Auto-generated method stub
		
	}
	
	//Classify the expressions
	//Type 1: don't need join, Type 2: need join
	private int getExpressionType(Expression e1, Expression e2) {
		if (e1 instanceof Column && e2 instanceof Column) return 2;
		return 1;
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
