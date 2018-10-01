package visitors;

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
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.StatementVisitor;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.drop.Drop;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.replace.Replace;
import net.sf.jsqlparser.statement.select.FromItemVisitor;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectVisitor;
import net.sf.jsqlparser.statement.select.SubJoin;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.statement.select.Union;
import net.sf.jsqlparser.statement.truncate.Truncate;
import net.sf.jsqlparser.statement.update.Update;
import operators.Operator;

public class BasicVisitor implements SelectVisitor, FromItemVisitor, ExpressionVisitor {
	private Operator op=null;
	
	public Operator getQueryPlan(Select select) {
		select.getSelectBody().accept(this);
		return op;
	}

	public void visit(NullValue arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visit(Function arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visit(InverseExpression arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visit(JdbcParameter arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visit(DoubleValue arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visit(LongValue arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visit(DateValue arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visit(TimeValue arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visit(TimestampValue arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visit(Parenthesis arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visit(StringValue arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visit(Addition arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visit(Division arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visit(Multiplication arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visit(Subtraction arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visit(AndExpression arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visit(OrExpression arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visit(Between arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visit(EqualsTo arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visit(GreaterThan arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visit(GreaterThanEquals arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visit(InExpression arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visit(IsNullExpression arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visit(LikeExpression arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visit(MinorThan arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visit(MinorThanEquals arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visit(NotEqualsTo arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visit(Column arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visit(CaseExpression arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visit(WhenClause arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visit(ExistsExpression arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visit(AllComparisonExpression arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visit(AnyComparisonExpression arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visit(Concat arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visit(Matches arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visit(BitwiseAnd arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visit(BitwiseOr arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visit(BitwiseXor arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visit(Table arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visit(SubSelect arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visit(SubJoin arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visit(PlainSelect arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visit(Union arg0) {
		// TODO Auto-generated method stub
		
	}


}
