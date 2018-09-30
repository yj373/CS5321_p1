package visitors;

import java.util.ArrayList;
import java.util.LinkedList;

import data.Tuple;
import net.sf.jsqlparser.expression.AllComparisonExpression;
import net.sf.jsqlparser.expression.AnyComparisonExpression;
import net.sf.jsqlparser.expression.CaseExpression;
import net.sf.jsqlparser.expression.DateValue;
import net.sf.jsqlparser.expression.DoubleValue;
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
import net.sf.jsqlparser.expression.operators.relational.GreaterThan;
import net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.IsNullExpression;
import net.sf.jsqlparser.expression.operators.relational.LikeExpression;
import net.sf.jsqlparser.expression.operators.relational.Matches;
import net.sf.jsqlparser.expression.operators.relational.MinorThan;
import net.sf.jsqlparser.expression.operators.relational.MinorThanEquals;
import net.sf.jsqlparser.expression.operators.relational.NotEqualsTo;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.SubSelect;

//Deal with expression with single table
public class BasicExpressionVisitor implements ExpressionVisitor{
	
	private Tuple currentTuple;
	private LinkedList<Boolean> result;
	private LinkedList<Long> data;
	
	
	//Constructors
	public BasicExpressionVisitor(Tuple tuple) {
		this.currentTuple = tuple;
		result = new LinkedList<Boolean>();
		data = new LinkedList<Long>();
	}
	
	//Getters and setters
	public Tuple getCurrentTuple() {
		return currentTuple;
	}


	public void setCurrentTuple(Tuple currentTuple) {
		this.currentTuple = currentTuple;
	}


	public LinkedList<Boolean> getResult() {
		return result;
	}


	public void setResult(LinkedList<Boolean> result) {
		this.result = result;
	}
	
	public LinkedList<Long> getData(){
		return data;
	}
	
	public void setData (LinkedList<Long> data) {
		
	}
	
	//Implement AndExpression, Column, LongValue, EqualsTo, NotEqualsTo, GreaterThan
	//GreaterThanEquals, MinorThan, MinorThanEquals
	
	@Override
	public void visit(AndExpression and) {
		and.getLeftExpression().accept(this);
		and.getRightExpression().accept(this);
		boolean right_res = this.result.get(result.size()-1);
		boolean left_res = this.result.get(result.size()-2);
		boolean res = left_res && right_res;
		this.result.add(res);
		
	}
	
	@Override
	public void visit(Column column) {
		String columnName = column.getWholeColumnName();
		int data_ind = currentTuple.getSchema().get(columnName);
		this.data.add(currentTuple.getTupleData()[data_ind]);
		
	}

	@Override
	public void visit(LongValue longV) {
		this.data.add(longV.getValue());
		
	}
	
	@Override
	public void visit(EqualsTo equals) {
		equals.getLeftExpression().accept(this);
		equals.getRightExpression().accept(this);
		Long right_v = this.data.getLast();
		Long left_v = this.data.get(data.size()-2);
		this.result.add(left_v == right_v);
			
	}
	
	@Override
	public void visit(NotEqualsTo notEquals) {
		notEquals.getLeftExpression().accept(this);
		notEquals.getRightExpression().accept(this);
		Long right_v = this.data.getLast();
		Long left_v = this.data.get(data.size()-2);
		this.result.add(left_v == right_v);
		
	}
	
	@Override
	public void visit(GreaterThan greater) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void visit(GreaterThanEquals graterEquals) {
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
	public void visit(OrExpression arg0) {
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

	@Override
	public void visit(SubSelect arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
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
